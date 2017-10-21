package com.devseven.gympack.setlogger;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ExercisesListActivity extends AppCompatActivity {
    ConstraintLayout parent;
    ArrayList<Exercise> exercises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_list);
        final ListView listView = (ListView) findViewById(R.id.listView);
        parent = (ConstraintLayout) listView.getParent();
        final Serializer serializer = new Persister();
        final File dir = new File(getFilesDir(), GlobalSettings.EXERCISES_STORAGE_FOLDER_NAME);
        exercises = new ArrayList<>();
        int corruptCount = 0;
        for (File f: dir.listFiles()
             ) {
            try {
                exercises.add(serializer.read(Exercise.class,f));
            } catch (Exception e) {
                corruptCount++;
                e.printStackTrace();
            }
        }
        if(corruptCount>0)
            Toast.makeText(this, corruptCount+" exercise files are corrupt.", Toast.LENGTH_SHORT).show();
        final MyListAdapter adapter = new MyListAdapter(exercises, getLayoutInflater(),this,listView,dir,serializer);
        listView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(ExercisesListActivity.this);
                AlertDialog.Builder alert = new AlertDialog.Builder(ExercisesListActivity.this);
                alert.setTitle("Add an exercise");
                alert.setView(editText);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Exercise ex = new Exercise();
                            ex.setName(editText.getText().toString());
                            serializer.write(ex,new File(dir,editText.getText().toString()));
                            exercises.add(ex);
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Toast.makeText(ExercisesListActivity.this, "Failed creating exercise.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }).setNeutralButton("Cancel", null).show();
            }
        });
    }
}


class MyListAdapter extends BaseAdapter {

    private ArrayList<Exercise> exercises;
    private LayoutInflater inflater;
    private Activity activity;
    ListView listView;
    File exerciseDir;
    Serializer serializer;
    private MyListAdapter me;
    public class Holder
    {
        TextView name;
        TextView weight;
        ImageView icon;
    }

    public MyListAdapter(ArrayList<Exercise> exercisesCollection, LayoutInflater layoutInflater, Activity activity, ListView myLV, File exerciseDirectory, Serializer serializer)
    {
        exercises = exercisesCollection;
        inflater = layoutInflater;
        this.activity = activity;
        listView = myLV;
        exerciseDir = exerciseDirectory;
        this.serializer = serializer;
        me = this;
    }

    @Override
    public int getCount() {
        return exercises.size();
    }

    @Override
    public Object getItem(int i) {
        return exercises.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Exercise exercise = exercises.get(i);
        final int pos = i;
        final Holder holder = new Holder();
        View rView = inflater.inflate(R.layout.exercise_view, null);
        holder.name = (TextView)((LinearLayout)rView).getChildAt(0);
        //holder.icon = (ImageView)rView.findViewById(R.id.recordIcon);
        holder.weight = (TextView)rView.findViewById(R.id.recordWeight);
        holder.name.setText(exercise.getName());
       // holder.icon.setImageResource(R.drawable.icon_reward_cup);
        DecimalFormat df = new DecimalFormat();
        df.setDecimalSeparatorAlwaysShown(false);
        holder.weight.setText(df.format(exercise.getRecordUsedWeight())+GlobalSettings.getGlobalWeightUnit());
        final File f = new File(exerciseDir,exercise.getName());
        rView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity,view);
                popupMenu.inflate(R.menu.exercise_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.removeExercise:

                                Toast.makeText(activity, "Removing at:"+i+", "+exercises.get(i).getName(), Toast.LENGTH_SHORT).show();
                                exercises.remove(i);
                                me.notifyDataSetChanged();
                                //f.delete();
                                break;
                            case R.id.clearRecord:
                                exercise.setRecordWeight(0);
                                holder.weight.setText(0+GlobalSettings.getGlobalWeightUnit());
                                try {
                                    serializer.write(exercise,f);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        return rView;
    }
}
