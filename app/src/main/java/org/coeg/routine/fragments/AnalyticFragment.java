package org.coeg.routine.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import org.coeg.routine.backend.InternalStorage;
import org.coeg.routine.backend.PreferencesStorage;

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

    private String  name;
    private Bitmap profilePicture;
    private Integer lateCount;
    private Integer onTimeCount;
    private Integer routineCount;
    private Integer accuracy;

    public AnalyticFragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        FetchPreferences();
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
        txtName.setText(name);
        txtRoutineCount.setText(routineCount.toString());
        txtOnTimeCount.setText(onTimeCount.toString());
        txtLateCount.setText(lateCount.toString());
        txtAccuracy.setText(accuracy.toString());
        imgUser.setImageBitmap(profilePicture);

        //Play animation according
        //int accuracy = 100;         // FOR DEBUG PURPOSE //I've made the global variable version of this
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

    private void FetchPreferences(){
        PreferencesStorage preferences = PreferencesStorage.getInstance();
        InternalStorage internalStorage = new InternalStorage(getContext());
        preferences.loadPreferences(this.getContext());

        name = preferences.getFullName();
        profilePicture = internalStorage.GetImageFromInternalStorage();
        lateCount = preferences.getLateCounter();
        onTimeCount = preferences.getOnTimeCounter();
        routineCount = preferences.getRoutineCounter();

        if (routineCount != 0)
        {
            accuracy = (onTimeCount / routineCount) * 100;
        }
        else
        {
            accuracy = 0;
        }
    }
}