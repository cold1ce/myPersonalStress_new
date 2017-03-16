package com.fimrc.mysensornetwork.sensors.event.call;

import android.util.Pair;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.Datatypes;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 23.02.2017.
 */

public class CallRecordStructure extends SensorRecordStructure {

    public CallRecordStructure(){
        super(new HashMap<Integer, Pair<String, Datatypes>>(){{
            put(1, new Pair<>("callee", Datatypes.STRING));
            put(2, new Pair<>("caller", Datatypes.STRING));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
