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
import android.widget.Button;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterProfessor extends Fragment {
    Button registerProfessor, loginProfessor;
    TextView registerWelcomeScreenTextView,nameTextView,instructorTextView,emailTextView,passwordTextView;

    EditText nameOfProfessor, passwordOfProfessor, instructorId, emailOfProfessor;

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPasswordProfessor";
    FirebaseAuth authInstance;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String userId;

    int checkValidation;

    Boolean validForm = true;

    ArrayList<String> professorInstructorID;

    public RegisterProfessor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        professorInstructorID = new ArrayList<>();
        databaseReference.child("Professor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    professorInstructorID.add(child.getKey());
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_professor, container, false);
        Firebase.setAndroidContext(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Firebase.setAndroidContext(getContext());
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        authInstance = FirebaseAuth.getInstance();
        registerProfessor = (Button) v.findViewById(R.id.registerProfessor);
        Typeface mytypeface = Typeface.createFromAsset(getActivity().getAssets(), "BodoniFLF-Bold.ttf");
        registerWelcomeScreenTextView=(TextView)v.findViewById(R.id.welcomeProfessor);
        nameTextView=(TextView)v.findViewById(R.id.nameProfessorId);
        instructorTextView=(TextView)v.findViewById(R.id.instructorIdTextView);
        emailTextView=(TextView)v.findViewById(R.id.ProfessorEmailId);
        passwordTextView=(TextView)v.findViewById(R.id.ProfessorPasswordId);
        registerWelcomeScreenTextView.setTypeface(mytypeface);
        nameTextView.setTypeface(mytypeface);
        instructorTextView.setTypeface(mytypeface);
        emailTextView.setTypeface(mytypeface);
        passwordTextView.setTypeface(mytypeface);

        nameOfProfessor = (EditText) v.findViewById(R.id.professorName);
        loginProfessor = (Button) v.findViewById(R.id.loginProfessor);
        emailOfProfessor = (EditText) v.findViewById(R.id.ProfessorEmailHere);
        instructorId = (EditText) v.findViewById(R.id.professorRedIdHere);
        passwordOfProfessor = (EditText) v.findViewById(R.id.ProfessorPasswordHere);
        registerProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFormData()) {
                    registerProfessorFunction();
                }
            }
        });

        loginProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginProfessor();
            }
        });
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
        return v;
    }

    public boolean validateFormData() {

        validForm = true;
        final String instructId = instructorId.getText().toString();
        if (professorInstructorID.contains(instructId)) {
            instructorId.setError("Instructor Id Already registered. Enter unique RedId");
            validForm = false;
        }
        if (nameOfProfessor.getText().toString().trim().length() == 0) {
            nameOfProfessor.setError("Enter your name");
            validForm = false;
        }

        if (instructorId.getText().toString().trim().length() == 0) {
            instructorId.setError("Enter your ID");
            validForm = false;
        }

        if (instructorId.getText().toString().trim().length() != 9) {
            instructorId.setError("Enter 9 characters");
            validForm = false;
        }

        if (emailOfProfessor.getText().toString().trim().length() == 0) {
            emailOfProfessor.setError("Enter your email");
            validForm = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailOfProfessor.getText().toString()).matches()) {
            emailOfProfessor.setError("Incorrect email");
            validForm = false;
        }

        if (passwordOfProfessor.getText().toString().trim().length() == 0) {
            passwordOfProfessor.setError("Enter your password");
            validForm = false;
        }

        if (passwordOfProfessor.getText().toString().trim().length() < 6) {
            passwordOfProfessor.setError("Enter at least 6 characters");
            validForm = false;
        }

        return validForm;
    }

    public void registerProfessorFunction() {
        saveDetailsToFirebaseProfessor();
        if (saveDetailsToFirebaseProfessor() == 1) {
            createAccount(emailOfProfessor.getText().toString(), passwordOfProfessor.getText().toString());
        }
    }

    public void goToLoginProfessor() {
        FragmentManager fragments = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
        LoginProfessor fragment = new LoginProfessor();
        fragmentTransaction.replace(R.id.holderProfessor, fragment);
        fragmentTransaction.commit();
    }

    public int saveDetailsToFirebaseProfessor() {
        validate();
        if (validate() == 1) {
            Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com/Professor");
            ProfessorDetails professor = new ProfessorDetails(nameOfProfessor.getText().toString());
            sRef.child(instructorId.getText().toString()).setValue(professor);
            Toast.makeText(getContext(), "Data Uploaded", Toast.LENGTH_SHORT).show();
            return 1;
        } else {
            Toast.makeText(getContext(), "Cannot Upload Data", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private void createAccount(String emailProfessor, String passwordProfessor) {

        mAuth.createUserWithEmailAndPassword(emailProfessor, passwordProfessor)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String instructorIdOfCurrentProfessor;
                        instructorIdOfCurrentProfessor = instructorId.getText().toString();
                        if (task.isSuccessful()) {
                            final FirebaseUser user = task.getResult().getUser();
                            final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(instructorIdOfCurrentProfessor)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Professor registered successfully", Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }
                                    });

                        }
                        if (!task.isSuccessful()) {
                        }
                    }
                });
    }

    public int validate() {
        checkValidation = 1;
        if (nameOfProfessor.getText().toString().length() == 0) {
            nameOfProfessor.setError("Enter your name");
            checkValidation = 0;
        }
        if (passwordOfProfessor.getText().toString().length() < 6) {
            passwordOfProfessor.setError("Password must be at least 6 characters");
            checkValidation = 0;
        }
        return checkValidation;
    }
}
