package com.excellassonde.tutoringapp;

import android.os.Bundle;
import android.support.design.widget.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class ChooseDayAndTutor extends AppCompatActivity {
    private TextView displayCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_day_and_tutor);
        displayCourse = (TextView) findViewById(R.id.toolbarText);
        String course = DaysFragment.courseSelected;
        String text = "Course chosen : "+ course;
        displayCourse.setText(text);

        TabLayout daysTabs = (TabLayout) findViewById(R.id.dayTabs);

        final PagerAdapter adapter = new DaysTabPagerAdapter(getSupportFragmentManager(), daysTabs.getTabCount());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pageView);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(daysTabs));
        daysTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

}
