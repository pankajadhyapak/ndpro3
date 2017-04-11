package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    public static String ACTION_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";

}

