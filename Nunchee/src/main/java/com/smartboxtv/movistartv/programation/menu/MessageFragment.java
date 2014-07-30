package com.smartboxtv.movistartv.programation.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.smartboxtv.movistartv.R;

/**
 * Created by Esteban- on 12-05-14.
 */
public class MessageFragment extends Fragment {

    public MessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.action_bar_message,container, false);

        return rootView;
    }
}
