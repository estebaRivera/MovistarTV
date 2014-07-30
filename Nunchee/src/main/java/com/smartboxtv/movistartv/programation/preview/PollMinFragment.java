package com.smartboxtv.movistartv.programation.preview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.models.Polls;

/**
 * Created by Esteban- on 07-05-14.
 */
public class PollMinFragment extends Fragment {

    private Polls polls;

    public PollMinFragment() {
    }

    public PollMinFragment(Polls polls) {
        this.polls = polls;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.preview_fg_poll_min,container, false);

        TextView txtQuestion = (TextView) rootView.findViewById(R.id.una_encuesta);
        TextView txtAnswer = (TextView) rootView.findViewById(R.id.una_encuesta_respuesta);
        TextView txtTitle = (TextView) rootView.findViewById(R.id.txt_title);
        RelativeLayout containerOneQuestion = (RelativeLayout) rootView.findViewById(R.id.contenedor_una_encuesta);

        // Set Typeface
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Light.ttf");

        txtAnswer.setTypeface(light);
        txtQuestion.setTypeface(bold);
        txtTitle.setTypeface(normal);
        RelativeLayout containerNoPolls = (RelativeLayout) rootView.findViewById(R.id.preview_encuesta_contenedor);

        /*if(!polls.getPreguntas().isEmpty()){
            txtQuestion.setText(polls.getPreguntas().get(0).getText());

            int votos = 0;
            for(int i = 0;i< polls.getPreguntas().get(0).getRespuestas().size();i++){
                votos= votos+ polls.getPreguntas().get(0).getRespuestas().get(i).getVotos();
            }
            txtAnswer.setText("" + votos + " persona han respondido");
        }
        else{*/
            containerOneQuestion.setVisibility(View.GONE);
            containerNoPolls.setVisibility(View.VISIBLE);
        //}
        return rootView;
    }
}
