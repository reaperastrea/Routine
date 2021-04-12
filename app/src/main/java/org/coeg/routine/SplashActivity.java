package org.coeg.routine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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
                        startActivity(new Intent(SplashActivity.this, FirstSetupActivity.class));
                        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
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
}