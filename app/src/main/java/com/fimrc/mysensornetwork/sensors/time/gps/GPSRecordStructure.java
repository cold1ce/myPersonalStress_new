package com.fimrc.mysensornetwork.sensors.time.gps;


import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;

import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by Sven on 01.03.2017.
 */

public class GPSRecordStructure extends SensorRecordStructure {

    public GPSRecordStructure(){
        super(new HashMap<Integer, SensorDataField>(){{
            put(1, new SensorDataField("provider", SensorDataType.STRING));
            put(2, new SensorDataField("longitude", SensorDataType.DOUBLE));
            put(3, new SensorDataField("latitude", SensorDataType.DOUBLE));
            put(4, new SensorDataField("altitude", SensorDataType.DOUBLE));
            put(5, new SensorDataField("bearing", SensorDataType.DOUBLE));
            put(6, new SensorDataField("speed",  SensorDataType.DOUBLE));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
