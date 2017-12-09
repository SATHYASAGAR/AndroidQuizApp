package com.example.esha.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Date;

public class ClassListActivity extends AppCompatActivity {
    FirebaseAuth authInstance;

    ArrayList<String> listOfClasses;
    ArrayList<String> listOfSelectedClasses;

    Button addClassButton;

    ListView classesListView;

    ArrayAdapter<String> classListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        authInstance = FirebaseAuth.getInstance();
        listOfClasses = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        addClassButton = (Button) findViewById(R.id.addClassesButton);
        listOfSelectedClasses = new ArrayList<>();
        classesListView = (ListView) findViewById(R.id.checkable_list);


        DatabaseReference databaseReference = database.getReference();
        databaseReference.child("Classes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    listOfClasses.add(child.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        classListAdapter = new ArrayAdapter<String>(this, R.layout.checkable_list_layout, R.id.txt_title, listOfClasses);
        classesListView.setAdapter(classListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onStart() {
        super.onStart();

        classesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        classesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView) view).getText().toString();
                if (listOfSelectedClasses.contains(selectedItem))
                    listOfSelectedClasses.remove(selectedItem);
                else
                    listOfSelectedClasses.add(selectedItem);
                classListAdapter.notifyDataSetChanged();
            }

        });

        addClassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                for (final String element : listOfSelectedClasses) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = database.getReference();
                    databaseReference.child("Classes").child(element).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Calendar c2 = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
                            String requestDate = sdf.format(c2.getTime());
                            Firebase sReference = new Firebase("https://quizapp-d5f3c.firebaseio.com/Classes");
                            ClassListClass classObject = new ClassListClass(false, false, "null", requestDate);
                            sReference.child(element).setValue(classObject);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
