package com.devseven.gympack.materialsetlogger.routinebuilder;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.devseven.gympack.materialsetlogger.R;
import com.devseven.gympack.materialsetlogger.data.ExerciseGroup;
import com.devseven.gympack.materialsetlogger.data.ExerciseSet;
import com.devseven.gympack.materialsetlogger.data.Routine;
import com.devseven.gympack.materialsetlogger.datacontroller.ApplicationFileManager;


import java.util.ArrayList;
import java.util.List;

/// This activity manages the builder. In here user will build the routine for later use in player.
/// I currently am on the verge whether to move the day tabstrip to TabView so that the user can scroll the days.
/// On top of that I still have TODO implement swipe in RoutineBuilderActivity.
/// The current idea is to have it swipe exercisegroups until the user reaches the end. Then swipe to the next day.
/// This is to be implemented and tested with real users. I feel it might be slightly confusing sometimes.
/// It's surely important to give user a visible feedback when day swipe happens so that they do not think they are
/// still editing the previous day.
public class RoutineBuilderActivity extends AppCompatActivity {

    public static final String ARG_DEFAULTREPCOUNT = "SETTING_DEFAULT_REPS";


    private Routine routine;



    private int currentDayIndex;
    private int currentExerciseIndex;



    private List<Integer> exerciseIndexes; // this holds the index of exercise that a day was left on.
    private Toast currentToast;


    private RecyclerView recyclerView;
    private FloatingActionButton actionButton;
    private Toolbar toolbar;



    private RoutineBuilderDayTabController dayController;

    private SetListAdapter adapter;

    private RoutineBuilderExerciseTabController exerciseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_builder);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        actionButton = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        adapter = new SetListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // region LISTENERS


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentDayIndex!=-1 && currentExerciseIndex!=-1)
                    addSet();
                else {
                    if(currentToast!=null)       // if user were to spam the button this would create a very long toast otherwise
                        currentToast.cancel();
                    currentToast = Toast.makeText(RoutineBuilderActivity.this, routine.days.size()==0?getString(R.string.add_day_first):getString(R.string.add_exercise_first), Toast.LENGTH_SHORT);
                    currentToast.show();
                    if(routine.days.size()>0) {
                        // TODO
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
        dayController = new RoutineBuilderDayTabController(this);
        exerciseController = new RoutineBuilderExerciseTabController(this);
        if (routine == null) {
            routine = new Routine(getString(R.string.new_routine_name));

            currentDayIndex = -1;        // -1 when there are no days
            currentExerciseIndex = -1;
            exerciseIndexes = new ArrayList<>();

            dayController.onRemovedAllDays();
            exerciseController.onRemovedAllGroups();
        }
        else
        {

            dayController = new RoutineBuilderDayTabController(this);
            exerciseController = new RoutineBuilderExerciseTabController(this);
        }
        getSupportActionBar().setTitle(routine.getName());

        // if there are no days then exercise groups cannot be added.
        // endregion
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.routine_builder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.saveRoutine:
                try {
                    saveRoutine();
                    finish();
                } catch (Exception e) {
                    buildToast(getString(R.string.toast_saving_failed));
                    e.printStackTrace();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveRoutine() throws Exception {
        ApplicationFileManager.saveRoutine(routine,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void buildToast(String message)
    {
        if(currentToast!=null)
            currentToast.cancel();
        currentToast = Toast.makeText(RoutineBuilderActivity.this, message, Toast.LENGTH_SHORT);
        currentToast.show();

    }
    // region BUTTON FUNCTIONS

    // this function supports selecting -1 as a day with no exercises

    void addSet()
    {
        ExerciseGroup group = routine.days.get(currentDayIndex).exerciseGroups.get(currentExerciseIndex);
        ExerciseSet e;
        if(group.exerciseSets.size()>0)
            e = new ExerciseSet(group.exerciseSets.get(group.exerciseSets.size()-1).getGoalReps());
        else
            e = new ExerciseSet(getIntent().getIntExtra(ARG_DEFAULTREPCOUNT,12)); // TODO pass this argument in intent
        group.exerciseSets.add(e);
        adapter.notifyItemInserted(group.exerciseSets.size()-1);
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



    public void clearAdapterData()
    {
        adapter.clearSetList();
    }
    public void setAdapterData(ArrayList<ExerciseSet> setList, boolean isTimed)
    {
        adapter.setSetList(setList,isTimed);
        adapter.notifyDataSetChanged();

    }

    public int getCurrentDayIndex() {
        return currentDayIndex;
    }

    public int getCurrentExerciseIndex() {
        return currentExerciseIndex;
    }
    public Routine getRoutine() {
        return routine;
    }
    public void setCurrentDayIndex(int currentDayIndex) {
        this.currentDayIndex = currentDayIndex;
    }

    public void setCurrentExerciseIndex(int currentExerciseIndex) {
        this.currentExerciseIndex = currentExerciseIndex;
    }
    public List<Integer> getExerciseIndexes() {
        return exerciseIndexes;
    }
    public RoutineBuilderDayTabController getDayController() {
        return dayController;
    }
    public RoutineBuilderExerciseTabController getExerciseController() {
        return exerciseController;
    }

}
