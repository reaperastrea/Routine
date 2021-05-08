package org.coeg.routine.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.coeg.routine.R;
import org.coeg.routine.activities.AddRoutineActivity;
import org.coeg.routine.backend.Days;
import org.coeg.routine.backend.History;
import org.coeg.routine.backend.Operations;
import org.coeg.routine.backend.Routine;

import java.util.LinkedList;
import java.util.Objects;

public class RecentRoutinesAdapter extends RecyclerView.Adapter<RecentRoutinesAdapter.RoutinesViewHolder> {
    private LinkedList<Routine> mRoutineList;
    private LinkedList<History> mHistoryList;
    private LayoutInflater mInflater;
    private Context mContext;
    //private int count = 0;

    public RecentRoutinesAdapter(Context context, LinkedList<Routine> routineList, LinkedList<History> historyList){
        this.mContext = context;
        this.mRoutineList = routineList;
        this.mHistoryList = historyList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecentRoutinesAdapter.RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_layout , parent , false);
        return new RecentRoutinesAdapter.RoutinesViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentRoutinesAdapter.RoutinesViewHolder holder, int position) {
        Routine mRoutine = mRoutineList.get(position);
        History mHistory = mHistoryList.get(position);
        holder.tvName.setText(mRoutine.getName());
        holder.tvTime.setText(Objects.requireNonNull(mRoutine.getTimeAsString()).substring(0,5));
        holder.tvStatus.setText(String.valueOf(Operations.countLateMinutes(mRoutine, mHistory)));
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }

    public class RoutinesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvStatus;
        private TextView tvName;
        private TextView tvTime;
        private RecentRoutinesAdapter mAdapter;

        public RoutinesViewHolder(@NonNull View itemView, RecentRoutinesAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            tvStatus = (TextView) itemView.findViewById(R.id.txt_routineStatus);
            tvName = (TextView) itemView.findViewById(R.id.txt_routineName);
            tvTime = (TextView) itemView.findViewById(R.id.txt_routineTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
