package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.Rating;
import com.rudik_maksim.cde.ScheduleAttestation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Максим on 01.12.2014.
 */
public class FragmentForPagerScheduleAttestation extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_WEEK = "week";
    private static final String ARG_NEAR_TAB_INDEX = "near_tab_index";

    ImageView imageView;
    Animation animation;
    Activity rootActivity;
    LinearLayout linRootLayout;
    LinearLayout linSubLayout;
    LayoutInflater layInflater;

    private int position, tabIndex;
    private String currentWeek = "";
    boolean disableAutoSetCurrentItem = false;

    public static FragmentForPagerScheduleAttestation newInstance(int position, String curWeek, int tabIndex) {
        FragmentForPagerScheduleAttestation f = new FragmentForPagerScheduleAttestation();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putInt(ARG_NEAR_TAB_INDEX, tabIndex);
        b.putString(ARG_WEEK, curWeek);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        tabIndex = getArguments().getInt(ARG_NEAR_TAB_INDEX);
        currentWeek = getArguments().getString(ARG_WEEK);

        if (FragmentScheduleAttestation.setCurrentItem)
            this.disableAutoSetCurrentItem = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_schedule, container, false);

        rootActivity = getActivity();

        imageView = (ImageView)inflatedView.findViewById(R.id.fragmentSchedule_imageView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        linSubLayout = (LinearLayout)inflatedView.findViewById(R.id.fragmentSchedule_linearLayoutContainer);
        linRootLayout = (LinearLayout)inflatedView.findViewById(R.id.fragment_schedule_linearLayout);
        layInflater = rootActivity.getLayoutInflater();

        return inflatedView;
    }

    @Override
    public void onStart(){
        super.onStart();

        if(Global.Application.currentFragmentId == Global.Configuration.NAV_ATTESTATION_SCHEDULE){
            Animation a = AnimationUtils.loadAnimation(rootActivity, R.anim.up_top);
            linRootLayout.startAnimation(a);
        }
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }

        showSchedule(position);
    }

    void showSchedule(int position){
        // Какие данные показывать определяем по currentWeek
        linSubLayout.removeAllViews();

        ArrayList<String> tests;
        HashMap<String, ArrayList<String>> weeks;
        String subject = "";

        for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry: Global.CDEData.SA_DATA.entrySet()){
            subject = entry.getKey().toString();
            weeks = entry.getValue();

            for (Map.Entry<String, ArrayList<String>> weeksSet: weeks.entrySet()){
                String weekNumber = weeksSet.getKey().toString();

                if (weekNumber.equals(currentWeek)){
                    // Выводим название предмета
                    View item = layInflater.inflate(R.layout.schedule_item, linSubLayout, false);
                    TextView tvSubject = (TextView)item.findViewById(R.id.schedule_item_textview_day);
                    tvSubject.setText(subject);

                    tests = weeksSet.getValue();

                    for (int i = 0; i < tests.size(); i++){
                        // Выводим названия тестов
                        LinearLayout ll = (LinearLayout)item.findViewById(R.id.schedule_item_linLayoutContent);
                        View subitem = layInflater.inflate(R.layout.schedule_attestation_sub_item, ll, false);

                        TextView tvTest = (TextView)subitem.findViewById(R.id.tv_schedule_attestation_test);
                        tvTest.setText(tests.get(i).toString());

                        ll.addView(subitem);
                    }

                    linSubLayout.addView(item);
                }
            }
        }

        if (disableAutoSetCurrentItem)
            Global.Configuration.isFirstAttestationOpen = false;

        if (Global.Configuration.isFirstAttestationOpen){
            FragmentScheduleAttestation.pager.setCurrentItem(tabIndex);
            FragmentScheduleAttestation.setCurrentItem = true;
        }
    }
}
