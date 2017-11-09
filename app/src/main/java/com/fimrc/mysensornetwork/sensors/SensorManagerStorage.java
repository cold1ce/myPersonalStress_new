package com.fimrc.mysensornetwork.sensors;

import com.fimrc.jdcf.sensors.SensorManager;
import com.fimrc.jdcf.sensors.SensorModule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sven on 19.05.2017.
 */

public class SensorManagerStorage<S extends SensorContainer> implements Iterable<SensorManager>{

    private static SensorManagerStorage unique = null;
    private HashMap<S, SensorManager> managerList;

    private SensorManagerStorage(){
        //sensorManagerList = new ArrayList<>();
        managerList = new HashMap<S, SensorManager>();
    }

    public static SensorManagerStorage instance(){
        if(unique == null){
            return unique = new SensorManagerStorage();
        }else{
            return unique;
        }
    }

    public void insertManager(S enumElement, SensorManager manager) {
        managerList.put(enumElement, manager);
    }

    public void removeSensor(SensorModule sensor) {
        managerList.remove(sensor);
    }

    public SensorManager getSensorManager(S enumElement) {
        return managerList.get(enumElement);
    }

    public Iterator<SensorManager> iterator(){
        return managerList.values().iterator();
    }
}
