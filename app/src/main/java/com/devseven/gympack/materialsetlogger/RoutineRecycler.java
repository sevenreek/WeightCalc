package com.devseven.gympack.materialsetlogger;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devseven.gympack.materialsetlogger.data.RoutineSketch;
import com.devseven.gympack.setlogger.ExerciseActivity;
import com.devseven.gympack.setlogger.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Klejnot Nilu on 27.10.2017.
 */

public class RoutineRecycler extends RecyclerView.Adapter<RoutineRecycler.RoutineHolder> {

    private ArrayList<RoutineSketch> routineList;

    public RoutineRecycler(ArrayList<RoutineSketch> list)
    {
        this.routineList = list;
    }
    // The recycler can be passed either the serialized list of sketches or a directory which it will deserialize
    // The latter is preferred to seperate activity logic from business logic
    public RoutineRecycler(File dir)
    {
        Serializer serializer = new Persister();
        routineList =  new ArrayList<>();
        for (File file : dir.listFiles())
        {
            try
            {
                Log.d("TESTESTEST","\n\n\n\nReading from: "+file.getAbsolutePath());
                routineList.add(
                    serializer.read(RoutineSketch.class, file)
                );
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    public RoutineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.material_routine_card, parent, false);
        return new RoutineHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RoutineHolder holder, int position) {
        RoutineSketch sketch = routineList.get(position);
        holder.vName.setText(sketch.getName());
        holder.vDays.setText(sketch.getDayCount()
            + (sketch.getDayCount()>1?
                holder.context.getString(R.string.days_amount_plural):
                holder.context.getString(R.string.days_amount_singular)));
        holder.vDescription.setText(sketch.getDescription());
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }
    public static class RoutineHolder extends RecyclerView.ViewHolder {
        protected static Context context;
        protected TextView vName;
        protected TextView vDays;
        protected TextView vDescription;
        protected TextView editRoutine;
        protected TextView startRoutine;
        public RoutineHolder(View v)
        {
            super(v);
            context = v.getContext();
            vName = (TextView) v.findViewById(R.id.routineName);
            vDays = (TextView) v.findViewById(R.id.daysText);
            vDescription = (TextView) v.findViewById(R.id.descriptionText);
            editRoutine = (TextView) v.findViewById(R.id.editRoutine);
            startRoutine = (TextView) v.findViewById(R.id.startRoutine);
            startRoutine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ExerciseActivity.class);

                }
            });
        }
    }



}
