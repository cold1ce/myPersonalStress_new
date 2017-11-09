package com.fimrc.mysensornetwork.sensors.time.light;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;


import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.sensors.time.SensorTimeController;

import java.util.Date;


/**
 * Created by Sven on 02.03.2017.
 */

public class LightController extends SensorTimeController {

    private boolean initialized = false, startedLight = false;
    private float light;
    private SensorManager sensorManager;
    private Sensor Light;
    private Context context;

    HandlerThread mHandlerThread;
    Handler handler;

    public LightController(LightModule module, Context context){
        super(module);
        this.context = context;
    }


    @Override
    protected SensorRecord buildSensorRecord() {
        Date date = new Date(System.currentTimeMillis());
        SensorRecord record = new SensorRecord(module.getNextIndex(), date , structure);

        initialize(context);
        //Warten, bis sich der Sensorwert geändert hat und der aktuelle Sensorwert geholt wurde.
        while (!initialized || startedLight) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        record.addData("light", SensorDataType.INTEGER, light);

        mHandlerThread.quit();
        logSensorRecord(record);
        return null;
    }

    private void initialize(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mHandlerThread = new HandlerThread("LightSensorThread");
        mHandlerThread.start();
        handler = new Handler(mHandlerThread.getLooper());
        startedLight =  sensorManager.registerListener(lightsensorlistener, Light, SensorManager.SENSOR_DELAY_FASTEST, 0, handler);
        initialized = true;
    }

    private SensorEventListener lightsensorlistener = new SensorEventListener() {
        //Erfassen einer Änderung des Sensor um den aktuellen Wert zu speichern.
        public void onSensorChanged(SensorEvent event) {
            light = event.values[0];
            sensorManager.unregisterListener(lightsensorlistener, Light);
            sensorManager.flush(lightsensorlistener);
            startedLight = false;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

}
