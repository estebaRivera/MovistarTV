package com.smartboxtv.movistartv.programation.menu;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartboxtv.movistartv.R;

/**
 * Created by Esteban- on 12-05-14.
 */
public class About extends DialogFragment{

    public About() {
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.about, container, false);

        /*ImageView containerAbout = (ImageView) (rootView != null ? rootView.findViewById(R.id.container_about) : null);

        containerAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.contenedor_preview);
                r.removeAllViews();
            }
        });*/

        return rootView;
    }
}
