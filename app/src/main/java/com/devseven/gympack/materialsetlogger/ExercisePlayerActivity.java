package com.devseven.gympack.materialsetlogger;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devseven.gympack.materialsetlogger.data.ExerciseDay;

import java.util.Timer;


public class ExercisePlayerActivity extends AppCompatActivity {

    ExerciseDay day;
    int groupIndex=-1;
    int finishedSets=0;
    ProgressBar timerBar;
    ProgressBar progressBar;
    View topContainer;
    TextView timerProgressText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_player);
        topContainer = findViewById(R.id.topBarContainer);
        timerBar = (ProgressBar) findViewById(R.id.timerBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        timerProgressText = (TextView) findViewById(R.id.progressText);
        timerBar.setInterpolator(new LinearInterpolator());
        //CountDownTimer t = startTimer(10,(TextView) findViewById(R.id.progressText),timerBar);
        startTimerAnimation(10,(TextView) findViewById(R.id.progressText),timerBar);

    }
    private CountDownTimer startTimerAnimation(final long timeInSeconds, final TextView tv, final ProgressBar progressBar)
    {

        final CountDownTimer timer = new CountDownTimer(timeInSeconds*1000,100) {
            int timerInSeconds;
            int minutes;
            int seconds;
            @Override
            public void onTick(long l) {
                timerInSeconds = (int)(l/1000);
                minutes = timerInSeconds/60;
                seconds = timerInSeconds-60*minutes;
                tv.setText(
                            String.format("%02d:%02d",minutes,seconds)
                );
            }

            @Override
            public void onFinish() {
                onTimerFinish();
            }
        };


        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, progressBar.getMax());
        animation.setDuration (timeInSeconds*1000);
        animation.setInterpolator (new LinearInterpolator());
        animation.start();
        return timer.start();

    }
    void onTimerFinish()
    {
        if(groupIndex<0)
        {
            progressBar.setProgress(progressBar.getMax());
            timerProgressText.setText("--%");
            return;
        }
        float percentage = (float)finishedSets/day.exerciseGroups.get(groupIndex).exerciseSets.size()*100;
        timerProgressText.setText(String.format("%2f",percentage));
    }
}
