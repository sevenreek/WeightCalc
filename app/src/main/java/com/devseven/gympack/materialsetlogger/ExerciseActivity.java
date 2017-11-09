package com.devseven.gympack.materialsetlogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.setlogger.R;

public class ExerciseActivity extends AppCompatActivity
{
    public static final String PROGRAM_TO_PASS = "PROGRAM_TO_PASS";
    ExerciseDay day;
    @Override
    public void onCreate(Bundle onSavedInstanceState)
    {
        super.onCreate(onSavedInstanceState);
        Intent intent = getIntent();
        if(intent!=null)
        {
            day = intent.getParcelableExtra(PROGRAM_TO_PASS);
        }
        // If there is no intent(dunno if it is possible)
        // or the day is not passed means that the user entered
        // quick start mode and we have to create a new ExerciseDay.
        if(day == null)
        {
            day = new ExerciseDay();
        }

        setContentView(R.layout.material_exercise_activity_v2);

    }




}