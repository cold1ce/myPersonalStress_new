package com.fimrc.sensorfusionframework.persistence;

import android.content.Context;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;


public abstract class PersistenceLogger implements Runnable {

    private volatile ConcurrentLinkedQueue<SensorRecord> bufferQueue = new ConcurrentLinkedQueue<SensorRecord>();;
    protected Semaphore mutex = new Semaphore(1);
    private volatile long currentIndex = 0;;
    private volatile boolean running = true;


    public abstract void initialize(Object[] array);

    /**
     *  run Thread for the PersistenceLogger
     */
    @Override
    public void run() {
        try {
            while (running) {
                if (!bufferQueue.isEmpty()) {
                    mutex.acquire();
                    try {
                        while (!bufferQueue.isEmpty()) {
                            log(bufferQueue.poll());
                        }
                    } finally {
                        mutex.release();
                    }
                }
                //thread should sleep for some time to let other sensors push their data into their buffer
                Thread.sleep(10);
            }
            finalize();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return nextIndex for next SensorRecord
     */
    public long getNextIndex() {
        return currentIndex++;
    }

    public void writeRecord(SensorRecord record) {
        //log(record);
        bufferQueue.offer(record);
    }

    public abstract Iterator<SensorRecord> readAllRecords(SensorRecordStructure structure);

    public abstract void modifyAllRecords(Iterator<SensorRecord> iterator);

    protected abstract void log(SensorRecord record);

    protected abstract void finalize();

    public boolean startLogging() {
        running = true;
        return true;
    }

    public boolean stopLogging() {
        running = false;
        return true;
    }
}
