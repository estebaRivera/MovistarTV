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
import com.smartboxtv.movistartv.data.models.Trivia;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.services.DataLoader;

/**
 * Created by Esteban- on 06-05-14.
 */
public class TriviaFragment extends Fragment {

    private Trivia trivia = null;
    private TextView txtName;
    private TextView txtQuestion;
    private TextView textDetail;

    public TriviaFragment(Trivia trivia) {
        this.trivia = trivia;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.preview_fg_trivia, container,false);

        txtName = (TextView) rootView.findViewById(R.id.accion_nombre_trivia);
        txtQuestion = (TextView) rootView.findViewById(R.id.accion_pregunta_trivia);
        textDetail = (TextView) rootView.findViewById(R.id.accion_detalle_trivia);

        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");

        txtName.setTypeface(normal);
        textDetail.setTypeface(normal);
        txtQuestion.setTypeface(bold);

        loadData();
        return rootView;
    }

    public void  loadData(){

        DataLoader dataLoader = new DataLoader(getActivity());
        dataLoader.getScoreTrivia(new DataLoader.DataLoadedHandler<String>() {

            @Override
            public void loaded(String data) {
                super.loaded(data);

                if (trivia.getPreguntas().size() > 0) {
                    txtName.setText("Trivia");
                    txtQuestion.setText(trivia.getPreguntas().get(0).getText());
                    textDetail.setText("Tu puntaje es : " + data);
                    Log.e("Trivia", "Carga Data");
                }
            }
        }, UserPreference.getIdNunchee(getActivity()));
    }

    public Trivia getTrivia() {
        return trivia;
    }

    public void setTrivia(Trivia trivia) {
        this.trivia = trivia;
    }
}
