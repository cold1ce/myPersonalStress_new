//DatabaseHelper.java
//Hier finden sich alle für die App notwendigen Schreib-, Lese- und Datenbankberechnungsmethoden.

package com.fimrc.mypersonalstress.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.fimrc.mypersonalstress.coefficients.Coefficient;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseHelper";
    public static Context context;
    public static final int DB_VERSION = 1;
    public static DatabaseHelper instance;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    public DatabaseHelper(Context context) {
        super(context, "name", null, 1);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //Erstellt alle Tabellen neu (z.B. bei Reset oder erstem Aufruf der App)
    public void createAllTables() {
        Log.d(TAG, "Erstelle alle Tabellen neu");
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.d(TAG, "Datenbank geöffnet");
        db.execSQL("CREATE TABLE IF NOT EXISTS Personalizations (ObservationNumber INTEGER, Datum DATETIME DEFAULT CURRENT_TIMESTAMP, Score REAL, Prediction REAL, Error REAL, Error2 REAL)");
        //Log.d(TAG, "Coefficients erstellt/exisitiert.");
        db.execSQL("CREATE TABLE IF NOT EXISTS CoefficientMeans (ObservationNumber INTEGER)");
        //Log.d(TAG, "CoefficientMeans erstellt/exisitiert.");
        db.execSQL("CREATE TABLE IF NOT EXISTS StandardDeviations (ObservationNumber INTEGER)");
        //Log.d(TAG, "StandardDeviations erstellt/exisitiert.");
        db.execSQL("CREATE TABLE IF NOT EXISTS Observations (ObservationNumber INTEGER)");
        //Log.d(TAG, "Observations erstellt/exisitiert.");
        db.execSQL("CREATE TABLE IF NOT EXISTS Coefficients (ObservationNumber INTEGER)");
        //Log.d(TAG, "Coefficients erstellt/exisitiert.");
        db.execSQL("CREATE TABLE IF NOT EXISTS Gradients (ObservationNumber INTEGER, Average REAL)");
        //Log.d(TAG, "Coefficients erstellt/exisitiert.");
    }

    //Setzt die Datenbank von myPersonalStress zurück.
    public void resetMPSDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.d(TAG, "Datenbank geöffnet");
        db.execSQL("DROP TABLE IF EXISTS Personalizations");
        db.execSQL("DROP TABLE IF EXISTS CoefficientMeans");
        db.execSQL("DROP TABLE IF EXISTS StandardDeviations");
        db.execSQL("DROP TABLE IF EXISTS Observations");
        db.execSQL("DROP TABLE IF EXISTS Coefficients");
        db.execSQL("DROP TABLE IF EXISTS Gradients");
        Log.d(TAG, "Alle Tabellen in der MPS Datenbank gelöscht.");
    }

    //Setzt die Datenbank von mySensorNetwork zurück.
    public void resetMSNDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.d(TAG, "Datenbank geöffnet");
        db.execSQL("DROP TABLE IF EXISTS TestSensor1");
        db.execSQL("DROP TABLE IF EXISTS TestSensor2");
        db.execSQL("DROP TABLE IF EXISTS TestSensor3");
        Log.d(TAG, "Alle Tabellen in der MSN Datenbank gelöscht.");
    }

    //Fügt eine neue Zeile (Laufvariable observnum) in alle Tabellen ein
    public void addObservationCount(int observnum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ObservationNumber", observnum);
        db.insert("CoefficientMeans", null, contentValues);
        db.insert("StandardDeviations", null, contentValues);
        db.insert("Observations", null, contentValues);
        db.insert("Coefficients", null, contentValues);
        db.insert("Gradients", null, contentValues);
    }

    //Fügt die Koeffizienten des allgemeinen Modells in die Datenbank ein
    public void addFirstCoefficientsRow() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ObservationNumber", 0);
        db.insert("Coefficients", null, contentValues);
    }

    //Fügt das Ergebnis eines Fragebogens in die Datenbank ein.
    public boolean addNewPSSScore(int observnumn, long zeit, double score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ObservationNumber", observnumn);
        contentValues.put("Datum", zeit);
        contentValues.put("Score", score);
        long result = db.insert("Personalizations", null, contentValues);
        return result != -1;
    }

    //Fügt eine neue Vorhersage hinzu.
    public void addNewPrediction(int observnumn, double prediction) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Personalizations SET Prediction = " + prediction + " WHERE ObservationNumber=" + (observnumn));
    }

    //Fügt einen neuen Vorhersagefehler hinzu.
    public void addNewPredictionError(int observnumn, double predictionerror1) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Personalizations SET Error = " + predictionerror1 + " WHERE ObservationNumber=" + (observnumn));
    }

    //Fügt einen neuen Vorhersagefehler(quadriert) hinzu.
    public void addNewPredictionErrorSquared(int observnumn, double predictionerror2) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Personalizations SET Error2 = " + predictionerror2 + " WHERE ObservationNumber=" + (observnumn));
    }

    //Gibt das zuletzt gespeicherte Fragebogenresultat zurück
    public double getLastPSSScore() {
        double score = 0.0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT Score FROM Personalizations ORDER BY ObservationNumber DESC LIMIT 1", null);
        cursor.moveToLast();
        score = cursor.getDouble(0);
        cursor.close();
        return score;
    }

    //Gibt ein bestimmtes gespeichertes Fragebogenresultat zurück
    public double getPSSScoreofObservationNumber(int observNumN) {
        double score = 0.0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT Score FROM Personalizations WHERE ObservationNumber = " + observNumN, null);
        cursor.moveToLast();
        score = cursor.getDouble(0);
        cursor.close();
        return score;
    }

    //Gibt zurück wieviele Durchgänge bereits erfolgt sind.
    public int getAmountofObservationsDoneYet() {
        int amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(ObservationNumber) FROM Personalizations", null);
        //Log.d(TAG, "Query done...");
        cursor.moveToLast();
        amount = cursor.getInt(0);
        cursor.close();
        Log.d(TAG, "Anzahl bisheriger Beobachtungen wurden abgefragt: " + amount);
        return amount;
    }

    //Fügt neu berechnete Koeffizienten, also ein neues Stressmodell in die Datenbank ein.
    public void addNewCoefficientValues(Coefficient c, int observNumN, double valuex) {
        Log.d(TAG, "addNewCoefficientMethode gestartet");
        SQLiteDatabase db = this.getWritableDatabase();
        if ((checkIfColumnExists(db, "Coefficients", c.name)) == false) {
            db.execSQL("ALTER TABLE Coefficients ADD COLUMN " + c.name + " REAL");
        }
        Log.d(TAG, "Füge Koeffizientenwert für " + c.name + " bei ObsNr " + (observNumN) + " mit Wert " + valuex + " ein");
        db.execSQL("UPDATE Coefficients SET " + c.name + " = " + valuex + " WHERE ObservationNumber=" + (observNumN));
    }

    //Fügt einen neuen Gradienten ein
    public void addNewGradient(Coefficient c, int observNumN, double valuex) {
        Log.d(TAG, "addNewGradientMethode gestartet");
        SQLiteDatabase db = this.getWritableDatabase();
        if ((checkIfColumnExists(db, "Gradients", c.name)) == false) {
            db.execSQL("ALTER TABLE Gradients ADD COLUMN " + c.name + " REAL");
        }
        Log.d(TAG, "Füge Gradient für " + c.name + " bei ObsNr " + (observNumN) + " mit Wert " + valuex + " ein");
        db.execSQL("UPDATE Gradients SET " + c.name + " = " + valuex + " WHERE ObservationNumber=" + (observNumN));

    }

    //Fügt eine TestSensor-Tabelle in mySensorNetwork ein
    public void createTestSensorTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Erstelle TestSensor Tabelle");
        db.execSQL("CREATE TABLE IF NOT EXISTS TestSensor1 (_id INTEGER PRIMARY KEY AUTOINCREMENT, Timestamp DATE NOT NULL, testvalue REAL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS TestSensor2 (_id INTEGER PRIMARY KEY AUTOINCREMENT, Timestamp DATE NOT NULL, testvalue TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS TestSensor3 (_id INTEGER PRIMARY KEY AUTOINCREMENT, Timestamp DATE NOT NULL, testvalue REAL)");
    }

    //Fügt Testwerte für timebased Sensoren hinzu
    public boolean addNewTimeTestSensorValues(String sensorname, String sensorvalue, double valuex) {
        SQLiteDatabase db = this.getWritableDatabase();
        long time = System.currentTimeMillis();
        Date timestamp = new Date(time);
        Format format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        format.format(timestamp);
        ContentValues contentValues = new ContentValues();
        contentValues.put("Timestamp", format.format(timestamp));
        contentValues.put(sensorvalue, valuex);
        long result = db.insert(sensorname, null, contentValues);
        return result != -1;
    }

    //Fügt Testwerte für eventbased Sensoren hinzu
    public boolean addNewEventTestSensorValues(String sensorname, String sensorvalue, String valuex) {
        SQLiteDatabase db = this.getWritableDatabase();
        long time = System.currentTimeMillis();
        Date timestamp = new Date(time);
        Format format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        format.format(timestamp);
        ContentValues contentValues = new ContentValues();
        contentValues.put("Timestamp", format.format(timestamp));
        contentValues.put(sensorvalue, valuex);
        long result = db.insert(sensorname, null, contentValues);
        return result != -1;
    }

    //Aggregationsmethode für das Minimum
    public double getAggrMin(String sensor, String value, int timeframe) {
        double min;
        int timeframenew = 60-timeframe; //Zeitverschiebung 1h von UTC-Zeit
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT min(" + value + ") FROM " + sensor+" WHERE datetime(Timestamp) > datetime(current_timestamp, '"+timeframenew+" minutes')", null);
        cursor.moveToLast();
        min = cursor.getDouble(0);
        cursor.close();
        return min;
    }

    //Aggregationsmethode für das Maximum
    public double getAggrMax(String sensor, String value, int timeframe) {
        double max;
        int timeframenew = 60-timeframe; //Zeitverschiebung 1h von UTC-Zeit
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT max(" + value + ") FROM " + sensor+" WHERE datetime(Timestamp) > datetime(current_timestamp, '"+timeframenew+" minutes')", null);
        cursor.moveToLast();
        max = cursor.getDouble(0);
        cursor.close();
        return max;
    }

    //Aggregationsmethode für die Spannweite
    public double getAggrRange(String sensor, String value, int timeframe) {
        double max, min, range = 0.0;
        int timeframenew = 60-timeframe; //Zeitverschiebung 1h von UTC-Zeit
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT max(" + value + ") FROM " + sensor+" WHERE datetime(Timestamp) > datetime(current_timestamp, '"+timeframenew+" minutes')", null);
        cursor.moveToLast();
        max = cursor.getDouble(0);
        Cursor cursor2 = db.rawQuery("SELECT min(" + value + ") FROM " + sensor+" WHERE datetime(Timestamp) > datetime(current_timestamp, '"+timeframenew+" minutes')", null);
        cursor2.moveToLast();
        min = cursor2.getDouble(0);
        range = (max - min);
        //Log.d(TAG, "max: " + max + " min: " + min + " range: " + range);
        cursor.close();
        cursor2.close();
        return range;
    }

    //Aggregationsmethode für den Median
    public double getAggrMedian(String sensor, String value, int timeframe) {
        double max;
        int timeframenew = 60-timeframe; //Zeitverschiebung 1h von UTC-Zeit
        SQLiteDatabase db = this.getWritableDatabase();
        //Crazy Befehl:
        Cursor cursor = db.rawQuery("SELECT AVG("+value+") FROM (SELECT "+value+" FROM "+sensor+" WHERE (datetime(Timestamp) > datetime(current_timestamp, '"+timeframenew+" minutes'))ORDER BY "+value+" LIMIT 2 - (SELECT COUNT(*) FROM "+sensor+" WHERE (datetime(Timestamp) > datetime(current_timestamp, '"+timeframenew+" minutes'))) % 2 OFFSET (SELECT (COUNT(*) - 1) / 2 FROM "+sensor+" WHERE (datetime(Timestamp) > datetime(current_timestamp, '"+timeframenew+" minutes'))))", null);
        cursor.moveToLast();
        max = cursor.getDouble(0);
        cursor.close();
        return max;
    }

    //Aggregationsmethode für den Mittelwert
    public double getAggrMean(String sensor, String value, int timeframe) {
        double mean;
        int timeframenew = 60-timeframe; //Zeitverschiebung 1h von UTC-Zeit
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT avg(" + value + ") FROM " + sensor+" WHERE datetime(Timestamp) > datetime(current_timestamp, '"+timeframenew+" minutes')", null);
        cursor.moveToLast();
        mean = cursor.getDouble(0);
        cursor.close();
        return mean;
    }

    //Aggregationsmethode für die Anzahl an Einträgen
    public double getAggrCount(String sensor, String value, int timeframe) {
        int timeframenew = 60-timeframe; //Zeitverschiebung 1h von UTC-Zeit
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT Count(*) from "+sensor+" WHERE datetime(Timestamp) > datetime(current_timestamp, '"+timeframenew+" minutes')", null);
        cursor.moveToLast();
        count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    /*public void createCoeffColumn(String sensor, String value, int observationnr, String trans1, String trans2, String aggregation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("ALTER TABLE CoefficientsValues ADD COLUMN " + sensor + "." + value + "." + trans1 + "." + trans2 + "." + aggregation + " REAL", null);
    }

    public void writeCoeff(String sensor, String value, int observationnr, String trans1, String trans2, String aggregation, double coefficientvalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sensor + "." + value + "." + trans1 + "." + trans2 + "." + aggregation, coefficientvalue);
    }*/

    //Gibt den letzten Mittelwert des Koeffizienten zurück.
    public double getOldMean(Coefficient c, int observNumN) {
        double oldmean = 0.0;
        if (observNumN > 1) {
            Log.d(TAG, "Hole altes u von BeobachtungsNr:" + (observNumN - 1) + " (Funktion getOldMean) (Aktuelle Beob.Nr: " + observNumN);
            SQLiteDatabase db = this.getWritableDatabase();
            if (checkIfColumnExists(db, "CoefficientMeans", c.name) == false) {
                db.execSQL("ALTER TABLE CoefficientMeans ADD COLUMN " + c.name + " REAL");
            } else {
                Log.d(TAG, "Spalte für Beobachtung Nr." + observNumN + " existiert. Hole letzten Wert.");
                Cursor cursor = db.rawQuery("SELECT " + c.name + " FROM CoefficientMeans WHERE ObservationNumber = " + (observNumN -1), null);
                cursor.moveToLast();
                oldmean = cursor.getDouble(0);
                cursor.close();
            }
        } else {
            Log.d(TAG, "Es ist die 1. Beobachtung, also gibt es noch kein altes u, gebe 0.0 zurück");
            oldmean = 0.0;
        }
        return oldmean;
    }

    ////Gibt die letzte Standardabweichung des Koeffizienten zurück.
    public double getOldStdDev(Coefficient c, int observNumN) {
        Log.d(TAG, "Hole altes o von BeobachtungsNr:" + (observNumN - 1) + " (Funktion getOldStdDev) (Aktuelle Beob.Nr: " + observNumN);
        double oldstddev;
        if (observNumN > 1) {
            SQLiteDatabase db = this.getWritableDatabase();
            if (checkIfColumnExists(db, "StandardDeviations", c.name) == false) {
                db.execSQL("ALTER TABLE StandardDeviations ADD COLUMN " + c.name + " REAL");
                return 0.0;
            } else {
                Cursor cursor = db.rawQuery("SELECT " + c.name + " FROM StandardDeviations WHERE ObservationNumber = " + (observNumN -1), null);
                cursor.moveToLast();
                oldstddev = cursor.getDouble(0);
                Log.d(TAG, "Geholter alter o-Wert ist: " + oldstddev);
                cursor.close();
                return oldstddev;
            }
        } else {
            Log.d(TAG, "Es ist die 1. Beobachtung, also gibt es noch kein altes o, gebe 0.0 zurück");
            oldstddev = 0.0;
        }
        return oldstddev;
    }

    //Rufe Sensorwert (Beobachtung) auf
    public double getObservation(Coefficient c, int observNumN) {
        Log.d(TAG, "Hole Observation:" + (c.name) + " (Funktion getObservation) (Aktuelle Beob.Nr: " + observNumN);
        double observ;
        SQLiteDatabase db = this.getWritableDatabase();
        //Log.d(TAG, "Spaltenname wo gelesen wird ist: " + c.name);
        Cursor cursor = db.rawQuery("SELECT " + c.name + " FROM Observations WHERE ObservationNumber = " + observNumN, null);
        cursor.moveToLast();
        observ = cursor.getDouble(0);
        //Log.d(TAG, "Geholter Observations-Wert ist: " + observ);
        cursor.close();
        return observ;
    }

    //Ruft den aktuellen Koeffizienten ab
    public double getOldCoefficient(Coefficient c, int observNumN) {
        Log.d(TAG, "Hole bisherigen Koeffizient:" + c.name + " (Funktion getCoefficient) (Aktuelle Beob.Nr: " + observNumN + " Koeffizient aus Nr. " + (observNumN - 1));
        double oldcoeff;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + c.name + " FROM Coefficients WHERE ObservationNumber = " + (observNumN -1), null);
        cursor.moveToLast();
        oldcoeff = cursor.getDouble(0);
        //Log.d(TAG, "Geholter OKoeffizienten-Wert ist: " + oldcoeff);
        cursor.close();
        return oldcoeff;
    }

    //Fügt einen neuen Mittelwert hinzu
    public void addNewMean(Coefficient c, int observNumN, double valuex) {
        Log.d(TAG, "addNewMean Methode gestartet");
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkIfColumnExists(db, "CoefficientMeans", c.name) == false) {
            db.execSQL("ALTER TABLE CoefficientMeans ADD COLUMN " + c.name + " REAL");
        }
        Log.d(TAG, "Füge neuen Mean ein in:" + c.name + " bei ObsNr " + observNumN + " mit Wert " + valuex);
        db.execSQL("UPDATE CoefficientMeans SET " + c.name + " = " + valuex + " WHERE ObservationNumber=" + observNumN);
    }

    //Fügt eine neue Standardabweichung ein
    public void addNewStdDev(Coefficient c, int observNumN, double valuex) {
        Log.d(TAG, "addNewStdDev Methode gestartet");
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkIfColumnExists(db, "StandardDeviations", c.name) == false) {
            db.execSQL("ALTER TABLE StandardDeviations ADD COLUMN " + c.name + " REAL");
        }
        Log.d(TAG, "Füge neue StdDev ein in: " + c.name + " bei ObsNr " + observNumN + " mit Wert " + valuex);
        db.execSQL("UPDATE StandardDeviations SET " + c.name + " = " + valuex + " WHERE ObservationNumber=" + observNumN);
    }

    //Fügt neuen Sensorwert in die Datenbank ein
    public void addNewObservationValue(Coefficient c, int observNumN, double zvalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkIfColumnExists(db, "Observations", c.name) == false) {
            db.execSQL("ALTER TABLE Observations ADD COLUMN " + c.name + " REAL");
        }
        Log.d(TAG, "Füge neuen Standardisierten Wert ein in:" + c.name + " bei ObsNr " + observNumN + " mit Wert " + zvalue);
        db.execSQL("UPDATE Observations SET " + c.name + " = " + zvalue + " WHERE ObservationNumber=" + observNumN);
    }

    /*
    public double updateStandardDeviation(double x, double u, int N, double o) {
        System.out.println("übergeben: x:" + x + " u: " + u + " N: " + N + " o: " + o);
        double zaehlerpart1 = (N + 1);
        double zaehlerpart2 = Math.pow(x, 2) + N * (Math.pow(o, 2) + Math.pow(u, 2));
        System.out.println("x+n*u ist: " + (x + (N * u)));
        double zaehlerpart3 = Math.pow((x + (N * u)), 2);
        double zaehler = zaehlerpart1 * zaehlerpart2 - zaehlerpart3;
        System.out.println("o-Berechnungen||Part1: " + zaehlerpart1 + " - Part2: " + zaehlerpart2 + " - Part3: " + zaehlerpart3);
        double nenner = Math.pow((N + 1), 2);
        System.out.println("o-Berechnungen||Zähler: " + zaehler + " Nenner: " + nenner);
        double onew = Math.sqrt(zaehler / nenner);
        return onew;
    }*/

    //Überprüft ob Tabellenspalte bereits vorhanden ist
    public boolean checkIfColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
            Log.e(TAG, "checkColumnExists2..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return result;
    }

    //Prüft die Abbruchsbedingung des Gradientenschnitts
    public double checkGradientsAverages(int gradienttimewindow, int observnum) {
        double min, max;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursormax = db.rawQuery("SELECT max(Average) FROM Gradients WHERE " + observnum + " >= " + (observnum - gradienttimewindow), null);
        Cursor cursormin = db.rawQuery("SELECT max(Average) FROM Gradients WHERE " + observnum + " >= " + (observnum - gradienttimewindow), null);
        cursormax.moveToLast();
        max = cursormax.getDouble(0);
        max = Math.abs(max);
        cursormax.close();
        cursormin.moveToLast();
        min = cursormin.getDouble(0);
        min = Math.abs(min);
        cursormin.close();
        if (max >= min) {
            return max;
        }
        else {
            return min;
        }

    }

    //Fügt für aktuelle Gradienten einen neuen Schnitt hinzu
    public void addNewGradientsAverage(double value, int observNumN) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Gradients SET Average = " + value + " WHERE ObservationNumber=" + (observNumN));
    }

    //Rufe Zeit ab, wann zuletzt ein Personalisierungsvorgang durchgeführt wurde.
    public long getTimeOfLastPersonalization() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT Datum FROM Personalizations ORDER BY ObservationNumber DESC LIMIT 1", null);
        cursor.moveToLast();
        long ts = cursor.getLong(0);
        return ts;
    }

}