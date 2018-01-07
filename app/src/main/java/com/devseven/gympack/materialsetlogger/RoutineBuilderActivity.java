package com.devseven.gympack.materialsetlogger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.materialsetlogger.data.ExerciseSet;
import com.devseven.gympack.materialsetlogger.data.Routine;
import com.devseven.gympack.setlogger.R;

import java.util.ArrayList;
import java.util.Collection;

public class RoutineBuilderActivity extends AppCompatActivity {

    private Routine routine;
    private BuilderViewHolder BVH;
    private int currentDayIndex;
    private int currentExerciseIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_builder);
        BVH = new BuilderViewHolder(this);

        // region LISTENERS
        BVH.prevDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectDay(--currentDayIndex, routine, BVH);
            }
        });
        BVH.nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectDay(++currentDayIndex, routine, BVH);
            }
        });
        BVH.prevExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectExercise(--currentExerciseIndex, routine);
            }
        });
        BVH.nextExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectExercise(++currentExerciseIndex, routine);
            }
        });
        BVH.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddSet();
            }
        });
        // endregion
        // region LISTVIEW
        if(routine == null)
        {
            routine = new Routine(getString(R.string.new_routine_name));
            routine.days.add(new ExerciseDay(R.string.new_day_name+" 1"));
            onHitLeftSetBound(BVH);
            onHitRightSetBound(BVH);
            onHitLeftDayBound(BVH);
            onHitRightDayBound(BVH);
            onCreateNewExercise(BVH,routine,this, currentExerciseIndex);
            currentDayIndex = 0;
            currentExerciseIndex = 0;
        }
        // endregion
    }
    void onHitLeftSetBound(BuilderViewHolder BVH)
    {
        BVH.prevExercise.setVisibility(View.INVISIBLE);
        BVH.prevExerciseButton.setVisibility(View.INVISIBLE);
    }
    void onHitRightSetBound(BuilderViewHolder BVH)
    {
        BVH.nextExercise.setText(getString(R.string.new_exercise));
        BVH.nextExerciseButton.setImageResource(R.drawable.ic_add_white);
    }
    void onHitLeftDayBound(BuilderViewHolder BVH)
    {
        BVH.prevDay.setVisibility(View.INVISIBLE);
        BVH.prevDayButton.setVisibility(View.INVISIBLE);
    }
    void onHitRightDayBound(BuilderViewHolder BVH)
    {
        BVH.nextDay.setText(getString(R.string.new_day));
        BVH.nextDayButton.setImageResource(R.drawable.ic_add);
    }
    void onCreateNewExercise(BuilderViewHolder BVH, Routine r, Context ctx, int index)
    {
        BVH.currentExercise.setText(getString(R.string.new_exercise));
        final ExerciseDay day = new ExerciseDay(getString(R.string.new_exercise));
        r.days.add(day);
        // TODO replace with custom dialog fragment or replace TextView with EditText and edit directly
        AlertDialog.Builder b = new AlertDialog.Builder(ctx);
        b.setTitle(getString(R.string.name_new_exercise_dialog_title));
        final EditText et = new EditText(ctx);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        b.setView(et);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                day.setName(et.getText().toString());
            }
        });
        b.show();
        onSelectDay(index+1, r, BVH);
    }

    // region BUTTON FUNCTIONS
    void onSelectDay(int index, Routine r, BuilderViewHolder BVH)
    {
        if(index==0)
        {
            onHitLeftDayBound(BVH);
        }
        else if(index>=r.days.size()-1)
        {
            BVH.prevDay.setText(r.days.get(index-1).getName());
            onHitRightDayBound(BVH);
        }
        else
        {
            BVH.currentDay.setText(r.days.get(index).getName());
            BVH.prevDay.setText(r.days.get(index-1).getName());
            BVH.nextDay.setText(r.days.get(index+1).getName());
        }
    }
    void onSelectExercise(int index, Routine r)
    {

    }
    void onAddSet()
    {

    }
    // endregion
    protected static class BuilderViewHolder
    {
        public TextView currentDay;
        public TextView nextDay;
        public TextView prevDay;
        public ImageView prevDayButton;
        public ImageView nextDayButton;
        public TextView currentExercise;
        public TextView nextExercise;
        public ImageView prevExerciseButton;
        public ImageView nextExerciseButton;
        public TextView prevExercise;
        public ListView listView;
        public FloatingActionButton actionButton;
        public TextView exerciseType;
        protected BuilderViewHolder(Activity a)
        {
            // region INITIALIZATION
            LinearLayout dayBar = (LinearLayout) a.findViewById(R.id.dayBar);
            LinearLayout exerciseBar = (LinearLayout) a.findViewById(R.id.exerciseBar);
            prevDayButton = (ImageView) dayBar.getChildAt(0);
            prevDay = (TextView) dayBar.getChildAt(1);
            currentDay = (TextView) dayBar.getChildAt(2);
            nextDay = (TextView) dayBar.getChildAt(3);
            nextDayButton = (ImageView) dayBar.getChildAt(4);
            prevExerciseButton = (ImageView) ((LinearLayout)exerciseBar.getChildAt(0)).getChildAt(0);
            prevExercise = (TextView) ((LinearLayout)exerciseBar.getChildAt(0)).getChildAt(1);
            currentExercise = (TextView) ((LinearLayout)exerciseBar.getChildAt(1)).getChildAt(0);
            exerciseType = (TextView) ((LinearLayout)exerciseBar.getChildAt(1)).getChildAt(1);
            nextExerciseButton = (ImageView) ((LinearLayout)exerciseBar.getChildAt(2)).getChildAt(0);
            nextExercise = (TextView) ((LinearLayout)exerciseBar.getChildAt(2)).getChildAt(1);
            listView = (ListView)a.findViewById(R.id.listView);
            actionButton = (FloatingActionButton)a.findViewById(R.id.fab);
            // endregion
        }
    }
    // this solution will not work. consider using a recyclerview or just a simple linearlayout if evertyhing else fails.
    protected class RoutineBuilderSetAdapter<ExerciseSet> extends ArrayAdapter<ExerciseSet>
    {
        Context context;
        ArrayList<ExerciseSet> sets;
        int layoutResourceId;
        public RoutineBuilderSetAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<ExerciseSet> sets) {
            super(context, resource);
            this.context = context;
            this.sets = sets;
            this.layoutResourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View set = convertView;
            BuilderSetHolder holder = null;
            if(set == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                set = inflater.inflate(layoutResourceId, parent, false);
                holder = new BuilderSetHolder();
                holder.index = (TextView)set.findViewById(R.id.setIndex);
                holder.menuButton = (View)set.findViewById(R.id.setMenu);
                holder.type = (TextView)set.findViewById(R.id.typeTextView);
                holder.repCount = (EditText)set.findViewById(R.id.repCount);
                set.setTag(holder);
            }
            else
            {
                holder = (BuilderSetHolder)set.getTag();
            }
            ExerciseSet data = sets.get(position);
            holder.index.setText(position+1+".");
            return set;
        }
    }
    protected static class BuilderSetHolder
    {
        TextView index;
        TextView type;
        View menuButton;
        EditText repCount;
    }

}
