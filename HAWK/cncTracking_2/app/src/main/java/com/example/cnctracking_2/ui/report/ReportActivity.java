package com.example.cnctracking_2.ui.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cnctracking_2.R;
import com.example.cnctracking_2.data.model.ReportResponse;
import com.example.cnctracking_2.ui.report.component.ReportAdapter;
import com.example.cnctracking_2.ui.report.component.ReportViewModel;
import com.example.cnctracking_2.util.ConstantUtil;

public class ReportActivity extends AppCompatActivity {

    private ReportViewModel mViewModel;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        setContentView(R.layout.fragment_report);
        setTitleFrag();
        recyclerView = findViewById(R.id.recyclerView);
        int extraIndex = -1;
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            if (bundle.containsKey(ConstantUtil.PREF_EXTRA_BUNDLE_1)) {
                extraIndex = bundle.getInt(ConstantUtil.PREF_EXTRA_BUNDLE_1);
            }
        }
        mViewModel.getReportsData(this, extraIndex, new ReportViewModel.ReportFetchListener() {
            @Override
            public void onRequestComplete(ReportResponse response) {
                if (!TextUtils.isEmpty(response.getMessage())) {
                    Toast.makeText(ReportActivity.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    initAdapter(response);
                }
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        });
    }


    private void initAdapter(ReportResponse response) {
        response.getAllData();
//        ReportAdapter reportAdapter = new ReportAdapter(response.getWeeklyReports());
        ReportAdapter reportAdapter = new ReportAdapter(response.getAllData());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.setAdapter(reportAdapter);
    }

    public void setTitleFrag() {
//        try {
////            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TextView txt = findViewById(R.id.toolbar_title);
        txt.setText("Reports");
        setTitle("");
//        } catch (Exception e) {
//        }
    }

}