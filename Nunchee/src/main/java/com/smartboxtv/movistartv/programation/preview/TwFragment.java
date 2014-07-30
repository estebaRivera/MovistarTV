package com.smartboxtv.movistartv.programation.preview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.delgates.SeconPreviewDelegate;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Esteban- on 06-05-14.
 */
public class TwFragment extends Fragment {

    private TextView hashtags;
    private View rootView;
    private final Program programaPreview;
    private AQuery aq;
    private ScrollView scroll_tw;

    public RelativeLayout relative1;
    public RelativeLayout relative2;
    public RelativeLayout relative3;
    public RelativeLayout relative4;
    public RelativeLayout relative5;
    public RelativeLayout relative6;
    public RelativeLayout relative7;
    public RelativeLayout relative8;
    public RelativeLayout relative9;
    public RelativeLayout relative10;

    private SeconPreviewDelegate delegate;


    public TwFragment(Program program) {
        this.programaPreview = program;
    }

    public void setDelegate(SeconPreviewDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.preview_fg_tw, container, false);
        hashtags = (TextView) rootView.findViewById(R.id.preview_tw_canal);
        scroll_tw = (ScrollView)rootView.findViewById(R.id.preview_scroll_tws);

        relative1 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw);
        relative2 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_2);
        relative3 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_3);
        relative4 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_4);
        relative5 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_5);
        relative6 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_6);
        relative7 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_7);
        relative8 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_8);
        relative9 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_9);
        relative10 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_10);

        aq = new AQuery(rootView);
        setData();

        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });

        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });
        relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });
        relative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });
        relative5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });
        relative6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });
        relative7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });
        relative8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });

        relative9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });
        relative10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delegate != null){
                    delegate.loadFragments();
                }
            }
        });
        return rootView;
    }

    public void setData(){

        if(programaPreview.getHashtags().split(";").length>0){
            hashtags.setText(programaPreview.getHashtags().split(";")[0]);
        }
        else if(programaPreview.getHashtags().split(";").length==0){
            hashtags.setText("@"+programaPreview.getPChannel().getChannelCallLetter());
        }

        if( programaPreview.getTweets()!= null){

            if(!programaPreview.getTweets().isEmpty()){
                cargarTws();
                recorreTws();
            }
            else{
                noTws();
            }
        }
        else{
            noTws();
        }

    }
    public void cargarTws(){

        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface myTypefaceBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        TextView nombre = (TextView) rootView.findViewById(R.id.tw_nombre);
        TextView usuario = (TextView) rootView.findViewById(R.id.tw_nombre_usuario);
        TextView texto = (TextView) rootView.findViewById(R.id.tw_texto);

        TextView nombre2 = (TextView) rootView.findViewById(R.id.tw_nombre_2);
        TextView usuario2 = (TextView) rootView.findViewById(R.id.tw_nombre_usuario_2);
        TextView texto2 = (TextView) rootView.findViewById(R.id.tw_texto_2);

        TextView nombre3 = (TextView) rootView.findViewById(R.id.tw_nombre_3);
        TextView usuario3 = (TextView) rootView.findViewById(R.id.tw_nombre_usuario_3);
        TextView texto3 = (TextView) rootView.findViewById(R.id.tw_texto_3);

        TextView nombre4 = (TextView) rootView.findViewById(R.id.tw_nombre_4);
        TextView usuario4 = (TextView) rootView.findViewById(R.id.tw_nombre_usuario_4);
        TextView texto4 = (TextView) rootView.findViewById(R.id.tw_texto_4);

        TextView nombre5 = (TextView) rootView.findViewById(R.id.tw_nombre_5);
        TextView usuario5 = (TextView) rootView.findViewById(R.id.tw_nombre_usuario_5);
        TextView texto5 = (TextView) rootView.findViewById(R.id.tw_texto_5);

        TextView nombre6 = (TextView) rootView.findViewById(R.id.tw_nombre_6);
        TextView usuario6 = (TextView) rootView.findViewById(R.id.tw_nombre_usuario_6);
        TextView texto6 = (TextView) rootView.findViewById(R.id.tw_texto_6);

        TextView nombre7 = (TextView) rootView.findViewById(R.id.tw_nombre_7);
        TextView usuario7 = (TextView) rootView.findViewById(R.id.tw_nombre_usuario_7);
        TextView texto7 = (TextView) rootView.findViewById(R.id.tw_texto_7);

        TextView nombre8 = (TextView) rootView.findViewById(R.id.tw_nombre_8);
        TextView usuario8 = (TextView) rootView.findViewById(R.id.tw_nombre_usuario_8);
        TextView texto8 = (TextView) rootView.findViewById(R.id.tw_texto_8);

        TextView nombre9 = (TextView) rootView.findViewById(R.id.tw_nombre_9);
        TextView usuario9 = (TextView) rootView.findViewById(R.id.tw_nombre_usuario_9);
        TextView texto9 = (TextView) rootView.findViewById(R.id.tw_texto_9);

        TextView nombre10 = (TextView) rootView.findViewById(R.id.tw_nombre_10);
        TextView usuario10 = (TextView) rootView.findViewById(R.id.tw_nombre_usuario_10);
        TextView texto10 = (TextView) rootView.findViewById(R.id.tw_texto_10);

        texto.setTypeface(myTypeface);
        usuario.setTypeface(myTypefaceBold);
        nombre.setTypeface(myTypefaceBold);

        texto2.setTypeface(myTypeface);
        usuario2.setTypeface(myTypefaceBold);
        nombre2.setTypeface(myTypefaceBold);

        texto3.setTypeface(myTypeface);
        usuario3.setTypeface(myTypefaceBold);
        nombre3.setTypeface(myTypefaceBold);

        texto4.setTypeface(myTypeface);
        usuario4.setTypeface(myTypefaceBold);
        nombre4.setTypeface(myTypefaceBold);

        texto5.setTypeface(myTypeface);
        usuario5.setTypeface(myTypefaceBold);
        nombre5.setTypeface(myTypefaceBold);

        texto6.setTypeface(myTypeface);
        usuario6.setTypeface(myTypefaceBold);
        nombre6.setTypeface(myTypefaceBold);

        texto7.setTypeface(myTypeface);
        usuario7.setTypeface(myTypefaceBold);
        nombre7.setTypeface(myTypefaceBold);

        texto8.setTypeface(myTypeface);
        usuario8.setTypeface(myTypefaceBold);
        nombre8.setTypeface(myTypefaceBold);

        texto9.setTypeface(myTypeface);
        usuario9.setTypeface(myTypefaceBold);
        nombre9.setTypeface(myTypefaceBold);

        texto10.setTypeface(myTypeface);
        usuario10.setTypeface(myTypefaceBold);
        nombre10.setTypeface(myTypefaceBold);

        // Tw 1
        if(programaPreview.getTweets().size()>1 || programaPreview.getTweets().size()== 1){
            if(programaPreview.getTweets().get(0).getTw().length()<140){
                texto.setText(programaPreview.getTweets().get(0).getTw().replace("\n"," "));
            }
            else {

                texto.setText(programaPreview.getTweets().get(0).getTw().substring(0,140).replace("\n"," ")+"...");
            }
            nombre.setText(programaPreview.getTweets().get(0).getNombre()+"  @"+(programaPreview.getTweets().get(0)
                    .getNombreUsuario()));

            if(programaPreview.getTweets().get(0).getUrlImagen() != null){
                aq.id(R.id.foto_tw).image(programaPreview.getTweets().get(0).getUrlImagen());
            }
        }

        // Tw 2
        if(programaPreview.getTweets().size()>2 || programaPreview.getTweets().size()== 2){

            if(programaPreview.getTweets().get(1).getTw().length()<140){
                texto2.setText(programaPreview.getTweets().get(1).getTw().replace("\n", " "));
            }
            else {
                texto2.setText(programaPreview.getTweets().get(1).getTw().substring(0, 140).replace("\n", " ")+"...");
            }

            //usuario2.setText(programaPreview.getTweets().get(1).getNombreUsuario());

            nombre2.setText(programaPreview.getTweets().get(1).getNombre()+"  @"+(programaPreview.getTweets().get(1)
                    .getNombreUsuario()));
            if(programaPreview.getTweets().get(1).getUrlImagen() != null){
                aq.id(R.id.foto_tw_2).image(programaPreview.getTweets().get(1).getUrlImagen());
            }
        }
        // Tw 3
        if(programaPreview.getTweets().size()>3 || programaPreview.getTweets().size()== 3){

            if(programaPreview.getTweets().get(2).getTw().length()<140){

                texto3.setText(programaPreview.getTweets().get(2).getTw().replace("\n", " "));
            }
            else {
                texto3.setText(programaPreview.getTweets().get(2).getTw().substring(0, 140).replace("\n", " ")+"...");
            }
            //usuario3.setText(programaPreview.getTweets().get(2).getNombreUsuario());
            nombre3.setText(programaPreview.getTweets().get(2).getNombre()+"  @"+(programaPreview.getTweets().get(2)
                    .getNombreUsuario()));

            if(programaPreview.getTweets().get(2).getUrlImagen() != null){
                aq.id(R.id.foto_tw_3).image(programaPreview.getTweets().get(2).getUrlImagen());
            }
        }
        // Tw 4
        if(programaPreview.getTweets().size()>4 || programaPreview.getTweets().size()== 4){
            if(programaPreview.getTweets().get(3).getTw().length()<140){
                texto4.setText(programaPreview.getTweets().get(3).getTw().replace("\n", " "));
            }
            else {
                texto4.setText(programaPreview.getTweets().get(3).getTw().substring(0, 140).replace("\n", " ")+"...");
            }
            nombre4.setText(programaPreview.getTweets().get(3).getNombre()+"  @"+(programaPreview.getTweets().get(3)
                    .getNombreUsuario()));
            if(programaPreview.getTweets().get(3).getUrlImagen() != null){

                aq.id(R.id.foto_tw_4).image(programaPreview.getTweets().get(3).getUrlImagen());
            }
        }

        // Tw 5
        if(programaPreview.getTweets().size() >5 || programaPreview.getTweets().size()== 5){

            if(programaPreview.getTweets().get(4).getTw().length()<140){
                texto5.setText(programaPreview.getTweets().get(4).getTw().replace("\n", " "));
            }
            else {
                texto5.setText(programaPreview.getTweets().get(4).getTw().substring(0, 140).replace("\n", " ")+"...");
            }
            //usuario5.setText(programaPreview.getTweets().get(4).getNombreUsuario());

            nombre5.setText(programaPreview.getTweets().get(4).getNombre()+"  @"+(programaPreview.getTweets().get(4)
                    .getNombreUsuario()));

            if(programaPreview.getTweets().get(4).getUrlImagen() != null){
                aq.id(R.id.foto_tw_5).image(programaPreview.getTweets().get(4).getUrlImagen());
            }
        }

        // Tw 6
        if(programaPreview.getTweets().size()>6 || programaPreview.getTweets().size()== 6){

            if(programaPreview.getTweets().get(5).getTw().length()<140){

                texto6.setText(programaPreview.getTweets().get(5).getTw().replace("\n", " "));
            }
            else {
                texto6.setText(programaPreview.getTweets().get(5).getTw().substring(0, 140).replace("\n", " ")+"...");
            }

            nombre6.setText(programaPreview.getTweets().get(5).getNombre()+"  @"+(programaPreview.getTweets().get(5)
                    .getNombreUsuario()));

            if(programaPreview.getTweets().get(5).getUrlImagen() != null){
                aq.id(R.id.foto_tw_6).image(programaPreview.getTweets().get(5).getUrlImagen());
            }
        }
        // Tw 7
        if(programaPreview.getTweets().size()>7 || programaPreview.getTweets().size()== 7){

            if(programaPreview.getTweets().get(6).getTw().length()<140){
                texto7.setText(programaPreview.getTweets().get(6).getTw().replace("\n", " "));
            }
            else {
                texto7.setText(programaPreview.getTweets().get(6).getTw().substring(0, 140).replace("\n", " ")+"...");
            }

            //usuario7.setText(programaPreview.getTweets().get(6).getNombreUsuario());
            nombre7.setText(programaPreview.getTweets().get(6).getNombre()+"  @"+(programaPreview.getTweets().get(6)
                    .getNombreUsuario()));

            if(programaPreview.getTweets().get(6).getUrlImagen() != null){
                aq.id(R.id.foto_tw_7).image(programaPreview.getTweets().get(6).getUrlImagen());

            }
        }

        // Tw 8
        if(programaPreview.getTweets().size()>8 || programaPreview.getTweets().size()== 8){

            if(programaPreview.getTweets().get(7).getTw().length()<140){
                texto8.setText(programaPreview.getTweets().get(7).getTw().replace("\n", " "));
            }
            else {
                texto8.setText(programaPreview.getTweets().get(7).getTw().substring(0, 140).replace("\n", " ")+"...");
            }

            //usuario8.setText(programaPreview.getTweets().get(7).getNombreUsuario());

            nombre8.setText(programaPreview.getTweets().get(7).getNombre()+"  @"+(programaPreview.getTweets()
                    .get(7).getNombreUsuario()));
            if(programaPreview.getTweets().get(7).getUrlImagen() != null){
                aq.id(R.id.foto_tw_8).image(programaPreview.getTweets().get(7).getUrlImagen());
            }
        }

        // Tw 9
        if(programaPreview.getTweets().size()>9 || programaPreview.getTweets().size()== 9){

            if(programaPreview.getTweets().get(8).getTw().length()<140){
                texto9.setText(programaPreview.getTweets().get(8).getTw().replace("\n", " "));
            }
            else {
                texto9.setText(programaPreview.getTweets().get(8).getTw().substring(0, 140).replace("\n", " ")+"...");
            }
            usuario9.setText(programaPreview.getTweets().get(8).getNombreUsuario());
            nombre9.setText(programaPreview.getTweets().get(8).getNombre()+"  @"+(programaPreview.getTweets()
                    .get(8).getNombreUsuario()));

            if(programaPreview.getTweets().get(8).getUrlImagen() != null){
                aq.id(R.id.foto_tw_9).image(programaPreview.getTweets().get(8).getUrlImagen());
            }
        }
        // Tw 10
        if(programaPreview.getTweets().size() ==10){
            if(programaPreview.getTweets().get(9).getTw().length()<140){
                texto10.setText(programaPreview.getTweets().get(9).getTw().replace("\n", " "));
            }
            else {
                texto10.setText(programaPreview.getTweets().get(9).getTw().substring(0, 140).replace("\n", " ")+"...");
            }

            usuario10.setText(programaPreview.getTweets().get(9).getNombreUsuario());
            nombre10.setText(programaPreview.getTweets().get(9).getNombre()+"  @"+(programaPreview.getTweets().get(9).getNombreUsuario()));

            if(programaPreview.getTweets().get(9).getUrlImagen() != null){
                aq.id(R.id.foto_tw_10).image(programaPreview.getTweets().get(9).getUrlImagen());

            }
        }
    }

    public void noTws(){

        /*Resources res = getResources();
        Drawable d = res.getDrawable(R.drawable.no_tweets);
        ImageView imagen = new ImageView(getActivity());
        imagen.setPadding(5,8,0,0);
        imagen.setImageDrawable(d);*/

        scroll_tw.setClickable(false);
        scroll_tw.setEnabled(false);

        relative1 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw);
        relative2 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_2);
        relative3 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_3);
        relative4 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_4);
        relative5 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_5);
        relative6 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_6);
        relative7 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_7);
        relative8 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_8);
        relative9 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_9);
        relative10 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_10);

        relative1.removeAllViews();
        relative2.removeAllViews();
        relative3.removeAllViews();
        relative4.removeAllViews();
        relative5.removeAllViews();
        relative6.removeAllViews();
        relative7.removeAllViews();
        relative8.removeAllViews();
        relative9.removeAllViews();
        relative10.removeAllViews();
        //relative1.addView(imagen);

        RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.container_notws);
        r.setVisibility(View.VISIBLE);

    }

    public void recorreTws(){

        final int contador = programaPreview.getTweets().size();

        final int max = (contador-2) * 85;
        final int[] count = {0};

        TimerTask timerTask = new TimerTask()
        {
            public void run()
            {

                int posicion =  scroll_tw.getScrollY();

                //Log.e("posicion",""+posicion);

                if(posicion == max || posicion > max){

                    scroll_tw.smoothScrollTo(0,0);
                    count[0]++;

                }
                else{
                    if(count[0] % 2 == 0)
                        scroll_tw.smoothScrollBy(0,84 +1);
                    else
                        scroll_tw.smoothScrollBy(0,84 -1);
                    count[0]++;
                }
            }
        };

        // Aquí se pone en marcha el timer cada segundo.
        Timer timer = new Timer();
        // Dentro de 0 milisegundos avísame cada 8000 milisegundos
        timer.scheduleAtFixedRate(timerTask, 0, 8000);
    }
}
