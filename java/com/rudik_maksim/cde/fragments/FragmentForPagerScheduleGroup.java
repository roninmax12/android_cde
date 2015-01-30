package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rudik_maksim.cde.ActivityScheduleGroup;
import com.rudik_maksim.cde.ActivityScheduleTeacher;
import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.Schedule;
import com.rudik_maksim.cde.ScheduleTeacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Максим on 12.11.2014.
 */
public class FragmentForPagerScheduleGroup extends Fragment {
    private static final String ARG_POSITION = "position";
    ImageView imageView;
    TextView textView;
    Animation animation;
    Activity rootActivity;
    LinearLayout linRootLayout;
    LinearLayout linSubLayout;
    LayoutInflater layInflater;

    private int position;

    public static FragmentForPagerScheduleGroup newInstance(int position) {
        FragmentForPagerScheduleGroup f = new FragmentForPagerScheduleGroup();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        Global.Configuration.isScheduleGroupFragment = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_schedule, container, false);

        rootActivity = getActivity();

        imageView = (ImageView)inflatedView.findViewById(R.id.fragmentSchedule_imageView);
        textView = (TextView)inflatedView.findViewById(R.id.fragmentSchedule_textView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        linSubLayout = (LinearLayout)inflatedView.findViewById(R.id.fragmentSchedule_linearLayoutContainer);
        linRootLayout = (LinearLayout)inflatedView.findViewById(R.id.fragment_schedule_linearLayout);
        layInflater = rootActivity.getLayoutInflater();

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

        if (!Global.DataLoaded.ScheduleGroup){
            if (!Global.Loading.ScheduleGroup){
                Global.Loading.ScheduleGroup = true;
                new AsyncGetScheduleGroup().execute();
            }
        }
        else{
            if (!Global.Loading.ScheduleGroup){
                showSchedule(position);
            }
        }

        //INSERT CUSTOM CODE HERE
    }

    @Override
    public void onStop(){
        super.onStop();
        Global.Configuration.isScheduleGroupFragment = false;
    }



    class AsyncGetScheduleGroup extends AsyncTask<Void,Void,Void> {
        private Schedule schedule;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            imageView.setVisibility(View.VISIBLE);
            imageView.startAnimation(animation);
            schedule = new Schedule();
        }

        @Override
        protected Void doInBackground(Void... params){
            try{
                schedule.parseGroup(ActivityScheduleGroup.groupNumber);
            }catch (Exception ex){}

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            imageView.setVisibility(View.INVISIBLE);
            imageView.clearAnimation();
            Global.DataLoaded.ScheduleGroup = true;

            if(Global.Application.currentFragmentId == Global.Configuration.NAV_SCHEDULE){
                Animation a = AnimationUtils.loadAnimation(rootActivity, R.anim.up_top);
                linRootLayout.startAnimation(a);
            }

            Global.Loading.ScheduleGroup = false;
            showSchedule(0);
        }
    }

    void showSchedule(int position){
        if (position == 0){
            //even week
            prepareSchedule(true);
        }else{
            //oven week
            prepareSchedule(false);
        }

        if (Global.CDEData.WeekNumber != 0) {
            String[] days = {"воскресенье", "понедельник", "вторник", "среда", "четверг", "пятница", "суббота"};
            Calendar calendar = Calendar.getInstance();
            rootActivity.getActionBar().setSubtitle(Global.CDEData.WeekNumber + " неделя, " + days[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        }
    }

    public void prepareSchedule(boolean isEvenWeek){
        int len = 0;
        try{
            len = Global.CDEData.SG_DATA.get(0).size();
        }catch (Exception ex){}

        String[] day_week, week_type, time, room, place, title_subject, person_title, status;

        if (len == 0){
            Global.DataLoaded.ScheduleGroup = false;
            textView.setVisibility(View.VISIBLE);
            return;
        }

        day_week = new String[len];       // 0 - Monday, 1 - Tuesday ...
        week_type = new String[len];      // 0 - all weeks, 1 - even week, 2 - oven week
        time = new String[len];           // ex. "9:30-10:50"
        room = new String[len];
        place = new String[len];          // "Крон" or "Грив" or ""
        title_subject = new String[len];
        person_title = new String[len];
        status = new String[len];         // ex. "Лаб"

        Global.CDEData.SG_DATA.get(0).toArray(day_week);
        Global.CDEData.SG_DATA.get(1).toArray(week_type);
        Global.CDEData.SG_DATA.get(2).toArray(time);
        Global.CDEData.SG_DATA.get(3).toArray(room);
        Global.CDEData.SG_DATA.get(4).toArray(place);
        Global.CDEData.SG_DATA.get(5).toArray(title_subject);
        Global.CDEData.SG_DATA.get(6).toArray(person_title);
        Global.CDEData.SG_DATA.get(7).toArray(status);

        //int days;
        String[] rightWeekValue = new String[2];

        if (isEvenWeek) {
            //days = getParityDaysCount(0, Global.CDEData.S_DATA.get(0), Global.CDEData.S_DATA.get(1));
            rightWeekValue[0] = "0"; rightWeekValue[1] = "1";
        }
        else {
            //days = getParityDaysCount(1, Global.CDEData.S_DATA.get(0), Global.CDEData.S_DATA.get(1));
            rightWeekValue[0] = "0"; rightWeekValue[1] = "2";
        }

        linSubLayout.removeAllViews();

        int currentDayIndex = -1;
        int addedDayIndex = -1;
        boolean needToAddView = false;
        View item = layInflater.inflate(R.layout.schedule_item, linSubLayout, false);

        boolean existsSchedule = false;
        for (int j = 0; j < title_subject.length; j++){
            if (week_type[j].equals(rightWeekValue[0]) || week_type[j].equals(rightWeekValue[1])){
                existsSchedule = true;
                currentDayIndex = Integer.parseInt(day_week[j]);

                if (currentDayIndex != addedDayIndex){
                    if (needToAddView){
                        linSubLayout.addView(item);
                    }

                    addedDayIndex = currentDayIndex;
                    item = layInflater.inflate(R.layout.schedule_item, linSubLayout, false);

                    TextView tvDay = (TextView)item.findViewById(R.id.schedule_item_textview_day);
                    tvDay.setText(getDayForIndex(currentDayIndex));
                    needToAddView = true;
                }

                if (addedDayIndex != -1){

                    LinearLayout ll = (LinearLayout)item.findViewById(R.id.schedule_item_linLayoutContent);

                    View itemItem = layInflater.inflate(R.layout.schedule_listview_item, ll, false);
                    final TextView tv_subject = (TextView)itemItem.findViewById(R.id.schedule_listview_item_textview_subject);
                    final TextView tv_place = (TextView)itemItem.findViewById(R.id.schedule_listview_item_textview_place);
                    final TextView tv_time = (TextView)itemItem.findViewById(R.id.schedule_listview_item_textview_time_and_week);
                    final TextView tv_teacher = (TextView)itemItem.findViewById(R.id.schedule_listview_item_textview_teacher);

                    tv_subject.setText(title_subject[j] + " (" + status[j] + ")");
                    String placeInfo = room[j] + " " + getRightFormatPlace(place[j]);

                    if (placeInfo.length() < 2){
                        placeInfo = "Нет данных";
                        tv_place.setTextColor(Color.parseColor("#aaaaaa"));
                    }

                    tv_place.setText(placeInfo);

                    tv_time.setText(getRightFormatTime(time[j]));

                    String teacher = person_title[j];
                    if (teacher.length() < 2){
                        teacher = "Нет данных";
                        tv_teacher.setTextColor(Color.parseColor("#aaaaaa"));
                    }
                    tv_teacher.setText(teacher);

                    itemItem.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (!Global.Application.isFragmentScheduleActionExists){
                                String onlyPlace = tv_place.getText().toString();
                                String teacherName = tv_teacher.getText().toString();
                                int firstSpaceIndex = onlyPlace.indexOf(' ');
                                String fisrtPart = onlyPlace.substring(0, firstSpaceIndex);
                                if (fisrtPart.matches(".*\\d.*"))
                                    onlyPlace = onlyPlace.substring(firstSpaceIndex + 1);

                                FragmentTransaction ft = getFragmentManager().beginTransaction();

                                try{
                                    DialogFragment newFragment = DialogScheduleActionFragment.newInstance(onlyPlace, teacherName, "");
                                    newFragment.show(ft, "dialog_schedule_action");
                                }catch (Exception ex){}
                            }

                            return false;
                        }
                    });

                    ll.addView(itemItem);

                }
            }

            if (j == title_subject.length - 1){
                linSubLayout.addView(item);
            }
        }

        if (!existsSchedule){
            textView.setVisibility(View.VISIBLE);
        }

        if (Global.Configuration.isFirstOpen){
            if (Global.CDEData.ParityOfWeek.contains("Нечетная")) {
                FragmentSchedule.pager.setCurrentItem(1);
            } else {
                FragmentSchedule.pager.setCurrentItem(0);
            }
            Global.Configuration.isFirstOpen = false;
        }
    }

    /*
        Parity:
            0 - Even
            1 - Oven
     */
    int getParityDaysCount(int Parity, ArrayList<String> days, ArrayList<String> weeks){
        int daysCount = 0;
        HashMap<String, String> map = new HashMap<String, String>();

        for (int i = 0; i < weeks.size(); i++){
            switch (Parity){
                case 0:
                    if (weeks.get(i).equals("0") || weeks.get(i).equals("1")){
                        map.put(days.get(i).toString(), "");
                    }
                    break;
                case 1:
                    if (weeks.get(i).equals("0") || weeks.get(i).equals("2")){
                        map.put(days.get(i).toString(), "");
                    }
                    break;
            }
        }

        daysCount = map.size();

        return daysCount;
    }

    String getDayForIndex(int index){
        String day = "";

        switch (index){
            case 0: day = "Понедельник"; break;
            case 1: day = "Вторник"; break;
            case 2: day = "Среда"; break;
            case 3: day = "Четверг"; break;
            case 4: day = "Пятница"; break;
            case 5: day = "Суббота"; break;
        }

        return day;
    }

    String getRightFormatTime(String time){
        //9:30-10:50 => 09:30\n10:50
        //8:00-9:20 = > 08:00\n09:20
        String formattedTime = "";

        if (time.equals("День"))
            return time;

        String[] parts = time.split("-");

        for (int i = 0; i < parts.length; i++){
            if (parts[i].length() < 5)
                parts[i] = "0" + parts[i];
        }

        formattedTime = parts[0] + "\n" + parts[1];

        return formattedTime;
    }

    String getRightFormatPlace(String place){
        String formattedPlace = "";

        if (place.equals("Крон")) formattedPlace = "Кронверкский пр., д. 49А";
        if (place.equals("Грив")) formattedPlace = "Пер. Гривцова, д. 14-16";

        return formattedPlace;
    }
}
