package com.excellassonde.tutoringapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SessionReview extends AppCompatActivity {
    public static final int MAX_TUTORING_HOURS = 3;
    public static String studentEmail, studentName, topics, day;
    public static String tutorName, tutorEmail, sessionTime, startTime, endTime, newStartTime, newEndTime, course, otherEmails;
    private static String errorToastMessage = "";
    public String sessionID;
    Spinner sessionTypeSpinner, startSpinner, endSpinner;
    EditText startEditBox, endEditBox;
    Utils.SessionType sessionType;
    DatabaseReference reference;

    public static void setTime(String time) {
        sessionTime = time;
    }

    public static void setStudentName(String aName) {
        studentName = aName;
    }

    public static void setStudentEmail(String email) {studentEmail = email; }

    public static void setTutorName(String name) {
        tutorName = name;
    }

    public static void setCourse(String aCourse) {
        course = aCourse;
    }

    public static void setOtherEmails(String emails){ otherEmails = emails;}

    public static void setDay(String aDay) {
        day = aDay;
    }

    public static void setTutorEmail(String anEmail) {
        tutorEmail = anEmail;
    }

    public static double getHoursBooked() {
        return getAdjustedNewEndTime() - getAdjustedNewStartTime();
    }

    public static double adjustTimeFromStringToDouble(String time) {
        time = time.replace(":", ".");
        String timeWithoutEnd = time.substring(0, time.length() - 2);
        double end = Double.parseDouble(timeWithoutEnd);

        if (time.endsWith("PM") || time.endsWith("pm")) {
            //if time ends with pm, add 12
            //if its 12pm we want to leave it as that
            //because 10am-12pm would be 2.
            if (end < 12)
                end += 12.00;
        }
        return end;
    }

    public static double getAdjustedNewStartTime() {
        return adjustTimeFromStringToDouble(newStartTime);
    }

    public static double getAdjustedStartTime() {
        return adjustTimeFromStringToDouble(startTime);
    }

    public static double getAdjustedNewEndTime() {
        return adjustTimeFromStringToDouble(newEndTime);
    }

    public static double getAdjustedEndTime() {
        return adjustTimeFromStringToDouble(endTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_session_review);
        sessionTypeSpinner = (Spinner) findViewById(R.id.session_type_dropdown);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.session_type_dropdown, R.layout.spinner_text);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sessionTypeSpinner.setAdapter(adapter);

        startSpinner = (Spinner) findViewById(R.id.start_session_dropdown_time);
        adapter = ArrayAdapter.createFromResource(this, R.array.time_list, R.layout.spinner_text);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        startSpinner.setAdapter(adapter);

        endSpinner = (Spinner) findViewById(R.id.end_session_dropdown_time);
        adapter = ArrayAdapter.createFromResource(this, R.array.time_list, R.layout.spinner_text);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        endSpinner.setAdapter(adapter);


        EditText tutorBox = (EditText) findViewById(R.id.tutor_name_edit);
        tutorBox.setText(tutorName);
        tutorBox.setEnabled(false);

        //set the course
        EditText courseBox = (EditText) findViewById(R.id.course_name_edit);
        courseBox.setText(course);
        courseBox.setEnabled(false);

        //set the day
        EditText dayBox = (EditText) findViewById(R.id.day_edit);
        dayBox.setText(day);
        dayBox.setEnabled(false);

        //set the start and end times
        //get the times first
        List<String> times = getTime(sessionTime);
        startTime = times.get(0);
        endTime = times.get(1);
        startEditBox = (EditText) findViewById(R.id.start_session_edit);
        startEditBox.setText(startTime, TextView.BufferType.EDITABLE);
        endEditBox = (EditText) findViewById(R.id.end_session_edit);
        endEditBox.setText(endTime, TextView.BufferType.EDITABLE);

    }

    public void goToConfirmation(View view) {
        final Intent intent = new Intent(this, StudentConfirmationPage.class);
        String sessionType = String.valueOf(sessionTypeSpinner.getSelectedItem());
        newStartTime = startEditBox.getText().toString() + startSpinner.getSelectedItem().toString();
        newEndTime = endEditBox.getText().toString() + endSpinner.getSelectedItem().toString();
        //I know it is kind of roundish but we need end time back in the format of session time
        String middle = "-";
        int breakHere = sessionTime.indexOf(middle);
        startTime = sessionTime.substring(0, breakHere);
        endTime = sessionTime.substring(breakHere + 1, sessionTime.length());
        this.sessionType = Utils.SessionType.ONE_ON_ONE;

        if(!checkTimeChosenIsAllowed()){
            //while this is not true, generate a toast message
            Toast.makeText(this, errorToastMessage, Toast.LENGTH_LONG).show();
        }

        else {

            //check if its a group session
            if (sessionType.equals("Group")) {
                this.sessionType = Utils.SessionType.GROUP;
                //if session is a group, then tell the student to add the remaining emails
                //inflate the view for the group email
                LayoutInflater factory = LayoutInflater.from(this);
                final View groupSessionEmailView = factory.inflate(R.layout.group_session_email, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(groupSessionEmailView)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText emailBox = (EditText) groupSessionEmailView.findViewById(R.id.other_emails);
                                String emails = emailBox.getText().toString();
                                setOtherEmails(emails);
                                if (!emails.isEmpty()) {
                                    dialog.cancel();
                                }
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.holoBlue));
                alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.holoBlue));
            }

            //check that the topic is not empty
            EditText topicBox = (EditText) findViewById(R.id.topic_edit);
            topics = topicBox.getText().toString();

            if (topics.isEmpty()) {
                Toast.makeText(this, "Please enter topics you need help with", Toast.LENGTH_LONG).show();
            } else {
                //add to the tutor's hours
                List<TutorInformation> tutors = DaysFragment.allTutors;
                for (int i = 0; i < tutors.size(); i++) {
                    if (tutors.get(i).getName().equals(tutorName)) {
                        tutors.get(i).addToHoursBooked(getHoursBooked());
                        //add the remaining hours
                        Map<String, Boolean> newTimes = adjustAvailability(tutors.get(i));
                        tutors.get(i).resetAvailability(day, newTimes);
                        DatabaseReference tutorsRef = reference.child(Utils.tutors);
                        tutorsRef.child(tutors.get(i).getName()).child("availability").setValue(tutors.get(i).getAvailability());
                        break;
                    }
                }
              //  sendSMS(tutorPhoneNumber, getSMS());
                sendEmail(tutorEmail);
                addToSessions();
                startActivity(intent);

            }
        }
    }

    //get the time a tutor is free broken down, and return the start time and end time in a list.
    //A list can only contain two items
    private List<String> getTime(String time) {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.time_list, R.layout.spinner_not_enabled);

        //split the time e.g time is 5:00pm-7:00pm.
        List<String> times = Arrays.asList(time.split("-"));
        String startTime = times.get(0);
        String endTime = times.get(1);
        //get the end of the time, if it is AM or PM
        String start = startTime.substring(startTime.length() - 2, startTime.length());
        String end = endTime.substring(endTime.length() - 2, endTime.length());
        //0 is AM and 1 is PM
        if (start.equals("am") && end.equals("am")) {
            startSpinner.setAdapter(adapter);
            endSpinner.setAdapter(adapter);
            startSpinner.setSelection(0);
            startSpinner.setEnabled(false);
            endSpinner.setSelection(0);
            endSpinner.setEnabled(false);
        }
        else if (start.equals("am") && end.equals("pm")) {
            startSpinner.setSelection(0);
            endSpinner.setSelection(1);
        }
        else {
            startSpinner.setAdapter(adapter);
            endSpinner.setAdapter(adapter);
            startSpinner.setSelection(1);
            startSpinner.setEnabled(false);
            endSpinner.setSelection(1);
            endSpinner.setEnabled(false);

        }
        List<String> justTime = new ArrayList();
        //replace all letters with spaces so you can get only integers
        //.replaceAll("[\\D]", "")

        //get 5:00 and 7:00
        justTime.add(startTime.substring(0, startTime.length() - 2));
        justTime.add(endTime.substring(0, endTime.length() - 2));
        return justTime;
    }

    private void sendEmail(String tutorEmail){

        String subject = "You have a request for " + course;
        String body = "Please log in to tutoring app to confirm this session or email us back if you don't have the app \n" + "Course : " +course + "\n Day: " +day + "\n Start time: " +
                startTime + "\n End time: "+endTime + "\n Student name: "+studentName;
        startActivity(Utils.sendEmail(tutorEmail, subject, body));
    }

    //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "Request Sent";
        String DELIVERED = "Request Delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Request sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Request delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "Request not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    private String getSMS() {
        return "You have a request, please log in to app to see details and to confirm the session. Thank you, ExcelLassonde";
    }

    /***
     * When a student books a time, this returns the remainder of what the student took.
     * @return e.g if a tutor is available from 8am-4pm, and the student requests only 8-9am, return 9-4pm
     * Also, if the student books 10-1pm, return 8-10am, and 1-2pm
     */
    public Map<String, Boolean> getRemainingTimes() {
        Map<String, Boolean> remainingTimes = new HashMap<>();

        String start, end, fullTime;

        //first case: the student uses the original start time
        if (getAdjustedNewStartTime() == getAdjustedStartTime()) {
            //if the end is the same, then we create a time ibject with the start to the end too
            //if the end is not the same, we create a new time object with the start to the new end
            //and then we create a new end to the end time object so we can store that too
            if (getAdjustedEndTime() != getAdjustedNewEndTime()) {
                //then create a new time with the newEnd to the original end
                start = ajustTimeFromDoubleToString(getAdjustedNewEndTime());
                end = ajustTimeFromDoubleToString(getAdjustedEndTime());
                fullTime = start + "-" + end;
                remainingTimes.put(fullTime, false);
            }
            //now add the time the student just booked but make it true that the
            //time has now been booked
            //if end is end too then we just return the original time chosen to add to the list

            start = ajustTimeFromDoubleToString(getAdjustedNewStartTime());
            end = ajustTimeFromDoubleToString(getAdjustedNewEndTime());
            fullTime = start + "-" + end;
            remainingTimes.put(fullTime, true);

        }
        //new start is not the same as start, it is somewhere in the middle
        //so we create one from the start to the new start
        else {
            start = ajustTimeFromDoubleToString(getAdjustedStartTime());
            end = ajustTimeFromDoubleToString(getAdjustedNewStartTime());
            fullTime = start + "-" + end;
            remainingTimes.put(fullTime, false);

            //we also have to add the time the student just booked
            //if new end time is equal to normal end time then we are good
            start = ajustTimeFromDoubleToString(getAdjustedNewStartTime());
            end = ajustTimeFromDoubleToString(getAdjustedNewEndTime());
            fullTime = start + "-" + end;
            remainingTimes.put(fullTime, true);

            //if end is not end then we create a new object too for the end
            if (getAdjustedEndTime() != getAdjustedNewEndTime()) {
                start = ajustTimeFromDoubleToString(getAdjustedNewEndTime());
                end = ajustTimeFromDoubleToString(getAdjustedEndTime());
                fullTime = start + "-" + end;
                remainingTimes.put(fullTime, false);

            }
        }

        return remainingTimes;
    }

    public String ajustTimeFromDoubleToString(Double time) {
        String appendToEnd = "";

        if (time > 12.59) {
            time -= 12.00;
            appendToEnd = "pm";
        } else if (time >= 12.00 && time <= 12.59) {
            appendToEnd = "pm";
        } else {
            appendToEnd = "am";
        }

        String timeToString = String.format("%.2f", time);
        String timeString = "" + timeToString + appendToEnd;
        timeString = timeString.replace(".", ":");
        return timeString;
    }

    public Map<String, Boolean> adjustAvailability(TutorInformation tutor) {
        //get the orginal availability of the tutor
        Map<String, Map<String, Boolean>> availability = tutor.getAvailability();
        //get the list assoicated with the day the student is trying to book
        Map<String, Boolean> times = availability.get(day);
        //now set the value of what the student just booked to true
        for (String time : times.keySet()) {
            if (time.equals(sessionTime)) {
                //remove that entry
                times.remove(time);
                //add the adjusted list
                times.putAll(getRemainingTimes());
                break;
            }
        }

        return times;
    }

    /***
     * We want to check three things with this.
     * 1. That the time booked is valid. That is, the end time is greater than the start time;
     * 2. That the time chosen is between the time the tutor is available for.
     *         - the new end time cannot be greater than the original end time
     *         - the new start time cannot be less than the original start time
     * 3. The time booked is not up to the max allowed to be booked at a time which is 3
     */
    public boolean checkTimeChosenIsAllowed() {
        boolean youCanBook = true;
        if (getAdjustedNewEndTime() <= getAdjustedNewStartTime()) {
            youCanBook = false;
            errorToastMessage = "Please check your time selection";
        }
        if (getAdjustedNewEndTime() > getAdjustedEndTime()) {
            youCanBook = false;
            errorToastMessage = "End time selected is not valid";
        }
        if (getAdjustedNewStartTime() < getAdjustedStartTime()) {
            youCanBook = false;
            errorToastMessage = "Start time selected is not valid";
        }
        if (getHoursBooked() > MAX_TUTORING_HOURS) {
            youCanBook = false;
            errorToastMessage = "A session can be at most 3 hours";
        }

        return youCanBook;
    }

    public void addToSessions() {

        List<String> studentEmailsList = new ArrayList<>();
        if(sessionType.equals(Utils.SessionType.GROUP)) {
            studentEmailsList.addAll(Arrays.asList(otherEmails.split(",")));
        }
        studentEmailsList.add(studentEmail);
        sessionID = reference.child("Sessions").push().getKey();

        //create a new session with the information given
        SessionInformation sessionInformation = new SessionInformation(sessionID, tutorName, course, day, startTime, endTime, sessionType.toString(), studentEmailsList, studentName, topics);
        reference.child(Utils.sessions).child(sessionID).setValue(sessionInformation);


        //also add the session to the student's list of sessions

    }


}
