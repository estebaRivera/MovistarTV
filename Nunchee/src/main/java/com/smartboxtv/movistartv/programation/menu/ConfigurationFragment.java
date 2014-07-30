package com.smartboxtv.movistartv.programation.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.animation.ManagerAnimation;
import com.smartboxtv.movistartv.programation.delegates.MenuConfigurationDelegate;

/**
 * Created by Esteban- on 09-05-14.
 */
public class ConfigurationFragment extends Fragment {

    private View rootView;
    private MenuConfigurationDelegate configurationDelegate;

    public ConfigurationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.action_bar_configuration ,container, false);

        ImageButton exit = (ImageButton) (rootView != null ? rootView.findViewById(R.id.exit) : null);
        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        RelativeLayout r1 = (RelativeLayout) rootView.findViewById(R.id.config_auto_post);
        RelativeLayout r2 = (RelativeLayout) rootView.findViewById(R.id.config_tutorial);
        RelativeLayout r3 = (RelativeLayout) rootView.findViewById(R.id.config_acerca_de);
        RelativeLayout r4 = (RelativeLayout) rootView.findViewById(R.id.confif_terminos_y_condiciones);

        TextView txtAutoPost = (TextView) rootView.findViewById(R.id.auto_post_principal);
        TextView txtTutorial = (TextView) rootView.findViewById(R.id.tutorial_principal);
        TextView txtAcercaDe = (TextView) rootView.findViewById(R.id.acerca_de_principal);
        TextView txtTerminos = (TextView) rootView.findViewById(R.id.termino_principal);

        TextView txtAutoPost2 = (TextView) rootView.findViewById(R.id.auto_post_secundario);
        TextView txtTutorial2 = (TextView) rootView.findViewById(R.id.tutorial_secundario);
        TextView txtAcercaDe2 = (TextView) rootView.findViewById(R.id.acerca_de_secundario);
        TextView txtTerminos2 = (TextView) rootView.findViewById(R.id.termino_secundario);

        txtAcercaDe.setTypeface(bold);
        txtAutoPost.setTypeface(bold);
        txtTerminos.setTypeface(bold);
        txtTutorial.setTypeface(bold);

        txtAcercaDe2.setTypeface(light);
        txtAutoPost2.setTypeface(light);
        txtTerminos2.setTypeface(light);
        txtTutorial2.setTypeface(light);

        ImageView arrow = (ImageView) rootView.findViewById(R.id.configuration_arrow);
        ImageView imageExit = (ImageView) rootView.findViewById(R.id.exit);
        RelativeLayout wrapper = (RelativeLayout) rootView.findViewById(R.id.wrapper);

        ManagerAnimation.fade(wrapper, arrow, imageExit);

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(configurationDelegate != null){
                    configurationDelegate.changedState(false);
                }
                /*RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.contenedor_preview);
                r.removeAllViews();*/

                finish();

            }
        });
        return rootView;
    }
    public void finish(){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(rootView, View.ALPHA,1,0);
        AnimatorSet animView = new AnimatorSet();

        animView.play(animatorX);
        animView.setDuration(400);

        animView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                onDetach();
                onDestroy();
                /*RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.contenedor_menu_bar);
                r.removeAllViews();*/
                RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.contenedor_preview);
                r.removeAllViews();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animView.start();
    }
    public void setConfigurationDelegate(MenuConfigurationDelegate configurationDelegate) {
        this.configurationDelegate = configurationDelegate;
    }
}
