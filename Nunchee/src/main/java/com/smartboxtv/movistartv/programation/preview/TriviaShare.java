package com.smartboxtv.movistartv.programation.preview;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.Trivia;
import com.smartboxtv.movistartv.social.DialogShare;

/**
 * Created by Esteban- on 09-08-14.
 */
public class TriviaShare extends Fragment {

    private String puntaje;
    private String message;
    private String programa;
    private String nivel;

    private Program p;
    private Trivia t;

    private boolean isShare;

    public TriviaShare() {
    }

    public TriviaShare(String puntaje, String programa, String nivel, Trivia t, Program p , boolean isShare ) {
        this.puntaje = puntaje;
        this.programa = programa;
        this.nivel = nivel;
        this.p = p;
        this.t = t;
        this.isShare = isShare;

        Log.e("Programa",p.getTitle());
        Log.e("Trivia",""+t.getPreguntas().size());
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
        subtitle.setText("Has completado el nivel "+nivel+" y has conseguido");

        ImageView facebook = (ImageView) rootView.findViewById(R.id.icon_facebook);
        ImageView twitter = (ImageView) rootView.findViewById(R.id.icon_twitter);

        ImageView exit = (ImageView) rootView.findViewById(R.id.share_back);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TriviaMaxFragment fragmentoTrivia = new TriviaMaxFragment(p,t,true);
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.contenedor_trivia,fragmentoTrivia);
                ft.commit();
            }
        });

        final String urlImage = "https://tvsmartbox.com/MovistarTV/icon-post-facebok_trivia.png";
        final String url = "http://www.movistar.cl/PortalMovistarWeb/tv-digital/guia-de-canales";
        final String titulo = "";
        final String description = "Pon a pruebas tus conocimientos y desafía a tus amigos. Con Movistar TV podrás interactuar, descubrir y compartir facetas que nunca imaginaste de " +
                "tus programas favoritos. Descarga Movistar TV y vive la TV más social que nunca!";
        message = "¡Felicitaciones! ¡ Ganaste "+puntaje+" puntos en la Trivia de "+programa;
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogShare mensaje = new DialogShare(titulo, description, urlImage, message, url);
                mensaje.show(getFragmentManager(),"");
            }
        });

        twitter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogShare mensaje = new DialogShare(message+"! ¡Vive la la TV más social que nunca!"+" @MovistarTV", true);
                mensaje.show(getFragmentManager(),"");
            }
        });

        if(isShare == false){
            LinearLayout wrapper = (LinearLayout) rootView.findViewById(R.id.wrapper_share);
            wrapper.setVisibility(View.GONE);

            ImageView yuhu  = (ImageView) rootView.findViewById(R.id.mono_yuhuu);
            Drawable buu = getActivity().getResources().getDrawable(R.drawable.icon_not_big);
            yuhu.setBackground(buu);

            title.setText("¡ Buuuu !");
            subtitle.setText("No has conseguido pasar el nivel "+nivel);
        }

        return rootView;
    }
}
