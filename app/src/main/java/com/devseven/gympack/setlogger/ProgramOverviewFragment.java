package com.devseven.gympack.setlogger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dickbutt on 09.06.2017.
 */

public class ProgramOverviewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_pick_exercise, container, false);
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();

    }
}
