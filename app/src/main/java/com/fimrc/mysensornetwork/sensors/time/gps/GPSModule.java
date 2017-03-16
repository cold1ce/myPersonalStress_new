package com.fimrc.mysensornetwork.sensors.time.gps;

import android.content.Context;
import android.content.IntentFilter;

import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorModule;

/**
 * Created by Sven on 01.03.2017.
 */

public class GPSModule extends SensorModule {

    private GPSController controller;

    public GPSModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(context, logger, structure);
        controller = new GPSController(this, context);
    }

    @Override
    public boolean activate() {
        String filterName = "GPSSensor";
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
