package com.fimrc.mysensornetwork.sensors.time.gps;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by Sven on 01.03.2017.
 */

public class GPSRecordStructure extends SensorRecordStructure {

    public GPSRecordStructure(){
        super(new ArrayList<String>(Arrays.asList(
                "longitude",
                "latitude",
                "altitude",
                "bearing",
                "speed"

        )));
    }

    @Override
    public Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType) {
        return null;
    }
}
