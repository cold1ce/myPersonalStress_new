package com.fimrc.mysensornetwork.sensors.event.screen;


import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 22.02.2017.
 */

public class ScreenRecordStructure extends SensorRecordStructure {

    public ScreenRecordStructure(){
        super(new HashMap<Integer, SensorDataField>(){{
            put(1, new SensorDataField("event", SensorDataType.STRING));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
