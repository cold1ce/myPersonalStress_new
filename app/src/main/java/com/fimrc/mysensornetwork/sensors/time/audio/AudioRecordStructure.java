package com.fimrc.mysensornetwork.sensors.time.audio;


import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 24.02.2017.
 */

public class AudioRecordStructure extends SensorRecordStructure {

    public AudioRecordStructure(){
        super(new HashMap<Integer, SensorDataField>(){{
            put(1, new SensorDataField("frequency", SensorDataType.DOUBLE));
            put(2, new SensorDataField("amplitude", SensorDataType.DOUBLE));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
