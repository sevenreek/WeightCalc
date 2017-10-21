package com.devseven.gympack.setlogger;

import android.content.Context;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by Dickbutt on 08.06.2017.
 */
@Root
public class Program {
    @ElementList
    public ArrayList<ExerciseDay> days = new ArrayList<ExerciseDay>();
    private boolean isActive;
    ExerciseDay lastDay;
    @Attribute
    private String name = new String();
    @Element(required = false)
    String description = new String();
    public String getName()
    {return name;}
    public void setName(String s)
    {name = s;}
    private boolean deserialized;
    public boolean wasDeserialized()
    {
        return  deserialized;
    }
    public void setDeserialized(boolean tf)
    {
        deserialized = tf;
    }

    public Program clone(Context context)
    {
        Program program = new Program();
        for (ExerciseDay day:this.days
             ) {
            program.days.add(day.clone(context));
        }
        program.name = this.name;
        program.description = this.description;
        return program;
    }
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Program)
        {
            Program program = (Program) o;
            if(!this.name.equals(program.name))
                return false;
            if(!this.description.equals(program.description))
                return false;
            for (ExerciseDay d:this.days // TODO deep equals
                 ) {
                if(!(d==program.days.get(this.days.indexOf(d))))
                {return false; }
            }
            return true;
        }
        else
            throw new IllegalArgumentException("Cannot compare Program to "+o.getClass().toString());
    }


    public ExerciseDay getNextDay()
    {
        if(lastDay==null)
            return days.get(0);
        if(days.indexOf(lastDay)<days.size()-1)
            return days.get(days.indexOf(lastDay)+1);
        else
            return days.get(0);
    }




}
