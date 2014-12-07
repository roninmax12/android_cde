package com.rudik_maksim.cde.fragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.adapters.ListViewChooseYearAdapter;

/**
 * Created by Максим on 24.09.2014.
 */
public class DialogChooseYearFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Dialog dialog = super.getDialog();
        dialog.setTitle("Выберите год обучения");

        // Set title divider color
        int titleId = getResources().getIdentifier("title", "id", "android");
        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");

        View titleDivider = dialog.findViewById(titleDividerId);
        View titleView = dialog.findViewById(titleId);

        if (titleDivider != null)
            titleDivider.setBackgroundColor(getResources().getColor(R.color.main_blue_color));

        if (titleView != null)
            titleView.setBackgroundColor(getResources().getColor(R.color.main_blue_color));

        View v = inflater.inflate(R.layout.dialog_choose_year, container, false);
        ListView lv = (ListView )v.findViewById(R.id.dialog_choose_year_listview);
        final String[] years = new String[Global.CDEData.YEARS.size()];

        for (int i = 0; i<years.length; i++){
            years[i] = Global.CDEData.YEARS.get(i);
        }

        ListViewChooseYearAdapter adapter = new ListViewChooseYearAdapter(getActivity(), years);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Global.CDEData.SELECTED_YEAR = Global.CDEData.YEARS.get(position);
                Global.Configuration.currentYearChosen = Global.CDEData.YEARS.get(position);
                Global.Fragments.fragment_year.dismiss();
                Global.DataLoaded.Points = false;
                Global.CDEData.clearPointsData();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FragmentPoints())
                        .commit();
            }
        });

        return v;
    }
}
