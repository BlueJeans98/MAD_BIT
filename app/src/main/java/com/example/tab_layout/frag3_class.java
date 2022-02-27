package com.example.tab_layout;


import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import org.w3c.dom.Text;

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
    private double balance = 1000000.0;
    private int num_BTC = 0;
    private String cost_str = "";
    private String prev_cost_str = "";
    private double cost_lf = 46500.0;
    private double fees = 0.0005;
    private double prev_cost_lf = 46500;

    long seed = System.currentTimeMillis();
    Random rand = new Random(seed);

    private static final String TAG = "frag3_class";

    private CandleStickChart chart;
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

        txt_balance.setText(lfTostr(balance));
        txt_num_BTC.setText(numToString(num_BTC).concat(" BTC"));

        final Button btn_buy = root.findViewById(R.id.btn_buy_init);
        final Button btn_sell = root.findViewById(R.id.btn_sell_init);

        chart = (CandleStickChart) root.findViewById(R.id.LineChart);

        //Y축
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);
        //leftAxis.setGridColor(Color.BLACK);


        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {

                handler.postDelayed(this, 1000);
                load();


                if(!cost_str.equals(prev_cost_str)) {
                    prev_cost_lf = cost_lf;
                    prev_cost_str = cost_str;
                    cost_lf = strTolf(cost_str);
                } else {
                    prev_cost_lf = cost_lf;
                    cost_lf *= (1 + rand.nextGaussian()/20000);
                }

                cur_price.setText(lfTostr(cost_lf));
                leftAxis.setAxisMinimum((float)cost_lf - 50);
                leftAxis.setAxisMaximum((float)cost_lf + 50);

            }
        };
        handler.postDelayed(r, 0000);

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyInit(cost_lf);
            }
        });

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellInit(cost_lf);
            }
        });

        chart = (CandleStickChart) root.findViewById(R.id.LineChart);
        chart.setDrawGridBackground(true);
        chart.setBackgroundColor(Color.WHITE);
        chart.setGridBackgroundColor(Color.WHITE);

// description text
        chart.getDescription().setEnabled(true);
        Description des = chart.getDescription();
        des.setEnabled(true);
        des.setText("BitCoin Price");
        des.setTextSize(15f);
        des.setTextColor(Color.BLACK);



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

        feedMultiple();
// don't forget to refresh the drawing
        chart.invalidate();

        return root;

    }

    private void buyInit(double price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.buy, null, false);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        final TextView buy_cur_price = view.findViewById(R.id.buy_cur_price);
        final EditText num_buy = view.findViewById(R.id.num_buy);
        final Button btn_buy = view.findViewById(R.id.btn_buy);
        final Button btn_cancel_buy = view.findViewById(R.id.btn_cancel_buy);

        buy_cur_price.setText(lfTostr(price));

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String num_str = num_buy.getText().toString();

                try {
                    int num = Integer.parseInt(num_str);
                    if(balance > num*price*(1+fees)) {
                        balance -= num*price*(1+fees);
                        num_BTC += num;
                        txt_balance.setText(lfTostr(balance));
                        txt_num_BTC.setText(numToString(num_BTC).concat(" BTC"));

                        Toast toast = Toast.makeText(getContext(), "매수 성공", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 1700);
                        toast.show();

                        dialog.dismiss();
                    }
                    else {
                        Toast toast = Toast.makeText(getContext(), "매수 실패(잔고 부족)", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 1700);
                        toast.show();
                    }
                } catch(NumberFormatException e) {
                    Toast toast = Toast.makeText(getContext(), "매수 실패(올바른 숫자를 입력하세요.)", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 1700);
                    toast.show();
                }
            }
        });

        // 취소 버튼 클릭
        btn_cancel_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sellInit(double price) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.sell, null, false);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        final TextView sell_cur_price = view.findViewById(R.id.sell_cur_price);
        final EditText num_sell = view.findViewById(R.id.num_sell);
        final Button btn_sell = view.findViewById(R.id.btn_sell);
        final Button btn_cancel_sell = view.findViewById(R.id.btn_cancel_sell);

        sell_cur_price.setText(lfTostr(price));

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String num_str = num_sell.getText().toString();

                try {
                    int num = Integer.parseInt(num_str);
                    if(num_BTC >= num) {
                        balance += num*price*(1-fees);
                        num_BTC -= num;
                        txt_balance.setText(lfTostr(balance));
                        txt_num_BTC.setText(numToString(num_BTC).concat(" BTC"));

                        Toast toast = Toast.makeText(getContext(), "매도 성공", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 1700);
                        toast.show();

                        dialog.dismiss();
                    }
                    else {
                        Toast toast = Toast.makeText(getContext(), "매도 실패(잔고 부족)", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 1700);
                        toast.show();
                    }
                } catch(NumberFormatException e) {
                    Toast toast = Toast.makeText(getContext(), "매도 실패(올바른 숫자를 입력하세요.)", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 1700);
                    toast.show();
                }
            }
        });

        // 취소 버튼 클릭
        btn_cancel_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public double strTolf(String str) {
        String ret = "";

        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == ',') continue;
            if (!Character.isDigit(ch) && ch != '.') break;

            ret = ret.concat(Character.toString(ch));
        }

        return Double.parseDouble(ret);
    }

    public String lfTostr(double lf) {
        String ret = Double.toString(round(lf * 10000) / 10000.0);
        int idx = 0;

        for (; idx < ret.length(); ++idx) {
            if (ret.charAt(idx) == '.') break;
        }
        if (idx == ret.length()) {
            ret = ret.concat(".");
            for (int j = 0; j < 4; ++j)
                ret = ret.concat("0");
        } else {
            for (int j = 0; j < 5 - (ret.length() - idx); ++j)
                ret = ret.concat("0");
        }

        return ret.concat(" $");
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

    private void feedMultiple() {

        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                int count = 0;
                while (true){
                    try {
                        Thread.sleep(1000);
                        addEntry(count,prev_cost_lf,cost_lf);
                        count++;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    private void addEntry(int count,double prev_num, double num) {

        CandleData data = chart.getData();

        if (data == null) {
            data = new CandleData();
            chart.setData(data);
        }

        ICandleDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }
        float high = (float)max(prev_num,num) + 5;
        float low = (float)min(prev_num,num) - 5;

        data.addEntry(new CandleEntry(count,high,low,(float)prev_num,(float)num),0);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        chart.notifyDataSetChanged();

        chart.setVisibleXRangeMaximum(150);
        // this automatically refreshes the chart (calls invalidate())
        chart.moveViewTo(data.getEntryCount(), 50f, YAxis.AxisDependency.LEFT);

    }

    private CandleDataSet createSet() {

        CandleDataSet set = new CandleDataSet(null, "Real-time Candle Data");
        set.setColor(Color.rgb(80, 80, 80));
        set.setShadowColor(Color.rgb(211,211,211));
        set.setShadowWidth(0.8f);
        set.setDecreasingColor(Color.BLUE);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(Color.RED);
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(Color.LTGRAY);
        set.setDrawValues(false);

        return set;
    }





}


