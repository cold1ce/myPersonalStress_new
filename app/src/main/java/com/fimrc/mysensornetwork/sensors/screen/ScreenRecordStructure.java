package com.fimrc.mysensornetwork.sensors.screen;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Sven on 22.02.2017.
 */

public class ScreenRecordStructure extends SensorRecordStructure {

    public ScreenRecordStructure(){
        super(new ArrayList<String>(Arrays.asList(
                "event"
        )));
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
