package com.example.esha.quizapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTestToClassFragment extends Fragment {

    ListView professorClassList;

    ArrayList<String> listOfClasses2;
    ArrayAdapter<String> adapterOfClasses2;

    String currentProfessor;

    FirebaseAuth authInstance;

    public AddTestToClassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Add Test");
        authInstance = FirebaseAuth.getInstance();
        currentProfessor = authInstance.getCurrentUser().getDisplayName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_class_list_of_professor, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        professorClassList = (ListView) v.findViewById(R.id.list);
        listOfClasses2 = new ArrayList<>();
        adapterOfClasses2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, listOfClasses2);
        professorClassList.setAdapter(adapterOfClasses2);

        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Classes").child(currentProfessor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    if(!listOfClasses2.contains(child.getKey())) {
                        listOfClasses2.add(child.getKey());
                    }
                    adapterOfClasses2.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        professorClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object nameOfclass = parent.getItemAtPosition(position);
                Intent i= new Intent(getContext(),AddTestActivity.class);
                i.putExtra("nameOfClass",nameOfclass.toString());
                startActivity(i);
            }
        });
        return v;
    }

}
