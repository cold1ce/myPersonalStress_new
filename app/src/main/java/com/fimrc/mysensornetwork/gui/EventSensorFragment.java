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
 * Created by Sven on 17.03.2017.
 */

public class EventSensorFragment extends Fragment {

    private ArrayList<TableRow> tableRowList = new ArrayList<>();
    private ArrayList<Button> buttonList = new ArrayList<>();
    private final int buttonWidth = 98;
    private final int buttonHeigth = 100;
    private TableLayout tableLayout;
    private View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Fragment", "on CreateView - EventSensorFragment");

        myView = inflater.inflate(R.layout.event_sensor_fragment, container, false);

        if (savedInstanceState != null) {
            for(int row = 0; row < SensorContainer.EventSensors.values().length; row++) {
                if (!SensorManager.instance().getSensor(row).isActive()) {
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_off);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                    Resources resources = getResources();
                    buttonList.get(row).setBackground(new BitmapDrawable(resources, scaledBitmap));
                } else {
                    Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_on);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                    Resources resources = getResources();
                    buttonList.get(row).setBackground(new BitmapDrawable(resources, scaledBitmap));
                }
            }
        } else{
            initialize();
        }


        return myView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save internal values if necessary
    }

    private void initialize(){
        tableLayout = (TableLayout) myView.findViewById(R.id.event_table_layout);
        for(int row = 0; row < SensorContainer.EventSensors.values().length; row++){
            TableRow tableRow = new TableRow(this.getContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            tableLayout.addView(tableRow);

            TextView textView = new TextView(this.getContext());
            textView.setText(SensorContainer.getSensor(row).eventSensor.toString());
            tableRow.addView(textView);

            Button button = new Button(this.getContext());
            button.setId(row);
            buttonList.add(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!SensorManager.instance().getSensor(v.getId()).isActive()) {
                        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_on);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                        Resources resources = getResources();
                        buttonList.get(v.getId()).setBackground(new BitmapDrawable(resources, scaledBitmap));
                        Log.d("Test", String.valueOf(v.getId()));
                        ((MainActivity)getActivity()).sendSensorActionToService(SensorService.ACTIVATE_SENSOR, v.getId());
                        ((MainActivity)getActivity()).sendSensorActionToService(SensorService.START_LOGGING, v.getId());
                    }else{
                        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor_off);
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, buttonWidth, buttonHeigth, true);
                        Resources resources = getResources();
                        buttonList.get(v.getId()).setBackground(new BitmapDrawable(resources, scaledBitmap));
                        ((MainActivity)getActivity()).sendSensorActionToService(SensorService.STOP_LOGGING, v.getId());
                        ((MainActivity)getActivity()).sendSensorActionToService(SensorService.DEACTIVATE_SENSOR, v.getId());
                    }
                }
            });

            tableRow.addView(button);

            if(!SensorManager.instance().getSensor(row).isActive()) {
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

}
