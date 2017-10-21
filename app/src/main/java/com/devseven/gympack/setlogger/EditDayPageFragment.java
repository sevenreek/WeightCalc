package com.devseven.gympack.setlogger;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

///// GROUPVIEW THAT IS OFTEN REFERED TO IN THIS CLASS IS NOT THE GroupView CLASS BUT A VIEW THAT CONTAINS INFORMATION ABOUT ExerciseGroup CLASS /////
public class EditDayPageFragment extends Fragment {

    private ProgramEditorInterface mListener;

    public EditDayPageFragment() {

    }
    //////////////////////////// Defining Variables ////////////////////////////////////
    public static final String ARG_POSTION = "ARG_POSITION";
    private int position;
    private ExerciseDay exerciseDay;
    LinearLayout verticalScrollView;
    private View selectedGroupView;
    private View addGroupView;
    ExerciseGroup selectedGroup;
    LinearLayout groupContainer;
    boolean deleteMode;
    InputMethodManager inputMethodManager;
    ImageButton addSet;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSTION);
        }
    }

    //////////////////////////// Update Group Layout  ////////////////////////////////////
    void UpdateGroupView(View groupView, ExerciseGroup group)
    {
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
                    repsText +=sets.get(i).getGoalReps()+"/\u200B";             // \u200B is a zero width space. It is placed here so that sets are separated nicely:
                    restText += sets.get(i).getRestTimeInSeconds()+"/\u200B";   // 10/10/10/                10/10/10/1
                }                                                               // 10/10...     INSTEAD OF  0/10/10...
            }
        }
        reps.setText(repsText);
        rests.setText(restText);
        name.setText(group.getExerciseName());
        index.setText((exerciseDay.exerciseGroups.indexOf(group)+1)+"");
    }

    //////////////////////////// New Set Layout  ////////////////////////////////////
    public View newSetView(final ExerciseSet baseSet, final LayoutInflater inflater, final ViewGroup container)
    {
        final View setView = inflater.inflate(R.layout.edit_set_layout, container, false);
        final EditText reps = (EditText) setView.findViewById(R.id.repsToDo);
        final EditText rests = (EditText) setView.findViewById(R.id.rests);
        final TextView index = (TextView)setView.findViewById(R.id.edit_setIndex);
        ////// EditTexts for reps and rest //////////
        reps.setText(baseSet.getGoalReps()+"");
        reps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(reps.getText().toString().isEmpty())
                    baseSet.setGoalReps(0);
                else
                    baseSet.setGoalReps(Integer.parseInt(reps.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reps.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                UpdateGroupView(selectedGroupView, selectedGroup);
            }
        });

        rests.setText(baseSet.getRestTimeInSeconds()+"s");
        rests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(rests.getText().toString().isEmpty())
                    baseSet.setRestTimeInSeconds(0);
                else {
                    String text = rests.getText().toString();
                    baseSet.setRestTimeInSeconds(Integer.parseInt(text.replaceAll("\\D+","")));
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rests.setSelectAllOnFocus(true);
        rests.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                UpdateGroupView(selectedGroupView,selectedGroup);
                if(!hasFocus)
                {
                    rests.setText(rests.getText()+"s");
                }
                else
                {
                    rests.setText(baseSet.getRestTimeInSeconds()+"");
                    rests.selectAll();
                }
            }
        });
        index.setText((container.getChildCount()+1)+".");

        /// Delete Set Popup ///
        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(getContext(), index);
                popupMenu.inflate(R.menu.delete_view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.delete_set);
                        {
                            int pos = selectedGroup.exerciseSets.indexOf(baseSet);
                            selectedGroup.exerciseSets.remove(pos);
                            UpdateGroupView(selectedGroupView,selectedGroup);
                            verticalScrollView.removeViewAt(pos);
                            for(int i = pos; i<selectedGroup.exerciseSets.size(); i++)
                            {
                                View setView = verticalScrollView.getChildAt(i);
                                TextView index = (TextView)setView.findViewById(R.id.edit_setIndex);
                                index.setText(Integer.toString(i+1)+".");
                            }
                            //SelectGroup(exerciseDay.exerciseGroups.indexOf(selectedGroup), inflater); // TODO Remove the set view properly. Currently it just reloads the day...
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        //// Last set view should lose focus on DONE press. The reason this override is applied only to the last set view is that overriding it in every view leads to weird behaviour.
        //// It causes the next focus to the right field that is  [reps edittext 1] -> [rest edittext 1] -> [reps edittext 2] ->...->[reps edittext n] -> DONE
        //// it does not jump to the last rest edittext(n).
        if(selectedGroup.exerciseSets.indexOf(baseSet)==selectedGroup.exerciseSets.size()-1) {
            reps.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        reps.clearFocus();
                        inputMethodManager.hideSoftInputFromWindow(reps.getWindowToken(),0);
                    }

                    return false;
                }
            });
            rests.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        rests.clearFocus();
                        inputMethodManager.hideSoftInputFromWindow(reps.getWindowToken(),0);
                    }
                    return false;
                }
            });
        }
        return setView;
    }
    //////////////////////////// Group Layout Function ////////////////////////////////////
    public View newGroupView(final ExerciseGroup group, final LayoutInflater inflater, final ViewGroup container)
    {
        final View groupView = inflater.inflate(R.layout.edit_group_view, container, false);
        group.Initialize(getContext());
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
        index.setText((exerciseDay.exerciseGroups.indexOf(group)+1)+"");
        childLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedGroupView==groupView)
                {
                    PopupMenu popupMenu = new PopupMenu(getContext(), selectedGroupView);
                    popupMenu.inflate(R.menu.group_menu);
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId())
                            {
                                case R.id.delete_group:
                                    int pos = groupContainer.indexOfChild(selectedGroupView);
                                    groupContainer.removeView(selectedGroupView);
                                    exerciseDay.exerciseGroups.remove(selectedGroup);
                                    for(int i = pos; i<exerciseDay.exerciseGroups.size(); i++)
                                    {
                                        View v = groupContainer.getChildAt(i);
                                        TextView tv = (TextView)v.findViewById(R.id.group_index);
                                        tv.setText(Integer.toString(i+1));
                                    }
                                    if(pos>0)
                                        SelectGroup(pos-1, inflater);
                                    else {
                                        verticalScrollView.removeAllViews();
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
                    SelectGroup(exerciseDay.exerciseGroups.indexOf(group), inflater);

                }
            }
        });

        return groupView;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.create_program_day_fragment, container, false);
         groupContainer = (LinearLayout)view.findViewById(R.id.create_horizontalExerciseContainer);
        // The program that this function grabs the day from is a clone. It will only get overwritten if the name remains the same.
        exerciseDay = mListener.getDay(position);
        //region FILL HORIZONTAL SCROLL VIEW WITH GROUPS
        for (ExerciseGroup group:exerciseDay.exerciseGroups
             ) {
            groupContainer.addView(newGroupView(group,inflater,groupContainer));
        }
        //endregion
        //region ADD NEW GROUP
        addGroupView = inflater.inflate(R.layout.add_group_view, groupContainer,false);
        inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        groupContainer.addView(addGroupView);
        Button addButton = (Button)addGroupView.findViewById(R.id.edit_addExerciseButton);
        ///// Add Group Button /////
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewSwitcher switcher =(ViewSwitcher)addGroupView.findViewById(R.id.switcher);
                switcher.showNext();
                final AutoCompleteTextView name = (AutoCompleteTextView)addGroupView.findViewById(R.id.overview_editExerciseName);
                name.requestFocus();
                inputMethodManager.toggleSoftInputFromWindow(name.getApplicationWindowToken(),InputMethodManager.SHOW_IMPLICIT,0);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, mListener.getExerciseNames().toArray(new String[mListener.getExerciseNames().size()]));
                name.setThreshold(1);
                name.setAdapter(adapter);
                name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(actionId== EditorInfo.IME_ACTION_DONE) {
                            ExerciseGroup group = new ExerciseGroup(name.getText().toString(), getContext());
                            exerciseDay.exerciseGroups.add(group);
                            int last = groupContainer.getChildCount() - 1;
                            groupContainer.addView(newGroupView(group, inflater, groupContainer), last);
                            switcher.showNext();
                            SelectGroup(last,inflater);
                            inputMethodManager.hideSoftInputFromWindow(name.getWindowToken(),0);
                        }
                        return false;
                    }
                });
            }
        });
        ///// Add Set Button /////
        addSet = (ImageButton)view.findViewById(R.id.create_addSet);
        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = selectedGroup.exerciseSets.size();
                ExerciseSet lastSet;
                ExerciseSet set;
                if(!selectedGroup.exerciseSets.isEmpty()) {
                    lastSet = selectedGroup.exerciseSets.get(size - 1);
                    set = new ExerciseSet(lastSet.getGoalReps(), lastSet.getRestTimeInSeconds());
                }
                else
                    set = new ExerciseSet(12,90);
                selectedGroup.exerciseSets.add(set);
                /// This is placed here because the last set view's EditTexts have a specific KeyEvent listener attached to them(ON IME_DONE) which causes undesired behaviour with
                /// how the next EditText to focus is selected. To prevent that adding a set also has to remove the listener from the previously last set.
                if(verticalScrollView.getChildCount()>0)
                {
                    View prevLastSet = verticalScrollView.getChildAt(size-1);
                    final EditText reps = (EditText)prevLastSet.findViewById(R.id.repsToDo);
                    TextView.OnEditorActionListener nextListener = new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if(actionId==EditorInfo.IME_ACTION_NEXT)
                            {
                                FocusFinder.getInstance().findNextFocus(container,v, View.FOCUS_DOWN).requestFocus();
                                return true;
                            }
                            return false;
                        }
                    };
                    reps.setOnEditorActionListener(nextListener);
                    final EditText rests = (EditText)prevLastSet.findViewById(R.id.rests);
                    rests.setOnEditorActionListener(nextListener);
                }
                ///
                verticalScrollView.addView(newSetView(set, inflater, verticalScrollView));
                UpdateGroupView(selectedGroupView,selectedGroup);
            }
        });
        verticalScrollView=(LinearLayout) view.findViewById(R.id.create_verticalContainer);
        return view;
        //endregion
    }
    @Override
    public void onStart()
    {
        if(!exerciseDay.exerciseGroups.isEmpty())
        {
            SelectGroup(0, getLayoutInflater(null));
            addSet.setVisibility(View.VISIBLE);

        }
        else
            addSet.setVisibility(View.INVISIBLE);

        super.onStart();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProgramEditorInterface) {
            mListener = (ProgramEditorInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void SelectGroup(int position, LayoutInflater inflater)
    {
        addSet.setVisibility(View.VISIBLE);
        if(selectedGroupView!=null) // remove color of previously selected group
            selectedGroupView.findViewById(R.id.group_index).setBackgroundColor(0);
        verticalScrollView.removeAllViews();
        ExerciseGroup group = exerciseDay.exerciseGroups.get(position);
        selectedGroup = group;
        selectedGroupView = groupContainer.getChildAt(position);
        for (int i = 0; i<group.exerciseSets.size(); i++
             ) {
            ExerciseSet set = group.exerciseSets.get(i);
            verticalScrollView.addView(newSetView(set,inflater,verticalScrollView));
        }
        View indexText = selectedGroupView.findViewById(R.id.group_index);
        indexText.setBackgroundColor(Color.argb(10,0, 255, 102));
    }

    public interface ProgramEditorInterface {
        public ExerciseDay getDay(int position);
        public ArrayList<String> getExerciseNames();
    }
}
