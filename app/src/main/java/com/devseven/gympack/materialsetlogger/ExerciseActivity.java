package com.devseven.gympack.materialsetlogger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.materialsetlogger.data.ExerciseGroup;
import com.devseven.gympack.materialsetlogger.data.ExerciseSet;
import com.devseven.gympack.setlogger.R;

import java.util.List;

public class ExerciseActivity extends AppCompatActivity
{
    public static final String PROGRAM_TO_PASS = "PROGRAM_TO_PASS";
    ExerciseDay day;
    ExerciseViewsFactory viewsFactory;
    @Override
    public void onCreate(Bundle onSavedInstanceState)
    {
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.material_exercise_activity_v2);
        Intent intent = getIntent();
        if(intent!=null)
        {
            day = intent.getParcelableExtra(PROGRAM_TO_PASS);
        }
        // If there is no intent(dunno if it is possible)
        // or the day is not passed means that the user entered
        // quick starpublic GroupView(Context context) {
        viewsFactory = new ExerciseViewsFactory(getLayoutInflater());
    }
}
// this class may become obsolete as to use the recycler view to improve performance...
// TODO remove if not neccessary or finish if recycler view will not work.
class ExerciseViewsFactory {
    LayoutInflater inflater;
    ExerciseViewsFactory(LayoutInflater inflater)
    {
        this.inflater = inflater;
    }
    // region Group Views
    public View createGroupView(ViewGroup parent, ExerciseGroup group)
    {
        View v = inflater.inflate(R.layout.group_view, parent, false);
        TextView exerciseName = (TextView) v.findViewById(R.id.overview_ExerciseName);
        TextView weights = (TextView) v.findViewById(R.id.overview_repsAmnt);
        TextView reps = (TextView) v.findViewById(R.id.overview_weight);
        exerciseName.setText(group.getExerciseName());
        weights.setText(GetWeightString(group.exerciseSets));
        reps.setText(GetRepsString(group.exerciseSets));
        return v;
    }
    private String GetRepsString(List<ExerciseSet> setList)
    {
        String str = "";
        str+=setList.get(0).getDoneReps()==0?"--":setList.get(0).getDoneReps();
        for(int i = 1; i<setList.size(); i++)
        {
            str+="/"+(setList.get(0).getDoneReps()==0?"--":setList.get(0).getDoneReps());
        }
        return str;
    }
    private String GetWeightString(List<ExerciseSet> setList)
    {
        String str = "";
        str+=setList.get(0).getUsedWeight()==0?"--":setList.get(0).getUsedWeight();
        for(int i = 1; i<setList.size(); i++)
        {
            str+="/"+(setList.get(0).getUsedWeight()==0?"--":setList.get(0).getUsedWeight());
        }
        return str;
    }
    //endregion
    //region Set views
    public View createSetView(ViewGroup parent, ExerciseSet set)
    {
        View v = inflater.inflate(R.layout.set_layout_v2 , parent, false);
        TextView setIndex = (TextView) v.findViewById(R.id.edit_setIndex);
        TextView restTime = (TextView) v.findViewById(R.id.restTime);
        TextView usedWeight = (TextView) v.findViewById(R.id.usedWeight);

        return v;
    }
    //endregion

}