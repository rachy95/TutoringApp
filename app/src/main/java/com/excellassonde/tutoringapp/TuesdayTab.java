package com.excellassonde.tutoringapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TuesdayTab extends DaysFragment {
    //you have to put the course too in checking

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the view from activity_tuesday_tab.xml
        View view = inflater.inflate(R.layout.activity_tuesday_tab, container, false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.TuesdayTab);
        //get the list of tutors
        String day = "Tuesday";
        setDisplay(day, view, layout);
        return view;
    }

}
