package com.fimrc.mysensornetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.fimrc.mysensornetwork.persistence.DatabaseLogger;
import com.fimrc.mysensornetwork.sensors.screen.ScreenModule;
import com.fimrc.mysensornetwork.sensors.screen.ScreenRecordStructure;

public class MainActivity extends AppCompatActivity {

    private ToggleButton ScreenToggleButton;
    private Button DatabasePrintButton;
    private ScreenModule module;
    private ScreenRecordStructure structure;
    private DatabaseLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("START");
        //SensorManager androidSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        logger = new DatabaseLogger();
        logger.initialize(this.getBaseContext());
        structure = new ScreenRecordStructure();
        module = new ScreenModule(this.getBaseContext(), logger, structure);
        module.startLogging();

        addListenerOnToggleButton();
        addListenerOnButton();
        //module.activateSensor();

    }

    public void addListenerOnToggleButton(){
        ScreenToggleButton = (ToggleButton) findViewById(R.id.ScreenToggleButton);
        ScreenToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    module.activateSensor();
                }else{
                    module.deactivateSensor();
                }
            }
        });
    }

    public void addListenerOnButton(){
        DatabasePrintButton = (Button) findViewById(R.id.printDatabase);
        DatabasePrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logger.print();
            }
        });
    }

}
