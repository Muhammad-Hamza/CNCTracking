package com.example.cnctracking_2.ui.maintenance.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnctracking_2.R;
import com.example.cnctracking_2.data.model.local.MaintenanceModel;
import com.example.cnctracking_2.ui.report.component.ReportAdapter;

import java.util.List;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.Holder> {

    private List<MaintenanceModel> list;

    public MaintenanceAdapter(List<MaintenanceModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_maintenance_new, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (list.get(position).isShowMarkAction()) {
            holder.btnMarkDone.setVisibility(View.VISIBLE);
            holder.ivType.setImageResource(R.mipmap.ic_due_icon);
        } else {
            holder.btnMarkDone.setVisibility(View.GONE);
            holder.ivType.setImageResource(R.mipmap.ic_checked);
        }
//        if (position == (list.size() - 1)) {
//            holder.vBottom.setVisibility(View.VISIBLE);
//        } else {
//            holder.vBottom.setVisibility(View.GONE);
//        }
//        holder.tvId.setText("" + (position + 1));
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvDue.setText(list.get(position).getDueOn());
        holder.tvRemaining.setText(list.get(position).getRemainingDays());
        holder.btnMarkDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setShowMarkAction(false);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView /*tvId,*/ tvTitle, tvDue, tvRemaining;
        private TextView btnMarkDone;
        //        private View vTop, vBottom;
        private ImageView ivType;

        public Holder(@NonNull View itemView) {
            super(itemView);
//            tvId = itemView.findViewById(R.id.tvId);
            ivType = itemView.findViewById(R.id.ivType);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDue = itemView.findViewById(R.id.tvDue);
            tvRemaining = itemView.findViewById(R.id.tvRemaining);
            btnMarkDone = itemView.findViewById(R.id.btnMarkDone);
//            vTop = itemView.findViewById(R.id.vTop);
//            vBottom = itemView.findViewById(R.id.vBottom);
        }
    }
}
