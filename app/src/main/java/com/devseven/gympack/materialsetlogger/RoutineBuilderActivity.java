package com.devseven.gympack.materialsetlogger;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
        // region LISTVIEW
        BVH.listView
        // endregion
        // region LISTENERS
        BVH.prevDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDay(currentDayIndex - 1);
            }
        });
        BVH.nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDay(currentDayIndex + 1);
            }
        });
        BVH.prevExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectExercise(currentExerciseIndex - 1);
            }
        });
        BVH.nextExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectExercise(currentExerciseIndex + 1);
            }
        });
        BVH.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSet();
            }
        });
        // endregion
    }

    // region BUTTON FUNCTIONS
    void SelectDay(int index)
    {

    }
    void SelectExercise(int index)
    {

    }
    void AddSet()
    {

    }
    // endregion
    protected static class BuilderViewHolder
    {
        public TextView currentDay;
        public TextView nextDay;
        public TextView prevDay;
        public View prevDayButton;
        public View nextDayButton;
        public TextView currentExercise;
        public TextView nextExercise;
        public View prevExerciseButton;
        public View nextExerciseButton;
        public TextView prevExercise;
        public ListView listView;
        public FloatingActionButton actionButton;
        public TextView exerciseType;
        protected BuilderViewHolder(Activity a)
        {
            // region INITIALIZATION
            LinearLayout dayBar = (LinearLayout) a.findViewById(R.id.dayBar);
            LinearLayout exerciseBar = (LinearLayout) a.findViewById(R.id.exerciseBar);
            prevDayButton = dayBar.getChildAt(0);
            prevDay = (TextView) dayBar.getChildAt(1);
            currentDay = (TextView) dayBar.getChildAt(2);
            nextDay = (TextView) dayBar.getChildAt(3);
            nextDayButton = dayBar.getChildAt(4);
            prevExerciseButton = ((LinearLayout)exerciseBar.getChildAt(0)).getChildAt(0);
            prevExercise = (TextView) ((LinearLayout)exerciseBar.getChildAt(0)).getChildAt(1);
            currentExercise = (TextView) ((LinearLayout)exerciseBar.getChildAt(1)).getChildAt(0);
            exerciseType = (TextView) ((LinearLayout)exerciseBar.getChildAt(1)).getChildAt(1);
            nextExerciseButton = ((LinearLayout)exerciseBar.getChildAt(2)).getChildAt(0);
            nextExercise = (TextView) ((LinearLayout)exerciseBar.getChildAt(2)).getChildAt(1);
            listView = (ListView)a.findViewById(R.id.listView);
            actionButton = (FloatingActionButton)a.findViewById(R.id.fab);
            // endregion
        }
    }
    // this solution will not work. consider using a recyclerview or just a simple linearlayout if evertyhing else fails.
    protected class SetBuilderAdapter<ExerciseSet> extends ArrayAdapter<ExerciseSet>
    {
        Context context;
        ArrayList<ExerciseSet> sets;
        int layoutResourceId;
        public SetBuilderAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<ExerciseSet> sets) {
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
            holder.type.setText()
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
