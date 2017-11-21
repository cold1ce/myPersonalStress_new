package com.fimrc.mypersonalstress.gui;

import android.content.Intent;
import android.content.SharedPreferences;
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
    public int currentPSSScore, personalizationtimewindow, currentPSSQuestionNumber;
    public int[] scoresPSSQuestions;
    public String questionBeginningText, questionMiddleText;
    public String[] questionEndTexts;
    public Button button1, button2, button3, button4, button5, button6;
    public TextView tv_instruction, textView1, textView2, textView3, textView4;
    public long aktuellezeit;
    private DatabaseHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stress_questionnaire);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final SharedPreferences prefs = getSharedPreferences("mps_preferences", MODE_PRIVATE);
        this.setTitle("Personalisierung");
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);

        tv_instruction = (TextView) findViewById(R.id.tv_instruction);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        currentPSSScore = 0;
        currentPSSQuestionNumber = 0;
        personalizationtimewindow = prefs.getInt("observationtimeframe", 1);
        questionBeginningText = getString(R.string.questionBeginning);
        questionMiddleText = getString(R.string.questionMiddle);
        scoresPSSQuestions = new int[4];
        Resources res = getResources();
        questionEndTexts = res.getStringArray(R.array.questionEndArray);

        textView4.setText(questionBeginningText+" "+personalizationtimewindow+" "+questionMiddleText+" "+questionEndTexts[currentPSSQuestionNumber]);

        myDB = new DatabaseHelper(this, "mypersonalstress.db");




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
            textView4.setText(questionBeginningText+" "+personalizationtimewindow+" "+questionMiddleText+" "+questionEndTexts[currentPSSQuestionNumber]);
            //textView4.setText(questionBeginningText+""+questionEndTexts[currentPSSQuestionNumber]);

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
            button1.setEnabled(false);
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            button5.setEnabled(false);
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
            button5.setVisibility(View.INVISIBLE);
            textView3.setVisibility(View.INVISIBLE);
            textView4.setVisibility(View.INVISIBLE);
            tv_instruction.setText("Personalisierung wird durchgeführt. Bitte warten Sie...");


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
