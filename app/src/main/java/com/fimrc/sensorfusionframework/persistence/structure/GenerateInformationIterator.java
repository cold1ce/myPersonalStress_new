package com.fimrc.sensorfusionframework.persistence.structure;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.persistence.container.SensorRecordBuffer;

import java.util.Iterator;
import java.util.function.Function;


public class GenerateInformationIterator implements Iterator<SensorRecord> {

    private Iterator<SensorRecord> iterator;
    private SensorRecordBuffer buffer;
    private Function<SensorRecordBuffer, SensorRecord> function;
    private SensorRecordStructure structure;

    /**
     * @param iterator Iterator above the SensorRecords you want to generateInformation
     * @param countPredecessors How much Predecessors do you need for the Information calculation
     * @param countSuccessors How much Successors do you need for the Information calculation
     * @param function How the calculation works
     * @param structure SensorRecordStructure from the SensorRecordObjects
     */
    public GenerateInformationIterator(Iterator<SensorRecord> iterator, int countPredecessors, int countSuccessors, Function<SensorRecordBuffer, SensorRecord> function, SensorRecordStructure structure) {
        this.iterator = iterator;
        buffer = new SensorRecordBuffer(countPredecessors, countSuccessors);
        this.function = function;
        this.structure = structure;
        for (int i = 0; i < countSuccessors; i++) {
            SensorRecord sensorRecord = iterator.next();
            buffer.pushInitialRecord(sensorRecord);
        }
    }

    /**
     * @return true: there is a next SensorRecord // false: else
     */
    @Override
    public boolean hasNext() {
        //when there is a queue for predecessors --> we need to have a look at the next SensorRecord
        if (buffer.getCountSuccessors() > 0) {
            return buffer.getSuccessor(0) != null;
        }
        //else our next record comes (maybe) from the iterator
        else {
            return iterator.hasNext();
        }
    }

    /**
     * @return Next SensorRecord in the Iterator
     */
    @Override
    public SensorRecord next() {
        SensorRecord sensorRecord = null;
        //when there are more records left in iterator --> grab them and push it to our buffer
        if (iterator.hasNext()) {
            sensorRecord = iterator.next();
        }
        buffer.pushRecord(sensorRecord);
        sensorRecord = function.apply(buffer);
        return sensorRecord;
    }


    @Override
    public void remove() {
        iterator.remove();
    }
}
