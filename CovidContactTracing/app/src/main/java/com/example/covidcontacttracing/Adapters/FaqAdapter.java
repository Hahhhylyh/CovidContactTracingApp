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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidcontacttracing.Models.Faq;
import com.example.covidcontacttracing.R;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqVH> {

    List<Faq> faqList;
    Context ctx;

    public FaqAdapter(List<Faq> faqList, Context context) {
        this.faqList = faqList;
        ctx = context;
    }

    @NonNull
    @Override
    public FaqVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylcer_view_faq, parent, false);
        return new FaqVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqVH holder, int position) {

        Faq faq = faqList.get(position);

        holder.tvQuestion.setText(faq.getQuestion());
        holder.tvAnswer.setText(faq.getAnswer());

        boolean getExpandable = faqList.get(position).getExpandable();
        holder.tvQuestion.setTextColor(getExpandable ? ContextCompat.getColor(ctx, R.color.my_red) : ContextCompat.getColor(ctx, R.color.black));
        holder.ivUpDown.setImageResource(getExpandable ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down);
        holder.ivUpDown.setColorFilter(getExpandable ? ContextCompat.getColor(ctx, R.color.my_red) : ContextCompat.getColor(ctx, R.color.black));
        holder.expandableLayout.setVisibility(getExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public class FaqVH extends RecyclerView.ViewHolder {

        TextView tvQuestion, tvAnswer;
        ImageView ivUpDown;
        RelativeLayout relativeLayout;
        LinearLayout expandableLayout;

        public FaqVH(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.question);
            tvAnswer = itemView.findViewById(R.id.answer);
            ivUpDown = itemView.findViewById(R.id.button);

            relativeLayout = itemView.findViewById(R.id.relative_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Faq faq = faqList.get(getAdapterPosition());
                    faq.setExpandable(!faq.getExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
