package com.example.tab_layout;

import static java.lang.Math.round;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

        if(num == 0) return "0";

        int i = 0;
        String ret = "";

        while(num != 0) {
            if(i != 0 && i%3 == 0) ret = (",").concat(ret);
            int temp = num%10;
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

}
