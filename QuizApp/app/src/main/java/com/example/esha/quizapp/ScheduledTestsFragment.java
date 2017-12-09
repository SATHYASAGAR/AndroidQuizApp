package com.example.esha.quizapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduledTestsFragment extends Fragment {
    View scheduledTestsView;

    ExpandableListView scheduledTestsListView;
    ExpandableListAdapter listAdapter;

    List<String> listDataHeader;

    HashMap<String, List<String>> classTestHash;

    String currentStudent;

    FirebaseAuth authInstance;

    ArrayList<String> classHeader;


    public ScheduledTestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getContext());
        getActivity().setTitle("Scheduled Tests");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        scheduledTestsView = inflater.inflate(R.layout.fragment_scheduled_tests, container, false);
        scheduledTestsListView = (ExpandableListView) scheduledTestsView.findViewById(R.id.scheduledTestsListViewID);
        authInstance = FirebaseAuth.getInstance();
        classHeader = new ArrayList<>();

        classTestHash = new HashMap<>();
        currentStudent = authInstance.getCurrentUser().getDisplayName();
        listDataHeader = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Students").child(currentStudent).child("AvailableTests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshotAvailableTests) {
                final Iterable<DataSnapshot> childrenOfAvailableTests = dataSnapshotAvailableTests.getChildren();
                for (final DataSnapshot childAvailableTests : childrenOfAvailableTests) {
                    if (!classHeader.contains(childAvailableTests.getKey())) {
                        classHeader.add(childAvailableTests.getKey());
                    }
                    final String subjectName = childAvailableTests.getKey();

                    databaseReference.child("Students").child(currentStudent).child("AvailableTests").child(subjectName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            final ArrayList<String> testNames = new ArrayList<>();
                            for (final DataSnapshot child : children) {
                                databaseReference.child("Students").child(currentStudent).child("AvailableTests").child(subjectName).child(child.getKey().toString()).child("testTaken").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try{
                                        if (dataSnapshot.getValue().toString().equals("false") && !testNames.contains(child.getKey())) {
                                            testNames.add(child.getKey());
                                        }
                                        }
                                        catch(Exception e){
                                            }
                                        classTestHash.put(subjectName, testNames);
                                        listAdapter = new com.example.esha.quizapp.ExpandableListAdapter(getContext(), classHeader, classTestHash);
                                        scheduledTestsListView.setAdapter(listAdapter);
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

        scheduledTestsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                String testName = classTestHash.get(classHeader.get(groupPosition)).get(childPosition);
                Intent go = new Intent(getContext(), TestInstructionsActivity.class);
                go.putExtra("className", classHeader.get(groupPosition));
                go.putExtra("testName", testName);
                startActivity(go);
                return true;
            }
        });

        return scheduledTestsView;


    }
}
