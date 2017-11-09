package com.fimrc.jdcf.persistence.structure;

/**
 * This ExceptionClass represents all errors occurring at runtime with the SensorRecordStructure
 */
public class SensorRecordStructureException extends RuntimeException {

    /**
     * Creates a new RuntimeException
     */
    public SensorRecordStructureException() {
        super();
    }

    /**
     * Creates a new RuntimeException with a specific message
     *
     * @param message message to be displayed
     */
    public SensorRecordStructureException(String message) {
        super(message);
    }

    /**
     * Creates a new RuntimeException with a specific message and cause
     *
     * @param message message to be displayed
     * @param cause   cause to be displayed
     */
    public SensorRecordStructureException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new RuntimeException with a specific cause
     *
     * @param cause cause to be displayed
     */
    public SensorRecordStructureException(Throwable cause) {
        super(cause);
    }
}
