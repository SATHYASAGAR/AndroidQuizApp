package com.example.esha.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTestActivity extends AppCompatActivity {

    Button addTestToClass;

    EditText testNameForClass, dateScheduledTotest;

    FirebaseAuth authInstance;

    String className;
    String testNameForClassHere;
    String currentProfessor;

    ArrayList<String> redidArrayList;
    ArrayList<String> testFromDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Test");
        setContentView(R.layout.activity_add_test);
        Firebase.setAndroidContext(getBaseContext());
        authInstance = FirebaseAuth.getInstance();
        className = getIntent().getStringExtra("nameOfClass");
        redidArrayList=new ArrayList<>();
        testFromDatabase=new ArrayList<>();
        currentProfessor = authInstance.getCurrentUser().getDisplayName();
        addTestToClass = (Button) findViewById(R.id.addTestButtonid);
        testNameForClass = (EditText)findViewById(R.id.nameOfTestId);
        dateScheduledTotest = (EditText) findViewById(R.id.dateScheduledInputId);
        testNameForClassHere = testNameForClass.getText().toString();
        addTestToClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTestToFirebase();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        databaseReference.child("testdata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    testFromDatabase.add(child.getKey());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addTestToFirebase() {

        if(testFromDatabase.contains(testNameForClass.getText().toString())){
            testNameForClass.setError("Test name already exists");
            return;
        }

        if(testNameForClass.getText().toString().length()==0){
            testNameForClass.setError("Enter a test name");
            return;
        }

        Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com");
        sRef.child("Classes").child(currentProfessor).child(className).child("Tests").child(testNameForClass.getText().toString()).setValue(dateScheduledTotest.getText().toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Classes").child(currentProfessor).child(className).child("StudentInClass").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Calendar c2 = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
                final String dateToday = sdf.format(c2.getTime());

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    redidArrayList.add(child.getKey().toString());
                }

                for (String elementRedId: redidArrayList) {
                    AvailableTest objectToPut= new AvailableTest(dateToday,0,false);
                    databaseReference.child("Students").child(elementRedId).child("AvailableTests").child(className).child(testNameForClass.getText().toString()).setValue(objectToPut);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Intent i = new Intent(getBaseContext(), AddQuestionsToTestActivity.class);
        i.putExtra("testName", testNameForClass.getText().toString());
        startActivity(i);
    }
}
