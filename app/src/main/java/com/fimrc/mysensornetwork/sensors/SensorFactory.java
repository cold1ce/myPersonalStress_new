package com.fimrc.mysensornetwork.sensors;

import android.content.Context;
import android.util.Log;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.SensorManager;
import com.fimrc.jdcf.sensors.SensorModule;
import com.fimrc.jdcf.sensors.time.SensorTimeModule;
import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.mysensornetwork.sensors.event.SensorEventContainer;
import com.fimrc.mysensornetwork.sensors.time.SensorTimeContainer;

/**
 * Created by Sven on 19.05.2017.
 */

public class SensorFactory {

    public static SensorModule getSensorModule(SensorTimeContainer sensor, Context context){
        if(sensor == null)
            return null;
        String structureClassName = "com.fimrc.mysensornetwork.sensors.time."+sensor.toString().toLowerCase()+"."+sensor.toString()+"RecordStructure";
        String moduleClassName = "com.fimrc.mysensornetwork.sensors.time."+sensor.toString().toLowerCase()+"."+sensor.toString()+"Module";
        String databaseName = sensor.toString()+"Sensor";

        try {
            SensorRecordStructure structure = (SensorRecordStructure)Class.forName(structureClassName).getConstructor().newInstance();
            DatabaseLogger logger = new DatabaseLogger(databaseName, structure, context);
            logger.initializeLogging(null);
            SensorModule module = (SensorTimeModule) Class.forName(moduleClassName).getConstructor(Context.class, PersistenceLogger.class, SensorRecordStructure.class).newInstance(context, logger, structure);
            SensorManagerStorage.instance().getSensorManager(SensorContainer.time).insertSensor(sensor, module);
            return module;
        } catch (Exception e) {
            Log.d("SensorFactory", e.toString());
        }
        return null;
    }

    public static SensorModule getSensorModule(SensorEventContainer sensor, Context context){
        if(sensor == null)
            return null;
        String structureClassName = "com.fimrc.mysensornetwork.sensors.event."+sensor.toString().toLowerCase()+"."+sensor.toString()+"RecordStructure";
        String moduleClassName = "com.fimrc.mysensornetwork.sensors.event."+sensor.toString().toLowerCase()+"."+sensor.toString()+"Module";
        String databaseName = sensor.toString()+"Sensor";

        try {
            SensorRecordStructure structure = (SensorRecordStructure)Class.forName(structureClassName).getConstructor().newInstance();
            DatabaseLogger logger = new DatabaseLogger(databaseName, structure, context);
            logger.initializeLogging(null);
            SensorModule module = (SensorModule) Class.forName(moduleClassName).getConstructor(Context.class, PersistenceLogger.class, SensorRecordStructure.class).newInstance(context, logger, structure);
            SensorManagerStorage.instance().getSensorManager(SensorContainer.event).insertSensor(sensor, module);
            return module;
        } catch (Exception e) {
            Log.d("SensorFactory", e.toString());
        }
        return null;
    }

}
