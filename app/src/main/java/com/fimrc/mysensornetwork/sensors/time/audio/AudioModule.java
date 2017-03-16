package com.fimrc.mysensornetwork.sensors.time.audio;

import android.content.Context;
import android.content.IntentFilter;
import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorModule;

/**
 * Created by Sven on 24.02.2017.
 */

public class AudioModule extends SensorModule {

    private AudioController controller;

    public AudioModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(context, logger, structure);
        controller = new AudioController(this);
    }

    @Override
    public boolean activate() {
        String filterName = "AudioSensor";
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
