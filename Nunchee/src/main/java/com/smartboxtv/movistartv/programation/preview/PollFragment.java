package com.smartboxtv.movistartv.programation.preview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.models.Polls;

/**
 * Created by Esteban- on 06-05-14.
 */
public class PollFragment extends Fragment {

    private final Polls polls;
    private TextView txtName;
    private TextView txtQuestion;
    private TextView txtDetail;

    public PollFragment(Polls polls) {
        this.polls = polls;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.preview_fg_poll, container,false);

        txtName = (TextView) rootView.findViewById(R.id.accion_nombre);
        txtQuestion = (TextView) rootView.findViewById(R.id.accion_pregunta);
        txtDetail = (TextView) rootView.findViewById(R.id.accion_detalle);

        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");

        txtName.setTypeface(normal);
        txtDetail.setTypeface(normal);
        txtQuestion.setTypeface(bold);

        cargaData();
        return rootView;

    }
    public void cargaData(){

        int contador = 0;

        if(polls.getPreguntas().size() >0){

            for(int i = 0;i < polls.getPreguntas().get(0).getRespuestas().size();i++){
                contador = polls.getPreguntas().get(0).getRespuestas().get(i).getVotos()+ contador;
            }

            if(polls.getPreguntas().size()>0){
                txtName.setText("Encuesta");
                txtQuestion.setText(polls.getPreguntas().get(0).getText());
                txtDetail.setText(contador + " personas han respondido ");
                Log.e("Encuesta", "Carga Data");
            }
        }


    }
}
