package com.devseven.gympack.setlogger;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DetailExerciseFragment extends Fragment {

        // internal config

    LinearLayout daysContainer;
    //
    public interface DetailExerciseInterface
    {
        public void end();
    }



    DetailExerciseInterface IActivity;

    void LoadDays()
    {
        daysContainer.removeAllViews();
        if(IActivity==null)
        {
            Log.e(this.toString(),"IActivity is null");
            return;
        }
        else
        {
                for (final ExerciseDay ed: GlobalSettings.getProgramToEdit().days
                        ) {
                    Log.d(this.toString(),GlobalSettings.getProgramToEdit().getName()+" contains day: "+ed.getName());
                    View view;
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP)
                         view = getLayoutInflater(null).inflate(R.layout.detail_day_view,daysContainer,false );
                    else
                        view = getLayoutInflater(null).inflate(R.layout.detail_day_view_support,daysContainer,false );
                    TextView dayName = (TextView)view.findViewById(R.id.dayName);
                    TextView setCount = (TextView)view.findViewById(R.id.setCount);
                    TextView exerciseCount = (TextView)view.findViewById(R.id.exerciseCount);
                    TextView bodyParts = (TextView)view.findViewById(R.id.bodyParts);
                    dayName.setText(ed.getName());
                    // TODO DICTIONARY CONTAINING BODYPARTS
                    int setC = 0;
                    for (ExerciseGroup g:ed.exerciseGroups
                         ) {
                        setC += g.exerciseSets.size();
                    }

                    setCount.setText(setC+" "+(setC==1?getString(R.string.sets_amount_singular):getString(R.string.sets_amount_plural)));
                    exerciseCount.setText(ed.exerciseGroups.size()+" "+(ed.exerciseGroups.size()==1?getString(R.string.exercises_amount_singular):getString(R.string.exercises_amount_plural)));

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StartDay(ed);
                        }
                    });

                    daysContainer.addView(view);

                }

        }
    }

    void StartDay(ExerciseDay ed)
    {
        //TODO
        Log.d("ExerciseDay:",ed.getName()+" started.");
        GlobalSettings.setDayToOpen(ed);
        Intent intent = new Intent(getContext(), ExerciseActivity.class);
        intent.putExtra(ExerciseActivity.ACTIVITY_MODE,ExerciseActivity.MODE_STARTPROGRAM);
        startActivity(intent);
        IActivity.end();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_routine_fragment, container, false);
        daysContainer = (LinearLayout)v. findViewById(R.id.daysContainer);
        return v;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        LoadDays();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity() instanceof DetailExerciseInterface) {
            IActivity = (DetailExerciseInterface) getActivity();
            Log.d(this.toString(),GlobalSettings.getProgramToEdit().getName()+" is set as programToOpen");
        }
        else
            Log.e("PickExerciseFragment", "Activity must implement PickExerciseInterface interface");

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
