package com.example.covidcontacttracing;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TabFragment3 extends Fragment {

    private Button btnSC;
    private TextView tvDate, tvNew, tvTotal, tvDeath, tvLastUpdate;
    private String str_jsonUpdated;
    private SwipeRefreshLayout srl;
    private ProgressDialog progressDialog;

    public TabFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_fragment3, container, false);

        tvDate = view.findViewById(R.id.tvDate);
        tvNew = view.findViewById(R.id.tvGNew);
        tvTotal = view.findViewById(R.id.tvGTotal);
        tvDeath = view.findViewById(R.id.tvGDeath);
        tvLastUpdate = view.findViewById(R.id.tvLastUpdate);

        RetrieveData();

        // button direct to next page
        btnSC = view.findViewById(R.id.btn_toSearchCountry);
        btnSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchCountryActivity.class);
                startActivity(intent);
            }
        });

        srl = view.findViewById(R.id.srl3);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ShowDialog(getActivity());
                srl.setRefreshing(false);
            }
        });

        return view;
    }

    // get data from api
    private void RetrieveData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        String apiUrl = "https://disease.sh/v3/covid-19/all";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            str_jsonUpdated = response.getString("updated");

                            tvDate.setText(jsonDateToDDMMYY(str_jsonUpdated));
                            tvLastUpdate.setText("Updated " + calculateHowLongAgo(str_jsonUpdated));
                            tvNew.setText(addCommaEvery3Digits(response.getString("todayCases")));
                            tvTotal.setText(addCommaEvery3Digits(response.getString("cases")));
                            tvDeath.setText(addCommaEvery3Digits(response.getString("deaths")));

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

    // convert jsonDate to specific date format
    public String jsonDateToDDMMYY(String jsonDateTime) {
        Long timeInMillis = Long.valueOf(jsonDateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return new SimpleDateFormat( "dd MMM yyyy" ).format( Calendar.getInstance().getTime());
    }

    // to show Updated XXX ago
    public String calculateHowLongAgo(String jsonDate) {
        long milliSecPerMinute = 60 * 1000; //Milliseconds Per Minute
        long milliSecPerHour = milliSecPerMinute * 60; //Milliseconds Per Hour
        long milliSecPerDay = milliSecPerHour * 24; //Milliseconds Per Day
        long milliSecPerMonth = milliSecPerDay * 30; //Milliseconds Per Month
        long milliSecPerYear = milliSecPerDay * 365; //Milliseconds Per Year
        
        Date pastTime = new Date(Long.parseLong(jsonDate));
        Date currentTime = new Date();
        long diff = currentTime.getTime() - pastTime.getTime();

        //Second or Seconds ago calculation
        if (diff < milliSecPerMinute) {
            if (Math.round(diff / 1000) == 1) {
                return String.valueOf(Math.round(diff / 1000)) + " second ago... ";
            } else {
                return String.valueOf(Math.round(diff / 1000) + " seconds ago...");
            }
        }
        //Minute or Minutes ago calculation
        else if (diff < milliSecPerHour) {
            if (Math.round(diff / milliSecPerMinute) == 1) {
                return String.valueOf(Math.round(diff / milliSecPerMinute)) + " minute ago... ";
            } else {
                return String.valueOf(Math.round(diff / milliSecPerMinute)) + " minutes ago... ";
            }
        }
        //Hour or Hours ago calculation
        else if (diff < milliSecPerDay) {
            if (Math.round(diff / milliSecPerHour) == 1) {
                return String.valueOf(Math.round(diff / milliSecPerHour)) + " hour ago... ";
            } else {
                return String.valueOf(Math.round(diff / milliSecPerHour)) + " hours ago... ";
            }
        }
        //Day or Days ago calculation
        else if (diff < milliSecPerMonth) {
            if (Math.round(diff / milliSecPerDay) == 1) {
                return String.valueOf(Math.round(diff / milliSecPerDay)) + " day ago... ";
            } else {
                return String.valueOf(Math.round(diff / milliSecPerDay)) + " days ago... ";
            }
        }
        //Month or Months ago calculation 
        else if (diff < milliSecPerYear) {
            if (Math.round(diff / milliSecPerMonth) == 1) {
                return String.valueOf(Math.round(diff / milliSecPerMonth)) + "  month ago... ";
            } else {
                return String.valueOf(Math.round(diff / milliSecPerMonth)) + "  months ago... ";
            }
        }
        //Year or Years ago calculation 
        else {
            if (Math.round(diff / milliSecPerYear) == 1) {
                return String.valueOf(Math.round(diff / milliSecPerYear)) + " year ago...";
            } else {
                return String.valueOf(Math.round(diff / milliSecPerYear)) + " years ago...";
            }
        }
    }

    // Alternative way to do it: Just realize that
    // * NumberFormat.getInstance().format(Integer.parseInt(your_str)) * can do that
    public String addCommaEvery3Digits(String s) {
        return s.replaceAll("(\\d)(?=(\\d{3})+$)", "$1,");
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