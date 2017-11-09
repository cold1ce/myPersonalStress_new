package com.fimrc.jdcf.sensors.time;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.SensorModule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class represents all physical sensors creating new SensorRecords in a fixed period of time (default: every second)
 */
public class SensorTimeModule extends SensorModule {

    private ScheduledExecutorService executor;
    private ScheduledFuture<?> futureTask;
    private long delay;

    /**
     * Creates a new SensorTimeModule with the default value of 1000ms for the interval (delay) between to SensorRecords
     *
     * @param logger    PersistenceLogger where this Sensor will logSensorRecord()
     * @param structure SensorRecordStructure which implements the structure for this Sensor
     */
    public SensorTimeModule(PersistenceLogger logger, SensorRecordStructure structure) {
        super(logger, structure);
        delay = 1000;
        executor = Executors.newScheduledThreadPool(1);
    }

    /**
     * Activates the sensor and starts the periodical writeSensorRecord of SensorRecords
     *
     * @return true if the sensor was activated
     */
    @Override
    public boolean activateSensor() {
        super.activateSensor();
        futureTask = executor.scheduleAtFixedRate((SensorTimeController) controller, 0, delay, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * Deactivates the sensor and stops the periodical writeSensorRecord of SensorRecords
     *
     * @return true if the sensor was deactivated
     */
    @Override
    public boolean deactivateSensor() {
        executor.shutdown();
        super.deactivateSensor();
        return true;
    }

    /**
     * Stops the periodical writeSensorRecord of SensorRecords (if needed) and sets the new interval between to SensorRecords
     *
     * @param delay the delay between the creation of two SensorRecords
     */
    public void setDelay(long delay) {
        if (delay > 0) {
            this.delay = delay;
            if (futureTask != null) {
                futureTask.cancel(true);
                executor.scheduleAtFixedRate((SensorTimeController) controller, 0, delay, TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * Returns the interval (delay) between two SensorRecords
     *
     * @return the delay between the creation of two SensorRecords
     */
    public long getDelay() {
        return delay;
    }
}
