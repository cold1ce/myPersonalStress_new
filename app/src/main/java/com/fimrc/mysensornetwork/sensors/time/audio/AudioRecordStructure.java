package com.fimrc.mysensornetwork.sensors.time.audio;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Sven on 24.02.2017.
 */

public class AudioRecordStructure extends SensorRecordStructure {

    public AudioRecordStructure(){
        super(new ArrayList<String>(Arrays.asList(
                "frequency",
                "amplitude"
        )));
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
