package com.devseven.gympack.materialsetlogger.mainmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public static final int PAGE_COUNT = 2;
    public static final int INDEX_HOME = 0;
    public static final int INDEX_ROUTINES = 1;
    public static final int INDEX_LOG = 2;



    public MainPagerAdapter(FragmentManager fm) {
        super(fm);

    }
    private Fragment[] fragments = new Fragment[PAGE_COUNT];
    @Override
    public Fragment getItem(int position) {
        Fragment f;
        switch(position)
        {
            case INDEX_HOME:
                if(fragments[position] instanceof HomeFragment)
                    f = fragments[position];
                else {
                    f = HomeFragment.newInstance(null);
                    fragments[position] = f;
                }
            break;
            case INDEX_ROUTINES:
                if(fragments[position] instanceof RoutineListFragment)
                    f = fragments[position];
                else {
                    f = RoutineListFragment.newInstance();
                    fragments[position] = f;
                }
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

}
