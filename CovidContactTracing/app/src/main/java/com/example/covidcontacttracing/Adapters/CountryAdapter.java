package com.example.covidcontacttracing.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.covidcontacttracing.CountryActivity;
import com.example.covidcontacttracing.Models.Country;
import com.example.covidcontacttracing.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.covidcontacttracing.Constants.COUNTRY_ACTIVE;
import static com.example.covidcontacttracing.Constants.COUNTRY_CONFIRMED;
import static com.example.covidcontacttracing.Constants.COUNTRY_DEATH;
import static com.example.covidcontacttracing.Constants.COUNTRY_FLAGURL;
import static com.example.covidcontacttracing.Constants.COUNTRY_NAME;
import static com.example.covidcontacttracing.Constants.COUNTRY_NEW_CONFIRMED;
import static com.example.covidcontacttracing.Constants.COUNTRY_NEW_DEATH;
import static com.example.covidcontacttracing.Constants.COUNTRY_PER_CASE;
import static com.example.covidcontacttracing.Constants.COUNTRY_PER_DEATH;
import static com.example.covidcontacttracing.Constants.COUNTRY_PER_TEST;
import static com.example.covidcontacttracing.Constants.COUNTRY_POPULATION;
import static com.example.covidcontacttracing.Constants.COUNTRY_RECOVERED;
import static com.example.covidcontacttracing.Constants.COUNTRY_TESTS;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryVH> {

    private Context ctx;
    private ArrayList<Country> countryList;
    private String searchText="";
    private SpannableStringBuilder sb;

    public CountryAdapter(Context ctx, ArrayList<Country> countryList) {
        this.ctx = ctx;
        this.countryList = countryList;
    }

    public void filterList(ArrayList<Country> filteredList, String text) {
        countryList = filteredList;
        this.searchText = text;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.recycle_view_country, parent, false);
        return new CountryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryVH holder, int position) {
        Country currentItem = countryList.get(position);
        String countryName = currentItem.getCountry();
        String countryTotal = currentItem.getConfirmed();
        String countryFlag = currentItem.getFlag();
        String countryRank = String.valueOf(position+1);
        int countryTotalInt = Integer.parseInt(countryTotal);
        holder.tv_ranking.setText(countryRank + ".");
        holder.tv_countryTotalCases.setText(NumberFormat.getInstance().format(countryTotalInt));

        if(searchText.length() > 0){
            //color your text here
            int index = countryName.indexOf(searchText);
            sb = new SpannableStringBuilder(countryName);
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(countryName.toLowerCase());
            while(match.find()){
                ForegroundColorSpan fcs = new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.my_red));
                sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
            holder.tv_countryName.setText(sb);

        }else{
            holder.tv_countryName.setText(countryName);
        }

        // use Glide to load image in cache, next time no need retrieve from url
        Glide.with(ctx).load(countryFlag).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.iv_flagImage);

        // give each layout (country) a listener
        // when clicked pass relevant data to next activity
        holder.lin_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Country clickedItem = countryList.get(position);
                Intent intent = new Intent(ctx, CountryActivity.class);

                intent.putExtra(COUNTRY_NAME, clickedItem.getCountry());
                intent.putExtra(COUNTRY_CONFIRMED, clickedItem.getConfirmed());
                intent.putExtra(COUNTRY_ACTIVE, clickedItem.getActive());
                intent.putExtra(COUNTRY_RECOVERED, clickedItem.getRecovered());
                intent.putExtra(COUNTRY_DEATH, clickedItem.getDeath());
                intent.putExtra(COUNTRY_NEW_CONFIRMED, clickedItem.getNewConfirmed());
                intent.putExtra(COUNTRY_NEW_DEATH, clickedItem.getNewDeath());
                intent.putExtra(COUNTRY_TESTS, clickedItem.getTests());
                intent.putExtra(COUNTRY_FLAGURL, clickedItem.getFlag());
                intent.putExtra(COUNTRY_POPULATION, clickedItem.getPopulation());
                intent.putExtra(COUNTRY_PER_CASE, clickedItem.getOneCasePerPeople());
                intent.putExtra(COUNTRY_PER_DEATH, clickedItem.getOneDeathPerPeople());
                intent.putExtra(COUNTRY_PER_TEST, clickedItem.getOneTestPerPeople());

                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList==null || countryList.isEmpty() ? 0 : countryList.size();
    }

    public class CountryVH extends RecyclerView.ViewHolder{
        private TextView tv_countryName, tv_countryTotalCases, tv_ranking;
        ImageView iv_flagImage;
        LinearLayout lin_country;
        public CountryVH(@NonNull View itemView) {
            super(itemView);
            tv_countryName = itemView.findViewById(R.id.tv_countryName);
            tv_countryTotalCases = itemView.findViewById(R.id.tv_totalCases);
            iv_flagImage = itemView.findViewById(R.id.iv_flag);
            tv_ranking = itemView.findViewById(R.id.tv_ranking);
            lin_country = itemView.findViewById(R.id.linear_layout_country);
        }
    }
}
