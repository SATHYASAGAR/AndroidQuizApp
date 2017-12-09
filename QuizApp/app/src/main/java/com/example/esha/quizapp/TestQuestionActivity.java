package com.example.esha.quizapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TestQuestionActivity extends AppCompatActivity {
    String TAG = "finalProject";
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private List<String> listDataHeadertest;
    private HashMap<String, List<String>> listHash;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();
    FirebaseAuth authInstance;

    ArrayList<ArrayList<String>> question1;
    ArrayList<String> choices;
    ArrayList<ArrayList<String>> allChoices;

    String testNumber;
    String className;
    String currentUser;

    int testScoreResult;

    private HashMap<String, String> questionAnswerHash;
    private HashMap<String, String> questionAnswerSelectedHash;

    Integer testScore = 0;

    Integer i = 0;

    Button submitButton;
    Button timerButton;

    Boolean allAnswersSelected = false;

    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);
        Intent intent = getIntent();
        testNumber = intent.getStringExtra("testName");
        className = intent.getStringExtra("className");
        authInstance = FirebaseAuth.getInstance();
        currentUser = authInstance.getCurrentUser().getDisplayName();
        submitButton = (Button) this.findViewById(R.id.submitButtonID);
        timerButton = (Button) this.findViewById(R.id.timerButton);
        question1 = new ArrayList<ArrayList<String>>();
        listView = (ExpandableListView) findViewById(R.id.lvExp);
        allChoices = new ArrayList<>();
        initData();
        setCountDownTimer();
        countDownTimer.start();
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);

                questionAnswerSelectedHash.put(listDataHeader.get(groupPosition),
                        listHash.get(listDataHeader.get(groupPosition)).get(childPosition));
                return true;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                countDownTimer.cancel();
                score();
            }
        });

    }

    public void setCountDownTimer() {

        countDownTimer = new CountDownTimer(900000, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                timerButton.setText(hms);
            }

            public void onFinish() {
                timerButton.setText("Done!");
                score();
            }
        };
    }

    private void score() {

        calculateScore(questionAnswerHash, questionAnswerSelectedHash);

        if (allAnswersSelected) {
            for (Map.Entry<String, String> entry : questionAnswerHash.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
            }
            for (Map.Entry<String, String> entry : questionAnswerSelectedHash.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
            }

            Intent go = new Intent(this, StudentProfileActivity.class);
            go.putExtra("testScore", testScore);
            testScoreResult = testScore;
            testScore = 0;

            Calendar c2 = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
            final String dateToday = sdf.format(c2.getTime());
            AvailableTest testDetails = new AvailableTest();
            testDetails.setDateScheduled(dateToday);
            testDetails.setScore(testScoreResult);
            testDetails.setTestTaken(true);
            databaseReference.child("Students").child(currentUser).child("AvailableTests").child(className).child(testNumber).setValue(testDetails);

            startActivity(go);
            finish();
        }
    }

    private void calculateScore(Map<String, String> m1, Map<String, String> m2) {
        if (m1.size() != m2.size()) {
            Toast.makeText(this, "Some questions not attempted!", Toast.LENGTH_SHORT).show();
        }

        allAnswersSelected = true;
        for (String key : m1.keySet())
            if (m1.get(key).equals(m2.get(key)))
                testScore++;
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listDataHeadertest = new ArrayList<>();
        listHash = new HashMap<>();
        questionAnswerHash = new HashMap<>();
        questionAnswerSelectedHash = new HashMap<>();
        databaseReference.child("testdata").child(testNumber).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    Questions question = child.getValue(Questions.class);
                    choices = new ArrayList<>();
                    choices.add(question.getChoice1());
                    choices.add(question.getChoice2());
                    choices.add(question.getChoice3());
                    choices.add(question.getChoice4());

                    allChoices.add(choices);
                    listDataHeader.add(question.getQuestion());
                    questionAnswerHash.put(question.getQuestion(), question.correctAnswer);
                    listHash.put(listDataHeader.get(i), allChoices.get(i));
                    i++;
                }
                listAdapter = new com.example.esha.quizapp.ExpandableListAdapter(getBaseContext(), listDataHeader, listHash);
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Default Back Button Disabled", Toast.LENGTH_SHORT).show();

    }


}
