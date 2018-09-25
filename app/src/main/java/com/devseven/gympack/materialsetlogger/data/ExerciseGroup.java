package com.devseven.gympack.materialsetlogger.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;

/**
 * Created by Dickbutt on 26.06.2017.
 */

public class ExerciseGroup implements Parcelable {
    @Attribute
    private String exerciseName;
    @ElementList
    public ArrayList<com.devseven.gympack.materialsetlogger.data.ExerciseSet> exerciseSets;
    private Exercise exercise;
    public ExerciseGroup()
    {}

    public ExerciseGroup(String exerciseName)
    {
        this.exerciseName = exerciseName;
        exerciseSets = new ArrayList<com.devseven.gympack.materialsetlogger.data.ExerciseSet>();
    }


    protected ExerciseGroup(Parcel in) {
        exerciseName = in.readString();
        exerciseSets = in.createTypedArrayList(ExerciseSet.CREATOR);
        exercise = in.readParcelable(Exercise.class.getClassLoader());
    }

    public static final Creator<ExerciseGroup> CREATOR = new Creator<ExerciseGroup>() {
        @Override
        public ExerciseGroup createFromParcel(Parcel in) {
            return new ExerciseGroup(in);
        }

        @Override
        public ExerciseGroup[] newArray(int size) {
            return new ExerciseGroup[size];
        }
    };

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public ArrayList<ExerciseSet> getExerciseSets() {
        return exerciseSets;
    }

    public void setExerciseSets(ArrayList<ExerciseSet> exerciseSets) {
        this.exerciseSets = exerciseSets;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
        this.exerciseName = exercise.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exerciseName);
        dest.writeTypedList(exerciseSets);
        dest.writeParcelable(exercise, flags);
    }
}
