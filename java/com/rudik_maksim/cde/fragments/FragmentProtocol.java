package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.Protocol;
import com.rudik_maksim.cde.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Максим on 16.09.2014.
 */
public class FragmentProtocol extends Fragment {
    ExpandableListView expListView;
    TextView textView;
    ImageView imageView;
    Animation animation;
    FrameLayout fLayout;
    Activity rootActivity;

    public FragmentProtocol(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_protocol, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle(R.string.title_fragmentProtocol);
        rootActivity.getActionBar().setSubtitle(null);

        expListView = (ExpandableListView)getView().findViewById(R.id.fragmentProtocol_ExpandableListView);
        textView = (TextView)getView().findViewById(R.id.fragmentProtocol_textView);
        imageView = (ImageView)getView().findViewById(R.id.fragmentProtocol_imageView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        fLayout = (FrameLayout)getView().findViewById(R.id.fragmentProtocol_frameLayout);

        Global.Application.currentFragmentId = Global.Configuration.NAV_PROTOCOL;

        ViewTreeObserver vto = expListView.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    expListView.setIndicatorBounds(expListView.getRight() - 64, expListView.getWidth());
                } else {
                    expListView.setIndicatorBoundsRelative(expListView.getRight() - 64, expListView.getWidth());
                }
            }
        });

        if (!Global.DataLoaded.Protocol){
            if (!Global.Loading.Protocol){
                Global.Loading.Protocol = true;
                new AsyncGetProtocol().execute();
            }
        }
        else{
            if (!Global.Loading.Protocol)
                showProtocol();
        }
    }

    class AsyncGetProtocol extends AsyncTask<Void,Void,Void> {
        private Protocol p;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            imageView.setVisibility(View.VISIBLE);
            expListView.setVisibility(View.INVISIBLE);
            imageView.startAnimation(animation);
            p = new Protocol();
        }

        @Override
        protected Void doInBackground(Void... params){
            try{
                p.parse();
            }catch (Exception ex){}
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            imageView.setVisibility(View.INVISIBLE);
            imageView.clearAnimation();
            Global.DataLoaded.Protocol = true;

            if (Global.Application.currentFragmentId == Global.Configuration.NAV_PROTOCOL){
                Animation a = AnimationUtils.loadAnimation(rootActivity, R.anim.up_top);
                fLayout.startAnimation(a);
            }

            Global.Loading.Protocol = false;
            showProtocol();
        }
    }

    void showProtocol(){
        int length = Global.CDEData.P_SUBJECTS.size();

        String[] arrPSubjects   = new String[length];
        String[] arrDescription = new String[length];
        String[] arrUserPoints  = new String[length];
        String[] arrMaxPoints   = new String[length];
        String[] arrDescs        = new String[length*2];

        Global.CDEData.P_SUBJECTS.toArray(arrPSubjects);
        Global.CDEData.P_DESCRIPTION.toArray(arrDescs);
        Global.CDEData.P_USERPOINTS.toArray(arrUserPoints);

        for (int i = 0; i<length; i++){
            int fisrtIndex = arrPSubjects[i].indexOf('(');
            arrPSubjects[i] = arrPSubjects[i].substring(0,fisrtIndex-1);
        }

        int maxIndex = 0;
        for (int i = 0; i<arrDescs.length; i++){
            if (i % 2 == 0){
                //Description
                arrDescription[i/2] = arrDescs[i];
            }else{
                //Points
                int firstIndex = arrDescs[i].indexOf(']');
                arrMaxPoints[maxIndex++] = arrDescs[i].substring(firstIndex-10,firstIndex).trim();
            }
        }

        if (arrPSubjects.length > 0){
            Global.CDEData.first_P_subject = arrPSubjects[0];
            Global.CDEData.first_P_description = arrDescription[0];
            Global.CDEData.first_P_point = arrUserPoints[0];
        }

/*
//test, DELETE THIS
        length = 3;
        String[] TarrPSubjects = {"Надежность информационных систем", "Веб-программирование", "Корпоративные информационные системы", "Надежность информационных систем", "Веб-программирование", "Корпоративные информационные системы", "Надежность информационных систем", "Веб-программирование", "Корпоративные информационные системы", "Надежность информационных систем", "Надежность информационных систем"};
        String[] TarrDescription = {"Посещение лекций", "Практические занятия", "Выполнение лабораторных работ", "Посещение лекций", "Практические занятия", "Выполнение лабораторных работ", "Посещение лекций", "Практические занятия", "Выполнение лабораторных работ", "Выполнение лабораторных работ", "Личностные качества"};
        String[] TarrUserPoints = {"18", "7", "15", "17", "5", "15", "20", "4", "14", "24", ",87"};
        String[] TarrMaxPoints = {"20", "8", "15", "20", "8", "15", "20", "8", "15", "25", "1,5"};
*/

        if (length == 0){
            textView.setText("Изменений нет");
            textView.setVisibility(View.VISIBLE);
        }else
            setExpandableAdapter(arrPSubjects, arrDescription, arrUserPoints, arrMaxPoints);
            //setExpandableAdapter(TarrPSubjects, TarrDescription, TarrUserPoints, TarrMaxPoints);
    }

    public void setExpandableAdapter(String[] subjects, String[] desc, String[] userpoints, String[] maxpoints){
        int size = subjects.length;

        ArrayList<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        Map<String, String> m;

        for (String item : subjects){
            m = new HashMap<String, String>();
            m.put("subject", item);
            if (!groupData.contains(m))
                groupData.add(m);
        }

        String[] groupFrom = new String[]{"subject"};
        int groupTo[] = new int[] {R.id.textMain1};

        ArrayList<ArrayList<Map<String, String>>> childData = new ArrayList<ArrayList<Map<String, String>>>();
        ArrayList<Map<String, String>> childDataItem;

        int sizeGD = groupData.size();

        for (int i = 0; i < sizeGD; i++){
            m = new HashMap<String, String>();
            m = groupData.get(i);
            String s = m.get("subject").toString();
            System.out.println(m.get("subject") + ": ");

            childDataItem = new ArrayList<Map<String, String>>();
            for (int j = 0; j < size; j++){
                if (subjects[j].equals(s)){
                    m = new HashMap<String, String>();
                    m.put("description", desc[j]);
                    m.put("points", userpoints[j] + " из " + maxpoints[j]);

                    childDataItem.add(m);
                }
            }
            childData.add(childDataItem);
        }

        String[] childFrom = new String[]{"description", "points"};
        int childTo[] = new int[] {android.R.id.text1, android.R.id.text2};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                rootActivity,
                groupData,
                R.layout.exp_list_item,
                groupFrom,
                groupTo,
                childData,
                R.layout.exp_list_subitem,
                childFrom,
                childTo);

        expListView.setAdapter(adapter);
        expListView.setVisibility(View.VISIBLE);

        if (Global.Configuration.expandListView){
            for (int i = 0; i < sizeGD; i++)
                expListView.expandGroup(i);
        }
    }
}
