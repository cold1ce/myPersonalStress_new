package com.fimrc.mysensornetwork.sensors.event.call;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.sensors.SensorEventController;

import java.util.Date;

/**
 * Created by Sven on 23.02.2017.
 */

public class CallController extends SensorEventController{

    public CallController(CallModule module){
        super(module);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Date date = new Date(System.currentTimeMillis());
        SensorRecord record = new SensorRecord(module.getNextIndex(), date , structure);
        String callee = null;
        String caller = null;
        if(action.equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            try{
                callee = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            }catch(Exception e){
                callee = "unknown:---";
            }
        }
        if(TelephonyManager.ACTION_PHONE_STATE_CHANGED.compareTo(intent.getAction()) == 0){
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)){
                try{
                    caller = new String(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
                }catch(Exception e){
                    caller = new String("unknown:---");
                }
            }
        }
        record.addData("callee",callee);
        record.addData("caller",caller);
        module.log(record);
    }
}
