package com.excellassonde.tutoringapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DaysFragment extends Fragment {
    //course chosen by the user
    public static String courseSelected = "";

    public static List<TutorInformation> allTutors;

    //set the course selected
    public static void setCourseSelected(String course) {
        courseSelected = course;
    }

    public static void setTutors(List<TutorInformation> tutors) {
        allTutors = tutors;
    }

    /***
     *
     * @param day - the day a tutor is wanted
     * @return if there are tutors available on a particular day
     */
    protected boolean AreTutorsAvailableThatDay(String day){
        List<TutorInformation> tutorsList = allTutors;
        Set<String> daysAvailable = new HashSet<>();
        boolean res = false;
        for(int i = 0; i < tutorsList.size(); i++) {
            daysAvailable.addAll (tutorsList.get(i).getDaysAvailable());
            for(String d: daysAvailable){
                if(d.equals(day)){
                    res =  true;
                }
            }
        }
        return res;
    }

    /***
     *
     * @param day a day tutoring is wanted
     * @param course a course being offered
     * @return the list of tutors that are available for day `day' and that teach course `course'
     */
    protected List<TutorInformation> getTutors(String day, String course) {
        List<TutorInformation> tutorsList = allTutors;
        List<TutorInformation> result = new ArrayList<>();
        for (int i = 0; i < tutorsList.size(); i++) {
            Set<String> daysAvailable = tutorsList.get(i).getDaysAvailable();
            if(daysAvailable.contains(day) && tutorsList.get(i).teachCourse(course)){
                result.add(tutorsList.get(i));
            }
        }

        return result;
    }

    //set the page view for the different tabs. This method is called by MonTab to FriTab
    protected void setDisplay(final String day, View view, LinearLayout layout) {
        String display;

        //text to display a string if no tutor is available
        TextView displayTextView = new TextView(view.getContext());

        //if there are tutors available, we want to be able to click on the tutors
        List<Button> displayTutors = new ArrayList<>();

        //if tutors are not even available on that day then don't check the list.
        //Just display we do not have tutors on that day
        if (AreTutorsAvailableThatDay(day)) {
            final List<TutorInformation> tutors = getTutors(day, courseSelected);
            if(tutors.isEmpty()){
                //there are tutors available on that day, but none that teach the course selected
                display = "Sorry, there are no tutors available for " + courseSelected + " on " + day;
                //set the text view, then add to layout
                displayTextView.setTextSize(20);
                displayTextView.setText(display);
                displayTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                layout.addView(displayTextView);
            }
            else{
                //this means this tutor is available and teach this course
                for (int i = 0; i < tutors.size(); i++) {
                    //get the times for that tutor
                    final Map<String, Boolean> times = tutors.get(i).getTimes(day);

                    for (String time : times.keySet()) {
                        final String name = tutors.get(i).getName();
                        display = name + ": " + time;
                        //add the tutor and times to a button
                        Button button = new Button(view.getContext());
                        if (Build.VERSION.SDK_INT >= 17) {
                            button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.ic_launcher_round, 0, 0);
                        }
                        button.setText(display);
                        //set the color and the way it looks
                        button.setTextColor(getResources().getColor(R.color.colorPrimary));
                        button.setBackgroundResource(R.drawable.textbox_background);
                        //if its that day, make every button disabled too
                        //if the tutor has NOT been booked, then getValue is false
                        //so if its true, the tutor has been booked- make the button un-clickable
                        //then make it greyed out if it is not avaialable
                        if (times.get(time)) {
                            button.setEnabled(false);
                            button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                            button.setTextColor(Color.GRAY);
                        }
                        //set space between the buttons
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(60, 20, 60, 0);
                        button.setLayoutParams(params);
                        button.setPadding(20,20,20,20);
                        //integers needed for getting objects in a loop
                        final String finalTime = time;
                        final int finalI = i;
                        button.setOnClickListener(new View.OnClickListener() {
                                                      public void onClick(View v) {
                                                          if (tutors.get(finalI).reachedMaximumHours()) {
                                                              showTutorExceededHoursNotification();
                                                          } else {
                                                              Intent intent1 = new Intent(getActivity(), SessionReview.class);
                                                              startActivity(intent1);
                                                          }
                                                          //change the isBooked for that time to be true
                                                          //setTutorIsBooked(name, times.get(finalJ).getKey());
                                                          SessionReview.setTime(finalTime);
                                                          SessionReview.setTutorName(name);
                                                          SessionReview.setCourse(courseSelected);
                                                          SessionReview.setTutorEmail(tutors.get(finalI).getEmail());
                                                          SessionReview.setDay(day);

                                                      }
                                                  });
                        displayTutors.add(button);
                    }
                }
            }

        } else {
            display = "Sorry, there are no tutors available on " + day;
            displayTextView.setText(display);
            displayTextView.setTextSize(20);
            displayTextView.setPadding(30, 0, 0, 0);
            displayTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            layout.addView(displayTextView);
        }
        //get the buttons and add them to the layout
        for(int i = 0; i< displayTutors.size(); i++){
            layout.addView(displayTutors.get(i));
        }
    }

    //get the current day, if the student is checking on that day, then make it disabled
    private String getCurrentDay(){
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return dayFormat.format(date);
    }

    private void showTutorExceededHoursNotification() {
        //if the tutor has reached the maximum hours they are willing to tutor, then
        //display a warning message
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View notificationView = factory.inflate(R.layout.available_hours_notification, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(notificationView)
                .setPositiveButton("Continue ?", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent1 = new Intent(getActivity(), SessionReview.class);
                        startActivity(intent1);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getWindow().setLayout(900, 500); //Controlling width and height.
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.holoBlue));
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.holoBlue));

    }



}
