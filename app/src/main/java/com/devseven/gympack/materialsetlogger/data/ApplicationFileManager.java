package com.devseven.gympack.materialsetlogger.data;

import android.content.Context;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by Klejnot Nilu on 09.11.2017.
 */

public class ApplicationFileManager {

    private Serializer serializer;
    private static final String DIR_SKETCHES = "sketches";
    private static final String DIR_ROUTINES = "routines";
    private static final String DIR_EXERCISES = "exercises";
    private static final String DIR_SETTINGS = "settings";
    private static final String DIR_LOGS  = "logs";
    private static final String LOG_DATE_FORMAT = "YYYY-MM-DD";
    SimpleDateFormat dateForrmat = new SimpleDateFormat(LOG_DATE_FORMAT);
    public ApplicationFileManager(Context context){
        initialize(context);
    }

    public boolean buildDirectorires(Context context)
    {
        boolean returnValue = true;
        File routinesDirectory   = new File(context.getFilesDir(), DIR_ROUTINES  );
        if(!routinesDirectory.exists()) {
            returnValue = (returnValue && routinesDirectory.mkdirs());
        }
        return returnValue;
    }
    public Routine getRoutine(String name, Context context) throws Exception {
        File routinesDirectory   = new File(context.getFilesDir(), DIR_ROUTINES  );
        return serializer.read(Routine.class,new File(routinesDirectory, name));
    }
    private void initialize(Context context)
    {
        serializer = new Persister();
        File exercisesDirectory  = new File(context.getFilesDir(), DIR_EXERCISES );
        File sketchesDirectory   = new File(context.getFilesDir(), DIR_SKETCHES  );
        for(File f: sketchesDirectory.listFiles())   // For testing purposes, I remove all files that were logged in the previous
            f.delete();                     // session.
        for(File f: exercisesDirectory.listFiles())  // TODO Eventually remove the code responsible for deleting all files when testing is finished.
            f.delete();
    }
}
