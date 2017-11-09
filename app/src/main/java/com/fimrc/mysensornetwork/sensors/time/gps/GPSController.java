package com.fimrc.mysensornetwork.sensors.time.gps;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.fimrc.jdcf.persistence.container.SensorRecord;
import com.fimrc.jdcf.persistence.structure.SensorDataType;
import com.fimrc.jdcf.sensors.SensorModule;
import com.fimrc.jdcf.sensors.time.SensorTimeController;

import java.util.Date;

/**
 * Created by Sven on 21.03.2017.
 */

public class GPSController extends SensorTimeController {

    private SensorRecord record;
    private Context context;
    private boolean running;
    private GPSTask myTask;

    public GPSController(SensorModule module, Context context) {
        super(module);
        this.context = context;
        running = false;
    }

    public void finishTask(GPSTask task){
        Log.d("GPSController", "Task cancel on Thread: "+Thread.currentThread().getId());
        task.cancel(true);
        running = false;
    }

    @Override
    protected SensorRecord buildSensorRecord() {
        if(running)
            finishTask(myTask);
        myTask = new GPSTask();
        try{
            myTask.execute();
            running = true;
        }catch(Exception e) {
            running = false;
            e.printStackTrace();
        }
        while(running == true && myTask.getStatus() != AsyncTask.Status.FINISHED){}
        return record;
    }

    private class GPSTask extends AsyncTask<Void,Void, Void> implements LocationListener{


        private LocationManager gpsLocationManager;
        private LocationManager networkLocationManager;
        private boolean gpsDone;
        private boolean networkDone;
        private Looper backgroundLooper;

        @Override
        protected void onPreExecute() {
            record = null;
            gpsDone = false;
            networkDone = false;
            gpsLocationManager = (LocationManager) GPSController.this.context.getSystemService(Context.LOCATION_SERVICE);
            networkLocationManager = (LocationManager) GPSController.this.context.getSystemService(Context.LOCATION_SERVICE);
            //isGPSEnabled = gpslocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //isNetworkEnabled = networklocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Thread timerThread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        Log.d("GPS AsyncTask", "GPS Timeout 30 Seconds on Thread: "+Thread.currentThread().getId());
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    GPSController.this.finishTask(GPSTask.this);
                }
            });
            timerThread.start();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("GPS AsyncTask", "GPS doInBackground on Thread: "+Thread.currentThread().getId());
            Looper.prepare();
            backgroundLooper = Looper.myLooper();
            try {
                networkLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this, Looper.myLooper());
                gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, Looper.myLooper());
            }catch(SecurityException e){
                Log.d("GPSFailure", e.toString());
            }
            Looper.loop();
            return null;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d("GPS AsyncTask", "GPS onLocationChanged on Thread: "+Thread.currentThread().getId());
            Date date = new Date(System.currentTimeMillis());
            record = new SensorRecord(GPSController.this.module.getNextIndex(), date , GPSController.this.structure);
            record.addData("provider", SensorDataType.STRING, location.getProvider());
            record.addData("longitude", SensorDataType.DOUBLE, location.getLatitude());
            record.addData("latitude", SensorDataType.DOUBLE, location.getLatitude());
            record.addData("altitude", SensorDataType.DOUBLE, location.getAltitude());
            record.addData("bearing", SensorDataType.DOUBLE, location.getBearing());
            record.addData("speed", SensorDataType.DOUBLE, location.getSpeed());
            if(location.getProvider().equals("gps")) {
                gpsLocationManager.removeUpdates(this);
                gpsDone = true;
            }else {
                networkLocationManager.removeUpdates(this);
                networkDone = true;
            }
            if(gpsDone && networkDone){
                backgroundLooper.quit();
                GPSController.this.finishTask(this);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("GPS AsyncTask", "GPS onStatusChanged on Thread: "+Thread.currentThread().getId());
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("GPS AsyncTask", "GPS onProviderEnabled on Thread: "+Thread.currentThread().getId());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("GPS AsyncTask", "GPS onProviderDisabled on Thread: "+Thread.currentThread().getId());
        }
    }
}
