package com.fimrc.mysensornetwork.gui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import java.util.ArrayList;

/**
 * Created by Sven on 17.03.2017.
 */

class TabsAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener{

    private final Context context;
    private final ActionBar actionBar;
    private final ViewPager viewPager;
    private final ArrayList<TabInfo> tabs = new ArrayList<>();

    static final class TabInfo{
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args){
            clss = _class;
            args = _args;
        }
    }

    public TabsAdapter(MainActivity activity, ViewPager pager){
        super(activity.getSupportFragmentManager());
        context = activity;
        actionBar = activity.getSupportActionBar();
        viewPager = pager;
        viewPager.setAdapter(this);
        viewPager.setOnPageChangeListener(this);
    }

    public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args){
        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        tabs.add(info);
        actionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = tabs.get(position);
        return Fragment.instantiate(context, info.clss.getName(), info.args);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
        Object tag = tab.getTag();
        for(int i = 0; i<tabs.size(); i++){
            if(tabs.get(i) == tag)
                viewPager.setCurrentItem(i);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
