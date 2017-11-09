package com.devseven.gympack.materialsetlogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.devseven.gympack.materialsetlogger.data.RoutineSketch;
import com.devseven.gympack.setlogger.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RoutineListActivity extends AppCompatActivity {

    public static final String ROUTINES_DIR = "ROUTINES_DIR";
    public static final String ROUTINES_DETAIL_DIR = "routine_details";
    private File routinesDirectory;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_list);
        Intent intent = getIntent();
        if (intent != null) {
            routinesDirectory = new File(intent.getStringExtra(ROUTINES_DIR));
        } else if (savedInstanceState != null) {
            routinesDirectory = new File(intent.getStringExtra(ROUTINES_DIR));
        } else {
            // If neither intent nor savedInstanceState is present then clearly something went wrong.
            // The activity cannot progress without the arguments and will be terminated.
            Toast.makeText(this, "FATAL ERROR: Cannot load intent/savedInstanceState from context. Please contact the developer with this error.", Toast.LENGTH_LONG).show();
            finish();
        }
        RoutineSketch sketch = new RoutineSketch("Routine 1", "Test Routine", 3);
        Serializer serializer = new Persister();
        try {
            serializer.write(sketch, new File(routinesDirectory, "testsketch.xml"));
            Log.d("TESTESTEST","\n\n\n\nSaving to: "+routinesDirectory.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        // Foreach routine in directory list programs
        RoutineRecycler adapter = new RoutineRecycler(routinesDirectory);
        recyclerView.setAdapter(adapter);

    }


}
