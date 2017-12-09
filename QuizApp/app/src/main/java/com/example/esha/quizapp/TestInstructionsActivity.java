package com.example.esha.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestInstructionsActivity extends AppCompatActivity {

    Button beginTestButton;
    String testName;
    String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_instructions);
        testName = getIntent().getStringExtra("testName");
        className = getIntent().getStringExtra("className");

        beginTestButton = (Button) this.findViewById(R.id.beginTestButtonID);

        beginTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getBaseContext(), TestQuestionActivity.class);
                go.putExtra("testName", testName);
                go.putExtra("className", className);

                startActivity(go);
                finish();
            }
        });
    }
}
