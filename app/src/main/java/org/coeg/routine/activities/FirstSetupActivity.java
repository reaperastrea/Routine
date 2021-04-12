package org.coeg.routine.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.coeg.routine.R;

public class FirstSetupActivity extends AppCompatActivity
{
    private TextView            txtTitle;
    private Button              btnStartTracking;
    private EditText            etDisplayName;
    private ImageView           imgUser;
    private ConstraintLayout    layoutUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);

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
        txtTitle            = findViewById(R.id.txt_titleFirstSetup);
        btnStartTracking    = findViewById(R.id.btn_startTracking);
        etDisplayName       = findViewById(R.id.et_inputDisplayName);
        imgUser             = findViewById(R.id.img_inputUserImage);
        layoutUserImage     = findViewById(R.id.layout_userImage);

        /** Check for database data
         *  If user already went through first setup
         *      THEN Change the txtTitle to Edit Profile getString(R.string.title_edit_profile);
         *           Change the btnStartTracking text to Update Info getString(R.string.btn_update_profile);
         *  If NOT
         *      THEN don't change the title
         */
    }

    /**
     * Initialize component listener
     */
    private void InitListener()
    {
        /** Check for database data
         *  IF user already went through first setup
         *      THEN button action is to update data from database
         *  IF NOT
         *      THEN button action is to add data from database
         */
        btnStartTracking.setOnClickListener(v -> {
            startActivity(new Intent(FirstSetupActivity.this, MainActivity.class));
        });

        layoutUserImage.setOnClickListener(v -> {
            /** Run intent to get image from
             *  phone gallery application
             */
        });
    }
}
