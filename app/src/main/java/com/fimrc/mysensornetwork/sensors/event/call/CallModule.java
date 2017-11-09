package com.fimrc.mysensornetwork.sensors.event.call;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;

import com.fimrc.jdcf.persistence.PersistenceLogger;
import com.fimrc.jdcf.persistence.structure.SensorRecordStructure;
import com.fimrc.jdcf.sensors.event.SensorEventModule;


/**
 * Created by Sven on 23.02.2017.
 */

public class CallModule extends SensorEventModule {

    private Context context;

    public CallModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(logger, structure);
        controller = new CallController(this);
        this.context = context;
    }

    @Override
    public boolean activateSensor() {
        IntentFilter filterOutgoingCall = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        IntentFilter filterStateChanged = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        context.registerReceiver(((CallController)controller).mBroadcastReceiver, filterOutgoingCall);
        context.registerReceiver(((CallController)controller).mBroadcastReceiver, filterStateChanged);
        return super.activateSensor();
    }

    @Override
    public boolean deactivateSensor() {
        context.unregisterReceiver(((CallController)controller).mBroadcastReceiver);
        return super.deactivateSensor();
    }
}
