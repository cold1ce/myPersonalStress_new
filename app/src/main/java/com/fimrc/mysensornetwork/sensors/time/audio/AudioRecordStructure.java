package com.fimrc.mysensornetwork.sensors.time.audio;

import android.util.Pair;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.Datatypes;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 24.02.2017.
 */

public class AudioRecordStructure extends SensorRecordStructure {

    public AudioRecordStructure(){
        super(new HashMap<Integer, Pair<String, Datatypes>>(){{
            put(1, new Pair("frequency", Datatypes.DOUBLE));
            put(2, new Pair("amplitude", Datatypes.DOUBLE));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
