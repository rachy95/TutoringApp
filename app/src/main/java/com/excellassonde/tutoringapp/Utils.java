package com.excellassonde.tutoringapp;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by praiseayorinde on 2017-12-08.
 */

public class Utils {

    public static final String appEmail = "excellassonde@gmail.com";
    public static final String tutors = "Tutors";
    public static final String sessions = "Sessions";
    public static final String Students = "Students";
    private static boolean isTranscriptSubmitted;

    public static  Map<String, Map<String, Boolean>> getAvailabilityInRightFormat(List<EditText> daysOfWeekEditBoxes){
        Map<String, Map<String, Boolean>> availability = new HashMap<>();
        String monday = daysOfWeekEditBoxes.get(0).getText().toString();
        String tuesday = daysOfWeekEditBoxes.get(1).getText().toString();
        String wednesday = daysOfWeekEditBoxes.get(2).getText().toString();
        String thursday = daysOfWeekEditBoxes.get(3).getText().toString();
        String friday = daysOfWeekEditBoxes.get(4).getText().toString();
        String saturday = daysOfWeekEditBoxes.get(5).getText().toString();
        String sunday = daysOfWeekEditBoxes.get(6).getText().toString();
        List<String> timesPerDay;
        String aTimeSlot;

        if(!monday.isEmpty()){
            Map<String, Boolean> mondayTimes = new HashMap<>();
            timesPerDay = Arrays.asList(monday.split(","));
            for (int i= 0; i<timesPerDay.size(); i++){
                aTimeSlot = getTimesInRightFormat(timesPerDay.get(i));
                mondayTimes.put(aTimeSlot, false);
            }
            availability.put("Monday", mondayTimes) ;
        }

        if(!tuesday.isEmpty()){
            Map<String, Boolean> tuesdayTimes = new HashMap<>();
            timesPerDay = Arrays.asList(tuesday.split(","));
            for (int i= 0; i<timesPerDay.size(); i++){
                aTimeSlot = getTimesInRightFormat(timesPerDay.get(i));
                tuesdayTimes.put(aTimeSlot, false);
            }
            availability.put("Tuesday", tuesdayTimes) ;
        }

        if(!wednesday.isEmpty()){
            Map<String, Boolean> wednesdayTimes = new HashMap<>();
            timesPerDay = Arrays.asList(wednesday.split(","));
            for (int i= 0; i<timesPerDay.size(); i++){
                aTimeSlot = getTimesInRightFormat(timesPerDay.get(i));
                wednesdayTimes.put(aTimeSlot, false);
            }
            availability.put("Wednesday", wednesdayTimes) ;
        }

        if(!thursday.isEmpty()){
            Map<String, Boolean> thursdayTimes = new HashMap<>();
            timesPerDay = Arrays.asList(thursday.split(","));
            for (int i= 0; i<timesPerDay.size(); i++){
                aTimeSlot = getTimesInRightFormat(timesPerDay.get(i));
                thursdayTimes.put(aTimeSlot, false);
            }
            availability.put("Thursday", thursdayTimes) ;
        }

        if(!friday.isEmpty()){
            Map<String, Boolean> fridayTimes = new HashMap<>();
            timesPerDay = Arrays.asList(friday.split(","));
            for (int i= 0; i<timesPerDay.size(); i++){
                aTimeSlot = getTimesInRightFormat(timesPerDay.get(i));
                fridayTimes.put(aTimeSlot, false);
            }
            availability.put("Friday", fridayTimes) ;
        }

        if(!saturday.isEmpty()){
            Map<String, Boolean> saturdayTimes = new HashMap<>();
            timesPerDay = Arrays.asList(saturday.split(","));
            for (int i= 0; i<timesPerDay.size(); i++){
                aTimeSlot = getTimesInRightFormat(timesPerDay.get(i));
                saturdayTimes.put(aTimeSlot, false);
            }
            availability.put("Saturday", saturdayTimes) ;
        }

        if(!sunday.isEmpty()){
            Map<String, Boolean> sundayTimes = new HashMap<>();
            timesPerDay = Arrays.asList(sunday.split(","));
            for (int i= 0; i<timesPerDay.size(); i++){
                aTimeSlot = getTimesInRightFormat(timesPerDay.get(i));
                sundayTimes.put(aTimeSlot, false);
            }
            availability.put("Sunday", sundayTimes) ;
        }

        return availability;
    }

    //there are many ways a person can put in the time, but we want it to all look in a certain way
    private static String getTimesInRightFormat(String time){
        String rightTime = "";
        List<String> times = Arrays.asList(time.split("-"));
        //so beginning time
        String startTime = times.get(0);
        String endTime = times.get(1);
        //1. we want to make sure that the time contains :00,
        //that is 3-6pm should change to 3:00-6:00pm
        //2. the user puts only a number, no ending time. e.g 3-6pm
        //then get the ending of the endTime
        if(!startTime.toLowerCase().endsWith("am") && !startTime.toLowerCase().endsWith("pm")){
            if(!startTime.contains(":")){
                startTime += ":00";
            }

            if(endTime.toLowerCase().endsWith("am") || endTime.toLowerCase().endsWith("pm")){
                startTime += endTime.substring(endTime.length() - 2, endTime.length());
            }

        }
        //this means that it has pm/am
        else if(!startTime.contains(":")){
            String startTimeTemp = startTime.substring(0, startTime.length()-2);
            startTime = startTime.replace(startTimeTemp, startTimeTemp+":00");
        }

        //we re making a risky assumption here that the end time always has am/pm
        if(!endTime.contains(":")){
            String endTimeTemp = endTime.substring(0, endTime.length() - 2);
            endTime = endTime.replace(endTimeTemp, endTimeTemp+":00");;
        }

        //to be safe, if the user puts in '.', we want to change it to ":"
        //e.g 2.30pm -> 2:30pm
        if(startTime.contains(".")){
            startTime = startTime.replace(".", ":");
        }
        if(endTime.contains(".")){
            endTime = endTime.replace(".", ":");
        }
        rightTime = startTime + "-" + endTime;

        return rightTime;
    }

    public static boolean getIsTranscriptSubmitted(){
        return isTranscriptSubmitted;
    }

    public static void setIsTranscriptSubmitted(boolean trueOrFalse){
        isTranscriptSubmitted = trueOrFalse;
    }

    public static String getTutorName(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String name = "";

        if(user != null){
            name = user.getDisplayName();
        }

        return name;
    }

    public enum SessionType {
        ONE_ON_ONE ("One-on-one"), GROUP("Group");


        private String value;

        private SessionType(String value){
            this.value = value;
        }

        public String toString(){
            return value;
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidStudentEmail(String email){
        return email.endsWith("@my.yorku.ca");
    }


    public static Intent sendEmail(String receiverEmail, String subject, String body){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String aEmailList[] = { Utils.appEmail};
        if(!TextUtils.isEmpty(receiverEmail)) {
            String aEmailBCCList[] = {receiverEmail};
            emailIntent.putExtra(android.content.Intent.EXTRA_BCC, aEmailBCCList);
        }

        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        return emailIntent;
    }

}
