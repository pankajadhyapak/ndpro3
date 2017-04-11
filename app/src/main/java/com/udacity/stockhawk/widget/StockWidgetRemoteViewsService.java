package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_SYMBOL;

public class StockWidgetRemoteViewsService implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;
    int mWidgetId;

    public StockWidgetRemoteViewsService(Context context, Intent intent) {
        mContext = context;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(
                Contract.Quote.URI,
                new String[]{
                        Contract.Quote._ID,
                        Contract.Quote.COLUMN_SYMBOL,
                        Contract.Quote.COLUMN_PRICE,
                        Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
                        Contract.Quote.COLUMN_PERCENTAGE_CHANGE,
                        Contract.Quote.COLUMN_HISTORY},
                null,null,
                null);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);
        if (mCursor.moveToPosition(position)) {

            String symbol = mCursor.getString(mCursor.getColumnIndex(COLUMN_SYMBOL));
            remoteViews.setTextViewText(R.id.symbol, symbol);
            String history = mCursor.getString(Contract.Quote.POSITION_HISTORY);
            remoteViews.setTextViewText(R.id.price, mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_PRICE)));
            remoteViews.setTextViewText(R.id.change, mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE)));
            float rawAbsoluteChange = mCursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);

            if (rawAbsoluteChange > 0) {
                remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }

            Intent intent = new Intent();
            Bundle extras = new Bundle();

            extras.putString(StockWidget.EXTRA_SYMBOL, symbol);
            extras.putString(StockWidget.EXTRA_HISTORY, history);
            intent.putExtras(extras);
            remoteViews.setOnClickFillInIntent(R.id.quote_item_row, intent);
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }}
