package com.fimrc.jdcf.persistence.structure;

import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.container.SensorRecordBuffer;

import java.util.Iterator;
import java.util.function.Function;

@Deprecated
public class GenerateInformationIterator implements Iterator<SensorRecord> {

    private Iterator<SensorRecord> iterator;
    private SensorRecordBuffer buffer;
    private Function<SensorRecordBuffer, SensorRecord> function;
    private SensorRecordStructure structure;

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
