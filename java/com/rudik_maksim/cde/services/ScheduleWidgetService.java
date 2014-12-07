package com.rudik_maksim.cde.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.rudik_maksim.cde.widgets.ScheduleWidgetFactory;

/**
 * Created by Максим on 30.09.2014.
 */
public class ScheduleWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScheduleWidgetFactory(getApplicationContext(), intent);
    }
}
