package com.fimrc.mysensornetwork.sensors.time.audio;

import android.content.Context;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.time.SensorTimeModule;

/**
 * Created by Sven on 24.02.2017.
 */

public class AudioModule extends SensorTimeModule {

    public AudioModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(logger, structure);
        controller = new AudioController(this);
    }

}
