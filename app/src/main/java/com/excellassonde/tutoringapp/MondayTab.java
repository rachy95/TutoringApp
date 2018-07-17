package com.excellassonde.tutoringapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MondayTab extends DaysFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view from activity_monday_tab.xml
        View view = inflater.inflate(R.layout.activity_monday_tab, container, false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.MondayTab);

        String day = "Monday";
        setDisplay(day, view, layout);
        return view;
    }

}


