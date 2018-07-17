package com.excellassonde.tutoringapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterAsTutor extends AppCompatActivity {

    final int STUDENT_NUMBER_LENGTH = 9;
    EditText nameBox, studentNumberBox, phoneNumberBox, emailBox, availableHoursBox, coursesBox;
    EditText mondayBox, tuesdayBox, wednesdayBox, thursdayBox, fridayBox, saturdayBox, sundayBox;
    String name, phoneNumber, email;
    double availableHours;
    String studentNumber;
    List<String> courses;


    DatabaseReference reference;
    //use their student number to store them in the database? or name
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_tutor);

        reference = FirebaseDatabase.getInstance().getReference();
        nameBox = (EditText) findViewById(R.id.tutor_name_edit);
        studentNumberBox = (EditText) findViewById(R.id.student_number_edit);
        phoneNumberBox = (EditText) findViewById(R.id.phoneNumber_edit);
        emailBox = (EditText) findViewById(R.id.tutor_email_edit);
        availableHoursBox = (EditText) findViewById(R.id.availableHours_edit);
        coursesBox = (EditText) findViewById(R.id.courses_edit);

        if(!TextUtils.isEmpty(TutorSignUp.tutorName)) {
            String name = TutorSignUp.tutorName;
            String firstName = name.replace(" A-Z*", " A-Z.");
            nameBox.setText(firstName);
        }

        if(!TextUtils.isEmpty(TutorSignUp.tutorEmail)) {
            emailBox.setText(TutorSignUp.tutorEmail);
        }


        //availability
        mondayBox = (EditText)findViewById(R.id.monday_availability);
        tuesdayBox = (EditText) findViewById(R.id.tuesday_availability);
        wednesdayBox = (EditText) findViewById(R.id.wednesday_availability);
        thursdayBox = (EditText) findViewById(R.id.thursday_availability);
        fridayBox = (EditText) findViewById(R.id.friday_availability);
        saturdayBox = (EditText) findViewById(R.id.saturday_availability);
        sundayBox = (EditText) findViewById(R.id.sunday_availability);

        //the phone number should start with +1
        phoneNumberBox.setText("+1");

    }

    public void addToTutors(View view){
        studentNumber = studentNumberBox.getText().toString();
        if (!validateForm()) {
            return;
        }

        Intent intent = new Intent(this, TutorRegistrationConfirmation.class);
        //create a new tutorInformation with the information given
        name = nameBox.getText().toString();
        phoneNumber = phoneNumberBox.getText().toString();
        email = emailBox.getText().toString();
        availableHours = Double.parseDouble(availableHoursBox.getText().toString());
        courses = Arrays.asList(coursesBox.getText().toString().trim().split(","));

        List<EditText> daysOfWeek = new ArrayList<>();
        daysOfWeek.add(mondayBox);
        daysOfWeek.add(tuesdayBox);
        daysOfWeek.add(wednesdayBox);
        daysOfWeek.add(thursdayBox);
        daysOfWeek.add(fridayBox);
        daysOfWeek.add(saturdayBox);
        daysOfWeek.add(sundayBox);

        Map<String, Map<String, Boolean>> availability = Utils.getAvailabilityInRightFormat(daysOfWeek);
        TutorInformation tutorInformation = new TutorInformation(name, studentNumber, email, phoneNumber, courses, availableHours, availability);

        Map<String, Object> tutor = new HashMap<>();
        tutor.put(name, tutorInformation);
        try {
            reference.child("Tutors").updateChildren(tutor);
        }
        catch (Exception e){
            //there is no child yet
            Map<String, Object> firstTutor = new HashMap<>();
            firstTutor.put("Tutors", tutor);
            reference.updateChildren(firstTutor);
        }

        startActivity(intent);

    }

    private boolean validateForm(){
        boolean valid = true;
        //1. student number cannot be empty
        if(TextUtils.isEmpty(studentNumber)){
            studentNumberBox.setError("Required.");
            valid = false;
        }
        else {
            studentNumberBox.setError(null);
        }

        //2. student number has to be exactly 9
        if(studentNumber.length() != STUDENT_NUMBER_LENGTH){
            studentNumberBox.setError("Enter a valid student number.");
            valid = false;
        }
        else {
            studentNumberBox.setError(null);
        }

        return valid;
    }


}
