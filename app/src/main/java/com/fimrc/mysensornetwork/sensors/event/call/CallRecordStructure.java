package com.fimrc.mysensornetwork.sensors.event.call;

import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 23.02.2017.
 */

public class CallRecordStructure extends SensorRecordStructure {

    public CallRecordStructure(){
        super(new HashMap<Integer, SensorDataField>(){{
            put(1, new SensorDataField("callee", SensorDataType.STRING));
            put(2, new SensorDataField("caller", SensorDataType.STRING));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
