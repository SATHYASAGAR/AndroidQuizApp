package com.example.esha.quizapp;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileDefaultFragment extends Fragment {

    TextView nameHolderId, infoWelcome, redHolderId, instructionDeatils, infoInstructions;

    String nameToDisplayStudent;
    String nameToDisplayProf;
    String currentUser;

    FirebaseAuth authInstance;

    NavigationView navigationView;

    int studentLogin;

    public ProfileDefaultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Home");
        Firebase.setAndroidContext(getContext());
        authInstance = FirebaseAuth.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            studentLogin = bundle.getInt("studentLogin", 0);
        }
        currentUser = authInstance.getCurrentUser().getDisplayName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_default, container, false);
        nameHolderId = (TextView) v.findViewById(R.id.nameHolderId);
        infoWelcome = (TextView) v.findViewById(R.id.infoIdWelcome);
        redHolderId = (TextView) v.findViewById(R.id.redHolderId);
        infoInstructions = (TextView) v.findViewById(R.id.infoInstructions);
        instructionDeatils = (TextView) v.findViewById(R.id.instructionDetails);
        Typeface mytypeface = Typeface.createFromAsset(getActivity().getAssets(), "BodoniFLF-Roman.ttf");
        navigationView = (NavigationView) v.findViewById(R.id.nav_view);
        nameHolderId.setTypeface(mytypeface);
        infoWelcome.setTypeface(mytypeface);
        redHolderId.setTypeface(mytypeface);
        instructionDeatils.setTypeface(mytypeface);
        infoInstructions.setTypeface(mytypeface);
        if (studentLogin == 1) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference sReferenceName = database.getReference();

            sReferenceName.child("Students").child(currentUser).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(getContext(), "You are not a student!", Toast.LENGTH_SHORT).show();
                        authInstance.signOut();
                        getActivity().finish();
                    } else {
                        nameToDisplayStudent = dataSnapshot.getValue().toString();
                        nameHolderId.setText(nameToDisplayStudent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            redHolderId.setText(currentUser);
        } else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference sReferenceName = database.getReference();
            sReferenceName.child("Professor").child(currentUser).child("nameOfProfessor").addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() == null) {
                        Toast.makeText(getContext(), "You are not a professor!", Toast.LENGTH_SHORT).show();
                        authInstance.signOut();
                        getActivity().finish();
                    } else {
                        nameToDisplayProf = dataSnapshot.getValue().toString();
                        nameHolderId.setText(nameToDisplayProf);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            redHolderId.setText(currentUser);
        }
        return v;
    }
}
