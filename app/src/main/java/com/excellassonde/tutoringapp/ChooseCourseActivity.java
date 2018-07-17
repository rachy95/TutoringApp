package com.excellassonde.tutoringapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChooseCourseActivity extends AppCompatActivity {
    static Set<String> allCourses;
    List<Button> displayCourses;

    public static boolean flagIfCoursesAvailable = true;



    public static void setFlagIfCoursesAvailable(boolean trueOrFalse){
        flagIfCoursesAvailable = trueOrFalse;
    }

    public static void setCourses(Set<String> courses) {
        allCourses = courses;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(flagIfCoursesAvailable) {
            setContentView(R.layout.activity_choose_course);
            LinearLayout layout = (LinearLayout) findViewById(R.id.chooseCourse);

            displayCourses = new ArrayList<>();

            TextView helperText = (TextView) findViewById(R.id.choose_course_beginning_text);
            helperText.setText(R.string.loading_message);

            if (allCourses != null) {
                helperText.setText(R.string.course_helper_text);
                for (final String course : allCourses) {
                    //add the tutor and times to a button
                    Button button = new Button(this);
                    button.setText(course);
                    //set the color and the way it looks
                    button.setTextColor(getResources().getColor(R.color.colorPrimary));
                    button.setBackgroundResource(R.drawable.textbox_background);
                    //set space between the buttons
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(60, 20, 60, 0);
                    button.setLayoutParams(params);
                    button.setPadding(20, 20, 20, 20);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(ChooseCourseActivity.this, ChooseDayAndTutor.class);
                            DaysFragment.setCourseSelected(course);
                            startActivity(intent);

                        }
                    });
                    displayCourses.add(button);
                }

                //get the buttons and add them to the layout
                for (int i = 0; i < displayCourses.size(); i++) {
                    layout.addView(displayCourses.get(i));
                }
            }
        }
        else {
            setContentView(R.layout.no_courses_available);
        }
    }



}
