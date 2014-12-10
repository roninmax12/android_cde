package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.ScheduleAttestation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Максим on 02.12.2014.
 */
public class FragmentScheduleAttestation extends Fragment {

    private PagerSlidingTabStrip tabs;
    private PagerAttestationAdapter adapter;

    public static ViewPager pager;
    public static boolean setCurrentItem = false;

    ImageView imageView;
    TextView textView;
    Animation animation;
    Activity rootActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_schedule_attestation, container, false);

        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle(R.string.title_fragmentScheduleAttestation);

        tabs = (PagerSlidingTabStrip) inflatedView.findViewById(R.id.fragment_schedule_tabs);
        pager = (ViewPager) inflatedView.findViewById(R.id.fragment_schedule_pager);
        imageView = (ImageView) inflatedView.findViewById(R.id.fragmentScheduleAttestation_imageView);
        textView = (TextView) inflatedView.findViewById(R.id.fragmentScheduleAttestation_textView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);

        Global.Application.currentFragmentId = Global.Configuration.NAV_ATTESTATION_SCHEDULE;

        return inflatedView;
    }

    @Override
    public void onStart(){
        super.onStart();

        if (Global.CDEData.CUR_GROUP.contains("и")){
            textView.setText("К сожалению, расписание аттестаций для студентов ИХиБТ недоступно из приложения");
            textView.setVisibility(View.VISIBLE);
            return;
        }

        if (!Global.DataLoaded.ScheduleAttestation){
            if (!Global.Loading.ScheduleAttestation){
                Global.Loading.ScheduleAttestation = true;
                new AsyncGetScheduleAttestation().execute();
            }
        }
        else{
            if (!Global.Loading.ScheduleAttestation){
                if (!Global.CDEData.attestationNotFound) {
                    adapter = new PagerAttestationAdapter(getChildFragmentManager());
                    pager.setAdapter(adapter);
                    tabs.setViewPager(pager);
                }else{
                    showNotFoundMessage();
                }
            }
        }
    }

    void showNotFoundMessage(){
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop(){
        super.onStop();
        Global.Configuration.isFirstAttestationOpen = true;
        setCurrentItem = false;
    }

    class AsyncGetScheduleAttestation extends AsyncTask<Void,Void,Void> {
        private ScheduleAttestation scheduleAttestation;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(animation);
            scheduleAttestation = new ScheduleAttestation();
        }

        @Override
        protected Void doInBackground(Void... params){
            try{
                scheduleAttestation.parse();
            }catch (Exception ex){}
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            imageView.setVisibility(View.INVISIBLE);
            imageView.clearAnimation();

            Global.DataLoaded.ScheduleAttestation = true;
            Global.Loading.ScheduleAttestation = false;

            for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry_data: Global.CDEData.SA_DATA.entrySet()) {
                String subject = entry_data.getKey().toString();

                if (subject.length() < 2){
                    Global.CDEData.attestationNotFound = true;
                    showNotFoundMessage();
                }else{
                    adapter = new PagerAttestationAdapter(getChildFragmentManager());
                    pager.setAdapter(adapter);
                    tabs.setViewPager(pager);
                }
            }

            if (Global.CDEData.WeekNumber != 0){
                String[] days = {"воскресенье", "понедельник", "вторник", "среда", "четверг", "пятница", "суббота"};
                Calendar calendar = Calendar.getInstance();
                rootActivity.getActionBar().setSubtitle(Global.CDEData.WeekNumber + " неделя, " + days[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
            }
        }
    }

    Random rand = new Random();

    public void qSort(List<Integer> array) {
        int n = array.size();
        int i = 0;
        int j = n-1;
        int x = array.get(rand.nextInt(n));
        while (i <= j) {
            while (array.get(i) < x) {
                i++;
            }
            while (array.get(j) > x) {
                j--;
            }
            if (i <= j) {
                Collections.swap(array, i, j);
                i++;
                j--;
            }
        }
        if (j>0){
            qSort(array.subList(0, j + 1));
        }
        if (i<n){
            qSort(array.subList(i,n));
        }
    }

    int getNearTabIndex(ArrayList<Integer> titles){
        for (int i = 0; i < titles.size(); i++){
            if (titles.get(i) >= Global.CDEData.WeekNumber)
                return i;
        }
        return titles.size() - 1;
    }

    public class PagerAttestationAdapter extends FragmentPagerAdapter {
        ArrayList<Integer> titles = new ArrayList<Integer>();
        int tabIndex;

        public PagerAttestationAdapter(FragmentManager fm) {
            super(fm);

            for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry: Global.CDEData.SA_DATA.entrySet()){
                HashMap<String, ArrayList<String>> weeks = entry.getValue();

                for (Map.Entry<String, ArrayList<String>> subentry: weeks.entrySet()){
                    String week = subentry.getKey().toString();
                    int weekNumber = Integer.parseInt(week.replace(" неделя",""));
                    if (!titles.contains(weekNumber))
                        titles.add(weekNumber);
                }
            }

            if (titles.size() > 1)
                qSort(titles);

            tabIndex = getNearTabIndex(titles);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position) + " неделя";
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentForPagerScheduleAttestation.newInstance(position, titles.get(position) + " неделя", tabIndex);
        }
    }
}
