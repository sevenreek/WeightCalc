package com.devseven.gympack.setlogger;

import android.content.Context;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;

/**
 * Created by Dickbutt on 05.06.2017.
 */

public class ExerciseDay {
    public  ExerciseDay(String name)
    {
        this.name = name;
    }
    public ExerciseDay(){}
    @ElementList
    public ArrayList<ExerciseGroup> exerciseGroups = new ArrayList<ExerciseGroup>();
    @Attribute
    private String name;
    @Attribute(required = false)
    private String programName;
    @Attribute(required = false)
    private boolean _usedImperial;
    public void setUsedImperial(boolean tf)
    {
        _usedImperial = tf;
    }
    public boolean usedImperial()
    {
        return _usedImperial;
    }
    public void SetProgamName(String na)
    {
           programName = na;
    }
    public String getProgramName()
    {
        return programName;
    }
    public ExerciseDay clone(Context context)
    {
        ExerciseDay day = new ExerciseDay(this.name);
        for (ExerciseGroup eg:exerciseGroups
             ) {
            day.exerciseGroups.add(eg.clone(context));
        }
        return day;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String s)
    {
        name = s;
    }


}
