package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.Rating;
import com.rudik_maksim.cde.adapters.ListViewRatingAdapter;

/**
 * Created by Максим on 16.09.2014.
 */
public class FragmentRating extends Fragment {
    ListView listView;
    TextView textView;
    ImageView imageView;
    Animation animation;
    FrameLayout fLayout;
    Activity rootActivity;

    public FragmentRating(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template_listview, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle(R.string.title_fragmentRating);
        rootActivity.getActionBar().setSubtitle(null);

        listView = (ListView)getView().findViewById(R.id.fragmentTemplate_listView);
        textView = (TextView)getView().findViewById(R.id.fragmentTemplate_textView);
        imageView = (ImageView)getView().findViewById(R.id.fragmentTemplate_imageView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        fLayout = (FrameLayout)getView().findViewById(R.id.fragmentTemplate_frameLayout);

        Global.Application.currentFragmentId = Global.Configuration.NAV_RATING;

        if (!Global.DataLoaded.Rating){
            if (!Global.Loading.Rating){
                Global.Loading.Rating = true;
                new AsyncGetRating().execute();
            }
        }
        else{
            if (!Global.Loading.Rating)
                showRating();
        }
    }

    class AsyncGetRating extends AsyncTask<Void,Void,Void> {
        private Rating r;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            listView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(animation);

            r = new Rating();
        }

        @Override
        protected Void doInBackground(Void... params){
            try{
                r.parse();
            }catch (Exception ex){}
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Global.DataLoaded.Rating = true;
            imageView.setVisibility(View.INVISIBLE);
            imageView.clearAnimation();

            if (Global.Application.currentFragmentId == Global.Configuration.NAV_RATING){
                Animation a = AnimationUtils.loadAnimation(rootActivity, R.anim.up_top);
                fLayout.startAnimation(a);
            }

            Global.Loading.Rating = false;
            showRating();
        }
    }

    void showRating(){
        int length = Global.CDEData.R_FACULTY.size();
        String[] faculty = new String[length];
        String[] course = new String[length];
        String[] position = new String[length];

        Global.CDEData.R_FACULTY.toArray(faculty);
        Global.CDEData.R_COURSE.toArray(course);
        Global.CDEData.R_POSITION.toArray(position);

        ListViewRatingAdapter lViewAdapter = new ListViewRatingAdapter(rootActivity, faculty, course, position);
        listView.setAdapter(lViewAdapter);
        listView.setVisibility(View.VISIBLE);

        if (length == 0){
            textView.setText("Нет данных");
            textView.setVisibility(View.VISIBLE);
        }
    }
}
