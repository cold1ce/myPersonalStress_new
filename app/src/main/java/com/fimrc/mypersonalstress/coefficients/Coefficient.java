package com.fimrc.mypersonalstress.coefficients;

//Stellt einen Koeffizienten dar, welcher für das Stressmodel verwendet wird.
//Anhand von Sensorname, Sensorwert sowie den Transformationen und der Aggregation wird
//ein eindeutiger Name für jeden Koeffizienten generiert und in der Personalisierung die
//entsprechenden Methoden durchgeführt.

public class Coefficient {
    public String sensorname, sensorvalue, transformation1, transformation2, aggregation, name;
    public double generalmodelvalue;
    public Coefficient (String sensorname, String sensorvalue, String transformation1, String transformation2, String aggregation, double generalmodelvalue) {
        this.sensorname = sensorname;
        this.sensorvalue = sensorvalue;
        this.transformation1 = transformation1;
        this.transformation2 = transformation2;
        this.aggregation = aggregation;
        this.name=this.sensorname+"_"+this.sensorvalue+"_"+this.transformation1+"_"+this.transformation2+"_"+this.aggregation;
        this.generalmodelvalue = generalmodelvalue;
    }
}
