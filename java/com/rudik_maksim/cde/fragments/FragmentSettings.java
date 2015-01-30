package com.rudik_maksim.cde.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.adapters.ListViewSettingsAdapter;

/**
 * Created by Максим on 16.09.2014.
 */
public class FragmentSettings extends Fragment {
    ListView listView;
    Activity rootActivity;

    public FragmentSettings(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_template_listview, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        rootActivity = getActivity();
        rootActivity.getActionBar().setTitle(R.string.title_fragmentSettings);
        rootActivity.getActionBar().setSubtitle(null);

        listView = (ListView)getView().findViewById(R.id.fragmentTemplate_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox chk = (CheckBox) view.findViewById(R.id.settings_row_checkbox);
//DELETE THIS AFTER FIX BUG
//if (position != 1){ // PUSH OFF
                    if (chk.isChecked())
                        chk.setChecked(false);
                    else
                        chk.setChecked(true);
//}
            }
        });

        Global.Application.currentFragmentId = -1;

        getSettings();
    }

    public void getSettings(){
        String item[] = {
                "Текущий семестр",
                "Уведомления",
                "Протокол изменений"};

        String description[] = {
                "Показывать баллы только за текущий семестр",
                "Уведомлять о выставлении новых баллов",
                "Показывать протокол изменений в развёрнутом виде"};

        ListViewSettingsAdapter lviewAdapter = new ListViewSettingsAdapter(getActivity(), item, description);
        listView.setAdapter(lviewAdapter);
    }
}
