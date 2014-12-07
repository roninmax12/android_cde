package com.rudik_maksim.cde.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;

/**
 * Created by Максим on 24.09.2014.
 */
public class ListViewChooseYearAdapter extends BaseAdapter {
    Activity context;
    String years[];
    int checkedItem;

    public ListViewChooseYearAdapter(Activity context, String[] years) {
        super();
        this.context = context;
        this.years = years;

        if (Global.CDEData.SELECTED_YEAR == null)
            checkedItem = getCount() - 1;
        else{
            for (int i = 0; i < years.length; i++){
                if (Global.CDEData.SELECTED_YEAR.equals(Global.CDEData.YEARS.get(i))){
                    checkedItem = i;
                    break;
                }
            }
        }
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return years.length;
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
        TextView textView;
        RadioButton radioButton;
        LinearLayout linearLayout;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.dialog_choose_year_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.dialog_choose_year_item_textview);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.dialog_choose_year_item_radio);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.dialog_choose_year_item_linLayout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(years[position]);

        holder.radioButton.setChecked(false);

        if (position == checkedItem)
            holder.radioButton.setChecked(true);
        else
            holder.radioButton.setChecked(false);

        return convertView;
    }
}
