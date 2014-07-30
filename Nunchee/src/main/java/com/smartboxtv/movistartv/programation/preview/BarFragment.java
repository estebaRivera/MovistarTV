package com.smartboxtv.movistartv.programation.preview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartboxtv.movistartv.R;

/**
 * Created by Esteban- on 06-05-14.
 */
public class BarFragment extends Fragment {

    public BarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.preview_fg_bar,container, false);
        return rootView;
    }
}
