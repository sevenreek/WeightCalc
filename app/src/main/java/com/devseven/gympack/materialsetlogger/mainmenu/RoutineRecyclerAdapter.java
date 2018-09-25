package com.devseven.gympack.materialsetlogger.mainmenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devseven.gympack.materialsetlogger.R;
import com.devseven.gympack.materialsetlogger.data.ExerciseDay;
import com.devseven.gympack.materialsetlogger.datacontroller.ApplicationFileManager;
import com.devseven.gympack.materialsetlogger.data.Routine;



import java.util.List;

/** The adapter handles the creation of views for the recyclerview that displays user created routines.
 *  At the moment the adapter gets a list of filenames passed to it. Routines are created when views are needed.
 *  Testing is required to find out which way is faster: load all routines at the fragment creation or load them one by one.
 *  Touching a routine view should expand its days and allow the user to choose one of them to start.
 *  By clicking the edit button located next to the routine name the user is able to open RoutineBuilder with the specified routine.
 */

public class RoutineRecyclerAdapter extends RecyclerView.Adapter<RoutineRecyclerAdapter.RoutineViewHolder> {
    public RoutineRecyclerAdapter(List<String> filenames, Context context) {
        this.filenames = filenames;
        this.context = context;
    }

    private List<String> filenames;
    private Context context;
    @Override
    public RoutineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_list_element,parent,false);
        final RoutineViewHolder RVH = new RoutineViewHolder(v);

        return RVH;
    }

    @Override
    public void onBindViewHolder(final RoutineViewHolder holder, int position) {
        try {
            final Routine routine = ApplicationFileManager.getRoutine(filenames.get(position),context);
            holder.setRoutineNameText(routine.getName());
            holder.routineNameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.expandContractDetails(routine);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return filenames.size();
    }
    public class RoutineViewHolder extends RecyclerView.ViewHolder {
        TextView routineNameText;
        ImageView editButton;
        LinearLayout daysContainer;
        boolean daysFilled;
        boolean extended;

        public RoutineViewHolder(View itemView) {
            super(itemView);
            routineNameText = (TextView) itemView.findViewById(R.id.routineNameText);
            editButton = (ImageView) itemView.findViewById(R.id.editRoutineButton);
            daysContainer = (LinearLayout) itemView.findViewById(R.id.expandedDayList);
        }

        public void setRoutineNameText(String routineNameText) {
            this.routineNameText.setText(routineNameText);
        }
        public void expandContractDetails(Routine routine)
        {
            extended = !extended;
            if(!daysFilled) {
                for (ExerciseDay exerciseDay : routine.getDays()
                        ) {
                    View exerciseDayView = LayoutInflater.from(routineNameText.getContext()).inflate(R.layout.routine_list_day_element, daysContainer, false);
                    TextView dayNameLabel = (TextView) exerciseDayView.findViewById(R.id.dayName);
                    dayNameLabel.setText(exerciseDay.getName());
                    daysContainer.addView(exerciseDayView);

                }
                daysFilled = true;
            }
            daysContainer.setVisibility(extended?View.VISIBLE:View.GONE);

        }
    }
}
