package com.example.covidcontacttracing;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private CardView cvSymptoms, cvPrevention, cvSOP, cvFAQ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cvSymptoms = view.findViewById(R.id.cv_symptoms);
        cvPrevention = view.findViewById(R.id.cv_prevention);
        cvSOP = view.findViewById(R.id.cv_sop);
        cvFAQ = view.findViewById(R.id.cv_qa);

        cvSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SymptomsActivity.class);
                startActivity(intent);
            }
        });

        cvPrevention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PreventionActivity.class);
                startActivity(intent);
            }
        });

        cvSOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SopActivity.class);
                startActivity(intent);
            }
        });

        cvFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FaqActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        ((HomeActivity) getActivity()).setActionBarTitle("Home");
    }
}
