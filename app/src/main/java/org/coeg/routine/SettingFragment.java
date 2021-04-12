package org.coeg.routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingFragment extends Fragment
{
    private TextView btnEditProfile;
    private TextView btnDeleteAllRoutine;
    private TextView btnDeleteAllAnalytic;
    private TextView btnDeleteEverything;

    private SwitchMaterial swDiagnostic;
    private SwitchMaterial swPushNotification;
    private SwitchMaterial swPrePushNotification;

    public SettingFragment() { }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        InitView(view);
        InitListener();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    /**
     * Initialize variable by findViewByID
     * and setup view properties
     * @param view fragment view
     */
    private void InitView(View view)
    {
        btnEditProfile = view.findViewById(R.id.btn_editProfile);
        btnDeleteAllRoutine = view.findViewById(R.id.btn_deleteAllRoutine);
        btnDeleteAllAnalytic = view.findViewById(R.id.btn_deleteAllAnalytics);
        btnDeleteEverything = view.findViewById(R.id.btn_deleteEverything);

        swDiagnostic = view.findViewById(R.id.switch_diagnostic);
        swPushNotification = view.findViewById(R.id.switch_pushNotification);
        swPrePushNotification = view.findViewById(R.id.switch_prePushNotification);
    }

    /**
     * Initialize component listener
     */
    private void InitListener()
    {
        btnEditProfile.setOnClickListener(v -> {

        });

        btnDeleteAllRoutine.setOnClickListener(v -> {

        });

        btnDeleteAllAnalytic.setOnClickListener(v -> {

        });

        btnDeleteEverything.setOnClickListener(v -> {

        });
    }
}