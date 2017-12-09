package com.example.esha.quizapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Iterator;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {

    private ArrayList<String> studentListData = new ArrayList<String>();

    ListView studentListView;

    String nameOfClass;

    Object nameOfStudent;

    MyListAdapter lstAdapter;

    String currentProfessor;

    FirebaseAuth authInstance;

    ArrayList<String> testToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_student);

        studentListView = (ListView) findViewById(R.id.listview);
        testToSend = new ArrayList<>();
        nameOfClass = getIntent().getStringExtra("nameOfClass");
        authInstance = FirebaseAuth.getInstance();
        currentProfessor = authInstance.getCurrentUser().getDisplayName();
        generateListContent();
        lstAdapter = new MyListAdapter(this, R.layout.list_add_ignore_students, studentListData);
        studentListView.setAdapter(lstAdapter);
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void generateListContent() {
        //extracting students from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference();
        databaseReference.child("AddClassRequest").child(nameOfClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (final DataSnapshot child : children) {
                    final String holdchild = child.getKey();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseRef = database.getReference();
                    databaseRef.child("AddClassRequest").child(nameOfClass).child(holdchild).child("dateAddedOrRejected").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String valueOfStatus = (String) dataSnapshot.getValue();
                            if (valueOfStatus.equals("null") && !studentListData.contains(holdchild)) {
                                studentListData.add(holdchild);
                                lstAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;

        public MyListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                viewHolder.ignoreButton = (ImageButton) convertView.findViewById(R.id.list_item_ignore_btn);
                viewHolder.addButton = (ImageButton) convertView.findViewById(R.id.list_item_add_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.ignoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c2 = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
                    final String dateToday = sdf.format(c2.getTime());
                    final String redid = (String) studentListView.getItemAtPosition(position);

                    //getting the student name
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = database.getReference();
                    databaseReference.child("Students").child(redid).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            nameOfStudent = dataSnapshot.getValue();
                            Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com");
                            ClassStatus status = new ClassStatus(nameOfStudent.toString(), false, dateToday);
                            sRef.child("AddClassRequest").child(nameOfClass).child(studentListView.getItemAtPosition(position).toString()).setValue(status);
                            Toast.makeText(getContext(), "Red id " + studentListView.getItemAtPosition(position) + " NOT added to class", Toast.LENGTH_SHORT).show();
                            studentListData.remove(redid);
                            lstAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    //getting the student name code ends here

                    //changing class status
                    Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com");
                    ClassEnrollStatus status = new ClassEnrollStatus();
                    status.setStatus(" Request Declined");
                    sRef.child("Students").child(redid).child("ClassStatus").child(nameOfClass).setValue(status);
                    //changing class status ends here
                }
            });

            mainViewholder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c2 = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
                    final String dateToday = sdf.format(c2.getTime());
                    final String redid = (String) studentListView.getItemAtPosition(position);

                    //getting the student name
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = database.getReference();
                    databaseReference.child("Students").child(redid).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            nameOfStudent = dataSnapshot.getValue();
                            Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com");
                            ClassStatus status = new ClassStatus(nameOfStudent.toString(), true, dateToday);
                            sRef.child("AddClassRequest").child(nameOfClass).child(redid).setValue(status);
                            sRef.child("Classes").child(currentProfessor).child(nameOfClass).child("StudentInClass").child(redid).setValue("student");
                            //adding students ends here

                            Toast.makeText(getContext(), "Red id " + redid + " added to class", Toast.LENGTH_SHORT).show();
                            studentListData.remove(redid);
                            lstAdapter.notifyDataSetChanged();

                            //getting  all tests of the class
                            databaseReference.child("Classes").child(currentProfessor).child(nameOfClass).child("Tests").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    testToSend.clear();
                                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                    for (final DataSnapshot child : children) {
                                        testToSend.add(child.getKey());
                                    }

                                    for (String testToSendElement : testToSend) {
                                        AvailableTest object = new AvailableTest(dateToday, 0, false);
                                        databaseReference.child("Students")
                                                .child(redid)
                                                .child("AvailableTests").child(nameOfClass)
                                                .child(testToSendElement).setValue(object);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //getting  all tests of the class ends here
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });




                    //changing class status
                    Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com");
                    ClassEnrollStatus status = new ClassEnrollStatus();
                    status.setStatus("Enrolled");
                    sRef.child("Students").child(redid).child("ClassStatus").child(nameOfClass).setValue(status);
                    //changing class status ends here


                }

            });

            mainViewholder.title.setText(getItem(position));
            return convertView;
        }

    }
}
