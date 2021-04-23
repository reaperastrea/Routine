package org.coeg.routine.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.coeg.routine.R;
import org.coeg.routine.backend.PreferencesStorage;

public class SplashActivity extends AppCompatActivity
{
    private static final int SPLASH_DELAY = 1000; //ms

    private ImageView logo;
    private Animation expandIn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        InitView();
        InitListener();
        PlayAnimation();
    }

    /**
     * Initialize variable by findViewByID
     * and setup view properties
     */
    private void InitView()
    {
        logo = findViewById(R.id.img_logo);
        expandIn = AnimationUtils.loadAnimation(this, R.anim.expand_in);
    }

    /**
     * Initialize component listener
     */
    private void InitListener()
    {
        expandIn.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CheckForConfig();
                    }
                }, SPLASH_DELAY);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }

    /**
     * Play splash screen animation
     */
    private void PlayAnimation()
    {
        logo.startAnimation(expandIn);
    }

    /**
     * Check whether user had undergone the first time setup
     */
    private void CheckForConfig()
    {
        PreferencesStorage preferences = PreferencesStorage.getInstance();
        preferences.loadPreferences(getApplicationContext());

        if (preferences.getUserId() == -1)
        {
            GoToFirstSetup();
            return;
        }

        GoToMainActivity();
    }

    private void GoToMainActivity()
    {
        finish();
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    private void GoToFirstSetup()
    {
        finish();
        startActivity(new Intent(SplashActivity.this, FirstSetupActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }
}