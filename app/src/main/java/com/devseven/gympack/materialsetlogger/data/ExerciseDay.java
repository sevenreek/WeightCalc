package com.devseven.gympack.materialsetlogger.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.devseven.gympack.setlogger.ExerciseGroup;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;

/**
 * Created by Dickbutt on 05.06.2017.
 */

public class ExerciseDay implements Parcelable{
    public ExerciseDay(String name)
    {
        exerciseGroups = new ArrayList<com.devseven.gympack.materialsetlogger.data.ExerciseGroup>();
        this.name = name;
    }
    public ExerciseDay(){exerciseGroups = new ArrayList<com.devseven.gympack.materialsetlogger.data.ExerciseGroup>();}
    @ElementList
    public ArrayList<com.devseven.gympack.materialsetlogger.data.ExerciseGroup> exerciseGroups;
    @Attribute
    private String name;
    @Attribute(required = false)
    private boolean usedImperial;

    protected ExerciseDay(Parcel in) {
        exerciseGroups = in.createTypedArrayList(com.devseven.gympack.materialsetlogger.data.ExerciseGroup.CREATOR);
        name = in.readString();
        usedImperial = in.readByte() != 0;
    }

    public static final Creator<ExerciseDay> CREATOR = new Creator<ExerciseDay>() {
        @Override
        public ExerciseDay createFromParcel(Parcel in) {
            return new ExerciseDay(in);
        }

        @Override
        public ExerciseDay[] newArray(int size) {
            return new ExerciseDay[size];
        }
    };

    public ArrayList<com.devseven.gympack.materialsetlogger.data.ExerciseGroup> getExerciseGroups() {
        return exerciseGroups;
    }

    public void setExerciseGroups(ArrayList<com.devseven.gympack.materialsetlogger.data.ExerciseGroup> exerciseGroups) {
        this.exerciseGroups = exerciseGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUsedImperial() {
        return usedImperial;
    }

    public void setUsedImperial(boolean usedImperial) {
        this.usedImperial = usedImperial;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(exerciseGroups);
        dest.writeString(name);
        dest.writeByte((byte) (usedImperial ? 1 : 0));
    }
}
