package com.devseven.gympack.materialsetlogger;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.devseven.gympack.materialsetlogger.data.Deserializer;
import com.devseven.gympack.materialsetlogger.data.Exercise;
import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.materialsetlogger.data.ExerciseGroup;
import com.devseven.gympack.materialsetlogger.data.ExerciseSet;
import com.devseven.gympack.materialsetlogger.data.Routine;
import com.devseven.gympack.materialsetlogger.data.RoutineSketch;
import com.devseven.gympack.setlogger.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// It is possible that turning this into a fragment of MainActivity may be a good decision.
public class RoutineListActivity extends AppCompatActivity {

    public static final String SKETCH_DIR = "sketches";
    public static final String ROUTINES_DIR = "routines";
    private File sketchDirectory;
    private File routineDirectory;
    RecyclerView recyclerView;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sketchDirectory = new File(getFilesDir(), Deserializer.SKETCHDIR);
        routineDirectory = new File(getFilesDir(), Deserializer.ROUTINESDIR);
        setContentView(R.layout.activity_routine_list);
        fragmentManager = getSupportFragmentManager();


        // read xml test
        RoutineSketch sketch = new RoutineSketch("Routine 1", "Test Routine", 3);
        ExerciseDay day = new ExerciseDay("test day");
        ExerciseGroup eg = new ExerciseGroup();
        eg.exerciseSets.add(new ExerciseSet(10,30));
        eg.exerciseSets.add(new ExerciseSet(10,30));
        eg.exerciseSets.add(new ExerciseSet(10,30));
        eg.setExercise(new Exercise("Squat"));
        day.exerciseGroups.add(eg);
        day.exerciseGroups.add(eg);
        Routine r1 = new Routine();
        r1.setName(sketch.getName());
        r1.days.add(day);
        ExerciseDay day2 = new ExerciseDay("test day 2");
        eg.exerciseSets.add(new ExerciseSet(10,30));
        eg.exerciseSets.add(new ExerciseSet(10,30));
        eg.exerciseSets.add(new ExerciseSet(10,30));
        eg.setExercise(new Exercise("Squat"));
        day2.exerciseGroups.add(eg);
        day2.exerciseGroups.add(eg);
        Routine r2 = new Routine();
        r2.setName(sketch.getName());
        r1.days.add(day2);


        //TODO TEST DESERIALIZER
        Serializer serializer = new Persister();
        try {
            serializer.write(sketch, new File(sketchDirectory, sketch.getName()));
            serializer.write(r1, new File(routineDirectory, sketch.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        // Foreach routine in directory list programs
        RoutineSketch[] sketches = null;
        try {
            sketches = Deserializer.getInstance(this).getSketches(RoutineListActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RoutineRecycler adapter = new RoutineRecycler(sketches, fragmentManager);
        recyclerView.setAdapter(adapter);
    }
}
