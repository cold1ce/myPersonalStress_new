package com.fimrc.sensorfusionframework.sensors;

import android.content.Context;
import android.util.Log;

import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;

/**
 * Created by Sven on 16.03.2017.
 */

public class SensorModuleFactory {

    public static SensorModule getSensorModule(String sensorType, String sensorName, Context context){
        if((!(sensorType.equals("event") || sensorType.equals("time"))) || sensorName == null){
            return null;
        }
        String structureClassName = "com.fimrc.mysensornetwork.sensors."+sensorType+"."+sensorName.toLowerCase()+"."+sensorName+"RecordStructure";
        String moduleClassName = "com.fimrc.mysensornetwork.sensors."+sensorType+"."+sensorName.toLowerCase()+"."+sensorName+"Module";
        String databaseName = sensorName+"Sensor";

        try {
            SensorRecordStructure structure = (SensorRecordStructure)Class.forName(structureClassName).getConstructor().newInstance();
            DatabaseLogger logger = new DatabaseLogger();
            logger.initialize(new Object[]{databaseName, structure, context});
            SensorModule module = (SensorModule) Class.forName(moduleClassName).getConstructor(Context.class, PersistenceLogger.class, SensorRecordStructure.class).newInstance(context, logger, structure);
            SensorManager.instance().insertSensor(module);
            return module;
        } catch (Exception e) {
            Log.d("SensorFactory", e.toString());
        }
        return null;
    }
}
