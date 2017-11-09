package com.fimrc.jdcf.sensors.event;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.SensorModule;

/**
 * This class is an extension of the class SensorModule. With a SensorEventModule it is possible to
 **/
public class SensorEventModule extends SensorModule {

    /**
     * Creates a new SensorEventModule
     *
     * @param logger    Implemented PersistenceLogger class where this Sensor will logSensorRecord
     * @param structure SensorRecordStructure which implements the structure for this Sensor
     */
    public SensorEventModule(PersistenceLogger logger, SensorRecordStructure structure) {
        super(logger, structure);
    }
}
