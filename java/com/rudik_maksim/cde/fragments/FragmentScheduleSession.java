package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;

/**
 * Created by Максим on 11.11.2014.
 */
public class FragmentScheduleSession extends Fragment {
    public FragmentScheduleSession(){
    }

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private PagerScheduleSessionAdapter adapter;
    Activity rootActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_schedule_session, container, false);

        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle(R.string.title_fragmentScheduleSession);
        rootActivity.getActionBar().setSubtitle(null);

        tabs = (PagerSlidingTabStrip) inflatedView.findViewById(R.id.tabs);
        pager = (ViewPager) inflatedView.findViewById(R.id.pager);
        adapter = new PagerScheduleSessionAdapter(getChildFragmentManager());

        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        Global.Application.currentFragmentId = Global.Configuration.NAV_SESSION_SCHEDULE;

        return inflatedView;
    }

    public class PagerScheduleSessionAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Экзамены", "Консультации"};

        public PagerScheduleSessionAdapter(FragmentManager fm) {
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
            return FragmentForPagerScheduleSession.newInstance(position);
        }
    }
}
