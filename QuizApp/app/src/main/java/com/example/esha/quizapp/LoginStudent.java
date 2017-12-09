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
public class LoginStudent extends Fragment {
    EditText email, password;

    Button login, goToRegister;

    String userId;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView loginWelcomeStudent, emailStudentTextView, passwordTextView, didNotSignUpTextView;

    Boolean studenLogin;
    Boolean formLoginValid = true;

    public LoginStudent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studenLogin = true;
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
    }

    public void goToRegisterPage() {
        FragmentManager fragments = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
        RegisterStudent fragment = new RegisterStudent();
        fragmentTransaction.replace(R.id.placeHolder, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_student, container, false);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) v.findViewById(R.id.StudentEmailHere);
        goToRegister = (Button) v.findViewById(R.id.takeToRegisterId);
        password = (EditText) v.findViewById(R.id.StudentPasswordHere);
        login = (Button) v.findViewById(R.id.loginHereStudentId);
        didNotSignUpTextView = (TextView) v.findViewById(R.id.notAMemberID);
        loginWelcomeStudent = (TextView) v.findViewById(R.id.infoAboutLoginStudent);
        emailStudentTextView = (TextView) v.findViewById(R.id.StudentEmailId);
        passwordTextView = (TextView) v.findViewById(R.id.StudentPasswordId);
        Typeface mytypeface = Typeface.createFromAsset(getActivity().getAssets(), "BodoniFLF-Bold.ttf");
        loginWelcomeStudent.setTypeface(mytypeface);
        didNotSignUpTextView.setTypeface(mytypeface);
        emailStudentTextView.setTypeface(mytypeface);
        passwordTextView.setTypeface(mytypeface);
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterPage();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLoginForm()) {
                    signIn(email.getText().toString(), password.getText().toString());
                }

            }
        });
        return v;
    }

    public Boolean validateLoginForm() {
        formLoginValid = true;

        if (email.getText().toString().trim().length() == 0) {
            email.setError("Enter your email");
            formLoginValid = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Incorrect email");
            formLoginValid = false;
        }

        if (password.getText().toString().trim().length() == 0) {
            password.setError("Enter your password");
            formLoginValid = false;
        }

        if (password.getText().toString().trim().length() < 6) {
            password.setError("Enter at least 6 characters");
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
                            Intent goToFilterActivity = new Intent(getContext(), DashboardNavigationActivity.class);
                            goToFilterActivity.putExtra("studentLogin", studenLogin);
                            goToFilterActivity.putExtra("studentLoggedIn", 1);
                            startActivity(goToFilterActivity);
                            getActivity().finish();

                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                        }
                        if (!task.isSuccessful()) {
                        }
                    }
                });
    }

}
