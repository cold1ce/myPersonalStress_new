package com.fimrc.jdcf.sensors;

import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;

/**
 * A SensorController is the interface between the physical sensor and the SensorModule for the JDCF-Framework.
 * A SensorController controls the physical sensor by e.g. calling the corresponding library or accessing the specific
 * api All implementations needed to create a new SensorRecord should be located here (or in a sub-class of SensorController).
 */
public abstract class SensorController {

    protected SensorModule module;
    protected SensorRecordStructure structure;

    /**
     * Creates a new SensorController
     *
     * @param module SensorModule to be linked with the controller
     */
    public SensorController(SensorModule module) {
        this.module = module;
        structure = module.getStructure();
    }

    /**
     * Passes a SensorRecord to the module
     *
     * @param sensorRecord SensorRecord to be logged
     */
    public void logSensorRecord(SensorRecord sensorRecord) {
        module.logSensorRecord(sensorRecord);
    }
}