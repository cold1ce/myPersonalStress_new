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


public enum SensorContainer{

    time,
    event

    /*
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

    public static int eventSensorCount(){
        return EventSensors.values().length;
    }

    public static int timeSensorCount(){
        return TimeSensors.values().length;
    }

    public static int sensorCount(){
        return EventSensors.values().length + TimeSensors.values().length;
    }

    public static int getSensorIndex(SensorContainer.EventSensors sensor){
        return SensorContainer.EventSensors.valueOf(sensor.toString()).ordinal();
    }

    public static int getSensorIndex(SensorContainer.TimeSensors sensor){
        int numberEventSensors = EventSensors.values().length;
        return SensorContainer.TimeSensors.valueOf(sensor.toString()).ordinal()+numberEventSensors;
    }

    public static SensorPackage getSensor(int index){
        int numberEventSensors = EventSensors.values().length;
        int numberTimeSensors = TimeSensors.values().length;
        if(index > (numberEventSensors + numberTimeSensors)-1)
            throw new IndexOutOfBoundsException("Index too high for number of Sensors");
        if(index < numberEventSensors)
            return new SensorPackage(EventSensors.values()[index], null);
        return new SensorPackage(null, TimeSensors.values()[((index - numberEventSensors))]);
    }

    public static SensorContainer.TimeSensors getTimeSensor(int index){
        if(!(index < timeSensorCount()))
            throw new IndexOutOfBoundsException("Index too high for number of Sensors");
        return getSensor(index + eventSensorCount()).timeSensor;
    }

    public static class SensorPackage {
        public final SensorContainer.EventSensors eventSensor;
        public final SensorContainer.TimeSensors timeSensor;

        public SensorPackage(SensorContainer.EventSensors eventSensor, SensorContainer.TimeSensors timeSensor){
            this.eventSensor = eventSensor;
            this.timeSensor = timeSensor;
        }

    }
    */

}
