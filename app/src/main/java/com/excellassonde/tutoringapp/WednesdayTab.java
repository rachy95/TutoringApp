package com.excellassonde.tutoringapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class WednesdayTab extends DaysFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view from activity_wednesday_tab.xml
        View view = inflater.inflate(R.layout.activity_wednesday_tab, container, false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.WednesdayTab);
        //get the list of tutors
        String day = "Wednesday";
        setDisplay(day, view, layout);
        return view;
    }
}
