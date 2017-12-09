package com.example.esha.quizapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ProfileContainer extends AppCompatActivity {
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_container);
        FragmentManager fragments = getSupportFragmentManager();
        Intent intent = getIntent();
        value = intent.getStringExtra("value");
        if (value.equals("Students")) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
            StudentProfile fragment = new StudentProfile();
            fragmentTransaction.replace(R.id.profileHolder, fragment);
            fragmentTransaction.commit();
        } else if (value.equals("Professor")) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
            ProfessorProfile fragment = new ProfessorProfile();
            fragmentTransaction.replace(R.id.profileHolder, fragment);
            fragmentTransaction.commit();

        }
    }
}
