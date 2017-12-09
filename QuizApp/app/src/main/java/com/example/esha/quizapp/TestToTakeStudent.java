package com.example.esha.quizapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestToTakeStudent extends AppCompatActivity {
    ArrayList<String> testTotake;
    ListView listOfTests;
    ArrayAdapter<String> adapter;
    ArrayList<String> listOfProfiles = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    TextView inoTestToTake;

    String currentUser;
    String valueToProfilePage;
    String testNumber;

    FirebaseAuth authInstance;

    Object name;

    Button addClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_to_take_student);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        addClass = (Button) findViewById(R.id.addClassButtonId);
        inoTestToTake = (TextView) findViewById(R.id.infoTestToTake);
        Spinner spinner = (Spinner) findViewById(R.id.profileSpinnerID);
        listOfTests = (ListView) findViewById(R.id.list);
        authInstance = FirebaseAuth.getInstance();
        currentUser = authInstance.getCurrentUser().getDisplayName();
        final DatabaseReference databaseReference = database.getReference();
        Typeface mytypeface = Typeface.createFromAsset(getAssets(), "BodoniFLF-Roman.ttf");
        inoTestToTake.setTypeface(mytypeface);
        Firebase.setAndroidContext(getBaseContext());
        testTotake = new ArrayList<>();
        listOfProfiles.clear();
        listOfProfiles.add("null");
        listOfProfiles.add("Students");
        listOfProfiles.add("Professor");
        arrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, listOfProfiles);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueToProfilePage = parent.getItemAtPosition(position).toString();
                if (valueToProfilePage.equals("Students")) {
                    Intent i = new Intent(TestToTakeStudent.this, ProfileContainer.class);
                    i.putExtra("value", valueToProfilePage);
                    startActivity(i);
                } else if (valueToProfilePage.equals("Professor")) {
                    Intent i = new Intent(TestToTakeStudent.this, ProfileContainer.class);
                    i.putExtra("value", valueToProfilePage);
                    startActivity(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listOfTests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                testNumber = (String) parent.getItemAtPosition(position);
                callTestQuestionActivity();
            }
        });
        // for setting the logged in user name
        databaseReference.child("Students").child(currentUser).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue();
                inoTestToTake.setText("Welcome" + " " + name.toString() + "." + " " + "Lets get you started!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // for setting the logged in user name ends here

        databaseReference.child("testdata").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    testTotake.add(child.getKey());
                    adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, testTotake);
                    listOfTests.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToClassList();
            }
        });

    }

    public void callTestQuestionActivity() {
        Intent i = new Intent(this, TestQuestionActivity.class);
        i.putExtra("testName", testNumber);
        startActivity(i);
    }

    public void goToClassList() {

        Intent i = new Intent(this, ClassListActivity.class);
        startActivity(i);
    }
}
