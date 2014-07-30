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
import com.smartboxtv.movistartv.data.models.Trivia;

/**
 * Created by Esteban- on 07-05-14.
 */
public class TriviaMinFragment extends Fragment {

    private Trivia trivia;

    public TriviaMinFragment(Trivia trivia) {
        this.trivia = trivia;
    }

    public TriviaMinFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.preview_fg_trivia_min, container, false);

        TextView txtQuestion = (TextView) rootView.findViewById(R.id.una_trivia);
        TextView txtAnswer = (TextView) rootView.findViewById(R.id.una_trivia_respuesta);
        TextView txtTitle = (TextView) rootView.findViewById(R.id.txt_title);

        RelativeLayout containerOneQuestion = (RelativeLayout) rootView.findViewById(R.id.contenedor_una_trivia);
        RelativeLayout containerNoTrivia = (RelativeLayout) rootView.findViewById(R.id.preview_trivia_contenedor);

        // Set Typeface
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Light.ttf");

        txtAnswer.setTypeface(light);
        txtQuestion.setTypeface(bold);
        txtTitle.setTypeface(normal);

        /*if(!trivia.getPreguntas().isEmpty()){
            txtQuestion.setText(trivia.getPreguntas().get(0).getText());
            int votos = 0;

            /*for(int i = 0;i< trivia.getPreguntas().get(0).getRespuestas().size();i++){
            }*/
            /*txtAnswer.setText("" + votos + " es tu puntaje ");
        }
        else{*/
            containerOneQuestion.setVisibility(View.GONE);
            containerNoTrivia.setVisibility(View.VISIBLE);
        //}
        return rootView;
    }
}
