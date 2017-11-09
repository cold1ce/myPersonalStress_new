package com.fimrc.mysensornetwork.sensors.time.light;

import android.content.Context;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.time.SensorTimeModule;

/**
 * Created by Sven on 02.03.2017.
 */

public class LightModule extends SensorTimeModule {

    public LightModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(logger, structure);
        controller = new LightController(this, context);
    }

}
