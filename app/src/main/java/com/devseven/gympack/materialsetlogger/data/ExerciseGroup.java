package com.devseven.gympack.materialsetlogger.data;

import android.content.Context;
import android.media.audiofx.BassBoost;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.devseven.gympack.materialsetlogger.ApplicationState;

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
    Exercise exercise;
    public ExerciseGroup(String exerciseName, ArrayList<Exercise> exerciseList, Context context)
    {
        for (Exercise e: ApplicationState.getInstance(context).getExerciseList())
        {
            if(e.getName() == exerciseName)
            {
                exercise = e;
                this.exerciseName = exerciseName;
                break;
            }
        }
        if(exercise == null) {
            Log.d("DEBUG3:","FAILURE: CREATING NEW EXERCISE WITH NAME: "+exerciseName);
            exercise = new Exercise(exerciseName);// TODO IMPORTANT: serialize the new exercise and add to appstate
            this.exerciseName = exerciseName;

        }
        Log.d("DEBUG3:","FAILURE: CONFIRMING NAME: "+exercise.getName());
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
