package com.rudik_maksim.cde.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rudik_maksim.cde.R;

import java.util.ArrayList;

/**
 * Created by Максим on 18.09.2014.
 */
public class ListViewScheduleAdapterNew extends BaseAdapter {
    Activity context;
    ArrayList<ArrayList<String>> arrayList;
    int length;

    String[] day_week, week_type, time, room, place, title_subject, person_title, status;

    public ListViewScheduleAdapterNew(Activity context, ArrayList<ArrayList<String>> arrayList) {
        super();
        this.context = context;
        this.arrayList = arrayList;

        length = this.arrayList.get(0).size();

        day_week = new String[length];
        week_type = new String[length];
        time = new String[length];
        room = new String[length];
        place = new String[length];
        title_subject = new String[length];
        person_title = new String[length];
        status = new String[length];

        this.arrayList.get(0).toArray(day_week);
        this.arrayList.get(1).toArray(week_type);
        this.arrayList.get(2).toArray(time);
        this.arrayList.get(3).toArray(room);
        this.arrayList.get(4).toArray(place);
        this.arrayList.get(5).toArray(title_subject);
        this.arrayList.get(6).toArray(person_title);
        this.arrayList.get(7).toArray(status);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return day_week.length;
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
        TextView txtTeacher, txtSubject, txtPlace, txtTime;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.schedule_listview_item, null);
            holder = new ViewHolder();
            holder.txtTeacher = (TextView) convertView.findViewById(R.id.schedule_listview_item_textview_teacher);
            holder.txtSubject = (TextView) convertView.findViewById(R.id.schedule_listview_item_textview_subject);
            holder.txtPlace = (TextView) convertView.findViewById(R.id.schedule_listview_item_textview_place);
            holder.txtTime = (TextView) convertView.findViewById(R.id.schedule_listview_item_textview_time_and_week);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.txtTeacher.setText(teacher[position]);
        //holder.txtSubject.setText(subject[position]);
        //holder.txtPlace.setText(place[position]);
        //holder.txtTime.setText(time[position]);

        convertView.setBackgroundColor(Color.parseColor("#f5f5f5"));

        return convertView;
    }
}
