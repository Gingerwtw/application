package com.xuexiang.application.activity;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.xuexiang.application.R;
import com.xuexiang.application.widget.DemoBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PulseResultActivity extends DemoBase implements OnChartValueSelectedListener {

    private LineChart chart;

    private String disease;
    private int select;
    private List<Float> pulse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_result);

        initResult();
        initChart();

        TextView tvDisease = findViewById(R.id.pulse_result);
        tvDisease.setText(disease);

        ImageButton btn_back = findViewById(R.id.pulse_result_toolbar_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initResult(){
        Bundle bundle=getIntent().getExtras();

        String respondJson = bundle.getString("pulse_result");
        select = bundle.getInt("select");

        try {
            JSONObject obj = new JSONObject(respondJson);
            disease = obj.getString("disease");

            Log.d("pulse result",disease);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        pulse = getRawTxtFileContent(this, select, R.raw.pulse2);
    }

    private void initChart(){

        {   // // Chart Style // //
            chart = findViewById(R.id.chart1);

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(Collections.max(pulse));
            yAxis.setAxisMinimum(Collections.min(pulse));
        }

        // add data
        setData();

        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }



    private void setData() {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < pulse.size(); i++) {

//            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, pulse.get(i), getResources().getDrawable(R.drawable.star)));
        }

        LineDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "pulse");

            set1.setDrawIcons(false);

            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(1f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(false);
//            set1.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return chart.getAxisLeft().getAxisMinimum();
//                }
//            });

            // set color of filled area
//            if (Utils.getSDKInt() >= 18) {
//                // drawables only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
//                set1.setFillDrawable(drawable);
//            } else {
//                set1.setFillColor(Color.BLACK);
//            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }

    public static List<Float> getRawTxtFileContent(Context context, int select, int rawResId) {
        String pulse = "";
        int count;
        List<Float> list = new ArrayList<>();

        try {
            InputStream inputStream = context.getResources().openRawResource(rawResId);
            if (inputStream != null){
                InputStreamReader reader = new InputStreamReader(inputStream,"GB2312");
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;
                count = 0;
                while ((line = bufferedReader.readLine()) != null){
                    if (count == select){
                        pulse = line;
                        break;
                    }else{
                        count += 1;
                    }

                }
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] strs2 = pulse.split("\t");
        for (String s : strs2){
            list.add(Float.parseFloat(s)) ;
        }

        return list;
    }

    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "LineChartActivity1");
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}