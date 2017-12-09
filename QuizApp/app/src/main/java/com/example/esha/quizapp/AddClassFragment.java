package com.example.esha.quizapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddClassFragment extends Fragment {

    TextView infoAddClass;

    EditText className;

    Button addClass;

    String currentProfessor;

    FirebaseAuth authInstance;

    public AddClassFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Add Class");
        Firebase.setAndroidContext(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_class, container, false);
        className = (EditText) v.findViewById(R.id.nameOfTestId);
        authInstance = FirebaseAuth.getInstance();
        infoAddClass = (TextView) v.findViewById(R.id.infoAddTestId);
        addClass = (Button) v.findViewById(R.id.addClassButtonid);
        currentProfessor = authInstance.getCurrentUser().getDisplayName();
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClassTofirebase();
            }
        });

        return v;
    }

    public void addClassTofirebase() {
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        final String dateToday = sdf.format(c2.getTime());
        Firebase sRef = new Firebase("https://quizapp-d5f3c.firebaseio.com/Classes");
        sRef.child(currentProfessor).child(className.getText().toString()).child("dateadded").setValue(dateToday);
        String classAdded = className.getText().toString();
        Toast.makeText(getContext(), "Class " + classAdded + " added", Toast.LENGTH_SHORT).show();
        className.setText("");
    }
}
