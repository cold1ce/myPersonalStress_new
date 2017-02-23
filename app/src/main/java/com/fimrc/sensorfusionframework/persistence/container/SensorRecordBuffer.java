package com.fimrc.sensorfusionframework.persistence.container;

/**
 * SensorRecordBuffer
 *
 *   ----------------
 *   | SensorRecord |
 *   ----------------
 *   ----------------
 *   | SensorRecord |       <-------------------|
 *   ----------------                           |
 *                                              |
 *   ----------------       ----------          |
 *   | SensorRecord |       |        |   -------|
 *   ----------------       |   P    |
 *   ----------------       |   R    |     A
 *   | SensorRecord |       |   E    |     |
 *   ----------------       |   D    |     |
 *   ----------------       |   E    |     |
 *   | SensorRecord |       |   C    |     |    countPredecessors: 5
 *   ----------------       |   E    |     |
 *   ----------------       |   S    |     |
 *   | SensorRecord |       |   S    |     |
 *   ----------------       |   O    |     |
 *   ----------------       |   R    |
 *   | SensorRecord |       |   S    |   <------|
 *   ----------------       ----------          |
 *                                              |
 *   ----------------    ----------------   ----|
 *   | SensorRecord |    | currentRecord |
 *   ----------------    ----------------   <---|
 *                                              |
 *   ----------------       ----------          |
 *   | SensorRecord |       |   S    |  --------|
 *   ----------------       |   U    |
 *   ----------------       |   C    |     A
 *   | SensorRecord |       |   C    |     |
 *   ----------------       |   E    |     |
 *   ----------------       |   S    |     |    countSuccessors: 4
 *   | SensorRecord |       |   S    |     |
 *   ----------------       |   O    |     |
 *   ----------------       |   R    |
 *   | SensorRecord |       |   S    |   <-----|
 *   ----------------       ----------         |
 *                                             |
 *   ----------------                          |
 *   | SensorRecord |       -------------------|
 *   ----------------
 *   ----------------
 *   | SensorRecord |
 *   ----------------
 *
 */

public class SensorRecordBuffer {

    private SensorRecordQueue queuePredecessors, queueSuccessors;
    private SensorRecord currentRecord;
    private int countPredecessors, countSuccessors;

    /**
     * @param countPredecessors Numbers of Predecessors
     * @param countSuccessors Numbers of Successors
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


    public SensorRecord getCurrentRecord() {
        return currentRecord;
    }

    /**
     * @param index Predecessor index. index={0,1,2,...,n}
     * @return: SensorRecord @ index
     *          no Predecessors or Index out of bound: null
     */
    public SensorRecord getPredecessor(int index) {
        if (queuePredecessors != null) {
            return queuePredecessors.get(index);
        } else {
            return null;
        }
    }

    /**
     * @param structureIndex Index of the Event in the implemented RecordStructure
     * @param event value must equal the SensorRecord at the structureIndex
     * @param index how much SensorRecords in the past. Index={0,1,...,n}
     * @return SensorRecord from a past event identified by the index (how far the Record is in the past)
     * and the event, representing a value at the SensorRecordStructure // on errors: always null
     */
    public SensorRecord getPreEvent(int structureIndex, Object event, int index) {
        try{
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
        }catch(Exception e){
            return null;
        }
        return null;
    }

    /**
     * @param index of the SensorRecord in the future. Index = {0,1,...,n}
     * @return SensorRecord in the SuccessorQueue at the index // Errors: null
     */
    public SensorRecord getSuccessor(int index) {
        if (queueSuccessors != null) {
            return queueSuccessors.get(index);
        } else {
            return null;
        }
    }

    /**
     * @param record to push in the Buffer. Visualisation on top in this class.
     */
    public void pushRecord(SensorRecord record) {
        if(queuePredecessors != null) {
            if(queuePredecessors.isFull())
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
     * @param record to push initial Record in the Successors queue
     */
    public void pushInitialRecord(SensorRecord record) {
        queueSuccessors.enqueue(record);
    }

    /**
     * @return size of Predecessors queue
     */
    public int getCountPredecessors() {
        return countPredecessors;
    }

    /**
     * @return size of Successors queue
     */
    public int getCountSuccessors() {
        return countSuccessors;
    }
}
