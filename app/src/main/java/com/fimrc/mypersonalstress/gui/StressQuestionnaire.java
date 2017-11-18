package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

import com.fimrc.mypersonalstress.persistence.DatabaseHelper;
import com.fimrc.mysensornetwork.R;

import java.text.DateFormat;
        import java.util.Date;

public class StressQuestionnaire extends AppCompatActivity {
    public static final String TAG = "StressQuestionnaire";
    public int currentPSSQuestionNumber;
    //public int currentPSSQuestionScore;
    public int currentPSSScore;
    public int[] scoresPSSQuestions;
    public String questionBeginningText;
    public String[] questionEndTexts;
    public Button button1, button2, button3, button4, button5, button6;
    public TextView textView1, textView2, textView3, textView4, textView5, textView6;
    public long aktuellezeit;
    private DatabaseHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Fragebogen gestartet.");
        setContentView(R.layout.activity_stress_questionnaire);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setTitle("Personalisierung");
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        currentPSSScore = 0;
        currentPSSQuestionNumber = 0;
        questionBeginningText = getString(R.string.questionBeginning);
        scoresPSSQuestions = new int[4];
        Resources res = getResources();
        questionEndTexts = res.getStringArray(R.array.questionEndArray);

        textView4.setText(questionBeginningText+""+questionEndTexts[currentPSSQuestionNumber]);

        myDB = new DatabaseHelper(this, "mypersonalstress.db");

        textView5.setVisibility(View.INVISIBLE);
        textView6.setVisibility(View.INVISIBLE);
        button6.setVisibility(View.INVISIBLE);


        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber] = 4;
                nextQuestion();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber] = 3;
                nextQuestion();

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber] = 2;
                nextQuestion();

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber] = 1;
                nextQuestion();

            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scoresPSSQuestions[currentPSSQuestionNumber] = 0;
                nextQuestion();

            }
        });

    }


    public void nextQuestion() {
        System.out.println("Aktuelle Fragenr.:"+currentPSSQuestionNumber);
        if (currentPSSQuestionNumber <3) {
            currentPSSQuestionNumber += 1;
            textView3.setText("Frage "+(currentPSSQuestionNumber+1)+" von 4:");
            textView4.setText(questionBeginningText+""+questionEndTexts[currentPSSQuestionNumber]);
            textView5.setText("Akt. Fr.: "+(currentPSSQuestionNumber+1));
            textView6.setText("Akt. PSS: "+getCurrentPSSScore());
        }
        else if (currentPSSQuestionNumber == 3) {
            Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), "PSS-Score: "+getCurrentPSSScore(), Snackbar.LENGTH_SHORT);
            mySnackbar.show();
            DateFormat df = DateFormat.getDateTimeInstance();
            aktuellezeit = new Date().getTime();
            double scorebuff = getCurrentPSSScore();
            Log.d(TAG, "Ergebnis des Fragebogens ist: "+scorebuff);
            int obs = myDB.getAmountofObservationsDoneYet()+1;
            Log.d(TAG, "Lege die Zeile mit der "+obs+". Beobachtung in allen Tabellen an");
            myDB.addObservationCount(obs);
            Log.d(TAG, "Schreibe neuen Score in PSSScore Tabelle in Beobachtung Nr. "+obs);
            myDB.addNewPSSScore(obs, aktuellezeit, scorebuff);


            Intent intent=new Intent();
            intent.putExtra("MESSAGE", 1);
            setResult(1,intent);
            finish();//finishing activity

        }
    };

    public int getCurrentPSSScore() {
        int buff = 0;
        for (int i=0; i<=3; i++) {
            buff += scoresPSSQuestions[i];
        }
        return buff;
    }

}
