package com.devseven.gympack.materialsetlogger;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.devseven.gympack.materialsetlogger.data.ExerciseSet;

import java.util.List;

/**
 * Created by Klejnot Nilu on 17.11.2017.
 */

public class SetRecycler extends RecyclerView.Adapter {
    List<ExerciseSet> setList;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public static class SetHolder extends RecyclerView.ViewHolder
    {

        public SetHolder(View itemView) {
            super(itemView);
        }
    }
}
