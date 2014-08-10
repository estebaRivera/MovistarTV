package com.smartboxtv.movistartv.programation.preview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.social.DialogMessage;
import com.smartboxtv.movistartv.social.DialogShare;

/**
 * Created by Esteban- on 09-08-14.
 */
public class TriviaShare extends Fragment {

    private String puntaje;
    private String message;
    private String programa;

    public TriviaShare() {
    }

    public TriviaShare(String puntaje,String programa ) {
        this.puntaje = puntaje;
        this.programa = programa;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.share_trivia, container,false);

        TextView txtPuntaje = (TextView) rootView.findViewById(R.id.trivia_puntaje_total);
        TextView txtPuntaje2 = (TextView) rootView.findViewById(R.id.trivia_puntaje_total2);
        TextView title = (TextView) rootView.findViewById(R.id.yuhuu);
        TextView subtitle = (TextView) rootView.findViewById(R.id.text_trivia_puntaje);

        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        txtPuntaje.setTypeface(bold);
        txtPuntaje2.setTypeface(normal);
        title.setTypeface(normal);
        subtitle.setTypeface(normal);

        txtPuntaje.setText(puntaje);

        ImageView facebook = (ImageView) rootView.findViewById(R.id.icon_facebook);
        ImageView twitter = (ImageView) rootView.findViewById(R.id.icon_twitter);


        message = "¡Felicitaciones! ¡ Ganaste "+puntaje+" en la Trivia de "+programa+"! ¡Vive la la TV más social que nunca!";
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogShare mensaje = new DialogShare(message, false);
                mensaje.show(getFragmentManager(),"");
            }
        });

        twitter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogShare mensaje = new DialogShare(message, true);
                mensaje.show(getFragmentManager(),"");
            }
        });

        return rootView;
    }
}
