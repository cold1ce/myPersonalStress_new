package com.fimrc.sensorfusionframework.persistence.structure;


public class SensorRecordStructureException extends RuntimeException {
    public SensorRecordStructureException() {
        super();
    }

    public SensorRecordStructureException(String message) {
        super(message);
    }

    public SensorRecordStructureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SensorRecordStructureException(Throwable cause) {
        super(cause);
    }
}
