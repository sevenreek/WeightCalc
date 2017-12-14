package com.devseven.gympack.setlogger;

import android.content.Context;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dickbutt on 03.08.2017.
 */

public class GlobalSettings {
    public static final String DIRECTORY_LOGS = "logs";
    public static final SimpleDateFormat LOGS_DATE_FORMAT = new SimpleDateFormat("yy-MM-dd");
    public static final  String EXERCISES_STORAGE_FOLDER_NAME = "exercises";
    public static final String DIRECTORY_SKETCHES = "sketches";
    private static ExerciseDay DAY_TO_OPEN;
    private static Program PROGRAM_TO_EDIT;
    public static final String FILENAME_USERSETTINGS = "usersettings";
    public static final String DIRECTORY_PROGRAMS = "programs";
    public static void setProgramToEdit(Program p)
    {
        PROGRAM_TO_EDIT = p;
    }
    public static Program getProgramToEdit()
    {
        return PROGRAM_TO_EDIT;
    }
    public static void setDayToOpen(ExerciseDay dayToOpen) {
        DAY_TO_OPEN = dayToOpen;
    }
    private static ArrayList<String > exerciseNames;
    public static ArrayList getExerciseNames(Context context)
    {
        if(exerciseNames==null)
        {
            File loc = new File(context.getFilesDir(),GlobalSettings.EXERCISES_STORAGE_FOLDER_NAME);
            exerciseNames = new ArrayList<>(Arrays.asList(loc.list()));
        }
        return exerciseNames;
    }
    public static void InvalidateExerciseNames()
    {
        exerciseNames = null;
    }


    public static ExerciseDay getDayToOpen() {
        return DAY_TO_OPEN;
    }

    public static String getGlobalWeightUnit()
    {
        if(useImperial)
            return "lbs";
        else
            return "kg";
    }
    public static String getWeightUnit(boolean usingImperial, Context context)
    {
        if(usingImperial)
            return context.getString(R.string.weight_unit_lb);
        else
            return context.getString(R.string.weight_unit_kg);
    }
    public static boolean useImperial;



}
