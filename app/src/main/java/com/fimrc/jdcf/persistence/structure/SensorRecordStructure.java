package com.fimrc.jdcf.persistence.structure;

import com.fimrc.jdcf.persistence.container.SensorDataField;
import com.fimrc.jdcf.persistence.container.SensorRecord;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A SensorRecordStructure defines the structure of a SensorRecord of a specific sensor.
 */
public abstract class SensorRecordStructure {
    private HashMap<Integer, SensorDataField> structure;

    /**
     * Creates a new SensorRecordStructure
     *
     * @param structure Naming list of the elements in the structure
     */
    public SensorRecordStructure(HashMap<Integer, SensorDataField> structure) {
        setStructure(structure);
    }

    @Deprecated
    public abstract Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType);

    /**
     * Returns the name of the field of a specific index
     *
     * @param index, whose name should be returned
     * @return the name of the field with the given
     */
    public String getNameAtIndex(int index) {
        SensorDataField tuple = getDataField(index + 1);
        return tuple.getName();
    }

    /**
     * Returns the dataType of the field of a specific index
     *
     * @param index, whose dataType should be returned
     * @return the dataType of the field with the given index
     */
    public SensorDataType getDataTypeAtIndex(int index) {
        SensorDataField tuple = getDataField(index + 1);
        return tuple.getDataType();
    }

    /**
     * Returns the amount of fields of the SensorRecordStructure without index and time
     *
     * @return the amount of fields without index and time
     */
    public int size() {
        return structure.size();
    }

    /**
     * Returns the SensorDataField of a specific index
     *
     * @param index whose SensorDataField should be returned
     * @return SensorDataField of the index
     */
    private SensorDataField getDataField(int index) {
        return structure.get(index);
    }

    /**
     * Checks if the structure is not null or empty and sets the local variable
     *
     * @param structure HashMap to be checked and set
     */
    private void setStructure(HashMap<Integer, SensorDataField> structure) {
        if (structure != null) {
            if (structure.size() == 0) {
                throw new SensorRecordStructureException("The structure to be set is empty");
            } else {
                this.structure = structure;
            }
        } else {
            throw new SensorRecordStructureException("Null is not a valid SensorRecordStructure");
        }
    }
}
