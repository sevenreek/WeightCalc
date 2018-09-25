package com.devseven.gympack.materialsetlogger.mainmenu;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class MainPagerAdapterChangeListener implements ViewPager.OnPageChangeListener {
    FragmentPagerAdapter pagerAdapter;
    public MainPagerAdapterChangeListener(FragmentPagerAdapter pagerAdapter)
    {
        this.pagerAdapter = pagerAdapter;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MainFragmentScrollInterface fragmentToShow = (MainFragmentScrollInterface)pagerAdapter.getItem(position);
        fragmentToShow.onFragmentEnter();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
