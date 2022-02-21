package com.example.cnctracking_2.ui.maintenance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.cnctracking_2.R;
import com.example.cnctracking_2.data.model.MaintenanceModel;
import com.example.cnctracking_2.ui.maintenance.component.MaintenanceAdapter;
import com.example.cnctracking_2.ui.maintenance.component.MaintenanceViewModel;
import com.example.cnctracking_2.ui.report.component.ReportViewModel;
import com.example.cnctracking_2.util.AppUtil;
import com.example.cnctracking_2.util.ConstantUtil;

import java.util.List;

public class MaintenanceFragment extends Fragment {
    private MaintenanceViewModel mViewModel;

    private MaintenanceAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel =
                new ViewModelProvider(this).get(MaintenanceViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        setTitleFrag();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mViewModel.getReportsData(getActivity(), new MaintenanceViewModel.MaintenanceFetchListener() {
            @Override
            public void onRequestComplete(Object response) {
                if (response instanceof MaintenanceModel) {
                    List<MaintenanceModel.MaintenanceDue> newList = AppUtil.Companion.doSortList(((MaintenanceModel) response).getMaintenanceDue());
                    mAdapter = new MaintenanceAdapter(newList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    public void setTitleFrag() {
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            TextView txt = getActivity().findViewById(R.id.toolbar_title);
            txt.setText("Maintenance");
            getActivity().setTitle("");
        } catch (Exception e) {
        }
    }
}