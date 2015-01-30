package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rudik_maksim.cde.ActivitySubjectDetails;
import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.SubjectDetails;
import com.rudik_maksim.cde.adapters.ListViewSubjectDetailsAdapter;

/**
 * Created by Максим on 28.09.2014.
 */
public class FragmentSubjectDetails extends Fragment {
    ImageView ivAnimation;
    TextView tvSSMessage;
    ListView lview;
    Animation animation;
    FrameLayout fLayout;
    Activity rootActivity;

    int fragmentID = 100;

    public FragmentSubjectDetails(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_subject_details, container, false);

        rootActivity = getActivity();

        ivAnimation = (ImageView)inflatedView.findViewById(R.id.sub_det_imageView);
        lview = (ListView)inflatedView.findViewById(R.id.sub_det_listView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        tvSSMessage = (TextView)inflatedView.findViewById(R.id.textViewSSMessage);
        fLayout = (FrameLayout)inflatedView.findViewById(R.id.fragmentSubjectDetails_frameLayout);

        Global.Application.currentFragmentId = fragmentID;

        new AsyncGetSubjectDetailsInfo().execute();

        return inflatedView;
    }

    class AsyncGetSubjectDetailsInfo extends AsyncTask<Void,Void,Void> {
        SubjectDetails sd = new SubjectDetails();
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            ivAnimation.setVisibility(View.VISIBLE);
            ivAnimation.startAnimation(animation);
            Global.CDEData.clearSubjectDetailsData();
        }

        @Override
        protected Void doInBackground(Void... params){
            try{
                sd.parse(ActivitySubjectDetails.syu_id);
            }catch (Exception ex){}

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            ivAnimation.setVisibility(View.INVISIBLE);
            ivAnimation.clearAnimation();

            if (Global.Application.currentFragmentId == fragmentID){
                Animation a = AnimationUtils.loadAnimation(rootActivity, R.anim.up_top);
                fLayout.startAnimation(a);
            }

            int length = Global.CDEData.SD_TITLE.size();
            String[] number = new String[length];
            String[] title  = new String[length];
            String[] rate   = new String[length];
            String[] date   = new String[length];

            Global.CDEData.SD_NUMBER.toArray(number);
            Global.CDEData.SD_TITLE.toArray(title);
            Global.CDEData.SD_RATE.toArray(rate);
            Global.CDEData.SD_DATE.toArray(date);

            ListViewSubjectDetailsAdapter lvAdapter = new ListViewSubjectDetailsAdapter(getActivity(), number, title, rate, date);
            lview.setAdapter(lvAdapter);

            if (number.length == 0){
                tvSSMessage.setText("Нет данных");
                tvSSMessage.setVisibility(View.VISIBLE);
            }else
                rootActivity.getActionBar().setSubtitle("Итоговый рейтинг: " + Global.CDEData.CURRENT_RATE);
        }
    }
}
