package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockDetailActivity;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class StockWidget extends AppWidgetProvider {

    public static String EXTRA_SYMBOL  = "SYMBOL";
    public static String EXTRA_HISTORY = "HISTORY";
    public static String ACTION_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews widgetViews = new RemoteViews(context.getPackageName(), R.layout.stock_widget);

        Intent svcIntent = new Intent(context, StockWidgetService.class);
        svcIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        widgetViews.setRemoteAdapter(R.id.stock_list, svcIntent);

        Intent clickAppIntent = new Intent(context, MainActivity.class);
        Intent clickIntent = new Intent(context, StockDetailActivity.class);
        Timber.e("inside click event");
        PendingIntent appPI = PendingIntent.getActivity(context, 0, clickAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        widgetViews.setOnClickPendingIntent(R.id.widgetTitle,appPI);
        widgetViews.setPendingIntentTemplate(R.id.stock_list, clickPI);

        appWidgetManager.updateAppWidget(appWidgetId, widgetViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        int[] appWidgetIDs;
        if ( intent.getAction().equals(ACTION_UPDATE) ) {
            // Debugging
            Log.i("WIDGETUPDATE","UPDATED!");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetIDs = appWidgetManager.getAppWidgetIds( new ComponentName( context, getClass() ) );
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIDs, R.id.stock_list);
        }
    }

}

