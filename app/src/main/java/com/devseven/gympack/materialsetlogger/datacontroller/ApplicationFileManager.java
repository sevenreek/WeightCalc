package com.devseven.gympack.materialsetlogger.datacontroller;

import android.content.Context;

import com.devseven.gympack.materialsetlogger.data.Routine;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

/**
 * Created by Klejnot Nilu on 09.11.2017.
 */

public class ApplicationFileManager {

    private static final String DIR_SKETCHES = "sketches";
    private static final String DIR_ROUTINES = "routines";
    private static final String DIR_EXERCISES = "exercises";
    private static final String DIR_SETTINGS = "settings";
    private static final String DIR_LOGS  = "logs";
    private static final String DIR_LISTS  = "lists";
    private static final String DIR_EXERCISELIST  = "exerciselist";
    private static final String LOG_DATE_FORMAT = "YYYY-MM-DD";
    SimpleDateFormat dateForrmat = new SimpleDateFormat(LOG_DATE_FORMAT);

    public static boolean buildDirectorires(Context context)
    {
        boolean returnValue = true;
        File routinesDirectory   = new File(context.getFilesDir(), DIR_ROUTINES  );
        if(!routinesDirectory.exists()) {
            returnValue = (returnValue && routinesDirectory.mkdirs());
        }
        return returnValue;
    }
    public static String[] getExerciseFileNames(Context context) throws NullPointerException {
        File dir = new File(context.getFilesDir(), DIR_EXERCISES);
        return dir.list();
    }
    public static Routine getRoutine(String name, Context context) throws Exception {
        File routinesDirectory = new File(context.getFilesDir(), DIR_ROUTINES  );
        return new Persister().read(Routine.class,new File(routinesDirectory, name));
    }
    public static String[] getRoutineFilenames(Context context) throws NullPointerException {
        File routinesDirectory = new File(context.getFilesDir(), DIR_ROUTINES  );
        return routinesDirectory.list();
    }
    public static boolean saveRoutine(Routine routine, Context context) throws Exception {
        File routinesDirectory = new File(context.getFilesDir(), DIR_ROUTINES);
        File saveFileLocation = new File(routinesDirectory,routine.getName());
        boolean exists = false;
        if(saveFileLocation.exists())
            exists = true;
        Serializer serializer = new Persister();
        serializer.write(routine,new File(routinesDirectory,routine.getName()));
        return exists;
    }
}
