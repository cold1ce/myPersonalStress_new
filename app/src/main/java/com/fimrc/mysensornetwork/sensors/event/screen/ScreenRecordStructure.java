package com.fimrc.mysensornetwork.sensors.event.screen;

import android.util.Pair;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.Datatypes;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Sven on 22.02.2017.
 */

public class ScreenRecordStructure extends SensorRecordStructure {

    public ScreenRecordStructure(){
        super(new HashMap<Integer, Pair<String, Datatypes>>(){{
            put(1, new Pair<>("event", Datatypes.STRING));
        }});
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
