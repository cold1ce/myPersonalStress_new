//StressQuestionnaire.java

//Hier werden über eine Bedienoberfläche die gewünschten Fragen zum Stresslevel gestellt und
//der daraus resultierende Wert in die Datenbank gespeichert.

package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;
import java.text.DateFormat;
import java.util.Date;

import static android.view.View.INVISIBLE;

public class StressQuestionnaire extends AppCompatActivity {

    //Definieren der für die Stressermittlung benötigten Objekte
    public static final String TAG = "StressQuestionnaire"; //Für Debugging-Funktion
    public int currentPSSScore, personalizationtimewindow, currentPSSQuestionNumber;
    public int[] scoresPSSQuestions;
    public String questionBeginningText, questionMiddleText;
    public String[] questionEndTexts;
    public Button btn_1, btn_2, btn_3, btn_4, btn_5;
    public TextView tv_instruction, tv_questioncounter, tv_questiontext;
    public long aktuellezeit;
    private DatabaseHelper myDB;
    public ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialisieren der GUI mit den entsprechenden Komponenten
        setContentView(R.layout.activity_stress_questionnaire);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        this.setTitle("Personalisierung");
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        tv_instruction = (TextView) findViewById(R.id.tv_instruction);
        tv_questioncounter = (TextView) findViewById(R.id.tv_questioncounter);
        tv_questiontext = (TextView) findViewById(R.id.tv_questiontext);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        progressbar.setVisibility(INVISIBLE);

        //Initialisieren und Abrufen der benötigten Variablen
        currentPSSScore = 0;
        currentPSSQuestionNumber = 0;
        personalizationtimewindow = prefs.getInt("observationtimeframe", 1);
        questionBeginningText = getString(R.string.questionBeginning);
        questionMiddleText = getString(R.string.questionMiddle);
        //Array mit Anzahl der Fragen die gestellt werden sollen
        //Anmerkung: Bei 4 Fragen muss in den [] die Zahl 4 eingetragen werden
        scoresPSSQuestions = new int[4];
        Resources res = getResources();
        questionEndTexts = res.getStringArray(R.array.questionEndArray);

        //GUI anpassen und erste Frage stellen, entsprechend die Laufvariable currentPSSQuestion erhöhen
        currentPSSQuestionNumber += 1;
        tv_questioncounter.setText("Frage "+(currentPSSQuestionNumber)+" von "+scoresPSSQuestions.length+":");
        tv_questiontext.setText(questionBeginningText+" "+personalizationtimewindow+" "+questionMiddleText+" "+questionEndTexts[currentPSSQuestionNumber-1]);

        //Datenbank initialisieren und somit das Speichern und Abrufen von Werten ermöglichen
        myDB = new DatabaseHelper(this, "mypersonalstress.db");


        //OnClick-Listener der Antwort-Buttons - Je nach Auswahl wird temporär im scoresPSSQuestion-Array
        //die entsprechende Bewertung für die aktuelle Frage abgespeichert, und dann die Methode
        //nextQuestion aufgerufen, um zur nächsten Frage voranzuschreiten.
        btn_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber-1] = 4;
                nextQuestion();

            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber-1] = 3;
                nextQuestion();

            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber-1] = 2;
                nextQuestion();

            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber-1] = 1;
                nextQuestion();

            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber-1] = 0;
                nextQuestion();

            }
        });

    }

    //nextQuestion-Methode, erhöht die Laufvariable currentPSSQuestionNumber und überprüft anschließend
    //ob schon alle Fragen gestellt wurden. Wenn nicht wird auf der Bedienoberfläche die nächste Frage
    //ausgegeben. Ansonsten wird die Methode calcTotalScore abgerufen, welche die Bewertungen der
    //einzelnen Fragen zusammenzählt und damit ein Gesamtergebnis errechnet. Dieses wird mit Zeit-
    //stempel in der Datenbank abgespeichert.
    public void nextQuestion() {
        currentPSSQuestionNumber += 1;
        if (currentPSSQuestionNumber <= scoresPSSQuestions.length) {
            //System.out.println("Aktuelle Fragenr.:"+currentPSSQuestionNumber);
            tv_questioncounter.setText("Frage "+(currentPSSQuestionNumber)+" von "+scoresPSSQuestions.length+":");
            tv_questiontext.setText(questionBeginningText+" "+personalizationtimewindow+" "+questionMiddleText+" "+questionEndTexts[currentPSSQuestionNumber-1]);
            //tv_questiontext.setText(questionBeginningText+""+questionEndTexts[currentPSSQuestionNumber]);
        }
        else if (currentPSSQuestionNumber > scoresPSSQuestions.length) {
            //Datum abrufen um den Score mit Zeitstempel speichern zu können.
            DateFormat df = DateFormat.getDateTimeInstance();
            aktuellezeit = new Date().getTime();
            //Gesamtscore berechnen
            double scorebuff = calcTotalScore();
            Log.d(TAG, "Ergebnis des Fragebogens ist: "+scorebuff);
            //Erhöhe die Zählvariable der durchgeführten Observationen
            int obs = myDB.getAmountofObservationsDoneYet()+1;
            Log.d(TAG, "Lege die Zeile mit der "+obs+". Beobachtung in allen Tabellen an");
            //Füge neue Zeile in Datenbank ein für die aktuelle Beobachtung
            myDB.addObservationCount(obs);
            Log.d(TAG, "Schreibe neuen Score in PSSScore Tabelle in Beobachtung Nr. "+obs);
            //Schreibe das Ergebnis mit Zeitstempel in die Datenbank
            myDB.addNewPSSScore(obs, aktuellezeit, scorebuff);

            //Da nun im Anschluss die rechenintensive Personalisierung beginnt und erst nach dem
            //Rechenvorgang zur Ergebnisseite weitergeleitet wird, muss verhindert werden, dass
            //auf der aktuellen Oberfläche weitere Interaktionen getätigt werden. Deshalb werden
            //alle bisherigen Elemente ausgeblendet und eine Benachrichtigung eingeblendet, dass die
            //Personalisierung im Gange ist.
            btn_1.setEnabled(false);
            btn_2.setEnabled(false);
            btn_3.setEnabled(false);
            btn_4.setEnabled(false);
            btn_5.setEnabled(false);
            btn_1.setVisibility(INVISIBLE);
            btn_2.setVisibility(INVISIBLE);
            btn_3.setVisibility(INVISIBLE);
            btn_4.setVisibility(INVISIBLE);
            btn_5.setVisibility(INVISIBLE);
            tv_questioncounter.setVisibility(INVISIBLE);
            tv_questiontext.setVisibility(INVISIBLE);
            tv_instruction.setText("Personalisierung wird durchgeführt. Bitte warten Sie...");
            progressbar.setVisibility(View.VISIBLE);


            Intent intent=new Intent();
            intent.putExtra("continue", 1);
            setResult(1,intent);
            finish();//Beenden der Aktivität

        }
    };

    //Zusammenzählen der einzelnen Fragebewertungen zu einem Gesamtergebnis
    public int calcTotalScore() {
        int buff = 0;
        for (int i=0; i<scoresPSSQuestions.length; i++) {
            buff += scoresPSSQuestions[i];
        }
        return buff;
    }

}
