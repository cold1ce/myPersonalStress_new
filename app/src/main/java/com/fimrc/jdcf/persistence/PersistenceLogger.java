package com.fimrc.jdcf.persistence;

import com.fimrc.jdcf.persistence.container.SensorRecord;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

/**
 * Abstract class which generically represents all possible kind of persistence e.g. database, csv-file, xml-file, ...
 */
public abstract class PersistenceLogger implements Runnable {

    protected Semaphore mutex;
    private volatile ConcurrentLinkedQueue<SensorRecord> bufferQueue;
    private volatile long currentIndex;
    private volatile boolean running = true;

    /**
     * Initializes the PersistenceLogger before starting the logging-process
     *
     * @param args arguments to be used within the initialization of the logger
     */
    public void initializeLogging(String[] args) {
        currentIndex = 0;
        bufferQueue = new ConcurrentLinkedQueue<SensorRecord>();
        mutex = new Semaphore(1);
    }

    /**
     * run-Method for the logging-thread, which pulls the data from the bufferQueue and triggers to write out the
     * SensorRecords
     */
    @Override
    public void run() {
        try {
            while (running) {
                if (!bufferQueue.isEmpty()) {
                    mutex.acquire();
                    try {
                        while (!bufferQueue.isEmpty()) {
                            writeSensorRecord(bufferQueue.poll());
                        }
                    } finally {
                        mutex.release();
                    }
                }
                //thread should sleep for some time to let other sensors push their data into their buffer
                Thread.sleep(10);
            }
            finalizeLogging();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the next index to create a unique identifier for every SensorRecord
     *
     * @return nextIndex for next SensorRecord
     */
    public long getNextIndex() {
        return currentIndex++;
    }

    /**
     * Pushes a SensorRecord to the bufferQueue
     *
     * @param record SensorRecord which is stored in a Queue and
     */
    public void logSensorRecord(SensorRecord record) {
        bufferQueue.offer(record);
    }

    //public abstract Iterator<SensorRecord> readAllRecords(SensorRecordStructure structure);

    @Deprecated
    public abstract void modifyAllRecords(Iterator<SensorRecord> iterator);

    /**
     * While the logging-process is active this method is periodically called to write the SensorRecords in the bufferQueue
     * out to e.g. the database or a file This method has to be implemented by the user of JDCF
     *
     * @param record SensorRecord to be written out
     */
    protected abstract void writeSensorRecord(SensorRecord record);

    /**
     * When the logging-process stops this method will be called to e.g. disconnect from the database
     */
    protected abstract void finalizeLogging();

    /**
     * Enables the logging-process. Needs to be called before starting the thread
     */
    public void startLogging() {
        running = true;
    }

    /**
     * Stops the logging-process and the corresponding thread
     */
    public void stopLogging() {
        running = false;
    }
}
