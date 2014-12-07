package com.rudik_maksim.cde.adapters;

/**
 * Created by Максим on 18.03.14.
 */
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rudik_maksim.cde.R;

public class ListViewPointsAdapter extends BaseAdapter
{
    Activity context;
    String subject[];
    String point[];
    String control[];

    public ListViewPointsAdapter(Activity context, String[] subject, String[] point, String[] control) {
        super();
        this.context = context;
        this.subject = subject;
        this.point = point;
        this.control = control;
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
        TextView txtViewSubject;
        TextView txtViewPoint;
        TextView txtViewControl;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listitem_points_row, null);
            holder = new ViewHolder();
            holder.txtViewSubject = (TextView) convertView.findViewById(R.id.textViewSubject);
            holder.txtViewPoint = (TextView) convertView.findViewById(R.id.textViewPoint);
            holder.txtViewControl = (TextView) convertView.findViewById(R.id.textViewControl);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewSubject.setText(subject[position]);
        holder.txtViewPoint.setText(point[position]);
        holder.txtViewControl.setText(control[position]);

        String thisPoint = holder.txtViewPoint.getText().toString();
        if (!"".equals(thisPoint)){
            thisPoint = thisPoint.replace(',','.');
            if ((int)Float.parseFloat(thisPoint) >= 60){
                //12,73,0
                holder.txtViewPoint.setTextColor(Color.rgb(25, 100, 25));
                holder.txtViewSubject.setTextColor(Color.rgb(25, 100, 25));
            }else{
                holder.txtViewPoint.setTextColor(Color.rgb(0,0,0));
                holder.txtViewSubject.setTextColor(Color.rgb(0, 0, 0));
            }
        }else{
            holder.txtViewPoint.setTextColor(Color.rgb(0,0,0));
            holder.txtViewSubject.setTextColor(Color.rgb(0, 0, 0));
        }

        return convertView;
    }
}