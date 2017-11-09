package com.fimrc.mysensornetwork.sensors.event.sms;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.event.SensorEventModule;
import com.fimrc.mysensornetwork.sensors.event.screen.ScreenController;

/**
 * Created by Sven on 29.05.2017.
 */

public class SmsModule extends SensorEventModule {

    private Context context;

    @Override
    public boolean activateSensor() {
        IntentFilter filterON = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        //IntentFilter filterOFF = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        context.registerReceiver(((ScreenController)controller).mBroadcastReceiver, filterON);
        //context.registerReceiver(((ScreenController)controller).mBroadcastReceiver, filterOFF);
        return super.activateSensor();
    }

    @Override
    public boolean deactivateSensor() {
        context.unregisterReceiver(((ScreenController)controller).mBroadcastReceiver);
        return super.deactivateSensor();
    }

    /**
     * Creates a new SensorEventModule
     *
     * @param logger    Implemented PersistenceLogger class where this Sensor will logSensorRecord
     * @param structure SensorRecordStructure which implements the structure for this Sensor
     */
    public SmsModule(Context context, PersistenceLogger logger, SensorRecordStructure structure) {
        super(logger, structure);
        controller = new SmsController(this);
        this.context = context;
    }
}
