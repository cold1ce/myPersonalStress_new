package com.fimrc.sensorfusionframework.sensors;

import android.content.Context;
import android.content.IntentFilter;

import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

/**
 * Created by Sven on 21.03.2017.
 */

public abstract class SensorTimeModule extends SensorModule {

    private long timer;

    public SensorTimeModule(Context context, PersistenceLogger logger, SensorRecordStructure structure, String filterName) {
        super(context, logger, structure, filterName);
    }

    public boolean activateTimeSensor(long time) {
        context.registerReceiver(controller, new IntentFilter(filterName));
        this.setTimer(time);
        active = true;
        return this.activate();
    }

    public boolean deactivateTimeSensor(){
        context.unregisterReceiver(controller);
        this.cancelTimer();
        active = false;
        return this.deactivate();
    }

    public void setTimer(long time){
        if(!(SensorTimeController.class.isAssignableFrom(controller.getClass()))){
            throw new RuntimeException("Wrong Controller-Type");
        }
        if(time<60){
            throw new RuntimeException("Timer must be >60 Seconds");
        }
        this.timer = time;
        ((SensorTimeController)controller).cancelAlarm(context);
        ((SensorTimeController)controller).setAlarm(context, time, filterName);
    }

    public void cancelTimer(){
        if(!(controller.getClass() == SensorTimeController.class)){
            throw new RuntimeException("Wrong Controllertype");
        }
        this.timer = -1;
        ((SensorTimeController)controller).cancelAlarm(context);
    }

    public long getTimer(){ return timer; }
}
