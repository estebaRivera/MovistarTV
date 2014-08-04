package com.smartboxtv.movistartv.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 25-07-14.
 */
public class AdapterPagerTutorial extends FragmentPagerAdapter{

    private List<Fragment> fragments = new ArrayList<Fragment>();

    public AdapterPagerTutorial(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment f){
        fragments.add(f);
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
