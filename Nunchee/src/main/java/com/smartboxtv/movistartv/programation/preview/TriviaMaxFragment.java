package com.smartboxtv.movistartv.programation.preview;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.database.DataBaseTrivia;
import com.smartboxtv.movistartv.data.database.DataGameTrivia;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.Trivia;
import com.smartboxtv.movistartv.data.models.TriviaQuestion;
import com.smartboxtv.movistartv.services.DataLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 06-05-14.
 */
public class TriviaMaxFragment extends Fragment {

    private RelativeLayout contenedorVacio;

    private ImageView fondoFacil;
    private ImageView fondoMedio;
    private ImageView fondoDificil;

    private List<TriviaQuestion> facil = new ArrayList<TriviaQuestion>();
    private List<TriviaQuestion> medio = new ArrayList<TriviaQuestion>();
    private List<TriviaQuestion> dificil = new ArrayList<TriviaQuestion>();

    private View rootView;
    private final Program programa;
    private Trivia trivia;

    private int levelMedio;
    private int levelDificil;
    private int vidas;
    private int puntaje;
    //private int nivel;
    private int NIVEL_ACTUAL;
    private boolean load;
    private boolean bloqueoMedio;
    private boolean bloqueoDificil;

    private DataGameTrivia dataGameTrivia;
    private DataBaseTrivia dataBaseTrivia;

    public TriviaMaxFragment(Program program, Trivia trivia, boolean flag) {
        this.trivia = trivia;
        this.programa = program;
        load = flag;
        separateNivel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.preview_fg_trivia_max, container, false);

        RelativeLayout contenedorFacil = (RelativeLayout) rootView.findViewById(R.id.trivia_nivel_facil);
        RelativeLayout contenedorMedio = (RelativeLayout) rootView.findViewById(R.id.trivia_nivel_medio);
        RelativeLayout contenedorDificil = (RelativeLayout) rootView.findViewById(R.id.trivia_nivel_dificil);

        contenedorVacio = (RelativeLayout) rootView.findViewById(R.id.contenedor_trivia_vacio);

        dataBaseTrivia = new DataBaseTrivia(getActivity(),"",null,0);

        dataGameTrivia = dataBaseTrivia.selectGame(programa.Title);

        if(dataGameTrivia == null){
            dataBaseTrivia.insertGameTrivia(programa.Title);
            dataGameTrivia = dataBaseTrivia.selectGame(programa.Title);
        }
        dataGameTrivia.game_over = false;
        dataGameTrivia.next_level = true;
        dataBaseTrivia.updateGame(programa.Title, dataGameTrivia);
        // Datos Juegos
        NIVEL_ACTUAL = dataGameTrivia.nivel;
        Log.e("NIVEL ACTUAL ","---> "+NIVEL_ACTUAL);

        bloqueoMedio = dataGameTrivia.bloqueo_nivel_2;
        bloqueoDificil = dataGameTrivia.bloqueo_nivel_3;
        vidas = dataGameTrivia.vidas;

        /*Log.e("nivel",""+dataGameTrivia.nivel);
        Log.e("puntaje",""+dataGameTrivia.puntaje);
        Log.e("vidas",""+dataGameTrivia.vidas);

        Log.e("bloqueo 1",""+dataGameTrivia.bloqueo_nivel_1);
        Log.e("bloqueo 2",""+dataGameTrivia.bloqueo_nivel_2);
        Log.e("bloqueo 3",""+dataGameTrivia.bloqueo_nivel_3);

        Log.e("puntaje 1",""+dataGameTrivia.puntaje_max_1);
        Log.e("puntaje 2",""+dataGameTrivia.puntaje_max_2);
        Log.e("puntaje 3",""+dataGameTrivia.puntaje_max_3);*/

        Button botonFacil = (Button) contenedorFacil.findViewById(R.id.trivia_boton_facil);
        Button botonMedio = (Button) contenedorMedio.findViewById(R.id.trivia_boton_medio);
        Button botonDificil = (Button) contenedorDificil.findViewById(R.id.trivia_boton_dificil);

        fondoFacil = (ImageView) contenedorFacil.findViewById(R.id.trivia_imagen);
        fondoMedio = (ImageView) contenedorMedio.findViewById(R.id.trivia_imagen);
        fondoDificil = (ImageView) contenedorDificil.findViewById(R.id.trivia_imagen);

        RelativeLayout blockPause1 = (RelativeLayout) contenedorFacil.findViewById(R.id.block_pause);
        RelativeLayout blockPause2 = (RelativeLayout) contenedorMedio.findViewById(R.id.block_pause);
        RelativeLayout blockPause3 = (RelativeLayout) contenedorDificil.findViewById(R.id.block_pause);

        TextView  text1 = (TextView) contenedorFacil.findViewById(R.id.score_oficial);
        TextView  text2 = (TextView) contenedorMedio.findViewById(R.id.score_oficial);
        TextView  text3 = (TextView) contenedorDificil.findViewById(R.id.score_oficial);

        ImageView lockMedio = (ImageView) contenedorMedio.findViewById(R.id.trivia_block_imagen);
        ImageView lockDificil = (ImageView) contenedorDificil.findViewById(R.id.trivia_block_imagen);

        if(dataGameTrivia.nivel_1_activo){
            blockPause1.setVisibility(View.VISIBLE);
            blockPause2.setVisibility(View.GONE);
            blockPause3.setVisibility(View.GONE);
            text1.setText("0");
            //text1.setText(dataGameTrivia.puntaje);
            botonFacil.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_continuar));
        }
        if(dataGameTrivia.nivel_2_activo){
            blockPause1.setVisibility(View.GONE);
            blockPause2.setVisibility(View.VISIBLE);
            blockPause3.setVisibility(View.GONE);
            text2.setText(dataGameTrivia.puntaje);
            botonMedio.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_continuar));
        }
        if(dataGameTrivia.nivel_3_activo){
            blockPause1.setVisibility(View.GONE);
            blockPause2.setVisibility(View.GONE);
            blockPause3.setVisibility(View.VISIBLE);
            text3.setText(dataGameTrivia.puntaje);
            botonDificil.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_continuar));
        }
        else if(!dataGameTrivia.nivel_3_activo && !dataGameTrivia.nivel_3_activo && !dataGameTrivia.nivel_3_activo){
            dataGameTrivia.puntaje = 0;
        }
        //if(levelMedio == 1){
          if(bloqueoMedio){

            lockMedio.setVisibility(View.GONE);
            botonMedio.setEnabled(true);
        }
        else{
            botonMedio.setEnabled(false);
        }

        //if(levelDificil == 1){
        if(bloqueoDificil){

            lockDificil.setVisibility(View.GONE);
            botonDificil.setEnabled(true);
        }
        else{
            botonDificil.setEnabled(false);
        }

        if(!trivia.getPreguntas().isEmpty())
            setData();
        else{
            if(load){
                obtieneTrivia();
            }

            else
                setDataVacio();
        }


        botonFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GameTrivia.setNIVEL_TRIVIA(getActivity(),1,programa.getTitle());
                dataGameTrivia.nivel = 1;
                dataGameTrivia.nivel_1_activo = true;
                dataGameTrivia.nivel_2_activo = false;
                dataGameTrivia.nivel_3_activo = false;
                dataBaseTrivia.updateGame(programa.Title,dataGameTrivia);
                setDataNivelFacil();

            }
        });

        botonMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if (levelMedio == 1){
                  if(bloqueoMedio){
                    if(medio.size() > 0 && medio != null){
                        dataGameTrivia.nivel = 2;
                        dataGameTrivia.nivel_1_activo = false;
                        dataGameTrivia.nivel_2_activo = true;
                        dataGameTrivia.nivel_3_activo = false;
                        dataBaseTrivia.updateGame(programa.Title,dataGameTrivia);
                        setDataNivelMedio();
                    }
                    else{
                        setDataVacio();
                    }
                }

            }
        });
        botonDificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if (levelDificil == 1){
                if(bloqueoDificil){
                    if(dificil.size() > 0 && dificil != null){
                        dataGameTrivia.nivel = 1;
                        dataGameTrivia.nivel_1_activo = false;
                        dataGameTrivia.nivel_2_activo = false;
                        dataGameTrivia.nivel_3_activo = true;
                        dataBaseTrivia.updateGame(programa.Title,dataGameTrivia);
                        setDataNivelDificil();
                    }
                    else{
                        setDataVacio();
                    }
                }

            }
        });

        return rootView;
    }

    public void setData(){

        ImageView corazonFacil1 = (ImageView) rootView.findViewById(R.id.trivia_vida_facil_1);
        ImageView corazonFacil2 = (ImageView) rootView.findViewById(R.id.trivia_vida_facil_2);
        ImageView corazonFacil3 = (ImageView) rootView.findViewById(R.id.trivia_vida_facil_3);

        ImageView corazonMedio1 = (ImageView) rootView.findViewById(R.id.trivia_vida_medio_1);
        ImageView corazonMedio2 = (ImageView) rootView.findViewById(R.id.trivia_vida_medio_2);
        ImageView corazonMedio3 = (ImageView) rootView.findViewById(R.id.trivia_vida_medio_3);

        ImageView corazonDificil1 = (ImageView) rootView.findViewById(R.id.trivia_vida_dificil_1);
        ImageView corazonDificil2 = (ImageView) rootView.findViewById(R.id.trivia_vida_dificil_2);
        ImageView corazonDificil3 = (ImageView) rootView.findViewById(R.id.trivia_vida_dificil_3);

        Resources res = getResources();
        Drawable d = res.getDrawable(R.drawable.vida_trivia);

        int nivel = 1;
        if(bloqueoMedio){
            nivel++;
            if(bloqueoDificil){
                nivel++;
            }
        }
        switch (nivel){

            case 1:
                if(vidas == 0){
                    corazonFacil1.setImageDrawable(d);
                    corazonFacil2.setImageDrawable(d);
                    corazonFacil3.setImageDrawable(d);
                }
                if(vidas == 1){
                    corazonFacil3.setImageDrawable(d);
                    corazonFacil2.setImageDrawable(d);
                }
                if(vidas == 2){
                    corazonFacil3.setImageDrawable(d);
                }
                break;

            case 2: if(vidas == 0){
                    corazonFacil1.setImageDrawable(d);
                    corazonFacil2.setImageDrawable(d);
                    corazonFacil3.setImageDrawable(d);

                    corazonMedio1.setImageDrawable(d);
                    corazonMedio2.setImageDrawable(d);
                    corazonMedio3.setImageDrawable(d);
                }
                if(vidas == 1){
                    corazonFacil3.setImageDrawable(d);
                    corazonFacil2.setImageDrawable(d);

                    corazonMedio3.setImageDrawable(d);
                    corazonMedio2.setImageDrawable(d);
                }
                if(vidas == 2){
                    corazonFacil3.setImageDrawable(d);

                    corazonMedio3.setImageDrawable(d);
                }
                break;

            case 3: if(vidas == 0){
                    corazonFacil1.setImageDrawable(d);
                    corazonFacil2.setImageDrawable(d);
                    corazonFacil3.setImageDrawable(d);

                    corazonMedio1.setImageDrawable(d);
                    corazonMedio2.setImageDrawable(d);
                    corazonMedio3.setImageDrawable(d);

                    corazonDificil1.setImageDrawable(d);
                    corazonDificil2.setImageDrawable(d);
                    corazonDificil3.setImageDrawable(d);
                }
                if(vidas == 1){
                    corazonFacil3.setImageDrawable(d);
                    corazonFacil2.setImageDrawable(d);

                    corazonMedio3.setImageDrawable(d);
                    corazonMedio2.setImageDrawable(d);

                    corazonDificil3.setImageDrawable(d);
                    corazonDificil2.setImageDrawable(d);
                }
                if(vidas == 2){
                    corazonFacil3.setImageDrawable(d);
                    corazonMedio3.setImageDrawable(d);
                    corazonDificil3.setImageDrawable(d);
                }
                break;
        }

        AQuery aq = new AQuery(rootView);
        String ruta = null;

        if(programa.getImageWidthType(Width.ORIGINAL_IMAGE,Type.POSTER_IMAGE) != null){
            ruta = programa.getImageWidthType(Width.ORIGINAL_IMAGE, Type.POSTER_IMAGE).getImagePath();
        }

        if(ruta != null){
            aq.id(fondoFacil).image(ruta);
            aq.id(fondoMedio).image(ruta);
            aq.id(fondoDificil).image(ruta);
        }
    }

    public void setDataNivelFacil(){

        Trivia t = new Trivia();
        t.setPreguntas(facil);
        TriviaGameFragment fragmentoTriviaJuego = new TriviaGameFragment(trivia,t,programa);
        FragmentTransaction ft =getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.contenedor_trivia,fragmentoTriviaJuego);
        ft.commit();
    }

    public void setDataNivelMedio(){
        //cleanTrivia();
        Trivia t = new Trivia();
        t.setPreguntas(medio);
        TriviaGameFragment fragmentoTriviaJuego = new TriviaGameFragment(trivia,t,programa);
        FragmentTransaction ft =getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.contenedor_trivia,fragmentoTriviaJuego);
        ft.commit();
    }

    public void setDataNivelDificil(){
        //cleanTrivia();
        Trivia t = new Trivia();
        t.setPreguntas(dificil);
        TriviaGameFragment fragmentoTriviaJuego = new TriviaGameFragment(trivia,t,programa);
        FragmentTransaction ft =getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.contenedor_trivia,fragmentoTriviaJuego);
        ft.commit();
    }

    public void cleanTrivia(){

        switch(NIVEL_ACTUAL){
            case 1:     break ;
            case 2:     for(int i = 0; i < trivia.getPreguntas().size();i++){
                            if(trivia.getPreguntas().get(i).getLevel() == 1 ){
                                trivia.getPreguntas().remove(i);
                            }
                        }
                        break ;
            case 3:     for(int i = 0; i < trivia.getPreguntas().size();i++){
                            if(trivia.getPreguntas().get(i).getLevel() == 1 || trivia.getPreguntas().get(i).getLevel() == 2){
                                trivia.getPreguntas().remove(i);
                            }
                        }
                        break ;
        }
    }
    public void separateNivel(){

        facil = new ArrayList<TriviaQuestion>();
        medio = new ArrayList<TriviaQuestion>();
        dificil = new ArrayList<TriviaQuestion>();

        for(int i = 0; i < trivia.getPreguntas().size();i++){

            //Log.e("Nombre "+trivia.getPreguntas().get(i).getLevel(),trivia.getPreguntas().get(i).getText());

            switch (trivia.getPreguntas().get(i).getLevel()){

                case 0:     facil.add(trivia.getPreguntas().get(i));
                            Log.e("0 "+trivia.getPreguntas().get(i).getLevel(),trivia.getPreguntas().get(i).getText());
                            break;

                case 1:     facil.add(trivia.getPreguntas().get(i));
                            Log.e("Facil "+trivia.getPreguntas().get(i).getLevel(),trivia.getPreguntas().get(i).getText());
                            break;
                case 2:     medio.add(trivia.getPreguntas().get(i));
                            Log.e("Medio "+trivia.getPreguntas().get(i).getLevel(),trivia.getPreguntas().get(i).getText());
                            break;
                case 3:     dificil.add(trivia.getPreguntas().get(i));
                            Log.e("Dificil "+trivia.getPreguntas().get(i).getLevel(),trivia.getPreguntas().get(i).getText());
                            break;
            }
        }
    }
    private void obtieneTrivia(){

        if(programa != null){
            DataLoader dataLoader = new DataLoader(getActivity());
            dataLoader.getTrivia(new DataLoader.DataLoadedHandler<Trivia>() {

                @Override
                public void loaded(final Trivia data) {
                    if(data != null){
                        trivia = data;
                        setData();
                    }
                }

                @Override
                public void error(String error) {
                    super.error(error);
                    Log.e("Preview error isdubf", " Trivia error --> " + error);
                }
            }, programa.getTitle());
        }

    }
    public void setDataVacio(){
        contenedorVacio.setVisibility(View.VISIBLE);
    }
    public Trivia getTrivia() {
        return trivia;
    }

    public void setTrivia(Trivia trivia) {
        this.trivia = trivia;
    }
}
