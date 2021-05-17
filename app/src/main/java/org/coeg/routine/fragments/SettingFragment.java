package org.coeg.routine.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import org.coeg.routine.activities.FirstSetupActivity;
import org.coeg.routine.activities.MainActivity;
import org.coeg.routine.backend.PreferencesStorage;
import org.coeg.routine.backend.Routine;
import org.coeg.routine.backend.RoutinesHandler;

import java.util.LinkedList;

public class SettingFragment extends Fragment
{
    private TextView btnEditProfile;
    private TextView btnDeleteAllRoutine;
    private TextView btnDeleteAllAnalytic;
    private TextView btnDeleteEverything;

    private SwitchMaterial swDiagnostic;

    PreferencesStorage preferences;
    private boolean enableTelemetry;

    private RoutinesHandler handler;
    private boolean deleteRoutines = false;
    private boolean deleteAnalytics = false;

    private DBAsync task;

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

        preferences = PreferencesStorage.getInstance();
        loadPreferences();

        task = new DBAsync();

        swDiagnostic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableTelemetry = b;
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
            Intent intent = new Intent(getContext(), FirstSetupActivity.class);
            startActivity(intent);
        });

        btnDeleteAllRoutine.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure to delete all your routines?").setTitle("Delete Routines");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteRoutines = true;
                    deleteAnalytics = false;
                    task.execute(getContext());
                }
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> {});
            builder.create().show();
        });

        btnDeleteAllAnalytic.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure to delete all your analytics?").setTitle("Delete Analytics");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteRoutines = false;
                    deleteAnalytics = true;
                    task.execute(getContext());
                }
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> {});
            builder.create().show();
        });

        btnDeleteEverything.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure to delete everything? This includes your routines, analytics, and your data!").setTitle("Reset All");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteRoutines = true;
                    deleteAnalytics = true;
                    task.execute(getContext());
                }
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> {});
            builder.create().show();
        });
    }

    private void loadPreferences() {
        preferences.loadPreferences(getContext());
        enableTelemetry = preferences.isTelemetryEnabled();
        swDiagnostic.setChecked(enableTelemetry);
    }

    private void savePreferences() {
        preferences.setEnableTelemetry(enableTelemetry);
        preferences.savePreferences();
    }

    private class DBAsync extends AsyncTask<Context, Integer, Void> {
        final ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setTitle("Delete");
            if(deleteRoutines && !deleteAnalytics) dialog.setMessage("Deleting all routines...");
            else if(!deleteRoutines && deleteAnalytics) dialog.setMessage("Deleting all analytics...");
            else if(deleteRoutines && deleteAnalytics) dialog.setMessage("Deleting all data...");
            else dialog.setMessage("What are you doing?");

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
            dialog.setIndeterminate(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Context... context) {
            try{
                if(handler == null) handler = new RoutinesHandler(context[0]);
                if(deleteRoutines) handler.deleteAllRoutines();
                if(deleteAnalytics) handler.deleteAllHistory();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();

            if(deleteRoutines && deleteAnalytics) {
                preferences.resetPreferences();
                preferences.savePreferences();
                Intent intent = new Intent(getContext(), FirstSetupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
}