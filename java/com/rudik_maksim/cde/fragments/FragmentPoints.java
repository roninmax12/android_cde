package com.rudik_maksim.cde.fragments;

import android.app.Activity;
//import android.app.Fragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rudik_maksim.cde.ActivitySubjectDetails;
import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.Points;
import com.rudik_maksim.cde.Protocol;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.ScheduleAttestation;
import com.rudik_maksim.cde.adapters.ListViewPointsAdapter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Максим on 16.09.2014.
 */
public class FragmentPoints extends Fragment {
    ListView listView;
    TextView textView;
    ImageView imageView;
    Animation animation;
    FrameLayout fLayout;
    Activity rootActivity;
    boolean wasShowDataCurSem;

    public FragmentPoints(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template_listview, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle(R.string.title_fragmentPoints);
        rootActivity.getActionBar().setSubtitle(null);

        listView = (ListView)getView().findViewById(R.id.fragmentTemplate_listView);
        textView = (TextView)getView().findViewById(R.id.fragmentTemplate_textView);
        imageView = (ImageView)getView().findViewById(R.id.fragmentTemplate_imageView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        fLayout = (FrameLayout)getView().findViewById(R.id.fragmentTemplate_frameLayout);

        Global.Application.currentFragmentId = Global.Configuration.NAV_POINTS;

        if (!Global.DataLoaded.Points){
            if (!Global.Loading.Points){
                Global.Loading.Points = true;
                new AsyncGetPoints().execute(Global.CDEData.SELECTED_YEAR);
            }
        }
        else{
            if (!Global.Loading.Points)
                showPoints();
        }
    }

    class AsyncGetPoints extends AsyncTask<String,Void,Void> {
        private Points p;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            listView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(animation);

            p = new Points();
        }

        @Override
        protected Void doInBackground(String... params){
            try{
                p.parse(params[0]);
            }catch (Exception ex){}

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            imageView.setVisibility(View.INVISIBLE);
            imageView.clearAnimation();
            Global.DataLoaded.Points = true;

            if (Global.Application.currentFragmentId == Global.Configuration.NAV_POINTS){
                Animation a = AnimationUtils.loadAnimation(rootActivity, R.anim.up_top);
                fLayout.startAnimation(a);
            }

            Global.Loading.Points = false;
            showPoints();
        }
    }

    void showPoints(){
        //SHOW
        String[] arrSubjects;
        String[] arrPoints;
        String[] arrControls;

        wasShowDataCurSem = Global.Configuration.show_data_on_cur_sem;

        if (Global.Configuration.currentYearChosen != null){
            if (!Global.Configuration.currentYearChosen.equals(Global.CDEData.YEARS.get(Global.CDEData.YEARS.size() - 1))){
                // т.к. ТЕКУЩИМ м.б. только 1 семестр, то данные за остальной период обучения показываем за целый год,
                // для этого временно меняем значение переменной ниже. В конце метода вернем обратно, для этого служит
                // переменная wasShowDataCurSem
                Global.Configuration.show_data_on_cur_sem = false;
            }
        }

        if (!Global.Configuration.show_data_on_cur_sem){
            arrSubjects = new String[Global.CDEData.SUBJECTS1.size() + Global.CDEData.SUBJECTS2.size()];
            arrPoints = new String[Global.CDEData.POINTS1.size() + Global.CDEData.POINTS2.size()];
            arrControls = new String[Global.CDEData.CONTROL1.size() + Global.CDEData.CONTROL2.size()];

            if (arrSubjects.length == 0){
                Global.DataLoaded.Points = false;
                return;
            }

            String[] sub1 = new String[Global.CDEData.SUBJECTS1.size()];
            String[] sub2 = new String[Global.CDEData.SUBJECTS2.size()];
            String[] p1 = new String[Global.CDEData.POINTS1.size()];
            String[] p2 = new String[Global.CDEData.POINTS2.size()];
            String[] c1 = new String[Global.CDEData.CONTROL1.size()];
            String[] c2 = new String[Global.CDEData.CONTROL2.size()];

            Global.CDEData.SUBJECTS1.toArray(sub1);
            Global.CDEData.SUBJECTS2.toArray(sub2);
            Global.CDEData.POINTS1.toArray(p1);
            Global.CDEData.POINTS2.toArray(p2);
            Global.CDEData.CONTROL1.toArray(c1);
            Global.CDEData.CONTROL2.toArray(c2);

            int curIndex = 0;
            for (int i = 0; i < sub1.length; i++){
                arrSubjects[i] = sub1[i];
                arrPoints[i] = p1[i];
                arrControls[i] = c1[i];
                curIndex++;
            }
            for (int i = 0; i < sub2.length; i++){
                arrSubjects[curIndex] = sub2[i];
                arrPoints[curIndex] = p2[i];
                arrControls[curIndex] = c2[i];
                curIndex++;
            }
        }else{
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
            int current_month = cal.get(Calendar.MONTH) + 1;
            if (current_month > 1 && current_month < 9){
                arrSubjects = new String[Global.CDEData.SUBJECTS2.size()];
                arrPoints = new String[Global.CDEData.POINTS2.size()];
                arrControls = new String[Global.CDEData.CONTROL2.size()];

                Global.CDEData.SUBJECTS2.toArray(arrSubjects);
                Global.CDEData.POINTS2.toArray(arrPoints);
                Global.CDEData.CONTROL2.toArray(arrControls);
            }else{
                arrSubjects = new String[Global.CDEData.SUBJECTS1.size()];
                arrPoints = new String[Global.CDEData.POINTS1.size()];
                arrControls = new String[Global.CDEData.CONTROL1.size()];

                Global.CDEData.SUBJECTS1.toArray(arrSubjects);
                Global.CDEData.POINTS1.toArray(arrPoints);
                Global.CDEData.CONTROL1.toArray(arrControls);
            }
        }

        Global.Configuration.show_data_on_cur_sem = wasShowDataCurSem;

        for (int i = 0; i<arrSubjects.length; i++){
            int fisrtIndex = arrSubjects[i].indexOf('(');
            arrSubjects[i] = arrSubjects[i].substring(0,fisrtIndex-1);
        }

        ListViewPointsAdapter lviewAdapter = new ListViewPointsAdapter(rootActivity, arrSubjects, arrPoints, arrControls);
        listView.setAdapter(lviewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String subject_title = "not_initialized";
                String syu_id = "not_initialized";

                if (Global.Configuration.show_data_on_cur_sem) {
                    if (Global.Configuration.currentYearChosen != null) {
                        if (!Global.Configuration.currentYearChosen.equals(Global.CDEData.YEARS.get(Global.CDEData.YEARS.size() - 1))) {
                            ArrayList<String> SUBJECTS_ALL = new ArrayList<String>();
                            SUBJECTS_ALL.addAll(Global.CDEData.SUBJECTS1);
                            SUBJECTS_ALL.addAll(Global.CDEData.SUBJECTS2);

                            subject_title = SUBJECTS_ALL.get(position).toString();
                            syu_id = Global.CDEData.URLS.get(position).toString();
                        } else {
                            if (Global.Configuration.show_data_on_cur_sem) {
                                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                                int current_month = cal.get(Calendar.MONTH) + 1;

                                if (current_month > 1 && current_month < 9) {
                                    subject_title = Global.CDEData.SUBJECTS2.get(position).toString();
                                    int indexes = Global.CDEData.SUBJECTS2.size() - 1;
                                    syu_id = Global.CDEData.URLS.get(indexes + position).toString();
                                } else {
                                    subject_title = Global.CDEData.SUBJECTS1.get(position).toString();
                                    syu_id = Global.CDEData.URLS.get(position).toString();
                                }
                            } else {
                                ArrayList<String> SUBJECTS_ALL = new ArrayList<String>();
                                SUBJECTS_ALL.addAll(Global.CDEData.SUBJECTS1);
                                SUBJECTS_ALL.addAll(Global.CDEData.SUBJECTS2);

                                subject_title = SUBJECTS_ALL.get(position).toString();
                                syu_id = Global.CDEData.URLS.get(position).toString();
                            }
                        }
                    } else {
                        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                        int current_month = cal.get(Calendar.MONTH) + 1;

                        if (current_month > 1 && current_month < 9) {
                            subject_title = Global.CDEData.SUBJECTS2.get(position).toString();
                            int indexes = Global.CDEData.SUBJECTS2.size() - 1;
                            syu_id = Global.CDEData.URLS.get(indexes + position).toString();
                        } else {
                            subject_title = Global.CDEData.SUBJECTS1.get(position).toString();
                            syu_id = Global.CDEData.URLS.get(position).toString();
                        }
                    }
                } else {
                    ArrayList<String> SUBJECTS_ALL = new ArrayList<String>();
                    SUBJECTS_ALL.addAll(Global.CDEData.SUBJECTS1);
                    SUBJECTS_ALL.addAll(Global.CDEData.SUBJECTS2);

                    subject_title = SUBJECTS_ALL.get(position).toString();
                    syu_id = Global.CDEData.URLS.get(position).toString();
                }

                Intent subject_details = new Intent(rootActivity, ActivitySubjectDetails.class);
                subject_details.putExtra("subject_title", subject_title);
                subject_details.putExtra("syu_id", syu_id);

                ArrayList<String> SUBJECTS_ALL = new ArrayList<String>();
                SUBJECTS_ALL.addAll(Global.CDEData.SUBJECTS1);
                SUBJECTS_ALL.addAll(Global.CDEData.SUBJECTS2);
                startActivity(subject_details);
            }
        });

        listView.setVisibility(View.VISIBLE);

        if (arrSubjects.length == 0){
            textView.setText("Данные не загружены");
            textView.setVisibility(View.VISIBLE);
        }

        if (!Global.Configuration.isProtocolBackgroundLoaded){
            new AsyncGetProtocolFirstRow().execute();
        }
    }

    class AsyncGetProtocolFirstRow extends AsyncTask<Void,Void,Void> {
        private Protocol p;
        boolean successLoad = true;

        @Override
        protected Void doInBackground(Void... params){
            try{
                p = new Protocol();
                p.parseBackground();
            }catch (Exception ex){successLoad = false;}
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if(successLoad){
                saveFirstLineOfProtocolChanges(Global.CDEData.first_P_subject, Global.CDEData.first_P_description, Global.CDEData.first_P_point);
                Global.Configuration.isProtocolBackgroundLoaded = true;
            }
        }
    }

    public void saveFirstLineOfProtocolChanges(final String subject, final String description, final String userPoint){
        /*
        * This method needs for NewPointsNotificationService
        * */

        Global.Application.preferences.edit().putString("shared_protocol", subject + "_" + description + "_" + userPoint).commit();


         new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OutputStream outputStream = new FileOutputStream(Global.Application.FILE_PROTOCOL);
                    OutputStreamWriter outStream = new OutputStreamWriter(outputStream);
                    outStream.write(subject +'\n');
                    outStream.write(description + '\n');
                    outStream.write(userPoint + '\n');

                    outStream.close();
                }
                catch (Throwable t) {
                    return;
                }
            }
        }).start();
    }
}
