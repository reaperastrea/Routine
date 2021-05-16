package org.coeg.routine.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import ca.antonious.materialdaypicker.MaterialDayPicker;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.shawnlin.numberpicker.NumberPicker;

import org.coeg.routine.R;
import org.coeg.routine.adapters.RoutineListAdapter;
import org.coeg.routine.backend.Routine;
import org.coeg.routine.backend.RoutineDatabase;
import org.coeg.routine.backend.RoutinesHandler;
import org.coeg.routine.fragments.ListFragment;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AddRoutineActivity extends AppCompatActivity
{
    private TextView            tvHeader;
    private Button              btnAddRoutine;
    private ImageButton         btnExit;

    private NumberPicker        npHour;
    private NumberPicker        npMinute;
    private MaterialDayPicker   dpDay;
    private EditText            etRoutineName;
    private SwitchMaterial      swSnooze;

    private RoutineListAdapter  mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        //new dbasnc().execute(this.getContext());

        InitView();
        InitListener();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        /** Allowing user to dismiss EditText by pressing
         *  somewhere outside the box
         */
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
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
        btnExit         = findViewById(R.id.btn_exit);
        npHour          = findViewById(R.id.picker_hour);
        npMinute        = findViewById(R.id.picker_minute);
        dpDay           = findViewById(R.id.picker_day);
        etRoutineName   = findViewById(R.id.et_inputRoutineName);
        swSnooze        = findViewById(R.id.switch_allowSnoozing);

        // Change NumberPicker font family when selected or not selected
        npHour.setTypeface(ResourcesCompat.getFont(this, R.font.nunito_extrabold));
        npHour.setSelectedTypeface(ResourcesCompat.getFont(this, R.font.nunito_extrabold));

        npMinute.setTypeface(ResourcesCompat.getFont(this, R.font.nunito_extrabold));
        npMinute.setSelectedTypeface(ResourcesCompat.getFont(this, R.font.nunito_extrabold));

        //Set Value NumberPicker
        npHour.setMaxValue(23);
        npHour.setMinValue(00);

        npMinute.setMaxValue(59);
        npMinute.setMinValue(0);

        //Set The Header Text View's Content
        Intent intent = getIntent();
        if(intent.getStringExtra("Update Routine").equals("Update Routine")){
            tvHeader.setText("Update Routine");
        }
    }

    /**
     * Initialize component listener
     */
    private void InitListener()
    {
        //Day Picker
        dpDay.selectAllDays();
        dpDay.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @Override
            public void onDayPressed(@NonNull MaterialDayPicker.Weekday weekday, boolean isSelected) {
                dpDay.setDayEnabled(weekday, isSelected);
            }
        });

        // Check and add user data to database
        btnAddRoutine.setOnClickListener(v -> {
            npHour.getValue();
            npMinute.getValue();
            dpDay.getSelectedDays();
        });

        // Exit the Add Routine activity
        btnExit.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    private class dbadd extends AsyncTask<Context, Integer, List<String>> {
        List<String>        selectedDays;
        boolean             exists;
        RoutinesHandler     handler;

        @Override
        protected List<String> doInBackground(Context... contexts) {
            /*handler = new RoutinesHandler(context[0]);
            handler.addRoutine(dpDay, npHour, npMinute);*/
            try {

                exists = false;
                /*for(int i = 0; i < selectedDays.contains(dpDay + npHour + npMinute); i++)
                {
                    checkIfExists(selectedDays.get(i), npHour)
                }*/

                if (exists)
                    Toast.makeText(getApplicationContext(), "Theres already a routine overlap that day and time", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (List<String>) handler;
        }

        private void checkIfExists(String s, NumberPicker npHour, MaterialDayPicker dpDay) {
            if (selectedDays.contains(npHour) && selectedDays.contains(dpDay))
                exists = true;
        }
    }

    /*private void removeDuplicates() {
        List<MaterialDayPicker> arr = Arrays.asList(dpDay);
        List<NumberPicker> brr = Arrays.asList(npHour, npMinute);
        Set<Integer> hashSet = new LinkedHashSet(arr);
        Set<Float> hashSet1 = new LinkedHashSet(brr);
        ArrayList<Integer> removedDuplicates1 = new ArrayList(hashSet);
        ArrayList<Integer> removedDuplicates2 = new ArrayList(hashSet1);
    }*/
}