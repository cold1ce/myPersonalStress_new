package com.fimrc.mysensornetwork.sensors.time.cell;

import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 10.03.2017.
 */

public class CellRecordStructure extends SensorRecordStructure {


    public CellRecordStructure() {
        super(new HashMap<Integer, SensorDataField>(){{
            put(1, new SensorDataField("cellID", SensorDataType.INTEGER));
            put(2, new SensorDataField("cellLac", SensorDataType.INTEGER));
            put(3, new SensorDataField("roaming", SensorDataType.INTEGER));
            put(4, new SensorDataField("signal", SensorDataType.INTEGER));
            put(5, new SensorDataField("signal_bar", SensorDataType.INTEGER));
            put(6, new SensorDataField("data_state", SensorDataType.INTEGER));
            put(7, new SensorDataField("mcc", SensorDataType.INTEGER));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
