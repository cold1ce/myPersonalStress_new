package com.fimrc.mysensornetwork.sensors.time.gps;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.sensors.SensorTimeController;

import java.util.Date;

/**
* Created by Sven on 01.03.2017.
*/

public class GPSController extends SensorTimeController {

    // The minimum distance to change Updates in meters
    private volatile static long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    // The minimum time between updates in milliseconds
    private volatile static long MIN_TIME_BW_UPDATES = 0;
    private volatile boolean isGPSEnabled = false;
    private volatile boolean isNetworkEnabled = false;
    private volatile double latitude = -1;
    private volatile double longitude = -1;
    private volatile double altitude = -1;
    private volatile double speed = -1;
    private volatile double bearing = -1;
    private volatile LocationManager locationManager;
    private volatile GPSLocationTask backgroundTask;

    public GPSController(GPSModule module, Context context) {
        super(module);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    private void SensorDataFinished() {
        Date date = new Date(System.currentTimeMillis());
        SensorRecord record = new SensorRecord(module.getNextIndex(), date, structure);
        if (longitude != -1)
            record.addData("longitude", String.valueOf(longitude));
        else
            record.addData("longitude", " ");

        if (latitude != -1)
            record.addData("latitude", String.valueOf(longitude));
        else
            record.addData("latitude", " ");

        if (altitude != -1)
            record.addData("altitude", String.valueOf(altitude));
        else
            record.addData("altitude", " ");

        if (bearing != -1)
            record.addData("bearing", String.valueOf(bearing));
        else
            record.addData("bearing", " ");

        if (speed != -1)
            record.addData("speed", String.valueOf(speed));
        else
            record.addData("speed", " ");

        module.log(record);

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        latitude = -1;
        longitude = -1;
        altitude = -1;
        speed = -1;
        bearing = -1;
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (backgroundTask != null && (backgroundTask.getStatus() == AsyncTask.Status.RUNNING || backgroundTask.getStatus() == AsyncTask.Status.PENDING)){
            backgroundTask.cancel(true);
            backgroundTask = new GPSLocationTask();
        } else {
            backgroundTask = new GPSLocationTask();
        }
        backgroundTask.execute();
    }

    private class GPSLocationTask extends AsyncTask<Void, Void, Void> implements LocationListener {

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("THREAD STARTED");
            Looper.myLooper().prepare();
                try {
                    if (isNetworkEnabled)
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (isGPSEnabled)
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {
            try {
                locationManager.removeUpdates(this);
            }catch(SecurityException e){
                e.printStackTrace();
            }
            SensorDataFinished();
        }

        @Override
        protected void onCancelled(Void nothing) {
            try {
                locationManager.removeUpdates(this);
            }catch(SecurityException e){
                e.printStackTrace();
            }
            SensorDataFinished();
        }

        public void onLocationChanged(Location location) {
            System.out.println("---onLocationChanged called---");
            if (location != null) {
                System.out.println("Location != null");
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                altitude = location.getAltitude();
                // any speed information?
                if (location.hasSpeed())
                    speed = (double) location.getSpeed();
                // any bearing information?
                if (location.hasBearing())
                    bearing = (double) location.getBearing();

            }
        }

        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}