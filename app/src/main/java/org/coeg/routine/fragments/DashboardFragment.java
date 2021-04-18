package org.coeg.routine.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.coeg.routine.adapters.RoutineListAdapter;
import org.coeg.routine.animations.AccuracyAnimation;
import org.coeg.routine.R;
import org.coeg.routine.backend.Routine;

import java.util.LinkedList;

public class DashboardFragment extends Fragment
{
    private static final int ANIMATION_DURATION = 1500;

    private TextView        txtName;
    private TextView        txtAccuracy;
    private ProgressBar     pbAccuracy;
    private RecyclerView    rvRecentRoutine;
    RoutineListAdapter mAdapter;
    LinkedList<Routine> routineList = new LinkedList<>();

    public DashboardFragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        InitView(view);
        routineListTest();
        mAdapter = new RoutineListAdapter(this.getContext(), routineList);
        rvRecentRoutine.setAdapter(mAdapter);
        rvRecentRoutine.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    /**
     * Initialize variable by findViewByID
     * and setup view properties
     */
    private void InitView(View view)
    {
        txtName         = view.findViewById(R.id.txt_name);
        txtAccuracy     = view.findViewById(R.id.txt_userAccuracy);
        pbAccuracy      = view.findViewById(R.id.progress_accuracy);
        rvRecentRoutine = view.findViewById(R.id.rv_recentRoutine);

        // Set view data from database

        //Play animation according
        int accuracy = 100;         // FOR DEBUG PURPOSE
        PlayAnimation(accuracy);
    }

    /**
     * Play accuracy progress animation
     * @param accuracy user accuracy according to calculation from database routine data
     */
    private void PlayAnimation(int accuracy)
    {
        AccuracyAnimation pbAnim = new AccuracyAnimation(pbAccuracy, txtAccuracy, 0, accuracy);
        pbAnim.setDuration(ANIMATION_DURATION);
        pbAccuracy.startAnimation(pbAnim);
    }

    public void routineListTest(){
        //routineList.add(new Routine());
    }
}