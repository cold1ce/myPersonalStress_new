package com.fimrc.mysensornetwork.sensors.time.gps;

import android.content.Context;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.time.SensorTimeModule;

/**
 * Created by Sven on 01.03.2017.
 */

public class GPSModule extends SensorTimeModule {

    public GPSModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(logger, structure);
        controller = new GPSController(this, context);
    }

}
