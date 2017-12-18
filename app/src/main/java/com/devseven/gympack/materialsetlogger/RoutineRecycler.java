package com.devseven.gympack.materialsetlogger;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devseven.gympack.materialsetlogger.data.Deserializer;
import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.materialsetlogger.data.ExerciseGroup;
import com.devseven.gympack.materialsetlogger.data.Routine;
import com.devseven.gympack.materialsetlogger.data.RoutineSketch;
import com.devseven.gympack.materialsetlogger.ExerciseActivity;
import com.devseven.gympack.setlogger.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Klejnot Nilu on 27.10.2017.
 */

public class RoutineRecycler extends RecyclerView.Adapter<RoutineRecycler.RoutineHolder> {

    private List<RoutineSketch> routineList;
    protected static Context context;
    protected static FragmentManager fragmentManager;
    public RoutineRecycler(RoutineSketch[] list, FragmentManager fm)
    {
        this.routineList = Arrays.asList(list);
        fragmentManager = fm;
    }
    // The recycler can be passed either the serialized list of sketches or a directory which it will deserialize
    // The latter is preferred to seperate activity logic from business logic
    /*public RoutineRecycler(File dir)
    {
        fragmentManager = fm;
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

    }*/

    @Override
    public RoutineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.material_routine_card, parent, false);
        return new RoutineHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RoutineHolder holder, int position) {
        final RoutineSketch sketch = routineList.get(position);
        holder.vName.setText(sketch.getName());
        holder.vDays.setText(sketch.getDayCount()
            + (sketch.getDayCount()>1?
                context.getString(R.string.days_amount_plural):
                context.getString(R.string.days_amount_singular)));
        holder.vDescription.setText(sketch.getDescription());
        holder.startRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // insert fragment and dim screen
                try {
                    RoutineHolder.DaySelector selector =
                            RoutineHolder.DaySelector.newInstance(
                                    Deserializer.getInstance(context).GetRoutine(sketch.getName()));
                    selector.show(fragmentManager,"dialog");

                } catch (Exception e) {
                    Toast.makeText(context, "ERROR:E000. Cannot deserialize file. Contact the developer.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }
    public static class RoutineHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vDays;
        protected TextView vDescription;
        protected TextView editRoutine;
        protected TextView startRoutine;
        public RoutineHolder(View v)
        {
            super(v);
            if(context==null)
                context = v.getContext();
            vName = (TextView) v.findViewById(R.id.routineName);
            vDays = (TextView) v.findViewById(R.id.daysText);
            vDescription = (TextView) v.findViewById(R.id.descriptionText);
            editRoutine = (TextView) v.findViewById(R.id.editRoutine);
            startRoutine = (TextView) v.findViewById(R.id.startRoutine);

        }
        public static class DayHolder
        {

            public DayHolder(View v, ExerciseDay day)
            {
                TextView name = (TextView) v.findViewById(R.id.dayName);
                TextView setCount = (TextView) v.findViewById(R.id.setCount);
                TextView exerciseCount = (TextView) v.findViewById(R.id.exerciseCount);
                TextView shortDesc = (TextView) v.findViewById(R.id.bodyParts);
                name.setText(day.getName());
                exerciseCount.setText(day.exerciseGroups.size()+" exercises");
                int setSum = 0;
                for (ExerciseGroup g:day.exerciseGroups
                     ) {
                    setSum+=g.exerciseSets.size();
                }
                setCount.setText(setSum+" sets");
                shortDesc.setText(""); //TODO short descriptor
            }
        }

        public static class DaySelector extends DialogFragment {
            public static final String ROUTINE_TO_PASS = "ROUTINE_PASS";
            Routine routine;
            public static DaySelector newInstance(Routine passRoutine) {

                Bundle args = new Bundle();
                args.putParcelable(ROUTINE_TO_PASS,passRoutine);
                DaySelector fragment = new DaySelector();
                fragment.setArguments(args);
                return fragment;
            }
            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                routine = getArguments().getParcelable(ROUTINE_TO_PASS);
                View fragView = inflater.inflate(R.layout.material_day_container, container, false);
                LinearLayout ll = (LinearLayout) fragView.findViewById(R.id.dayContainer);
                TextView routineName = (TextView) fragView.findViewById(R.id.routineName);
                routineName.setText(routine.getName());
                for (ExerciseDay day : routine.days
                     ) {
                    Log.d("RoutineRecycler", "inflating view"+ day.getName());
                    View v = inflater.inflate(R.layout.material_day_view, ll, false);
                    DayHolder holder = new DayHolder(v,day);
                    ll.addView(v);
                }
                return fragView;


            }
        }
    }



}
