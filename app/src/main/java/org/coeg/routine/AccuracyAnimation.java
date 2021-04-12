package org.coeg.routine;

import android.annotation.SuppressLint;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AccuracyAnimation extends Animation
{
    private final ProgressBar progressBar;
    private final TextView    textView;
    private final float       from;
    private final float       to;

    public AccuracyAnimation(ProgressBar progressBar, TextView textView, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.textView = textView;
        this.from = from;
        this.to = to;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
        textView.setText((int) value + "%");
    }
}