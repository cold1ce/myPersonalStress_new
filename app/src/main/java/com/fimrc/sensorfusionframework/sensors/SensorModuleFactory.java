package com.fimrc.sensorfusionframework.sensors;

import android.content.Context;
import android.util.Log;

import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.mysensornetwork.sensors.SensorContainer;
import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

/**
 * Created by Sven on 16.03.2017.
 */

public class SensorModuleFactory {

    public static SensorModule getSensorModule(SensorContainer.TimeSensors sensor, Context context){
        if(sensor == null)
            return null;
        String structureClassName = "com.fimrc.mysensornetwork.sensors.time."+sensor.toString().toLowerCase()+"."+sensor.toString()+"RecordStructure";
        String moduleClassName = "com.fimrc.mysensornetwork.sensors.time."+sensor.toString().toLowerCase()+"."+sensor.toString()+"Module";
        String databaseName = sensor.toString()+"Sensor";

        try {
            SensorRecordStructure structure = (SensorRecordStructure)Class.forName(structureClassName).getConstructor().newInstance();
            DatabaseLogger logger = new DatabaseLogger();
            logger.initialize(new Object[]{databaseName, structure, context});
            SensorTimeModule module = (SensorTimeModule) Class.forName(moduleClassName).getConstructor(Context.class, PersistenceLogger.class, SensorRecordStructure.class, String.class).newInstance(context, logger, structure, databaseName);
            SensorManager.instance().insertSensor(module);
            return module;
        } catch (Exception e) {
            Log.d("SensorFactory", e.toString());
        }
        return null;
    }

    public static SensorModule getSensorModule(SensorContainer.EventSensors sensor, Context context){
        if(sensor == null)
            return null;
        String structureClassName = "com.fimrc.mysensornetwork.sensors.event."+sensor.toString().toLowerCase()+"."+sensor.toString()+"RecordStructure";
        String moduleClassName = "com.fimrc.mysensornetwork.sensors.event."+sensor.toString().toLowerCase()+"."+sensor.toString()+"Module";
        String databaseName = sensor.toString()+"Sensor";

        try {
            SensorRecordStructure structure = (SensorRecordStructure)Class.forName(structureClassName).getConstructor().newInstance();
            DatabaseLogger logger = new DatabaseLogger();
            logger.initialize(new Object[]{databaseName, structure, context});
            SensorModule module = (SensorModule) Class.forName(moduleClassName).getConstructor(Context.class, PersistenceLogger.class, SensorRecordStructure.class, String.class).newInstance(context, logger, structure, databaseName);
            SensorManager.instance().insertSensor(module);
            return module;
        } catch (Exception e) {
            Log.d("SensorFactory", e.toString());
        }
        return null;
    }

}
