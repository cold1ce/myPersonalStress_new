package com.fimrc.jdcf.persistence.container;

import java.nio.BufferOverflowException;

/**
 * A SensorRecordQueue is a simple queue for SensorRecords with a specific size
 */
public class SensorRecordQueue {

    private SensorRecord[] elementArray;
    private int firstElement;
    private int currentSize;

    /**
     * Creates a new SensorRecordQueue with a specific size
     *
     * @param size size of the queue
     */
    public SensorRecordQueue(int size) throws RuntimeException {
        if (size < 0) {
            throw new NegativeArraySizeException();
        }
        elementArray = new SensorRecord[size];
        firstElement = 0;
        currentSize = 0;
    }

    /**
     * Returns the dequeued element of the queue
     *
     * @return last element in the queue; null for empty queue
     */
    public SensorRecord dequeue() {
        if (!(this.isEmpty())) {
            currentSize--;
            if (firstElement == (elementArray.length - 1)) {
                firstElement = 0;
                return this.elementArray[elementArray.length - 1];
            }
            return this.elementArray[firstElement++];
        } else {
            return null;
        }
    }

    /**
     * Enqueues a new SensorRecord to the queue
     *
     * @param record SensorRecord for entering the queue
     * @throws RuntimeException if queue is full
     */
    public void enqueue(SensorRecord record) throws RuntimeException {
        if (record == null) {
        } else if (!(this.isFull())) {
            int neu = (firstElement + currentSize) % elementArray.length;
            elementArray[neu] = record;
            currentSize++;
        } else {
            throw new BufferOverflowException();
        }
    }

    /**
     * Returns the SensorRecord on a specific index of the queue
     *
     * @param index index of the element to read.
     * @return SensorRecord at position: index
     */
    public SensorRecord get(int index) throws RuntimeException {
        if (index >= currentSize) {
            return null;
        } else {
            return elementArray[(firstElement + index) % elementArray.length];
        }
    }

    /**
     * Returns whether the queue is empty or not
     *
     * @return true if queue is empty; false if queue is not empty
     */
    public boolean isEmpty() {
        return (currentSize == 0);
    }

    /**
     * Returns whether the queue is full or not
     *
     * @return true if queue is full; false if queue is not full
     */
    public boolean isFull() {
        return elementArray.length == currentSize;
    }
}