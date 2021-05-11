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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;

public class RecentRoutinesAdapter extends RecyclerView.Adapter<RecentRoutinesAdapter.RoutinesViewHolder> {
    private LinkedList<Routine> mRoutineList;
    private LinkedList<History> mHistoryList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int latetime;
    private final int lateLimit = 20;
    //private int count = 0;

    //formatter
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    //private String tes;

    public RecentRoutinesAdapter(Context context, LinkedList<Routine> routineList, LinkedList<History> historyList){
        this.mContext = context;
        this.mRoutineList = routineList;
        this.mHistoryList = historyList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecentRoutinesAdapter.RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recent_layout , parent , false);
        return new RecentRoutinesAdapter.RoutinesViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentRoutinesAdapter.RoutinesViewHolder holder, int position) {
        Routine mRoutine = mRoutineList.get(position);
        History mHistory = mHistoryList.get(position);
        holder.tvName.setText(mRoutine.getName());
        //holder.tvName.setText(dateFormatter.format(Calendar.getInstance().getTime()));
        holder.tvTime.setText(Objects.requireNonNull(mRoutine.getTimeAsString()).substring(0,5));
        //holder.tvTime.setText(mHistory.getDateAsString());

        try {
            latetime = countLate(mRoutine, mHistory);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //holder.tvStatus.setText(String.valueOf(latetime));

        if (latetime < 1){
            holder.tvStatus.setText("On Time");
        }else if (latetime >= 1 && latetime < lateLimit){
            StringBuilder builder = new StringBuilder();
            builder.append(latetime);
            builder.append(" Minutes Late");
            holder.tvStatus.setText(builder.toString());
        }else if (latetime >= lateLimit){
            holder.tvStatus.setText("Missed");
        }

        //holder.tvName.setText(String.valueOf(mHistory.getId()));
        //holder.tvTime.setText(String.valueOf(mHistory.getRoutineId()));
        //tes = "Date : "+(mHistory.getTimeAsString())+"Time : "+(mHistory.getDateAsString());
        //holder.tvStatus.setText(tes);
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

    private Integer countLate(Routine routine, History history) throws ParseException {
        Date lateTime = formatter.parse(history.getTimeAsString());
        Date routineTime = formatter.parse(routine.getTimeAsString());
        Long late = lateTime.getTime() - routineTime.getTime();
        Integer mins = (int)(late - (late % 60)) / 60;

        return mins/1000;
    }
}
