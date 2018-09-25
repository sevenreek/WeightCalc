package com.devseven.gympack.materialsetlogger.mainmenu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devseven.gympack.materialsetlogger.R;
import com.devseven.gympack.materialsetlogger.data.ExerciseDay;

/**
 * HomeFragment is the fragment that gets displayed as the first thing the user sees in the main menu.
 * It contains basic information about the next exercise day that the user should do, or in the case of
 * an exercise being already in progress shows data about the ongoing day. The buttons present on this widget
 * allow for quick change of the selected exercise day and routine, as well as continue it. The displayed labels
 * provide information such as: completion percentage, exercises to do and perhaps the last exercise done.
 */

public class HomeFragment extends Fragment implements MainFragmentScrollInterface{

    private HomeFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Bundle b) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_v2, container, false);
        return v;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentInteractionListener) {
            mListener = (HomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HomeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFragmentEnter() {
        FloatingActionButton fab = mListener.getFloatingActionButton();
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentExit() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface HomeFragmentInteractionListener extends MainMenuFragmentListener {
        // TODO: Update argument type and name
        void onNextDayStarted();
        void onNextDayChanged(int dayIndex);

    }

}
