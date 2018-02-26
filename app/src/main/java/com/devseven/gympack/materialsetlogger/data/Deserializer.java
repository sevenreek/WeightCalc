package com.devseven.gympack.materialsetlogger.data;

import android.content.Context;
import android.util.Log;

import com.devseven.gympack.materialsetlogger.ApplicationState;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;

/**
 * Created by Klejnot Nilu on 09.11.2017.
 */

public class Deserializer {
    private static Deserializer _instance;
    private Serializer serializer;
    public static final String SKETCHDIR = "sketches";
    public static final String ROUTINESDIR = "routines";
    public static final String EXERCISESDIR  = "exercises";
    public static final String SETTINGSFILE  = "settings";
    File routinesDirectory;
    File exercisesDirectory;
    File sketchesDirectory;
    File settingsFile;
    private Deserializer(Context context){
        serializer = new Persister();
        routinesDirectory = new File(context.getFilesDir(), ROUTINESDIR);
        exercisesDirectory = new File(context.getFilesDir(), EXERCISESDIR);
        sketchesDirectory = new File(context.getFilesDir(), SKETCHDIR);
        settingsFile = new File(context.getFilesDir(), SETTINGSFILE);
    };
    public static Deserializer getInstance(Context context)
    {
        if(_instance==null)
        {
            _instance = new Deserializer(context);
        }
        return _instance;
    }
    public ApplicationState loadSettings() throws Exception {
        return serializer.read(ApplicationState.class, settingsFile);
    }

    public Routine getRoutine(String name) throws Exception {
        return serializer.read(Routine.class,new File(routinesDirectory, name));
    }

    public RoutineSketch[] getSketches() throws Exception {

        File routineDir = routinesDirectory;
        File[] flist = routineDir.listFiles();
        RoutineSketch[] sketches = new RoutineSketch[flist.length];
        Log.d(this.toString(),"Sketches length = "+flist.length);
        for (int i = 0; i<flist.length; i++)
        {
             sketches[i] = (serializer.read(RoutineSketch.class, flist[i]));
        }
        return sketches;
    }
    public Exercise[] getExercises() throws Exception {
        File exerciseDir = exercisesDirectory;
        File[] flist = exerciseDir.listFiles();
        Exercise[] exercises = new Exercise[flist.length];
        for(int i = 0; i<flist.length; i++)
        {
            exercises[i] = serializer.read(Exercise.class,flist[i]);
        }
        return exercises;
    }
}
