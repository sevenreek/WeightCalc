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
    public ExerciseSet(int goalReps)
    {
        this.goalReps = goalReps;

    }
    public ExerciseSet(Parcel in)
    {

        goalReps = in.readInt();
        doneReps = in.readInt();
        usedWeight = in.readDouble();
        finished = in.readByte() != 0;
    }

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
        ExerciseSet set = new ExerciseSet(this.goalReps);
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
        dest.writeInt(goalReps);
        dest.writeInt(doneReps);
        dest.writeDouble(usedWeight);
        dest.writeByte((byte) (finished ? 1 : 0));
    }
}
