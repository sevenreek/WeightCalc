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
import java.util.List;

/**
 * Created by Dickbutt on 08.06.2017.
 */
@Root
public class Routine implements Parcelable {
    public static final String ROUTINE_PARCEL = "routine_parcel";
    @ElementList
    public List<ExerciseDay> days;
    @Attribute
    private String name;
    @Element(required = false)
    String description;
    public Routine()
    {
        days = new ArrayList<>();
    }
    public Routine(String name)
    {
        days = new ArrayList<>(); this.name = name;
    }


    protected Routine(Parcel in) {
        days = in.createTypedArrayList(ExerciseDay.CREATOR);
        name = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(days);
        dest.writeString(name);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public List<ExerciseDay> getDays() {
        return days;
    }

    public void setDays(List<ExerciseDay> days) {
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


}
