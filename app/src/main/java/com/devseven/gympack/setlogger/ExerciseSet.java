package com.devseven.gympack.setlogger;

import android.content.Context;
import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;

/**
 * Created by Dickbutt on 05.06.2017.
 */

public class ExerciseSet {
    public ExerciseSet()
    {

    }

    public ExerciseSet clone(@Nullable Context context)
    {
        ExerciseSet set = new ExerciseSet(this.goalReps,this.restTimeInSeconds);
        set.doneReps = this.doneReps;
        set.usedWeight = this.usedWeight;
        return set;
    }
    public ExerciseSet(int goalReps, int restTimeInSeconds)
    {
        this.goalReps = goalReps;
        this.restTimeInSeconds = restTimeInSeconds;
        // TODO: code that finds Exercise from exerciseName in ExerciseSet and finishedExerciseSet class;
    }

    @Element
    int restTimeInSeconds;
    @Element
    int goalReps;
    @Element(required = false)
    int doneReps;
    @Element(required = false)
    double usedWeight;
    public double getUsedWeight() {
        return usedWeight;
    }
    public void setUsedWeight(double usedWeight) {
        this.usedWeight = usedWeight;
    }
    public int getDoneReps() {
        return doneReps;
    }
    public void setDoneReps(int doneReps) {
        this.doneReps = doneReps;
    }
    private boolean finished;
    public void setFinished(boolean fin)
    {
        finished = fin;
    }
    public boolean isFinished()
    {
        return finished;
    }

    // If exercise is the last of day restTime is set to 0 in methods setLastOfDay and setRestTime...
    public int getRestTimeInSeconds() {
        return restTimeInSeconds;
    }
    public void setRestTimeInSeconds(int restTimeInSeconds) {
        this.restTimeInSeconds = restTimeInSeconds;
    }




    public int getGoalReps() {
        return goalReps;
    }
    public void setGoalReps(int goalReps) {
        this.goalReps = goalReps;
    }


}
