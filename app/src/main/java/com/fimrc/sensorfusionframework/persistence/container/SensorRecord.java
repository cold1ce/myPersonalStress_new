package com.fimrc.sensorfusionframework.persistence.container;

import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructureException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


public class SensorRecord implements Iterable<Object> {
    private long ID;
    private Date timestamp;
    private ArrayList<Object> valueList;
    private final SensorRecordStructure structure;

    /**
     * @param ID of the new SensorRecord
     * @param timestamp Time when the SensorRecord is created. Standard: LocalDateTime.now()
     * @param structure the SensorRecordStructure which is be implemented from this SensorRecord
     */
    public SensorRecord(long ID, Date timestamp, SensorRecordStructure structure) {
        setID(ID);
        valueList = new ArrayList<>();
        setTimestamp(timestamp);
        this.structure = structure;
    }

    /**
     * @param name of the SensorRecordStructure column to add the value
     * @param value the value added to the column where SensorRecordStructure == name
     * @throws RuntimeException if there is a mismatch with the SensorRecordStructure
     */
    public void addData(String name, Object value) throws RuntimeException {
        boolean isValueSet = false;
        if (structure.getStructure().size() > valueList.size()) {
            while (structure.getStructure().size() > valueList.size()) {
                if (structure.getStructure().get(valueList.size()).equals(name)) {
                    valueList.add(value);
                    isValueSet = true;
                    break;
                }else
                    this.addData(structure.getStructure().get(valueList.size()).toString(), " ");
            }
        }else{
            throw new SensorRecordStructureException("The current valueList has already reached the capacity of the corresponding SensorDataStructure ");
        }
        if(!isValueSet) {
            throw new SensorRecordStructureException("Mismatching between Object to be inserted and the corresponding SensorDataStructure");
        }
    }

    /**
     * @return LocalDateTime of the SensorRecord
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp changes the current Timestamp of the SensorRecord
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return implemented SensorRecordStructure of this SensorRecord
     */
    public SensorRecordStructure getStructure() {
        return structure;
    }

    /**
     * @param ID new ID for the SensorRecord
     */
    private void setID(long ID) {
        this.ID = ID;
    }

    /**
     * @return the ID of the SensorRecord
     */
    public long getID() {
        return ID;
    }

    /**
     * @return iterator for the values in the SensorRecord
     */
    @Override
    public Iterator<Object> iterator() {
        return valueList.iterator();
    }

    /**
     * @param index of the value in the SensorRecordStructure.
     * @return value of the Object in the SensorRecord at position index
     */
    public Object get(int index) {
        return valueList.get(index);
    }
}
