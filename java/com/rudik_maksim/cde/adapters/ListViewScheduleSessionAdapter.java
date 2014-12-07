package com.rudik_maksim.cde.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;

/**
 * Created by Максим on 25.04.14.
 */
public class ListViewScheduleSessionAdapter extends BaseAdapter {
    Activity context;
    String teacher[];
    String subject[];
    String date[];
    String time[];
    String place[];

    public ListViewScheduleSessionAdapter(Activity context, String[] teacher, String[] subject, String[] date, String[] time, String[] place) {
        super();
        this.context = context;
        this.teacher = teacher;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.place = place;
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
        TextView txtTeacher, txtSubject, txtTime, txtDate, txtPlace;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listitem_schedule_session, null);
            holder = new ViewHolder();
            holder.txtTeacher = (TextView) convertView.findViewById(R.id.schedule_session_listview_item_textview_teacher);
            holder.txtSubject = (TextView) convertView.findViewById(R.id.schedule_session_listview_item_textview_subject);
            holder.txtDate    = (TextView) convertView.findViewById(R.id.schedule_session_listview_item_textview_date);
            holder.txtTime    = (TextView) convertView.findViewById(R.id.schedule_session_listview_item_textview_time);
            holder.txtPlace   = (TextView) convertView.findViewById(R.id.schedule_session_listview_item_textview_place);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtSubject.setText(subject[position]);
        holder.txtDate.setText(date[position]);

        String empty_data = "Нет данных";

        if (time[position].length() < 2){
            holder.txtTime.setText(empty_data);
            holder.txtTime.setTextColor(Color.parseColor("#aaaaaa"));
        }else
            holder.txtTime.setText(time[position]);


        if (place[position].length() < 2){
            holder.txtPlace.setText(empty_data);
            holder.txtPlace.setTextColor(Color.parseColor("#aaaaaa"));
        }else
            holder.txtPlace.setText(place[position]);

        if (teacher[position].length() < 2){
            holder.txtTeacher.setText(empty_data);
            holder.txtTeacher.setTextColor(Color.parseColor("#aaaaaa"));
        }else
            holder.txtTeacher.setText(teacher[position]);


        return convertView;
    }
}
