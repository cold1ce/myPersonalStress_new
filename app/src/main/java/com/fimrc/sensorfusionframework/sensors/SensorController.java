package com.fimrc.sensorfusionframework.sensors;

import android.content.BroadcastReceiver;

import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

/**
 * Created by Sven on 15.03.2017.
 */

public abstract class SensorController extends BroadcastReceiver {

    protected SensorModule module;
    protected SensorRecordStructure structure;

    public SensorController(SensorModule module){
        this.module = module;
        structure = module.getStructure();
    }

}
