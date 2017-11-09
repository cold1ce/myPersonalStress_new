package com.fimrc.mysensornetwork.sensors.time.light;

import android.util.Pair;

import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;

import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by Sven on 02.03.2017.
 */

public class LightRecordStructure extends SensorRecordStructure {

    public LightRecordStructure(){
        super(new HashMap<Integer, SensorDataField>(){{
            put(1, new SensorDataField("light", SensorDataType.INTEGER));
        }});
    }


    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
