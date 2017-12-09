package com.example.esha.quizapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfile extends Fragment {

    String currentUser;

    FirebaseAuth authInstance;

    ArrayAdapter<String> arrayStudents;

    ListView listOfStudentsToDisplay;

    ArrayList<String> list;

    Object studentNames;

    public StudentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_profile, container, false);
        Firebase.setAndroidContext(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        listOfStudentsToDisplay = (ListView) v.findViewById(R.id.listOfStudentsId);
        final DatabaseReference databaseReference = database.getReference();
        authInstance = FirebaseAuth.getInstance();
        currentUser = authInstance.getCurrentUser().getDisplayName();
        list = new ArrayList<>();
        listOfStudentsToDisplay.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
            }
        });

        databaseReference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    String redid = child.getKey();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference srefer = database.getReference();
                    srefer.child("Students").child(redid).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            studentNames = dataSnapshot.getValue();
                            list.add(studentNames.toString());
                            arrayStudents = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
                            listOfStudentsToDisplay.setAdapter(arrayStudents);
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
        return v;
    }
}
