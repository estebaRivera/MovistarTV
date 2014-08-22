package com.smartboxtv.movistartv.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.activities.LoginActivity;
import com.smartboxtv.movistartv.activities.RecommendedActivity;
import com.smartboxtv.movistartv.activities.TutorialActivity;
import com.smartboxtv.movistartv.data.preference.UserPreference;

/**
 * Created by Esteban- on 25-07-14.
 */
public class TutorialFragment extends Fragment {

    private String path = null;
    private int id = -1;
    private int position;
    private TutorialActivity tutorialActivity;

    public TutorialFragment(String path) {
        this.path = path;
    }

    public TutorialFragment(int id, TutorialActivity t, int p) {
        this.id = id;
        this.tutorialActivity = t;
        this.position = p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tutorial_fragment,container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.background);
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");

        TextView p1 = (TextView) rootView.findViewById(R.id.tuto_p1);
        TextView p2 = (TextView) rootView.findViewById(R.id.tuto_p2);

        p1.setTypeface(normal);
        p2.setTypeface(normal);

        TextView title = (TextView) rootView.findViewById(R.id.title_slide_tuto);
        title.setTypeface(normal);

        if(id != -1){
            Drawable drawable = getResources().getDrawable(id);
            imageView.setImageDrawable(drawable);
        }
        else if(path!= null){
            AQuery aq = new AQuery(rootView);
            aq.id(imageView).image(path);
        }

        switch (position){

            case 1 :    p1.setTextSize((float) 75);
                        p2.setAlpha((float)0.7);
                        p2.setTextSize((float) 60);
                        title.setText("Selecciona tus preferencias para recibir recomendaciones de programas");
                        break;
            case 2 :    p1.setTextSize((float) 60);
                        p1.setAlpha((float)0.7);
                        p2.setTextSize((float) 75);
                        title.setText("¡Toda la programación Movistar tv en las palmas de tus manos! ");
                        break;
        }

        Button skip = (Button) rootView.findViewById(R.id.tutorial_skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tutorialActivity.onBackPressed();
            }
        });

        return rootView;
    }

}
