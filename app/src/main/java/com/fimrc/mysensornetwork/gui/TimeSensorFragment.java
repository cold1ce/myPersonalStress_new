package com.fimrc.mysensornetwork.gui;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.SensorService;
import com.fimrc.mysensornetwork.sensors.SensorContainer;
import com.fimrc.sensorfusionframework.sensors.SensorManager;

import java.util.ArrayList;

/**
 * Created by Sven on 16.03.2017.
 */

public class TimeSensorFragment extends Fragment implements View.OnClickListener {

    ArrayList<TableRow> tableRowList = new ArrayList<>();
    ArrayList<Button> buttonList = new ArrayList<>();
    private final int buttonWidth = 98;
    private final int buttonHeigth = 100;
    private TableLayout tableLayout;
    private View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Fragment", "on CreateView - EventSensorFragment");

        myView = inflater.inflate(R.layout.time_sensor_fragment, container, false);

        if (savedInstanceState != null) {
            for(int row = 0; row < SensorContainer.TimeSensors.values().length; row++) {
                if (!SensorManager.instance().getSensor(SensorContainer.getSensorIndex(SensorContainer.getTimeSensor(getId()))).isActive()) {
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_off);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                    Resources resources = getResources();
                    buttonList.get(getId()).setBackground(new BitmapDrawable(resources, scaledBitmap));
                } else {
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_on);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                    Resources resources = getResources();
                    buttonList.get(getId()).setBackground(new BitmapDrawable(resources, scaledBitmap));
                }
            }
        }else{
            initialize();
        }
        return myView;
    }

    private void initialize(){
        tableLayout = (TableLayout) myView.findViewById(R.id.time_table_layout);
        for(int row = 0; row < SensorContainer.timeSensorCount(); row++){
            TableRow tableRow = new TableRow(this.getContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            tableLayout.addView(tableRow);

            TextView textView = new TextView(this.getContext());
            textView.setText(SensorContainer.getTimeSensor(row).toString());
            tableRow.addView(textView);

            Button button = new Button(this.getContext());
            button.setId(row);
            buttonList.add(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!SensorManager.instance().getSensor(SensorContainer.getSensorIndex(SensorContainer.getTimeSensor(v.getId()))).isActive()) {
                        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_on);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                        Resources resources = getResources();
                        buttonList.get(v.getId()).setBackground(new BitmapDrawable(resources, scaledBitmap));
                        ((MainActivity)getActivity()).sendSensorActionToService(SensorService.ACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.getTimeSensor(v.getId())));
                        ((MainActivity)getActivity()).sendSensorActionToService(SensorService.START_LOGGING, SensorContainer.getSensorIndex(SensorContainer.getTimeSensor(v.getId())));
                    }else{
                        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_off);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                        Resources resources = getResources();
                        buttonList.get(v.getId()).setBackground(new BitmapDrawable(resources, scaledBitmap));
                        ((MainActivity)getActivity()).sendSensorActionToService(SensorService.STOP_LOGGING, SensorContainer.getSensorIndex(SensorContainer.getTimeSensor(v.getId())));
                        ((MainActivity)getActivity()).sendSensorActionToService(SensorService.DEACTIVATE_SENSOR, SensorContainer.getSensorIndex(SensorContainer.getTimeSensor(v.getId())));
                    }
                }
            });

            tableRow.addView(button);

            if(!SensorManager.instance().getSensor(SensorContainer.getSensorIndex(SensorContainer.getTimeSensor(row))).isActive()) {
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_off);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                Resources resources = getResources();
                buttonList.get(row).setBackground(new BitmapDrawable(resources, scaledBitmap));
            }else{
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_on);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                Resources resources = getResources();
                buttonList.get(row).setBackground(new BitmapDrawable(resources, scaledBitmap));
            }

            tableRowList.add(row, tableRow);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save internal values if necessary
    }

    @Override
    public void onClick(View v) {
        buttonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_on);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                Resources resources = getResources();
                buttonList.get(0).setBackground(new BitmapDrawable(resources, scaledBitmap));
            }
        });

    }


}
