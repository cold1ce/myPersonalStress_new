package com.fimrc.mysensornetwork.gui;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.SensorService;
import com.fimrc.mysensornetwork.sensors.SensorContainer;
import com.fimrc.sensorfusionframework.sensors.SensorModuleFactory;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener{

    private static boolean FIRST_START = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
            return;

        if(FIRST_START) {
            initialize();
            FIRST_START = false;
        }
        build();
    }

    private void initialize(){
        //Create Service
        Intent intent = new Intent(MainActivity.this, SensorService.class);
        startService(intent);
    }

    private void build(){
        ActionBar actionBar;
        ViewPager viewPager;

        viewPager = new ViewPager(this);
        viewPager.setId(R.id.pager);
        setContentView(viewPager);

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        TabsAdapter tabAdapter = new TabsAdapter(this, viewPager);
        tabAdapter.addTab(actionBar.newTab().setText("Event Sensors"), EventSensorFragment.class, null);
        tabAdapter.addTab(actionBar.newTab().setText("Time Sensors"), TimeSensorFragment.class, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void sendSensorActionToService(int action, int sensorNumber){
        Intent i = new Intent(MainActivity.this, SensorService.class);
        i.putExtra("Action", action);
        i.putExtra("Sensor", sensorNumber);
        startService(i);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Log.d("Permissions", "LocationPermissions granted");
        }
    }
}
