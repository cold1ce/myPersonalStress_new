package com.fimrc.mypersonalstress.coefficients;

/**
 * Created by ZEBRA on 06.11.2017.
 */

public class SingleSensorModel {

    public String sensorname, sensorvalue;
    public boolean raw, outrm, none, log, posms, negms, min, max, range, median, mean, count, counth;
    public int timeframe;

    public SingleSensorModel(String sensorname, String sensorvalue, boolean raw, boolean outrm, boolean none, boolean log, boolean posms, boolean negms, boolean min, boolean max, boolean range, boolean median, boolean mean, boolean count, boolean counth, int timeframe) {
        this.sensorname = sensorname;
        this.sensorvalue= sensorvalue;
        this.raw = raw;
        this.outrm = outrm;
        this.none = none;
        this.log = log;
        this.posms = posms;
        this.negms = negms;
        this.min = min;
        this.max = max;
        this.range = range;
        this.median = median;
        this.mean = mean;
        this.count = count;
        this.counth = counth;
        this.timeframe = timeframe;
    }
}
