package org.coeg.routine.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.coeg.routine.R;
import org.coeg.routine.activities.AddRoutineActivity;
import org.coeg.routine.backend.Routine;

import java.util.LinkedList;

public class RoutineListAdapter extends RecyclerView.Adapter<RoutineListAdapter.RoutinesViewHolder> {
    private LinkedList<Routine> mRoutineList;
    private LayoutInflater mInflater;
    private Context mContext;

    public RoutineListAdapter(Context context, LinkedList<Routine> routineList){
        this.mContext = context;
        this.mRoutineList = routineList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RoutinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_layout , parent , false);
        return new RoutinesViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutinesViewHolder holder, int position) {
        Routine mRoutine = mRoutineList.get(position);
        holder.tvName.setText(mRoutine.getName());
        holder.tvDay.setText(mRoutine.getTime().toString());
        holder.tvTime.setText(mRoutine.getTime().toString());
    }

    @Override
    public int getItemCount() {
        return mRoutineList.size();
    }

    public class RoutinesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDay;
        private TextView tvName;
        private TextView tvTime;
        private RoutineListAdapter mAdapter;
        private int mPosisi;
        private Routine mRoutine;

        public RoutinesViewHolder(@NonNull View itemView, RoutineListAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            tvDay = (TextView) itemView.findViewById(R.id.txt_routineDay);
            tvName = (TextView) itemView.findViewById(R.id.txt_routineName);
            tvTime = (TextView) itemView.findViewById(R.id.txt_routineTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mPosisi = getLayoutPosition();
            mRoutine = mRoutineList.get(mPosisi);
            Intent detilInten = new Intent(mContext, AddRoutineActivity.class);
            String title = "Update Routine";
            detilInten.putExtra("Update Routine", title);
            Bundle bundle = new Bundle();
            bundle.putSerializable("UrutanRoutine", mPosisi);
            bundle.putSerializable("ListRoutine", mRoutineList);
            detilInten.putExtras(bundle);
            mContext.startActivity(detilInten);
        }
    }
}


