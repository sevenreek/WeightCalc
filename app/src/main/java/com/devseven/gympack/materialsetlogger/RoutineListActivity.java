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
import com.devseven.gympack.materialsetlogger.data.RoutineSketch;
import com.devseven.gympack.setlogger.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RoutineListActivity extends AppCompatActivity {

    public static final String SKETCH_DIR = "SKETCH_DIR";
    public static final String ROUTINES_DIR = "ROUTINE_DIR";
    private File sketchDirectory;
    private File routineDirectory;
    RecyclerView recyclerView;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_list);
        fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        if (intent != null) {

        } else if (savedInstanceState != null) {

        } else {
            // If neither intent nor savedInstanceState is present then clearly something went wrong.
            // The activity cannot progress without the arguments and will be terminated.
            Toast.makeText(this, "FATAL ERROR: Cannot load intent/savedInstanceState from context. Please contact the developer with this error.", Toast.LENGTH_LONG).show();
            finish();
        }
        RoutineSketch sketch = new RoutineSketch("Routine 1", "Test Routine", 3);
        RoutineSketch sketch1 = new RoutineSketch("Routine 2", "Test Routine", 3);
        RoutineSketch sketch2 = new RoutineSketch("Routine 3", "Test Routine", 3);
        Serializer serializer = new Persister();
        try {
            serializer.write(sketch, new File(sketchDirectory, "testsketch.xml"));
            serializer.write(sketch1, new File(sketchDirectory, "testsketch1.xml"));
            serializer.write(sketch2, new File(sketchDirectory, "testsketch2.xml"));
            Log.d("TESTESTEST","\n\n\n\nSaving to: "+sketchDirectory.getAbsolutePath());
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
            sketches = Deserializer.getInstance().getSketches(RoutineListActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RoutineRecycler adapter = new RoutineRecycler(sketches, fragmentManager);
        recyclerView.setAdapter(adapter);
    }
}
