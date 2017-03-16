package com.fimrc.sensorfusionframework.sensors;

import android.content.Context;
import android.content.Intent;
import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import java.util.Date;

/**
 * Created by Sven on 15.03.2017.
 */

public abstract class SensorThreadController extends SensorController {
    public SensorThreadController(SensorModule module) {
        super(module);
    }
/*
    private SensorRecord record;

    public SensorThreadController(SensorModule module) {
        super(module);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        record = null;
        record = createSensorRecord();
        Runnable prepareSensorRecord = new Runnable(){
            @Override
            public void run() {
                doInBackground(record);
                //this.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        module.log(record);
                    }
                });
            }
        };
        //Thread backgroundThread = new Thread(prepareSensorRecord);
    }

    protected abstract void doInBackground(SensorRecord record);

    protected SensorRecord createSensorRecord(){
        Date date = new Date(System.currentTimeMillis());
        return new SensorRecord(module.getNextIndex(), date , structure);
    }
*/
}
