package com.devseven.gympack.materialsetlogger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.devseven.gympack.materialsetlogger.data.Deserializer;
import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.setlogger.GlobalSettings;
import com.devseven.gympack.setlogger.R;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class MainActivity extends AppCompatActivity  {

    AdView adView;
    ExerciseDay dayToContinue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final File exercises = new File(getFilesDir(), GlobalSettings.EXERCISES_STORAGE_FOLDER_NAME);   // This checks whether all folders neccessary
        final File sketches = new File(getFilesDir(), GlobalSettings.DIRECTORY_SKETCHES);               // to write save data files are present
        final File logs = new File(getFilesDir(),GlobalSettings.DIRECTORY_LOGS);                        // If they are not it creates them.
        final File routines = new File(getFilesDir(), Deserializer.ROUTINESDIR);
        if(!exercises.exists())
            exercises.mkdirs();
        if(!logs.exists())
            logs.mkdirs();
        if(!sketches.exists())
            sketches.mkdirs();
        if(!routines.exists())
            routines.mkdirs();

        // TESTING ONLY
        for(File f: sketches.listFiles())
            f.delete();

        setContentView(R.layout.main_menu);

        // At run time the program checks for read/write permission
        if(!PermissionGranted())
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE},requestCode);
        }
        //region TOOLBAR INITIALIZATION
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //endregion




    }

    @Override
    public void onResume()
    {
        super.onResume();

    }
    public boolean PermissionGranted() {
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
    int requestCode = 100;
}
