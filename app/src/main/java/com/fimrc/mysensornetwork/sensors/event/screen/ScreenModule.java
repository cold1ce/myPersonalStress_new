package com.fimrc.mysensornetwork.sensors.event.screen;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.event.SensorEventModule;

/**
 * Created by Sven on 22.02.2017.
 */

public class ScreenModule extends SensorEventModule {

    private Context context;

    public ScreenModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(logger, structure);
        this.context = context;
        controller = new ScreenController(this);
    }

    @Override
    public boolean activateSensor() {
        Log.d("ScreenModule", "activateSensor");
        IntentFilter filterON = new IntentFilter(Intent.ACTION_SCREEN_ON);
        IntentFilter filterOFF = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(((ScreenController)controller).mBroadcastReceiver, filterON);
        context.registerReceiver(((ScreenController)controller).mBroadcastReceiver, filterOFF);
        return super.activateSensor();
    }

    @Override
    public boolean deactivateSensor() {
        context.unregisterReceiver(((ScreenController)controller).mBroadcastReceiver);
        return super.deactivateSensor();
    }
}
