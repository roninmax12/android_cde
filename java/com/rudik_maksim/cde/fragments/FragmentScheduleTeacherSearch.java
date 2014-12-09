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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.rudik_maksim.cde.ActivityScheduleTeacher;
import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.ScheduleTeacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Created by Максим on 14.11.2014.
 */
public class FragmentScheduleTeacherSearch extends Fragment {
    Activity rootActivity;
    public static ListView listView;
    public static TextView textView;
    ImageView imageView;
    Animation animation;
    FrameLayout fLayout;

    public FragmentScheduleTeacherSearch(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView =  inflater.inflate(R.layout.fragment_template_listview, container, false);

        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle("Расписание преподавателя");
        rootActivity.getActionBar().setSubtitle(null);

        listView = (ListView)inflatedView.findViewById(R.id.fragmentTemplate_listView);
        textView = (TextView)inflatedView.findViewById(R.id.fragmentTemplate_textView);
        imageView = (ImageView)inflatedView.findViewById(R.id.fragmentTemplate_imageView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        fLayout = (FrameLayout)inflatedView.findViewById(R.id.fragmentTemplate_frameLayout);

        final ScheduleTeacher scheduleTeacher = new ScheduleTeacher(rootActivity);
        ArrayList<String> allTeachers = scheduleTeacher.getAllTeachers();
        Collections.sort(allTeachers);

        int size = allTeachers.size();

        final String[] teachers = new String[size];
        allTeachers.toArray(teachers);

        ListViewSearchAdapter adapter = new ListViewSearchAdapter(getActivity(), teachers);
        listView.setAdapter(adapter);

        FragmentScheduleTeacherSearch.listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityScheduleTeacher.teacherId = scheduleTeacher.getTeacherId(teachers[position]);
                if (ActivityScheduleTeacher.teacherId.length() > 5){
                    ActivityScheduleTeacher.teacherName = teachers[position];

                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FragmentScheduleTeacher())
                            .commit();
                }
            }
        });

        return inflatedView;
    }

    public static class ListViewSearchAdapter extends BaseAdapter{
        Activity context;
        String teachers[];

        public ListViewSearchAdapter(Activity context, String[] teachers) {
            super();
            this.context = context;
            this.teachers = teachers;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return teachers.length;
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        private class ViewHolder {
            TextView txtViewTeacherLastName;
            TextView txtViewTeacherFirstName;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            // TODO Auto-generated method stub
            ViewHolder holder;
            LayoutInflater inflater =  context.getLayoutInflater();

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.listitem_search_teacher_row, null);
                holder = new ViewHolder();
                holder.txtViewTeacherLastName = (TextView) convertView.findViewById(R.id.textViewTeacherLastName);
                holder.txtViewTeacherFirstName = (TextView) convertView.findViewById(R.id.textViewTeacherFirstName);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            String[] fio = teachers[position].split(" ");

            holder.txtViewTeacherLastName.setText(fio[0]);

            if (fio.length == 3)
                holder.txtViewTeacherFirstName.setText(fio[1] + " " + fio[2]);
            else
                holder.txtViewTeacherFirstName.setText(fio[1]);

            return convertView;
        }
    }
}
