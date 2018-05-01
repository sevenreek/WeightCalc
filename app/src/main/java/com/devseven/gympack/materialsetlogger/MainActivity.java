package com.devseven.gympack.materialsetlogger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.devseven.gympack.materialsetlogger.data.Deserializer;
import com.devseven.gympack.materialsetlogger.data.Routine;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class MainActivity extends AppCompatActivity  {
    // might wrap these in a holder like BuilderViewHolder
    AdView adView;
    int continuedDay;
    Routine continuedRoutine;
    TextView currentRoutine;
    TextView currentDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!areIOPermissionsGranted())
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE},requestCode);
        }
        final File exercises = new File(getFilesDir(), Deserializer.EXERCISESDIR);           // This checks whether all folders neccessary
        final File sketches = new File(getFilesDir(), Deserializer.SKETCHDIR);               // to write save data files are present
                   // If they are not it creates them.
        final File routines = new File(getFilesDir(), Deserializer.ROUTINESDIR);
        if(!exercises.exists())

        if(!sketches.exists())
            sketches.mkdirs();
        if(!routines.exists())
            routines.mkdirs();


        for(File f: sketches.listFiles())   // For testing purposes, I remove all files that were logged in the previous
            f.delete();                     // session.
        for(File f: exercises.listFiles())  // TODO Eventually remove the code resposible for delete all files when testing is finished.
            f.delete();

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setContentView(R.layout.main_menu);/// CONTENT VIEW. FINDVIEW WILL CRASH BEFORE THIS LINE //////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        currentRoutine = (TextView) findViewById(R.id.currentRoutine);
        currentDay = (TextView) findViewById(R.id.nextDay);

        // load settings from file
        try {
            String name =  ApplicationState.getInstance(this).getCurrentRoutine();
            continuedDay = ApplicationState.getInstance(this).getCurrentDay();
            if(!name.isEmpty())
                continuedRoutine = Deserializer.getInstance(this).getRoutine(name);
            else
                onNoContinuedRoutine();
        } catch (Exception e) {
            e.printStackTrace();
            onNoContinuedRoutine();
        }
        if(continuedRoutine!=null)
        {
            currentRoutine.setText(continuedRoutine.getName());
            currentDay.setText(continuedDay==-1?getString(R.string.no_active_day):continuedRoutine.days.get(continuedDay).getName());
        }
        else
        {
            onNoContinuedRoutine();
        }
        //region TOOLBAR INITIALIZATION
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //endregion
        View menuButton = findViewById(R.id.routinesMenu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RoutineBuilderActivity.class);
                startActivity(i);
            }
        });
        View continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ExercisePlayerActivity.class);
                startActivity(i);
            }
        });




    }
    private void onNoContinuedRoutine()
    {
        currentRoutine.setText(getString(R.string.no_active_routine));
        currentDay.setText(getString(R.string.no_active_day));
    }
    @Override
    public void onResume()
    {
        super.onResume();

    }

    public boolean areIOPermissionsGranted() {
        /** The function areIOPermissionsGranted() returns true if the user has already granted all the permissions necessary for
         *  the proper operation of the application, i.e. read/write files to external and internal storage. The application stores
         *  routine files, logs and exercise data.
         */
        int permission_write_external = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read_external = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission_storage = ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE);
        if (permission_read_external == PackageManager.PERMISSION_DENIED) {
            Log.e(this.toString(), "PERMISSION ERROR. READ EXTERNAL NOT GRANTED.");
            return false;
        } else if (permission_write_external == PackageManager.PERMISSION_DENIED) {
            Log.e(this.toString(), "PERMISSION ERROR. WRITE EXTERNAL NOT GRANTED.");
            return false;

        } else if(permission_storage == PackageManager.PERMISSION_DENIED)
        {
            Log.e(this.toString(),"PERMISSION ERROR. STORAGE NOT GRANTED");
            return false;
        }
        else {
            Log.d(this.toString(), "Permissions granted.");
            return true;
        }

    }
    int requestCode = 100; // I don't actaully remember what this was for...
}
