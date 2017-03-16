package com.fimrc.mysensornetwork.sensors.event.screen;

import android.content.Context;
import android.content.Intent;
import com.fimrc.sensorfusionframework.persistence.container.SensorRecord;
import com.fimrc.sensorfusionframework.sensors.SensorEventController;

import java.util.Date;

/**
 * Created by Sven on 22.02.2017.
 */

public class ScreenController extends SensorEventController{

    public ScreenController(ScreenModule module){
        super(module);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Date date = new Date(System.currentTimeMillis());
        SensorRecord record = new SensorRecord(module.getNextIndex(), date , structure);
        if(action.equals(Intent.ACTION_SCREEN_ON)){
            record.addData("event", "SCREEN ON");
        }else if(action.equals(Intent.ACTION_SCREEN_OFF)) {
            record.addData("event", "SCREEN OFF");
        }
        module.log(record);
    }
}
