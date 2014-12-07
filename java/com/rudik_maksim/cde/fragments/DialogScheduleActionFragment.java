package com.rudik_maksim.cde.fragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.rudik_maksim.cde.ActivityScheduleTeacher;
import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;

import java.util.Locale;

/**
 * Created by Максим on 15.10.2014.
 */
public class DialogScheduleActionFragment extends DialogFragment {

    static DialogScheduleActionFragment newInstance(String place, String teacher){
        DialogScheduleActionFragment dialog = new DialogScheduleActionFragment();
        Bundle args = new Bundle();
        args.putString("address", place);
        args.putString("teacher", teacher);
        dialog.setArguments(args);
        Global.Application.isFragmentScheduleActionExists = true;

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Dialog dialog = super.getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final String address = getArguments().getString("address");
        final String teacherName = getArguments().getString("teacher");

        View v = inflater.inflate(R.layout.dialog_schedule_action, container, false);

        ListView listView = (ListView) v.findViewById(R.id.dialog_schedule_action_listview);

        String item[] = {"Показать на карте", "Расписание преподавателя"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.dialog_schedule_action_item, R.id.dialog_schedule_action_item_textview_action, item);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position){
                    case 0:
                        if (address.length() < 7 || address.equals("Нет данных")){
                            Toast.makeText(getActivity(), "Невозможно показать на карте", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        String uri = String.format(Locale.getDefault(), "geo:0,0?q=" + address);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                        dismiss();
                        break;

                    case 1:
                        if (teacherName.length() < 7 || teacherName.equals("Нет данных")){
                            Toast.makeText(getActivity(), "Невозможно показать на карте", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        intent = new Intent(getActivity(), ActivityScheduleTeacher.class);
                        intent.putExtra("teacherName", teacherName);
                        startActivity(intent);
                        dismiss();
                        break;
                }
            }
        });

        return v;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Global.Application.isFragmentScheduleActionExists = false;
    }
}
