package com.fimrc.jdcf.persistence.container;

/**
 * SensorRecordBuffer
 * <p>
 * ----------------
 * | SensorRecord |
 * ----------------
 * ----------------
 * | SensorRecord |       <-------------------|
 * ----------------                           |
 * |
 * ----------------       ----------          |
 * | SensorRecord |       |        |   -------|
 * ----------------       |   P    |
 * ----------------       |   R    |     A
 * | SensorRecord |       |   E    |     |
 * ----------------       |   D    |     |
 * ----------------       |   E    |     |
 * | SensorRecord |       |   C    |     |    countPredecessors: 5
 * ----------------       |   E    |     |
 * ----------------       |   S    |     |
 * | SensorRecord |       |   S    |     |
 * ----------------       |   O    |     |
 * ----------------       |   R    |
 * | SensorRecord |       |   S    |   <------|
 * ----------------       ----------          |
 * |
 * ----------------    ----------------   ----|
 * | SensorRecord |    | currentRecord |
 * ----------------    ----------------   <---|
 * |
 * ----------------       ----------          |
 * | SensorRecord |       |   S    |  --------|
 * ----------------       |   U    |
 * ----------------       |   C    |     A
 * | SensorRecord |       |   C    |     |
 * ----------------       |   E    |     |
 * ----------------       |   S    |     |    countSuccessors: 4
 * | SensorRecord |       |   S    |     |
 * ----------------       |   O    |     |
 * ----------------       |   R    |
 * | SensorRecord |       |   S    |   <-----|
 * ----------------       ----------         |
 * |
 * ----------------                          |
 * | SensorRecord |       -------------------|
 * ----------------
 * ----------------
 * | SensorRecord |
 * ----------------
 * <p>
 * Enough explained ;)
 */

public class SensorRecordBuffer {

    private SensorRecordQueue queuePredecessors, queueSuccessors;
    private SensorRecord currentRecord;
    private int countPredecessors, countSuccessors;

    /**
     * Creates a new SensorRecordBuffer
     *
     * @param countPredecessors Numbers of Predecessors
     * @param countSuccessors   Numbers of Successors
     */
    public SensorRecordBuffer(int countPredecessors, int countSuccessors) {
        this.countPredecessors = countPredecessors;
        this.countSuccessors = countSuccessors;
        if (countPredecessors > 0) {
            queuePredecessors = new SensorRecordQueue(countPredecessors);
        }
        if (countSuccessors > 0) {
            queueSuccessors = new SensorRecordQueue(countSuccessors);
        }
    }

    /**
     * Returns the current SensorRecord
     *
     * @return the current SensorRecord between the two SensorRecordQueues
     */
    public SensorRecord getCurrentRecord() {
        return currentRecord;
    }

    /**
     * Returns the SensorRecord of the predecessors on a specific index
     *
     * @param index predecessor index. index={0,1,2,...,n}
     * @return predecessor-SensorRecord on the specific index, null if there are no Predecessors or the Index is out of Bounds
     */
    public SensorRecord getPredecessor(int index) {
        if (queuePredecessors != null) {
            return queuePredecessors.get(index);
        } else {
            return null;
        }
    }

    @Deprecated
    public SensorRecord getPreEvent(int structureIndex, Object event, int index) {
        try {
            if (queuePredecessors != null) {
                int gefunden = -1;
                int zaehler = 0;
                SensorRecord record;
                while (queuePredecessors.get(zaehler) != null) {
                    record = queuePredecessors.get(zaehler);
                    String stringCheck = record.get(structureIndex).toString();
                    if (stringCheck.equals(event)) {
                        gefunden++;
                        if (gefunden == index)
                            return record;
                    }
                    zaehler++;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Returns the SensorRecord of the successors on a specific index
     *
     * @param index successor index. index={0,1,2,...,n}
     * @return successor-SensorRecord on the specific index, null if there are no Predecessors or the Index is out of Bounds
     */
    public SensorRecord getSuccessor(int index) {
        if (queueSuccessors != null) {
            return queueSuccessors.get(index);
        } else {
            return null;
        }
    }

    /**
     * pushes a new SensorRecord in the SensorRecordBuffer. The oldest SensorRecord will be removed from the buffer
     *
     * @param record to push in the Buffer. Visualisation on top in this class.
     */
    public void pushRecord(SensorRecord record) {
        if (queuePredecessors != null) {
            if (queuePredecessors.isFull())
                queuePredecessors.dequeue();
            queuePredecessors.enqueue(currentRecord);
        }
        if (queueSuccessors != null) {
            currentRecord = queueSuccessors.dequeue();
            queueSuccessors.enqueue(record);
        } else {
            currentRecord = record;
        }
    }

    /**
     * Call this Method to initially fill the Successor-Queue without moving SensorRecords between Successor, current and Predecessor
     *
     * @param record to push initial Record in the Successors queue
     */
    public void pushInitialRecord(SensorRecord record) {
        queueSuccessors.enqueue(record);
    }

    /**
     * Returns the amount of predecessors in the predecessor-queue
     *
     * @return size of predecessors queue
     */
    public int getCountPredecessors() {
        return countPredecessors;
    }

    /**
     * Returns the amount of successors in the successor-queue
     *
     * @return size of successors queue
     */
    public int getCountSuccessors() {
        return countSuccessors;
    }
}
