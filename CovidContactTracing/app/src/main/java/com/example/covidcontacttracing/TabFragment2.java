package com.example.covidcontacttracing;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabFragment2 extends Fragment {

    private CircleImageView slg, prk, ns, kl, kdh, png, lbn, jhr, klt, sbh, mlk, ptj, prl, trg, srw, phg;
    private TextView tvDate, tvCases, tvSlg, tvPrk, tvNs, tvKl, tvKdh, tvPng, tvLbn, tvJhr, tvKlt, tvSbh, tvMlk, tvPtj, tvPrl, tvTrg, tvSrw, tvPhg;
    private SwipeRefreshLayout srl;
    private ProgressDialog progressDialog;
    
    public TabFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_fragment2, container, false);

        slg = view.findViewById(R.id.slg);
        prk = view.findViewById(R.id.prk);
        ns = view.findViewById(R.id.ns);
        kl = view.findViewById(R.id.kl);
        kdh = view.findViewById(R.id.kdh);
        png = view.findViewById(R.id.png);
        lbn = view.findViewById(R.id.lbn);
        jhr = view.findViewById(R.id.jhr);
        klt = view.findViewById(R.id.klt);
        sbh = view.findViewById(R.id.sbh);
        mlk = view.findViewById(R.id.mlk);
        ptj = view.findViewById(R.id.ptj);
        prl = view.findViewById(R.id.prl);
        trg = view.findViewById(R.id.trg);
        srw = view.findViewById(R.id.srw);
        phg = view.findViewById(R.id.phg);

        tvDate = view.findViewById(R.id.tb2_date);
        tvCases = view.findViewById(R.id.tb2_cases);
        tvSlg = view.findViewById(R.id.tb2_selangor);
        tvPrk = view.findViewById(R.id.tb2_perak);
        tvNs = view.findViewById(R.id.tb2_ns);
        tvKl = view.findViewById(R.id.tb2_kl);
        tvKdh = view.findViewById(R.id.tb2_kedah);
        tvPng = view.findViewById(R.id.tb2_penang);
        tvLbn = view.findViewById(R.id.tb2_labuan);
        tvJhr = view.findViewById(R.id.tb2_johor);
        tvKlt = view.findViewById(R.id.tb2_kelantan);
        tvSbh = view.findViewById(R.id.tb2_sabah);
        tvMlk = view.findViewById(R.id.tb2_melaka);
        tvPtj = view.findViewById(R.id.tb2_putrajaya);
        tvPrl = view.findViewById(R.id.tb2_perlis);
        tvTrg = view.findViewById(R.id.tb2_terengganu);
        tvSrw = view.findViewById(R.id.tb2_sarawak);
        tvPhg = view.findViewById(R.id.tb2_pahang);

        srl = view.findViewById(R.id.srl2);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ShowDialog(getActivity());
                srl.setRefreshing(false);
            }
        });

        Glide.with(this).load(R.drawable.flag_selangor).into(slg);
        Glide.with(this).load(R.drawable.flag_perak).into(prk);
        Glide.with(this).load(R.drawable.flag_ns).into(ns);
        Glide.with(this).load(R.drawable.flag_kl).into(kl);
        Glide.with(this).load(R.drawable.flag_kedah).into(kdh);
        Glide.with(this).load(R.drawable.flag_penang).into(png);
        Glide.with(this).load(R.drawable.flag_labuan).into(lbn);
        Glide.with(this).load(R.drawable.flag_johor).into(jhr);
        Glide.with(this).load(R.drawable.flag_kelantan).into(klt);
        Glide.with(this).load(R.drawable.flag_sabah).into(sbh);
        Glide.with(this).load(R.drawable.flag_melaka).into(mlk);
        Glide.with(this).load(R.drawable.flag_putrajaya).into(ptj);
        Glide.with(this).load(R.drawable.flag_perlis).into(prl);
        Glide.with(this).load(R.drawable.flag_terengganu).into(trg);
        Glide.with(this).load(R.drawable.flag_sarawak).into(srw);
        Glide.with(this).load(R.drawable.flag_pahang).into(phg);

        RetrieveData();

        return view;
    }

    // get data from json file in github
    private void RetrieveData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        String apiUrl = "https://raw.githubusercontent.com/akutaktau/covid-19-data/main/covid-19-states-daily-cases.json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // get the last one (latest) - because the json file contains time series row
                            JSONObject latest = response.getJSONObject(response.length() - 1);
                            tvDate.setText(latest.getString("date"));
                            tvSlg.setText(latest.getString("selangor"));
                            tvPrk.setText(latest.getString("perak"));
                            tvNs.setText(latest.getString("negeri-sembilan"));
                            tvKl.setText(latest.getString("wp-kuala-lumpur"));
                            tvKdh.setText(latest.getString("kedah"));
                            tvPng.setText(latest.getString("pulau-pinang"));
                            tvLbn.setText(latest.getString("wp-labuan"));
                            tvJhr.setText(latest.getString("johor"));
                            tvKlt.setText(latest.getString("kelantan"));
                            tvSbh.setText(latest.getString("sabah"));
                            tvMlk.setText(latest.getString("melaka"));
                            tvPtj.setText(latest.getString("wp-putrajaya"));
                            tvPrl.setText(latest.getString("perlis"));
                            tvTrg.setText(latest.getString("terengganu"));
                            tvSrw.setText(latest.getString("sarawak"));
                            tvPhg.setText(latest.getString("pahang"));

                            // sum all the cases to have total cases
                            int totalCases = 0;
                            for(int i = 1; i < latest.names().length(); i++){
                                totalCases += latest.getInt(latest.names().getString(i));
                            }
                            tvCases.setText("" + totalCases);

                        } catch (JSONException e) {
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
        requestQueue.add(jsonArrayRequest);
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