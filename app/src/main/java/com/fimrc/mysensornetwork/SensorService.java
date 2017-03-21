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
import com.fimrc.sensorfusionframework.sensors.SensorTimeModule;

/**
 * Created by Sven on 16.03.2017.
 */

public class SensorService extends Service {

    public static final int ACTIVATE_SENSOR = 0;
    public static final int DEACTIVATE_SENSOR = 1;
    public static final int START_LOGGING = 2;
    public static final int STOP_LOGGING = 3;

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
                case ACTIVATE_SENSOR:
                    if(SensorTimeModule.class.isAssignableFrom(SensorManager.instance().getSensor(msg.arg2).getClass()))
                        ((SensorTimeModule)SensorManager.instance().getSensor(msg.arg2)).activateTimeSensor(60);
                    else
                        SensorManager.instance().getSensor(msg.arg2).activateSensor();
                    break;
                case DEACTIVATE_SENSOR:
                    if(SensorManager.instance().getSensor(msg.arg2).getClass() == SensorTimeModule.class)
                        ((SensorTimeModule)SensorManager.instance().getSensor(msg.arg2)).deactivateTimeSensor();
                    else
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
}
