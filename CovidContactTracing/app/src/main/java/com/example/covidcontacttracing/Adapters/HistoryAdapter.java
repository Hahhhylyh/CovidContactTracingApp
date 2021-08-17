package com.example.covidcontacttracing.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidcontacttracing.Models.History;
import com.example.covidcontacttracing.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryVH> {

    List<History> historyList;
    Context ctx;

    public HistoryAdapter(List<History> historyList, Context context) {
        this.historyList = historyList;
        ctx = context;
    }

    @NonNull
    @Override
    public HistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_history, parent, false);
        return new HistoryVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryVH holder, int position) {

        History history = historyList.get(position);

        holder.tvLocation.setText(history.getLocation());
        holder.tvDate.setText(history.getDate());
        holder.tvTime.setText(history.getTime());
        holder.tvRisk.setText(history.getRisk());
        holder.tvAddress.setText(history.getAddress());
        holder.tvTel.setText(history.getTel());
        holder.ivImg.setImageResource(ctx.getResources().getIdentifier(
                history.getImg(), "drawable", ctx.getPackageName()));

        boolean getExpandable = historyList.get(position).getExpandable();
        holder.expandableLayout.setVisibility(getExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class HistoryVH extends RecyclerView.ViewHolder {

        TextView tvLocation, tvDate, tvTime, tvRisk, tvAddress, tvTel;
        ImageView ivImg;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public HistoryVH(@NonNull View itemView) {
            super(itemView);

            tvLocation = itemView.findViewById(R.id.location);
            tvDate = itemView.findViewById(R.id.date);
            tvTime = itemView.findViewById(R.id.time);
            tvRisk = itemView.findViewById(R.id.risk);
            tvAddress = itemView.findViewById(R.id.address);
            tvTel = itemView.findViewById(R.id.tel);
            ivImg = itemView.findViewById(R.id.img);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    History history = historyList.get(getAdapterPosition());
                    history.setExpandable(!history.getExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
