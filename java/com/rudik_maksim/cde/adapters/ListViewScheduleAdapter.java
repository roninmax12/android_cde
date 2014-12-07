package com.rudik_maksim.cde.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rudik_maksim.cde.R;

/**
 * Created by Максим on 18.09.2014.
 */
public class ListViewScheduleAdapter extends BaseAdapter {
    Activity context;
    String subject[];
    String place[];
    String time[];
    String teacher[];

    public ListViewScheduleAdapter(Activity context, String[] subject, String[] place, String[] time, String[] teacher) {
        super();
        this.context = context;
        this.teacher = teacher;
        this.subject = subject;
        this.place = place;
        this.time = time;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return subject.length;
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

        holder.txtTeacher.setText(teacher[position]);
        holder.txtSubject.setText(subject[position]);
        holder.txtPlace.setText(place[position]);
        holder.txtTime.setText(time[position]);

        convertView.setBackgroundColor(Color.parseColor("#f5f5f5"));

        return convertView;
    }
}
