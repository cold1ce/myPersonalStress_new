package com.fimrc.mysensornetwork.sensors.time.audio;

import android.content.Context;
import android.content.IntentFilter;
import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorModule;

/**
 * Created by Sven on 24.02.2017.
 */

public class AudioModule extends SensorModule {

    private AudioController controller;
    private Context context;
    private DatabaseLogger logger;
    private AudioRecordStructure structure;

    public AudioModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(logger, structure);
        this.context = context;
        this.logger = (DatabaseLogger)logger;
        this.structure = (AudioRecordStructure)structure;
        controller = new AudioController(this);
    }

    @Override
    public boolean activateSensor() {
        String filterName = "AudioSensor";
        context.registerReceiver(controller, new IntentFilter(filterName));
        controller.setAlarm(context, 2, filterName);
        return true;
    }

    @Override
    public boolean deactivateSensor() {
        context.unregisterReceiver(controller);
        controller.cancelAlarm(context);
        return false;
    }

    public void printDatabase(){
        logger.print();
    }
}
