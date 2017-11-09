package com.fimrc.jdcf.sensors;

import java.util.HashMap;
import java.util.Iterator;

/**
 * The SensorManager is a container for an amount of sensors of a specific type S (enum)
 *
 * @param <S> an enum in which all possible SensorTypes, which can be stored, are listed
 */
public class SensorManager<S extends Enum> implements Iterable<SensorModule> {

    private HashMap<S, SensorModule> sensorList;
    private int countSensors = -1;

    /**
     * Creates a new SensorManager to store Sensors
     */
    public SensorManager() {
        sensorList = new HashMap<>();
        countSensors = 0;
    }

    /**
     * Inserts a new Sensor in the HashMap with the index of an element of an enum
     *
     * @param enumElement element of an enum used as an index
     * @param sensor      the sensor to be stored
     */
    public void insertSensor(S enumElement, SensorModule sensor) {
        sensorList.put(enumElement, sensor);
        countSensors++;
    }

    /**
     * Removes a sensor from the HashMap
     *
     * @param sensor SensorModule will be removed from the SensorManager
     */
    public void removeSensor(SensorModule sensor) {
        sensorList.remove(sensor);
        countSensors--;
    }

    /**
     * Returns the SensorModule for a specific enumElement
     *
     * @param enumElement Index of the SensorModule in the ArrayList of all SensorModules
     * @return SensorModule at Index if exists
     */
    public SensorModule getSensor(S enumElement) {
        return sensorList.get(enumElement);
    }

    /**
     * Returns an Iterator for all SensorModules which a currently stored in the HashMap
     *
     * @return Iterator for all SensorModules
     */
    public Iterator<SensorModule> iterator() {
        return sensorList.values().iterator();
    }

    /**
     * Activates all SensorModules which are currently in the HashMap
     */
    public void activateAllSensors() {
        Iterator<SensorModule> sensorIterator = iterator();
        while (sensorIterator.hasNext()) {
            SensorModule sensor = sensorIterator.next();
            sensor.activateSensor();
        }
    }

    /**
     * Deactivates all SensorModules which are currently in the HashMap
     */
    public void deactivateAllSensors() {
        Iterator<SensorModule> sensorIterator = iterator();
        while (sensorIterator.hasNext()) {
            SensorModule sensor = sensorIterator.next();
            sensor.deactivateSensor();
        }
    }

    /**
     * Returns the amount of Sensors which a currently stored in the HashMap
     *
     * @return the current count of Sensors stored in the SensorManager
     */
    public int getCountSensors() {
        return countSensors;
    }
}
