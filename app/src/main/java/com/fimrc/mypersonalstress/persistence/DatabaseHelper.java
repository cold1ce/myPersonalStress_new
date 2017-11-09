package com.fimrc.mypersonalstress.persistence;


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.MatrixCursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import com.fimrc.mypersonalstress.coefficients.Coefficient;
        import com.fimrc.mypersonalstress.coefficients.SingleSensorModel;

        import java.util.ArrayList;

        import static java.sql.Types.REAL;

/**
 * DatabaseHelper
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "DatabaseHelper";
    public static Context context;
    //public static final String DB_NAME = "mypersonalstress.db";
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

    public void createAllTables() {
        Log.d(TAG, "Erstelle alle Tabellen neu");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Datenbank geöffnet");
        db.execSQL("CREATE TABLE IF NOT EXISTS PSSScores (ObservationNumber INTEGER, Datum DATETIME DEFAULT CURRENT_TIMESTAMP, Score REAL)");
        Log.d(TAG, "Coefficients erstellt/exisitiert.");
        db.execSQL("CREATE TABLE IF NOT EXISTS CoefficientMeans (ObservationNumber INTEGER)");
        Log.d(TAG, "CoefficientMeans erstellt/exisitiert.");
        db.execSQL("CREATE TABLE IF NOT EXISTS CoefficientStandardDerivations (ObservationNumber INTEGER)");
        Log.d(TAG, "CoefficientStandardDerivations erstellt/exisitiert.");
        db.execSQL("CREATE TABLE IF NOT EXISTS Observations (ObservationNumber INTEGER)");
        Log.d(TAG, "Observations erstellt/exisitiert.");
        db.execSQL("CREATE TABLE IF NOT EXISTS Coefficients (ObservationNumber INTEGER)");
        Log.d(TAG, "Coefficients erstellt/exisitiert.");
    }

    public void resetMPSDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Datenbank geöffnet");
        db.execSQL("DROP TABLE IF EXISTS PSSScores");
        db.execSQL("DROP TABLE IF EXISTS CoefficientMeans");
        db.execSQL("DROP TABLE IF EXISTS CoefficientStandardDerivations");
        db.execSQL("DROP TABLE IF EXISTS Observations");
        db.execSQL("DROP TABLE IF EXISTS Coefficients");
        Log.d(TAG, "Alle Tabellen in der MPS Datenbank gelöscht.");
    }

    public void addObservationCount(int observnum) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ObservationNumber", observnum);
            db.insert("CoefficientMeans", null, contentValues);
            db.insert("CoefficientStandardDerivations", null, contentValues);
            db.insert("Observations", null, contentValues);
            db.insert("Coefficients", null, contentValues);
    }

    public void resetMSNDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Datenbank geöffnet");
        db.execSQL("DROP TABLE IF EXISTS TestSensor");
        Log.d(TAG, "Alle Tabellen in der MSN Datenbank gelöscht.");
    }

    public boolean addNewPSSScore(int observnumn, long zeit, double score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ObservationNumber", observnumn);
        contentValues.put("Datum", zeit);
        contentValues.put("Score", score);
        long result = db.insert("PSSScores", null, contentValues);
        return result != -1;
    }

    public double getLastPSSScore(){
        Log.d(TAG, "Methodenbeginn getLastPSSscore");
        double score = 0.0;
        Log.d(TAG, "Öffne Datenbank");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "SQLite Query...");
        Cursor cursor = db.rawQuery("SELECT Score FROM PSSScores ORDER BY ObservationNumber DESC LIMIT 1", null);
        Log.d(TAG, "Query done...");
        cursor.moveToLast();
        score = cursor.getDouble(0);
        Log.d(TAG, "Score abgerufen: "+score);
        cursor.close();
        return score;
    }

    public int getAmountofObservationsDoneYet(){
        Log.d(TAG, "Methodenbeginn getAmountofObservationsDoneYet");
        int amount = 0;
        Log.d(TAG, "Öffne Datenbank");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "SQLite Query...");
        Cursor cursor = db.rawQuery("SELECT MAX(ObservationNumber) FROM PSSScores", null);
        Log.d(TAG, "Query done...");
        cursor.moveToLast();
        amount = cursor.getInt(0);
        Log.d(TAG, "Anzahl bisheriger Beobachtungen: "+amount);
        cursor.close();
        return amount;
    }

    public boolean addNewCoefficients(long zeit, double coeff1, double coeff2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Datum", zeit);
        contentValues.put("coeff1", coeff1);
        contentValues.put("coeff2", coeff2);
        long result = db.insert("Coefficients", null, contentValues);
        return result != -1;
    }

    public void createTestSensorTable() {
        Log.d(TAG, "Öffne Datenbank");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Erstelle TestSensor Tabelle");
        db.execSQL("CREATE TABLE IF NOT EXISTS TestSensor (_id INTEGER PRIMARY KEY AUTOINCREMENT, Datum DATETIME DEFAULT CURRENT_TIMESTAMP, testvalue REAL)");
    }

    public boolean addNewTestSensorValues(long zeit, double value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Datum", zeit);
        contentValues.put("testvalue", value);
        long result = db.insert("TestSensor", null, contentValues);
        return result != -1;
    }

    public double getZufallszahl(int min, int max) {
        double  random = Math.random() * (max - min) + min;
        return random;
    }

    public double getAggrMin() {
        double min = 0.0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT min(value1) FROM TestSensorValues", null);
        cursor.moveToLast();
        min = cursor.getDouble(0);
        cursor.close();
        return min;
    }

    public double getAggrMax(String sensor, String value, int timeframe) {
        double max;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT max("+value+") FROM "+sensor, null);
        cursor.moveToLast();
        max = cursor.getDouble(0);
        cursor.close();
        return max;
    }

    public double getAggrMean(String sensor, String value, int timeframe) {
        double max;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT avg("+value+") FROM "+sensor, null);
        cursor.moveToLast();
        max = cursor.getDouble(0);
        cursor.close();
        return max;
    }

    public void createCoeffColumn(String sensor, String value, int observationnr, String trans1, String trans2, String aggregation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("ALTER TABLE CoefficientsValues ADD COLUMN "+sensor+"."+value+"."+trans1+"."+trans2+"."+aggregation+" REAL", null);
    }

    public void writeCoeff(String sensor, String value, int observationnr, String trans1, String trans2, String aggregation, double coefficientvalue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sensor+"."+value+"."+trans1+"."+trans2+"."+aggregation, coefficientvalue);
    }

    public double getOldMean(Coefficient c, int observNumN) {
            double oldmean = 0.0;
            if (observNumN>1) {
                Log.d(TAG, "Hole altes u von BeobachtungsNr:" + (observNumN - 1) + " (Funktion getOldMean) (Aktuelle Beob.Nr: " + observNumN);
                SQLiteDatabase db = this.getWritableDatabase();
                Log.d(TAG, "Spaltenname welcher gelesen wird ist: " + c.name);
                if (checkIfColumnExists(db, "CoefficientMeans", c.name) == false) {
                    Log.d(TAG, "Eine Solche Spalte exisitiert nicht. Erstellen...");
                    db.execSQL("ALTER TABLE CoefficientMeans ADD COLUMN " + c.name + " REAL");
                    Log.d(TAG, "Spalte erstellt.");

                } else {
                    Log.d(TAG, "Spalte für Beobachtung Nr."+observNumN+" existiert. Hole letzten Wert.");
                    Cursor cursor = db.rawQuery("SELECT " + c.name + " FROM CoefficientMeans WHERE ObservationNumber = " + (observNumN -1), null);
                    cursor.moveToLast();
                    oldmean = cursor.getDouble(0);
                    Log.d(TAG, "Geholter alter u-Wert ist: " + oldmean);
                    cursor.close();
                }
            }
            else {
                    Log.d(TAG, "Es ist die 1. Beobachtung, also gibt es noch kein altes u, gebe 0.0 zurück");
                    oldmean = 0.0;
            }
            return oldmean;
    }

    public double getOldStdDer(Coefficient c, int observNumN) {
        Log.d(TAG, "Hole altes o von BeobachtungsNr:"+(observNumN-1)+" (Funktion getOldStdDer) (Aktuelle Beob.Nr: "+observNumN);
        double oldstdder;
        if (observNumN>1) {
            SQLiteDatabase db = this.getWritableDatabase();
            Log.d(TAG, "Spaltenname wo gelesen wird ist: " + c.name);
            if (checkIfColumnExists(db, "CoefficientStandardDerivations", c.name) == false) {
                Log.d(TAG, "Eine Solche Spalte exisitiert nicht. Erstellen...");
                db.execSQL("ALTER TABLE CoefficientStandardDerivations ADD COLUMN " + c.name + " REAL");
                Log.d(TAG, "Spalte erstellt.");
                Log.d(TAG, "Da kein alter Wert vorhanden wird 0 returnt (Problematisch?!)");
                return 0.0;
            } else {
                Log.d(TAG, "Spalte für Beobachtung Nr.  existiert. Hole letzten Wert.");
                Cursor cursor = db.rawQuery("SELECT " + c.name + " FROM CoefficientStandardDerivations WHERE ObservationNumber = " + (observNumN -1), null);
                cursor.moveToLast();
                oldstdder = cursor.getDouble(0);
                Log.d(TAG, "Geholter alter o-Wert ist: " + oldstdder);
                cursor.close();
                return oldstdder;
            }
        }
        else {
            Log.d(TAG, "Es ist die 1. Beobachtung, also gibt es noch kein altes o, gebe 0.0 zurück");
            oldstdder = 0.0;
        }
        return oldstdder;
    }

    public void addNewMean(Coefficient c, int observNumN, double valuex) {
        Log.d(TAG, "addNewMean Methode gestartet");
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkIfColumnExists(db, "CoefficientMeans", c.name) == false) {
            Log.d(TAG, "Eine Solche Spalte exisitiert nicht. Erstellen...");
            db.execSQL("ALTER TABLE CoefficientMeans ADD COLUMN " + c.name + " REAL");
            Log.d(TAG, "Spalte erstellt.");
        }
        Log.d(TAG, "Füge neuen Mean ein in:"+c.name+" bei ObsNr "+observNumN+" mit Wert "+valuex);
        db.execSQL("UPDATE CoefficientMeans SET "+c.name+" = "+valuex+" WHERE ObservationNumber="+observNumN);
    }

    public void addNewStdDer(Coefficient c, int observNumN, double valuex) {
        Log.d(TAG, "addNewStdDer Methode gestartet");
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkIfColumnExists(db, "CoefficientStandardDerivations", c.name) == false) {
            Log.d(TAG, "Eine Solche Spalte exisitiert nicht. Erstellen...");
            db.execSQL("ALTER TABLE CoefficientStandardDerivations ADD COLUMN "+c.name+" REAL");
            Log.d(TAG, "Spalte erstellt.");
        }
        Log.d(TAG, "Füge neue StdDer ein in: "+c.name+" bei ObsNr "+observNumN+" mit Wert "+valuex);
        db.execSQL("UPDATE CoefficientStandardDerivations SET "+c.name+" = "+valuex+" WHERE ObservationNumber="+observNumN);
    }

    public void addNewObservationValue(Coefficient c, int observNumN, double zvalue) {
        Log.d(TAG, "addNewObservationValue Methode gestartet");
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkIfColumnExists(db, "Observations", c.name) == false) {
            Log.d(TAG, "Eine Solche Spalte exisitiert nicht. Erstellen...");
            db.execSQL("ALTER TABLE Observations ADD COLUMN " + c.name + " REAL");
            Log.d(TAG, "Spalte erstellt.");
        }
        Log.d(TAG, "Füge neuen Standardisierten Wert ein in:"+c.name+" bei ObsNr "+observNumN+" mit Wert "+zvalue);
        db.execSQL("UPDATE Observations SET "+c.name+" = "+zvalue+" WHERE ObservationNumber="+observNumN);
    }

    public double updateStandardDeviation (double x, double u, int N, double o) {
        System.out.println("übergeben: x:"+x+" u: "+u+" N: "+N+" o: "+o);
        double zaehlerpart1 = (N+1);
        double zaehlerpart2 = Math.pow(x, 2)+N*(Math.pow(o, 2)+Math.pow(u, 2));
        System.out.println("x+n*u ist: "+(x+(N*u)));
        double zaehlerpart3 = Math.pow((x+(N*u)), 2);
        double zaehler = zaehlerpart1*zaehlerpart2-zaehlerpart3;
        System.out.println("o-Berechnungen||Part1: "+zaehlerpart1+" - Part2: "+zaehlerpart2+" - Part3: "+zaehlerpart3);
        double nenner = Math.pow((N+1), 2);
        System.out.println("o-Berechnungen||Zähler: "+zaehler+" Nenner: "+nenner);
        double onew = Math.sqrt(zaehler/nenner);
        return onew;
    }

    private boolean checkIfColumnExists(SQLiteDatabase inDatabase, String inTable, String columnToCheck) {
        Log.d(TAG, "Es wird nun überprüft ob es in ''"+inTable+"'' eine Spalte namens ''"+columnToCheck+"'' gibt.");
        Cursor mCursor = null;
        try {
            // Query 1 row
            Log.d(TAG, "Query machen beim Check...");
            mCursor = inDatabase.rawQuery("SELECT * FROM " + inTable + " LIMIT 0", null);

            // getColumnIndex() gives us the index (0 to ...) of the column - otherwise we get a -1
            if (mCursor.getColumnIndex(columnToCheck) != -1) {
                Log.d(TAG, "Index ist nicht -1 also return true");
                return true;
            }
            else {
                Log.d(TAG, "Index -1 also return false");
                return false;
            }

        } catch (Exception Exp) {
            // Something went wrong. Missing the database? The table?
            Log.d(TAG, "When checking whether a column exists in the table, an error occurred: " + Exp.getMessage());
            return false;
        } finally {
            if (mCursor != null) mCursor.close();
        }
    }




























    /*Von DatabaseManager benötigt
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }*/

}