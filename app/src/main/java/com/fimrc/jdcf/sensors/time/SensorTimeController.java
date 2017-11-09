package com.fimrc.jdcf.sensors.time;

import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.sensors.SensorController;
import com.fimrc.jdcf.sensors.SensorModule;

/**
 * A Subclass of SensorController for the implementation of a time-based sensor, which creates a SensorRecord in a fixed
 * period of time (default: every second)
 */
public abstract class SensorTimeController extends SensorController implements Runnable {

    /**
     * Creates a new SensorTimeController
     *
     * @param module SensorModule to be linked with the controller
     */
    public SensorTimeController(SensorModule module) {
        super(module);
    }

    /**
     * run-method which is periodically called by the SensorTimeModule to create a new SensorRecord
     */
    @Override
    public void run() {
        SensorRecord sensorRecord = buildSensorRecord();
        super.logSensorRecord(sensorRecord);
    }

    /**
     * At least every (delay/1000) seconds this method will be called by the run-method to create a new SensorRecord.
     * This method has to be implemented by the user of JDCF
     *
     * @return a new created SensorRecord
     */
    protected abstract SensorRecord buildSensorRecord();
}
