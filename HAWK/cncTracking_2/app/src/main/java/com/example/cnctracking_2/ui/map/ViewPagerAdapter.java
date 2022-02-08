package com.example.cnctracking_2.ui.map;


import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Saylani_IT on 18/10/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    String[] mTitles;
    String[] mTitlesFuel;
    int numCheck;
    List<String> mtitles;
    boolean tempAllow = false;
    boolean fuelAllow = false;
    boolean TripReportAllow = false;
    boolean HistoryOnMapAllow = false;
    boolean SpeedAnalysisAllow = false;
    boolean case1 = false;
    boolean case2 = false;
    boolean case3 = false;
    boolean case4 = false;
    boolean case5 = false;
    int listSize;
    Object[] obj = new Object[10];


    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs, String[] titles, boolean TripReportAllow, boolean SpeedAnalysisAllow, boolean HistoryOnMapAllow, boolean tempAllow, boolean fuelAllow, String unitType) {
        super(fm);
        mTitles = titles;
        numCheck = NumOfTabs;
        this.TripReportAllow = TripReportAllow;
        this.SpeedAnalysisAllow = SpeedAnalysisAllow;
        this.HistoryOnMapAllow = HistoryOnMapAllow;
        this.tempAllow = tempAllow;
        this.fuelAllow = fuelAllow;


        ArrayList<Fragment> ar = new ArrayList<Fragment>();

        obj = new Object[numCheck+1];

        obj[0] = new Fragment1();// trip
        int i =1;

        if (TripReportAllow) {
            obj[i] = new Fragment3();// trip
            i++;
        }
        if (SpeedAnalysisAllow) {
            obj[i] = new Fragment3();// speed
            i++;

        }
        if (HistoryOnMapAllow) {
            obj[i] = new Fragment3();//map
            i++;

        }
        if (tempAllow) {
            obj[i] = new Fragment3();//temp
            i++;

        }
        if (fuelAllow) {
            obj[i] = new Fragment3();// fuel}
            i++;
        }

    }



    @Override
    public Fragment getItem(int position) {
        if(position <= numCheck )
        {
            return (Fragment) obj[position];
        }

        return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
