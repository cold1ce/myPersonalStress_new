package com.fimrc.mysensornetwork.sensors.time.gps;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.sensors.SensorTimeController;

import java.util.Date;

/**
 * Created by Sven on 09.03.2017.
 */

public class GPSControllerT extends SensorTimeController {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;
    //private GPSThread backgroundThread;
    private volatile LocationManager locationManager;
    private volatile boolean isGPSEnabled = false;
    private volatile boolean isNetworkEnabled = false;
    private volatile boolean keepRunning = false;
    private volatile double latitude = -1;
    private volatile double longitude = -1;
    private volatile double altitude = -1;
    private volatile double speed = -1;
    private volatile double bearing = -1;

    public GPSControllerT(GPSModule module, Context context) {
        super(module);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
/*
    @Override
    public void onReceive(Context context, Intent intent) {
        latitude = -1;
        longitude = -1;
        altitude = -1;
        speed = -1;
        bearing = -1;
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (backgroundThread == null)
            backgroundThread = new GPSThread();
        if(backgroundThread.isAlive())
            backgroundThread.interrupt();
        keepRunning = true;
        backgroundThread.start();
    }
*/


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
        new Thread ( new Runnable(){
            public void run(){
                Looper.myLooper().prepare();
                LocListener locationListener = new LocListener();
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener, Looper.myLooper());
                }catch(SecurityException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public class LocListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location)
        {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();
            // any speed information?
            if (location.hasSpeed())
                speed = (double) location.getSpeed();
            // any bearing information?
            if (location.hasBearing())
                bearing = (double) location.getBearing();
            SensorDataFinished();
        }

        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }


    /*

    private class GPSThread extends Thread implements Runnable, LocationListener {

        @Override
        public void run() {
            System.out.println("THREAD STARTED");
            Thread.yield();
            Looper.myLooper().prepare();
            Looper.myLooper().loop();
            try {
                if (isNetworkEnabled)
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.myLooper());
                if (isGPSEnabled)
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this, Looper.myLooper());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            while(keepRunning){
                try{
                    Thread.sleep(3000);
                }catch(InterruptedException e){
                    try {
                        locationManager.removeUpdates(this);
                    }catch(SecurityException e2){
                        e2.printStackTrace();
                    }
                }
            }
            SensorDataFinished();
        }

        @Override
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
            keepRunning = false;
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
    */
}
