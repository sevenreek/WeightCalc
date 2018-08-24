package com.devseven.gympack.materialsetlogger.mainmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public static final int PAGE_COUNT = 1;
    public static final int INDEX_HOME = 0;
    public static final int INDEX_ROUTINES = 1;
    public static final int INDEX_EXERCISES = 2;
    public static final int INDEX_LOG = 3;

    private MainPagerInteractionListener mainListener;

    public MainPagerAdapter(FragmentManager fm, MainPagerInteractionListener listener) {
        super(fm);
        mainListener = listener;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment f;
        switch(position)
        {
            case INDEX_HOME:
                f = HomeFragment.newInstance(mainListener.getFragmentBundle(INDEX_HOME));
            break;
            default:
                f = null;
            break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    public interface MainPagerInteractionListener
    {
        Bundle getFragmentBundle(int index);

    }
}
