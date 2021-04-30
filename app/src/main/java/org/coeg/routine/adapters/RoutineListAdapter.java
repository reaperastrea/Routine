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
import org.coeg.routine.backend.Days;
import org.coeg.routine.backend.Routine;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class RoutineListAdapter extends RecyclerView.Adapter<RoutineListAdapter.RoutinesViewHolder> {
    private LinkedList<Routine> mRoutineList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int count = 0;

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
        holder.tvTime.setText(mRoutine.getTime().toString().substring(0,5));

        count = 0;
        Days[] days = new Days[] { Days.Monday, Days.Tuesday, Days.Wednesday, Days.Thursday, Days.Friday, Days.Saturday, Days.Sunday };
        Days[] routineDays = mRoutine.getDays();
        StringBuilder builder = new StringBuilder();

        if(routineDays.length > 1 && routineDays.length < 7){
            for(int i = 0; i < routineDays.length; i++) {
                if(routineDays[count].equals(days[i])){
                    count++;
                    switch (i){
                        case 1:
                            builder.append("Mon");
                            break;
                        case 2:
                            builder.append("Tue");
                            break;
                        case 3:
                            builder.append("Wed");
                            break;
                        case 4:
                            builder.append("Thu");
                            break;
                        case 5:
                            builder.append("Fri");
                            break;
                        case 6:
                            builder.append("Sat");
                            break;
                        case 7:
                            builder.append("Sun");
                            break;
                    }
                }
                if (i < (routineDays.length-1)){
                    builder.append(",");
                }
            }
            holder.tvDay.setText(builder.toString());
        }else if(routineDays.length == 1){
            switch (routineDays[0]){
                case Monday:
                    holder.tvDay.setText("Monday");
                    break;
                case Tuesday:
                    holder.tvDay.setText("Tuesday");
                    break;
                case Wednesday:
                    holder.tvDay.setText("Wednesday");
                    break;
                case Thursday:
                    holder.tvDay.setText("Thursday");
                    break;
                case Friday:
                    holder.tvDay.setText("Friday");
                    break;
                case Saturday:
                    holder.tvDay.setText("Saturday");
                    break;
                case Sunday:
                    holder.tvDay.setText("Sunday");
                    break;
            }
        }else if(routineDays.length == 7){
            holder.tvDay.setText("Everyday");
        }else{
            holder.tvDay.setText("Error");
        }
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


