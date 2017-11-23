package com.devseven.gympack.materialsetlogger.data;

import android.content.Context;

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
    private Deserializer(){
        serializer = new Persister();
    };
    public static Deserializer getInstance()
    {
        if(_instance==null)
        {
            _instance = new Deserializer();
        }
        return _instance;
    }
    public static final String SKETCHDIR = "sketches";
    public static final String ROUTINESDIR = "routines";
    public static final String EXERCISESDIR  = "exercises";
    public Routine GetRoutine(Context context, String name) throws Exception {
        File directory = new File(context.getFilesDir(),ROUTINESDIR);
        return serializer.read(Routine.class,new File(directory, name));
    }

    public RoutineSketch[] getSketches(Context context) throws Exception {

        File routineDir = new File(context.getFilesDir(), SKETCHDIR);
        File[] flist = routineDir.listFiles();
        RoutineSketch[] sketches = new RoutineSketch[flist.length];
        for (int i = 0; i<flist.length; i++
             ) {
             sketches[i] = (serializer.read(RoutineSketch.class, flist[i]));
        }
        return sketches;
    }
}
