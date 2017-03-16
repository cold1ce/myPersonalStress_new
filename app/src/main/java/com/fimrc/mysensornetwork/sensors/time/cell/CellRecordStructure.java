package com.fimrc.mysensornetwork.sensors.time.cell;

import android.util.Pair;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.Datatypes;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 10.03.2017.
 */

public class CellRecordStructure extends SensorRecordStructure {


    public CellRecordStructure() {
        super(new HashMap<Integer, Pair<String, Datatypes>>(){{
            put(1, new Pair<>("cellID", Datatypes.INTEGER));
            put(2, new Pair<>("cellLac", Datatypes.INTEGER));
            put(3, new Pair<>("roaming", Datatypes.INTEGER));
            put(4, new Pair<>("signal", Datatypes.INTEGER));
            put(5, new Pair<>("signal_bar", Datatypes.INTEGER));
            put(6, new Pair<>("data_state", Datatypes.INTEGER));
            put(7, new Pair<>("mcc", Datatypes.INTEGER));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
