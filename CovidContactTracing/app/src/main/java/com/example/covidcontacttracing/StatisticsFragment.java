package com.example.covidcontacttracing;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.covidcontacttracing.Adapters.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class StatisticsFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // use to store tab index
        pref = this.getActivity().getSharedPreferences("tab_info", 0);

        // set the title of the tabs
        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label3));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = view.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        // control tab navigation
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                SharedPreferences.Editor editor = pref.edit();
                // used to remember which tab is the last activated one,
                // before switching to other bottom navigation
                editor.putInt("lastTab", tab.getPosition());
                editor.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        ((HomeActivity) getActivity()).setActionBarTitle("Statistics");

        // resume to previously selected tab
        viewPager.setCurrentItem(pref.getInt("lastTab", 0));
    }
}
