package com.fimrc.mysensornetwork.sensors.time.cell;

import android.content.Context;
import android.content.IntentFilter;

import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorModule;
import com.fimrc.sensorfusionframework.sensors.SensorTimeModule;

/**
 * Created by Sven on 10.03.2017.
 */

public class CellModule extends SensorTimeModule {

    public CellModule(Context context, PersistenceLogger logger, SensorRecordStructure structure, String filterName) {
        super(context, logger, structure, filterName);
        controller = new CellController(this);
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public boolean deactivate() {
        return false;
    }
}
