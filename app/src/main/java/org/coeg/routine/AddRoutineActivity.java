package org.coeg.routine;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import ca.antonious.materialdaypicker.MaterialDayPicker;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.shawnlin.numberpicker.NumberPicker;

public class AddRoutineActivity extends AppCompatActivity
{
    private Button              btnAddRoutine;
    private ImageButton         btnExit;

    private NumberPicker        npHour;
    private NumberPicker        npMinute;
    private MaterialDayPicker   dpDay;
    private EditText            etRoutineName;
    private SwitchMaterial      swSnooze;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

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
    }

    /**
     * Initialize component listener
     */
    private void InitListener()
    {
        // Check and add user data to database
        btnAddRoutine.setOnClickListener(v -> {

        });

        // Exit the Add Routine activity
        btnExit.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}