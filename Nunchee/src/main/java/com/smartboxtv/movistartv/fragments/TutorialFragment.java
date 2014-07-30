package com.smartboxtv.movistartv.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.activities.LoginActivity;
import com.smartboxtv.movistartv.activities.RecommendedActivity;
import com.smartboxtv.movistartv.data.preference.UserPreference;

/**
 * Created by Esteban- on 25-07-14.
 */
public class TutorialFragment extends Fragment {

    private String path = null;
    private int id = -1;

    public TutorialFragment(String path) {
        this.path = path;
    }

    public TutorialFragment(int id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tutorial_fragment,container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.background);

        if(id != -1){
            Drawable drawable = getResources().getDrawable(id);
            imageView.setImageDrawable(drawable);
        }
        else if(path!= null){
            AQuery aq = new AQuery(rootView);
            aq.id(imageView).image(path);
        }

        Button skip = (Button) rootView.findViewById(R.id.tutorial_skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(UserPreference.isShowTutorial(getActivity())) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                UserPreference.setShowTutorial(false, getActivity());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Log.e("FragmentTutoral", "Login");
                }
            else{
                  Log.e("FragmentTutorial","Login NO");
                  Intent intent = new Intent(getActivity(), RecommendedActivity.class);
                  UserPreference.setShowTutorial(false, getActivity());
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(intent);
            }
            }
        });

        return rootView;
    }

}
