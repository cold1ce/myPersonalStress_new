package com.fimrc.mysensornetwork.sensors.event.screen;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorModule;

/**
 * Created by Sven on 22.02.2017.
 */

public class ScreenModule extends SensorModule {

    private ScreenController controller;

    public ScreenModule(Context context, PersistenceLogger logger, SensorRecordStructure structure, String filterName){
        super(context, logger, structure, filterName);
        controller = new ScreenController(this);
    }

    @Override
    public boolean activate() {
        Log.d("Sensor", "Screen activate");
        IntentFilter filterON = new IntentFilter(Intent.ACTION_SCREEN_ON);
        IntentFilter filterOFF = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(controller, filterON);
        context.registerReceiver(controller, filterOFF);
        return true;
    }

    @Override
    public boolean deactivate() {
        context.unregisterReceiver(controller);
        return false;
    }

}
