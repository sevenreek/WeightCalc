package com.devseven.gympack.setlogger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity  {

    ExerciseDay dayToContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File exercises = new File(getFilesDir(),GlobalSettings.EXERCISES_STORAGE_FOLDER_NAME);
        if(!exercises.exists())
            exercises.mkdirs();
        File logs = new File(getFilesDir(),GlobalSettings.DIRECTORY_LOGS);
        if(!logs.exists())
            logs.mkdirs();


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.main_menu_content);
            Log.d("CONTENTVIEW", "onCreate: loading non-support");
        }
        else {
            setContentView(R.layout.main_menu_content_support);
            Log.d("CONTENTVIEW", "onCreate: loading support");
        }
        if(!PermissionGranted())
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.STORAGE},requestCode);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View exerciseDayButton = findViewById(R.id.startExerciseButton);
        exerciseDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PickExerciseActivity.class);
                intent.putExtra(PickExerciseActivity.IS_IN_EDIT_MODE, false);
                MainActivity.this.startActivity(intent);
            }
        });
        final View pickProgramButton = findViewById(R.id.viewProgramsButton);
        pickProgramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickProgramButton.setPressed(true);
                Intent intent = new Intent(MainActivity.this,PickExerciseActivity.class);
                intent.putExtra(PickExerciseActivity.IS_IN_EDIT_MODE, true);
                MainActivity.this.startActivity(intent);
            }
        });
        View logsButton = findViewById(R.id.logsButton);
        logsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LogsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        View exerciseList = findViewById(R.id.exercisesList);
        exerciseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ExercisesListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        MobileAds.initialize(this,  "ca-app-pub-2369459734202932~7489310469");

        adView = (AdView) findViewById(R.id.adView);
        if(UserSettings.getUserSettings(this).getShowBanner())
            adView.loadAd(new AdRequest.Builder().build());







    }
    AdView adView;
    @Override
    public void onResume()
    {
        Log.d("CONTINUE","MainActivity.onResume()");
        View quickStart = findViewById(R.id.quickStartButton);
        if(UserSettings.getUserSettings(this).getShowBanner())
            adView.loadAd(new AdRequest.Builder().build());
        else
            adView.destroy();
        // quickStart listener is setup depending on whether there is a log from today
        dayToContinue=null;
        File dayFile = DayToContinue();
        if(dayFile!=null)
        {

            Serializer serializer = new Persister();
            try {
                dayToContinue = serializer.read(ExerciseDay.class, dayFile);
                Log.d("CONTINUE:MainActivity","Found a log from today. The name of file is "+dayFile.getName()+" and the day name is "+dayToContinue.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(dayToContinue!=null)
        {
            GlobalSettings.setDayToOpen(dayToContinue);
            ImageView iv = (ImageView)((LinearLayout)quickStart).getChildAt(0);
            TextView tv = (TextView)((LinearLayout)quickStart).getChildAt(1);
            iv.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            tv.setText(getString(R.string.continue_training));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
            quickStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CONTINUE:MainActivity","Starting Exercise Activity with dayToOpen set as "+dayToContinue.getName());
                    GlobalSettings.setDayToOpen(dayToContinue);
                    Intent intent = new Intent(MainActivity.this,ExerciseActivity.class);
                    intent.putExtra(ExerciseActivity.ACTIVITY_MODE, ExerciseActivity.MODE_CONTINUE);
                    startActivity(intent);
                }
            });
        }
        super.onResume();
    }
    public File DayToContinue()
    {
        File dir = new File(getFilesDir(),GlobalSettings.DIRECTORY_LOGS);
        String compareTo = GlobalSettings.LOGS_DATE_FORMAT.format(Calendar.getInstance().getTime());
        for (String s:dir.list()
             ) {
            if(s.equals(compareTo))
            {
                File f = new File(dir, s);
                return f;
            }
        }
        return null;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
        return false;
    }
}
