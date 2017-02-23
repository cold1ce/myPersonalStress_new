package com.fimrc.sensorfusionframework.persistence.structure;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;

import java.util.Iterator;
import java.util.List;


public abstract class SensorRecordStructure {
    private List<String> structure;

    /**
     * @param structure Naming list of the elements in the structure
     */
    public SensorRecordStructure(List<String> structure) {
        setStructure(structure);
    }

    /**
     * @param loggedRecords Iterator with the SensorRecords you want to generate new information
     * @param informationType Number of the information you want to generate
     * @return Iterator above the calculated SensorRecords
     */
    public abstract Iterator<SensorRecord> generateInformation(Iterator<SensorRecord> loggedRecords, int informationType);

    /**
     * @return naming list of the elements in the structure
     */
    public List<String> getStructure() {
        return structure;
    }

    /**
     * @param structure naming list of the elements in the structure
     */
    private void setStructure(List<String> structure) {
        this.structure = structure;
    }
}
