package com.rudik_maksim.cde.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rudik_maksim.cde.R;

/**
 * Created by Максим on 13.06.14.
 */
public class ListViewSubjectDetailsAdapter extends BaseAdapter {
    Activity context;
    String number[];
    String title[];
    String rate[];
    String date[];

    public ListViewSubjectDetailsAdapter(Activity context, String[] number, String[] title, String[] rate, String[] date) {
        super();
        this.context = context;
        this.number = number;
        this.title = title;
        this.rate = rate;
        this.date = date;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
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
        TextView txtViewNumber;
        TextView txtViewTitle;
        TextView txtViewRate;
        TextView txtViewDate;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listitem_subject_details_row, null);
            holder = new ViewHolder();
            holder.txtViewNumber = (TextView) convertView.findViewById(R.id.tv_sd_Number);
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.tv_sd_Title);
            holder.txtViewRate = (TextView) convertView.findViewById(R.id.tv_sd_rate);
            holder.txtViewDate = (TextView) convertView.findViewById(R.id.tv_sd_date);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewNumber.setText(number[position]);
        holder.txtViewTitle.setText(title[position]);
        holder.txtViewRate.setText(rate[position]);
        holder.txtViewDate.setText(date[position]);

        return convertView;
    }
}
