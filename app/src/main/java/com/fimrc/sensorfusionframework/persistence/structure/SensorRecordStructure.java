package com.fimrc.sensorfusionframework.persistence.structure;

import android.util.Pair;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public abstract class SensorRecordStructure {
    private HashMap<Integer, Pair<String, Datatypes>> structure;

    /**
     * @param structure Naming list of the elements in the structure
     */
    public SensorRecordStructure(HashMap<Integer, Pair<String, Datatypes>> structure) {
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
    public HashMap<Integer, Pair<String, Datatypes>> getStructure() {
        return structure;
    }

    public String getNameAtIndex(int index){
        Pair<String, Datatypes> pair = structure.get(index+1);
        return pair.first;
    }

    public Datatypes getDatatypeAtIndex(int index){
        Pair<String, Datatypes> pair = structure.get(index+1);
        return pair.second;
    }

    /**
     * @param structure naming list of the elements in the structure
     */
    private void setStructure(HashMap<Integer, Pair<String, Datatypes>> structure) {
        this.structure = structure;
    }
}
