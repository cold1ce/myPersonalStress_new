package com.fimrc.mysensornetwork.sensors.time.light;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Sven on 02.03.2017.
 */

public class LightRecordStructure extends SensorRecordStructure {

    public LightRecordStructure(){
        super(new ArrayList<String>(Arrays.asList(
                "light"
        )));
    }


    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
