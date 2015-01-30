package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.ScheduleSession;
import com.rudik_maksim.cde.adapters.ListViewScheduleSessionAdapter;

/**
 * Created by Максим on 12.11.2014.
 */
public class FragmentForPagerScheduleSession extends Fragment {
    private static final String ARG_POSITION = "position";
    ListView listView;
    TextView textView;
    ImageView imageView;
    Animation animation;
    FrameLayout fLayout;
    Activity rootActivity;

    private int position;

    public static FragmentForPagerScheduleSession newInstance(int position) {
        FragmentForPagerScheduleSession f = new FragmentForPagerScheduleSession();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_template_listview, container, false);

        rootActivity = getActivity();

        listView = (ListView)inflatedView.findViewById(R.id.fragmentTemplate_listView);
        textView = (TextView)inflatedView.findViewById(R.id.fragmentTemplate_textView);
        imageView = (ImageView)inflatedView.findViewById(R.id.fragmentTemplate_imageView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        fLayout = (FrameLayout)inflatedView.findViewById(R.id.fragmentTemplate_frameLayout);

        return inflatedView;
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

        if (!Global.DataLoaded.ScheduleSession){
            if (!Global.Loading.ScheduleSession){
                Global.Loading.ScheduleSession = true;
                new AsyncGetSessionSchedule().execute();
            }
        }
        else{
            if (!Global.Loading.ScheduleSession)
                showScheduleSession(position);
        }

        //INSERT CUSTOM CODE HERE
    }

    class AsyncGetSessionSchedule extends AsyncTask<Void,Void,Void> {
        private ScheduleSession ss;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            listView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(animation);
            ss = new ScheduleSession();
        }

        @Override
        protected Void doInBackground(Void... params){
            try{
                ss.parseNew();
            }catch (Exception ex){}

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            imageView.setVisibility(View.INVISIBLE);
            imageView.clearAnimation();
            Global.DataLoaded.ScheduleSession = true;

            if (Global.Application.currentFragmentId == Global.Configuration.NAV_SESSION_SCHEDULE){
                Animation a = AnimationUtils.loadAnimation(rootActivity, R.anim.up_top);
                fLayout.startAnimation(a);
            }

            Global.Loading.ScheduleSession = false;
            showScheduleSession(0);
        }
    }

    void getLogScheduleData(){
        int length = Global.CDEData.SS_SUBJECTS.size();

        Log.d(Global.Debug.LOG_TAG, "SUBJECTS: ");
        for (int i = 0; i < length; i++)
            Log.d(Global.Debug.LOG_TAG, ".   " + Global.CDEData.SS_SUBJECTS.get(i).toString());

        Log.d(Global.Debug.LOG_TAG, "\nTEACHERS: ");
        for (int i = 0; i < length; i++)
            Log.d(Global.Debug.LOG_TAG, ".   " + Global.CDEData.SS_TEACHERS.get(i).toString());

        Log.d(Global.Debug.LOG_TAG, "\nEXAMS: ");
        for (int i = 0; i < length; i++) {
            Log.d(Global.Debug.LOG_TAG, ".   DATE: " + Global.CDEData.SS_EXAM_DATE.get(i).toString());
            Log.d(Global.Debug.LOG_TAG, ".   TIME: " + Global.CDEData.SS_EXAM_TIME.get(i).toString());
            Log.d(Global.Debug.LOG_TAG, ".   PLACE: " + Global.CDEData.SS_EXAM_PLACE.get(i).toString() + "\n");
        }

        Log.d(Global.Debug.LOG_TAG, "CONS: ");
        for (int i = 0; i < length; i++) {
            Log.d(Global.Debug.LOG_TAG, ".   DATE: " + Global.CDEData.SS_CONS_DATE.get(i).toString());
            Log.d(Global.Debug.LOG_TAG, ".   TIME: " + Global.CDEData.SS_CONS_TIME.get(i).toString());
            Log.d(Global.Debug.LOG_TAG, ".   PLACE: " + Global.CDEData.SS_CONS_PLACE.get(i).toString() + "\n");
        }
    }

    void showScheduleSession(int position){
        //getLogScheduleData();
        int length = Global.CDEData.SS_SUBJECTS.size();

        String[] arrTeachers  = new String[length]; Global.CDEData.SS_TEACHERS.toArray(arrTeachers);
        String[] arrSubjects  = new String[length]; Global.CDEData.SS_SUBJECTS.toArray(arrSubjects);

        String[] arrDates     = new String[length];
        String[] arrTimes     = new String[length];
        String[] arrPlaces     = new String[length];

        if (position == 0){
            //Exam page
            Global.CDEData.SS_EXAM_DATE.toArray(arrDates);
            Global.CDEData.SS_EXAM_TIME.toArray(arrTimes);
            Global.CDEData.SS_EXAM_PLACE.toArray(arrPlaces);
        }else{
            //Consultation page
            Global.CDEData.SS_CONS_DATE.toArray(arrDates);
            Global.CDEData.SS_CONS_TIME.toArray(arrTimes);
            Global.CDEData.SS_CONS_PLACE.toArray(arrPlaces);
        }

        ListViewScheduleSessionAdapter lviewAdapter = new ListViewScheduleSessionAdapter(rootActivity, arrTeachers,
                arrSubjects, arrDates, arrTimes, arrPlaces);

        listView.setAdapter(lviewAdapter);
        listView.setVisibility(View.VISIBLE);

        if (length == 0){
            textView.setText("Нет данных");
            textView.setVisibility(View.VISIBLE);
        }
    }
}
