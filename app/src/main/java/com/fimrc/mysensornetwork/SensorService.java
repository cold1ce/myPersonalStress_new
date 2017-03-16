package com.fimrc.mysensornetwork;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.mysensornetwork.sensors.event.call.CallModule;
import com.fimrc.mysensornetwork.sensors.event.call.CallRecordStructure;
import com.fimrc.mysensornetwork.sensors.event.screen.ScreenModule;
import com.fimrc.mysensornetwork.sensors.event.screen.ScreenRecordStructure;
import com.fimrc.mysensornetwork.sensors.time.audio.AudioModule;
import com.fimrc.mysensornetwork.sensors.time.audio.AudioRecordStructure;
import com.fimrc.mysensornetwork.sensors.time.cell.CellModule;
import com.fimrc.mysensornetwork.sensors.time.cell.CellRecordStructure;
import com.fimrc.mysensornetwork.sensors.time.gps.GPSModule;
import com.fimrc.mysensornetwork.sensors.time.gps.GPSRecordStructure;
import com.fimrc.mysensornetwork.sensors.time.light.LightModule;
import com.fimrc.mysensornetwork.sensors.time.light.LightRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorManager;

/**
 * Created by Sven on 16.03.2017.
 */

public class SensorService extends Service {

    public static final int CREATE = 0;
    public static final int ACTIVATE_SENSOR = 1;
    public static final int DEACTIVATE_SENSOR = 2;
    public static final int START_LOGGING = 3;
    public static final int STOP_LOGGING = 4;

    public static final int SCREEN_SENSOR = 0;
    public static final int CALL_SENSOR = 1;
    public static final int AUDIO_SENSOR = 2;
    public static final int GPS_SENSOR = 3;
    public static final int LIGHT_SENSOR = 4;
    public static final int CELL_SENSOR = 5;

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            Log.d("handleMessage", "arg1: "+msg.arg1+" arg2: "+msg.arg2);
            Log.d("handleMessageThread", String.valueOf(Thread.currentThread().getId()));
            switch(msg.arg1){
                case CREATE:
                    switch(msg.arg2){
                        /*case SCREEN_SENSOR:
                            createScreenSensor();
                            break;
                        case CALL_SENSOR:
                            createCallSensor();
                            break;
                        case AUDIO_SENSOR:
                            createAudioSensor();
                            break;
                        case GPS_SENSOR:
                            createGPSSensor();
                            break;
                        case LIGHT_SENSOR:
                            createLightSensor();
                            break;
                        case CELL_SENSOR:
                            createCellSensor();
                            break;*/
                        default:
                            break;
                    }
                    break;
                case ACTIVATE_SENSOR:
                    SensorManager.instance().getSensor(msg.arg2).activateSensor();
                    break;
                case DEACTIVATE_SENSOR:
                    SensorManager.instance().getSensor(msg.arg2).deactivateSensor();
                    break;
                case START_LOGGING:
                    SensorManager.instance().getSensor(msg.arg2).startLogging();
                    break;
                case STOP_LOGGING:
                    SensorManager.instance().getSensor(msg.arg2).stopLogging();
                    break;
                default:
                    break;
            }
            stopSelf(msg.what);
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SensorService", "onStartCommand");
        Message msg = serviceHandler.obtainMessage();
        msg.what = startId;
        msg.arg1 = intent.getIntExtra("Action", -1);
        msg.arg2 = intent.getIntExtra("Sensor", -1);
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("SensorService", "onDestroy");
    }
/*
    public void createScreenSensor() {
        ScreenRecordStructure structure = new ScreenRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"ScreenSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        SensorManager.instance().insertSensor(new ScreenModule(this.getBaseContext(), logger, structure));
    }

    public void createCallSensor() {
        CallRecordStructure structure = new CallRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"CallSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        SensorManager.instance().insertSensor(new CallModule(this.getBaseContext(), logger, structure));
    }

    public void createAudioSensor(){
        AudioRecordStructure structure = new AudioRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"AudioSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        SensorManager.instance().insertSensor(new AudioModule(this.getBaseContext(), logger, structure));
    }

    public void createGPSSensor(){
        GPSRecordStructure structure = new GPSRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"GPSSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        SensorManager.instance().insertSensor(new GPSModule(this.getBaseContext(), logger, structure));
    }

    public void createLightSensor(){
        LightRecordStructure structure = new LightRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"LightSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        SensorManager.instance().insertSensor(new LightModule(this.getBaseContext(), logger, structure));
    }

    public void createCellSensor(){
        CellRecordStructure structure = new CellRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"CellSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        SensorManager.instance().insertSensor(new CellModule(this.getBaseContext(), logger, structure));
    }
*/
}
