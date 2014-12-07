package com.rudik_maksim.cde.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;

/**
 * Created by Максим on 11.04.14.
 */
public class ListViewSettingsAdapter extends BaseAdapter {
    Activity context;
    String item[];
    String description[];

    public ListViewSettingsAdapter(Activity context, String item[], String description[]) {
        super();
        this.context = context;
        this.item = item;
        this.description = description;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return item.length;
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
        LinearLayout linearLayout;
        TextView txtViewItem;
        TextView txtViewDescription;
        CheckBox chkBox;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listitem_settings_row, null);
            holder = new ViewHolder();
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.settings_row_linearLayout);
            holder.txtViewItem = (TextView) convertView.findViewById(R.id.textViewSettingsItem);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.textViewSettingsDescription);
            holder.chkBox = (CheckBox) convertView.findViewById(R.id.settings_row_checkbox);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewItem.setText(item[position]);
        holder.txtViewDescription.setText(description[position]);

        if (position == 0){
            holder.chkBox.setChecked(Global.Configuration.show_data_on_cur_sem);
        }
        if (position == 1){
            holder.chkBox.setChecked(Global.Configuration.push_enabled);
        }
        if (position == 2){
            holder.chkBox.setChecked(Global.Configuration.expandListView);
        }

        holder.chkBox.setOnCheckedChangeListener(myCheckChangList);
        holder.chkBox.setTag(position);

        return convertView;
    }

    // обработчик для чекбоксов
    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {

            switch ((Integer)buttonView.getTag()){
                case 0:
                    Global.Configuration.show_data_on_cur_sem = isChecked; break;
                case 1:
                    Global.Configuration.push_enabled = isChecked; break;
                case 2:
                    Global.Configuration.expandListView = isChecked; break;
            }
        }
    };
}
