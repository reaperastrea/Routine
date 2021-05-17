package org.coeg.routine.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.coeg.routine.R;
import org.coeg.routine.backend.PreferencesStorage;

public class SettingFragment extends Fragment
{
    private TextView btnEditProfile;
    private TextView btnDeleteAllRoutine;
    private TextView btnDeleteAllAnalytic;
    private TextView btnDeleteEverything;

    private SwitchMaterial swDiagnostic;
    private SwitchMaterial swPushNotification;
    private SwitchMaterial swPrePushNotification;

    PreferencesStorage preferences;

    private boolean enableTelemetry;
    private boolean enablePushNotifications;
    private boolean enablePreReminder;

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

        preferences = PreferencesStorage.getInstance();
        loadPreferences();

        swDiagnostic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i(getClass().toString(), "enableTelemetry = " + b);
                enableTelemetry = b;
                savePreferences();
            }
        });
        swPushNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i(getClass().toString(), "enablePushNotifications = " + b);
                enablePushNotifications = b;
                savePreferences();
            }
        });
        swPrePushNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i(getClass().toString(), "enablePreReminder = " + b);
                enablePreReminder = b;
                savePreferences();
            }
        });
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

    private void loadPreferences() {
        preferences.loadPreferences(getContext());

        // load
        enableTelemetry = preferences.isTelemetryEnabled();
        enablePushNotifications = preferences.isPushNotificationsEnabled();
        enablePreReminder = preferences.isPreReminderEnabled();
        Log.i(getClass().toString(), "loaded = " + enableTelemetry + " " + enablePushNotifications + " " + enablePreReminder);

        // set switches
        swDiagnostic.setChecked(enableTelemetry);
        swPushNotification.setChecked(enablePushNotifications);
        swPrePushNotification.setChecked(enablePreReminder);
    }

    private void savePreferences() {
        preferences.setEnableTelemetry(enableTelemetry);
        preferences.setEnablePushNotifications(enablePushNotifications);
        preferences.setEnablePreReminder(enablePreReminder);
        preferences.savePreferences();
    }
}