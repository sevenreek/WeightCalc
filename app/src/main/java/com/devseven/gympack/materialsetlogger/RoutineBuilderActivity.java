package com.devseven.gympack.materialsetlogger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.materialsetlogger.data.ExerciseGroup;
import com.devseven.gympack.materialsetlogger.data.ExerciseSet;
import com.devseven.gympack.materialsetlogger.data.Routine;
import com.devseven.gympack.materialsetlogger.design.SimpleDividerLine;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/// This activity manages the builder. In here user will build the routine for later use in player.
/// I currently am on the verge whether to move the day tabstrip to TabView so that the user can scroll the days.
/// On top of that I still have TODO implement swipe in RoutineBuilderActivity.
/// The current idea is to have it swipe exercisegroups until the user reaches the end. Then swipe to the next day.
/// This is to be implemented and tested against real users. I feel it might be slightly confusing sometimes.
/// It's surely important to give user a visible feedback when day swipe happens so that they do not think they are
/// still editing the previous day.
public class RoutineBuilderActivity extends AppCompatActivity {

    private Routine routine;
    private BuilderViewHolder BVH;
    private int currentDayIndex;
    private int currentExerciseIndex;
    private ArrayList<Integer> exerciseIndexes; // this holds the index of exercise that a day was left on.
    private Toast currentToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_builder);
        BVH = new BuilderViewHolder(this);

        // prev/next Day/Exercise Button moves the user to the previous/next Day/Exercise.
        // Because the button themselves where slightly small, I opted to also add click listeners
        // to

        // region LISTENERS
        BVH.prevDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectDay(currentDayIndex-1, routine, BVH);
            }
        });
        BVH.nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentDayIndex >= routine.days.size() - 1)
                    onCreateNewDay(BVH, routine, RoutineBuilderActivity.this, currentDayIndex);
                else
                    onSelectDay(currentDayIndex+1, routine, BVH);
            }
        });
        BVH.prevExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectExercise(currentExerciseIndex-1, currentDayIndex, routine, BVH);
            }
        });
        BVH.nextExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentExerciseIndex >= routine.days.get(currentDayIndex).exerciseGroups.size() - 1)
                    onCreateNewExercise(BVH, RoutineBuilderActivity.this, currentDayIndex, routine, currentExerciseIndex);

                else
                    onSelectExercise(currentExerciseIndex+1, currentDayIndex, routine, BVH);
            }
        });
        BVH.prevExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectExercise(currentExerciseIndex-1, currentDayIndex, routine, BVH);
            }
        });
        BVH.nextExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentExerciseIndex >= routine.days.get(currentDayIndex).exerciseGroups.size() - 1)
                    onCreateNewExercise(BVH, RoutineBuilderActivity.this, currentDayIndex, routine, currentExerciseIndex);

                else
                    onSelectExercise(currentExerciseIndex+1, currentDayIndex, routine, BVH);
            }
        });
        BVH.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentDayIndex!=-1 && currentExerciseIndex!=-1)
                    onAddSet(routine, RoutineBuilderActivity.this);
                else {
                    if(currentToast!=null)       // if user were to spam the button this would create a very long toast otherwise
                        currentToast.cancel();
                    currentToast = Toast.makeText(RoutineBuilderActivity.this, routine.days.size()==0?getString(R.string.add_day_first):getString(R.string.add_exercise_first), Toast.LENGTH_SHORT);
                    currentToast.show();
                    if(routine.days.size()>0) {
                        //BVH.nextExercise.startAnimation(AnimationUtils.loadAnimation(RoutineBuilderActivity.this, R.anim.shake));
                        BVH.nextExerciseButton.startAnimation(AnimationUtils.loadAnimation(RoutineBuilderActivity.this, R.anim.shake));
                    }
                    else {
                        //BVH.nextDay.startAnimation(AnimationUtils.loadAnimation(RoutineBuilderActivity.this,R.anim.shake));
                        BVH.nextDayButton.startAnimation(AnimationUtils.loadAnimation(RoutineBuilderActivity.this, R.anim.shake));
                    }
                }
            }
        });
        // endregion
        // load routine from arguments if present
        if(savedInstanceState != null)
        {
            routine = savedInstanceState.getParcelable(Routine.ROUTINE_PARCEL);
        }
        // region LISTVIEW
        // If the builder is not in edit mode then create a new routine
        if (routine == null) {
            routine = new Routine(getString(R.string.new_routine_name));
            //routine.days.add(new ExerciseDay(R.string.new_day_name+" 1"));
            currentDayIndex = -1;        // -1 when there are no days
            currentExerciseIndex = -1;
            exerciseIndexes = new ArrayList<>();

            onRemovedAllDays(BVH);
            onRemovedAllGroups(BVH);
        }
        else
        {

        }
        // if there are no days then exercise groups cannot be added.
        // endregion
    }
    void onRemovedAllDays(BuilderViewHolder BVH)
    {
        onHitRightDayBound(BVH);
        onHitLeftDayBound(BVH);
        BVH.currentDay.setText("");
        BVH.currentExercise.setText("");
        BVH.nextExerciseButton.setVisibility(View.INVISIBLE);
        BVH.nextExercise.setVisibility(View.INVISIBLE);
    }
    void onRemovedAllGroups(BuilderViewHolder BVH)
    {
        onHitLeftExerciseBound(BVH);
        onHitRightExerciseBound(BVH);
        BVH.currentExercise.setText("");
        BVH.exerciseType.setText("");
    }
    void onHitLeftExerciseBound(BuilderViewHolder BVH)
    {
        BVH.prevExercise.setVisibility(View.INVISIBLE);
        BVH.prevExerciseButton.setVisibility(View.INVISIBLE);
    }
    void onHitRightExerciseBound(BuilderViewHolder BVH)
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
    void onCreateNewDay(final BuilderViewHolder BVH, final Routine r, final Context ctx, final int index)
    {
        //BVH.currentExercise.setText(getString(R.string.new_exercise));

        // TODO replace with custom dialog fragment or replace TextView with EditText and edit directly
        AlertDialog.Builder b = new AlertDialog.Builder(ctx);
        b.setTitle(getString(R.string.name_new_day_dialog_title));
        final EditText et = new EditText(ctx);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        b.setView(et);
        et.setText(getString(R.string.new_day_name)+" "+(index+2));
        et.setHint(getString(R.string.add_day_hint));
        et.setSelectAllOnFocus(true);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(et.getText() == null || et.getText().toString().trim().isEmpty()) { // this checks if it is not just whitespace
                    if(currentToast!=null)
                        currentToast.cancel();
                    currentToast = Toast.makeText(ctx, getString(R.string.day_name_cannot_be_empty), Toast.LENGTH_SHORT);
                    currentToast.show();
                    return;
                }
                ExerciseDay day;
                day = new ExerciseDay(et.getText().toString());
                Log.d("DEBUG1:",day.getName());
                r.days.add(index+1, day);
                exerciseIndexes.add(index+1,-1);
                onSelectDay(index+1, r, BVH);
                BVH.nextExerciseButton.setVisibility(View.VISIBLE);
                BVH.nextExercise.setVisibility(View.VISIBLE);
            }
        });
        b.setCancelable(true);
        b.setNeutralButton("CANCEL",null);
        b.show();


    }
    void onCreateNewExercise(final BuilderViewHolder BVH,final Context ctx, final int dayIndex, final Routine routine, final int exerciseIndex)
    {
        final ExerciseDay day = routine.days.get(dayIndex);
        final AlertDialog.Builder b = new AlertDialog.Builder(ctx);
        b.setTitle(getString(R.string.name_new_exercise_dialog_title));
        final EditText et = new EditText(ctx);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        b.setView(et);
        et.setSelectAllOnFocus(true);
        et.setHint(getString(R.string.add_exercise_hint));
        // this is supposedly required to be empty for support.
        // the real listener is slightly lower as I do not want to dismiss
        // the dialog if the string is empty
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        b.setNeutralButton(getString(R.string.cancel),null);
        b.setCancelable(true);
        final AlertDialog alertDialog = b.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getText() == null || et.getText().toString().trim().isEmpty()) { // this checks if it is not just whitespace
                    if(currentToast!=null)
                        currentToast.cancel();
                    currentToast = Toast.makeText(ctx, getString(R.string.exercise_name_cannot_be_empty), Toast.LENGTH_SHORT);
                    currentToast.show();
                    return;
                }

                ExerciseGroup group;
                group = new ExerciseGroup(et.getText().toString(),ApplicationState.getInstance(ctx).getExerciseList(), ctx);
                day.exerciseGroups.add(group);
                onSelectExercise(exerciseIndex+1,dayIndex,routine,BVH);
                alertDialog.dismiss();
            }
        });
    }
    // region BUTTON FUNCTIONS
    void onSelectDay(int index, Routine r, BuilderViewHolder BVH)
    {
        if(currentDayIndex!=-1)
            exerciseIndexes.set(currentDayIndex,currentExerciseIndex);
        currentExerciseIndex = exerciseIndexes.get(index);
        int dayCount = r.days.size();
        blinkAndFadeIn(BVH.currentDay,4000);
        blinkAndFadeIn(BVH.nextDay,4000);
        blinkAndFadeIn(BVH.prevDay,4000);
        //blinkAndFadeIn(BVH.prevDayButton,4000);
        //blinkAndFadeIn(BVH.nextDayButton,4000);
        currentDayIndex = index;
        BVH.currentDay.setText(r.days.get(index).getName());
        onSelectExercise(exerciseIndexes.get(index),currentDayIndex,r,BVH);
        if(index==0)
        {
            onHitLeftDayBound(BVH);
        }
        else
        {
            BVH.prevDay.setText(r.days.get(index-1).getName());
            BVH.prevDay.setVisibility(View.VISIBLE);
            BVH.prevDayButton.setVisibility(View.VISIBLE);
            BVH.prevDayButton.setImageResource(R.drawable.ic_chevron_left);
        }
        if(index==dayCount-1)
        {
            onHitRightDayBound(BVH);
        }
        else
        {
            BVH.nextDay.setText(r.days.get(index+1).getName());
            BVH.nextDay.setVisibility(View.VISIBLE);
            BVH.nextDayButton.setVisibility(View.VISIBLE);
            BVH.nextDayButton.setImageResource(R.drawable.ic_chevron_right);
        }

    }
    // this function supports selecting -1 as a day with no exercises
    void onSelectExercise(int exerciseIndex, int currentDayIndex, Routine r, BuilderViewHolder BVH)
    {
        blinkAndFadeIn(BVH.currentExercise,4000);
        blinkAndFadeIn(BVH.nextExercise,4000);
        blinkAndFadeIn(BVH.prevExercise,4000);
        blinkAndFadeIn(BVH.recyclerView,4000);

        currentExerciseIndex = exerciseIndex;

        ExerciseDay day = r.days.get(currentDayIndex);
        int exerciseCount = day.exerciseGroups.size();
        if(exerciseIndex==-1 || exerciseCount == 0) // the second one is just for safety. I dont think it will be ever checked against
        {
            onRemovedAllGroups(BVH);
            BVH.clearAdapterData();
            return;
        }
        BVH.currentExercise.setText(day.exerciseGroups.get(exerciseIndex).getExerciseName());

        if(exerciseIndex==0)
        {
            onHitLeftExerciseBound(BVH);
        }
        else
        {
            BVH.prevExercise.setText(day.exerciseGroups.get(exerciseIndex-1).getExerciseName());
            BVH.prevExercise.setVisibility(View.VISIBLE);
            BVH.prevExerciseButton.setVisibility(View.VISIBLE);
            BVH.prevExerciseButton.setImageResource(R.drawable.ic_chevron_left_white);
        }
        if(exerciseIndex==exerciseCount-1)
        {
            onHitRightExerciseBound(BVH);
        }
        else
        {
            BVH.nextExercise.setText(day.exerciseGroups.get(exerciseIndex+1).getExerciseName());
            BVH.nextExercise.setVisibility(View.VISIBLE);
            BVH.nextExerciseButton.setVisibility(View.VISIBLE);
            BVH.nextExerciseButton.setImageResource(R.drawable.ic_chevron_right_white);
        }
        BVH.setAdapterData(day.exerciseGroups.get(exerciseIndex).exerciseSets, false); // TODO IMPLEMENT BETTER TIMED EXERCISES

    }
    void onAddSet(Routine r, Context context)
    {
        ExerciseGroup group = r.days.get(currentDayIndex).exerciseGroups.get(currentExerciseIndex);
        ExerciseSet e;
        if(group.exerciseSets.size()>0)
            e = new ExerciseSet(group.exerciseSets.get(group.exerciseSets.size()-1).getGoalReps());
        else
            e = new ExerciseSet(ApplicationState.getInstance(context).getDefaultSetReps());
        group.exerciseSets.add(e);
        BVH.onAddSet(group.exerciseSets.size()-1);


    }
    // If I ever feel like coming back to it I will actually develop the mock slide animation here.
    // Right now changing days/exercises fades out all textviews as they change data.
    // I might also just move to a tabView in regards to the day list.
    void blinkAndFadeIn(final View v, int duration)
    {
        v.setAlpha(0);
        v.animate()
                .alpha(255)
                .setDuration(duration)
                .start();
    }

    // endregion
    /// This class holds all references to the views in this activity.
    /// Honestly I am not sure why I did it this way. It simplifies passing views between different functions
    /// but I believe it is completely unnecessary.
    protected class BuilderViewHolder
    {
        private Activity act;
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
        public RecyclerView recyclerView;
        public FloatingActionButton actionButton;
        public TextView exerciseType;
        private SetListAdapter adapter = new SetListAdapter();
        protected BuilderViewHolder(Activity a)
        {
            act = a;
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
            recyclerView = (RecyclerView)a.findViewById(R.id.listView);
            actionButton = (FloatingActionButton)a.findViewById(R.id.fab);
            // endregion
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(a));
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new SimpleDividerLine(a,32));
        }
        public void clearAdapterData()
        {
            adapter.clearSetList();
        }
        public void setAdapterData(ArrayList<ExerciseSet> setList, boolean isTimed)
        {
            adapter.setSetList(setList,isTimed);
            adapter.notifyDataSetChanged();
        }
        public void onAddSet(int pos)
        {
            adapter.notifyItemInserted(pos);
        }


    }
    // This class holds information about views for the recycler view.
    // The recycler view implementation might not have been necessary here but I decided to do it to learn how to.
    protected class SetHolder extends RecyclerView.ViewHolder
    {
        TextView index;
        TextView type;
        View menuButton;
        EditText repCount;

        public SetHolder(View itemView) {
            super(itemView);
            index = (TextView) itemView.findViewById(R.id.setIndex);
            type = (TextView) itemView.findViewById(R.id.typeTextView);
            menuButton = itemView.findViewById(R.id.setMenu);
            repCount = (EditText) itemView.findViewById(R.id.repCount);
            //repCount.setInputType(InputType.TYPE_CLASS_NUMBER); // this may not be neccessary, testing required
            //repCount.setBackground(null);

        }
        public void bindData(int position, final ExerciseSet set, boolean isTimed)
        {
            index.setText(position+1+".");
            type.setText(isTimed?getString(R.string.time_to_last):getString(R.string.reps_to_do));
            repCount.setText(Integer.toString(set.getGoalReps()));
            repCount.addTextChangedListener(new TextWatcher() { // dont know if this should be moved somewhere else
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    set.setGoalReps(Integer.parseInt(repCount.getText().toString()));
                }
            });
        }
    }
    public class SetListAdapter extends RecyclerView.Adapter<SetHolder>
    {
        public SetListAdapter()
        {
            setList = new ArrayList<>();
        }
        public SetListAdapter(ArrayList<ExerciseSet> setList, boolean isExerciseTimed) {
            this.setList = setList;
            this.isExerciseTimed = isExerciseTimed;
        }
        private ArrayList<ExerciseSet> setList;
        boolean isExerciseTimed;
        public void clearSetList()
        {
            setList = null;
            isExerciseTimed = false;
            this.notifyDataSetChanged();
        }
        public void setSetList(ArrayList setList, boolean isTimed)
        {
            this.setList = setList;
            isExerciseTimed = isTimed;
            this.notifyDataSetChanged();
        }

        @Override
        public SetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.builder_set,parent,false);
            return new SetHolder(v);
        }

        @Override
        public void onBindViewHolder(SetHolder holder, int position) {
            holder.bindData(position,setList.get(position),isExerciseTimed);
        }

        @Override
        public int getItemCount() {
            if(setList == null)
                return 0;
            else
                return setList.size();
        }

    }

}
