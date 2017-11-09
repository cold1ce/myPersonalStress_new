package com.fimrc.mysensornetwork.sensors.event.sms;

import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 29.05.2017.
 */

public class SmsRecordStructure extends SensorRecordStructure {

    public SmsRecordStructure(){
        super(new HashMap<Integer, SensorDataField>(){{
            put(1, new SensorDataField("sender", SensorDataType.STRING));
            put(2, new SensorDataField("message", SensorDataType.STRING));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
