package com.example.esha.quizapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestsTakenFragment extends Fragment {
    View testsTakenView;

    ExpandableListView testsTakenListView;
    ArrayList<String> classHeader;

    List<String> listDataHeader;

    HashMap<String, List<String>> classTestHash;

    android.widget.ExpandableListAdapter listAdapter;

    String currentStudent;

    FirebaseAuth authInstance;

    Object scoreToDisplay;
    Object scheduledDateToDisplay;

    public TestsTakenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Tests Taken");

        Firebase.setAndroidContext(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        testsTakenView = inflater.inflate(R.layout.fragment_tests_taken, container, false);
        testsTakenListView = (ExpandableListView) testsTakenView.findViewById(R.id.testsTakenListViewID);
        authInstance = FirebaseAuth.getInstance();
        classHeader = new ArrayList<>();

        classTestHash = new HashMap<>();
        currentStudent = authInstance.getCurrentUser().getDisplayName();
        listDataHeader = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Students").child(currentStudent).child("AvailableTests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (final DataSnapshot child : children) {
                    final String classNameToPut = child.getKey();
                    if (!classHeader.contains(child.getKey())) {
                        classHeader.add(child.getKey());
                    }
                    final String subjectName = child.getKey();
                    databaseReference.child("Students").child(currentStudent).child("AvailableTests").child(child.getKey().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            final ArrayList<String> testNames = new ArrayList<>();
                            for (final DataSnapshot child : children) {

                                databaseReference.child("Students").child(currentStudent).child("AvailableTests").child(classNameToPut.toString()).child(child.getKey().toString()).child("testTaken").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot snapTestTakenTrueOrFalse) {

                                        databaseReference.child("Students").child(currentStudent).child("AvailableTests").child(classNameToPut.toString()).child(child.getKey().toString()).child("dateScheduled").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshotDateScheduled) {
                                                scheduledDateToDisplay = dataSnapshotDateScheduled.getValue();
                                                databaseReference.child("Students").child(currentStudent).child("AvailableTests").child(classNameToPut.toString()).child(child.getKey().toString()).child("score").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshotScore) {
                                                        scoreToDisplay = dataSnapshotScore.getValue();
                                                       try {
                                                           if (snapTestTakenTrueOrFalse.getValue().toString().equals("true")) {
                                                               testNames.add(child.getKey() + "\n" + "Test taken on: " + scheduledDateToDisplay + "\n" + "Score = " + scoreToDisplay);
                                                           }
                                                       }catch (Exception e){
                                                           }
                                                        classTestHash.put(subjectName, testNames);
                                                        listAdapter = new com.example.esha.quizapp.ExpandableListAdapter(getContext(), classHeader, classTestHash);
                                                        testsTakenListView.setAdapter(listAdapter);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return testsTakenView;
    }
}
