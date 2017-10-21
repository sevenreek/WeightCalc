package com.devseven.gympack.setlogger;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.RippleDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ExerciseActivity extends AppCompatActivity implements WeightCalcFragment.IWeightCalculator {


    ExerciseDay currentDay;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private final String TAG = "ExerciseActivity";
    View focusedExerciseView;
    ExerciseSet focusedSet;
    LinearLayout overviewSetContainer;
    LinearLayout detailSetContainer;
    View currentColoredView;
    DisplayMetrics metrics = new DisplayMetrics();
        View weightCalcContainer;
    InputMethodManager inputMethodManager;
    TextView selectedGroupText;
    ExerciseGroup selectedGroup;
    View selectedGroupView;
    EditText programName;
    CountDownTimer timer;
    String pName;
    Toolbar tb;
    public static final int MODE_QUICKSTART = 0;
    public static final int MODE_CONTINUE = 1;
    public static final int MODE_STARTPROGRAM = 2;
    public static final String ACTIVITY_MODE = "ACTIVITY_MODE";
    private int mode;
    private boolean dayLogged;
    View addSet;
    UserSettings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_activity_v2);
        settings = UserSettings.getUserSettings(this);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        addSet = findViewById(R.id.addSet);
        tb = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(tb);
        inflater = getLayoutInflater();
        overviewSetContainer = (LinearLayout) findViewById(R.id.overviewSetContainer);
        detailSetContainer = (LinearLayout) findViewById(R.id.detailSetContainer);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt(ACTIVITY_MODE);
            if(mode==MODE_STARTPROGRAM) {
                ExerciseDay ed = GlobalSettings.getDayToOpen();
                currentDay = ed.clone(this);
            }
            else if (mode==MODE_CONTINUE)
            {
                currentDay = GlobalSettings.getDayToOpen();
            }
        } else if (getIntent() != null) {
            Bundle b = getIntent().getExtras();
            mode = b.getInt(ACTIVITY_MODE);
            if(mode==MODE_STARTPROGRAM) {
                Log.d("EXERCISEACTIVITY_MODE","MODE_STARTPROGRAM");
                ExerciseDay ed = GlobalSettings.getDayToOpen();
                currentDay = ed.clone(this);
            }
            else if (mode==MODE_CONTINUE)
            {
                Log.d("EXERCISEACTIVITY_MODE","MODE_CONTINUE");
                currentDay = GlobalSettings.getDayToOpen();
            }
        } else
            throw new IllegalArgumentException(this.toString() + " cannot find one or more of the needed arguments.");
        selectedGroupText  = (TextView) findViewById(R.id.selectedExercise);
        decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalSeparatorAlwaysShown(false);
        if(currentDay.exerciseGroups.isEmpty())
            addSet.setVisibility(View.INVISIBLE);
        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedGroup.exerciseSets.isEmpty()) {
                    UserSettings settings = UserSettings.getUserSettings(ExerciseActivity.this);
                    ExerciseSet set = new ExerciseSet(settings.get_defaultReps(),settings.get_defaultRest());
                    if(settings.getSetToRecord())
                        set.setUsedWeight(selectedGroup.getExercise().getRecordUsedWeight());
                    selectedGroup.exerciseSets.add(set);
                    detailSetContainer.addView(newSetView(set,inflater,detailSetContainer));
                    UpdateGroupView(selectedGroupView,selectedGroup);
                }
                else {
                    ExerciseSet lastSet = selectedGroup.exerciseSets.get(selectedGroup.exerciseSets.size() - 1);
                    ExerciseSet set = new ExerciseSet(lastSet.getGoalReps(), lastSet.getRestTimeInSeconds());
                    set.setUsedWeight(lastSet.getUsedWeight());
                    selectedGroup.exerciseSets.add(set);
                    detailSetContainer.addView(newSetView(set,inflater,detailSetContainer));
                    UpdateGroupView(selectedGroupView,selectedGroup);
                }

            }
        });
        bottomTimer = findViewById(R.id.bottomTimer);
        bottomSheetBeh = BottomSheetBehavior.from(bottomTimer);
        bottomSheetBeh.setHideable(true);
        bottomSheetBeh.setState(BottomSheetBehavior.STATE_HIDDEN);

        for (final ExerciseGroup group:currentDay.exerciseGroups
                ) {
            final View groupView = inflater.inflate(R.layout.group_view,overviewSetContainer, false);
            overviewSetContainer.addView(groupView);
            final View clickableV = groupView.findViewById(R.id.overview_childLayoutClickable);
            final TextView nameTV = (TextView)groupView.findViewById(R.id.overview_ExerciseName);
            nameTV.setText(group.getExerciseName());
            final TextView index = (TextView)groupView.findViewById(R.id.group_index);
            index.setText(Integer.toString(overviewSetContainer.getChildCount()));
            UpdateGroupView(groupView,group);
            clickableV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedGroupView==groupView)
                    {
                        PopupMenu popupMenu = new PopupMenu(ExerciseActivity.this, selectedGroupView);
                        popupMenu.inflate(R.menu.group_menu);
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId())
                                {
                                    case R.id.delete_group:
                                        int pos = overviewSetContainer.indexOfChild(selectedGroupView);
                                        overviewSetContainer.removeView(selectedGroupView);
                                        currentDay.exerciseGroups.remove(selectedGroup);
                                        for(int i = pos; i<currentDay.exerciseGroups.size(); i++)
                                        {
                                            View v = overviewSetContainer.getChildAt(i);
                                            TextView tv = (TextView)v.findViewById(R.id.group_index);
                                            tv.setText(Integer.toString(i+1));
                                        }
                                        if(pos>0)
                                            SelectGroup(currentDay.exerciseGroups.get(pos-1));
                                        else {
                                            detailSetContainer.removeAllViews();
                                            addSet.setVisibility(View.INVISIBLE);
                                            selectedGroup = null;
                                            selectedGroupView = null;
                                            selectedGroupText.setText(getString(R.string.no_exercises_added_to_day));
                                        }
                                        break;
                                }
                                return false;
                            }
                        });
                    }
                    else {
                        SelectGroup(group);
                    }
                }
            });
            SelectGroup(currentDay.exerciseGroups.get(0));

        }
        final View addGroupView = inflater.inflate(R.layout.add_group_view, overviewSetContainer, false);
        final ViewSwitcher vs = (ViewSwitcher) addGroupView.findViewById(R.id.switcher);
        Button addButton = (Button)addGroupView.findViewById(R.id.edit_addExerciseButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vs.showNext();
                final AutoCompleteTextView exerciseName = (AutoCompleteTextView) addGroupView.findViewById(R.id.overview_editExerciseName);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExerciseActivity.this, android.R.layout.select_dialog_item, GlobalSettings.getExerciseNames(ExerciseActivity.this));
                exerciseName.setThreshold(1);
                exerciseName.setAdapter(adapter);
                exerciseName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if(i==EditorInfo.IME_ACTION_DONE )
                        {
                            if(exerciseName.getText().toString().isEmpty())
                            {
                                exerciseName.setText("");
                                vs.showNext();
                            }
                            else
                            {
                            ExerciseGroup newGroup = new ExerciseGroup(exerciseName.getText().toString(),ExerciseActivity.this);
                            currentDay.exerciseGroups.add(newGroup);
                            int last = overviewSetContainer.getChildCount() - 1;
                            overviewSetContainer.addView(newGroupView(newGroup, inflater, overviewSetContainer),last);
                            vs.showNext();
                            SelectGroup(currentDay.exerciseGroups.get(last));
                            }
                        }
                        return false;
                    }
                });
            }
        });
        programName = (EditText) findViewById(R.id.create_programName);

//        programName.setText(currentDay.getName());
//        programName.setFocusable(false);
        overviewSetContainer.addView(addGroupView);

        soundManagerUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if(soundManagerUri == null){
            // alert is null, using backup
            soundManagerUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            // I can't see this ever being null (as always have a default notification)
            // but just incase
            if(soundManagerUri == null) {
                // alert backup is null, using 2nd backup
                soundManagerUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

    }

    View newSetView(final ExerciseSet set, LayoutInflater inflater, LinearLayout container)
    {
        final View eView = inflater.inflate(R.layout.set_layout_v2,container,false);
        final TextView weight = (TextView)eView.findViewById(R.id.usedWeight);
        final TextView repsToDo = (TextView)eView.findViewById(R.id.repsToDo);
        final TextView repsDone = (TextView)eView.findViewById(R.id.repsDone);
        final TextView rest = (TextView)eView.findViewById(R.id.restTime);
        final View weightButton = eView.findViewById(R.id.weightButton);
        final ImageView checkmark = (ImageView) eView.findViewById(R.id.checkmark);
        final TextView index = (TextView)eView.findViewById(R.id.edit_setIndex);
        final View repsButton = eView.findViewById(R.id.repsButton);
        final View indexHolder = eView.findViewById(R.id.indexHolder);
        final ViewSwitcher timeSwitcher = (ViewSwitcher)eView.findViewById(R.id.restSwitcher);
        final ViewSwitcher repsSwitcher = (ViewSwitcher)eView.findViewById(R.id.repsDoneSwitcher);
        final ViewSwitcher repsToDoSwitcher = (ViewSwitcher)eView.findViewById(R.id.repsToDoSwitcher);
        final ViewSwitcher weightSwitcher = (ViewSwitcher)eView.findViewById(R.id.weightSwitcher);
        weight.setText(set.getUsedWeight()+"kg"); // TODO get last used weight.
        repsToDo.setText("/"+set.getGoalReps());

        if(set.isFinished()||set.getDoneReps()!=0)
            repsDone.setText(Integer.toString(set.getDoneReps()));
        else
            repsDone.setText("--");
        index.setText(container.getChildCount()+1+".");
        indexHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.quickstart_set_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final int setIndex = selectedGroup.exerciseSets.indexOf(set);
                        switch (item.getItemId()) {
                            case R.id.delete_set:
                                selectedGroup.exerciseSets.remove(setIndex);
                                detailSetContainer.removeViewAt(setIndex);
                                for(int i = setIndex; i<detailSetContainer.getChildCount(); i++)
                                {
                                    View temp = detailSetContainer.getChildAt(i);
                                    TextView tv = (TextView)temp.findViewById(R.id.edit_setIndex);
                                    tv.setText(Integer.toString(i+1)+".");
                                }
                                UpdateGroupView(selectedGroupView,selectedGroup);
                                break;
                            case R.id.edit_rest:
                                timeSwitcher.showNext();
                                Log.d(TAG, "onMenuItemClick: should show next");
                                final EditText et = (EditText)eView.findViewById(R.id.restTime_edit);
                                et.setText(set.getRestTimeInSeconds()+"");
                                et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View view, boolean b) {
                                        if(!b)
                                        {
                                            set.setRestTimeInSeconds(et.getText().toString().isEmpty()?0:Integer.parseInt(et.getText().toString()));
                                            timeSwitcher.showNext();
                                            rest.setText(set.getRestTimeInSeconds()+"s");
                                        }
                                        else
                                            et.selectAll();
                                    }
                                });
                                et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                        if(i==EditorInfo.IME_ACTION_DONE)
                                        {
                                            et.clearFocus();

                                        }
                                        return false;
                                    }
                                });
                                et.requestFocus();
                                break;
                            case R.id.edit_repsToDo:
                                repsToDoSwitcher.showNext();
                                final EditText et1 = (EditText)eView.findViewById(R.id.repsToDo_edit);
                                et1.setText(set.getGoalReps()+"");
                                et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View view, boolean b) {
                                        if(!b)
                                        {
                                            set.setGoalReps(et1.getText().toString().isEmpty()?0:Integer.parseInt(et1.getText().toString()));
                                            repsToDoSwitcher.showNext();
                                            repsToDo.setText("/"+set.getGoalReps());
                                        }
                                        else
                                            et1.selectAll();
                                    }
                                });
                                et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                        if(i==EditorInfo.IME_ACTION_DONE)
                                        {
                                            et1.clearFocus();

                                        }
                                        return false;
                                    }
                                });
                                et1.requestFocus();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        rest.setText(Integer.toString(set.getRestTimeInSeconds())+"s");
        //region WEIGHT BUTTON
        weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weightCalcContainer!=null)
                {
                    fragmentManager.popBackStack(WeightCalcFragment.WEIGHTCALC_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    if(detailSetContainer.indexOfChild(weightCalcContainer)==detailSetContainer.indexOfChild(eView)+1) {
                        detailSetContainer.removeView(weightCalcContainer);
                        weightCalcContainer = null;
                        return;
                    }
                    else
                    {
                        detailSetContainer.removeView(weightCalcContainer);
                        weightCalcContainer = null;
                    }
                }
                if(set.isFinished())
                {
                    set.setFinished(false);
                    checkmark.setAlpha(0.2f);
                }
                if(getCurrentFocus()!=null)
                    getCurrentFocus().clearFocus();
                focusedSet =  set;
                focusedExerciseView = eView;
                FragmentTransaction ft = fragmentManager.beginTransaction();
                WeightCalcFragment wcf = new WeightCalcFragment();
                Bundle b = new Bundle();
                b.putDouble(WeightCalcFragment.ARG_CURRENT_WEIGHT, set.getUsedWeight());
                b.putInt(WeightCalcFragment.LINEAR_POSITION, detailSetContainer.indexOfChild(eView) + 1);
                wcf.setArguments(b);
                weightCalcContainer = getLayoutInflater().inflate(R.layout.empty_container, detailSetContainer, false);
                detailSetContainer.addView(weightCalcContainer, detailSetContainer.indexOfChild(eView)+1);
                ft.add(weightCalcContainer.getId(), wcf, WeightCalcFragment.WEIGHTCALC_TAG);
                ft.addToBackStack(WeightCalcFragment.WEIGHTCALC_TAG);
                ft.commit();
                fragmentManager.executePendingTransactions();

            }
        });
        weightButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(set.isFinished())
                {
                    set.setFinished(false);
                    checkmark.setAlpha(0.2f);
                }
                if(weightCalcContainer!=null) //.destroy weight calc fragment if it exists
                {
                    fragmentManager.popBackStack(WeightCalcFragment.WEIGHTCALC_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    LinearLayout ll = (LinearLayout)weightCalcContainer.getParent();
                    ll.removeView(weightCalcContainer);
                    weightCalcContainer = null;
                }
                Log.d(this.toString(),"Registered long press.");
                weightSwitcher.showNext();

                final EditText weightEdit = (EditText)eView.findViewById(R.id.usedWeight_edit);
                weightEdit.setText(Double.toString(set.getUsedWeight()));
                weightEdit.requestFocus();
                inputMethodManager.showSoftInput(weightEdit,InputMethodManager.SHOW_IMPLICIT);
                weightEdit.selectAll();

                weightEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if(i == EditorInfo.IME_ACTION_DONE)
                        {
                            weightEdit.clearFocus();
                        }
                        return false;
                    }
                });
                weightEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(!b)
                        {
                            double weightD = 0;
                            String str = weightEdit.getText().toString();
                            if(!str.isEmpty())
                                weightD = Double.parseDouble(str);
                            set.setUsedWeight(weightD);
                            inputMethodManager.hideSoftInputFromWindow(weightEdit.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            weightSwitcher.showNext();
                            weight.setText(decimalFormat.format(weightD)+"kg");
                        }
                    }
                });
                return true;
            }
        });
        //endregion
        //region CHECKMARK BUTTON
        if(set.isFinished())
            checkmark.setAlpha(0.5f);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weightCalcContainer!=null) //.destroy weight calc fragment if it exists
                {
                    fragmentManager.popBackStack(WeightCalcFragment.WEIGHTCALC_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    LinearLayout ll = (LinearLayout)weightCalcContainer.getParent();
                    ll.removeView(weightCalcContainer);
                    weightCalcContainer = null;
                }
                View focus = getCurrentFocus();
                if(focus!=null)
                    focus.clearFocus();
                if(set.isFinished()) {
                    checkmark.setAlpha(0.2f);
                    set.setFinished(false);
                }
                else
                {
                    SaveExerciseRecords(selectedGroup.getExercise(), set.getUsedWeight());
                    checkmark.setAlpha(0.5f);
                    if(set.getDoneReps()==0)
                        repsDone.setText(Integer.toString(0));
                    set.setFinished(true);
                    if(timer!=null)
                        timer.cancel();
                    final TextView restTimerText = (TextView)bottomTimer.findViewById(R.id.restTimer);
                    final TextView endRest = (TextView)bottomTimer.findViewById(R.id.restEnd);
//                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bannerAd.getLayoutParams();
//                    params.setMargins(0,0,0,(int)(56*metrics.density+0.5));
//                    bannerAd.setLayoutParams(params);
                    timer = new CountDownTimer(set.getRestTimeInSeconds()*1000,1000) {
                        @Override
                        public void onTick(long l) {
                            restTimerText.setText("REST: "+ dateFormat_mmss.format(l));
                        }

                        @Override
                        public void onFinish() {
                            bottomSheetBeh.setState(BottomSheetBehavior.STATE_HIDDEN);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),soundManagerUri);
                            r.play();
//                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bannerAd.getLayoutParams();
//                            params.setMargins(0,0,0,0);
//                            bannerAd.setLayoutParams(params);
                            Toast.makeText(ExerciseActivity.this, getString(R.string.rest_finished_toast), Toast.LENGTH_SHORT).show();
                        }
                    };
                    timer.start();
                    endRest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            timer.onFinish();
                            timer.cancel();
                        }
                    });
                    bottomSheetBeh.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBeh.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            if(newState==BottomSheetBehavior.STATE_DRAGGING)
                                bottomSheetBeh.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                        }
                    });

                }

                UpdateGroupView(selectedGroupView,selectedGroup);


            }
        });
        //endregion
        //region REPS BUTTON
        repsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(set.isFinished())
                {
                    set.setFinished(false);
                    checkmark.setAlpha(0.2f);
                }
                if(set.getDoneReps()==0)
                    set.setDoneReps(set.getGoalReps());
                else
                    set.setDoneReps(set.getDoneReps()-1);
                repsDone.setText(""+set.getDoneReps());

            }

        });
        repsButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(set.isFinished())
                {
                    set.setFinished(false);
                    checkmark.setAlpha(0.2f);
                }
                if (weightCalcContainer != null) //.destroy weight calc fragment if it exists
                {
                    fragmentManager.popBackStack(WeightCalcFragment.WEIGHTCALC_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    LinearLayout ll = (LinearLayout) weightCalcContainer.getParent();
                    ll.removeView(weightCalcContainer);
                    weightCalcContainer = null;
                }
                repsSwitcher.showNext();
                final EditText repsDone_edit = (EditText)eView.findViewById(R.id.repsDone_edit);
                if(set.getDoneReps()==0)
                    repsDone_edit.setText(Integer.toString(set.getGoalReps()));
                else
                    repsDone_edit.setText(Integer.toString(set.getDoneReps()));
                repsDone_edit.requestFocus();
                inputMethodManager.showSoftInput(repsDone_edit,InputMethodManager.SHOW_IMPLICIT);
                repsDone_edit.selectAll();
                repsDone_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if(i == EditorInfo.IME_ACTION_DONE)
                        {
                            repsDone_edit.clearFocus();
                        }
                        return false;
                    }
                });

                repsDone_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(!b)
                        {
                            String str = repsDone_edit.getText().toString();
                            int repet = 0;
                            if(!str.isEmpty())
                                repet = Integer.parseInt(str);
                            set.setDoneReps( repet);
                            inputMethodManager.hideSoftInputFromWindow(repsDone_edit.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                            repsSwitcher.showNext();
                            repsDone.setText(Integer.toString( repet));
                        }
                    }
                });
                return false;
            }
        });
        //endregion
        return eView;
    }
//    //void StartExerciseDay(ExerciseDay day) {
//        currentDay = day.clone(this); // TODO clone day here.
//    }

    DecimalFormat decimalFormat;
    void UpdateGroupView(View groupView, ExerciseGroup srcGroup)
    {
        TextView weightTV = (TextView)groupView.findViewById(R.id.overview_weight);
        TextView repsTV = (TextView)groupView.findViewById(R.id.overview_repsAmnt);
        String weights = new String();
        String reps = new String ();
        for(int i=0; i< srcGroup.exerciseSets.size(); i++)
        {
            ExerciseSet set = srcGroup.exerciseSets.get(i);
            double kg = set.getUsedWeight();
            int no = set.getDoneReps();
            boolean fin = set.isFinished();
            if(i == srcGroup.exerciseSets.size()-1)
            {
                if(!fin)
                    weights+="--";
                else
                    weights+=decimalFormat.format(set.getUsedWeight());
                if(!fin)
                    reps+="--";
                else
                    reps+=set.getDoneReps();
            }
            else
            {
                if(!fin)
                    weights+="--/\u200B";
                else
                    weights+=decimalFormat.format(set.getUsedWeight())+"/\u200B";
                if(!fin)
                    reps+="--/\u200B";
                else
                    reps+=set.getDoneReps()+"/\u200B";
            }
        }
        weightTV.setText(weights);
        repsTV.setText(reps);
    }


    // This is the main function that is responsible for nearly everything



    void LogDay(ExerciseDay day)
    {
        Serializer serializer = new Persister();
        File loc = new File(getFilesDir(),GlobalSettings.DIRECTORY_LOGS);
        if(!loc.exists())
            loc.mkdirs();
        Calendar c = Calendar.getInstance();
        if(day.getProgramName()==null || day.getProgramName().isEmpty())
            day.SetProgamName(pName);

        day.setUsedImperial(GlobalSettings.useImperial);

        String formatted = GlobalSettings.LOGS_DATE_FORMAT.format(c.getTime());
        Log.d(this.toString(),"Saving to: " + formatted);
        File file = new File(loc, formatted);
        try {
            serializer.write(day, file);
            Log.d("CONTINUE:LogDay()", "File is:" +file.getPath());
            for (String s:loc.list()
                 ) {
                Log.d("CONTINUE:LogDay()", "File found: "+s);
            }
        } catch (Exception e) {
            Toast.makeText(this, "ERROR: Logging exercise failed. Make sure there is enough space.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    BottomSheetBehavior bottomSheetBeh;
    View bottomTimer;
    SimpleDateFormat dateFormat_mmss = new SimpleDateFormat("mm:ss", Locale.getDefault());
    void SelectGroup(ExerciseGroup group)
    {
        if(mode == MODE_CONTINUE)
            group.Initialize(this);
        if(weightCalcContainer!=null)
        {
            fragmentManager.popBackStack(WeightCalcFragment.WEIGHTCALC_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            weightCalcContainer = null;
        }
        selectedGroup = group;
        View groupView = overviewSetContainer.getChildAt(currentDay.exerciseGroups.indexOf(group));
        selectedGroupView = groupView;
        View colorView = groupView.findViewById(R.id.group_index);
        if(currentColoredView!=null)
            currentColoredView.setBackgroundColor(0);
        currentColoredView = colorView;
        colorView.setBackgroundColor(ContextCompat.getColor(this,R.color.highlight));
        detailSetContainer.removeAllViews();
        selectedGroupText.setText(group.getExerciseName());
        for (final ExerciseSet set:group.exerciseSets
             ) {
            if(!selectedGroup.wasSelected())
            {
                set.setUsedWeight(selectedGroup.getExercise().getRecordUsedWeight());
            }
            detailSetContainer.addView(newSetView(set,inflater,detailSetContainer));

        }
        selectedGroup.setWasSelected(true);

    }
    void SaveExerciseRecords(Exercise exercise, double lastSet)
    {
        if(lastSet>exercise.getRecordUsedWeight()) {
            exercise.setRecordWeight(lastSet);
            Serializer serializer = new Persister();
            File file = new File(getFilesDir() + File.separator + GlobalSettings.EXERCISES_STORAGE_FOLDER_NAME + File.separator + exercise.getName());
            try {
                serializer.write(exercise, file);
                Toast.makeText(this, "New record set for "+exercise.getName(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    Uri soundManagerUri;

    LayoutInflater inflater;
    AdView adView;


    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    public void onResume()
    {
        super.onResume();
        dayLogged = false;
    }
    @Override
    public void onPause()
    {
        dayLogged=true;
        LogDay(currentDay);
        super.onPause();
    }
    @Override
    public void onDestroy()
    {
        if(dayLogged==false)
            LogDay(currentDay);
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exerciseact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.finish_exercise:
                boolean showMessage = false;
                for (ExerciseGroup g:currentDay.exerciseGroups
                     ) {
                    for (ExerciseSet s:g.exerciseSets
                         ) {
                        if(!s.isFinished())
                            showMessage=true;
                        break;
                    }
                    if(showMessage)
                        break;
                }
                if(showMessage) {
                    final AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.unfinished_sets_popup_title))
                            .setMessage(getString(R.string.unfinished_sets_popup_message))
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FinishExercise(settings.getShowInter());
                                }
                            }).show();
                }
                else{
                finish();
                }
            break;

        }
        return false;
    }
    private InterstitialAd interstitialAd;
    public void FinishExercise(boolean showAd)
    {
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed()
            {
                finish();
            }

        });
        if(showAd)
        {
            if(interstitialAd.isLoaded())
                interstitialAd.show();
        }
        else
            finish();
    }
    @Override
    public void onBackPressed(){
        if(getCurrentFocus() instanceof EditText)
            getCurrentFocus().clearFocus();
        else
            super.onBackPressed();

    }

    @Override
    public double getWeight() {
        return focusedSet.getUsedWeight();
    }

    @Override
    public void setWeight(double weight) {
        focusedSet.setUsedWeight(weight);
        TextView tv = (TextView)focusedExerciseView.findViewById(R.id.usedWeight);
        tv.setText(weight+"kg");

    }
    public View newGroupView(final ExerciseGroup group, final LayoutInflater inflater, final ViewGroup container)
    {
        final View groupView = inflater.inflate(R.layout.edit_group_view, container, false);
        group.Initialize(ExerciseActivity.this);
        final TextView reps = (TextView)groupView.findViewById(R.id.overview_repsAmnt);
        final TextView rests = (TextView)groupView.findViewById(R.id.overview_weight);
        final TextView name = (TextView)groupView.findViewById(R.id.overview_ExerciseName);
        final TextView index = (TextView)groupView.findViewById(R.id.group_index);
        final View childLayout = groupView.findViewById(R.id.overview_childLayoutClickable);
        String repsText = new String();
        String restText = new String();
        ArrayList<ExerciseSet> sets = group.exerciseSets;
        if(sets.isEmpty())
        {
            repsText="--";
            restText="--";
        }
        else {
            for (int i = 0; i < sets.size(); i++) {
                if (i == sets.size() - 1) {
                    repsText += sets.get(i).getGoalReps();
                    restText += sets.get(i).getRestTimeInSeconds();
                }
                else {
                    repsText +=sets.get(i).getGoalReps()+"/\u200B";             // "\u200B" is a zero width space. It is placed here so that sets are separated nicely.
                    restText += sets.get(i).getRestTimeInSeconds()+"/\u200B";   // See UpdateGroupView() at the top for a more in depth explaination.
                }
            }
        }
        reps.setText(repsText);
        rests.setText(restText);
        name.setText(group.getExerciseName());
        Log.d(this.toString(), "newGroupView: Group count = "+ currentDay.exerciseGroups.size());
        index.setText((currentDay.exerciseGroups.indexOf(group)+1)+"");
        childLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedGroupView==groupView)
                {
                    PopupMenu popupMenu = new PopupMenu(ExerciseActivity.this, selectedGroupView);
                    popupMenu.inflate(R.menu.group_menu);
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId())
                            {
                                case R.id.delete_group:
                                    int pos = overviewSetContainer.indexOfChild(selectedGroupView);
                                    overviewSetContainer.removeView(selectedGroupView);
                                    currentDay.exerciseGroups.remove(selectedGroup);
                                    for(int i = pos; i<currentDay.exerciseGroups.size(); i++)
                                    {
                                        View v = overviewSetContainer.getChildAt(i);
                                        TextView tv = (TextView)v.findViewById(R.id.group_index);
                                        tv.setText(Integer.toString(i+1));
                                    }
                                    if(pos>0)
                                        SelectGroup(currentDay.exerciseGroups.get(pos-1));
                                    else {
                                        detailSetContainer.removeAllViews();
                                        addSet.setVisibility(View.INVISIBLE);
                                        selectedGroup = null;
                                        selectedGroupView = null;

                                    }
                                    break;
                            }
                            return false;
                        }
                    });
                }
                else
                {
                    SelectGroup(group);
                }
            }
        });

        return groupView;
    }

}
