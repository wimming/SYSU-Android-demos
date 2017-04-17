package com.ym.lab4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class Lab5Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = "Widget";
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.lab5_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.lab5_widget);
        rv.setOnClickPendingIntent(R.id.appwidget_image, pi);
        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.lab5_widget);
        if (intent.getAction().equals("com.ym.lab4.receiver1")) {
            int index = intent.getIntExtra("index", -1);
            rv.setTextViewText(R.id.appwidget_text, FruitsModel.getInstance().data.get(index).name);
            rv.setImageViewResource(R.id.appwidget_image, FruitsModel.getInstance().data.get(index).res);
        }
        else if (intent.getAction().equals("com.ym.lab4.receiver2")) {
            String content = intent.getStringExtra("content");
            rv.setTextViewText(R.id.appwidget_text, content);
            rv.setImageViewResource(R.id.appwidget_image, R.mipmap.dynamic);
        }

        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, Lab5Widget.class), rv);

    }
}

