package com.fimrc.mysensornetwork.gui;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fimrc.mysensornetwork.R;
import com.fimrc.mysensornetwork.SensorService;
import com.fimrc.mysensornetwork.sensors.SensorContainer;
import com.fimrc.sensorfusionframework.sensors.SensorManager;
import com.fimrc.sensorfusionframework.sensors.SensorTimeModule;


/**
 * Created by Sven on 16.03.2017.
 */

public class TimeSensorFragment extends Fragment {

    private View myView;
    private ListView listView;
    private String[] timeSensorList;
    private String[] timeSensorDescriptionList;
    private int indexcorrection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Fragment", "on CreateView - TimeSensorFragment");

        myView = inflater.inflate(R.layout.time_sensor_fragment, container, false);
        indexcorrection = SensorContainer.eventSensorCount();

        timeSensorList = new String[SensorContainer.timeSensorCount()];
        for(int i = 0; i< SensorContainer.timeSensorCount();i++){
            timeSensorList[i] = SensorContainer.getSensor(i+indexcorrection).timeSensor.toString();
        }

        listView = (ListView)myView.findViewById(R.id.time_listView);
        timeSensorDescriptionList = getResources().getStringArray(R.array.timeSensorDescription);
        TimeSensorFragment.myAdapter adapter = new TimeSensorFragment.myAdapter(container.getContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!SensorManager.instance().getSensor(indexcorrection+position).isActive()){
                    ((MainActivity)getActivity()).sendSensorActionToService(SensorService.ACTIVATE_SENSOR, indexcorrection+position);
                    ((MainActivity)getActivity()).sendSensorActionToService(SensorService.START_LOGGING, indexcorrection+position);
                    ((ImageView)(view.findViewById(R.id.single_row_time_imageView))).setImageResource(R.drawable.sensor_on);
                    ((TextView)view.findViewById(R.id.single_row_time_showTimerSet)).setText("Timer set to 60 Seconds");
                    view.findViewById(R.id.single_row_time_setTimerButton).setVisibility(View.VISIBLE);
                    myParams params = new myParams(indexcorrection+position, view.findViewById(R.id.single_row_time_showTimerSet));
                    view.findViewById(R.id.single_row_time_setTimerButton).setTag(params);
                    view.findViewById(R.id.single_row_time_setTimerButton).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getUserInputForNewTimer(((int)((myParams)v.getTag()).arg1), (TextView)(((myParams)v.getTag()).arg2));
                        }
                    });
                }else{
                    ((MainActivity)getActivity()).sendSensorActionToService(SensorService.STOP_LOGGING, indexcorrection+position);
                    ((MainActivity)getActivity()).sendSensorActionToService(SensorService.DEACTIVATE_SENSOR, indexcorrection+position);
                    ((ImageView)(view.findViewById(R.id.single_row_time_imageView))).setImageResource(R.drawable.sensor_off);
                    ((TextView)view.findViewById(R.id.single_row_time_showTimerSet)).setText("No Timer active - Sensor deactivated");
                    view.findViewById(R.id.single_row_time_setTimerButton).setVisibility(View.GONE);
                }
            }
        });

        return myView;
    }

    private class myParams{
        Object arg1;
        Object arg2;

        public myParams(Object arg1, Object arg2){
            this.arg1 = arg1;
            this.arg2 = arg2;
        }
    }

    class myAdapter extends ArrayAdapter<String> {

        Context context;
        MyViewHolder holder;

        public myAdapter(Context context) {
            super(context, R.layout.single_row_time, R.id.single_row_time_sensorName, timeSensorList);
            this.context = context;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            holder = null;
            if(row == null) { //erster Aufruf
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.single_row_time, parent, false);
                holder = new MyViewHolder(row);
                row.setTag(holder);
            }else{ //recycling
                holder = (MyViewHolder)row.getTag();
            }

            if(SensorManager.instance().getSensor(indexcorrection+position).isActive()) {
                holder.myImage.setImageResource(R.drawable.sensor_on);
                holder.showTimerSet.setText("Timer set to "+((SensorTimeModule)SensorManager.instance().getSensor(indexcorrection+position)).getTimer()+" Seconds");
                holder.setTimerButton.setVisibility(View.VISIBLE);
                myParams params = new myParams(indexcorrection+position, holder);
                holder.setTimerButton.setTag(params);
                holder.setTimerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getUserInputForNewTimer((int)((myParams)v.getTag()).arg1, ((MyViewHolder)((myParams)v.getTag()).arg2).showTimerSet);
                    }
                });
            }else {
                holder.myImage.setImageResource(R.drawable.sensor_off);
                holder.setTimerButton.setVisibility(View.GONE);
                holder.showTimerSet.setText("No Timer active - Sensor deactivated");
            }
            holder.mySensorName.setText(SensorContainer.getSensor(indexcorrection+position).timeSensor.toString());
            holder.mySensorDescription.setText(timeSensorDescriptionList[position]);

            return row;
        }
    }
    class MyViewHolder {
        ImageView myImage;
        TextView mySensorName;
        TextView mySensorDescription;
        ImageView setTimerButton;
        TextView showTimerSet;

        MyViewHolder(View v){
            myImage=(ImageView)v.findViewById(R.id.single_row_time_imageView);
            mySensorName = (TextView)v.findViewById(R.id.single_row_time_sensorName);
            mySensorDescription = (TextView)v.findViewById(R.id.single_row_time_sensorDescription);
            setTimerButton = (ImageView) v.findViewById(R.id.single_row_time_setTimerButton);
            showTimerSet = (TextView) v.findViewById(R.id.single_row_time_showTimerSet);
        }
    }

    private void getUserInputForNewTimer(final int sensorIndex, final TextView timeTextView){
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setMessage("What interval should the sensor use? (Type in seconds)");
        alert.setTitle("New Timer");

        final EditText input = new EditText(this.getActivity().getBaseContext());
        input.setTextColor(Color.BLACK);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);


        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int timer = Integer.parseInt(input.getText().toString());
                if(timer < 60){
                    new AlertDialog.Builder(TimeSensorFragment.this.getActivity())
                            .setTitle("Error")
                            .setMessage("You input have to be at least 60 seconds")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }else{
                    ((SensorTimeModule)SensorManager.instance().getSensor(sensorIndex)).setTimer(timer);
                    timeTextView.setText("Timer set to "+timer+" Seconds");
                    //timeTextView.setText("Timer set to "+((SensorTimeModule)SensorManager.instance().getSensor(sensorIndex)).getTimer()+" Seconds");

                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save internal values if necessary
    }

}
