package com.example.covidcontacttracing;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Calendar;

public class TabFragment1 extends Fragment {

    private Context context;
    private TextView tvConfirmed, tvConfirmedNew, tvActive, tvActiveNew, tvRecovered, tvRecoveredNew, tvDeath, tvDeathNew, tvDateTime;
    private String strC, strCN, strA, strR, strRN, strD, strDN, strDateTime;
    private int active;
    private PieChart pieChart;
    private SwipeRefreshLayout srl;
    private ProgressDialog progressDialog;
    
    public TabFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_fragment1, container, false);

        tvDateTime = view.findViewById(R.id.tv_date_time);
        tvActive = view.findViewById(R.id.tv_active_num);
        tvActiveNew = view.findViewById(R.id.tv_active_plus);
        tvConfirmed = view.findViewById(R.id.tv_confirmed_num);
        tvConfirmedNew = view.findViewById(R.id.tv_confirmed_plus);
        tvRecovered = view.findViewById(R.id.tv_recovered_num);
        tvRecoveredNew = view.findViewById(R.id.tv_recovered_plus);
        tvDeath = view.findViewById(R.id.tv_death_num);
        tvDeathNew = view.findViewById(R.id.tv_death_plus);

        pieChart = view.findViewById(R.id.piechart_my);

        // initialize swipeRefreshLayout
        srl = view.findViewById(R.id.srl1);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // when refresh, show loading dialog
                ShowDialog(getActivity());
                srl.setRefreshing(false);
            }
        });

        RetrieveData();

        return view;
    }

    // get data from api
    private void RetrieveData() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        String apiUrl = "https://disease.sh/v3/covid-19/countries/malaysia";

        pieChart.clearChart();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // parse json datetime to string in standard
                            strDateTime = jsonDateToDate(response.getString("updated"));
                            strC = response.getString("cases");
                            strCN = response.getString("todayCases");
                            strD = response.getString("deaths");
                            strDN = response.getString("todayDeaths");
                            strR = response.getString("recovered");
                            strRN = response.getString("todayRecovered");
                            strA = response.getString("active");

                            tvDateTime.setText("Last Update:\n" + strDateTime);
                            tvConfirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(strC)));
                            tvConfirmedNew.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(strCN)) + " new cases");
                            tvRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(strR)));
                            tvRecoveredNew.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(strRN)) + " new cases");
                            tvDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(strD)));
                            tvDeathNew.setText("+ " + NumberFormat.getInstance().format(Integer.parseInt(strDN)) + " new cases");
                            tvActive.setText(NumberFormat.getInstance().format(Integer.parseInt(strA)));
                            active = Integer.parseInt(strCN) - (Integer.parseInt(strRN) + Integer.parseInt(strDN));
                            if (active >= 0)
                                tvActiveNew.setText("+ " + active + " new cases");
                            else
                                tvActiveNew.setText("- " + Math.abs(active) + " cases");

                            pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(strA), ContextCompat.getColor(context, R.color.yellow_active)));
                            pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(strR), ContextCompat.getColor(context, R.color.green_recovered)));
                            pieChart.addPieSlice(new PieModel("Death", Integer.parseInt(strD), ContextCompat.getColor(context, R.color.grey_death)));
                            pieChart.addPieSlice(new PieModel("Total Cases", Integer.parseInt(strC), ContextCompat.getColor(context, R.color.red_confirmed)));

                            pieChart.startAnimation();

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    // convert the jsonDate to Date format
    public String jsonDateToDate(String jsonDate) {
        Long timeInMillis = Long.valueOf(jsonDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar.getTime().toString();
    }

    // when user swipe/scroll down (refresh) the screen/page, show refresh/loading effect
    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // when refresh, delay 0.5s only show
        Handler delayToshowProgess = new Handler();
        delayToshowProgess.postDelayed(new Runnable() {
            @Override
            public void run() {
                RetrieveData();
                DismissDialog();
            }
        }, 500);
    }

    public void DismissDialog() {
        progressDialog.dismiss();
    }
}