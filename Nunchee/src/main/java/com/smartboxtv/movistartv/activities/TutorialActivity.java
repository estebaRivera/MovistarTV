package com.smartboxtv.movistartv.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.smartboxtv.movistartv.Adapter.AdapterPagerTutorial;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.fragments.TutorialFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Esteban- on 25-07-14.
 */
public class TutorialActivity extends ActionBarActivity {

    private List<Fragment> listFragments = new ArrayList<Fragment>();
    private AdapterPagerTutorial adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);

        viewPager = (ViewPager) findViewById(R.id.pager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TutorialFragment fragment1 = new TutorialFragment(R.drawable.slide_1);
        TutorialFragment fragment2 = new TutorialFragment(R.drawable.slide_2);
        TutorialFragment fragment3 = new TutorialFragment(R.drawable.slide_3);

        listFragments.add(fragment1);
        listFragments.add(fragment2);
        listFragments.add(fragment3);

        adapter = new AdapterPagerTutorial(getSupportFragmentManager());
        adapter.setFragments(listFragments);

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (this.viewPager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() - 1);

    }

}
