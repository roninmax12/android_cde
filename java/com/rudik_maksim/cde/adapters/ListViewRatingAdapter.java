package com.rudik_maksim.cde.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rudik_maksim.cde.R;

/**
 * Created by Максим on 12.04.14.
 */
public class ListViewRatingAdapter extends BaseAdapter {
    Activity context;
    String faculty[];
    String course[];
    String position[];

    public ListViewRatingAdapter(Activity context, String[] faculty, String[] course, String[] position) {
        super();
        this.context = context;
        this.faculty = faculty;
        this.course = course;
        this.position = position;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return position.length;
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
        TextView txtViewFaculty;
        TextView txtViewCourse;
        TextView txtViewPosition;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listitem_rating_row, null);
            holder = new ViewHolder();
            holder.txtViewFaculty = (TextView) convertView.findViewById(R.id.textViewFaculty);
            holder.txtViewCourse = (TextView) convertView.findViewById(R.id.textViewCourse);
            holder.txtViewPosition = (TextView) convertView.findViewById(R.id.textViewPosition);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewFaculty.setText(faculty[position]);
        holder.txtViewCourse.setText(course[position]);
        holder.txtViewPosition.setText(this.position[position]);

        return convertView;
    }
}
