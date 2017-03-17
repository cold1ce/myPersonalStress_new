package com.fimrc.mysensornetwork.sensors;


/**
 * Created by Sven on 17.03.2017.
 */

/*
        Sensor -> Index
        SensorContainer.getSensorIndex(SensorContainer. SENSOR_TYPE . SENSOR_NAME)

        Index -> Sensor
        SensorContainer.getSensor( YOUR_INDEX ).SENSOR_TYPE
        all wrong SENSOR_TYPEs will return null
*/


public class SensorContainer {

    public enum EventSensors {
        Screen,
        Call
    }
    public enum TimeSensors {
        Audio,
        GPS,
        Light,
        Cell
    }

    public static int getNumberOfSensors(){
        return EventSensors.values().length + TimeSensors.values().length;
    }
    public static int getSensorIndex(SensorContainer.EventSensors sensor){
        return SensorContainer.EventSensors.valueOf(sensor.toString()).ordinal();
    }

    public static int getSensorIndex(SensorContainer.TimeSensors sensor){
        int numberEventSensors = EventSensors.values().length;
        return SensorContainer.TimeSensors.valueOf(sensor.toString()).ordinal()+numberEventSensors;
    }

    public static SensorTypes getSensor(int index){
        int numberEventSensors = EventSensors.values().length;
        int numberTimeSensors = TimeSensors.values().length;
        if(index > (numberEventSensors + numberTimeSensors)-1)
            throw new IndexOutOfBoundsException("Index too high for number of Sensors");
        if(index < numberEventSensors)
            return new SensorTypes(EventSensors.values()[index], null);
        return new SensorTypes(null, TimeSensors.values()[((index - numberEventSensors))]);
    }

    public static class SensorTypes {
        public final SensorContainer.EventSensors eventSensor;
        public final SensorContainer.TimeSensors timeSensor;

        public SensorTypes(SensorContainer.EventSensors eventSensor, SensorContainer.TimeSensors timeSensor){
            this.eventSensor = eventSensor;
            this.timeSensor = timeSensor;
        }

    }

}
