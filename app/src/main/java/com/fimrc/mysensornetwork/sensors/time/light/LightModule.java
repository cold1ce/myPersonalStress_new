package com.fimrc.mysensornetwork.sensors.time.light;

import android.content.Context;
import android.content.IntentFilter;

import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorModule;

/**
 * Created by Sven on 02.03.2017.
 */

public class LightModule extends SensorModule {

    private LightController controller;

    public LightModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(context, logger, structure);
        controller = new LightController(this);
    }

    @Override
    public boolean activate() {
        String filterName = "LightSensor";
        context.registerReceiver(controller, new IntentFilter(filterName));
        controller.setAlarm(context, 60, filterName);
        return true;
    }

    @Override
    public boolean deactivate() {
        context.unregisterReceiver(controller);
        controller.cancelAlarm(context);
        return false;
    }

}
