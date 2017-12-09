package com.example.esha.quizapp;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterStudent extends Fragment {
    TextView helpUsKnow;

    String majorSelected;
    String yearNumber;
    String userId;

    Spinner spinner;
    Spinner spinner2;

    EditText name, redid, email, password;

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    FirebaseAuth authInstance;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();

    Button register, login;

    ArrayList<String> redIdFromDatabase;

    Boolean formValid = true;

    public RegisterStudent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        redIdFromDatabase = new ArrayList<>();
        final DatabaseReference databaseReference = database.getReference();
        authInstance = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();
                    System.out.println(userId);
                } else {
                    // User is signed out
                }

            }
        };


        databaseReference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    redIdFromDatabase.add(child.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register_student, container, false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.year, android.R.layout.simple_spinner_item);
        spinner = (Spinner) v.findViewById(R.id.spinnerStudent);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2Student);
        register = (Button) v.findViewById(R.id.registerStudent);
        login = (Button) v.findViewById(R.id.loginStudent);
        name = (EditText) v.findViewById(R.id.studentName);
        helpUsKnow = (TextView) v.findViewById(R.id.welcomeStudent);
        Typeface mytypeface = Typeface.createFromAsset(getActivity().getAssets(), "BodoniFLF-Bold.ttf");
        helpUsKnow.setTypeface(mytypeface);
        redid = (EditText) v.findViewById(R.id.redIdHere);
        email = (EditText) v.findViewById(R.id.StudentEmailHere);
        password = (EditText) v.findViewById(R.id.StudentPasswordHere);
        mAuth = FirebaseAuth.getInstance();
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.major, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearNumber = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                majorSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFormData()) {
                    saveDetailsToFirebase();
                    createAccount(email.getText().toString(), password.getText().toString());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginScreen();
            }
        });


        return v;
    }

    public void saveDetailsToFirebase() {
        Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com/Students");
        Students student = new Students(name.getText().toString(), Integer.parseInt(redid.getText().toString()), Integer.parseInt(yearNumber), majorSelected, email.getText().toString(), password.getText().toString());
        sRef.child(redid.getText().toString()).setValue(student);
    }

    public void goToLoginScreen() {
        FragmentManager fragments = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
        LoginStudent fragment = new LoginStudent();
        fragmentTransaction.replace(R.id.placeHolder, fragment);
        fragmentTransaction.commit();
    }

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String redidOfCurrentUser;
                        redidOfCurrentUser = redid.getText().toString();

                        if (task.isSuccessful()) {
                            final FirebaseUser user = task.getResult().getUser();
                            final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(redidOfCurrentUser)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                try{Toast.makeText(getContext(), "User registered successfully", Toast.LENGTH_SHORT)
                                                        .show();
                                                }catch (Exception e){
                                                    }
                                            }
                                        }
                                    });

                        }
                        if (!task.isSuccessful()) {
                        }
                    }
                });
    }

    public Boolean validateFormData() {

        formValid = true;
        final String redIdGiven = redid.getText().toString();
        if (redIdFromDatabase.contains(redIdGiven)) {
            redid.setError("Red Id Already registered. Enter unique RedId");
            formValid = false;
        }

        if (name.getText().toString().trim().length() == 0) {
            name.setError("Enter your name");
            formValid = false;
        }

        if (redid.getText().toString().trim().length() == 0) {
            redid.setError("Enter your ID");
            formValid = false;
        }

        if (redid.getText().toString().trim().length() != 9) {
            redid.setError("Enter 9 characters");
            formValid = false;
        }

        if (email.getText().toString().trim().length() == 0) {
            email.setError("Enter your email");
            formValid = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Incorrect email");
            formValid = false;
        }

        if (password.getText().toString().trim().length() == 0) {
            password.setError("Enter your password");
            formValid = false;
        }

        if (password.getText().toString().trim().length() < 6) {
            password.setError("Enter at least 6 characters");
            formValid = false;
        }
        return formValid;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
