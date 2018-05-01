package com.devseven.gympack.materialsetlogger;

import android.content.Context;

import com.devseven.gympack.materialsetlogger.data.Deserializer;
import com.devseven.gympack.materialsetlogger.data.Exercise;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dickbutt on 08.02.2018.
 */
@Root
public class ApplicationState {
    public final static String DAY_TO_PASS = "PASSDAYBUNDLE";
    private static ApplicationState _instance;
    private ArrayList<Exercise> exercises;
    public static ApplicationState getInstance(Context context) {
        if (_instance == null) {
            try {
                _instance = Deserializer.getInstance(context).loadSettings(); // TODO load settings from file
            } catch (Exception e) {
                _instance = new ApplicationState();
                e.printStackTrace();
            }
            _instance.InitializeExercises(context);
        }
        return _instance;
    }
    private ApplicationState()
    {

    }

    public void InitializeExercises(Context context)
    {
        try {
            exercises = new ArrayList<Exercise>(Arrays.asList(Deserializer.getInstance(context).getExercises()));
        } catch (Exception e) {
            exercises = new ArrayList<Exercise>();
            e.printStackTrace();
        }
    }

    @Element
    private int defaultSetReps = 12;
    @Element
    private String currentRoutine;
    @Element
    private int currentDay;

    public int getDefaultSetReps() {
        return defaultSetReps;
    }
    public ArrayList<Exercise> getExerciseList() {
        return exercises;
    }

    public String getCurrentRoutine() {
        return currentRoutine;
    }

    public void setCurrentRoutine(String currentRoutine) {
        this.currentRoutine = currentRoutine;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }
}
