package com.example.tab_layout;


import static java.lang.Math.round;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import android.widget.Toast;
import androidx.fragment.app.Fragment;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class frag3_class extends Fragment {

    private TextView cur_price;
    private TextView txt_balance;
    private TextView txt_num_BTC;
    public static final String BPI_ENDPOINT = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final int balance = 1000000;
    private final int num_BTC = 0;
    private String cost_str = "";
    private String prev_cost_str = "";
    private double cost_lf = 0;

    long seed = System.currentTimeMillis();
    Random rand = new Random(seed);

    private static final String TAG = "frag3_class";

    private LineChart chart;
    private Thread thread;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment3, null);
        cur_price = root.findViewById(R.id.cur_price);
        txt_balance = root.findViewById(R.id.balance);
        txt_num_BTC = root.findViewById(R.id.num_BTC);

        txt_balance.setText(numToString(balance).concat(" $"));
        txt_num_BTC.setText(numToString(num_BTC).concat(" BTC"));


        Handler handler =new Handler();
        final Runnable r = new Runnable() {
            public void run() {

                handler.postDelayed(this, 1000);
                load();

                if(!cost_str.equals(prev_cost_str)) {
                    prev_cost_str = cost_str;
                    cost_lf = strTolf(cost_str);
                } else cost_lf *= (1 + rand.nextGaussian()/200000);

                cur_price.setText(lfTostr(cost_lf));

            }
        };
        handler.postDelayed(r, 0000);

        chart = (LineChart) root.findViewById(R.id.LineChart);

        chart.setDrawGridBackground(true);
        chart.setBackgroundColor(Color.BLACK);
        chart.setGridBackgroundColor(Color.BLACK);

// description text
        chart.getDescription().setEnabled(true);
        Description des = chart.getDescription();
        des.setEnabled(true);
        des.setText("Real-Time DATA");
        des.setTextSize(15f);
        des.setTextColor(Color.WHITE);


// touch gestures (false-비활성화)
        chart.setTouchEnabled(false);

// scaling and dragging (false-비활성화)
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);

//auto scale
        chart.setAutoScaleMinMaxEnabled(true);

// if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

//X축
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawAxisLine(false);

        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setDrawGridLines(false);

//Legend
        Legend l = chart.getLegend();
        l.setEnabled(true);
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setTextSize(12f);
        l.setTextColor(Color.WHITE);

//Y축
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(getResources().getColor(R.color.colorgrid));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(getResources().getColor(R.color.colorgrid));
        leftAxis.setAxisMinimum(46500);
        leftAxis.setAxisMaximum(47500);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);


        feedMultiple();
// don't forget to refresh the drawing
        chart.invalidate();

        return root;

    }

public double strTolf(String str) {
        String ret = "";

        for(int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if(ch == ',') continue;
            if(!Character.isDigit(ch) && ch != '.') break;

            ret = ret.concat(Character.toString(ch));
        }

        return Double.parseDouble(ret);
    }

    public String lfTostr(double lf) {
        return Double.toString(round(lf*10000)/10000.0).concat("$");
    }

    public String numToString(int num) {

        if (num == 0) return "0";

        int i = 0;
        String ret = "";

        while (num != 0) {
            if (i != 0 && i % 3 == 0) ret = (",").concat(ret);
            int temp = num % 10;
            num /= 10;
            ret = Integer.toString(temp).concat(ret);
            i++;
        }

        return ret;
    }

    private void load() {
        Request request = new Request.Builder()
                .url(BPI_ENDPOINT)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /*Toast.makeText(MainActivity.this, "Error during BPI loading : "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String body = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseBpiResponse(body);
                    }
                });
            }
        });

    }

    private void parseBpiResponse(String body) {
        try {
            StringBuilder builder = new StringBuilder();

            JSONObject jsonObject = new JSONObject(body);

            JSONObject bpiObject = jsonObject.getJSONObject("bpi");
            JSONObject usdObject = bpiObject.getJSONObject("USD");
            builder.append(usdObject.getString("rate")).append("$");

            cost_str = builder.toString();

        } catch (Exception e) {

        }
    }


    public static Handler mHandler;

   private void feedMultiple() {

        if (thread != null){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                boolean a = true;
                while (true){

                    try {
                        Thread.sleep(1000);
                        addEntry(cost_lf);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    private void addEntry(double num) {

        LineData data = chart.getData();

        if (data == null) {
            data = new LineData();
            chart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }



        data.addEntry(new Entry((float)set.getEntryCount(), (float)num), 0);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        chart.setVisibleXRangeMaximum(150);
        // this automatically refreshes the chart (calls invalidate())
        chart.moveViewTo(data.getEntryCount(), 50f, YAxis.AxisDependency.LEFT);

    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Real-time Line Data");
        set.setLineWidth(1f);
        set.setDrawValues(false);
        set.setValueTextColor(getResources().getColor(R.color.white));
        set.setColor(getResources().getColor(R.color.white));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawCircles(false);
        set.setHighLightColor(Color.rgb(190, 190, 190));

        return set;
    }


}
