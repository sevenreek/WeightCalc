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
class UserSettings
{
    private static UserSettings _userSettings;
    private boolean _useImperial = false;
    private int _defaultRest = 90;
    private int _defaultReps = 12;
    private boolean showBanner = true;
    private boolean showInterstitial = true;
    private boolean setToRecord = true;

    public boolean getSetToRecord()
    {
        return setToRecord;
    }
    public boolean getUseImperial()
    {
        return _useImperial;
    }
    public boolean getShowBanner()
    {
        return showBanner;
    }
    public boolean getShowInter()
    {
        return showInterstitial;
    }
    public void setSetToRecord(boolean setToRecord) {
        this.setToRecord = setToRecord;
    }

    public void setShowBanner(boolean showBanner) {
        this.showBanner = showBanner;
    }

    public void setShowInterstitial(boolean showInterstitial) {
        this.showInterstitial = showInterstitial;
    }

    public void set_useImperial(boolean _useImperial) {
        this._useImperial = _useImperial;
    }

    public void set_defaultReps(int _defaultReps) {
        this._defaultReps = _defaultReps;
    }

    public void set_defaultRest(int _defaultRest) {
        this._defaultRest = _defaultRest;
    }

    public int get_defaultRest()
    {
        return _defaultRest;
    }
    public int get_defaultReps()
    {
        return _defaultReps;
    }
    public static UserSettings getUserSettings(Context context)
    {
        if(_userSettings==null)
        {
            File loc = new File(context.getFilesDir(),GlobalSettings.FILENAME_USERSETTINGS);
            Serializer s = new Persister();
            try {
                _userSettings = s.read(UserSettings.class,loc);
            } catch (Exception e) {
                e.printStackTrace();
                _userSettings = new UserSettings();
            }

        }
        return _userSettings;
    }
    public static void SaveUserSettings(Context context)
    {

    }

}