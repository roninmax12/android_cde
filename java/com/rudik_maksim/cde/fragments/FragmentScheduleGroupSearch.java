package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rudik_maksim.cde.ActivityScheduleGroup;
import com.rudik_maksim.cde.ActivityScheduleTeacher;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.ScheduleTeacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Максим on 14.11.2014.
 */
public class FragmentScheduleGroupSearch extends Fragment {
    Activity rootActivity;
    public static ListView listView;
    public static TextView textView;
    ImageView imageView;
    Animation animation;
    FrameLayout fLayout;

    public FragmentScheduleGroupSearch(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView =  inflater.inflate(R.layout.fragment_template_listview, container, false);

        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle("Расписание группы");
        rootActivity.getActionBar().setSubtitle(null);

        listView = (ListView)inflatedView.findViewById(R.id.fragmentTemplate_listView);
        textView = (TextView)inflatedView.findViewById(R.id.fragmentTemplate_textView);
        imageView = (ImageView)inflatedView.findViewById(R.id.fragmentTemplate_imageView);
        animation = AnimationUtils.loadAnimation(rootActivity, R.anim.animation_logo);
        fLayout = (FrameLayout)inflatedView.findViewById(R.id.fragmentTemplate_frameLayout);

        ListViewSearchAdapter adapter = new ListViewSearchAdapter(getActivity(), ActivityScheduleGroup.groupNumber);
        listView.setAdapter(adapter);

        FragmentScheduleGroupSearch.listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkGroupNumber()){
                    if (view != null) {
                        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    ActivityScheduleGroup.searchView.onActionViewCollapsed();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FragmentScheduleGroup())
                            .commit();
                }else{
                    Toast.makeText(getActivity(), "Некорректный номер группы", Toast.LENGTH_SHORT).show();
                }
            }

            public boolean checkGroupNumber(){
                Pattern p = Pattern.compile("^[и]?[1-6]{1}[0-9]{3}$");
                Matcher m = p.matcher(ActivityScheduleGroup.groupNumber);
                return m.matches();
            }
        });

        return inflatedView;
    }

    public static class ListViewSearchAdapter extends BaseAdapter{
        Activity context;
        String group;

        public ListViewSearchAdapter(Activity context, String group) {
            super();
            this.context = context;
            this.group = group;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return 1;
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
            TextView txtViewGroup;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            // TODO Auto-generated method stub
            ViewHolder holder;
            LayoutInflater inflater =  context.getLayoutInflater();

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.listitem_search_group_row, null);
                holder = new ViewHolder();
                holder.txtViewGroup= (TextView) convertView.findViewById(R.id.textViewGroup);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtViewGroup.setText("Группа " + group);

            return convertView;
        }
    }
}
