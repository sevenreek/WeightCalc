package com.devseven.gympack.materialsetlogger.routinebuilder;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.devseven.gympack.materialsetlogger.R;
import com.devseven.gympack.materialsetlogger.data.ExerciseSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SetListAdapter extends RecyclerView.Adapter<SetListAdapter.SetHolder>
{
    public SetListAdapter()
    {
    }
    public SetListAdapter(ArrayList<ExerciseSet> setList, boolean isExerciseTimed) {
        this.setList = setList;
        this.isExerciseTimed = isExerciseTimed;
    }
    private List<ExerciseSet> setList;
    boolean isExerciseTimed;
    public void clearSetList()
    {
        setList = null;
        isExerciseTimed = false;
        notifyDataSetChanged();
    }
    public void setSetList(List<ExerciseSet> setList, boolean isTimed)
    {
        this.setList = setList;
        isExerciseTimed = isTimed;
        notifyDataSetChanged();
    }

    @Override
    public SetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.builder_set,parent,false);
        return new SetHolder(v);
    }

    @Override
    public void onBindViewHolder(SetHolder holder, int position) {
        holder.bindData(position,setList.get(position),isExerciseTimed);
    }

    @Override
    public int getItemCount() {
        if(setList == null)
            return -1;
        else
            return setList.size();
    }
    protected class SetHolder extends RecyclerView.ViewHolder
    {
        TextView index;
        TextView type;
        View menuButton;
        EditText repCount;

        public SetHolder(View itemView) {
            super(itemView);
            index = (TextView) itemView.findViewById(R.id.setIndex);
            type = (TextView) itemView.findViewById(R.id.typeTextView);
            menuButton = itemView.findViewById(R.id.setMenu);
            repCount = (EditText) itemView.findViewById(R.id.repCount);
            //repCount.setInputType(InputType.TYPE_CLASS_NUMBER); // this may not be neccessary, testing required
            //repCount.setBackground(null);

        }
        public void bindData(int position, final ExerciseSet set, boolean isTimed)
        {
            index.setText(position+1+".");
            type.setText(isTimed?index.getResources().getString(R.string.time_to_last):index.getResources().getString(R.string.reps_to_do));
            repCount.setText(Integer.toString(set.getGoalReps()));
            repCount.addTextChangedListener(new TextWatcher() { // dont know if this should be moved somewhere else
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    set.setGoalReps(Integer.parseInt(repCount.getText().toString()));
                }
            });
        }
    }
}
