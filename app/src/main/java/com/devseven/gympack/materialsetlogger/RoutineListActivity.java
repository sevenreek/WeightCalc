package com.devseven.gympack.materialsetlogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.devseven.gympack.setlogger.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;


public class RoutineListActivity extends AppCompatActivity {

    public static final String ROUTINES_DIR = "ROUTINES_DIR";
    public static final String ROUTINES_DETAIL_DIR = "routine_details";
    private File routinesDirectory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_list);
        Intent intent = getIntent();
        if(intent!=null)
        {
            routinesDirectory = new File(intent.getStringExtra(ROUTINES_DIR));
        }
        else if(savedInstanceState != null)
        {
            routinesDirectory = new File(intent.getStringExtra(ROUTINES_DIR));
        }
        else
        {
            // If neither intent nor savedInstanceState is present then clearly something went wrong.
            // The activity cannot progress without the arguments and will be terminated.
            Toast.makeText(this, "FATAL ERROR: Cannot load intent/savedInstanceState from context. Please contact the developer with this error.", Toast.LENGTH_LONG).show();
            finish();
        }
        // Foreach routine in directory list programs
        for(File f:routinesDirectory.listFiles())
        {
            Serializer serializer = new Persister();
        }
    }


    // A class that contains basic routine data for faster xml deserialization:
    // - name
    // - day amount
    class RoutineSketch {
        String name;
        int dayCount;
    }
}
