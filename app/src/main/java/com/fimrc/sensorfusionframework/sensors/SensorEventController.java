package com.fimrc.sensorfusionframework.sensors;

import android.content.BroadcastReceiver;

import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

/**
 * Created by Sven on 02.03.2017.
 */

public abstract class SensorEventController extends BroadcastReceiver {

    protected SensorModule module;
    protected SensorRecordStructure structure;

    public SensorEventController(SensorModule module){
        this.module = module;
        structure = module.getStructure();
    }

}
