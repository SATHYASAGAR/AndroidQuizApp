package com.example.esha.quizapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContainerForProfessor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_for_professor);
        FragmentManager fragments = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
        LoginProfessor fragment = new LoginProfessor();
        fragmentTransaction.replace(R.id.holderProfessor, fragment);
        fragmentTransaction.commit();
    }
}
