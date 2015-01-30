package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.rudik_maksim.cde.ActivityScheduleGroup;
import com.rudik_maksim.cde.ActivityScheduleTeacher;
import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;

import java.util.Calendar;

/**
 * Created by Максим on 14.11.2014.
 */
public class FragmentScheduleGroup extends Fragment {
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private PagerScheduleTeacherAdapter adapter;
    Activity rootActivity;

    public FragmentScheduleGroup(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView =  inflater.inflate(R.layout.fragment_schedule_new, container, false);

        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle("Расписание занятий");
        rootActivity.getActionBar().setSubtitle("Группа " + ActivityScheduleGroup.groupNumber);
/*
        if (Global.CDEData.WeekNumber != 0){
            String[] days = {"воскресенье", "понедельник", "вторник", "среда", "четверг", "пятница", "суббота"};
            Calendar calendar = Calendar.getInstance();
            rootActivity.getActionBar().setSubtitle(Global.CDEData.WeekNumber + " неделя, " + days[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        }*/

        Global.DataLoaded.ScheduleGroup = false;
        Global.CDEData.SG_DATA.clear();

        tabs = (PagerSlidingTabStrip) inflatedView.findViewById(R.id.fragment_schedule_tabs);
        pager = (ViewPager) inflatedView.findViewById(R.id.fragment_schedule_pager);
        adapter = new PagerScheduleTeacherAdapter(getChildFragmentManager());

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        return inflatedView;
    }

    public class PagerScheduleTeacherAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Чётная неделя", "Нечётная неделя"};

        public PagerScheduleTeacherAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentForPagerScheduleGroup.newInstance(position);
        }
    }
}
