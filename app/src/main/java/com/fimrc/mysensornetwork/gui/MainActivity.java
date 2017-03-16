package com.fimrc.mysensornetwork.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.SensorService;
import com.fimrc.sensorfusionframework.sensors.SensorModuleFactory;

import static com.fimrc.mysensornetwork.SensorService.ACTIVATE_SENSOR;
import static com.fimrc.mysensornetwork.SensorService.AUDIO_SENSOR;
import static com.fimrc.mysensornetwork.SensorService.CALL_SENSOR;
import static com.fimrc.mysensornetwork.SensorService.CELL_SENSOR;
import static com.fimrc.mysensornetwork.SensorService.DEACTIVATE_SENSOR;
import static com.fimrc.mysensornetwork.SensorService.GPS_SENSOR;
import static com.fimrc.mysensornetwork.SensorService.LIGHT_SENSOR;
import static com.fimrc.mysensornetwork.SensorService.SCREEN_SENSOR;
import static com.fimrc.mysensornetwork.SensorService.START_LOGGING;
import static com.fimrc.mysensornetwork.SensorService.STOP_LOGGING;


public class MainActivity extends AppCompatActivity {

    private Switch ScreenSensorSwitch;
    private Switch CallSensorSwitch;
    private Switch AudioSensorSwitch;
    private Switch GPSSensorSwitch;
    private Switch LightSensorSwitch;
    private Switch CellSensorSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("START");
        Log.d("ActivityThread", String.valueOf(Thread.currentThread().getId()));
        Intent i = new Intent(MainActivity.this, SensorService.class);
        startService(i);

        ScreenSensorSwitch = (Switch) findViewById(R.id.ScreenSensorSwitch);
        CallSensorSwitch = (Switch) findViewById(R.id.CallSensorSwitch);
        AudioSensorSwitch = (Switch) findViewById(R.id.AudioSensorSwitch);
        GPSSensorSwitch = (Switch) findViewById(R.id.GPSSensorSwitch);
        LightSensorSwitch = (Switch) findViewById(R.id.LightSensorSwitch);
        CellSensorSwitch = (Switch) findViewById(R.id.CellSensorSwitch);

        SensorModuleFactory.getSensorModule("event", "Screen", this);
        SensorModuleFactory.getSensorModule("event", "Call", this);
        SensorModuleFactory.getSensorModule("time", "Audio", this);
        SensorModuleFactory.getSensorModule("time", "GPS", this);
        SensorModuleFactory.getSensorModule("time", "Light", this);
        SensorModuleFactory.getSensorModule("time", "Cell", this);

        addListenerOnSwitchButton();

    }

    private void sendSensorActionToService(int action, int sensorNumber){
        Intent i = new Intent(MainActivity.this, SensorService.class);
        i.putExtra("Action", action);
        i.putExtra("Sensor", sensorNumber);
        startService(i);
    }

    public void addListenerOnSwitchButton(){
        ScreenSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, SCREEN_SENSOR);
                    sendSensorActionToService(START_LOGGING, SCREEN_SENSOR);
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, SCREEN_SENSOR);
                    sendSensorActionToService(STOP_LOGGING, SCREEN_SENSOR);
                }
            }
        });
        CallSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, CALL_SENSOR);
                    sendSensorActionToService(START_LOGGING, CALL_SENSOR);
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, CALL_SENSOR);
                    sendSensorActionToService(STOP_LOGGING, CALL_SENSOR);
                }
            }
        });

        AudioSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, AUDIO_SENSOR);
                    sendSensorActionToService(START_LOGGING, AUDIO_SENSOR);
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, AUDIO_SENSOR);
                    sendSensorActionToService(STOP_LOGGING, AUDIO_SENSOR);
                }
            }
        });

        GPSSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, GPS_SENSOR);
                    sendSensorActionToService(START_LOGGING, GPS_SENSOR);
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, GPS_SENSOR);
                    sendSensorActionToService(STOP_LOGGING, GPS_SENSOR);
                }
            }
        });

        LightSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, LIGHT_SENSOR);
                    sendSensorActionToService(START_LOGGING, LIGHT_SENSOR);
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, LIGHT_SENSOR);
                    sendSensorActionToService(STOP_LOGGING, LIGHT_SENSOR);
                }
            }
        });

        CellSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, CELL_SENSOR);
                    sendSensorActionToService(START_LOGGING, CELL_SENSOR);
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, CELL_SENSOR);
                    sendSensorActionToService(STOP_LOGGING, CELL_SENSOR);
                }
            }
        });
    }

}
