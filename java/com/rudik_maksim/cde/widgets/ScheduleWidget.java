package com.rudik_maksim.cde.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.services.ScheduleWidgetService;

import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class ScheduleWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(Global.Debug.LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        SharedPreferences sp = context.getSharedPreferences(ScheduleWidgetConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        for (int id: appWidgetIds)
            //updateAppWidget(context, appWidgetManager, sp, id);
            updateWidget(context, appWidgetManager, sp, id);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
        Log.d(Global.Debug.LOG_TAG, "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
        Log.d(Global.Debug.LOG_TAG, "onDisabled");
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.schedule_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateWidget(Context context, AppWidgetManager appWidgetManager, SharedPreferences sp, int appWidgetId){
        String widgetText = sp.getString(ScheduleWidgetConfigActivity.WIDGET_TEXT + appWidgetId, null);
        if (widgetText == null) return;

        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.schedule_widget);
        //RemoteViews newView = new RemoteViews(context.getPackageName(), R.layout.schedule_listview_item);

        Intent adapter = new Intent(context, ScheduleWidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        widgetView.setRemoteAdapter(R.id.schedule_widget_listview, adapter);

        appWidgetManager.updateAppWidget(appWidgetId, widgetView);
    }
}


