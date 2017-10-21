package com.devseven.gympack.setlogger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateProgramActivity extends AppCompatActivity implements EditDayPageFragment.ProgramEditorInterface, LastPageFragment.LastPageListener {

    private static int NUM_PAGES;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    EditText currentTab;
    ImageButton nextTab;
    ImageButton prevTab;
    Program program;
    ArrayList<String> exerciseNames;
    Toolbar toolbar;
    EditText programName;
    InputMethodManager inm;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);
        inm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        programName = (EditText)toolbar.findViewById(R.id.create_programName);

        programName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    inm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
                    programName.selectAll();
                }
            }
        });
        programName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE);
                {
                    program.setName(programName.getText().toString());
                    inm.hideSoftInputFromWindow(programName.getWindowToken(),0);
                    programName.clearFocus();
                    programName.setFocusableInTouchMode(false);
                }
                return false;
            }
        });
        programName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                program.setName(programName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(GlobalSettings.getProgramToEdit()!=null) {
            program = GlobalSettings.getProgramToEdit().clone(this);
            programName.setFocusableInTouchMode(false);
            programName.setText(program.getName());
        }
        else {
            program = new Program();
            program.setName("New Program");
            programName.setFocusableInTouchMode(true);
            programName.clearFocus();
            programName.requestFocus();
            inm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        }
        NUM_PAGES = program.days.size()+1;
        mPager = (ViewPager)findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        //region EXERCISE NAMES FROM FILES
        if(Exercise.exerciseStorageDir==null)
            Exercise.exerciseStorageDir = new File(getFilesDir(),GlobalSettings.EXERCISES_STORAGE_FOLDER_NAME);
        if(!Exercise.exerciseStorageDir.exists())
            Exercise.exerciseStorageDir.mkdirs();
        exerciseNames = new ArrayList<>(Arrays.asList(Exercise.exerciseStorageDir.list()));
        //endregion
        //region TABS VIEWS SETUP
        currentTab = (EditText)findViewById(R.id.create_currentSelectedTab);
        nextTab = (ImageButton)findViewById(R.id.create_nextTab);
        nextTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPager.getCurrentItem()!=mPagerAdapter.getCount())
                    mPager.setCurrentItem(mPager.getCurrentItem()+1);
            }
        });
        prevTab = (ImageButton)findViewById(R.id.create_previousTab);
        prevTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPager.getCurrentItem()!=0)
                    mPager.setCurrentItem(mPager.getCurrentItem()-1);
            }
        });
        //endregion

        //region PAGE CHANGE LISTENERS
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==NUM_PAGES-1)
                {
                    currentTab.setText("Add Day");
                    deleteDayMenuItem.setVisible(false);
                    SetTabAccesible(false);
                    inm.hideSoftInputFromWindow(currentTab.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else
                {
                    deleteDayMenuItem.setVisible(true);
                    // grab day name from list.
                    currentTab.setText(program.days.get(position).getName());
                    SetTabAccesible(true);


                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //endregion
        //region TAB TEXT LISTENERS
        UpdateTab();
        currentTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTab.setCursorVisible(true);
            }
        });
        currentTab.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                currentTab.setCursorVisible(false);
                if(event!=null && (event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
                    inm.hideSoftInputFromWindow(currentTab.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        currentTab.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mPager.getCurrentItem()!=mPagerAdapter.getCount()-1)
                    program.days.get(mPager.getCurrentItem()).setName(currentTab.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //endregion
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.save_program:
                SerializeAndAddProgram(program, true);
                return true;
            case R.id.change_program_name:
                Log.d(this.toString(),"???");
                programName.setFocusableInTouchMode(true);
                programName.requestFocus();
                inm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                break;
            case R.id.delete_program:
                DeleteProgram(program);
                break;
            ///////////////// Delete Current Day //////////////////////////
            case R.id.delete_day:
                int position = mPager.getCurrentItem();
                if(program.days.isEmpty()) {
                    Log.e(this.toString(),"Delete Day was pressed even though there are no days! Something is wrong!");
                }
                else {
                    deleteDayMenuItem.setVisible(true);
                    NUM_PAGES--;
                    Toast.makeText(this, program.days.get(position).getName() + " deleted.", Toast.LENGTH_SHORT).show();
                    program.days.remove(position);
                    mPagerAdapter.notifyDataSetChanged();
                    if(program.days.isEmpty()) {
                        deleteDayMenuItem.setVisible(false);
                    }
                    UpdateTab();
                }

        }
        return false;
    }
    ////// Function for updating currentTab text ///////
    void UpdateTab()
    {
        int position = mPager.getCurrentItem();
        if(position==NUM_PAGES-1)
        {
            SetTabAccesible(false);
            currentTab.setText("Add Day");
        }
        else
        {
            SetTabAccesible(true);
            currentTab.setText(program.days.get(position).getName());
        }
    }

    void SerializeAndAddProgram(Program program, boolean exitActivity)
    {
        Serializer serializer = new Persister();
        File storageDir = new File(getFilesDir(),GlobalSettings.DIRECTORY_PROGRAMS);
        if(!storageDir.exists())
            storageDir.mkdirs();
        File file = new File(storageDir,program.getName());
        try {
            serializer.write(program,file);
        } catch (Exception e) {
            Toast.makeText(this, "Failed saving program. If this problem reoccurs please inform the developer.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        if(exitActivity)
        {
            finish();
        }
    }
    void DeleteProgram(Program prg)
    {
        String name = prg.getName();
        File storageDir = new File(getFilesDir(),GlobalSettings.DIRECTORY_PROGRAMS);
        boolean found = false;
        for (String s:storageDir.list()
             ) {
            if(s.equals(name))
            {
                File f = new File(storageDir,s);
                f.delete();
                found=true;
                break;
            }

        }
        if(!found)
            Toast.makeText(this, "Deleting failed. The program may already not exist. Exiting activity.", Toast.LENGTH_SHORT).show();
        finish();
        return;
    }
    MenuItem deleteDayMenuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_create_program,menu);
        deleteDayMenuItem = menu.findItem(R.id.delete_day);
        if(program.days.isEmpty())
            deleteDayMenuItem.setVisible(false);
        else
            deleteDayMenuItem.setVisible(true);
        return true;
    }
    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public ExerciseDay getDay(int position) {
        return program.days.get(position);
    }

    @Override
    public ArrayList<String> getExerciseNames() {
        return exerciseNames;
    }
    public void SetTabAccesible(boolean accessible)
    {
        currentTab.setClickable(accessible);
        currentTab.setFocusableInTouchMode(accessible);
        currentTab.setFocusable(accessible);
        if(!accessible) {
            currentTab.clearFocus();
        }
    }

    @Override
    public void AddDay() {
        ExerciseDay day = new ExerciseDay("Day"+program.days.size());
        program.days.add(day);
        NUM_PAGES++;
        deleteDayMenuItem.setVisible(true);
        mPagerAdapter.notifyDataSetChanged();
        SetTabAccesible(true);
        currentTab.requestFocus();
        currentTab.setHint("New Day");
        currentTab.setText("Day "+(NUM_PAGES-1));
        currentTab.selectAll();
        inm.showSoftInput(currentTab, InputMethodManager.SHOW_IMPLICIT);


    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            if(position==NUM_PAGES-1) {

                return new LastPageFragment();
            }

            EditDayPageFragment edpf = new EditDayPageFragment();
            Bundle args = new Bundle();
            args.putInt(EditDayPageFragment.ARG_POSTION, position);
            edpf.setArguments(args);
            return edpf;
        }
        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE; // TODO THIS IS A HACK. FIND A BETTER WAY...
        }
        @Override
        public int getCount()
        {
            return NUM_PAGES;
        }

    }



}
