package com.devseven.gympack.materialsetlogger.mainmenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devseven.gympack.materialsetlogger.R;
import com.devseven.gympack.materialsetlogger.routinebuilder.RoutineBuilderActivity;
import com.devseven.gympack.materialsetlogger.datacontroller.ApplicationFileManager;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class RoutineListFragment extends Fragment implements MainFragmentScrollInterface, MainFragmentOnDataChangeListener {


    String[] routineNames;
    MainMenuFragmentListener listener;
    RecyclerView recyclerView;
    public RoutineListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.dayRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        repopulateRoutineNames(ApplicationFileManager.getRoutineFilenames(getContext()));
        super.onViewCreated(view, savedInstanceState);

    }

    public static RoutineListFragment newInstance() {
        RoutineListFragment fragment = new RoutineListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  MainMenuFragmentListener)
            listener = (MainMenuFragmentListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement MainMenuFragmentListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }



    @Override
    public void onFragmentEnter() {
        if(routineNames == null)
        {
            routineNames = ApplicationFileManager.getRoutineFilenames(getContext());
        }
        else
        {

        }
        listener.getFloatingActionButton().setVisibility(View.VISIBLE);
        listener.getFloatingActionButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RoutineBuilderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void repopulateRoutineNames(String[] list) {
        recyclerView.setAdapter(new RoutineRecyclerAdapter(Arrays.asList(ApplicationFileManager.getRoutineFilenames(getContext())),getContext()));
    }
    @Override
    public void onFragmentExit() {

    }

    @Override
    public void invalidateFragmentData() {
        routineNames = null;
    }
}
