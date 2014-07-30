package com.smartboxtv.movistartv.programation.preview;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.database.DataBaseTrivia;
import com.smartboxtv.movistartv.data.database.DataGameTrivia;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.Trivia;
import com.smartboxtv.movistartv.data.models.TriviaQuestion;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.data.trivia.GameTrivia;
import com.smartboxtv.movistartv.services.DataLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 07-05-14.
 */
public class TriviaGameFragment extends Fragment {

    private Trivia trivia;
    private Trivia returnTrivia;
    private Program program;
    private RelativeLayout contenedorTrivia;
    private RelativeLayout contenedorTriviaImagenUnica;
    private RelativeLayout contenedorTriviaImagenes;
    private RelativeLayout contenedorTriviaTexto;
    private RelativeLayout contenedorResultado;

    private TextView respuesta1;
    private TextView respuesta2;
    private TextView respuesta3;
    private TextView respuesta4;
    private TextView segundos;
    private TextView pregunta;
    private TextView numero;


    private ImageView imagen;

    private List<TriviaQuestion> tipo1;
    private List<TriviaQuestion> tipo2;
    private List<TriviaQuestion> tipo3;

    private final String URL = "http://190.215.44.18/";
    private Typeface normal, bold;

    private final int  TIEMPO = 100;
    private int sg = 10;
    private int TRIVIA_ACTUAL;
    private int NIVEL_ACTUAL;
    private int VIDAS;
    private int PUNTAJE_TOTAL;
    private int[] indiceFacil;
    private int[] indiceMedio;
    private int[] indiceDificil;
    private ProgressBar progressBar;
    private final TareaAsincrona tarea = new TareaAsincrona();

    private boolean  hayRespuesta = false;
    private AQuery aq;
    private Resources res;
    private DataGameTrivia dataGameTrivia;
    private DataBaseTrivia dataBaseTrivia;
    private boolean nextLevel = true;
    private boolean gameOver = false;

    public TriviaGameFragment() {
    }

    public TriviaGameFragment(Trivia returntrivia, Trivia trivia, Program program) {

        this.trivia = trivia;
        this.returnTrivia = returntrivia;
        this.program = program;

        cleanTrivia();
        separateType();
    }
    public void resetTrivia(){

        GameTrivia.getJUEGO_ACTUAL(getActivity(), ""+program.getTitle());
        GameTrivia.setNIVEL_DESBLOQUEO_MEDIO(getActivity(), 0, program.getTitle());
        GameTrivia.setNIVEL_DESBLOQUEO_DIFICIL(getActivity(), 0, program.getTitle());
        GameTrivia.setPUNTAJE_MAX_NIVEL_FACIL(getActivity(), 0, program.getTitle());
        GameTrivia.setPUNTAJE_MAX_NIVEL_MEDIO(getActivity(), 0, program.getTitle());
        GameTrivia.setPUNTAJE_MAX_NIVEL_DIFICIL(getActivity(), 0, program.getTitle());
        GameTrivia.setVIDA_TRIVIA(getActivity(),3 , program.getTitle());

        TriviaMaxFragment fragmentoTrivia = new TriviaMaxFragment(program,trivia, true);
        FragmentTransaction ft =getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.contenedor_trivia,fragmentoTrivia);
        ft.commit();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.preview_fg_trivia_game, container, false);

        nextLevel = true;
        TRIVIA_ACTUAL = 0;
        ImageView corazon1 = (ImageView) rootView.findViewById(R.id.trivia_vida_1);
        ImageView corazon2 = (ImageView) rootView.findViewById(R.id.trivia_vida_2);
        ImageView corazon3 = (ImageView) rootView.findViewById(R.id.trivia_vida_3);

        Drawable d = getResources().getDrawable(R.drawable.vida_trivia);

        Log.e("Trivia Actual",trivia.getPreguntas().get(TRIVIA_ACTUAL).getText());
        Log.e("Trivia Actual tamaño",""+trivia.getPreguntas().size());
        Log.e("Nivel Actual",""+ NIVEL_ACTUAL);
        Log.e("Trivia Nivel ", "" + trivia.getPreguntas().get(TRIVIA_ACTUAL).getLevel());
        Log.e("VIDAS ", "" + GameTrivia.getVIDA_TRIVIA(getActivity(),program.getTitle()));

        dataBaseTrivia = new DataBaseTrivia(getActivity(),"",null,0);

        dataGameTrivia = dataBaseTrivia.selectGame(program.Title);

        if(dataGameTrivia == null){
            dataBaseTrivia.insertGameTrivia(program.Title);
            dataGameTrivia = dataBaseTrivia.selectGame(program.Title);
        }

        NIVEL_ACTUAL = dataGameTrivia.nivel;
        VIDAS = dataGameTrivia.vidas;

        switch (VIDAS){
            case 1: corazon3.setImageDrawable(d);
                corazon2.setImageDrawable(d);
                break;
            case 2: corazon3.setImageDrawable(d);
                break;
        }
        //VIDAS = GameTrivia.getVIDA_TRIVIA(getActivity(), "" + trivia.getPreguntas().get(0).getId());

        bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");  res = getResources();
        Animation rotacion = AnimationUtils.loadAnimation(getActivity(), R.anim.rota_moneda);

        contenedorTriviaImagenUnica =(RelativeLayout) rootView.findViewById(R.id.trivia_con_imagen);
        contenedorTriviaImagenes = (RelativeLayout) rootView.findViewById(R.id.trivia_imagenes);
        contenedorTriviaTexto = (RelativeLayout) rootView.findViewById(R.id.trivia_solo_texto);
        contenedorResultado = (RelativeLayout) rootView.findViewById(R.id.trivia_resultado);
        RelativeLayout contenedorHeader = (RelativeLayout) rootView.findViewById(R.id.contenedor_trivia_juego_tiempo);
        RelativeLayout contenedorPie = (RelativeLayout) rootView.findViewById(R.id.contenedor_trivia_juego_pie);
        contenedorTrivia = (RelativeLayout) rootView.findViewById(R.id.contenedor_trivia_juego);

        Button pause = (Button) rootView.findViewById(R.id.trivia_boton_pause);
        progressBar = (ProgressBar) rootView.findViewById(R.id.trivia_progress_bar);

        TextView txtNivel  = (TextView) rootView.findViewById(R.id.texto_dificultad_);

        switch(NIVEL_ACTUAL){
            case 1: txtNivel.setText("Facil");  break;
            case 2: txtNivel.setText("Medio");  break;
            case 3: txtNivel.setText("Dificil");  break;
        }

        aq = new AQuery(rootView);


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tarea.onCancelled();
                if(tarea.isCancelled())
                    resetTrivia();
                getFragmentManager().popBackStack();

            }
        });
        return rootView;
    }

    public void setDataTipo1(final TriviaQuestion q){

        if(contenedorTriviaImagenUnica.getVisibility() == View.VISIBLE)
            contenedorTriviaImagenUnica.setVisibility(View.GONE);

        if(contenedorTriviaImagenes.getVisibility() == View.VISIBLE)
            contenedorTriviaImagenes.setVisibility(View.GONE);

        if(contenedorTriviaTexto.getVisibility() == View.GONE)
            contenedorTriviaTexto.setVisibility(View.VISIBLE);

        segundos = (TextView) contenedorTriviaTexto.findViewById(R.id.trivia_texto_timer);
        pregunta = (TextView) contenedorTriviaTexto.findViewById(R.id.trivia_pregunta_juego);
        respuesta1 = (TextView) contenedorTriviaTexto.findViewById(R.id.respuesta1);
        respuesta2 = (TextView) contenedorTriviaTexto.findViewById(R.id.respuesta2);
        respuesta3 = (TextView) contenedorTriviaTexto.findViewById(R.id.respuesta3);
        respuesta4 = (TextView) contenedorTriviaTexto.findViewById(R.id.respuesta4);
        numero = (TextView) contenedorTriviaTexto.findViewById(R.id.trivia_numero_pregunta_juego);

        LinearLayout respuestaTexto1 = (LinearLayout) contenedorTriviaTexto.findViewById(R.id.trivia_pregunta_1);
        LinearLayout respuestaTexto2 = (LinearLayout) contenedorTriviaTexto.findViewById(R.id.trivia_pregunta_2);
        LinearLayout respuestaTexto3 = (LinearLayout) contenedorTriviaTexto.findViewById(R.id.trivia_pregunta_3);
        LinearLayout respuestaTexto4 = (LinearLayout) contenedorTriviaTexto.findViewById(R.id.trivia_pregunta_4);

        if(!tipo1.get(TRIVIA_ACTUAL).getRespuestas().isEmpty()){

            pregunta.setText(q.getText());
            numero.setText("Pregunta "+(TRIVIA_ACTUAL+1));
            respuesta1.setText(q.getRespuestas().get(0).getRespuesta());
            respuesta2.setText(q.getRespuestas().get(1).getRespuesta());
            respuesta3.setText(q.getRespuestas().get(2).getRespuesta());
            respuesta4.setText(q.getRespuestas().get(3).getRespuesta());

        }
        // Capturas de eventos en trivia ded tipo 1
        respuestaTexto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(0).isValor());
            }
        });

        respuestaTexto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(1).isValor());
            }
        });

        respuestaTexto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(2).isValor());
            }
        });

        respuestaTexto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(3).isValor());
            }
        });

    }
    public void setDataTipo2(final TriviaQuestion q){

        if(contenedorTriviaImagenUnica.getVisibility() == View.VISIBLE)
            contenedorTriviaImagenUnica.setVisibility(View.GONE);

        if(contenedorTriviaImagenes.getVisibility() == View.GONE)
            contenedorTriviaImagenes.setVisibility(View.VISIBLE);

        if(contenedorTriviaTexto.getVisibility() == View.VISIBLE)
            contenedorTriviaTexto.setVisibility(View.GONE);

        ImageView imagen1 = (ImageView) contenedorTriviaImagenes.findViewById(R.id.trivia_imagen1);
        ImageView imagen2 = (ImageView) contenedorTriviaImagenes.findViewById(R.id.trivia_imagen2);
        ImageView imagen3 = (ImageView) contenedorTriviaImagenes.findViewById(R.id.trivia_imagen3);
        ImageView imagen4 = (ImageView) contenedorTriviaImagenes.findViewById(R.id.trivia_imagen4);

        segundos = (TextView) contenedorTriviaImagenes.findViewById(R.id.trivia_texto_timer);
        pregunta = (TextView) contenedorTriviaImagenes.findViewById(R.id.trivia_pregunta_juego);
        numero = (TextView) contenedorTriviaImagenes.findViewById(R.id.trivia_numero_pregunta_juego);

        if(!tipo2.get(TRIVIA_ACTUAL).getRespuestas().isEmpty()){

            aq.id(imagen1).image(URL+q.getRespuestas().get(0).getRespuesta());
            aq.id(imagen2).image(URL+q.getRespuestas().get(1).getRespuesta());
            aq.id(imagen3).image(URL+q.getRespuestas().get(2).getRespuesta());
            aq.id(imagen4).image(URL+q.getRespuestas().get(3).getRespuesta());

            pregunta.setText(q.getText());
            numero.setText("Pregunta "+(TRIVIA_ACTUAL+1));

        }
        // Capturas de eventos en trivia ded tipo 2
        imagen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(0).isValor());
            }
        });

        imagen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(1).isValor());
            }
        });

        imagen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(2).isValor());
            }
        });

        imagen4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(3).isValor());
            }
        });

    }
    public void setDataTipo3(final TriviaQuestion q){

        if(contenedorTriviaImagenUnica.getVisibility() == View.GONE)
            contenedorTriviaImagenUnica.setVisibility(View.VISIBLE);

        if(contenedorTriviaImagenes.getVisibility() == View.VISIBLE)
            contenedorTriviaImagenes.setVisibility(View.GONE);

        if(contenedorTriviaTexto.getVisibility() == View.VISIBLE)
            contenedorTriviaTexto.setVisibility(View.GONE);

        segundos = (TextView) contenedorTriviaImagenUnica.findViewById(R.id.trivia_texto_timer);
        pregunta = (TextView) contenedorTriviaImagenUnica.findViewById(R.id.trivia_pregunta_juego);
        respuesta1 = (TextView) contenedorTriviaImagenUnica.findViewById(R.id.respuesta1);
        respuesta2 = (TextView) contenedorTriviaImagenUnica.findViewById(R.id.respuesta2);
        respuesta3 = (TextView) contenedorTriviaImagenUnica.findViewById(R.id.respuesta3);
        respuesta4 = (TextView) contenedorTriviaImagenUnica.findViewById(R.id.respuesta4);

        LinearLayout respuestaImagenUnica1 = (LinearLayout) contenedorTriviaImagenUnica.findViewById(R.id.trivia_pregunta_1);
        LinearLayout respuestaImagenUnica2 = (LinearLayout) contenedorTriviaImagenUnica.findViewById(R.id.trivia_pregunta_2);
        LinearLayout respuestaImagenUnica3 = (LinearLayout) contenedorTriviaImagenUnica.findViewById(R.id.trivia_pregunta_3);
        LinearLayout respuestaImagenUnica4 = (LinearLayout) contenedorTriviaImagenUnica.findViewById(R.id.trivia_pregunta_4);

        imagen = (ImageView) contenedorTriviaImagenUnica.findViewById(R.id.trivia_imagen_juego);
        numero = (TextView) contenedorTriviaImagenUnica.findViewById(R.id.trivia_numero_pregunta_juego);

        if(!tipo3.get(TRIVIA_ACTUAL).getRespuestas().isEmpty()){

            pregunta.setText(q.getText());
            numero.setText("Pregunta "+(TRIVIA_ACTUAL+1));
            respuesta1.setText(q.getRespuestas().get(0).getRespuesta());
            respuesta2.setText(q.getRespuestas().get(1).getRespuesta());
            respuesta3.setText(q.getRespuestas().get(2).getRespuesta());
            respuesta4.setText(q.getRespuestas().get(3).getRespuesta());

            aq.id(imagen).image(URL + q.getTextAlt());

        }

        // Capturas de eventos en trivia ded tipo 3
        respuestaImagenUnica1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(0).isValor());
            }
        });

        respuestaImagenUnica2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(1).isValor());
            }
        });

        respuestaImagenUnica3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(2).isValor());
            }
        });

        respuestaImagenUnica4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hayRespuesta = true;
                esCorrecta(q.getRespuestas().get(3).isValor());
            }
        });
    }

    public void separateType(){

        tipo1 = new ArrayList<TriviaQuestion>();
        tipo2 = new ArrayList<TriviaQuestion>();
        tipo3 = new ArrayList<TriviaQuestion>();

        for(int i = 0; i < trivia.getPreguntas().size();i++){
            switch (trivia.getPreguntas().get(i).getType()){

                case 1:     tipo1.add(trivia.getPreguntas().get(i));
                            break;
                case 2:     tipo2.add(trivia.getPreguntas().get(i));
                            break;
                case 3:     tipo3.add(trivia.getPreguntas().get(i));
                            break;
            }
        }
    }
    public void separateNivel(){
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void esCorrecta(boolean respuesta){

        imagen = (ImageView) contenedorResultado.findViewById(R.id.trivia_imagen_resultado);
        TextView textoResultado = (TextView) contenedorResultado.findViewById(R.id.trivia_texto_resultado);
        TextView textoPuntaje = (TextView) contenedorResultado.findViewById(R.id.trivia_puntaje);
        TextView subTextoResultado = (TextView) contenedorResultado.findViewById(R.id.trivia_sub_texto_resultado);
        Button siguiente = (Button) contenedorResultado.findViewById(R.id.trivia_siguiente);
        contenedorTrivia.setVisibility(View.GONE);

        if(contenedorResultado.getVisibility() == View.GONE)
            contenedorResultado.setVisibility(View.VISIBLE);

        textoResultado.setTypeface(normal);
        subTextoResultado.setTypeface(normal);
        textoPuntaje.setTypeface(bold);

        int puntaje = ((sg + 1) * 400) / 10;
        Drawable d1,d2;

        if(respuesta){

            textoResultado.setText("Respuesta Correcta");
            subTextoResultado.setText("Has ganado " + puntaje + " puntos");
            textoPuntaje.setText(puntaje + " puntos");
            textoPuntaje.setVisibility(View.VISIBLE);

            d1 = res.getDrawable(R.drawable.icon_coins);
            imagen.setBackground(d1);

            RelativeLayout r = (RelativeLayout) contenedorResultado.findViewById(R.id.trivia_contenedor_moneda);
            AnimationCustom animacion = new AnimationCustom(getActivity());

            animacion.rotacion(r);
            //PUNTAJE_TOTAL = GameTrivia.getPUNTAJE(getActivity(),program.getTitle());
            PUNTAJE_TOTAL = dataGameTrivia.puntaje;
            Log.e("Puntaje obtenido",""+puntaje);
            PUNTAJE_TOTAL = PUNTAJE_TOTAL + puntaje;
            Log.e("Puntaje total",""+PUNTAJE_TOTAL);
            //GameTrivia.setPUNTAJE(getActivity(), PUNTAJE_TOTAL, program.getTitle());
            //GameTrivia.setVIDA_TRIVIA(getActivity(),VIDAS,program.getTitle());

            dataGameTrivia.puntaje = puntaje;
            dataGameTrivia.vidas = VIDAS;

        }
        else{

            textoResultado.setText("Respuesta Incorrecta");
            textoPuntaje.setVisibility(View.GONE);
            nextLevel = false;
            d2 = res.getDrawable(R.drawable.icon_no_coins);
            imagen.setBackground(d2);
            VIDAS--;
            dataGameTrivia.next_level = false;
            dataGameTrivia.vidas = VIDAS;

            if(VIDAS == 0){
                Toast.makeText(getActivity(),"Has perdido todas la vidas ",Toast.LENGTH_LONG).show();
                dataGameTrivia.game_over = true;
            }
        }

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tarea.cancel(true);
                if (tarea.isCancelled()) {

                    switch (NIVEL_ACTUAL){
                        case 1 :    if(dataGameTrivia.game_over){
                                        dataGameTrivia.vidas = 3;
                                        dataGameTrivia.puntaje = 0;
                                        dataBaseTrivia.updateGame(program.Title,dataGameTrivia);
                                        TriviaMaxFragment fragmentoTrivia = new TriviaMaxFragment(program,returnTrivia,true);
                                        FragmentTransaction ft =getFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        ft.replace(R.id.contenedor_trivia,fragmentoTrivia);
                                        ft.commit();
                                        break;
                                      }

                                    if(trivia.getPreguntas().size() > 1){
                                        trivia.getPreguntas().remove(TRIVIA_ACTUAL);
                                        dataBaseTrivia.updateGame(program.Title,dataGameTrivia);
                                        TriviaGameFragment fragmentoTriviaJuego = new TriviaGameFragment(returnTrivia,trivia,program);
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        ft.replace(R.id.contenedor_trivia,fragmentoTriviaJuego);
                                        ft.commit();

                                    }
                                    else if(trivia.getPreguntas().size() == 1){                                                             // Paso de nivel :O

                                        if(VIDAS > 0){

                                            dataGameTrivia.puntaje = 0;

                                            int puntaje = dataGameTrivia.puntaje_max_1;

                                            if(puntaje < PUNTAJE_TOTAL){
                                                dataGameTrivia.puntaje_max_1 = puntaje;
                                                DataLoader dataLoader = new DataLoader(getActivity());
                                                dataLoader.updateScoreTrvia(""+PUNTAJE_TOTAL,"1", UserPreference.getIdNunchee(getActivity()),program.Title);
                                            }
                                            if( dataGameTrivia.next_level){
                                                if(!dataGameTrivia.bloqueo_nivel_2){
                                                    dataGameTrivia.bloqueo_nivel_2 = true;
                                                    dataGameTrivia.nivel = 2;
                                                    Toast.makeText(getActivity(),"Nivel 2 desbloqueado",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else{
                                                Toast.makeText(getActivity(),"Intenta de nuevo",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        trivia.getPreguntas().remove(TRIVIA_ACTUAL);
                                        dataGameTrivia.next_level = true;
                                        dataGameTrivia.game_over = false;
                                        dataBaseTrivia.updateGame(program.Title,dataGameTrivia);
                                        TriviaMaxFragment fragmentoTrivia = new TriviaMaxFragment(program,returnTrivia,true);
                                        FragmentTransaction ft =getFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        ft.replace(R.id.contenedor_trivia,fragmentoTrivia);
                                        ft.commit();
                                    }
                                    break ;

                        case 2 :    if(dataGameTrivia.game_over){
                                        dataGameTrivia.vidas = 3;
                                        dataGameTrivia.puntaje = 0;
                                        dataGameTrivia.next_level = true;
                                        dataGameTrivia.game_over = false;
                                        dataBaseTrivia.updateGame(program.Title,dataGameTrivia);
                                        TriviaMaxFragment fragmentoTrivia = new TriviaMaxFragment(program,returnTrivia,true);
                                        FragmentTransaction ft =getFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        ft.replace(R.id.contenedor_trivia,fragmentoTrivia);
                                        ft.commit();
                                        break;
                                    }

                                    if(trivia.getPreguntas().size() > 1){
                                        trivia.getPreguntas().remove(TRIVIA_ACTUAL);
                                        dataBaseTrivia.updateGame(program.Title,dataGameTrivia);
                                        TriviaGameFragment fragmentoTriviaJuego = new TriviaGameFragment(returnTrivia,trivia,program);
                                        FragmentTransaction ft =getFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        ft.replace(R.id.contenedor_trivia,fragmentoTriviaJuego);
                                        ft.commit();

                                        Log.e("Nivel 2","nivel 2");
                                    }
                                    else if(trivia.getPreguntas().size() == 1){                                                             // Paso de nivel :O

                                        if(VIDAS > 0){
                                            dataGameTrivia.puntaje = 0;

                                            int puntaje = dataGameTrivia.puntaje_max_2;

                                            if(puntaje < PUNTAJE_TOTAL){
                                                dataGameTrivia.puntaje_max_2 = puntaje;
                                                DataLoader dataLoader = new DataLoader(getActivity());
                                                dataLoader.updateScoreTrvia(""+PUNTAJE_TOTAL,"2", UserPreference.getIdNunchee(getActivity()),program.Title);
                                            }
                                            if(dataGameTrivia.next_level){
                                                if(!dataGameTrivia.bloqueo_nivel_3){
                                                    dataGameTrivia.bloqueo_nivel_3 = true;
                                                    dataGameTrivia.nivel = 3;
                                                    Toast.makeText(getActivity(),"Nivel 3 desbloqueado",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else{
                                                Toast.makeText(getActivity(),"Intenta de nuevo",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        trivia.getPreguntas().remove(TRIVIA_ACTUAL);
                                        dataGameTrivia.next_level = true;
                                        dataGameTrivia.game_over = false;
                                        dataBaseTrivia.updateGame(program.Title,dataGameTrivia);
                                        TriviaMaxFragment fragmentoTrivia = new TriviaMaxFragment(program,returnTrivia,true);
                                        FragmentTransaction ft =getFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        ft.replace(R.id.contenedor_trivia,fragmentoTrivia);
                                        ft.commit();
                                    }
                                    break ;
                        case 3 :    if(dataGameTrivia.game_over){
                                        dataGameTrivia.vidas = 3;
                                        dataGameTrivia.puntaje = 0;
                                        dataGameTrivia.next_level = true;
                                        dataGameTrivia.game_over = false;
                                        dataBaseTrivia.updateGame(program.Title,dataGameTrivia);
                                        TriviaMaxFragment fragmentoTrivia = new TriviaMaxFragment(program,returnTrivia,true);
                                        FragmentTransaction ft =getFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        ft.replace(R.id.contenedor_trivia,fragmentoTrivia);
                                        ft.commit();
                                        break;
                                    }

                                    if(trivia.getPreguntas().size() > 1){
                                        trivia.getPreguntas().remove(TRIVIA_ACTUAL);
                                        dataBaseTrivia.updateGame(program.Title,dataGameTrivia);
                                        TriviaGameFragment fragmentoTriviaJuego = new TriviaGameFragment(returnTrivia,trivia,program);
                                        FragmentTransaction ft =getFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        ft.replace(R.id.contenedor_trivia,fragmentoTriviaJuego);
                                        ft.commit();
                                    }
                                    else if(trivia.getPreguntas().size() == 1){                                                             // Paso de nivel :O
                                        if(VIDAS > 0){
                                            dataGameTrivia.puntaje = 0;

                                            int puntaje = dataGameTrivia.puntaje_max_2;

                                            if(puntaje < PUNTAJE_TOTAL){
                                                dataGameTrivia.puntaje_max_3 = puntaje;
                                                DataLoader dataLoader = new DataLoader(getActivity());
                                                dataLoader.updateScoreTrvia(""+PUNTAJE_TOTAL,"3", UserPreference.getIdNunchee(getActivity()),program.Title);
                                            }
                                            if(dataGameTrivia.next_level){

                                                Toast.makeText(getActivity(),"Nivel 3 desbloqueado",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getActivity(),"Intenta de nuevo",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        trivia.getPreguntas().remove(TRIVIA_ACTUAL);
                                        dataGameTrivia.next_level = true;
                                        dataGameTrivia.game_over = false;
                                        dataBaseTrivia.updateGame(program.Title,dataGameTrivia);
                                        TriviaMaxFragment fragmentoTrivia = new TriviaMaxFragment(program,returnTrivia,true);
                                        FragmentTransaction ft =getFragmentManager().beginTransaction();
                                        ft.addToBackStack(null);
                                        ft.replace(R.id.contenedor_trivia,fragmentoTrivia);
                                        ft.commit();
                                    }
                                    break ;
                    }

                }
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        tarea.execute(TIEMPO);
        super.onResume();
    }

    public void cleanTrivia(){
        switch(NIVEL_ACTUAL){
            case 1: break ;
            case 2: for(int i = 0; i < trivia.getPreguntas().size();i++){
                        if(trivia.getPreguntas().get(i).getLevel() == 1 ){
                            trivia.getPreguntas().remove(i);
                        }
                    }
                    break ;
            case 3:for(int i = 0; i < trivia.getPreguntas().size();i++){
                        if(trivia.getPreguntas().get(i).getLevel() == 1 || trivia.getPreguntas().get(i).getLevel() == 2){
                            trivia.getPreguntas().remove(i);
                        }
                   } 
                   break ;
        }
    }

    public class TareaAsincrona extends AsyncTask<Integer,Integer,Integer> {

        @Override
        protected void onPreExecute() {

            progressBar.setProgress(0);

                switch (trivia.getPreguntas().get(TRIVIA_ACTUAL).getType()){

                    case 1: if(trivia.getPreguntas().size() >= TRIVIA_ACTUAL)
                                setDataTipo1(trivia.getPreguntas().get(TRIVIA_ACTUAL));
                            break;

                    case 2: if(trivia.getPreguntas().size() >= TRIVIA_ACTUAL)
                                setDataTipo2(trivia.getPreguntas().get(TRIVIA_ACTUAL));
                            break;

                    case 3: if(trivia.getPreguntas().size() >= TRIVIA_ACTUAL)
                                setDataTipo3(trivia.getPreguntas().get(TRIVIA_ACTUAL));
                            break;
                }

        }

        @Override
        protected Integer doInBackground(Integer... n){

            Long delta;
            Long fin;
            Long inicio = System.currentTimeMillis();
            int  i = 0;

            while(i < n[0]+1 && !hayRespuesta){

                fin = System.currentTimeMillis();
                delta = fin - inicio;

                if(delta > 100){
                    inicio = fin;
                    publishProgress(i);
                    i++;
                }
            }
            if(hayRespuesta){
                onCancelled();
            }
            return 1;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setProgress(values[0]);

            if(values[0]%10==0){
                sg--;
                /*if(sg != 1 && sg != 0){
                    //segundos.setText(sg+" Segundos");
                }*/
                }
                else{
                    //segundos.setText(sg+" Segundo");
                }
        }


        @Override
        protected void onPostExecute(Integer integer) {

            if(!hayRespuesta){
                try{
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            esCorrecta(false);
                        }
                    });
                } catch (NullPointerException ex){
                    ex.printStackTrace();
                } catch(Exception e ){
                    e.printStackTrace();
                }
            }


        }

        @Override
        protected void onCancelled() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //esCorrecta(false);
                }
            });
            super.onCancelled();
        }
    }
}
