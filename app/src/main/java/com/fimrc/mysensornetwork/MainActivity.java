package com.fimrc.mysensornetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.mysensornetwork.sensors.event.call.CallModule;
import com.fimrc.mysensornetwork.sensors.event.call.CallRecordStructure;
import com.fimrc.mysensornetwork.sensors.event.screen.ScreenModule;
import com.fimrc.mysensornetwork.sensors.event.screen.ScreenRecordStructure;
import com.fimrc.mysensornetwork.sensors.time.audio.AudioModule;
import com.fimrc.mysensornetwork.sensors.time.audio.AudioRecordStructure;
import com.fimrc.mysensornetwork.sensors.time.gps.GPSModule;
import com.fimrc.mysensornetwork.sensors.time.gps.GPSRecordStructure;
import com.fimrc.mysensornetwork.sensors.time.light.LightModule;
import com.fimrc.mysensornetwork.sensors.time.light.LightRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorManager;

public class MainActivity extends AppCompatActivity {

    private static final int SCREEN_SENSOR = 0;
    private static final int CALL_SENSOR = 1;
    private static final int AUDIO_SENSOR = 2;
    private static final int GPS_SENSOR = 3;
    private static final int LIGHT_SENSOR = 4;
    private SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("START");


        //Reihenfolge wichtig für die globalen SensorVariablen (SCREEN_SENSOR,...)
        sensorManager = SensorManager.instance();
        sensorManager.insertSensor(createScreenSensor());
        sensorManager.insertSensor(createCallSensor());
        sensorManager.insertSensor(createAudioSensor());
        sensorManager.insertSensor(createGPSSensor());
        sensorManager.insertSensor(createLightSensor());

        addListenerOnSwitchButton();
    }

    public void addListenerOnSwitchButton(){
        Switch ScreenSensorSwitch = (Switch) findViewById(R.id.ScreenSensorSwitch);
        ScreenSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sensorManager.getSensor(SCREEN_SENSOR).activateSensor();
                    sensorManager.getSensor(SCREEN_SENSOR).startLogging();
                }else{
                    sensorManager.getSensor(SCREEN_SENSOR).deactivateSensor();
                    sensorManager.getSensor(SCREEN_SENSOR).stopLogging();
                }
            }
        });
        Switch CallSensorSwitch = (Switch) findViewById(R.id.CallSensorSwitch);
        CallSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sensorManager.getSensor(CALL_SENSOR).activateSensor();
                    sensorManager.getSensor(CALL_SENSOR).startLogging();
                }else{
                    sensorManager.getSensor(CALL_SENSOR).deactivateSensor();
                    sensorManager.getSensor(CALL_SENSOR).stopLogging();
                }
            }
        });
        Switch AudioSensorSwitch = (Switch) findViewById(R.id.AudioSensorSwitch);
        AudioSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sensorManager.getSensor(AUDIO_SENSOR).activateSensor();
                    sensorManager.getSensor(AUDIO_SENSOR).startLogging();
                }else{
                    sensorManager.getSensor(AUDIO_SENSOR).deactivateSensor();
                    sensorManager.getSensor(AUDIO_SENSOR).stopLogging();
                }
            }
        });
        Switch GPSSensorSwitch = (Switch) findViewById(R.id.GPSSensorSwitch);
        GPSSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sensorManager.getSensor(GPS_SENSOR).activateSensor();
                    sensorManager.getSensor(GPS_SENSOR).startLogging();
                }else{
                    sensorManager.getSensor(GPS_SENSOR).deactivateSensor();
                    sensorManager.getSensor(GPS_SENSOR).stopLogging();
                }
            }
        });
        Switch LightSensorSwitch = (Switch) findViewById(R.id.LightSensorSwitch);
        LightSensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sensorManager.getSensor(LIGHT_SENSOR).activateSensor();
                    sensorManager.getSensor(LIGHT_SENSOR).startLogging();
                }else{
                    sensorManager.getSensor(LIGHT_SENSOR).deactivateSensor();
                    sensorManager.getSensor(LIGHT_SENSOR).stopLogging();
                }
            }
        });
    }

    public ScreenModule createScreenSensor() {
        ScreenRecordStructure structure = new ScreenRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"ScreenSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        ScreenModule module = new ScreenModule(this.getBaseContext(), logger, structure);
        return module;
    }

    public CallModule createCallSensor() {
        CallRecordStructure structure = new CallRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"CallSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        CallModule module = new CallModule(this.getBaseContext(), logger, structure);
        return module;
    }

    public AudioModule createAudioSensor(){
        AudioRecordStructure structure = new AudioRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"AudioSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        AudioModule module = new AudioModule(this.getBaseContext(), logger, structure);
        return module;
    }

    public GPSModule createGPSSensor(){
        GPSRecordStructure structure = new GPSRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"GPSSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        GPSModule module = new GPSModule(this.getBaseContext(), logger, structure);
        return module;
    }

    public LightModule createLightSensor(){
        LightRecordStructure structure = new LightRecordStructure();
        DatabaseLogger logger = new DatabaseLogger();
        Object[] array = {"LightSensor", structure, this.getBaseContext()};
        logger.initialize(array);
        LightModule module = new LightModule(this.getBaseContext(), logger, structure);
        return module;
    }

}
