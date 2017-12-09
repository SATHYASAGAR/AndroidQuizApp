package com.example.esha.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class StudentProfileActivity extends AppCompatActivity {

    private static final String TAG = "finalProject";

    Integer testScore;

    EditText testScoreField;

    Button goBackToHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        testScoreField = (EditText) findViewById(R.id.testScoreTextID);

        testScore = getIntent().getIntExtra("testScore", 0);

        goBackToHomeButton = (Button) findViewById(R.id.goBackToHomeButtonID);
        Log.i(TAG, "test Score after intent : " + testScore);

        testScoreField.setEnabled(false);
        testScoreField.setText(testScore.toString());

        goBackToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Default Back Button Disabled", Toast.LENGTH_SHORT).show();
    }
}
