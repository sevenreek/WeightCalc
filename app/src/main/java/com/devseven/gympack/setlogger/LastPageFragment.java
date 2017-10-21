package com.devseven.gympack.setlogger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class LastPageFragment extends Fragment {

    private LastPageListener mListener;

    public LastPageFragment() {

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
       View view = inflater.inflate(R.layout.last_page_fragment, container, false);
        final ImageButton createDay = (ImageButton)view.findViewById(R.id.create_addDayButton);
        createDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.AddDay();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LastPageListener) {
            mListener = (LastPageListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LastPageListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface LastPageListener {
        public void AddDay();

    }
}
