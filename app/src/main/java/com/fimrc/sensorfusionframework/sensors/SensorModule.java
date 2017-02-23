package com.fimrc.sensorfusionframework.sensors;

import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.Iterator;


public abstract class SensorModule {

    private PersistenceLogger logger;
    private final SensorRecordStructure structure;
    protected boolean active;
    private Thread loggingThread;

    /**
     * @param logger    Implemented PersistenceLogger class where this Sensor will log
     * @param structure SensorRecordStructure which implements the structure for this Sensor
     */
    public SensorModule(PersistenceLogger logger, SensorRecordStructure structure) {
        setLogger(logger);
        this.structure = structure;
    }

    /**
     * @return true: Sensor activated, false: Sensor not activated
     */
    public abstract boolean activateSensor();

    /**
     * @return true: Sensor deactivated, false: Sensor not deactivated
     */
    public abstract boolean deactivateSensor();

    /**
     * @return true if logging started successfully / else: true
     */
    public boolean startLogging() {
        logger.startLogging();
        loggingThread = new Thread(logger);
        loggingThread.start();
        return true;
    }

    /**
     * @return true if logging stopped successfully / else: false
     */
    public boolean stopLogging() {
        try {
            logger.stopLogging();
            if (loggingThread != null) {
                loggingThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param informationType Integer from the Information which will be generated / implemented in SensorRecordStructure
     * @return true if Information generated successfully
     */
    public boolean generateInformation(int informationType) {
        stopLogging();
        Iterator<SensorRecord> readIterator = logger.readAllRecords(structure);
        Iterator<SensorRecord> modifyIterator = structure.generateInformation(readIterator, informationType);
        logger.modifyAllRecords(modifyIterator);
        startLogging();
        return true;
    }

    /**
     * @param record SensorRecord which will be logged
     */
    public void log(SensorRecord record) {
        logger.writeRecord(record);
    }

    /**
     * @return Sensor active: true / Sensor not active: false
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return SensorRecordStructure which implements the structure & Calculate Methods for this Sensor
     */
    public SensorRecordStructure getStructure() {
        return structure;
    }

    /**
     * @return PrimaryKey for the next SensorRecord which will be logged
     */
    public long getNextIndex() {
        return logger.getNextIndex();
    }

    /**
     * @param logger Implemented PersistenceLogger class where this Sensor will log
     */
    private void setLogger(PersistenceLogger logger) {
        this.logger = logger;
    }

}