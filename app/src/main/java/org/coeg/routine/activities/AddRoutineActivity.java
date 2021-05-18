package org.coeg.routine.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import ca.antonious.materialdaypicker.MaterialDayPicker;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.shawnlin.numberpicker.NumberPicker;

import org.coeg.routine.R;
import org.coeg.routine.backend.Days;
import org.coeg.routine.backend.PreferencesStorage;
import org.coeg.routine.backend.Routine;
import org.coeg.routine.backend.RoutinesHandler;
import org.coeg.routine.fragments.ListFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AddRoutineActivity extends AppCompatActivity
{
    private final static int REQ_ADD_SCHEDULE = 1;
    private final static int REQ_DELETE_SCHEDULE = 2;

    private final static int REQUEST_ADD = 0;
    private final static int REQUEST_EDIT = 1;

    private int                 reqCode;

    private TextView            tvHeader;
    private Button              btnAddRoutine;
    private Button              btnDeleteRoutine;
    private ImageButton         btnExit;

    private NumberPicker        npHour;
    private NumberPicker        npMinute;
    private MaterialDayPicker   dpDay;
    private EditText            etRoutineName;

    private RoutinesHandler     handler;
    private LinkedList<Routine> routineList = new LinkedList<>();

    private Routine             updatedRoutine;

    private boolean             isReady;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        InitView();
        InitListener();
        LoadFromDatabase();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        /** Allowing user to dismiss EditText by pressing
         *  somewhere outside the box
         */
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();
            if ( v instanceof EditText)
            {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Initialize variable by findViewByID
     * and setup view properties
     */
    private void InitView()
    {
        tvHeader        = findViewById(R.id.header);
        btnAddRoutine   = findViewById(R.id.btn_addRoutine);
        btnDeleteRoutine= findViewById(R.id.btn_deleteRoutine);
        btnExit         = findViewById(R.id.btn_exit);
        npHour          = findViewById(R.id.picker_hour);
        npMinute        = findViewById(R.id.picker_minute);
        dpDay           = findViewById(R.id.picker_day);
        etRoutineName   = findViewById(R.id.et_inputRoutineName);

        // Change NumberPicker font family when selected or not selected
        npHour.setTypeface(ResourcesCompat.getFont(this, R.font.nunito_extrabold));
        npHour.setSelectedTypeface(ResourcesCompat.getFont(this, R.font.nunito_extrabold));

        npMinute.setTypeface(ResourcesCompat.getFont(this, R.font.nunito_extrabold));
        npMinute.setSelectedTypeface(ResourcesCompat.getFont(this, R.font.nunito_extrabold));

        //Set The Header Text View's Content
        Intent intent = getIntent();
        reqCode = intent.getIntExtra("ReqCode", -1);

        // If Update mode
        if(reqCode == 1)
        {
            btnDeleteRoutine.setVisibility(View.VISIBLE);

            int rvPos = intent.getIntExtra("Position", -1);
            Bundle bundle = intent.getExtras();
            Routine routine = (Routine) bundle.getSerializable("Routine");
            updatedRoutine = new Routine();
            updatedRoutine = routine;
            Days[] days = routine.getDays();

            String routineTimeString = routine.getTimeAsString();
            Calendar routineTime = Calendar.getInstance();

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            try
            {
                routineTime.setTime(formatter.parse(routineTimeString));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Error();
            }

            tvHeader.setText("Update Routine");
            btnAddRoutine.setText("Update Routine");

            etRoutineName.setText(routine.getName());
            npHour.setValue(routineTime.get(Calendar.HOUR_OF_DAY));
            npMinute.setValue(routineTime.get(Calendar.MINUTE));

            MaterialDayPicker.Weekday[] selectedDays = new MaterialDayPicker.Weekday[days.length];
            for (int i = 0; i < days.length; i++)
            {
                switch(days[i])
                {
                    case Sunday:
                        selectedDays[i] = MaterialDayPicker.Weekday.SUNDAY;
                        break;

                    case Monday:
                        selectedDays[i] = MaterialDayPicker.Weekday.MONDAY;
                        break;

                    case Tuesday:
                        selectedDays[i] = MaterialDayPicker.Weekday.TUESDAY;
                        break;

                    case Wednesday:
                        selectedDays[i] = MaterialDayPicker.Weekday.WEDNESDAY;
                        break;

                    case Thursday:
                        selectedDays[i] = MaterialDayPicker.Weekday.THURSDAY;
                        break;

                    case Friday:
                        selectedDays[i] = MaterialDayPicker.Weekday.FRIDAY;
                        break;

                    case Saturday:
                        selectedDays[i] = MaterialDayPicker.Weekday.SATURDAY;
                        break;
                }
            }

            dpDay.setSelectedDays(selectedDays);
        }
    }

    /**
     * Initialize component listener
     */
    private void InitListener()
    {
        // Check and add user data to database
        btnAddRoutine.setOnClickListener(v -> {
            if (reqCode != 1)
                addRoutine();
            else
                updateRoutine();
        });

        // Exit the Add Routine activity
        btnExit.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        if(reqCode == 1) {
            btnDeleteRoutine.setOnClickListener(v -> {
                deleteRoutine();
            });
        }
    }

    private void LoadFromDatabase()
    {
        isReady = false;
        Context context = getApplicationContext();
        handler = new RoutinesHandler(context);

        Runnable r = () -> {
            routineList.addAll(handler.getAllRoutines());
            isReady = true;
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void addRoutine()
    {
        // If database not yet loaded the current routine data
        if (!isReady)
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Please wait, we're still loading data from database",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Log.i("DEBUG-TEST", "ADD ROUTINE CALLED");

        String routineName = etRoutineName.getText().toString();
        int hour = npHour.getValue();
        int minute = npMinute.getValue();
        List<MaterialDayPicker.Weekday> selectedDays = dpDay.getSelectedDays();

        // Check if there are empty input field
        if (routineName.isEmpty() || selectedDays.isEmpty())
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Please fill all the necessary field!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Convert from day picker to own Days class
        Days[] days = new Days[selectedDays.size()];
        for (int i = 0; i < selectedDays.size(); i++)
        {
            switch(selectedDays.get(i))
            {
                case MONDAY:
                    days[i] = Days.Monday;
                    break;

                case TUESDAY:
                    days[i] = Days.Tuesday;
                    break;

                case WEDNESDAY:
                    days[i] = Days.Wednesday;
                    break;

                case THURSDAY:
                    days[i] = Days.Thursday;
                    break;

                case FRIDAY:
                    days[i] = Days.Friday;
                    break;

                case SATURDAY:
                    days[i] = Days.Saturday;
                    break;

                case SUNDAY:
                    days[i] = Days.Sunday;
                    break;
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Routine routine = new Routine();
        routine.setName(routineName);
        routine.setDays(days);
        routine.setActive(true);
        try
        {
            routine.setTime(formatter.parse("" + hour + ":" + minute + ":00"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Error();
        }

        if (CheckAvailability(routine))
        {
            incrementRoutineCount();
            new DBAsync().execute(routine);
        }
        else
        {
            Toast.makeText(
                    getApplicationContext(),
                    "There is already a routine with the same date and time!",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void updateRoutine()
    {
        // If database not yet loaded the current routine data
        if (!isReady)
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Please wait, we're still loading data from database",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        Log.i("DEBUG-TEST", "ADD ROUTINE CALLED");

        String routineName = etRoutineName.getText().toString();
        int hour = npHour.getValue();
        int minute = npMinute.getValue();
        List<MaterialDayPicker.Weekday> selectedDays = dpDay.getSelectedDays();

        // Check if there are empty input field
        if (routineName.isEmpty() || selectedDays.isEmpty())
        {
            Toast.makeText(
                    getApplicationContext(),
                    "Please fill all the necessary field!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Convert from day picker to own Days class
        Days[] days = new Days[selectedDays.size()];
        for (int i = 0; i < selectedDays.size(); i++)
        {
            switch(selectedDays.get(i))
            {
                case MONDAY:
                    days[i] = Days.Monday;
                    break;

                case TUESDAY:
                    days[i] = Days.Tuesday;
                    break;

                case WEDNESDAY:
                    days[i] = Days.Wednesday;
                    break;

                case THURSDAY:
                    days[i] = Days.Thursday;
                    break;

                case FRIDAY:
                    days[i] = Days.Friday;
                    break;

                case SATURDAY:
                    days[i] = Days.Saturday;
                    break;

                case SUNDAY:
                    days[i] = Days.Sunday;
                    break;
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Routine oldRoutine = updatedRoutine;
        Date date = new Date();
        updatedRoutine.setName(routineName);
        updatedRoutine.setDays(days);
        updatedRoutine.setActive(true);
        try
        {
            updatedRoutine.setTime(formatter.parse("" + hour + ":" + minute + ":00"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Error();
        }

        Log.i("DEBUG-TEST", "" + updatedRoutine.getName());

        if (CheckAvailability(updatedRoutine))
        {
            oldRoutine.schedule(getApplicationContext(), REQ_DELETE_SCHEDULE);
            updatedRoutine.schedule(getApplicationContext(), REQ_ADD_SCHEDULE);
            new UpdateDBAsync().execute(updatedRoutine);
        }
        else
        {
            Toast.makeText(
                    getApplicationContext(),
                    "There is already a routine with the same date and time!",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void deleteRoutine()
    {
        decrementRoutineCount();
        updatedRoutine.schedule(getApplicationContext(), REQ_DELETE_SCHEDULE);
        new DeleteDBAsync().execute(updatedRoutine);
    }

    private void scheduleRoutine(int id, Routine routine)
    {
        routine.setId(id);
        routine.schedule(getApplicationContext(), REQ_ADD_SCHEDULE);
    }

    /**
     * Check if there are any routines already in database
     * @param routine user input
     * @return true if available
     */
    private boolean CheckAvailability(Routine routine)
    {
        // Check if there are existing routine with the same hours + days
        for (Routine routineDB : routineList)
        {
            if (routineDB.getId() == routine.getId())
            {
                continue;
            }

            Log.i("ROUTINE-LIST", "" + routineDB.getId());
            // Check if there are existing related to hours
            if (routineDB.getTimeAsString().equals(routine.getTimeAsString()))
            {
                Days[] dayDB = routineDB.getDays();

                // Check if there are existing related to days
                for (Days day : routine.getDays())
                {
                    if (Arrays.asList(dayDB).contains(day))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void incrementRoutineCount()
    {
        PreferencesStorage preferences = PreferencesStorage.getInstance();
        preferences.incrementRoutineCounter();
        preferences.savePreferences();
    }

    private void decrementRoutineCount()
    {
        PreferencesStorage preferences = PreferencesStorage.getInstance();
        preferences.decrementRoutineCounter();
        preferences.savePreferences();
    }

    private void Error()
    {
        Toast.makeText(
                getApplicationContext(),
                "Something went wrong",
                Toast.LENGTH_LONG)
                .show();
    }

    private class DBAsync extends AsyncTask<Routine, Void, Void>
    {
        Routine routine;
        long id;

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Routine... routine)
        {
            this.routine = routine[0];
            id = handler.addRoutine(routine[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            super.onPostExecute(unused);
            int routineID = (int) id;
            scheduleRoutine(routineID, routine);
            Toast.makeText(
                    getApplicationContext(),
                    "Routine added!",
                    Toast.LENGTH_LONG)
                    .show();
            setResult(RESULT_OK);
            finish();
        }
    }

    private class UpdateDBAsync extends AsyncTask<Routine, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Routine... routine)
        {
            handler.updateRoutine(routine[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            super.onPostExecute(unused);
            Toast.makeText(
                    getApplicationContext(),
                    "Routine updated!",
                    Toast.LENGTH_LONG)
                    .show();
            setResult(RESULT_OK);
            finish();
        }
    }

    private class DeleteDBAsync extends AsyncTask<Routine, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Routine... routine)
        {
            handler.deleteRoutine(routine[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            super.onPostExecute(unused);
            Toast.makeText(
                    getApplicationContext(),
                    "Routine updated!",
                    Toast.LENGTH_LONG)
                    .show();
            setResult(RESULT_OK);
            finish();
        }
    }
}