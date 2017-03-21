package com.fimrc.mysensornetwork.sensors.time.gps;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.sensors.SensorModule;
import com.fimrc.sensorfusionframework.sensors.SensorTimeController;

import java.util.Date;

/**
 * Created by Sven on 21.03.2017.
 */

public class GPSController extends SensorTimeController {

    private Context context;
    private boolean running;
    private GPSTask myTask;

    public GPSController(SensorModule module, Context context) {
        super(module);
        this.context = context;
        running = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GPSController", "onReceive started");
        if(running)
            finishTask(myTask);
        myTask = new GPSTask();
        myTask.execute();
        running = true;
    }


    public void logSensorRecord(SensorRecord record){
        module.log(record);
    }
    public void finishTask(GPSTask task){
        Log.d("GPSController", "Task cancel");
        task.cancel(true);
        running = false;
    }

    private class GPSTask extends AsyncTask<Void,Void, Void> implements LocationListener{

        private SensorRecord record;
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
        }

        @Override
        protected Void doInBackground(Void... params) {
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
            Date date = new Date(System.currentTimeMillis());
            record = new SensorRecord(GPSController.this.module.getNextIndex(), date , GPSController.this.structure);
            record.addData("provider", location.getProvider());
            record.addData("longitude", location.getLatitude());
            record.addData("latitude", location.getLatitude());
            record.addData("altitude", location.getAltitude());
            record.addData("bearing", location.getBearing());
            record.addData("speed", location.getSpeed());
            GPSController.this.logSensorRecord(record);
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

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
