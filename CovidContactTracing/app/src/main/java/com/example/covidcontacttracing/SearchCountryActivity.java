package com.example.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidcontacttracing.Adapters.CountryAdapter;
import com.example.covidcontacttracing.Models.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchCountryActivity extends AppCompatActivity {

    private RecyclerView rv_country;
    private CountryAdapter countryAdapter;
    private ArrayList<Country> countryList;
    private SwipeRefreshLayout srl;
    private EditText et_search;

    private String str_country, str_confirmed, str_confirmed_new, str_active, str_recovered, str_death,
            str_death_new, str_tests, str_population, str_oneCasePerPeople, str_oneDeathPerPeople, str_oneTestPerPeople;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_country);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.tv_ab_title);
        title.setText("Searching Country");

        initialize();

        retrieveData();

        // initialize swipeRefreshLayout
        srl = findViewById(R.id.srl_country);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ShowDialog();
                srl.setRefreshing(false);
            }
        });

        // detect changes in search bar
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<Country> filteredList = new ArrayList<>();
        for (Country item : countryList) {
            if (item.getCountry().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        countryAdapter.filterList(filteredList, text);
    }

    // get data from api
    private void retrieveData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        String apiURL = "https://disease.sh/v3/covid-19/countries";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // first clear the list to avoid duplicated data from last open
                            countryList.clear();
                            for (int i = 0; i < response.length(); i++){
                                JSONObject countryJSONObject = response.getJSONObject(i);

                                str_country = countryJSONObject.getString("country");
                                str_confirmed = countryJSONObject.getString("cases");
                                str_confirmed_new = countryJSONObject.getString("todayCases");
                                str_active = countryJSONObject.getString("active");
                                str_recovered = countryJSONObject.getString("recovered");
                                str_death = countryJSONObject.getString("deaths");
                                str_death_new = countryJSONObject.getString("todayDeaths");
                                str_tests = countryJSONObject.getString("tests");
                                str_population = countryJSONObject.getString("population");
                                str_oneCasePerPeople = countryJSONObject.getString("oneCasePerPeople");
                                str_oneDeathPerPeople = countryJSONObject.getString("oneDeathPerPeople");
                                str_oneTestPerPeople = countryJSONObject.getString("oneTestPerPeople");
                                JSONObject flagObject = countryJSONObject.getJSONObject("countryInfo");
                                String flagUrl = flagObject.getString("flag");

                                // for each country, create one list view
                                Country country  = new Country(str_country, str_confirmed, str_confirmed_new,
                                        str_active, str_death, str_death_new, str_recovered, str_tests,
                                        flagUrl, str_population, str_oneCasePerPeople, str_oneDeathPerPeople, str_oneTestPerPeople);
                                countryList.add(country);
                                countryAdapter.notifyDataSetChanged();
                            }
                            // sort the order with highest confirmed cases on top
                            Collections.sort(countryList, new Comparator<Country>() {
                                @Override
                                public int compare(Country o1, Country o2) {
                                    if (Integer.parseInt(o1.getConfirmed())>Integer.parseInt(o2.getConfirmed())){
                                        return -1;
                                    } else {
                                        return 1;
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void initialize() {
        srl = findViewById(R.id.srl_country);
        et_search = findViewById(R.id.et_search);

        rv_country = findViewById(R.id.rv_country);
        rv_country.setHasFixedSize(true);
        rv_country.setLayoutManager(new LinearLayoutManager(this));

        countryList = new ArrayList<>();
        countryAdapter = new CountryAdapter(SearchCountryActivity.this, countryList);
        rv_country.setAdapter(countryAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    // when user swipe/scroll down (refresh) the screen/page, show refresh/loading effect
    public void ShowDialog() {
        //setting up progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // when refresh, delay 0.5s only show
        Handler delayToshowProgess = new Handler();
        delayToshowProgess.postDelayed(new Runnable() {
            @Override
            public void run() {
                retrieveData();
                DismissDialog();
            }
        }, 500);
    }

    public void DismissDialog() {
        progressDialog.dismiss();
        Toast.makeText(this, "Latest Data Retrieved", Toast.LENGTH_SHORT).show();
    }
}