package com.smartboxtv.movistartv.programation.preview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.database.DataBaseUser;
import com.smartboxtv.movistartv.data.database.UserNunchee;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Polls;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.social.DialogShare;

import java.text.SimpleDateFormat;

/**
 * Created by Esteban- on 06-05-14.
 */
public class PollMaxFragment extends Fragment {

    private final Polls polls;

    private LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5;
    private LinearLayout linearLayout1_,linearLayout2_,linearLayout3_,linearLayout4_,linearLayout5_;
    private LinearLayout espacio_porcentaje1,espacio_porcentaje2,espacio_porcentaje3,espacio_porcentaje4,espacio_porcentaje5;
    private RelativeLayout linearLayout1_resultado,contenedorNoEncuesta,linearLayout2_resultado,linearLayout3_resultado,linearLayout4_resultado,linearLayout5_resultado;

    private TextView respuesta1,respuesta2,respuesta3,respuesta4,respuesta5,titulo,subtitulo, numeroPregunta;
    private TextView resultado1,resultado2,resultado3,resultado4,resultado5;
    private TextView respuesta1_,respuesta2_,respuesta3_,respuesta4_,respuesta5_,titulo_,subtitulo_;
    private TextView respuesta1_reultado,respuesta2_resultado,respuesta3_resultado,respuesta4_resultado,respuesta5_resultado,titulo_resultado,subtitulo_resultado;

    private Button volver;
    private Button avanzar;
    private Button facebook;
    private Button twitter;
    private RelativeLayout contenedorPrimera;
    private RelativeLayout contenedorSegunda;
    private RelativeLayout contenedorBoton;
    private RelativeLayout contenedorResultado;
    private Program programa;
    private Program programaPreview;

    private int contador = 0;
    private double porcentaje;
    private int contadorRespuesta = 0;
    private int[] cantidadVotos;

    //Facebook is Acrive
    private boolean fbActivate;

    private Animation animacion_in;

    public PollMaxFragment(Polls polls, Program p, Program preview) {

        this.polls = polls;
        this.programa = p;
        this.programaPreview = preview;
        if(polls != null)
            loadPoll();

    }

    public void loadPoll(){
        cantidadVotos = new int[polls.getPreguntas().size()];

        for(int i = 0; i < polls.getPreguntas().size(); i++){
            for(int j = 0; j < polls.getPreguntas().get(i).getRespuestas().size(); j++){
                cantidadVotos[i] = cantidadVotos[i] + polls.getPreguntas().get(i).getRespuestas().get(j).getVotos();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.preview_fg_poll_max, container, false);

        animacion_in = AnimationUtils.loadAnimation(getActivity(), R.anim.derecha_encuesta);
        Animation animacion_out = AnimationUtils.loadAnimation(getActivity(), R.anim.derecha_encuesta_out);

        avanzar = (Button) rootView.findViewById(R.id.encuesta_siguiente);
        volver = (Button) rootView.findViewById(R.id.encuesta_volver);
        facebook = (Button) rootView.findViewById(R.id.share_facebook);
        twitter = (Button) rootView.findViewById(R.id.share_tw);

        contenedorPrimera = (RelativeLayout) rootView.findViewById(R.id.primer_encuesta);
        contenedorSegunda = (RelativeLayout) rootView.findViewById(R.id.segunda_encuesta);
        contenedorBoton = (RelativeLayout) rootView.findViewById(R.id.contenedor_boton);
        contenedorResultado = (RelativeLayout) rootView.findViewById(R.id.contenedor_share);
        contenedorNoEncuesta = (RelativeLayout) rootView.findViewById(R.id.no_encuesta);

        contenedorPrimera.setVisibility(View.VISIBLE);
        contenedorSegunda.setVisibility(View.VISIBLE);

        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        // Publish Facebook
        DataBaseUser dataBaseUser = new DataBaseUser(getActivity(), "", null, 0);
        UserNunchee userNunchee = dataBaseUser.select(UserPreference.getIdFacebook(getActivity()));
        fbActivate = userNunchee.isFacebookActive;

        if(polls.getPreguntas().isEmpty()){
            nopolls();
        }
        else{
            if(polls.getPreguntas().size() ==1 ){
                contenedorBoton.setVisibility(View.GONE);
            }
            else{
                contenedorBoton.setVisibility(View.VISIBLE);
            }
            contenedorPrimera.bringToFront();
            contenedorBoton.bringToFront();
        }

        // contenedorSegunda.setBackgroundColor(Color.WHITE);
        contenedorResultado.setVisibility(View.GONE);
        numeroPregunta = (TextView) contenedorBoton.findViewById(R.id.encuesta_numero);
        respuesta1 = (TextView) contenedorPrimera.findViewById(R.id.respuesta1);
        respuesta2 = (TextView) contenedorPrimera.findViewById(R.id.respuesta2);
        respuesta3 = (TextView) contenedorPrimera.findViewById(R.id.respuesta3);
        respuesta4 = (TextView)contenedorPrimera.findViewById(R.id.respuesta4);
        respuesta5 = (TextView)contenedorPrimera.findViewById(R.id.respuesta5);

        titulo = (TextView) contenedorPrimera.findViewById(R.id.titulo_encuesta);
        titulo.setTypeface(bold);
        subtitulo = (TextView) contenedorPrimera.findViewById(R.id.han_contestado_encuesta);
        subtitulo.setTypeface(normal);
        // SegundoContenedor de preguntas
        respuesta1_ = (TextView) contenedorSegunda.findViewById(R.id.respuesta1);
        respuesta2_ = (TextView) contenedorSegunda.findViewById(R.id.respuesta2);
        respuesta3_ = (TextView) contenedorSegunda.findViewById(R.id.respuesta3);
        respuesta4_ = (TextView)contenedorSegunda.findViewById(R.id.respuesta4);
        respuesta5_ = (TextView)contenedorSegunda.findViewById(R.id.respuesta5);

        titulo_ = (TextView) contenedorSegunda.findViewById(R.id.titulo_encuesta);
        titulo_.setTypeface(bold);
        subtitulo_ = (TextView) contenedorSegunda.findViewById(R.id.han_contestado_encuesta);
        subtitulo_.setTypeface(normal);

        // Resultado
        respuesta1_reultado = (TextView) contenedorResultado.findViewById(R.id.respuesta1);
        respuesta2_resultado = (TextView) contenedorResultado.findViewById(R.id.respuesta2);
        respuesta3_resultado = (TextView) contenedorResultado.findViewById(R.id.respuesta3);
        respuesta4_resultado = (TextView)contenedorResultado.findViewById(R.id.respuesta4);
        respuesta5_resultado = (TextView)contenedorResultado.findViewById(R.id.respuesta5);

        resultado1 = (TextView) contenedorResultado.findViewById(R.id.porcentaje1);
        resultado2 = (TextView) contenedorResultado.findViewById(R.id.porcentaje2);
        resultado3 = (TextView) contenedorResultado.findViewById(R.id.porcentaje3);
        resultado4 = (TextView) contenedorResultado.findViewById(R.id.porcentaje4);
        resultado5 = (TextView) contenedorResultado.findViewById(R.id.porcentaje5);

        titulo_resultado = (TextView) contenedorResultado.findViewById(R.id.titulo_encuesta);
        subtitulo_resultado = (TextView) contenedorResultado.findViewById(R.id.han_contestado_encuesta);
        ImageView foto = (ImageView) contenedorPrimera.findViewById(R.id.foto_polls);

        // SegundoContenedor de preguntas
        ImageView foto_ = (ImageView) contenedorSegunda.findViewById(R.id.foto_polls);
        RelativeLayout noEncuesta = (RelativeLayout) rootView.findViewById(R.id.no_encuesta);

        linearLayout1 = (LinearLayout) contenedorPrimera.findViewById(R.id.encuesta_respuesta_1);
        linearLayout2 = (LinearLayout) contenedorPrimera.findViewById(R.id.encuesta_respuesta_2);
        linearLayout3 = (LinearLayout) contenedorPrimera.findViewById(R.id.encuesta_respuesta_3);
        linearLayout4 = (LinearLayout) contenedorPrimera.findViewById(R.id.encuesta_respuesta_4);
        linearLayout5 = (LinearLayout) contenedorPrimera.findViewById(R.id.encuesta_respuesta_5);

        // SegundoContenedor de preguntas
        linearLayout1_ = (LinearLayout) contenedorSegunda.findViewById(R.id.encuesta_respuesta_1);
        linearLayout2_ = (LinearLayout) contenedorSegunda.findViewById(R.id.encuesta_respuesta_2);
        linearLayout3_ = (LinearLayout) contenedorSegunda.findViewById(R.id.encuesta_respuesta_3);
        linearLayout4_ = (LinearLayout) contenedorSegunda.findViewById(R.id.encuesta_respuesta_4);
        linearLayout5_ = (LinearLayout) contenedorSegunda.findViewById(R.id.encuesta_respuesta_5);

        // Contenedor Resultado
        linearLayout1_resultado = (RelativeLayout) contenedorResultado.findViewById(R.id.encuesta_respuesta_1);
        linearLayout2_resultado = (RelativeLayout) contenedorResultado.findViewById(R.id.encuesta_respuesta_2);
        linearLayout3_resultado = (RelativeLayout) contenedorResultado.findViewById(R.id.encuesta_respuesta_3);
        linearLayout4_resultado = (RelativeLayout) contenedorResultado.findViewById(R.id.encuesta_respuesta_4);
        linearLayout5_resultado = (RelativeLayout) contenedorResultado.findViewById(R.id.encuesta_respuesta_5);

        // Espacio porcentaje
        espacio_porcentaje1 = (LinearLayout) contenedorResultado.findViewById(R.id.espacio_porcentaje1);
        espacio_porcentaje2 = (LinearLayout) contenedorResultado.findViewById(R.id.espacio_porcentaje2);
        espacio_porcentaje3 = (LinearLayout) contenedorResultado.findViewById(R.id.espacio_porcentaje3);
        espacio_porcentaje4 = (LinearLayout) contenedorResultado.findViewById(R.id.espacio_porcentaje4);
        espacio_porcentaje5 = (LinearLayout) contenedorResultado.findViewById(R.id.espacio_porcentaje5);

        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.GONE);
        linearLayout3.setVisibility(View.GONE);
        linearLayout4.setVisibility(View.GONE);
        linearLayout5.setVisibility(View.GONE);

        // SegundoContenedor de preguntas
        linearLayout1_.setVisibility(View.GONE);
        linearLayout2_.setVisibility(View.GONE);
        linearLayout3_.setVisibility(View.GONE);
        linearLayout4_.setVisibility(View.GONE);
        linearLayout5_.setVisibility(View.GONE);

        volver.setEnabled(false);
        volver.setAlpha((float) 0.5);

        if(!polls.getPreguntas().isEmpty()){
            if(polls.getPreguntas().size()== 1){

                avanzar.setEnabled(false);
                avanzar.setAlpha((float)0.5);
            }
        }
        setData();

        if(polls.getPreguntas().size()== 0){

            titulo.setVisibility(View.GONE);
            subtitulo.setVisibility(View.GONE);
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.GONE);
            linearLayout4.setVisibility(View.GONE);
            linearLayout5.setVisibility(View.GONE);

            foto.setVisibility(View.GONE);
            noEncuesta.setVisibility(View.VISIBLE);
        }

        // set onClickListener Share

        if(polls.getPreguntas().size() > 1){
            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fbActivate){

                        shareDialog();

                        facebook.setEnabled(false);
                        facebook.setAlpha((float) 0.5);
                    }
                    else{
                        noPublish();
                    }
                }
            });

            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
        }

        // Set onClickListener respuestas
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout1.setBackgroundColor(Color.parseColor("#034767"));
                ImageView margen = (ImageView) linearLayout1.findViewById(R.id.margin_check);
                margen.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(linearLayout1, View.ALPHA, 1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(anim);
                animSet.setDuration(1300);
                animSet.start();
                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        DataLoader dataLoader = new DataLoader(getActivity());
                        dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(0).getIdAnswer());

                        polls.getPreguntas().get(contador).getRespuestas().get(0).setVotos(polls.getPreguntas()
                                .get(contador).getRespuestas().get(0).getVotos()+1);

                        cantidadVotos[contador] = cantidadVotos[contador]+1;
                        contadorRespuesta = 0;
                        setDataResultado();
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout2.setBackgroundColor(Color.parseColor("#034767"));
                ImageView margen = (ImageView) linearLayout2.findViewById(R.id.margin_check);
                margen.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(linearLayout2, View.ALPHA, 1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(anim);
                animSet.setDuration(1300);
                animSet.start();
                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        DataLoader dataLoader = new DataLoader(getActivity());
                        dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(1).getIdAnswer());

                        polls.getPreguntas().get(contador).getRespuestas().get(1).setVotos(polls.getPreguntas()
                                .get(contador).getRespuestas().get(1).getVotos()+1);

                        cantidadVotos[contador] = cantidadVotos[contador]+1;
                        contadorRespuesta = 1;
                        setDataResultado();
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout3.setBackgroundColor(Color.parseColor("#034767"));
                ImageView margen = (ImageView) linearLayout3.findViewById(R.id.margin_check);
                margen.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(linearLayout3, View.ALPHA, 1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(anim);
                animSet.setDuration(1300);
                animSet.start();
                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        DataLoader dataLoader = new DataLoader(getActivity());
                        dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(2).getIdAnswer());

                        polls.getPreguntas().get(contador).getRespuestas().get(2).setVotos(polls.getPreguntas()
                                .get(contador).getRespuestas().get(2).getVotos()+1);

                        cantidadVotos[contador] = cantidadVotos[contador]+1;
                        contadorRespuesta = 1;
                        setDataResultado();
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout4.setBackgroundColor(Color.parseColor("#034767"));
                ImageView margen = (ImageView) linearLayout4.findViewById(R.id.margin_check);
                margen.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(linearLayout4, View.ALPHA, 1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(anim);
                animSet.setDuration(1300);
                animSet.start();
                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        DataLoader dataLoader = new DataLoader(getActivity());
                        dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(3).getIdAnswer());

                        polls.getPreguntas().get(contador).getRespuestas().get(3).setVotos(polls.getPreguntas()
                                .get(contador).getRespuestas().get(3).getVotos()+1);

                        cantidadVotos[contador] = cantidadVotos[contador]+1;
                        contadorRespuesta = 3;
                        setDataResultado();
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
            }
        });
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout5.setBackgroundColor(Color.parseColor("#034767"));
                ImageView margen = (ImageView) linearLayout5.findViewById(R.id.margin_check);
                margen.setVisibility(View.VISIBLE);
                ObjectAnimator anim = ObjectAnimator.ofFloat(linearLayout5, View.ALPHA, 1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(anim);
                animSet.setDuration(1300);
                animSet.start();
                animSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        DataLoader dataLoader = new DataLoader(getActivity());
                        dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(4).getIdAnswer());

                        polls.getPreguntas().get(contador).getRespuestas().get(4).setVotos(polls.getPreguntas()
                                .get(contador).getRespuestas().get(4).getVotos()+1);

                        cantidadVotos[contador] = cantidadVotos[contador]+1;
                        contadorRespuesta = 4;
                        setDataResultado();
                    }
                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
            }
        });

        // Segundo contenedor de respuesta

        linearLayout1_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader dataLoader = new DataLoader(getActivity());
                dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(0).getIdAnswer());

                polls.getPreguntas().get(contador).getRespuestas().get(0).setVotos(polls.getPreguntas()
                        .get(contador).getRespuestas().get(0).getVotos()+1);

                cantidadVotos[contador] = cantidadVotos[contador]+1;
                contadorRespuesta = 0;
                setDataResultado();

            }
        });

        linearLayout2_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader dataLoader = new DataLoader(getActivity());
                dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(1).getIdAnswer());

                polls.getPreguntas().get(contador).getRespuestas().get(1).setVotos(polls.getPreguntas()
                        .get(contador).getRespuestas().get(1).getVotos()+1);

                cantidadVotos[contador] = cantidadVotos[contador]+1;
                contadorRespuesta = 1;
                setDataResultado();
            }
        });
        linearLayout3_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader dataLoader = new DataLoader(getActivity());
                dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(2).getIdAnswer());

                polls.getPreguntas().get(contador).getRespuestas().get(2).setVotos(polls.getPreguntas()
                        .get(contador).getRespuestas().get(2).getVotos()+1);

                cantidadVotos[contador] = cantidadVotos[contador]+1;
                contadorRespuesta = 2;
                setDataResultado();
            }
        });
        linearLayout4_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader dataLoader = new DataLoader(getActivity());

                dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(3).getIdAnswer());

                polls.getPreguntas().get(contador).getRespuestas().get(3).setVotos(polls.getPreguntas()
                        .get(contador).getRespuestas().get(3).getVotos()+1);

                cantidadVotos[contador] = cantidadVotos[contador]+1;
                contadorRespuesta = 3;
                setDataResultado();
            }
        });
        linearLayout5_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader dataLoader = new DataLoader(getActivity());
                dataLoader.votoEncuesta("" + polls.getPreguntas().get(contador).getRespuestas().get(4).getIdAnswer());

                polls.getPreguntas().get(contador).getRespuestas().get(4).setVotos(polls.getPreguntas().
                        get(contador).getRespuestas().get(4).getVotos()+1);

                cantidadVotos[contador] = cantidadVotos[contador]+1;
                contadorRespuesta = 4;
                setDataResultado();
            }
        });

        volver.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(contador > 0){

                    contador--;
                    if(contador % 2 == 0 || contador == 0){
                        setData(true);
                    }
                    else if(contador % 2 != 0 ){
                        setDataImpar(true);
                    }
                    if(contador == 0){
                        volver.setEnabled(false);
                        volver.setAlpha((float)0.5);
                    }
                    if(!avanzar.isEnabled()){
                        avanzar.setEnabled(true);
                        avanzar.setAlpha(1);
                    }

                    linearLayout1_resultado.setVisibility(View.GONE);
                    linearLayout2_resultado.setVisibility(View.GONE);
                    linearLayout3_resultado.setVisibility(View.GONE);
                    linearLayout4_resultado.setVisibility(View.GONE);
                    linearLayout5_resultado.setVisibility(View.GONE);

                    linearLayout1.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout2.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout3.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout4.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout5.setBackgroundColor(Color.parseColor("#272727"));

                    linearLayout1_.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout2_.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout3_.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout4_.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout5_.setBackgroundColor(Color.parseColor("#272727"));

                    ImageView margen1 = (ImageView) linearLayout1.findViewById(R.id.margin_check);
                    margen1.setVisibility(View.GONE);

                    ImageView margen2 = (ImageView) linearLayout2.findViewById(R.id.margin_check);
                    margen2.setVisibility(View.GONE);

                    ImageView margen3 = (ImageView) linearLayout3.findViewById(R.id.margin_check);
                    margen3.setVisibility(View.GONE);

                    ImageView margen4 = (ImageView) linearLayout4.findViewById(R.id.margin_check);
                    margen4.setVisibility(View.GONE);


                    ImageView margen5 = (ImageView) linearLayout1_.findViewById(R.id.margin_check);
                    margen5.setVisibility(View.GONE);

                    ImageView margen6 = (ImageView) linearLayout2_.findViewById(R.id.margin_check);
                    margen6.setVisibility(View.GONE);

                    ImageView margen7 = (ImageView) linearLayout3_.findViewById(R.id.margin_check);
                    margen7.setVisibility(View.GONE);

                    ImageView margen8 = (ImageView) linearLayout4_.findViewById(R.id.margin_check);
                    margen8.setVisibility(View.GONE);

                }
            }
        });

        avanzar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(contador < polls.getPreguntas().size()){
                    contador++;

                    if(contador % 2 == 0 || contador == 0){
                        setData(false);
                    }
                    else if(contador % 2 != 0 ){
                        setDataImpar(false);
                    }

                    if(contador == polls.getPreguntas().size()-1){
                        avanzar.setEnabled(false);
                        avanzar.setAlpha((float)0.5);
                    }

                    if(!volver.isEnabled()){
                        volver.setEnabled(true);
                        volver.setAlpha(1);
                    }

                    linearLayout1_resultado.setVisibility(View.GONE);
                    linearLayout2_resultado.setVisibility(View.GONE);
                    linearLayout3_resultado.setVisibility(View.GONE);
                    linearLayout4_resultado.setVisibility(View.GONE);
                    linearLayout5_resultado.setVisibility(View.GONE);

                    linearLayout1.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout2.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout3.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout4.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout5.setBackgroundColor(Color.parseColor("#272727"));

                    linearLayout1_.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout2_.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout3_.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout4_.setBackgroundColor(Color.parseColor("#272727"));
                    linearLayout5_.setBackgroundColor(Color.parseColor("#272727"));

                    ImageView margen1 = (ImageView) linearLayout1.findViewById(R.id.margin_check);
                    margen1.setVisibility(View.GONE);

                    ImageView margen2 = (ImageView) linearLayout2.findViewById(R.id.margin_check);
                    margen2.setVisibility(View.GONE);

                    ImageView margen3 = (ImageView) linearLayout3.findViewById(R.id.margin_check);
                    margen3.setVisibility(View.GONE);

                    ImageView margen4 = (ImageView) linearLayout4.findViewById(R.id.margin_check);
                    margen4.setVisibility(View.GONE);


                    ImageView margen5 = (ImageView) linearLayout1_.findViewById(R.id.margin_check);
                    margen5.setVisibility(View.GONE);

                    ImageView margen6 = (ImageView) linearLayout2_.findViewById(R.id.margin_check);
                    margen6.setVisibility(View.GONE);

                    ImageView margen7 = (ImageView) linearLayout3_.findViewById(R.id.margin_check);
                    margen7.setVisibility(View.GONE);

                    ImageView margen8 = (ImageView) linearLayout4_.findViewById(R.id.margin_check);
                    margen8.setVisibility(View.GONE);
                }
            }
        });
        return rootView;
    }
    public void noPublish(){
        Toast.makeText(getActivity(), "Activa el Autopost para poder publicar", Toast.LENGTH_LONG).show();
    }
    public void nopolls(){

        contenedorPrimera.setVisibility(View.GONE);
        contenedorSegunda.setVisibility(View.GONE);
        contenedorResultado.setVisibility(View.GONE);
        contenedorBoton.setVisibility(View.GONE);
        contenedorNoEncuesta.setVisibility(View.VISIBLE);
    }

    public void setDataResultado(){

        boolean result = true;
        contenedorResultado.startAnimation(animacion_in);

        if(contenedorResultado.getVisibility()== View.GONE)
            contenedorResultado.setVisibility(View.VISIBLE);

        contenedorResultado.bringToFront();
        contenedorBoton.bringToFront();

        //contenedorPrimera.setVisibility(View.GONE);
        //contenedorSegunda.setVisibility(View.GONE);

        if(!polls.getPreguntas().isEmpty()){
            if(!polls.getPreguntas().get(contador).getRespuestas().isEmpty()){

                // Respuesta 1
                linearLayout1_resultado.setVisibility(View.VISIBLE);
                respuesta1_reultado.setText(polls.getPreguntas().get(contador).getRespuestas().get(0).getText());
                resultado1.setText(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(0)
                        .getVotos())+"%");

                // Calculo del espacio de porcentaje
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) espacio_porcentaje1.getLayoutParams();
                assert params != null;
                params.width = calculaAncho(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(0)
                        .getVotos()), espacio_porcentaje1);
                espacio_porcentaje1.setLayoutParams(params);

                // Respuesta 2
                if(polls.getPreguntas().get(contador).getRespuestas().size()>1){

                    linearLayout2_resultado.setVisibility(View.VISIBLE);
                    respuesta2_resultado.setText(polls.getPreguntas().get(contador).getRespuestas().get(1).getText());
                    resultado2.setText(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(1)
                            .getVotos())+"%");

                    // Calculo del espacio de porcentaje
                    RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) espacio_porcentaje2.getLayoutParams();
                    assert params2 != null;
                    params2.width = calculaAncho(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(1)
                            .getVotos()), espacio_porcentaje2);
                    espacio_porcentaje2.setLayoutParams(params2);
                }
                // Respuesta 3
                if(polls.getPreguntas().get(contador).getRespuestas().size()>2){

                    linearLayout3_resultado.setVisibility(View.VISIBLE);
                    respuesta3_resultado.setText(polls.getPreguntas().get(contador).getRespuestas().get(2).getText());
                    resultado3.setText(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(2)
                            .getVotos())+"%");

                    // Calculo del espacio de porcentaje

                    RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) espacio_porcentaje3.getLayoutParams();
                    assert params3 != null;
                    params3.width = calculaAncho(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(2)
                            .getVotos()), espacio_porcentaje3);
                    espacio_porcentaje3.setLayoutParams(params3);
                }
                // Respuesta 4
                if(polls.getPreguntas().get(contador).getRespuestas().size()>3){

                    linearLayout4_resultado.setVisibility(View.VISIBLE);
                    respuesta4_resultado.setText(polls.getPreguntas().get(contador).getRespuestas().get(3).getText());
                    resultado4.setText(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(3)
                            .getVotos())+"%");

                    // Calculo del espacio de porcentaje
                    RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) espacio_porcentaje4.getLayoutParams();
                    assert params4 != null;
                    params4.width = calculaAncho(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas()
                            .get(3).getVotos()), espacio_porcentaje4);
                    espacio_porcentaje4.setLayoutParams(params4);

                }
                // Respuesta 5
                if(polls.getPreguntas().get(contador).getRespuestas().size()> 4 ){

                    linearLayout5_resultado.setVisibility(View.VISIBLE);
                    respuesta5_resultado.setText(polls.getPreguntas().get(contador).getRespuestas().get(4).getText());
                    resultado5.setText(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(4)
                            .getVotos())+"%");

                    // Calculo del espacio de porcentaje

                    RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) espacio_porcentaje5.getLayoutParams();

                    assert params5 != null;
                    params5.width = calculaAncho(calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas()
                            .get(4).getVotos()), espacio_porcentaje5);

                    espacio_porcentaje5.setLayoutParams(params5);
                }

                titulo_resultado.setText(polls.getPreguntas().get(contador).getText());
                subtitulo_resultado.setText(cantidadVotos[contador]+" Respuestas");
                numeroPregunta.setText("Pregunta "+(contador+1));
            }
        }

        final String imageUrl= "https://tvsmartbox.com/MovistarTV/icon-post-facebok_polls.png";

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                porcentaje = calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(contadorRespuesta).getVotos());
                int p = (int) porcentaje;
                String text = polls.getPreguntas().get(contador).getText()+" Mi respuesta fue: "+polls.getPreguntas().get(contador).getRespuestas()
                        .get(contadorRespuesta).getText()+" El "+p+"% piensa como yo!";
                DialogShare dialog = new DialogShare(text,"Pon a prueba tus conocimientos y desafía a tus amigos." +
                                                   "Con Movistar TV podrás interactuar, descubrir y compartir facetas que nunca imaginaste de" +
                                                   " tus programas favoritos. Descarga Movistar TV y vive la TV más social que nunca!"
                        ,imageUrl,"","http://www.movistar.cl/PortalMovistarWeb/tv-digital/programacion");
                dialog.show(getFragmentManager(),"");

            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                porcentaje = calculaPorcentaje(polls.getPreguntas().get(contador).getRespuestas().get(contadorRespuesta).getVotos());
                int p = (int) porcentaje;
                String text = polls.getPreguntas().get(contador).getText()+" Mi respuesta fue: "+polls.getPreguntas().get(contador).getRespuestas()
                        .get(contadorRespuesta).getText()+". El "+p+"% piensa como yo!";
                String hasthTag = "@MovistarTV";
                if(text.length() < 128){
                    text +=hasthTag;
                }
                else{
                    text = text.substring(0,124)+"..."+hasthTag;
                }

                DialogShare dialog = new DialogShare(text, true);
                dialog.show(getFragmentManager(),"");

            }
        });
    }

    public void setData(boolean esVolver){

        if(contenedorResultado.getVisibility() == View.VISIBLE)
            contenedorResultado.setVisibility(View.GONE);

        if(!esVolver){
            contenedorPrimera.startAnimation(animacion_in);
            Log.e("seData","Avanzar");
        }
        else{
            contenedorPrimera.startAnimation(animacion_in);
            Log.e("seData","Volver");
        }

        contenedorPrimera.bringToFront();
        contenedorBoton.bringToFront();


        if(!polls.getPreguntas().isEmpty()){

            if(!polls.getPreguntas().get(contador).getRespuestas().isEmpty()){

                linearLayout1.setVisibility(View.VISIBLE);
                respuesta1.setText(polls.getPreguntas().get(contador).getRespuestas().get(0).getText());

                if(polls.getPreguntas().get(contador).getRespuestas().size()>1){

                    linearLayout2.setVisibility(View.VISIBLE);
                    respuesta2.setText(polls.getPreguntas().get(contador).getRespuestas().get(1).getText());
                }

                if(polls.getPreguntas().get(contador).getRespuestas().size()>2){

                    linearLayout3.setVisibility(View.VISIBLE);
                    respuesta3.setText(polls.getPreguntas().get(contador).getRespuestas().get(2).getText());
                }

                if(polls.getPreguntas().get(contador).getRespuestas().size()>3){

                    linearLayout4.setVisibility(View.VISIBLE);
                    respuesta4.setText(polls.getPreguntas().get(contador).getRespuestas().get(3).getText());
                }

                if(polls.getPreguntas().get(contador).getRespuestas().size()> 4 ){

                    linearLayout5.setVisibility(View.VISIBLE);
                    respuesta5.setText(polls.getPreguntas().get(contador).getRespuestas().get(5).getText());
                }

                titulo.setText(polls.getPreguntas().get(contador).getText());
                subtitulo.setText(cantidadVotos[contador]+" personas han contestado");
                numeroPregunta.setText("Pregunta "+(contador+1));
            }
        }
    }

    public void setDataImpar(boolean esVolver){

        if(contenedorResultado.getVisibility() == View.VISIBLE)
            contenedorResultado.setVisibility(View.GONE);

        if(esVolver){
            contenedorSegunda.startAnimation(animacion_in);
            Log.e("seDataImpar","Volver");
        }
        else{
            contenedorSegunda.startAnimation(animacion_in);
            Log.e("seDataImpar","Avanzar");
        }

        if(contenedorSegunda.getVisibility()== View.GONE)
            contenedorSegunda.setVisibility(View.VISIBLE);

        contenedorSegunda.bringToFront();
        contenedorBoton.bringToFront();

        if(!polls.getPreguntas().isEmpty()){

            if(!polls.getPreguntas().get(contador).getRespuestas().isEmpty()){

                linearLayout1_.setVisibility(View.VISIBLE);
                respuesta1_.setText(polls.getPreguntas().get(contador).getRespuestas().get(0).getText());

                if(polls.getPreguntas().get(contador).getRespuestas().size()>1){

                    linearLayout2_.setVisibility(View.VISIBLE);
                    respuesta2_.setText(polls.getPreguntas().get(contador).getRespuestas().get(1).getText());
                }

                if(polls.getPreguntas().get(contador).getRespuestas().size()>2){

                    linearLayout3_.setVisibility(View.VISIBLE);
                    respuesta3_.setText(polls.getPreguntas().get(contador).getRespuestas().get(2).getText());
                }

                if(polls.getPreguntas().get(contador).getRespuestas().size()>3){

                    linearLayout4_.setVisibility(View.VISIBLE);
                    respuesta4_.setText(polls.getPreguntas().get(contador).getRespuestas().get(3).getText());

                }

                if(polls.getPreguntas().get(contador).getRespuestas().size()> 4 ){

                    linearLayout5_.setVisibility(View.VISIBLE);
                    respuesta5_.setText(polls.getPreguntas().get(contador).getRespuestas().get(5).getText());
                }

                titulo_.setText(polls.getPreguntas().get(contador).getText());
                subtitulo_.setText(cantidadVotos[contador]+" personas han contestado");
                numeroPregunta.setText("Pregunta "+(contador+1));

            }
        }
    }
    public double calculaPorcentaje(int x){

        double resultado, votos = cantidadVotos[contador], y = x;
        if(cantidadVotos[contador]!= 0){
            resultado = (y * 100) / votos;
            resultado = Math.round(resultado);
        }
        else
            resultado = 0;
        Log.e("Cantidad de Votos totales",""+cantidadVotos[contador]);
        Log.e("Cantidad de Votos ",""+x);
        Log.e("Porcentaje",""+resultado);
        return resultado;
    }

    public int calculaAncho(double y, View v){

        int x = (int) y;
        int resultado,ancho = 350;
        resultado = (ancho * x) / 100;
        return resultado;
    }
    public void setData(){

        contenedorPrimera.setVisibility(View.VISIBLE);
        contenedorSegunda.setVisibility(View.GONE);

        if(!polls.getPreguntas().isEmpty()){

            if(!polls.getPreguntas().get(contador).getRespuestas().isEmpty()){

                linearLayout1.setVisibility(View.VISIBLE);
                respuesta1.setText(polls.getPreguntas().get(contador).getRespuestas().get(0).getText());

                if(polls.getPreguntas().get(contador).getRespuestas().size()>1){

                    linearLayout2.setVisibility(View.VISIBLE);
                    respuesta2.setText(polls.getPreguntas().get(0).getRespuestas().get(1).getText());
                }

                if(polls.getPreguntas().get(contador).getRespuestas().size()>2){

                    linearLayout3.setVisibility(View.VISIBLE);
                    respuesta3.setText(polls.getPreguntas().get(0).getRespuestas().get(2).getText());
                }

                if(polls.getPreguntas().get(contador).getRespuestas().size()>3){

                    linearLayout4.setVisibility(View.VISIBLE);
                    respuesta4.setText(polls.getPreguntas().get(0).getRespuestas().get(3).getText());
                }

                if(polls.getPreguntas().get(contador).getRespuestas().size()> 4 ){

                    linearLayout5.setVisibility(View.VISIBLE);
                    respuesta5.setText(polls.getPreguntas().get(0).getRespuestas().get(5).getText());
                }

                titulo.setText(polls.getPreguntas().get(contador).getText());
                subtitulo.setText("Respuestas "+cantidadVotos[contador]);
                numeroPregunta.setText("Pregunta "+(contador+1));

            }
        }
    }

    public  void shareDialog(){

        SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd' 'HH'$'mm'$'ss");

        String url = "http://www.movistar.cl/PortalMovistarWeb/tv-digital/guia-de-canales";

        Image imagen = programaPreview.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
        String imageUrl;

        imageUrl= "https://tvsmartbox.com/MovistarTV/icon-post-facebok_polls.png";

        String title = programa.Title;
        String description;
        String text = "He contestado un encuesta de "+programa.getTitle()+" a través de Movistar TV";
        if(programaPreview.Description != null)
            description = programaPreview.Description;
        else
            description = "";

        DialogShare dialogShare = new DialogShare(text,description,imageUrl,title,url);
        dialogShare.show(getActivity().getSupportFragmentManager(),"");
    }
}
