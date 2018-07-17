package com.excellassonde.tutoringapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentLogin extends AppCompatActivity {
    AutoCompleteTextView studentNameTextBox, studentEmailTextBox;
    String studentName = "";
    String studentEmail = "";

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    public void bookSession(View view){
        studentNameTextBox = (AutoCompleteTextView) findViewById(R.id.firstName);
        studentEmailTextBox = (AutoCompleteTextView) findViewById(R.id.email);
        Intent intent = new Intent(this, ChooseCourseActivity.class);
        studentName = studentNameTextBox.getEditableText().toString();
        studentEmail = studentEmailTextBox.getEditableText().toString();
        setAllCourses();
        setAllTutors();

        if(Utils.isValidEmail(studentEmail)){
            SessionReview.setStudentName(studentName);
            SessionReview.setStudentEmail(studentEmail);
            startActivity(intent);
        }

    }

    public void setAllCourses() {
        final Set<String> courses = new HashSet<>();
        ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tutorsRef = ref.child("Tutors");
        tutorsRef.keepSynced(true);

        tutorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    TutorInformation tutor = child.getValue(TutorInformation.class);
                    courses.addAll(tutor.getCourses());
                }
                    ChooseCourseActivity.setCourses(courses);
                    Intent intent = new Intent(getApplicationContext(), ChooseCourseActivity.class);
                    startActivity(intent);
                    if(courses.isEmpty()){
                        ChooseCourseActivity.setFlagIfCoursesAvailable(false);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void setAllTutors() {
        final List<TutorInformation> tutors = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference tutorsRef = ref.child("Tutors");
        tutorsRef.keepSynced(true);

        tutorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    TutorInformation tutor = child.getValue(TutorInformation.class);
                    tutors.add(tutor);
                }
                DaysFragment.setTutors(tutors);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


}
