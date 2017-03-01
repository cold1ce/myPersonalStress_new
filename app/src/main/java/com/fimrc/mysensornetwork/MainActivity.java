package com.fimrc.mysensornetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.mysensornetwork.sensors.event.call.CallModule;
import com.fimrc.mysensornetwork.sensors.event.call.CallRecordStructure;
import com.fimrc.mysensornetwork.sensors.event.screen.ScreenModule;
import com.fimrc.mysensornetwork.sensors.event.screen.ScreenRecordStructure;
import com.fimrc.mysensornetwork.sensors.time.audio.AudioModule;
import com.fimrc.mysensornetwork.sensors.time.audio.AudioRecordStructure;
import com.fimrc.sensorfusionframework.sensors.SensorManager;

public class MainActivity extends AppCompatActivity {

    private static final int SCREEN_SENSOR = 0;
    private static final int CALL_SENSOR = 1;
    private static final int AUDIO_SENSOR = 2;
    private SensorManager sensorManager;
    private Button DatabasePrintButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("START");

        sensorManager = SensorManager.instance();
        sensorManager.insertSensor(createScreenSensor());
        sensorManager.insertSensor(createCallSensor());
        sensorManager.insertSensor(createAudioSensor());

        addListenerOnToggleButton();
        addListenerOnButton();
        //module.activateSensor();
    }

    public void addListenerOnToggleButton(){
        ToggleButton ScreenToggleButton = (ToggleButton) findViewById(R.id.ScreenSensorToggleButton);
        ScreenToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sensorManager.getSensor(SCREEN_SENSOR).activateSensor();
                    sensorManager.getSensor(SCREEN_SENSOR).startLogging();
                }else{
                    sensorManager.getSensor(SCREEN_SENSOR).stopLogging();
                    sensorManager.getSensor(SCREEN_SENSOR).deactivateSensor();
                }
            }
        });
        ToggleButton CallToggleButton = (ToggleButton) findViewById(R.id.CallSensorToggleButton);
        CallToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sensorManager.getSensor(CALL_SENSOR).activateSensor();
                    sensorManager.getSensor(CALL_SENSOR).startLogging();
                }else{
                    sensorManager.getSensor(CALL_SENSOR).stopLogging();
                    sensorManager.getSensor(CALL_SENSOR).deactivateSensor();
                }
            }
        });
        ToggleButton AudioToggleButton = (ToggleButton) findViewById(R.id.AudioSensorToggleButton);
        AudioToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sensorManager.getSensor(AUDIO_SENSOR).activateSensor();
                    sensorManager.getSensor(AUDIO_SENSOR).startLogging();
                }else{
                    sensorManager.getSensor(AUDIO_SENSOR).stopLogging();
                    sensorManager.getSensor(AUDIO_SENSOR).deactivateSensor();
                }
            }
        });
    }

    public void addListenerOnButton(){
        DatabasePrintButton = (Button) findViewById(R.id.printDatabase);
        DatabasePrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenModule module = (ScreenModule)sensorManager.getSensor(SCREEN_SENSOR);
                module.printDatabase();
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

}
