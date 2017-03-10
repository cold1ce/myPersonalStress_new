package com.fimrc.mysensornetwork.sensors.time.gps;

import android.util.Pair;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.Datatypes;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 01.03.2017.
 */

public class GPSRecordStructure extends SensorRecordStructure {

    public GPSRecordStructure(){
        super(new HashMap<Integer, Pair<String, Datatypes>>(){{
            put(1, new Pair<>("longitude", Datatypes.DOUBLE));
            put(2, new Pair<>("latitude", Datatypes.DOUBLE));
            put(3, new Pair<>("altitude", Datatypes.DOUBLE));
            put(4, new Pair<>("bearing", Datatypes.DOUBLE));
            put(5, new Pair<>("speed",  Datatypes.DOUBLE));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
