package com.devseven.gympack.setlogger;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class LogsActivity extends AppCompatActivity {

    MaterialCalendarView calendarView;
    Serializer serializer;
    ExerciseDay selectedDay;
    int baseHeight;
    TextView dayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        dayName = (TextView)findViewById(R.id.dayName);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        File logsDir = new File(getFilesDir(),GlobalSettings.DIRECTORY_LOGS);
        ArrayList<CalendarDay> dates = new ArrayList<CalendarDay>();
        for (String s:logsDir.list()
             ) {
            try {
                Date date = GlobalSettings.LOGS_DATE_FORMAT.parse(s);
                dates.add(CalendarDay.from(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        final DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        serializer = new Persister();
        final ExpandableListView expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String fileName = GlobalSettings.LOGS_DATE_FORMAT.format(date.getDate());
                File loc = new File(getFilesDir()+File.separator+GlobalSettings.DIRECTORY_LOGS+File.separator+fileName);
                try {
                    selectedDay = serializer.read(ExerciseDay.class, loc);
                    MyExpandableListAdapter listAdapter = new MyExpandableListAdapter(selectedDay, getBaseContext(), R.drawable.hashtag_icon, R.drawable.weight_icon);
                    expandableListView.setAdapter(listAdapter);
                    int height = 0;
                    for (ExerciseGroup g:selectedDay.exerciseGroups
                         ) {
                        height+=Math.ceil(36*metrics.density);
                        baseHeight = height+8;
                    }
                    expandableListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,baseHeight));
                    dayName.setText(selectedDay.getProgramName()+" - "+selectedDay.getName());
                    dayName.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                int height = expandableListView.getHeight();
                for (ExerciseSet s:selectedDay.exerciseGroups.get(i).exerciseSets
                        ) {
                    height+=Math.ceil(34*metrics.density);
                }
                expandableListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height));
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                int height = expandableListView.getHeight();
                for (ExerciseSet s:selectedDay.exerciseGroups.get(i).exerciseSets
                        ) {
                    height-=Math.ceil(34*metrics.density);
                }
                if(height<baseHeight)
                    height=baseHeight;
                expandableListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height));
            }
        });
        MyCalendarDecorator decorator = new MyCalendarDecorator(dates);
        DisableDecorator disableDecorator = new DisableDecorator(dates);
        calendarView.addDecorator(disableDecorator);
        calendarView.addDecorator(decorator);





    }
}


class DisableDecorator implements  DayViewDecorator {

    public DisableDecorator(Collection<CalendarDay> dates) {this.dates = new HashSet<>(dates); }
    HashSet<CalendarDay> dates;
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if(dates.contains(day))
            return false;
        else
            return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
    }
}

class MyCalendarDecorator implements DayViewDecorator {
    public MyCalendarDecorator(Collection<CalendarDay> dates)
    {
        this.dates = new HashSet<>(dates);
    }
    HashSet<CalendarDay> dates;
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if(dates.contains(day)) {
            Log.d(this.toString(),"Found a day to decorate: "+day.getDay()+"-"+(day.getMonth()+1)+"-"+day.getYear());
            return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(DotSpan.DEFAULT_RADIUS, Color.RED));
        view.setDaysDisabled(false);
    }
}
class MyExpandableListAdapter extends BaseExpandableListAdapter {

    int wICO;
    int rICO;
    Context context;
    public MyExpandableListAdapter(ExerciseDay day, Context context, int rICO, int wICO)
    {
        this.day = day;
        this.context = context;
        this.wICO = wICO;
        this.rICO = rICO;
    }
    ExerciseDay day;
    @Override
    public int getGroupCount() {
        return day.exerciseGroups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return day.exerciseGroups.get(i).exerciseSets.size();
    }

    @Override
    public Object getGroup(int i) {
        return day.exerciseGroups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return day.exerciseGroups.get(i).exerciseSets.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String groupName = day.exerciseGroups.get(i).getExerciseName();
        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.log_group_header, null);
        }
        TextView nameTV = (TextView)view.findViewById(R.id.groupName);
        nameTV.setText(groupName);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ExerciseSet set = day.exerciseGroups.get(i).exerciseSets.get(i1);
        String index = Integer.toString(i1+1);
        DecimalFormat df = new DecimalFormat();
        df.setDecimalSeparatorAlwaysShown(false);
        String weight = df.format(set.getUsedWeight());
        String repsDone = Integer.toString(set.getDoneReps());
        String repsToDo = Integer.toString(set.getGoalReps());
        String reps = repsDone+"/"+repsToDo;
        if(view==null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.log_details,null);
        }
        TextView indexTV = (TextView)view.findViewById(R.id.edit_setIndex);
        TextView weightsTV = (TextView)view.findViewById(R.id.usedWeight);
        TextView repsTV = (TextView)view.findViewById(R.id.repsDone);
        ImageView repsICO = (ImageView)view.findViewById(R.id.repsICO);
        ImageView weightICO = (ImageView)view.findViewById(R.id.weightICO);
        repsICO.setImageResource(rICO);
        weightICO.setImageResource(wICO);
        indexTV.setText(index+".");
        weightsTV.setText(weight+GlobalSettings.getWeightUnit(day.usedImperial(),context));
        repsTV.setText(reps);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
