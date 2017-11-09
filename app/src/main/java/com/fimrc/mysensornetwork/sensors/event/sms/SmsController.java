package com.fimrc.mysensornetwork.sensors.event.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.sensors.SensorModule;
import com.fimrc.jdcf.sensors.event.SensorEventController;
import com.fimrc.jdcf.sensors.time.SensorTimeController;
import com.fimrc.mysensornetwork.sensors.event.screen.ScreenController;

import java.util.Date;

/**
 * Created by Sven on 29.05.2017.
 */

public class SmsController extends SensorEventController {

    public myBroadcastReceiver mBroadcastReceiver;

    /**
     * Creates a new SensorTimeController
     *
     * @param module SensorModule to be linked with the controller
     */
    public SmsController(SensorModule module) {
        super(module);
    }

    private class myBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SmsController.this.receive(intent);
        }
    }

    public void receive(Intent intent) {
        String action = intent.getAction();
        Date date = new Date(System.currentTimeMillis());
        SensorRecord record = new SensorRecord(module.getNextIndex(), date , structure);
        record.addData("sender", SensorDataType.STRING, "1234");
        record.addData("message", SensorDataType.STRING, "test");
        logSensorRecord(record);
    }

}
