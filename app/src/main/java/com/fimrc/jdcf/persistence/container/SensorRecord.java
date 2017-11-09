package com.fimrc.jdcf.persistence.container;

import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructureException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * A SensorRecord is a n-tuple of an identifier, a timestamp and a list of different objects. The order and type of the
 * values is described in the SensorRecordStructure. A SensorRecord can be compared to a row in a database.
 */
public class SensorRecord implements Iterable<Object> {
    private long ID;
    private Date timestamp;
    private ArrayList<Object> valueList;
    private final SensorRecordStructure structure;

    /**
     * Creates a new SensorRecord
     *
     * @param ID        Identifier of the new SensorRecord
     * @param timestamp Time when the SensorRecord is created. Standard: LocalDateTime.now()
     * @param structure The SensorRecordStructure the SensorRecord has to follow
     */
    public SensorRecord(long ID, Date timestamp, SensorRecordStructure structure) {
        setID(ID);
        valueList = new ArrayList<>();
        setTimestamp(timestamp);
        this.structure = structure;
    }

    /**
     * Adds a new value to the SensorRecord. Values will be skipped when the name is not the next entry in the SensorRecordStructure
     *
     * @param name  Name of the column to be added
     * @param value The value of the column to be added
     */
    public void addData(String name, SensorDataType type, Object value) {
        boolean isValueSet = false;
        if (structure.size() > valueList.size()) {
            while (structure.size() > valueList.size()) {
                String fieldName = structure.getNameAtIndex(valueList.size());
                SensorDataType fieldDataType = structure.getDataTypeAtIndex(valueList.size());
                if (fieldName.equals(name) && fieldDataType == type) {
                    valueList.add(value);
                    isValueSet = true;
                    break;
                } else {
                    this.addData(structure.getNameAtIndex(valueList.size()), structure.getDataTypeAtIndex(valueList.size()), "");
                }
            }
        } else {
            throw new SensorRecordStructureException("The current valueList has already reached the capacity of the corresponding SensorDataStructure ");
        }
        if (!isValueSet) {
            throw new SensorRecordStructureException("Mismatching between Object and/or type to be inserted and the corresponding SensorDataStructure");
        }
    }

    /**
     * Returns the timestamp of the SensorRecord
     *
     * @return LocalDateTime of the SensorRecord
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the SensorRecord
     *
     * @param timestamp changes the current Timestamp of the SensorRecord
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Sets the ID of a SensorRecord
     *
     * @param ID new ID for the SensorRecord
     */
    private void setID(long ID) {
        this.ID = ID;
    }

    /**
     * Returns the ID of a SensorRecord
     *
     * @return the ID of the SensorRecord
     */
    public long getID() {
        return ID;
    }

    /**
     * Returns an Iterator for all values in the SensorRecord
     *
     * @return iterator for the values in the SensorRecord
     */
    @Override
    public Iterator<Object> iterator() {
        return valueList.iterator();
    }

    /**
     * Returns the value-object of a specific index
     *
     * @param index of the value in the SensorRecordStructure.
     * @return value of the Object in the SensorRecord at position index
     */
    public Object get(int index) {
        return valueList.get(index);
    }
}
