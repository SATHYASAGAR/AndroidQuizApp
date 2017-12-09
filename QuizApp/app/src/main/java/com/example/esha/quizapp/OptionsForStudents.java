package com.example.esha.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsForStudents extends AppCompatActivity {
    Button viewTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_for_students);
        viewTest = (Button) findViewById(R.id.viewTestID);
        viewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToQuestionActivity();
            }
        });
    }

    public void goToQuestionActivity() {

    }
}
