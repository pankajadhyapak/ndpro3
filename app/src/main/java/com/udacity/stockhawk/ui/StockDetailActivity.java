package com.udacity.stockhawk.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

import static com.udacity.stockhawk.widget.StockWidget.EXTRA_HISTORY;
import static com.udacity.stockhawk.widget.StockWidget.EXTRA_SYMBOL;

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        Bundle extras = getIntent().getExtras();
        Timber.e("Inside details");
        String symbol = "", historyData = "";

        if (extras != null) {
            historyData = extras.getString(EXTRA_HISTORY);
            symbol = extras.getString(EXTRA_SYMBOL);
            displayChart(historyData, symbol);
        } else {
            finish();
        }
    }

    private void displayChart(String historyData, String stockSymbol) {

        LineChart chart = (LineChart) findViewById(R.id.historyChart);
        List<Entry> entries = new ArrayList<>();

        final ArrayList<String> stockDates = new ArrayList<>();

        String[] stockHistory = historyData.split("\n");

        int timeCount = 0;

        for (String historyDataItem : stockHistory) {
            String[] data = historyDataItem.split(", ");
            entries.add(new Entry(timeCount++, Float.parseFloat(data[1])));
            stockDates.add(data[0]);
        }

        Context context = getApplicationContext();
        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.legend_chart_stock_price));
        LineData lineData = new LineData(dataSet);

        Legend legend = chart.getLegend();
        legend.setTextColor(getColorFromResource(context, R.color.chart_legend_color));
        dataSet.setColor(getColorFromResource(context, R.color.chart_dataset_color));
        dataSet.setCircleColor(getColorFromResource(context, R.color.chart_dataset_circlecolor));
        dataSet.setLineWidth(1f);
        dataSet.setFillAlpha(10);
        dataSet.setFillColor(getColorFromResource(context, R.color.chart_dataset_fillcolor));
        dataSet.setDrawCircles(true);

        chart.setData(lineData);

        Description description = new Description();
        description.setText(stockSymbol + " - " + getString(R.string.label_chart_stock_price));
        description.setTextColor(getColorFromResource(context, R.color.chart_description_color));
        description.setTextSize(10f);
        chart.setDescription(description);

        XAxis XAxis = chart.getXAxis();
        XAxis.setValueFormatter(new IAxisValueFormatter() {

            Calendar calendar = Calendar.getInstance();


            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                calendar.setTimeInMillis((long) Float.parseFloat(stockDates.get((int) Math.floor(value))));
                int mYear = calendar.get(Calendar.YEAR) - 2000;
                int mMonth = calendar.get(Calendar.MONTH) + 1;
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                return mDay + "/" + mMonth + "/" + mYear;
            }
        });

        XAxis.setTextColor(getColorFromResource(context, R.color.chart_xaxis_color));

        YAxis YAxisL = chart.getAxis(YAxis.AxisDependency.LEFT);
        YAxis YAxisR = chart.getAxis(YAxis.AxisDependency.RIGHT);
        YAxisL.setTextColor(getColorFromResource(context, R.color.chart_yaxis_left_color));
        YAxisR.setTextColor(getColorFromResource(context, R.color.chart_yaxisright_color));
        chart.invalidate();
    }

    private int getColorFromResource(Context context, int colorResourceId) {
        return ContextCompat.getColor(context, colorResourceId);
    }
}