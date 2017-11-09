package com.fimrc.mysensornetwork.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fimrc.mypersonalstress.gui.Testfield;
import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.SensorService;
import com.fimrc.mysensornetwork.sensors.SensorContainer;
import com.fimrc.mysensornetwork.sensors.event.SensorEventContainer;
import com.fimrc.mysensornetwork.sensors.time.SensorTimeContainer;


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


        Intent myIntent2 = new Intent(MainActivity.this, Testfield.class);
        //myIntent2.putExtra("key", value); //Optional parameters
        MainActivity.this.startActivity(myIntent2);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lay, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.personalization:
                Intent myIntent1 = new Intent(MainActivity.this, Testfield.class);
                MainActivity.this.startActivity(myIntent1);
                return true;

            case R.id.sensorsettings:
                Intent myIntent2 = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(myIntent2);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        TabsAdapter tabAdapter = new TabsAdapter(this, viewPager);

        tabAdapter.addTab(actionBar.newTab().setText("Event Sensors"), EventSensorFragment.class, null);
        tabAdapter.addTab(actionBar.newTab().setText("Time Sensors"), TimeSensorFragment.class, null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void sendSensorActionToService(int action, SensorEventContainer sensor){
        Intent i = new Intent(MainActivity.this, SensorService.class);
        i.putExtra("Type", SensorContainer.event);
        i.putExtra("Sensor", sensor);
        i.putExtra("Action", action);
        startService(i);
    }

    public void sendSensorActionToService(int action, SensorTimeContainer sensor){
        Intent i = new Intent(MainActivity.this, SensorService.class);
        i.putExtra("Type", SensorContainer.time);
        i.putExtra("Sensor", sensor);
        i.putExtra("Action", action);
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

}
