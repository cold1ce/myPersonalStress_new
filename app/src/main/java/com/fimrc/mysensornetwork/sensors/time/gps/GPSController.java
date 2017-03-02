package com.fimrc.mysensornetwork.sensors.time.gps;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.sensors.SensorTimeController;

import java.util.Date;

/**
 * Created by Sven on 01.03.2017.
 */

public class GPSController extends SensorTimeController {

    private static final int INIT_GPS 	= 1;
    private static final int KILL_GPS 	= 2;

    // are these there?
    private boolean isKilled;
    private boolean finished = false;

    // sensor data
    private double Longitude = -1, Latitude = -1, Altitude = -1, Speed, Bearing;

    // for GPS
    private LocationManager manager;
    private LocationReceiver mReceiver;
    private HandlerThread mHandlerThread;
    private Handler mHandler;

    public GPSController(GPSModule module){
        super(module);
    }

    private boolean initialize(Context context){
        try{
            mReceiver = new LocationReceiver();
            manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            // check if there is some GPS
            try{
                LocationProvider gps = manager.getProvider(LocationManager.GPS_PROVIDER);
                if (gps != null){
                    mHandlerThread = new HandlerThread("GPSSensorThread");
                    mHandlerThread.start();
                    mHandler = new Handler(mHandlerThread.getLooper(), new GPSSensorHandler());
                    return true;
                }else
                    return false;
            }catch(Exception e){
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Date date = new Date(System.currentTimeMillis());
        SensorRecord record = new SensorRecord(module.getNextIndex(), date , structure);

        if(!initialize(context))
            return;

        mHandler.sendMessage(mHandler.obtainMessage(INIT_GPS));

        while(!finished){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mHandlerThread.quit();
        mHandlerThread = new HandlerThread("GPSSensorThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), new GPSSensorHandler());
        mHandler.sendMessage(mHandler.obtainMessage(KILL_GPS));

        while(!isKilled) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(Longitude != -1)
            record.addData("longitude", String.valueOf(Longitude));
        else
            record.addData("longitude", " ");

        if(Latitude != -1)
            record.addData("latitude", String.valueOf(Longitude));
        else
            record.addData("latitude", " ");

        if(Altitude != -1)
            record.addData("altitude", String.valueOf(Longitude));
        else
            record.addData("altitude", " ");

        if(Bearing != 0L)
            record.addData("bearing", String.valueOf(Longitude));
        else
            record.addData("bearing", " ");

        if(Speed != 0L)
            record.addData("speed", String.valueOf(Longitude));
        else
            record.addData("speed", " ");

        mHandlerThread.quit();

        module.log(record);
    }

    private class GPSSensorHandler implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case INIT_GPS:
                    try{
                        if (manager != null) {
                            mHandlerThread.quit();
                            mHandlerThread = new HandlerThread("GPSSensorThread");
                            mHandlerThread.start();

                            // request location updates
                            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, (float) 0, mReceiver, mHandlerThread.getLooper());
                            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, (float) 0, mReceiver, mHandlerThread.getLooper());
                        }
                    }catch(SecurityException e){
                        e.printStackTrace();
                    }
                    break;
                case KILL_GPS:
                    if (manager!=null)
                        try{
                            manager.removeUpdates(mReceiver);
                        }catch(SecurityException e){
                            e.printStackTrace();
                        }
                    isKilled = true;
                    break;
                default:
                    break;
            }

            return true;
        }
    }

    private class LocationReceiver implements LocationListener {
        public void	onLocationChanged(Location location) {
            if (location != null){
                Longitude 	= location.getLongitude();
                Latitude 	= location.getLatitude();
                Altitude 	= location.getAltitude();

                // don't do anything if we get a null reading for some reason
                if (Longitude == 0.0f && Latitude == 0.0f)
                    return;

                // any speed information?
                if (location.hasSpeed())
                    Speed		= (double)location.getSpeed();

                // any bearing information?
                if (location.hasBearing())
                    Bearing		= (double)location.getBearing();

                finished = true;
            }
        }

        public void onProviderDisabled(String provider) {

        }

        public void	onProviderEnabled(String provider) {

        }

        public void	onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}
