package com.excellassonde.tutoringapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by PRAISEAyorinde on 2017-07-22.
 */

class DaysTabPagerAdapter extends FragmentPagerAdapter {
    final int TAB_COUNT;


    public DaysTabPagerAdapter(FragmentManager supportFragmentManager, int numberOfTabs) {
        super(supportFragmentManager);
        this.TAB_COUNT = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MondayTab mondayTab = new MondayTab();
                return mondayTab;
            case 1:
                TuesdayTab tuesdayTab = new TuesdayTab();
                return tuesdayTab;
            case 2:
                WednesdayTab wednesdayTab = new WednesdayTab();
                return wednesdayTab;
            case 3:
                ThursdayTab thursdayTab = new ThursdayTab();
                return thursdayTab;
            case 4:
                FridayTab fridayTab = new FridayTab();
                return fridayTab;
            default:
                return null;
        }
    }

    public int getCount(){
        return TAB_COUNT;
    }

}
