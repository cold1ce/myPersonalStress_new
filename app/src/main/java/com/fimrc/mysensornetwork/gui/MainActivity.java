package com.fimrc.mysensornetwork.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.SensorService;
import com.fimrc.mysensornetwork.sensors.SensorContainer;
import com.fimrc.sensorfusionframework.sensors.SensorModuleFactory;

import static com.fimrc.mysensornetwork.SensorService.ACTIVATE_SENSOR;
import static com.fimrc.mysensornetwork.SensorService.DEACTIVATE_SENSOR;
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
        Intent intent = new Intent(MainActivity.this, SensorService.class);
        startService(intent);

        ScreenSensorSwitch = (Switch) findViewById(R.id.ScreenSensorSwitch);
        CallSensorSwitch = (Switch) findViewById(R.id.CallSensorSwitch);
        AudioSensorSwitch = (Switch) findViewById(R.id.AudioSensorSwitch);
        GPSSensorSwitch = (Switch) findViewById(R.id.GPSSensorSwitch);
        LightSensorSwitch = (Switch) findViewById(R.id.LightSensorSwitch);
        CellSensorSwitch = (Switch) findViewById(R.id.CellSensorSwitch);

        for(int i=0; i<SensorContainer.getNumberOfSensors(); i++){
            if(SensorContainer.getSensor(i).eventSensor != null)
                SensorModuleFactory.getSensorModule(SensorContainer.getSensor(i).eventSensor, this);
            SensorModuleFactory.getSensorModule(SensorContainer.getSensor(i).timeSensor, this);
        }

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
                    sendSensorActionToService(ACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.EventSensors.Screen));
                    sendSensorActionToService(START_LOGGING, SensorContainer.getSensorIndex(SensorContainer.EventSensors.Screen));
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.EventSensors.Screen));
                    sendSensorActionToService(STOP_LOGGING, SensorContainer.getSensorIndex(SensorContainer.EventSensors.Screen));
                }
            }
        });
        CallSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.EventSensors.Call));
                    sendSensorActionToService(START_LOGGING, SensorContainer.getSensorIndex(SensorContainer.EventSensors.Call));
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.EventSensors.Call));
                    sendSensorActionToService(STOP_LOGGING, SensorContainer.getSensorIndex(SensorContainer.EventSensors.Call));
                }
            }
        });

        AudioSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Audio));
                    sendSensorActionToService(START_LOGGING, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Audio));
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Audio));
                    sendSensorActionToService(STOP_LOGGING, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Audio));
                }
            }
        });

        GPSSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.GPS));
                    sendSensorActionToService(START_LOGGING, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.GPS));
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.GPS));
                    sendSensorActionToService(STOP_LOGGING, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.GPS));
                }
            }
        });

        LightSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Light));
                    sendSensorActionToService(START_LOGGING, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Light));
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Light));
                    sendSensorActionToService(STOP_LOGGING, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Light));
                }
            }
        });

        CellSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sendSensorActionToService(ACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Cell));
                    sendSensorActionToService(START_LOGGING, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Cell));
                }else{
                    sendSensorActionToService(DEACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Cell));
                    sendSensorActionToService(STOP_LOGGING, SensorContainer.getSensorIndex(SensorContainer.TimeSensors.Cell));
                }
            }
        });
    }

}
