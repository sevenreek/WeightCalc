package com.devseven.gympack.setlogger;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
// This activity is responsible for picking an exercise to start and picking one to edit.
// The mode is dependent on the boolean isInEditMode
public class PickExerciseActivity extends AppCompatActivity implements DetailExerciseFragment.DetailExerciseInterface  {
// this activity is mostly a holder. it starts pickexercisefragment
    public static final String IS_IN_EDIT_MODE = "IS_IN_EDIT_MODE";
    FragmentManager fragmentManager;
    View fragmentContainer;
    boolean isInEditMode;
    LayoutInflater inflater;
    ArrayList<Program> userPrograms;
    LinearLayout programContainer;
    public static final String filename = "preexisitng_programs"; // FILE TO OPEN
    // /CONFIG


    // A BETTER WAY TO HANDLE THIS WILL HAVE TO BE IMPLEMENTED TODO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = getLayoutInflater();
        if(savedInstanceState!=null) {
            isInEditMode = savedInstanceState.getBoolean(IS_IN_EDIT_MODE, false);
            Log.d(this.toString(), "SavedInstanceState bundle sucessfully restored. "+(isInEditMode?"App is in edit mode":"App is NOT in edit mode."));
        }
        else if (getIntent().getExtras()!=null)
        {
            isInEditMode = getIntent().getExtras().getBoolean(IS_IN_EDIT_MODE);
            Log.d(this.toString(), "Intent extras grabbed. "+(isInEditMode?"App is in edit mode":"App is NOT in edit mode."));

        }
        else
            Log.e(this.toString(), "Warning! There is no savedInstanceState or intent extras recieved is null. Cannot set boolean isInEditMode. The app has no idea in which mode it has to select routines. The default is false - not in Edit Mode. This may not be a desired behaviour!");
        setContentView(R.layout.activity_pick_exercise);
        fragmentManager = getSupportFragmentManager();
        fragmentContainer = findViewById(R.id.pickExerciseContainer);
        programContainer = (LinearLayout) findViewById(R.id.programsContainer);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.addProgramFloatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PickExerciseActivity.this,CreateProgramActivity.class);
                GlobalSettings.setProgramToEdit(null);
                startActivity(intent);
                finish();
            }
        });
        // XML DESERIALIZER
        Serializer serializer = new Persister();
        File userProgramsDir = new File(getFilesDir(), GlobalSettings.DIRECTORY_PROGRAMS);
        Log.d(this.toString(),userProgramsDir.getPath());
        userPrograms = new ArrayList<Program>();
        if(userProgramsDir.exists()) {
            for (File f :  userProgramsDir.listFiles()
                    ) {
                try {
                    final Program p = serializer.read(Program.class, f);
                    Log.d("PickExercise", "onCreate: Found program "+p.getName());
                    userPrograms.add(p);
                    View v;
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP)
                         v = inflater.inflate(R.layout.overview_exercise_routine,programContainer, false);
                    else
                        v = inflater.inflate(R.layout.overview_exercise_routine_support,programContainer, false);
                    TextView programName = (TextView)v.findViewById(R.id.programName);
                    TextView dayCount = (TextView)v.findViewById(R.id.dayAmount);
                    programName.setText(p.getName());
                    ImageView actionIcon = (ImageView)v.findViewById(R.id.actionIcon);
                    dayCount.setText(p.days.size()+" "+(p.days.size()==1?getString(R.string.days_amount_singular):getString(R.string.days_amount_plural)));
                    if(!isInEditMode) {
                        actionIcon.setImageResource(R.drawable.ic_chevron_right_black_24dp);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                DetailExerciseFragment def = new DetailExerciseFragment();
                                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
                                transaction.replace(R.id.pickExerciseContainer, def);
                                transaction.addToBackStack("DETAILFRAGMENT");
                                GlobalSettings.setProgramToEdit(p);
                                transaction.commit();
                                fragmentActive = def;
                            }
                        });
                    }
                    else
                    {
                        actionIcon.setImageResource(R.drawable.ic_edit_black_24dp);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GlobalSettings.setProgramToEdit(p);
                                Intent intent = new Intent(PickExerciseActivity.this,CreateProgramActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                    programContainer.addView(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    Fragment fragmentActive;
    @Override
    protected void onSaveInstanceState(Bundle b)
    {
        b.putBoolean(IS_IN_EDIT_MODE,isInEditMode);
        super.onSaveInstanceState(b);
    }
    @Override
    protected void onRestoreInstanceState(Bundle b)
    {
        isInEditMode = b.getBoolean(IS_IN_EDIT_MODE);
        super.onRestoreInstanceState(b);
    }

// this is the function to open the days fragment and let the user select a day to start exercise.



    @Override
    public void end() {
        finish();
    }


}
