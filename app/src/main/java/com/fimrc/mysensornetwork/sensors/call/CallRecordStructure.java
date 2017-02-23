package com.fimrc.mysensornetwork.sensors.call;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Sven on 23.02.2017.
 */

public class CallRecordStructure extends SensorRecordStructure {

    public CallRecordStructure(){
        super(new ArrayList<String>(Arrays.asList(
                "callee",
                "caller"
        )));
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
