package com.devseven.gympack.materialsetlogger.routinebuilder;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devseven.gympack.materialsetlogger.R;
import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.materialsetlogger.data.Routine;

import java.util.Locale;

public class RoutineBuilderDayTabController {
    RoutineBuilderActivity activity;
    private TextView currentDay;
    private TextView nextDay;
    private TextView prevDay;
    private ImageView prevDayButton;
    private ImageView nextDayButton;
    public RoutineBuilderDayTabController(final RoutineBuilderActivity activity) {

        this.activity = activity;

        LinearLayout dayBar = (LinearLayout) activity.findViewById(R.id.dayBar);
        prevDayButton = (ImageView) dayBar.getChildAt(0);
        prevDay = (TextView) dayBar.getChildAt(1);
        currentDay = (TextView) dayBar.getChildAt(2);
        nextDay = (TextView) dayBar.getChildAt(3);
        nextDayButton = (ImageView) dayBar.getChildAt(4);
        prevDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDay(activity.getCurrentDayIndex()-1 );
            }
        });
        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.getCurrentDayIndex() >= activity.getRoutine().days.size() - 1)
                    createNewDay(activity.getRoutine(),  activity.getCurrentDayIndex());
                else
                    selectDay(activity.getCurrentDayIndex()+1 );
            }
        });
    }
    void onRemovedAllDays( )
    {
        onHitRightDayBound();
        onHitLeftDayBound();
        currentDay.setText("");
        activity.getExerciseController().onNoExerciseDays();
    }
    void onHitLeftDayBound( )
    {
        prevDay.setVisibility(View.INVISIBLE);
        prevDayButton.setVisibility(View.INVISIBLE);
    }
    void onHitRightDayBound( )
    {
        nextDay.setText(activity.getString(R.string.new_day));
        nextDayButton.setImageResource(R.drawable.ic_add);
    }
    void selectDay(int index )
    {
        if(activity.getCurrentDayIndex()!=-1)
            activity.getExerciseIndexes().set(activity.getCurrentDayIndex(),activity.getCurrentExerciseIndex());
        activity.setCurrentExerciseIndex(activity.getExerciseIndexes().get(index));
        int dayCount = activity.getRoutine().days.size();
        activity.setCurrentDayIndex(index);
        currentDay.setText(activity.getRoutine().days.get(index).getName());
        activity.getExerciseController().selectExercise(activity.getExerciseIndexes().get(index),activity.getCurrentDayIndex());
        if(index==0)
        {
            onHitLeftDayBound();
        }
        else
        {
            prevDay.setText(activity.getRoutine().days.get(index-1).getName());
            prevDay.setVisibility(View.VISIBLE);
            prevDayButton.setVisibility(View.VISIBLE);
            prevDayButton.setImageResource(R.drawable.ic_chevron_left);
        }
        if(index==dayCount-1)
        {
            onHitRightDayBound();
        }
        else
        {
            nextDay.setText(activity.getRoutine().days.get(index+1).getName());
            nextDay.setVisibility(View.VISIBLE);
            nextDayButton.setVisibility(View.VISIBLE);
            nextDayButton.setImageResource(R.drawable.ic_chevron_right);
        }

    }
    protected void createNewDay(final Routine r, final int index)
    {
        //currentExercise.setText(getString(R.string.new_exercise));

        // TODO replace with custom dialog fragment or replace TextView with EditText and edit directly
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        b.setTitle(activity.getString(R.string.name_new_day_dialog_title));
        final EditText et = new EditText(activity);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        b.setView(et);
        et.setText(String.format(Locale.getDefault(),"%s %d",activity.getString(R.string.new_day_name),index+2));
        et.setHint(activity.getString(R.string.add_day_hint));
        et.setSelectAllOnFocus(true);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(et.getText() == null || et.getText().toString().trim().isEmpty()) { // this checks if it is not just whitespace
                    activity.buildToast(activity.getString(R.string.day_name_cannot_be_empty));
                    return;
                }
                ExerciseDay day;
                day = new ExerciseDay(et.getText().toString());
                Log.d("DEBUG1:",day.getName());
                r.days.add(index+1, day);
                activity.getExerciseIndexes().add(index+1,-1);
                selectDay(index+1);
                activity.getExerciseController().onExerciseDayCreated();
            }
        });
        b.setCancelable(true);
        b.setNeutralButton("CANCEL",null);
        b.show();


    }


}
