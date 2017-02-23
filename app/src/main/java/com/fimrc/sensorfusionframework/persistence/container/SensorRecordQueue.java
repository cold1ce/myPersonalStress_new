package com.fimrc.sensorfusionframework.persistence.container;

import java.nio.BufferOverflowException;


public class SensorRecordQueue {

    private SensorRecord[] elementArray;
    private int firstElement; //Position des ersten Element - Array Indexschreibweise
    private int currentSize; // Länge der Queue - Längenschreibweise

    /**
     * @param size Size of the Queue
     * @throws RuntimeException for negative Size
     */
    public SensorRecordQueue(int size) throws RuntimeException {
        // Leere Warteschlange fuer bis zu n Elemente erzeugen.
        if (size < 0) {
            throw new NegativeArraySizeException();
        }
        elementArray = new SensorRecord[size];
        firstElement = 0;
        currentSize = 0;
    }

    /**
     * @return last Element in the Queue. / For empty Queue: null
     */
    public SensorRecord dequeue() {
        // Erstes Element der Liste zurueckgeben.
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
     * @param record SensorRecord for entering the Queue
     * @throws RuntimeException if queue is full
     */
    public void enqueue(SensorRecord record) throws RuntimeException {
        // Element hinten einfuegen.
        if(record == null) {
        }else if(!(this.isFull())) {
            int neu = (firstElement+currentSize) % elementArray.length;
            elementArray[neu] = record;
            currentSize++;
        } else {
            throw new BufferOverflowException();
        }
    }

    /**
     * @param index Index of the Element to read.
     * @return SensorRecord at Position: index
     * @throws RuntimeException If index > size
     */
    public SensorRecord get(int index) throws RuntimeException {
        if (index >= currentSize) {
            return null;
        } else {
            return elementArray[(firstElement + index) % elementArray.length];
        }
    }

    /**
     * @return true if Queue is empty / false if Queue is not empty
     */
    public boolean isEmpty() {
        // Testen, ob die Liste leer ist.
        return (currentSize == 0);
    }

    /**
     * @return true if Queue is full / false if Queue is empty
     */
    public boolean isFull() {
        // Testen, ob die Liste voll ist.
        return elementArray.length == currentSize;
    }
}