package com.example.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout_left);
        getSupportActionBar().setElevation(0);

        bottomNav = findViewById(R.id.bottom_navigation);

        //BottomNavigation - begin with HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        //Set onclick --> change fragment
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    //Btm Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.nav_trend:
                    selectedFragment = new StatisticsFragment();
                    break;

                case R.id.nav_qrcode:
                    selectedFragment = new ScanFragment();
                    break;

                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            //Every time the user click on the 4 bottom navigation - HOME, TREND, SCAN, PROFILE
            //The system will clear the fragment stack - so that when backstack, will not repeated
            //No backstack among the 4 bottom navigation, each of them is the 'last page' in HomeActivity
            //If the user press the back button in the HomeActivity, then the app will be closed
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

    public void setActionBarTitle(String text) {
        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.tv_ab_title_left);
        title.setText(text);
    }

    //If the user click back button in one of the 4 base fragment (HomeActivity),
    //System will prompt a dialog box asking whether to exit app or not
    //If yes clear everything and exit
    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStack();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Really Exit?");
            builder.setMessage("Are you sure you want to exit?");
            builder.setNegativeButton(android.R.string.no, null);
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HomeActivity.super.onBackPressed();
                    quit();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.my_red));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.my_red));
        }
    }

    public void quit() {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);
    }
}