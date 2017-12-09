package com.example.esha.quizapp;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginProfessor extends Fragment {
    EditText emailProfessor, passwordProfessor;

    Button loginProfessor, goToRegister;

    String userIdProfessor;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView loginWelcome, emailTextView, passwordTextView, notMemberStill;


    Boolean professorLogin;
    Boolean formLoginValid = true;


    public LoginProfessor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        professorLogin = true;
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userIdProfessor = user.getUid();
                    System.out.println(userIdProfessor);
                } else {
                    // User is signed out
                }

            }
        };
    }

    public void goToRegisterProfFragment() {
        FragmentManager fragments = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
        RegisterProfessor fragment = new RegisterProfessor();
        fragmentTransaction.replace(R.id.holderProfessor, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login_professor, container, false);
        mAuth = FirebaseAuth.getInstance();
        emailProfessor = (EditText) v.findViewById(R.id.professorLoginEmailEditText);
        passwordProfessor = (EditText) v.findViewById(R.id.professorLoginPasswordEditText);
        loginProfessor = (Button) v.findViewById(R.id.loginHereProfessorId);
        loginWelcome = (TextView) v.findViewById(R.id.infoAboutLoginProfessor);
        goToRegister = (Button) v.findViewById(R.id.profRegisterId);
        notMemberStill = (TextView) v.findViewById(R.id.memberSignUpId);
        emailTextView = (TextView) v.findViewById(R.id.professorLoginEmailTextView);
        passwordTextView = (TextView) v.findViewById(R.id.professorLoginPasswordTextView);
        Typeface mytypeface = Typeface.createFromAsset(getActivity().getAssets(), "BodoniFLF-Bold.ttf");
        loginWelcome.setTypeface(mytypeface);
        emailTextView.setTypeface(mytypeface);
        notMemberStill.setTypeface(mytypeface);
        passwordTextView.setTypeface(mytypeface);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterProfFragment();
            }
        });

        loginProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLoginForm()) {
                    signIn(emailProfessor.getText().toString(), passwordProfessor.getText().toString());
                }
            }
        });
        return v;
    }


    public Boolean validateLoginForm() {
        formLoginValid = true;

        if (emailProfessor.getText().toString().trim().length() == 0) {
            emailProfessor.setError("Enter your email");
            formLoginValid = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailProfessor.getText().toString()).matches()) {
            emailProfessor.setError("Incorrect email");
            formLoginValid = false;
        }

        if (passwordProfessor.getText().toString().trim().length() == 0) {
            passwordProfessor.setError("Enter your password");
            formLoginValid = false;
        }

        if (passwordProfessor.getText().toString().trim().length() < 6) {
            passwordProfessor.setError("Enter at least 6 characters");
            formLoginValid = false;
        }
        return formLoginValid;
    }


    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent goToProfessorOptionsActivityNav = new Intent(getContext(), DashboardNavigationActivity.class);
                            goToProfessorOptionsActivityNav.putExtra("professorLoggedIn", 2);
                            startActivity(goToProfessorOptionsActivityNav);
                            getActivity().finish();
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Could not Login successfully", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        if (!task.isSuccessful()) {
                        }
                    }
                });
    }
}
