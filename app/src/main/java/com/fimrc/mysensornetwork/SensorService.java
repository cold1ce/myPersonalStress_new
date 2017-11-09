package com.fimrc.mysensornetwork;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.fimrc.jdcf.sensors.SensorManager;
import com.fimrc.mysensornetwork.sensors.SensorContainer;
import com.fimrc.mysensornetwork.sensors.SensorManagerStorage;
import com.fimrc.mysensornetwork.sensors.SensorFactory;
import com.fimrc.mysensornetwork.sensors.event.SensorEventContainer;
import com.fimrc.mysensornetwork.sensors.time.SensorTimeContainer;

/**
 * Created by Sven on 16.03.2017.
 */

public class SensorService extends Service {

    private final IBinder sensorBinder = new MyLocalBinder();

    public static final int ACTIVATE_SENSOR = 0;
    public static final int DEACTIVATE_SENSOR = 1;
    public static final int START_LOGGING = 2;
    public static final int STOP_LOGGING = 3;

    private static boolean FIRST_START = true;

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            Intent intent = (Intent)msg.obj;
            if(intent.getExtras() != null) {
                Bundle bundle = intent.getExtras();
                SensorContainer sensorType = (SensorContainer) bundle.get("Type");
                Object module;
                if (sensorType == SensorContainer.event) {
                    module = bundle.get("Sensor");
                } else {
                    module = bundle.get("Sensor");
                }
                int action = (int) bundle.get("Action");
                switch (action) {
                    case ACTIVATE_SENSOR:
                        SensorManagerStorage.instance().getSensorManager(sensorType).getSensor((Enum)module).activateSensor();
                        break;
                    case DEACTIVATE_SENSOR:
                        SensorManagerStorage.instance().getSensorManager(sensorType).getSensor((Enum)module).deactivateSensor();
                        break;
                    case START_LOGGING:
                        SensorManagerStorage.instance().getSensorManager(sensorType).getSensor((Enum)module).startLogging();
                        break;
                    case STOP_LOGGING:
                        SensorManagerStorage.instance().getSensorManager(sensorType).getSensor((Enum)module).stopLogging();
                        break;
                    default:
                        break;
                }
            }
            stopSelf(msg.what);
        }
    }

    @Override
    public void onCreate() {
        Log.d("SensorService", "onCreate");
        HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

        /*
        myThread = new MyThread();
        myThread.start();
        */
        if(FIRST_START) {

            SensorManagerStorage.instance().insertManager(SensorContainer.event, new SensorManager());
            SensorManagerStorage.instance().insertManager(SensorContainer.time, new SensorManager());
            //Create Sensors
            for(SensorEventContainer element : SensorEventContainer.values()){
                SensorFactory.getSensorModule(element, this);
            }
            for(SensorTimeContainer element : SensorTimeContainer.values()){
                SensorFactory.getSensorModule(element, this);
            }
            Log.d("TEST",String.valueOf(SensorManagerStorage.instance().getSensorManager(SensorContainer.event).getCountSensors()));
            Log.d("TEST",String.valueOf(SensorManagerStorage.instance().getSensorManager(SensorContainer.time).getCountSensors()));
            FIRST_START = false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SensorService", "onStartCommand");
        Message msg = serviceHandler.obtainMessage();
        msg.what = startId;
        msg.obj = intent;
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    public void sendSensorAction(Intent intent){
        Log.d("SensorService", "onStartCommand");
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = intent.getIntExtra("Action", -1);
        msg.arg2 = intent.getIntExtra("Sensor", -1);
        serviceHandler.sendMessage(msg);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sensorBinder;
    }

    @Override
    public void onDestroy() {
        Log.d("SensorService", "onDestroy");
    }


    public class MyLocalBinder extends Binder{
        public SensorService getService(){
            return SensorService.this;
        }
    }

}
