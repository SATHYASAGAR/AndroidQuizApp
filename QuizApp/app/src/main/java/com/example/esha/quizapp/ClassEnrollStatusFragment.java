package com.example.esha.quizapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ClassEnrollStatusFragment extends Fragment {

    View classEnrollStatusView;

    ArrayList<String> classEnrollStatusList;

    ListView classEnrollStatusListView;

    ArrayAdapter<String> classEnrollStatusArrayAdapter;

    String currentStudent;

    FirebaseAuth authInstance;

    public ClassEnrollStatusFragment() {
        // Required empty public constructor
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Enroll Status");
        Firebase.setAndroidContext(getContext());
        authInstance=FirebaseAuth.getInstance();
        currentStudent=authInstance.getCurrentUser().getDisplayName();
        classEnrollStatusList = new ArrayList<>();
        classEnrollStatusArrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,classEnrollStatusList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        classEnrollStatusView = inflater.inflate(R.layout.fragment_class_enroll_status, container, false);
        classEnrollStatusListView = (ListView) classEnrollStatusView.findViewById(R.id.enrollStatusListID);
        classEnrollStatusListView.setAdapter(classEnrollStatusArrayAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Students").child(currentStudent).child("ClassStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (final DataSnapshot child : children) {
                    String status=child.getValue().toString();
                    status = status.replaceAll("\\{", " ");
                    status = status.replaceAll("\\}", " ");
                    status = status.substring(status.lastIndexOf("=") + 1);
                    classEnrollStatusList.add(child.getKey()+"\n"+status);
                    classEnrollStatusArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return classEnrollStatusView;
    }

}
