package org.coeg.routine.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;

import org.coeg.routine.animations.AccuracyAnimation;
import org.coeg.routine.R;

public class AnalyticFragment extends Fragment
{
    private static final int ANIMATION_DURATION = 1500;

    private TextView            txtName;
    private TextView            txtRoutineCount;
    private TextView            txtOnTimeCount;
    private TextView            txtLateCount;
    private TextView            txtAccuracy;
    private ProgressBar         pbAccuracy;
    private ShapeableImageView  imgUser;

    public AnalyticFragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        InitView(view);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_analytic, container, false);
    }

    /**
     * Initialize variable by findViewByID
     * and setup view properties
     * @param view fragment view
     */
    private void InitView(View view)
    {
        txtName             = view.findViewById(R.id.txt_name);
        txtRoutineCount     = view.findViewById(R.id.txt_routineCount);
        txtOnTimeCount      = view.findViewById(R.id.txt_onTimeCount);
        txtLateCount        = view.findViewById(R.id.txt_lateCount);
        txtAccuracy         = view.findViewById(R.id.txt_userAccuracy);
        pbAccuracy          = view.findViewById(R.id.progress_accuracy);
        imgUser             = view.findViewById(R.id.imgUser);

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
}