package com.example.esha.quizapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
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
public class ViewScoresFragment extends Fragment {

    Spinner classSpinner;
    Spinner testSpinner;

    ArrayList<String> classSpinnerArrayList;
    ArrayList<String> testSpinnerArrayList;
    List<String> listDataHeaderRedIds;

    String currentProfessor;
    String className;
    String testName;
    String score;

    FirebaseAuth authInstance;

    ListView viewScoresListView;

    View viewScoresView;

    HashMap<String, List<String>> testScoreHash;

    ArrayAdapter<String> classAdapter;
    ArrayAdapter<String> testAdapter;

    ArrayAdapter<String> listAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("View Scores");
        Firebase.setAndroidContext(getContext());
        authInstance = FirebaseAuth.getInstance();
        currentProfessor = authInstance.getCurrentUser().getDisplayName();
    }

    public ViewScoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewScoresView = inflater.inflate(R.layout.fragment_view_scores, container, false);
        viewScoresListView = (ListView) viewScoresView.findViewById(R.id.viewScoresListViewID);
        classSpinner = (Spinner) viewScoresView.findViewById(R.id.classSpinnerID);
        testSpinner = (Spinner) viewScoresView.findViewById(R.id.testSpinnerID);

        classSpinnerArrayList = new ArrayList<>();
        classSpinnerArrayList.add(" ");
        classAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, classSpinnerArrayList);
        classSpinner.setAdapter(classAdapter);

        testSpinnerArrayList = new ArrayList<>();
        testSpinnerArrayList.add(" ");
        testAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, testSpinnerArrayList);
        testSpinner.setAdapter(testAdapter);

        listDataHeaderRedIds = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listDataHeaderRedIds);
        viewScoresListView.setAdapter(listAdapter);


        initData();


        return viewScoresView;

    }

    public void initData() {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Classes").child(currentProfessor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    classSpinnerArrayList.add(child.getKey());
                }
                classAdapter.notifyDataSetChanged();
                classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        className = parent.getItemAtPosition(position).toString();
                        testSpinnerArrayList.clear();
                        testSpinnerArrayList.add(" ");
                        testAdapter.notifyDataSetChanged();

                        listDataHeaderRedIds.clear();
                        listDataHeaderRedIds.add(" ");
                        listAdapter.notifyDataSetChanged();

                        //getting tests
                        databaseReference.child("Classes").child(currentProfessor).child(className).child("Tests").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot child : children) {
                                    testSpinnerArrayList.add(child.getKey());
                                }
                                testAdapter.notifyDataSetChanged();
                                testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        testName = parent.getItemAtPosition(position).toString();
                                        listDataHeaderRedIds.clear();
                                        listDataHeaderRedIds.add(" ");
                                        listAdapter.notifyDataSetChanged();

                                        if (testName.equals(" ")) {
                                            Toast.makeText(getContext(), "Please select a test name", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        //getting redid here
                                        databaseReference.child("Classes").child(currentProfessor).child(className).child("StudentInClass").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                                for (DataSnapshot child : children) {
                                                    listDataHeaderRedIds.add(child.getKey());
                                                }
                                                listAdapter.notifyDataSetChanged();
                                                viewScoresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                        if (testName.equals(" ")) {
                                                            Toast.makeText(getContext(), "Please select a test name", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        final String redid;
                                                        redid = parent.getItemAtPosition(position).toString();
                                                        if (redid.trim().length() == 0) {
                                                            return;
                                                        }
                                                        //getting the score
                                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                        final DatabaseReference databaseReference = database.getReference();

                                                        if (testName.equals(" ")) {
                                                            Toast.makeText(getContext(), "Please select a test name", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        databaseReference.child("Students").child(redid).child("AvailableTests").child(className).child(testName).child("score").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (testName.equals(" ")) {
                                                                    Toast.makeText(getContext(), "Please select a test name", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                                score = dataSnapshot.getValue().toString();
                                                                try{
                                                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                                                alert.setMessage("Test Score: " + score);
                                                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                                    }
                                                                });
                                                                AlertDialog alertDialog = alert.create();
                                                                alertDialog.show();
                                                                alertDialog.setCancelable(true);
                                                                }
                                                                catch (Exception e){

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                            }
                                                        });
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                        //getting re did here ends here
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        //getting tests ends here
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

