package com.example.cnctracking_2.ui.report;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class ReportFragment extends Fragment {

    private ReportViewModel mViewModel;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        setTitleFrag();
        recyclerView = root.findViewById(R.id.recyclerView);
        int extraIndex = -1;
        if (getArguments() != null) {
            if (getArguments().containsKey(ConstantUtil.PREF_EXTRA_BUNDLE_1)) {
                extraIndex = getArguments().getInt(ConstantUtil.PREF_EXTRA_BUNDLE_1);
            }
        }
        mViewModel.getReportsData(getActivity(), extraIndex, new ReportViewModel.ReportFetchListener() {
            @Override
            public void onRequestComplete(ReportResponse response) {
                if (!TextUtils.isEmpty(response.getMessage())) {
                    Toast.makeText(requireContext(), "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    initAdapter(response);
                }
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initAdapter(ReportResponse response) {
        response.getAllData();
//        ReportAdapter reportAdapter = new ReportAdapter(response.getWeeklyReports());
        ReportAdapter reportAdapter = new ReportAdapter(response.getAllData());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(reportAdapter);
    }

    public void setTitleFrag() {
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            TextView txt = getActivity().findViewById(R.id.toolbar_title);
            txt.setText("Report");
            getActivity().setTitle("");
        } catch (Exception e) {
        }
    }

}