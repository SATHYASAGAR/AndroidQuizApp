package com.example.esha.quizapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        FragmentManager fragments = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
        LoginStudent fragment = new LoginStudent();
        fragmentTransaction.replace(R.id.placeHolder, fragment);
        fragmentTransaction.commit();
    }
}
