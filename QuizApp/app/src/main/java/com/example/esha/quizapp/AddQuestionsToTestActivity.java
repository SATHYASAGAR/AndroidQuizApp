package com.example.esha.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddQuestionsToTestActivity extends AppCompatActivity {
    Button submit, goToNext, finish;

    int maxId;
    int countOfQuestions;

    String testNumber;

    EditText getQuestion, getChoice1, getChoice2, getChoice3, getChoice4, getCorrectAnswer;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Create Test");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        countOfQuestions = 1;
        Intent intent = getIntent();
        testNumber = intent.getStringExtra("testName");
        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        finish = (Button) findViewById(R.id.finishId);
        finish.setEnabled(false);
        getQuestion = (EditText) findViewById(R.id.getQuestionid);
        getChoice1 = (EditText) findViewById(R.id.choice1ID);
        getChoice2 = (EditText) findViewById(R.id.choice2Id);
        getChoice3 = (EditText) findViewById(R.id.choice3ID);
        getChoice4 = (EditText) findViewById(R.id.choice4ID);
        getCorrectAnswer = (EditText) findViewById(R.id.correctAnswerId);
        submit = (Button) findViewById(R.id.submit);
        goToNext = (Button) findViewById(R.id.nextQuestionId);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataToFireBase();

            }
        });
        goToNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNextQuestion();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        databaseReference.child("testdata").child(testNumber).limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    maxId = Integer.parseInt(child.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addDataToFireBase() {
        if (!(getCorrectAnswer.getText().toString().equals(getChoice1.getText().toString()) ||
                getCorrectAnswer.getText().toString().equals(getChoice2.getText().toString()) ||
                getCorrectAnswer.getText().toString().equals(getChoice3.getText().toString()) ||
                getCorrectAnswer.getText().toString().equals(getChoice4.getText().toString()))) {
            Toast.makeText(this, "Correct answer should be one of the choices", Toast.LENGTH_SHORT).show();
            return;
        } else if (getChoice1.getText().toString().length() == 0 ||
                getChoice2.getText().toString().length() == 0 ||
                getChoice3.getText().toString().length() == 0 ||
                getChoice4.getText().toString().length() == 0 ||
                getCorrectAnswer.getText().toString().length() == 0 ||
                getQuestion.getText().toString().length() == 0) {
            Toast.makeText(this, "Enter all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com/testdata");
        Questions question = new Questions(
                getQuestion.getText().toString(),
                getChoice1.getText().toString(),
                getChoice2.getText().toString(),
                getChoice3.getText().toString(),
                getChoice4.getText().toString(),
                getCorrectAnswer.getText().toString()
        );

        sRef.child(testNumber).child(String.valueOf(maxId + 1)).setValue(question);
        maxId++;
        Toast.makeText(this, "Question Uploaded", Toast.LENGTH_SHORT).show();
        finish.setEnabled(true);
    }

    public void writeNextQuestion() {
        getQuestion.setText("");
        getChoice1.setText("");
        getChoice2.setText("");
        getChoice3.setText("");
        getChoice4.setText("");
        getCorrectAnswer.setText("");
    }
}
