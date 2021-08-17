package com.example.covidcontacttracing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidcontacttracing.Adapters.HistoryAdapter;
import com.example.covidcontacttracing.Models.History;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class ScanFragment extends Fragment {

    RecyclerView recyclerView;
    List<History> historyList;
    private DatabaseHelper myDb;
    SharedPreferences pref;
    String uid, location, date, time, risk, address, tel, img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        myDb = new DatabaseHelper(view.getContext());
        pref = this.getActivity().getSharedPreferences("user_details", 0);
        uid = pref.getString("uid", null);

        recyclerView = view.findViewById(R.id.rcv_history);
        initializeData();
        setRecyclerView();

        FloatingActionButton fab = view.findViewById(R.id.fab_scan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());

                // suggested (default) setting of the scanner library
                intentIntegrator.setPrompt("For flash use volume up key");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                intentIntegrator.forSupportFragment(ScanFragment.this).initiateScan();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // Check condition
        if (intentResult.getContents() != null) {
            // when result content is not null - add to database & initialize alert dialog
            // split the return messages - one for title; one for content
            String messages = addDataAfterScan(intentResult.getContents(), uid);
            String[] message = messages.split(Pattern.quote("|"));

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(message[0]);
            builder.setMessage(message[1]);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    // change button text color
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                            ContextCompat.getColor(requireActivity(), R.color.my_red));
                }
            });
            alert.show();
        }
        else {
            Toast.makeText(getActivity(), "OOPS... You did not scan anything", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecyclerView() {
        HistoryAdapter historyAdapter = new HistoryAdapter(historyList, getActivity());
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setHasFixedSize(false);
    }

    // populate the history list from database
    private void initializeData() {
        historyList = new ArrayList<>();

        //get History data based on user id store in session
        Cursor res = myDb.getHistoryData(uid);
        while(res.moveToNext()){
            location = res.getString(res.getColumnIndex("location"));
            date = res.getString(res.getColumnIndex("date"));
            time = res.getString(res.getColumnIndex("time"));
            risk = res.getString(res.getColumnIndex("risk"));
            address = res.getString(res.getColumnIndex("address"));
            tel = res.getString(res.getColumnIndex("tel"));
            img = res.getString(res.getColumnIndex("img"));

            // populate the history record
            historyList.add(new History(location, date, time, risk, address, tel, img));
        }
    }

    // handle scanned information - either success (update database) or failed (wrong qr code)
    public String addDataAfterScan(String contents, String uid) {
        String[] content = contents.split(Pattern.quote("|"));
        // if the QR code is not the one we created with specific format, then toast user
        if (content.length != 5) {
            return "Error|This is not a valid NoCovid QR code.";    // separate with a '|' to indicate title and content
        }
        // generate a timestamp when scan successfully
        Date now = new Date();
        SimpleDateFormat dateForm = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat timeForm = new SimpleDateFormat("hh:mm a");
        String output = content[0] + "|" +
                "\nDatetime: " + dateForm.format(now) + " " + timeForm.format(now).replace("am", "AM").replace("pm","PM") +
                "\nRisk: " + content[1] + "\nTel: " + content[3] +
                "\nAddress: " + content[2];

        // update database
        boolean success = myDb.addHistory(content[0], dateForm.format(now), timeForm.format(now).replace("am", "AM").replace("pm","PM"), content[1], content[2], content[3], content[4], Integer.parseInt(uid));
        if(success)
        {
            // if successfully scan qr code, then refresh page
            initializeData();
            setRecyclerView();
            return output;
        }
        else
        {
            return "This is not a valid NoCovid QR code.";
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        ((HomeActivity) getActivity()).setActionBarTitle("Scan & History");
    }
}
