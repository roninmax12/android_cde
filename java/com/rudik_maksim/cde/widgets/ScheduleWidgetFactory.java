package com.rudik_maksim.cde.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.rudik_maksim.cde.R;

import java.util.ArrayList;

/**
 * Created by Максим on 30.09.2014.
 */
public class ScheduleWidgetFactory implements RemoteViewsService.RemoteViewsFactory{

    ArrayList<String> data;
    Context context;
    //SimpleDateFormat sdf;
    int widgetID;

    public ScheduleWidgetFactory(Context ctx, Intent intent) {
        context = ctx;
        //sdf = new SimpleDateFormat("HH:mm:ss");
        //widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
         //       AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        data = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(context.getPackageName(),
                R.layout.listitem_points_row);
        //rView.setTextViewText(R.id.tvItemText, data.get(position));
        rView.setTextViewText(R.id.textViewSubject, data.get(position));
        rView.setTextViewText(R.id.textViewControl, data.get(position));
        rView.setTextViewText(R.id.textViewPoint, "98");
        return rView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        data.clear();
        //data.add(sdf.format(new Date(System.currentTimeMillis())));
        //data.add(String.valueOf(hashCode()));
        //data.add(String.valueOf(widgetID));
        for (int i = 3; i < 15; i++) {
            data.add("Item " + i);
        }
    }

    @Override
    public void onDestroy() {

    }

}
