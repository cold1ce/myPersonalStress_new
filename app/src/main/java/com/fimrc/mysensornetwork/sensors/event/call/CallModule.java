package com.fimrc.mysensornetwork.sensors.event.call;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;

import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.sensorfusionframework.persistence.PersistenceLogger;
import com.fimrc.sensorfusionframework.persistence.structure.SensorRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorModule;

/**
 * Created by Sven on 23.02.2017.
 */

public class CallModule extends SensorModule {

    private CallController controller;
    private Context context;
    private DatabaseLogger logger;
    private CallRecordStructure structure;

    public CallModule(Context context, PersistenceLogger logger, SensorRecordStructure structure){
        super(logger, structure);
        this.context = context;
        this.logger = (DatabaseLogger)logger;
        this.structure = (CallRecordStructure)structure;
        controller = new CallController(this);
    }

    @Override
    public boolean activateSensor() {
        IntentFilter filterOutgoingCall = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        IntentFilter filterStateChanged = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        context.registerReceiver(controller, filterOutgoingCall);
        context.registerReceiver(controller, filterStateChanged);
        return true;
    }

    @Override
    public boolean deactivateSensor() {
        return false;
    }
}
