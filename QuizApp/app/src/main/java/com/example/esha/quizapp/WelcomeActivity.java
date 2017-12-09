package com.example.esha.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    Button student, professor;

    TextView textspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        this.setTitle("Welcome");
        student = (Button) findViewById(R.id.studentOptionId);
        textspace = (TextView) findViewById(R.id.welcomeIDHere);
        professor = (Button) findViewById(R.id.professorOptionId);
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToContainerActivity();
            }
        });
        Typeface mytypeface = Typeface.createFromAsset(getAssets(), "BodoniFLF-Roman.ttf");
        textspace.setTypeface(mytypeface);
        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToContainerForProfessor();
            }
        });
    }

    public void goToContainerActivity() {
        Intent i = new Intent(this, ContainerActivity.class);
        startActivity(i);
    }

    public void goToContainerForProfessor() {
        Intent i = new Intent(this, ContainerForProfessor.class);
        startActivity(i);
    }

}
