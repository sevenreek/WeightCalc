package com.devseven.gympack.materialsetlogger.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.simpleframework.xml.Element;

/**
 * Created by Dickbutt on 05.06.2017.
 */

public class ExerciseSet implements Parcelable{
    public ExerciseSet() {}
    public ExerciseSet(int goalReps, int restTimeInSeconds)
    {
        this.goalReps = goalReps;
        this.restTimeInSeconds = restTimeInSeconds;
    }
    public ExerciseSet(Parcel in)
    {
        restTimeInSeconds = in.readInt();
        goalReps = in.readInt();
        doneReps = in.readInt();
        usedWeight = in.readDouble();
        finished = in.readByte() != 0;
    }

    @Element
    int restTimeInSeconds;
    @Element
    int goalReps;
    @Element(required = false)
    int doneReps;
    @Element(required = false)
    double usedWeight;
    private boolean finished;

    public static final Creator<ExerciseSet> CREATOR = new Creator<ExerciseSet>() {
        @Override
        public ExerciseSet createFromParcel(Parcel in) {
            return new ExerciseSet(in);
        }

        @Override
        public ExerciseSet[] newArray(int size) {
            return new ExerciseSet[size];
        }
    };

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

    public int getDoneReps() {
        return doneReps;
    }

    public void setDoneReps(int doneReps) {
        this.doneReps = doneReps;
    }

    public double getUsedWeight() {
        return usedWeight;
    }

    public void setUsedWeight(double usedWeight) {
        this.usedWeight = usedWeight;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public ExerciseSet clone()
    {
        ExerciseSet set = new ExerciseSet(this.goalReps,this.restTimeInSeconds);
        set.doneReps = this.doneReps;
        set.usedWeight = this.usedWeight;
        return set;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(restTimeInSeconds);
        dest.writeInt(goalReps);
        dest.writeInt(doneReps);
        dest.writeDouble(usedWeight);
        dest.writeByte((byte) (finished ? 1 : 0));
    }
}
