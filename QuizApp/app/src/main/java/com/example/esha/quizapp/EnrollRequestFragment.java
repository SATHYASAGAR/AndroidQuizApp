package com.example.esha.quizapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class EnrollRequestFragment extends Fragment {

    View classEnrollRequestView;

    String currentStudent;

    ArrayList<String> selectedItems;
    ArrayList<String> classesList;
    ArrayList<String> classesEnrolled;

    Button addClassesButton;

    FirebaseDatabase database;
    FirebaseAuth authInstance;

    ArrayAdapter<String> classesEnrolledListAdapter;

    Object nameOfStudent;

    public EnrollRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Enroll to a class");
        Firebase.setAndroidContext(getContext());
        database = FirebaseDatabase.getInstance();
        classesList = new ArrayList<>();
        authInstance = FirebaseAuth.getInstance();
        currentStudent = authInstance.getCurrentUser().getDisplayName();
        classesEnrolled = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        classEnrollRequestView = inflater.inflate(R.layout.fragment_enroll_request, container, false);
        selectedItems = new ArrayList<String>();
        addClassesButton = (Button) classEnrollRequestView.findViewById(R.id.addClassesButton);
        return classEnrollRequestView;
    }

    public void onStart() {
        super.onStart();
        ListView classesListView = (ListView) classEnrollRequestView.findViewById(R.id.checkable_list);
        classesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        final DatabaseReference databaseReference = database.getReference();
        //getting status data

        databaseReference.child("Students").child(currentStudent).child("ClassStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (final DataSnapshot child : children) {
                    String status = child.getValue().toString();// status
                    status = status.replaceAll("\\{", " ");
                    status = status.replaceAll("\\}", " ");
                    status = status.substring(status.lastIndexOf("=") + 1);
                    if (status.trim().equals("Enrolled")) {
                        classesEnrolled.add(child.getKey());
                    }
                }

                //getting status data ends here
                databaseReference.child("Classes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (final DataSnapshot child : children) {
                            String instructId = child.getKey();
                            DatabaseReference userReference = database.getReference();
                            userReference.child("Classes").child(instructId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Iterable<DataSnapshot> childrenTwo = dataSnapshot.getChildren();
                                    for (DataSnapshot childTwo : childrenTwo) {
                                        if (!classesList.contains(childTwo.getKey()) && !classesEnrolled.contains(childTwo.getKey())) {
                                            classesList.add(childTwo.getKey());
                                        }
                                        classesEnrolledListAdapter.notifyDataSetChanged();
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

                // ending here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        classesEnrolledListAdapter = new ArrayAdapter<String>(getContext(), R.layout.enroll_request_checkable_list, R.id.txt_title, classesList);
        classesListView.setAdapter(classesEnrolledListAdapter);
        classesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem);
                else
                    selectedItems.add(selectedItem);
            }
        });

        addClassesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addSelectedClasses(view);
                Toast.makeText(getContext(), "Request sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addSelectedClasses(View view) {

        for (final String item : selectedItems) {
            Calendar dateCalender = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
            final String dateToday = sdf.format(dateCalender.getTime());
            //getting the logged in name
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = database.getReference();
            databaseReference.child("Students").child(currentStudent).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    nameOfStudent = dataSnapshot.getValue();
                    Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com");
                    ClassStatus status = new ClassStatus(nameOfStudent.toString(), false, dateToday, "null");
                    sRef.child("AddClassRequest").child(item).child(currentStudent).setValue(status);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            //ends here
        }
        for (String item : selectedItems) {
            Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com");
            ClassEnrollStatus status = new ClassEnrollStatus();
            status.setStatus("Pending");
            sRef.child("Students").child(currentStudent).child("ClassStatus").child(item).setValue(status);
        }
    }
}

