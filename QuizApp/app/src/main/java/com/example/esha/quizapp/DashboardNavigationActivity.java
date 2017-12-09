package com.example.esha.quizapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;

    TextView nameDisplay, instructIdDisplay;

    String currentProfessor;

    FirebaseAuth authInstance;

    Boolean studentLogin;

    int professorLoggedInCheck;
    int studentLoggedInCheck;

    Object nameToDisplay;
    Object nameToDisplayProfessor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_options_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        authInstance = FirebaseAuth.getInstance();

        currentProfessor = authInstance.getCurrentUser().getDisplayName();
        professorLoggedInCheck = getIntent().getIntExtra("professorLoggedIn", 0);
        studentLoggedInCheck = getIntent().getIntExtra("studentLoggedIn", 0);
        studentLogin = getIntent().getBooleanExtra("studentLogin", false);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (studentLogin) {
            navigationView.inflateMenu(R.menu.activity_student_options_nav_drawer);
        } else {
            navigationView.inflateMenu(R.menu.activity_professor_options_nav_drawer);
        }


        ProfileDefaultFragment fragment = new ProfileDefaultFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("studentLogin", studentLoggedInCheck);
        fragment.setArguments(bundle);
        FragmentManager fragments = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
        fragmentTransaction.replace(R.id.holdAllFragments, fragment);
        fragmentTransaction.commit();
        View headerView = navigationView.getHeaderView(0);
        instructIdDisplay = (TextView) headerView.findViewById(R.id.instructorIdProf);
        nameDisplay = (TextView) findViewById(R.id.nameOfProfessor);
        instructIdDisplay.setText(currentProfessor);
        instructIdDisplay.setText(currentProfessor);
        //getting the name of the professor
        if (professorLoggedInCheck == 2) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference sReferenceName = database.getReference();
            sReferenceName.child("Professor").child(currentProfessor).child("nameOfProfessor").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {

                        Toast.makeText(DashboardNavigationActivity.this, "You are not a professor!", Toast.LENGTH_SHORT).show();
                        authInstance.signOut();
                        finish();
                    } else {
                        nameToDisplayProfessor = dataSnapshot.getValue();
                        View headerView = navigationView.getHeaderView(0);
                        nameDisplay = (TextView) headerView.findViewById(R.id.nameOfProfessor);
                        nameDisplay.setText(nameToDisplayProfessor.toString());
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } else if (studentLoggedInCheck == 1) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference sReferenceName = database.getReference();
            sReferenceName.child("Students").child(currentProfessor).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {

                        Toast.makeText(DashboardNavigationActivity.this, "You are not a student!", Toast.LENGTH_SHORT).show();
                        authInstance.signOut();
                        finish();
                    } else {
                        nameToDisplay = dataSnapshot.getValue();
                        View headerView = navigationView.getHeaderView(0);
                        nameDisplay = (TextView) headerView.findViewById(R.id.nameOfProfessor);
                        nameDisplay.setText(nameToDisplay.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        //getting name ends here
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(this, "Default Back Button Disabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.professor_options_activity_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            //sign out
            authInstance.signOut();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //student options
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_requestToEnroll) {

            fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            ).replace(R.id.holdAllFragments, new EnrollRequestFragment()).commit();

        } else if (id == R.id.nav_enrollRequestStatus) {

            fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            ).replace(R.id.holdAllFragments, new ClassEnrollStatusFragment()).commit();

        } else if (id == R.id.nav_scheduledTests) {

            fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            ).replace(R.id.holdAllFragments, new ScheduledTestsFragment()).commit();

        } else if (id == R.id.nav_testsTaken) {

            fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            ).replace(R.id.holdAllFragments, new TestsTakenFragment()).commit();
        } else if (id == R.id.nav_studentHome) {

            ProfileDefaultFragment fragment = new ProfileDefaultFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("studentLogin", 1);
            fragment.setArguments(bundle);

            FragmentManager fragments = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            );
            fragmentTransaction.replace(R.id.holdAllFragments, fragment);
            fragmentTransaction.commit();
        }

        //student options end here

        if (id == R.id.nav_addClass) {
            FragmentManager fragments = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
            AddClassFragment fragment = new AddClassFragment();
            fragmentTransaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            );
            fragmentTransaction.replace(R.id.holdAllFragments, fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_addTest) {
            FragmentManager fragments = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
            AddTestToClassFragment fragment = new AddTestToClassFragment();
            fragmentTransaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            );
            fragmentTransaction.replace(R.id.holdAllFragments, fragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_addStudents) {
            fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            ).replace(R.id.holdAllFragments, new ClassListOfProfessor()).commit();

        } else if (id == R.id.nav_viewScores) {
            fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            ).replace(R.id.holdAllFragments, new ViewScoresFragment()).commit();
        } else if (id == R.id.nav_professorHome) {
            fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            ).replace(R.id.holdAllFragments, new ProfileDefaultFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
