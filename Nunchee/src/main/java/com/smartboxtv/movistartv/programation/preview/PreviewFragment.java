package com.smartboxtv.movistartv.programation.preview;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.FacebookDialog;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.clean.DataClean;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Polls;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.Recommendations;
import com.smartboxtv.movistartv.data.models.Trivia;
import com.smartboxtv.movistartv.data.models.Tweets;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.programation.delegates.PreviewImageFavoriteDelegate;
import com.smartboxtv.movistartv.programation.menu.DialogError;
import com.smartboxtv.movistartv.services.DataLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Esteban- on 21-04-14.
 */
public class PreviewFragment extends Fragment {

    private TextView txtName;
    private TextView txtDate;
    private TextView txtDescription;
    private TextView txtChannel;
    private TextView txtHashTags;
    private TextView sugerencia1;
    private TextView sugerencia2;
    private TextView sugerencia3;

    private Button btnLike;
    private Button btnFavorite;
    private Button btnShare;
    private Button btnReminder;
    private Button btnCheckIn_;
    private Button btnCheckIn;

    private ImageView imgFavorite;

    private RelativeLayout r1;
    private RelativeLayout r2;
    private RelativeLayout r3;
    private RelativeLayout contenedorAnimacion;
    private RelativeLayout contenedorTw;
    private RelativeLayout contenedorTrivia;
    private RelativeLayout contenedorEncuesta;
    private RelativeLayout contenedorSugeridos;
    private RelativeLayout contenedorHeader;
    private RelativeLayout contenedorLoading;

    private Program programa;
    private Program programaPreview;

    private List<Tweets> twitts = new ArrayList<Tweets>();
    private AQuery aq;
    private View rootView;
    private Animation animation;
    private Animation animLeft;
    private Trivia trivia = new Trivia();
    private Polls polls = new Polls();
    //private Recommendations recomendaciones = new Recommendations();
    private View viewLoading;
    private ScrollView scrollTw;
    private ScrollView scrollPreview;
    private LayoutInflater inflaterPrivate;
    private LinearLayout contenedorAccion;

    // Componentes del preview
    private HeaderFragment fragmentoHeader;
    private ActionFragment fragmentoAccion;
    private TwFragment fragmentoTw;
    private TwMaxFragment fragmentoTwMax;
    private PollMinFragment fragmentoEncuestaP;
    private PollMaxFragment fragmentoEncuestaMax;
    private TriviaMinFragment fragmentoTriviaP;
    private TriviaMaxFragment fragmentoTriviaMax;
    private BarFragment fragmentoBarra;

    private boolean esTrivia = false;
    private boolean esEncuesta = false;
    private boolean esTw = false;
    private boolean facebookActive;

    // Share Facebook
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;
    private UiLifecycleHelper uiHelper;

    public PreviewFragment(Program programaPreview) {
        this.programa = programaPreview;
    }

    public PreviewFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.preview_fragment ,container, false);

        scrollPreview = (ScrollView) rootView.findViewById(R.id.preview_scroll);
        scrollPreview.setVisibility(View.INVISIBLE);

        inflaterPrivate = inflater;
        contenedorLoading = (RelativeLayout) rootView.findViewById(R.id.preview);

        loading();
        DataClean.garbageCollector("Preview Fragment");

        ImageView exit = (ImageView) rootView.findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout r  = (RelativeLayout) getActivity().findViewById(R.id.contenedor_preview);
                r.removeAllViews();
            }
        });

        // Contenedores
        contenedorEncuesta = (RelativeLayout) rootView.findViewById(R.id.preview_encuesta_contenedor);
        contenedorTrivia = (RelativeLayout) rootView.findViewById(R.id.preview_trivia_contenedor);
        contenedorTw = (RelativeLayout) rootView.findViewById(R.id.preview_tw_contenedor);
        contenedorHeader = (RelativeLayout) rootView.findViewById(R.id.preview_programa);
        contenedorSugeridos = (RelativeLayout) rootView.findViewById(R.id.preview_sugerencias);
        contenedorAccion = (LinearLayout) rootView.findViewById(R.id.preview_accion);
        contenedorAnimacion = (RelativeLayout) rootView.findViewById(R.id.preview_animado_contenedor);
        contenedorAnimacion.setVisibility(View.GONE);

        // Suplentes
        RelativeLayout encuestaSuplente = (RelativeLayout) rootView.findViewById(R.id.preview_animado_encuesta);
        RelativeLayout triviaSuplente = (RelativeLayout) rootView.findViewById(R.id.preview_animado_trivia);
        RelativeLayout twSuplente = (RelativeLayout) rootView.findViewById(R.id.preview_animado_tw);

        RelativeLayout s1 = (RelativeLayout) rootView.findViewById(R.id.s1);
        RelativeLayout s2 = (RelativeLayout) rootView.findViewById(R.id.s2);
        RelativeLayout s3 = (RelativeLayout) rootView.findViewById(R.id.s3);

        txtName = (TextView) rootView.findViewById(R.id.preview_nombre);
        txtDate = (TextView) rootView.findViewById(R.id.preview_hora);
        txtDescription = (TextView) rootView.findViewById(R.id.preview_descripcion);
        txtChannel = (TextView) rootView.findViewById(R.id.preview_nombre_canal);

        btnLike = (Button) rootView.findViewById(R.id.preview_boton_like);
        btnReminder = (Button) rootView.findViewById(R.id.preview_boton_recordar);
        btnFavorite = (Button) rootView.findViewById(R.id.preview_boton_favorito);
        btnShare =  (Button) rootView.findViewById(R.id.preview_boton_compartir);
        btnCheckIn_ = (Button) rootView.findViewById(R.id.preview_check_in_);
        btnCheckIn = (Button) rootView.findViewById(R.id.preview_check_in);

        ImageView imgChannel = (ImageView) rootView.findViewById(R.id.preview_foto_canal);
        ImageView imgProgram = (ImageView) rootView.findViewById(R.id.preview_cabeza_foto);
        imgFavorite = (ImageView) rootView.findViewById(R.id.preview_image_favorito);
        imgFavorite.setVisibility(View.INVISIBLE);

        txtHashTags = (TextView) rootView.findViewById(R.id.preview_tw_canal);

        Resources res = getResources();

        Drawable drwFocusShare = res.getDrawable(R.drawable.compartir_foco_acc);
        Drawable drwFocusFavorite = res.getDrawable(R.drawable.favorito_foco_acc);
        Drawable drwFocusLike = res.getDrawable(R.drawable.like_foco_acc);
        Drawable drwFocusReminder = res.getDrawable(R.drawable.recordar_foco_acc);

        scrollTw = (ScrollView)rootView.findViewById(R.id.preview_scroll_tws);

        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.agranda);
        animLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.left_in);

        facebookActive = UserPreference.isFacebookActive(getActivity());

        // Check In

        btnCheckIn.setVisibility(View.GONE);
        btnCheckIn_.setVisibility(View.GONE);

        // Foto favorito
        /*PreviewImageFavoriteDelegate  imageFavoriteDelegate = new PreviewImageFavoriteDelegate() {
            @Override
            public void showImage(boolean mostrarImagen, ActionFragment fg) {
                fragmentoHeader.muestraImagen(mostrarImagen);
            }
        };
        fragmentoHeader.setImageFavoriteDelegate(imageFavoriteDelegate);*/

        // Sugerencias
        sugerencia1 =(TextView) rootView.findViewById(R.id.sugerencia_nombre1);
        sugerencia2 =(TextView) rootView.findViewById(R.id.sugerencia_nombre2);
        sugerencia3 =(TextView) rootView.findViewById(R.id.sugerencia_nombre3);

        r1 = (RelativeLayout) rootView.findViewById(R.id.s1);
        r2 = (RelativeLayout) rootView.findViewById(R.id.s2);
        r3 = (RelativeLayout) rootView.findViewById(R.id.s3);

        r1.setVisibility(View.INVISIBLE);
        r2.setVisibility(View.INVISIBLE);
        r3.setVisibility(View.INVISIBLE);

        aq = new AQuery(rootView);

        //obtieneEncuesta();

        cargarPreview();
        obtieneRecomendaciones();

        // Capturas de eventos de los contenedores para la animación
        contenedorTw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                esTw = true;
                contenedorAnimacion.setVisibility(View.VISIBLE);

                AnimationCustom animacion = new AnimationCustom(getActivity());
                animacion.desapareceVistas(contenedorTrivia, contenedorEncuesta, contenedorTw, contenedorHeader,
                        contenedorSugeridos, contenedorAccion, btnCheckIn, btnCheckIn_);
                RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.preview_animado_tw);
                r.setVisibility(View.GONE);

                // Fragmento Transaccion

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);

                ft.replace(R.id.preview_animado_header, fragmentoHeader);
                ft.replace(R.id.preview_animado_accion, fragmentoAccion);
                ft.replace(R.id.preview_animado_trivia, fragmentoTriviaP);
                ft.replace(R.id.preview_animado_encuesta, fragmentoEncuestaP);
                ft.replace(R.id.preview_animado_max, fragmentoTwMax);
                ft.replace(R.id.preview_barra,fragmentoBarra);
                ft.commit();

            }
        });

        contenedorEncuesta.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                esEncuesta = true;
                contenedorAnimacion.setVisibility(View.VISIBLE);
                AnimationCustom animacion = new AnimationCustom(getActivity());

                animacion.mueveTrivia(contenedorTrivia,contenedorEncuesta,contenedorTw,contenedorHeader,
                        contenedorSugeridos,contenedorAccion, btnCheckIn, btnCheckIn_);

                RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.preview_animado_encuesta);
                r.setVisibility(View.GONE);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);

                ft.replace(R.id.preview_animado_header, fragmentoHeader);
                ft.replace(R.id.preview_animado_accion, fragmentoAccion);
                ft.replace(R.id.preview_animado_tw, fragmentoTw);
                ft.replace(R.id.preview_animado_trivia, fragmentoTriviaP);
                ft.replace(R.id.preview_animado_max, fragmentoEncuestaMax);
                ft.replace(R.id.preview_barra,fragmentoBarra);
                ft.commit();

            }
        });

        contenedorTrivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                esTrivia = true;
                contenedorAnimacion.setVisibility(View.VISIBLE);
                AnimationCustom animacion = new AnimationCustom(getActivity());

                animacion.mueveTrivia(contenedorTrivia,contenedorEncuesta,contenedorTw,contenedorHeader,
                        contenedorSugeridos,contenedorAccion, btnCheckIn, btnCheckIn_);
                RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.preview_animado_trivia);
                r.setVisibility(View.GONE);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);

                ft.replace(R.id.preview_animado_header, fragmentoHeader);
                ft.replace(R.id.preview_animado_accion, fragmentoAccion);
                ft.replace(R.id.preview_animado_tw, fragmentoTw);
                ft.replace(R.id.preview_animado_encuesta, fragmentoEncuestaP);
                ft.replace(R.id.preview_animado_max, fragmentoTriviaMax);
                ft.replace(R.id.preview_barra,fragmentoBarra);
                ft.commit();

            }
        });

        // Suplente
        encuestaSuplente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!esEncuesta) {

                    esTrivia = false;
                    esEncuesta = true;
                    esTw = false;

                    RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.preview_animado_encuesta);
                    r.setVisibility(View.GONE);

                    RelativeLayout r1 = (RelativeLayout) rootView.findViewById(R.id.preview_animado_tw);

                    if (r1.getVisibility() == View.GONE) {
                        r1.setVisibility(View.VISIBLE);
                    }

                    RelativeLayout r2 = (RelativeLayout) rootView.findViewById(R.id.preview_animado_trivia);

                    if (r2.getVisibility() == View.GONE) {
                        r2.setVisibility(View.VISIBLE);
                    }
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.remove(fragmentoEncuestaP);
                    ft.remove(fragmentoBarra);

                    ft.replace(R.id.preview_animado_tw, fragmentoTw);
                    ft.replace(R.id.preview_animado_trivia, fragmentoTriviaP);
                    ft.replace(R.id.preview_animado_max, fragmentoEncuestaMax);
                    ft.replace(R.id.preview_barra, fragmentoBarra);
                    ft.commit();
                }
            }
        });

        triviaSuplente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!esTrivia) {

                    esTrivia = true;
                    esEncuesta = false;
                    esTw = false;

                    RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.preview_animado_trivia);
                    r.setVisibility(View.GONE);

                    RelativeLayout r1 = (RelativeLayout) rootView.findViewById(R.id.preview_animado_encuesta);

                    if (r1.getVisibility() == View.GONE) {
                        r1.setVisibility(View.VISIBLE);
                    }

                    RelativeLayout r2 = (RelativeLayout) rootView.findViewById(R.id.preview_animado_tw);

                    if (r2.getVisibility() == View.GONE) {
                        r2.setVisibility(View.VISIBLE);
                    }

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.remove(fragmentoTriviaP);
                    ft.remove(fragmentoBarra);

                    ft.replace(R.id.preview_animado_tw, fragmentoTw);
                    ft.replace(R.id.preview_animado_encuesta, fragmentoEncuestaP);
                    ft.replace(R.id.preview_animado_max, fragmentoTriviaMax);
                    ft.replace(R.id.preview_barra, fragmentoBarra);
                    ft.commit();
                }

            }
        });

        twSuplente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!esTw) {

                    esTrivia = false;
                    esEncuesta = false;
                    esTw = true;

                    RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.preview_animado_tw);
                    r.setVisibility(View.GONE);

                    RelativeLayout r1 = (RelativeLayout) rootView.findViewById(R.id.preview_animado_encuesta);
                    r1.setVisibility(View.VISIBLE);

                    RelativeLayout r2 = (RelativeLayout) rootView.findViewById(R.id.preview_animado_trivia);
                    r2.setVisibility(View.VISIBLE);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.remove(fragmentoTw);
                    ft.remove(fragmentoBarra);

                    ft.commit();

                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.preview_animado_encuesta, fragmentoEncuestaP);
                    ft.replace(R.id.preview_animado_trivia, fragmentoTriviaP);
                    ft.replace(R.id.preview_animado_max, fragmentoTwMax);
                    ft.replace(R.id.preview_barra, fragmentoBarra);
                    ft.commit();
                }
            }
        });

        // Listener de botonces de acción social
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader data = new DataLoader(getActivity());
                data.actionCheckIn(UserPreference.getIdNunchee(getActivity()), "7", programaPreview.getIdProgram(),
                        programaPreview.getPChannel().getChannelID());

                btnCheckIn.startAnimation(animation);
                btnCheckIn_.setText("+ " + (programaPreview.getCheckIn() + 1));
                btnCheckIn.setAlpha((float) 0.5);
                btnCheckIn.setEnabled(false);

                if(facebookActive)
                    publishCheckIn();

            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader data = new DataLoader(getActivity());
                data.actionLike(UserPreference.getIdNunchee(getActivity()), "2", programaPreview.getIdProgram(),
                        programaPreview.getPChannel().getChannelID());

                btnLike.startAnimation(animation);
                btnLike.setText((programaPreview.getLike() + 1) + " Likes");
                btnLike.setEnabled(false);
                btnLike.setAlpha((float) 0.5);
                if(facebookActive)
                    publishStory();
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader data = new DataLoader(getActivity());
                data.actionFavorite(UserPreference.getIdNunchee(getActivity()), "4", programaPreview.getIdProgram(),
                        programaPreview.getPChannel().getChannelID());

                btnFavorite.startAnimation(animation);
                btnFavorite.setAlpha((float) 0.5);
                btnFavorite.setEnabled(false);

                imgFavorite.setVisibility(View.VISIBLE);
                imgFavorite.bringToFront();

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader data = new DataLoader(getActivity());
                data.actionShare(UserPreference.getIdNunchee(getActivity()), "3", programaPreview.getIdProgram()
                        , programaPreview.getPChannel().getChannelID());

                btnShare.setEnabled(false);
                btnShare.startAnimation(animation);
                btnShare.setAlpha((float) 0.5);
            }
        });

        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReminder.setEnabled(false);
                btnReminder.startAnimation(animation);
                btnReminder.setAlpha((float) 0.5);

                createReminder(programa);
            }
        });

        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

        return rootView;
    }

    public void cargarPreview(){

        SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH$mm$ss");
        final SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat formatDia = new SimpleDateFormat("MMM dd");
        final Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");

        if(programa != null){
            //Log.e("Programa", programa.getTitle());
            DataLoader dataLoader = new DataLoader(getActivity());
            dataLoader.getPreview(new DataLoader.DataLoadedHandler<Program>() {

                @Override
                public void loaded(final Program data) {

                    programaPreview = data;
                    // Inicialización de Fragmentos
                    fragmentoHeader = new HeaderFragment(programaPreview, programa);
                    fragmentoAccion = new ActionFragment(programaPreview, programa);
                    PreviewImageFavoriteDelegate delegate = new PreviewImageFavoriteDelegate() {
                        @Override
                        public void showImage(boolean mostrarImagen, ActionFragment fg) {
                            fragmentoHeader.muestraImagen(true);
                        }
                    };
                    fragmentoAccion.setImageFavoriteDelegate(delegate);
                    //fragmentoAccion.setDelegadoIMagenFavoritoPreview(delegadoIMagenFavoritoPreview);
                    fragmentoTw = new TwFragment(programaPreview);
                    fragmentoTwMax = new TwMaxFragment(programaPreview);
                    fragmentoBarra = new BarFragment();
                    //programa = data;

                    txtName.setText(data.getTitle());
                    txtName.setTypeface(normal);
                    txtDate.setText(capitalize(formatDia.format(programa.getStartDate())) + ", " +
                            "" + formatHora.format(programa.getStartDate()) + " | " + formatHora.format(programa
                            .getEndDate()));
                    txtDate.setTypeface(normal);

                    txtDescription.setText(data.getDescription());
                    txtDescription.setTypeface(normal);
                    txtChannel.setText(data.getPChannel().getChannelCallLetter() + " " + data.getPChannel()
                            .getChannelNumber());
                    txtChannel.setTypeface(normal);

                    Image image = data.getImageWidthType(Width.ORIGINAL_IMAGE, Type.BACKDROP_IMAGE);

                    if (image != null) {
                        aq.id(R.id.preview_cabeza_foto).image(image.getImagePath());
                    }
                    aq.id(R.id.preview_foto_canal).image(data.getPChannel().getChannelImageURL());

                    if (data.isFavorite()) {
                        imgFavorite.setVisibility(View.VISIBLE);
                        btnFavorite.setEnabled(false);
                        btnFavorite.setAlpha((float) 0.5);
                    }

                    if (data.isICheckIn()) {
                        btnCheckIn.setEnabled(false);
                        btnCheckIn.setAlpha((float) 0.5);
                    }

                    if (data.isILike()) {
                        btnLike.setAlpha((float) 0.5);
                        btnLike.setEnabled(false);
                    }

                    if (data.getHashtags().split(";").length > 0) {
                        txtHashTags.setText(data.getHashtags().split(";")[0]);
                    } else if (data.getHashtags().split(";").length == 0) {
                        txtHashTags.setText("@" + data.getPChannel().getChannelCallLetter());
                    }

                    //LIKES
                    if (data.getLike() > 1) {
                        btnLike.setText("" + data.getLike() + " Likes");
                    } else if (data.getLike() == 1) {
                        btnLike.setText("" + data.getLike() + " Like");
                    }
                    //Check In
                    if (esActual(programa)) {
                        btnCheckIn.setVisibility(View.VISIBLE);
                        btnCheckIn_.setVisibility(View.VISIBLE);
                        btnCheckIn_.setText("+ " + data.getCheckIn());
                    }
                    if (data.getTweets() != null) {
                        if (!data.getTweets().isEmpty()) {
                            cargarTws();
                            recorreTws();
                        } else {
                            noTws();
                        }
                    } else {
                        noTws();
                    }
                    borraLoading();
                    obtieneTrivia();
                    obtieneEncuesta();
                }

                @Override
                public void error(String error) {
                    super.error(error);
                    Log.e("Preview error", " error --> " + error);
                    borraLoading();
                    DialogError dialogError = new DialogError();
                    dialogError.show(getActivity().getSupportFragmentManager(),"");
                }
            }, programa.getIdProgram(), programa.getPChannel().getChannelID(), UserPreference.getIdNunchee(getActivity()),
                    dateFormat.format(programa.getStartDate()), dateFormat.format(programa.getEndDate()));
        }

    }
    public void obtieneRecomendaciones(){

        if(programa != null){

            DataLoader dataLoader = new DataLoader(getActivity());
            dataLoader.getRecomendaciones(new DataLoader.DataLoadedHandler<Recommendations>() {

                @Override
                public void loaded(final Recommendations data) {

                    if (data != null) {
                        Log.e("ObtieneRecomendacion", programa.getTitle());
                        //recomendaciones = data;

                        AQuery aq = new AQuery(getActivity());
                        if (data.getSameCategoria().size() > 0) {

                            for (int i = 0; i < data.getSameCategoria().size(); i++) {
                                Image imagen = data.getSameCategoria().get(i).getImageWidthType(Width.ORIGINAL_IMAGE,
                                        Type.BACKDROP_IMAGE);

                                if (imagen != null && i == 0) {

                                    r1.setVisibility(View.VISIBLE);
                                    r1.startAnimation(animLeft);
                                    aq.id(R.id.sugerencia_imagen1).image(imagen.getImagePath());
                                    sugerencia1.setText(data.getSameCategoria().get(i).getTitle());
                                }

                                if (imagen != null && i == 1) {

                                    if(r1.getVisibility() == View.VISIBLE){
                                        r2.setVisibility(View.VISIBLE);
                                        r2.startAnimation(animLeft);
                                        aq.id(R.id.sugerencia_imagen2).image(imagen.getImagePath());
                                        sugerencia2.setText(data.getSameCategoria().get(i).getTitle());
                                    }
                                    else{
                                        r1.setVisibility(View.VISIBLE);
                                        r1.startAnimation(animLeft);
                                        aq.id(R.id.sugerencia_imagen1).image(imagen.getImagePath());
                                        sugerencia1.setText(data.getSameCategoria().get(i).getTitle());
                                    }
                                }

                                if (imagen != null && i == 2) {

                                    if(r2.getVisibility() == View.VISIBLE && r1.getVisibility() == View.VISIBLE ){
                                        r3.setVisibility(View.VISIBLE);
                                        r3.startAnimation(animLeft);
                                        aq.id(R.id.sugerencia_imagen3).image(imagen.getImagePath());
                                        sugerencia3.setText(data.getSameCategoria().get(i).getTitle());
                                    }
                                    else{
                                        if(r1.getVisibility() == View.VISIBLE){
                                            r2.setVisibility(View.VISIBLE);
                                            r2.startAnimation(animLeft);
                                            aq.id(R.id.sugerencia_imagen2).image(imagen.getImagePath());
                                            sugerencia2.setText(data.getSameCategoria().get(i).getTitle());
                                        }
                                        else{
                                            r1.setVisibility(View.VISIBLE);
                                            r1.startAnimation(animLeft);
                                            aq.id(R.id.sugerencia_imagen1).image(imagen.getImagePath());
                                            sugerencia1.setText(data.getSameCategoria().get(i).getTitle());
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void error(String error) {
                    super.error(error);
                    Log.e("Preview error", " Recomendaciones error --> " + error);
                }
            }, programa.getIdProgram(), programa.getPChannel().getChannelID(), UserPreference.getIdNunchee(getActivity()));
        }
    }
    public boolean esActual(Program p){

        Date ahora = new Date();
        //if(!((p.getStartDate().getTime() > ahora.getTime())   ||  (p.getEndDate().getTime() < ahora.getTime()))){
        return !((p.getStartDate().getTime() > ahora.getTime()) || (p.getEndDate().getTime() < ahora.getTime()));
    }
    private void obtieneTrivia(){

        if(programa != null){

            DataLoader dataLoader = new DataLoader(getActivity());
            dataLoader.getTrivia(new DataLoader.DataLoadedHandler<Trivia>() {

                @Override
                public void loaded(final Trivia data) {

                    if(data != null){

                        trivia = data;
                        fragmentoTriviaP = new TriviaMinFragment(trivia);
                        fragmentoTriviaMax = new TriviaMaxFragment(programaPreview,trivia, false);

                        if(trivia.getPreguntas().size()>0){

                            TriviaFragment fragmentoTrivia = new TriviaFragment(trivia);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();

                            ft.replace(R.id.preview_trivia_contenedor, fragmentoTrivia);
                            ft.commit();
                        }
                    }

                }

                @Override
                public void error(String error) {
                    super.error(error);
                    Log.e("Preview error", " Trivia error --> " + error);
                }
            }, programa.getTitle());
        }

    }

    private void obtieneEncuesta(){

        if(programa != null){

            DataLoader dataLoader = new DataLoader(getActivity());
            dataLoader.getPolls(new DataLoader.DataLoadedHandler<Polls>() {
                @Override
                public void loaded(final Polls data) {

                    if (data != null) {
                        polls = data;
                        fragmentoEncuestaMax = new PollMaxFragment(polls,programa, programaPreview);
                        fragmentoEncuestaP = new PollMinFragment(polls);

                        if (polls.getPreguntas().size() > 0) {

                            PollFragment fragmentoEncuesta = new PollFragment(polls);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.preview_encuesta_contenedor, fragmentoEncuesta);
                            ft.commit();
                        }
                    }


                }
                @Override
                public void error(String error) {
                    super.error(error);
                    Log.e("Preview error", " Encuenta error --> " + error);
                }
            }, programa.getTitle());
        }
    }

    public void loading(){

        viewLoading = inflaterPrivate.inflate(R.layout.progress_dialog_pop_corn, null);
        ImageView imgPopCorn = (ImageView) viewLoading.findViewById(R.id.pop_corn_centro);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Animation animaPop = AnimationUtils.loadAnimation(getActivity(),R.anim.animacion_pop_hacia_derecha_centro);

        viewLoading.setLayoutParams(params);
        assert (animaPop != null ? animaPop : null) != null;
        imgPopCorn.startAnimation(animaPop != null ? animaPop : null);

        contenedorLoading.addView(viewLoading);
        viewLoading.bringToFront();

        Log.e("Loading","Loading");

        contenedorLoading.bringToFront();
        contenedorLoading.setEnabled(false);

    }

    public void borraLoading(){

        Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.deaparece);

        viewLoading.startAnimation(anim);
        contenedorLoading.removeView(viewLoading);
        contenedorLoading.setEnabled(true);
        scrollPreview.setVisibility(View.VISIBLE);

        if(esActual(programaPreview)){

            btnCheckIn.setVisibility(View.VISIBLE);
            btnCheckIn_.setVisibility(View.VISIBLE);
            btnCheckIn_.setText("+ "+programaPreview.getCheckIn());
        }

    }
    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public void setPrograma(Program p){

        this.programaPreview = p;
    }

    public void noTws(){

       /*Resources res = getResources();
        Drawable d = res.getDrawable(R.drawable.no_tweets);
        ImageView imagen = new ImageView(getActivity());

        imagen.setPadding(5,90,0,0);
        imagen.setImageDrawable(d);*/

        scrollTw.setClickable(false);
        scrollTw.setEnabled(false);

        RelativeLayout relative1 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw);
        RelativeLayout relative2 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_2);
        RelativeLayout relative3 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_3);
        RelativeLayout relative4 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_4);
        RelativeLayout relative5 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_5);
        RelativeLayout relative6 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_6);
        RelativeLayout relative7 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_7);
        RelativeLayout relative8 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_8);
        RelativeLayout relative9 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_9);
        RelativeLayout relative10 = (RelativeLayout) rootView.findViewById(R.id.preview_contenedor_tw_10);

        RelativeLayout noTweet = (RelativeLayout) rootView.findViewById(R.id.container_no_tweets);
        noTweet.setVisibility(View.VISIBLE);

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

    }

    public void cargarTws(){

        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

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


        texto.setTypeface(normal);
        usuario.setTypeface(bold);
        nombre.setTypeface(bold);

        texto2.setTypeface(normal);
        usuario2.setTypeface(bold);
        nombre2.setTypeface(bold);

        texto3.setTypeface(normal);
        usuario3.setTypeface(bold);
        nombre3.setTypeface(bold);

        texto4.setTypeface(normal);
        usuario4.setTypeface(bold);
        nombre4.setTypeface(bold);

        texto5.setTypeface(normal);
        usuario5.setTypeface(bold);
        nombre5.setTypeface(bold);

        texto6.setTypeface(normal);
        usuario6.setTypeface(bold);
        nombre6.setTypeface(bold);

        texto7.setTypeface(normal);
        usuario7.setTypeface(bold);
        nombre7.setTypeface(bold);

        texto8.setTypeface(normal);
        usuario8.setTypeface(bold);
        nombre8.setTypeface(bold);

        texto9.setTypeface(normal);
        usuario9.setTypeface(bold);
        nombre9.setTypeface(bold);

        texto10.setTypeface(normal);
        usuario10.setTypeface(bold);
        nombre10.setTypeface(bold);

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

            nombre8.setText(programaPreview.getTweets().get(7).getNombre()+"  @"+(programaPreview.getTweets().get(7)
                    .getNombreUsuario()));

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
            nombre9.setText(programaPreview.getTweets().get(8).getNombre()+"  @"+(programaPreview.getTweets().get(8)
                    .getNombreUsuario()));

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

            nombre10.setText(programaPreview.getTweets().get(9).getNombre()+"  @"+(programaPreview.getTweets().get(9)
                    .getNombreUsuario()));

            if(programaPreview.getTweets().get(9).getUrlImagen() != null){
                aq.id(R.id.foto_tw_10).image(programaPreview.getTweets().get(9).getUrlImagen());

            }

        }

    }

    public void recorreTws(){

        final int contador = programaPreview.getTweets().size();
        final int max = (contador-2) * 85;

        Log.e("Contador",""+contador);
        Log.e("max",""+max);

        TimerTask timerTask = new TimerTask()
        {
            public void run()
            {

                int posicion =  scrollTw.getScrollY();

                //Log.e("posicion",""+posicion);
                if(posicion == max || posicion > max){
                    scrollTw.smoothScrollTo(0, 10);
                }
                else{
                    scrollTw.smoothScrollBy(0, 85);
                }
            }
        };

        // Aquí se pone en marcha el timer cada segundo.
        Timer timer = new Timer();
        // Dentro de 0 milisegundos avísame cada 1000 milisegundos
        timer.scheduleAtFixedRate(timerTask, 0, 8000);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void createReminder(Program p){

        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm");

        Log.e("Recordar","Recordar "+ p.getTitle());

        SharedPreferences prefs = getActivity().getSharedPreferences("com.nunchee", Context.MODE_PRIVATE);

        if(prefs.getBoolean("reminder_" +p.getIdProgram()+p.getEndDate(), false)) {
            Toast.makeText(getActivity(), "El recordatorio ya ha sido creado", Toast.LENGTH_SHORT).show();
            return;
        }

        int  id_calendars[] = getCalendar(getActivity());
        long calID = id_calendars[0];

        long startMillis;
        long endMillis;

        //Calendar cal = Calendar.getInstance();

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(p.getStartDate());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(p.getEndDate());
        endMillis = endTime.getTimeInMillis();

        TimeZone timeZone = TimeZone.getDefault();

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, p.getTitle());

        if(p.getPChannel().getChannelCallLetter()!= null)
            values.put(CalendarContract.Events.DESCRIPTION,p.getPChannel().getChannelCallLetter()+" "+
                    horaFormat.format(p.getStartDate())+" | "+ horaFormat.format(p.getEndDate())+" "+p.getEpisodeTitle());
        else
            values.put(CalendarContract.Events.DESCRIPTION,p.getPChannel().getChannelName()+" "+
                    horaFormat.format(p.getStartDate())+" | "+ horaFormat.format(p.getEndDate())+" "+p.getEpisodeTitle());

        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Reminders.MINUTES, 3);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.ALL_DAY,0);


        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Log.e("Uri",""+CalendarContract.Events.CONTENT_URI);

        long eventID = Long.parseLong(uri != null ? uri.getLastPathSegment() : null);

        Log.e("EventID",""+eventID);
        Log.e("Uri Reminder",""+CalendarContract.Reminders.CONTENT_URI);

        ContentValues reminderValues = new ContentValues();

        reminderValues.put(CalendarContract.Reminders.MINUTES, 3);
        reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        //Uri _reminder = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues);

        prefs.edit().putBoolean("reminder_" + p.getIdProgram() + p.getEndDate(), true).commit();

        Toast t = Toast.makeText(getActivity(),p.getTitle()+" agregado a tus recordatorios",Toast.LENGTH_LONG);
        t.show();
    }

    public int [] getCalendar(Context c) {

        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = c.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

        int aux[] = new int[0];

        if (managedCursor.moveToFirst()){

            aux = new int[managedCursor.getCount()];
            int cont= 0;
            //int nameCol = managedCursor.getColumnIndex(projection[1]);
            //int idCol = managedCursor.getColumnIndex(projection[0]);
            do {
                aux[cont] = managedCursor.getInt(0);
                cont++;
            } while(managedCursor.moveToNext());

            managedCursor.close();
        }
        return aux;
    }
    private void publishStory() {
        Session session = Session.getActiveSession();

        if (session != null){

            // Check for publish permissions
            Log.e("Session","No null");
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }
            SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd' 'HH'$'mm'$'ss");

            String url = "http://www.movistar.cl/PortalMovistarWeb/tv-digital/guia-de-canales";

            String imagen;

            if(programaPreview.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE)!= null){

                imagen = programaPreview.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE).getImagePath();
            }
            else{
                imagen= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
            }

            Bundle postParams = new Bundle();
            postParams.putString("name", programaPreview.getTitle());
            postParams.putString("caption", "Movistar TV");
            postParams.putString("description", programaPreview.getDescription());
            postParams.putString("link", url);
            postParams.putString("message", "Me gusta "+programaPreview.getTitle());
            postParams.putString("picture", imagen);

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {

                    String postId = null;
                    if(response != null){

                        GraphObject graphObject = response.getGraphObject();
                        if(graphObject != null){

                            JSONObject graphResponse = response
                                    .getGraphObject()
                                    .getInnerJSONObject();

                            try {
                                postId = graphResponse.getString("id");
                            } catch (JSONException e) {
                                Log.i("TAG",
                                        "JSON error "+ e.getMessage());
                            }
                        }
                        FacebookRequestError error = response.getError();
                        if (error != null) {
                            Toast.makeText(getActivity()
                                    .getApplicationContext(),
                                    error.getErrorMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity()
                                    .getApplicationContext(),
                                    postId,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }
            };

            Request request = new Request(session, "me/feed", postParams,
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }
        else{
            Log.e("Session","Null");
        }

    }

    private void publishCheckIn() {
        Session session = Session.getActiveSession();

        if (session != null){

            // Check for publish permissions
            Log.e("Session","No null");
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }
            SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd' 'HH'$'mm'$'ss");

            String url = "http://www.movistar.cl/PortalMovistarWeb/tv-digital/guia-de-canales";

            String imagen;

            if(programaPreview.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE)!= null){
                imagen = programaPreview.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE).getImagePath();
            }
            else{
                imagen= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
            }

            Bundle postParams = new Bundle();
            postParams.putString("name", programaPreview.getTitle());
            postParams.putString("caption", "Movistar TV");
            postParams.putString("description", programaPreview.getDescription());
            postParams.putString("link", url);
            postParams.putString("message", "Check in "+programaPreview.getTitle());
            postParams.putString("picture", imagen);

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {

                    String postId = null;
                    if(response != null){

                        GraphObject graphObject = response.getGraphObject();
                        if(graphObject != null){

                            JSONObject graphResponse = response
                                    .getGraphObject()
                                    .getInnerJSONObject();
                            try {
                                postId = graphResponse.getString("id");
                            } catch (JSONException e) {
                                Log.i("TAG",
                                        "JSON error "+ e.getMessage());
                            }
                        }
                        FacebookRequestError error = response.getError();
                        if (error != null) {
                            Toast.makeText(getActivity()
                                    .getApplicationContext(),
                                    error.getErrorMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity()
                                    .getApplicationContext(),
                                    postId,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }
            };

            Request request = new Request(session, "me/feed", postParams,
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }
        else{
            Log.e("Session","Null");
        }

    }
    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {

        if (pendingPublishReauthorization &&
                state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
            pendingPublishReauthorization = false;
            publishStory();
        }
    }


    private final Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

}
