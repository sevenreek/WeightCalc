package com.devseven.gympack.materialsetlogger.routinebuilder;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devseven.gympack.materialsetlogger.R;
import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.materialsetlogger.data.ExerciseGroup;

public class RoutineBuilderExerciseTabController {
    private TextView    currentExercise;
    private TextView    nextExercise;
    private ImageView   prevExerciseButton;
    private ImageView   nextExerciseButton;
    private TextView    prevExercise;
    private TextView    exerciseType;
    final private RoutineBuilderActivity activity;
    public RoutineBuilderExerciseTabController(final RoutineBuilderActivity activity) {
        this.activity = activity;
        currentExercise =       (TextView) activity.findViewById(R.id.currentExercise);
        prevExerciseButton =    (ImageView) activity.findViewById(R.id.prevExerciseButton);
        prevExercise =          (TextView) activity.findViewById(R.id.prevExercise);
        exerciseType =          (TextView) activity.findViewById(R.id.exerciseType);
        nextExercise =          (TextView) activity.findViewById(R.id.nextExercise);
        nextExerciseButton =    (ImageView) activity.findViewById(R.id.nextExerciseButton);
        prevExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectExercise(activity.getCurrentExerciseIndex()-1, activity.getCurrentDayIndex() );
            }
        });
        nextExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.getCurrentExerciseIndex() >= activity.getRoutine().days.get(activity.getCurrentDayIndex()).exerciseGroups.size() - 1)
                    onCreateNewExercise(activity.getCurrentDayIndex(), activity.getCurrentExerciseIndex());

                else
                    selectExercise(activity.getCurrentExerciseIndex()+1, activity.getCurrentDayIndex() );
            }
        });
        prevExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectExercise(activity.getCurrentExerciseIndex()-1, activity.getCurrentDayIndex() );
            }
        });
        nextExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.getCurrentExerciseIndex() >= activity.getRoutine().days.get(activity.getCurrentDayIndex()).exerciseGroups.size() - 1)
                    onCreateNewExercise( activity.getCurrentDayIndex(), activity.getCurrentExerciseIndex());

                else
                    selectExercise(activity.getCurrentExerciseIndex()+1, activity.getCurrentDayIndex() );
            }
        });
    }
    void onExerciseDayCreated()
    {
        nextExerciseButton.setVisibility(View.VISIBLE);
        nextExercise.setVisibility(View.VISIBLE);
    }

    void onNoExerciseDays()
    {
        currentExercise.setText("");
        nextExerciseButton.setVisibility(View.INVISIBLE);
        nextExercise.setVisibility(View.INVISIBLE);
    }

    void onRemovedAllGroups( )
    {
        onHitLeftExerciseBound();
        onHitRightExerciseBound();
        currentExercise.setText("");
        exerciseType.setText("");
    }
    void onHitLeftExerciseBound( )
    {
        prevExercise.setVisibility(View.INVISIBLE);
        prevExerciseButton.setVisibility(View.INVISIBLE);
    }
    void onHitRightExerciseBound( )
    {
        nextExercise.setText(activity.getString(R.string.new_exercise));
        nextExerciseButton.setImageResource(R.drawable.ic_add_white);
    }
    protected void selectExercise(int exerciseIndex, int currentDayIndex)
    {


        activity.setCurrentExerciseIndex(exerciseIndex);

        ExerciseDay day = activity.getRoutine().days.get(currentDayIndex);
        int exerciseCount = day.exerciseGroups.size();
        if(exerciseIndex==-1 || exerciseCount == 0) // the second one is just for safety. I don't think it will be ever checked against
        {
            onRemovedAllGroups();
            activity.clearAdapterData();
            return;
        }
        currentExercise.setText(day.exerciseGroups.get(exerciseIndex).getExerciseName());

        if(exerciseIndex==0)
        {
            onHitLeftExerciseBound();
        }
        else
        {
            prevExercise.setText(day.exerciseGroups.get(exerciseIndex-1).getExerciseName());
            prevExercise.setVisibility(View.VISIBLE);
            prevExerciseButton.setVisibility(View.VISIBLE);
            prevExerciseButton.setImageResource(R.drawable.ic_chevron_left_white);
        }
        if(exerciseIndex==exerciseCount-1)
        {
            onHitRightExerciseBound();
        }
        else
        {
            nextExercise.setText(day.exerciseGroups.get(exerciseIndex+1).getExerciseName());
            nextExercise.setVisibility(View.VISIBLE);
            nextExerciseButton.setVisibility(View.VISIBLE);
            nextExerciseButton.setImageResource(R.drawable.ic_chevron_right_white);
        }
        activity.setAdapterData(day.exerciseGroups.get(exerciseIndex).exerciseSets, false); // TODO IMPLEMENT BETTER TIMED EXERCISES

    }
    void onCreateNewExercise(final int dayIndex, final int exerciseIndex)
    {
        final ExerciseDay day = activity.getRoutine().days.get(dayIndex);
        final AlertDialog.Builder b = new AlertDialog.Builder(activity);
        b.setTitle(activity.getString(R.string.name_new_exercise_dialog_title));
        final EditText et = new EditText(activity);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        b.setView(et);
        et.setSelectAllOnFocus(true);
        et.setHint(activity.getString(R.string.add_exercise_hint));
        // this is supposedly required to be empty for support.
        // the real listener is slightly lower as I do not want to dismiss
        // the dialog if the string is empty
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        b.setNeutralButton(activity.getString(R.string.cancel),null);
        b.setCancelable(true);
        final AlertDialog alertDialog = b.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et.getText() == null || et.getText().toString().trim().isEmpty()) { // this checks if it is not just whitespace
                    activity.buildToast(activity.getString(R.string.exercise_name_cannot_be_empty));
                    return;
                }
                ExerciseGroup group;
                group = new ExerciseGroup(et.getText().toString());
                day.exerciseGroups.add(group);
                selectExercise(exerciseIndex+1,dayIndex);
                alertDialog.dismiss();
            }
        });
    }
}
