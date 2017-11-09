package com.fimrc.mysensornetwork.sensors.time.cell;

import android.content.Context;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.time.SensorTimeModule;

/**
 * Created by Sven on 10.03.2017.
 */

public class CellModule extends SensorTimeModule {

    public CellModule(Context context, PersistenceLogger logger, SensorRecordStructure structure) {
        super(logger, structure);
        controller = new CellController(this, context);
    }

}
