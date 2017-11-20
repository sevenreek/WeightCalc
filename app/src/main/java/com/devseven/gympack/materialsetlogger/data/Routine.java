package com.devseven.gympack.materialsetlogger.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.devseven.gympack.materialsetlogger.data.ExerciseDay;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by Dickbutt on 08.06.2017.
 */
@Root
public class Routine implements Parcelable {
    @ElementList
    public ArrayList<ExerciseDay> days;
    @Attribute
    private String name;
    @Element(required = false)
    String description;

    protected Routine(Parcel in) {
        name = in.readString();
        description = in.readString();
    }

    public static final Creator<Routine> CREATOR = new Creator<Routine>() {
        @Override
        public Routine createFromParcel(Parcel in) {
            return new Routine(in);
        }

        @Override
        public Routine[] newArray(int size) {
            return new Routine[size];
        }
    };

    public ArrayList<ExerciseDay> getDays() {
        return days;
    }

    public void setDays(ArrayList<ExerciseDay> days) {
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
    }
}
