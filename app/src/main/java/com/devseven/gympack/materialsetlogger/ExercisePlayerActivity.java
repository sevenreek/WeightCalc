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
import android.widget.Toast;

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
    TextView currentExercise;
    TextView prevExercise;
    TextView nextExercise;
    View prevExerciseButton;
    View nextExerciseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_exercise_player);
        topContainer = findViewById(R.id.topBarContainer);
        timerBar = (ProgressBar) findViewById(R.id.timerBar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        timerProgressText = (TextView) findViewById(R.id.progressText);
        prevExercise = (TextView) findViewById(R.id.prevExercise);
        nextExercise = (TextView) findViewById(R.id.nextExercise);
        prevExerciseButton = findViewById(R.id.prevExerciseButton);
        nextExerciseButton = findViewById(R.id.nextExerciseButton);
        startTimerAnimation(10,(TextView) findViewById(R.id.progressText),timerBar);
    }
    private CountDownTimer startTimerAnimation(long timeInSeconds, final TextView tv, ProgressBar progressBar)
    {

         CountDownTimer timer = new CountDownTimer(timeInSeconds*1000,100) {
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
        // I got an idea that I could remove this animation by putting setProgress in onTick and setting
        // the countdown interval to timeInSeconds/quotient. A value of quotient could be found that would
        // make the animation look smooth while not being called every frame(or even more often).
        // However I do not think that the animation is that expensive on resources to refactor this method.
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 0, progressBar.getMax());
        animation.setDuration (timeInSeconds*1000);
        animation.setInterpolator (new LinearInterpolator());
        animation.start();
        return timer.start();

    }
    void onSelectExercise(int index)
    {
        // This function should get called every time the user wants to select an exercisegroup. It supports selecting -1 when
        // the day has no groups(i.e. user removed them all or never bothered to add any when creating a day).
        // Most often this will get currentIndex+-1 passed to it, but I am considering creating an alternative listView
        // which would allow users to select any exercise they wish to. It will come in handy then.
        if(index == -1)
        {
            onAllExercisesRemoved();
        }
    }

    private void onAllExercisesRemoved()
    {

    }

    void onHitLeftExerciseBound()
    {
        // This function gets called whenever the user selects the exercise group 0.
        // It causes the prevDayButton to hide.

    }
    void onHitRightExerciseBound()
    {
        // This function gets called whenever the user selects the last exercise group.
        // It causes the nextDayButton to shift to its other functionality - add group.
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
