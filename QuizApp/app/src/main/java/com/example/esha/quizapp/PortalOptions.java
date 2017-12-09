package com.example.esha.quizapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PortalOptions extends AppCompatActivity {
    TextView welcomeProfessorText, setTestNameText, setClassNameText;

    Button addTest, addClass;

    String testName;
    String dateUploaded;
    String classNameVariable;

    EditText className;
    EditText setNameHere;

    TextView setName;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_options);
        setTestNameText = (TextView) findViewById(R.id.setNameId);
        welcomeProfessorText = (TextView) findViewById(R.id.infoID);
        setClassNameText = (TextView) findViewById(R.id.addClassId);
        addClass = (Button) findViewById(R.id.addClassButtonId);
        className = (EditText) findViewById(R.id.testNameId);
        Firebase.setAndroidContext(getBaseContext());
        addTest = (Button) findViewById(R.id.addTestId);
        setName = (TextView) findViewById(R.id.setNameId);
        setNameHere = (EditText) findViewById(R.id.setNameHere);
        Typeface mytypeface = Typeface.createFromAsset(getAssets(), "BodoniFLF-Roman.ttf");
        setTestNameText.setTypeface(mytypeface);
        welcomeProfessorText.setTypeface(mytypeface);
        setClassNameText.setTypeface(mytypeface);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddClass();
            }
        });
        addTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddTest();
            }
        });
    }

    public void goToAddTest() {
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        final String strdate1 = sdf.format(c1.getTime());
        dateUploaded = strdate1;
        testName = setNameHere.getText().toString();
        databaseReference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    String redid = child.getKey();
                    Firebase sReference = new Firebase("https://quizapp-d5f3c.firebaseio.com/Students/" + redid);
                    AvailableTest testDetails = new AvailableTest();
                    sReference.child("AvailableTest").child(testName + "-" + dateUploaded).setValue(testDetails);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Intent i = new Intent(PortalOptions.this, AddQuestionsToTestActivity.class);
        i.putExtra("testName", testName);
        startActivity(i);
    }

    public void goToAddClass() {
        Calendar c4 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        final String dateClassForm = sdf.format(c4.getTime());
        classNameVariable = className.getText().toString();
        Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com/Classes");
        ClassListClass classObject2 = new ClassListClass();
        classObject2.setDateClassFormed(dateClassForm);
        sRef.child(classNameVariable).setValue(classObject2);
        Toast.makeText(this, "adding class successful", Toast.LENGTH_SHORT).show();
    }
}
