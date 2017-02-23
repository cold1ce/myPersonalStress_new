package com.fimrc.mysensornetwork.sensors.screen;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorModule;

/**
 * Created by Sven on 22.02.2017.
 */

public class ScreenModule extends SensorModule {

    private ScreenController controller;
    private Context context;
    private PersistenceLogger logger;
    private SensorRecordStructure structure;

    public ScreenModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(logger, structure);
        this.context = context;
        this.logger = logger;
        this.structure = structure;
        controller = new ScreenController(this);
    }

    @Override
    public boolean activateSensor() {
        IntentFilter filterON = new IntentFilter(Intent.ACTION_SCREEN_ON);
        IntentFilter filterOFF = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(controller, filterON);
        context.registerReceiver(controller, filterOFF);
        return true;
    }

    @Override
    public boolean deactivateSensor() {
        context.unregisterReceiver(controller);
        return false;
    }
}
