package com.devseven.gympack.setlogger;

import android.content.Context;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;

/**
 * Created by Dickbutt on 26.06.2017.
 */

public class ExerciseGroup {
    @Attribute
    private String exerciseName;
    @ElementList
    public ArrayList<ExerciseSet> exerciseSets = new ArrayList<ExerciseSet>();

    public ExerciseGroup() {
    }

    private boolean isInitialized;
    private boolean _wasSelected;
    public void setWasSelected(boolean tf)
    {
        _wasSelected = tf;
    }
    public boolean wasSelected()
    {
        return _wasSelected;
    }

    Exercise exercise;

    public Exercise getExercise()
    {
        return exercise;
    }
    public void Initialize(Context context) {
        exercise = Exercise.findExerciseByName(exerciseName, context, true);
        isInitialized = true;
    }

    public String getExerciseName()
    {
        return exerciseName;
    }
    ExerciseSet currentSet;
    public ExerciseGroup clone(Context context)
    {
        if(!isInitialized)
            Initialize(context);
        ExerciseGroup group = new ExerciseGroup(this.exercise);
        for (ExerciseSet es :exerciseSets
             ) {
         group.exerciseSets.add(es.clone(context));
        }
        group.Initialize(context);
        return group;
    }

    public String getShortRepsOfSets()
    {
        String str = "";
        for (int i=0;i<exerciseSets.size();i++)
        {
            if(i==exerciseSets.size()-1)
                str+=exerciseSets.get(i).getGoalReps();
            else
                str+=exerciseSets.get(i).getGoalReps()+"/\u200B";
        }
        return str;
    }


    public ExerciseGroup(String exerciseName, Context context)
    {
        this.exerciseName = exerciseName;
        this.Initialize(context);
    }
    public ExerciseGroup(Exercise exercise)
    {
        this.exercise = exercise;
        this.exerciseName = exercise.getName();
    }

}
