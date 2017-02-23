package com.fimrc.sensorfusionframework.sensors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SensorManager implements Iterable<SensorModule> {
    private static SensorManager unique = null;
    private List<SensorModule> sensorList;

    /**
     *  Singleton constructor
     */
    private SensorManager() {
        sensorList = new ArrayList<>();
    }

    /**
     * @return SensorManager / If no SensorManager exists: null
     */
    public static SensorManager instance() {
        if (unique == null) {
            return unique = new SensorManager();
        } else {
            return unique;
        }
    }

    /**
     * @param sensor SensorModule will be inserted to the SensorManager
     */
    public void insertSensor(SensorModule sensor) {
        sensorList.add(sensor);
    }

    /**
     * @param sensor SensorModule will be deleted from the SensorManager
     */
    public void removeSensor(SensorModule sensor) {
        sensorList.remove(sensor);
    }

    /**
     * @param index Index of the SensorModule in the ArrayList of all SensorModules
     * @return SensorModule at Index if exists
     */
    public SensorModule getSensor(int index) {
        return sensorList.get(index);
    }

    /**
     * @return Iterator for all SensorModules
     */
    public Iterator<SensorModule> iterator() {
        return sensorList.iterator();
    }
}
