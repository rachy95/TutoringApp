package com.excellassonde.tutoringapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjustAvailability extends AppCompatActivity {

    EditText availableHoursBox;
    EditText mondayBox, tuesdayBox, wednesdayBox, thursdayBox, fridayBox, saturdayBox, sundayBox;
    double availableHours;

    DatabaseReference reference;

    static TutorInformation tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_availability);


        reference = FirebaseDatabase.getInstance().getReference();

        availableHoursBox = (EditText) findViewById(R.id.availableHours_edit);

        //availability
        mondayBox = (EditText)findViewById(R.id.monday_availability);
        tuesdayBox = (EditText) findViewById(R.id.tuesday_availability);
        wednesdayBox = (EditText) findViewById(R.id.wednesday_availability);
        thursdayBox = (EditText) findViewById(R.id.thursday_availability);
        fridayBox = (EditText) findViewById(R.id.friday_availability);
        saturdayBox = (EditText) findViewById(R.id.saturday_availability);
        sundayBox = (EditText) findViewById(R.id.sunday_availability);

        //set the previous availability
        //get the previous from the database and set it
        //necessary evil for now
        availableHoursBox.setText(String.valueOf(tutor.getAvailableHours()));
        mondayBox.setText(setDayField(tutor.getAvailability(), "Monday"));
        tuesdayBox.setText(setDayField(tutor.getAvailability(), "Tuesday"));
        wednesdayBox.setText(setDayField(tutor.getAvailability(), "Wednesday"));
        thursdayBox.setText(setDayField(tutor.getAvailability(), "Thursday"));
        fridayBox.setText(setDayField(tutor.getAvailability(), "Friday"));
        saturdayBox.setText(setDayField(tutor.getAvailability(), "Saturday"));
        sundayBox.setText(setDayField(tutor.getAvailability(), "Sunday"));


    }

    public static void setTutorInformation(TutorInformation tutorInformation){
        tutor = tutorInformation;
    }

    public void adjustAvailability(View view){

        List<EditText> daysOfWeek = new ArrayList<>();
        daysOfWeek.add(mondayBox);
        daysOfWeek.add(tuesdayBox);
        daysOfWeek.add(wednesdayBox);
        daysOfWeek.add(thursdayBox);
        daysOfWeek.add(fridayBox);
        daysOfWeek.add(saturdayBox);
        daysOfWeek.add(sundayBox);

        Map<String, Map<String, Boolean>> availability = Utils.getAvailabilityInRightFormat(daysOfWeek);

        availableHours = Double.valueOf(availableHoursBox.getText().toString());
        //get the values and send to the database
        Map<String, Object> updates = new HashMap<>();
        updates.put("availableHours", availableHours);
        updates.put("availability", availability);
        reference.child(Utils.tutors).child(Utils.getTutorName()).updateChildren(updates);
        Toast.makeText(AdjustAvailability.this, "Your availability has been updated.", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, TutorMainPage.class);
        startActivity(intent);
    }

    private String getTimes(Map<String, Boolean> dayTimes){
        try {
            return TextUtils.join(",", dayTimes.keySet());
        }catch (Exception e){
            return "";
        }
    }

    private String setDayField(Map<String, Map<String, Boolean>> availability, String day){
        Map<String, Boolean> dayTimes;

        try{
            dayTimes = availability.get(day);
        }catch (Exception e) {
            dayTimes = null;
        }

        return getTimes(dayTimes);
    }

}
