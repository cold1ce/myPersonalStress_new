package com.fimrc.mysensornetwork.sensors.event.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.sensors.event.SensorEventController;
import com.fimrc.mysensornetwork.sensors.event.call.CallController;
import com.fimrc.mysensornetwork.sensors.event.call.CallModule;

import java.util.Date;

/**
 * Created by Sven on 22.02.2017.
 */

public class ScreenController extends SensorEventController{

    public myBroadcastReceiver mBroadcastReceiver;

    public ScreenController(ScreenModule module){
        super(module);
        mBroadcastReceiver = new myBroadcastReceiver();
    }

    private class myBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ScreenController.this.receive(intent);
        }
    }

    public void receive(Intent intent) {
        String action = intent.getAction();
        Date date = new Date(System.currentTimeMillis());
        SensorRecord record = new SensorRecord(module.getNextIndex(), date , structure);
        if(action.equals(Intent.ACTION_SCREEN_ON)){
            record.addData("event", SensorDataType.STRING, "SCREEN ON");
        }else if(action.equals(Intent.ACTION_SCREEN_OFF)) {
            record.addData("event", SensorDataType.STRING, "SCREEN OFF");
        }
        logSensorRecord(record);
    }
}
