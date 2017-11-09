package com.fimrc.jdcf.persistence.container;

import com.fimrc.jdcf.persistence.structure.SensorDataType;

import java.util.Objects;

/**
 * A SensorDataField is a tuple consisting of a name and a dataType. It can be compared to a column in a database
 */
public class SensorDataField {
    private final String name;
    private final SensorDataType dataType;

    /**
     * Creates a new SensorDataField
     *
     * @param name     the name of the dataField
     * @param dataType the dataType of the dataField
     */
    public SensorDataField(String name, SensorDataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SensorDataField)) {
            return false;
        }
        SensorDataField p = (SensorDataField) o;
        return Objects.equals(p.name, name) && Objects.equals(p.dataType, dataType);
    }

    @Override
    public int hashCode() {
        return (name == null ? 0 : name.hashCode()) ^ (dataType == null ? 0 : dataType.hashCode());
    }

    @Override
    public String toString() {
        return "Pair{" + String.valueOf(name) + " " + String.valueOf(dataType) + "}";
    }

    /**
     * Returns the name of the SensorDataField
     *
     * @return the name of the SensorDataField
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the SensorDataType of the SensorDataField
     *
     * @return the SensorDataType of the SensorDataField
     */
    public SensorDataType getDataType() {
        return dataType;
    }
}
