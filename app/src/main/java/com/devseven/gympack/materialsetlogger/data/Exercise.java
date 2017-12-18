package com.devseven.gympack.materialsetlogger.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.devseven.gympack.setlogger.GlobalSettings;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

/**
 * Created by Dickbutt on 05.06.2017.
 */

public class Exercise implements Parcelable{
    public Exercise(String name)
    {
        this.name = name;
    }
    public Exercise(){}
    public Exercise(Parcel in)
    {
        this.name = in.readString();
        this.recordUsedWeight = in.readFloat();
    }
    @Attribute
    String name;
    @Element
    float recordUsedWeight;

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRecordUsedWeight() {
        return recordUsedWeight;
    }

    public void setRecordUsedWeight(float recordUsedWeight) {
        this.recordUsedWeight = recordUsedWeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeFloat(recordUsedWeight);
    }
}
