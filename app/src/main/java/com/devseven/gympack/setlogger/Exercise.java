package com.devseven.gympack.setlogger;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

/**
 * Created by Dickbutt on 05.06.2017.
 */

public class Exercise {
    public Exercise(String name, @Nullable String[] targetedBodyParts)
    {
        this.name = name;
    }
    public static File exerciseStorageDir;


    public static Exercise findExerciseByName(String name, Context context, boolean createIfNotFound)
    {
        Serializer serializer = new Persister();
        Exercise exercise = null;
        if(exerciseStorageDir == null) {
            Log.d("EXERCISE_DESERIALZER","Storage Dir is null");
            File exerciseStorage = new File(context.getFilesDir(),GlobalSettings.EXERCISES_STORAGE_FOLDER_NAME);
            if(!exerciseStorage.exists())
            {
                Log.d("EXERCISE_DESERIALZER","Exercise Storage does not exist.");
                exerciseStorage.mkdirs();
            }
            exerciseStorageDir=exerciseStorage;
        }
        for (String s:exerciseStorageDir.list()
             ) {
            if(s.equals(name))
            {
                Log.d("EXERCISE_DESERIALZER","Found file with name "+name+", as "+s);
                try {
                   exercise = serializer.read(Exercise.class,new File(exerciseStorageDir,s));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(exercise==null)
        {
            if(createIfNotFound)
            {
                Log.d("EXERCISE_DESERIALZER","Exercise("+name+") not found. Creating.");
                exercise = new Exercise(name, null);
                exercise.setRecordWeight(0);
                try {
                    serializer.write(exercise,new File(exerciseStorageDir,name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("EXERCISE_DESERIALZER", "Exercise(" + name + ") not found! Cannot continue!");
                return null;
            }
        }
        Log.e("EXERCISE_DESERIALIZER","Returning exercise: "+exercise.getName());
        return exercise;

    }

    public Exercise(){}
    @Element
    String name = new String();
    @Element
    double recordUsedWeight;

    public double getRecordUsedWeight()
    {
        return recordUsedWeight;
    }
    public void setRecordUsedWeight(double W)
    {
        recordUsedWeight = W;
    }

    public void  setRecordWeight(double weight)
    {
        recordUsedWeight = weight;
    }
    @Element(required = false)
    int[] targetedBodyParts;
    public String getName()
    {
        return name;
    }
    public void setName(String nm)
    {
        name = nm;
    }
    public int[] getTargetedBodyPart()
    {
        return targetedBodyParts;
    }
    public void setTargetedBodyPart(int[] bodyPart)
    {
        targetedBodyParts = bodyPart;
    }

    public static final int BODYPART_LEGS = 0;
    public static final int BODYPART_CHEST = 1;
    public static final int BODYPART_BICEP = 2;
    public static final int BODYPART_TRICEP = 3;
    public static final int BODYPART_ABS = 4;
    public static final int BODYPART_LOWERBACK = 5;
    public static final int BODYPART_UPPERBACK = 6;
    public static final int BODYPART_WRIST = 7;
}
