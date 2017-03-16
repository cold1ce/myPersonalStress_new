package com.fimrc.mysensornetwork.sensors.time.cell;

import android.content.Context;
import android.content.IntentFilter;

import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorModule;

/**
 * Created by Sven on 10.03.2017.
 */

public class CellModule extends SensorModule {

    private CellController controller;

    public CellModule(Context context, PersistenceLogger logger, SensorRecordStructure structure) {
        super(context, logger, structure);
        controller = new CellController(this);
    }

    @Override
    public boolean activateSensor() {
        String filterName = "CellSensor";
        context.registerReceiver(controller, new IntentFilter(filterName));
        controller.setAlarm(context, 60, filterName);
        return true;
    }

    @Override
    public boolean deactivateSensor() {
        context.unregisterReceiver(controller);
        controller.cancelAlarm(context);
        return false;
    }
}
