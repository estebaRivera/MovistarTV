package com.smartboxtv.movistartv.activities;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.smartboxtv.movistartv.data.database.DataBase;

import com.smartboxtv.movistartv.data.database.DataBaseUser;
import com.smartboxtv.movistartv.data.database.UserNunchee;
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
import com.smartboxtv.movistartv.programation.menu.About;
import com.smartboxtv.movistartv.programation.menu.DialogError;
import com.smartboxtv.movistartv.programation.menu.NotificationFragment;
import com.smartboxtv.movistartv.programation.menu.Politica;
import com.smartboxtv.movistartv.programation.preview.ActionFragment;

import com.smartboxtv.movistartv.programation.preview.BarFragment;
import com.smartboxtv.movistartv.programation.preview.HeaderFragment;
import com.smartboxtv.movistartv.programation.preview.PollFragment;
import com.smartboxtv.movistartv.programation.preview.PollMaxFragment;
import com.smartboxtv.movistartv.programation.preview.PollMinFragment;
import com.smartboxtv.movistartv.programation.preview.Preview;
import com.smartboxtv.movistartv.programation.preview.TriviaFragment;
import com.smartboxtv.movistartv.programation.preview.TriviaMaxFragment;
import com.smartboxtv.movistartv.programation.preview.TriviaMinFragment;
import com.smartboxtv.movistartv.programation.preview.TwFragment;
import com.smartboxtv.movistartv.programation.preview.TwMaxFragment;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.services.ServiceManager;
import com.smartboxtv.movistartv.social.DialogShare;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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
 * Created by Esteban- on 23-05-14.
 */
public class PreviewActivity extends ActionBarActivity {

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
    private RelativeLayout btnCheckIn;

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
    private RelativeLayout contenedorActionbarOption;

    private Program programa;
    private Program programaPreview;

    private List<Tweets> twitts = new ArrayList<Tweets>();
    private AQuery aq;
    private String path;
    //private View rootView;
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

    private ImageView background;

    private boolean esTrivia = false;
    private boolean esEncuesta = false;
    private boolean esTw = false;

    private boolean isMessage = false;
    private boolean isNotification = false;
    private boolean isConfiguration = false;

    // Share Facebook
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;
    private UiLifecycleHelper uiHelper;

    //Facebook is Acrive
    private boolean fbActivate;
    private DataBaseUser dataBaseUser;
    private UserNunchee userNunchee;


    public PreviewActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_fragment);

        Bundle extra = this.getIntent().getExtras();

        programa = (Program) extra.get("programa");

        path =   extra.getString("background");
        Bitmap image = BitmapFactory.decodeFile(path);

        background = (ImageView) findViewById(R.id.background);
        background.setImageBitmap(image);

        scrollPreview = (ScrollView) findViewById(R.id.preview_scroll);
        scrollPreview.setVisibility(View.INVISIBLE);
        contenedorLoading = (RelativeLayout) findViewById(R.id.contenedor_loading);
        contenedorActionbarOption = (RelativeLayout) findViewById(R.id.contenedor_action_bar);

        loading();
        loadListener();
        DataClean.garbageCollector("Preview Fragment");

        // Contenedores
        contenedorEncuesta = (RelativeLayout) findViewById(R.id.preview_encuesta_contenedor);
        contenedorTrivia = (RelativeLayout) findViewById(R.id.preview_trivia_contenedor);
        contenedorTw = (RelativeLayout) findViewById(R.id.preview_tw_contenedor);
        contenedorHeader = (RelativeLayout) findViewById(R.id.preview_programa);
        contenedorSugeridos = (RelativeLayout) findViewById(R.id.preview_sugerencias);
        contenedorAccion = (LinearLayout) findViewById(R.id.preview_accion);
        contenedorAnimacion = (RelativeLayout) findViewById(R.id.preview_animado_contenedor);
        contenedorAnimacion.setVisibility(View.GONE);

        // Suplentes
        RelativeLayout encuestaSuplente = (RelativeLayout) findViewById(R.id.preview_animado_encuesta);
        RelativeLayout triviaSuplente = (RelativeLayout) findViewById(R.id.preview_animado_trivia);
        RelativeLayout twSuplente = (RelativeLayout) findViewById(R.id.preview_animado_tw);

        RelativeLayout s1 = (RelativeLayout) findViewById(R.id.s1);
        RelativeLayout s2 = (RelativeLayout) findViewById(R.id.s2);
        RelativeLayout s3 = (RelativeLayout) findViewById(R.id.s3);

        txtName = (TextView) findViewById(R.id.preview_nombre);
        txtDate = (TextView) findViewById(R.id.preview_hora);
        txtDescription = (TextView) findViewById(R.id.preview_descripcion);
        txtChannel = (TextView) findViewById(R.id.preview_nombre_canal);

        btnLike = (Button) findViewById(R.id.preview_boton_like);
        btnReminder = (Button) findViewById(R.id.preview_boton_recordar);
        btnFavorite = (Button) findViewById(R.id.preview_boton_favorito);
        btnShare =  (Button) findViewById(R.id.preview_boton_compartir);
        btnCheckIn_ = (Button) findViewById(R.id.preview_check_in_);
        btnCheckIn = (RelativeLayout) findViewById(R.id.preview_check_in);

        ImageView imgChannel = (ImageView) findViewById(R.id.preview_foto_canal);
        ImageView imgProgram = (ImageView) findViewById(R.id.preview_cabeza_foto);
        imgFavorite = (ImageView) findViewById(R.id.preview_image_favorito);
        imgFavorite.setVisibility(View.INVISIBLE);

        txtHashTags = (TextView) findViewById(R.id.preview_tw_canal);

        Resources res = getResources();

        Drawable drwFocusShare = res.getDrawable(R.drawable.compartir_foco_acc);
        Drawable drwFocusFavorite = res.getDrawable(R.drawable.favorito_foco_acc);
        Drawable drwFocusLike = res.getDrawable(R.drawable.like_foco_acc);
        Drawable drwFocusReminder = res.getDrawable(R.drawable.recordar_foco_acc);

        scrollTw = (ScrollView) findViewById(R.id.preview_scroll_tws);

        animation = AnimationUtils.loadAnimation(getApplication(), R.anim.agranda);
        animLeft = AnimationUtils.loadAnimation(getApplication(), R.anim.left_in);

        ImageView blockPreview = (ImageView) findViewById(R.id.background_2);
        blockPreview.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("<sdvzdf","dfblñfg");
                finish();
            }
        });

        // Publish Facebook
        dataBaseUser = new DataBaseUser(getApplicationContext(),"",null,0);
        userNunchee = dataBaseUser.select(UserPreference.getIdFacebook(getApplicationContext()));
        fbActivate = userNunchee.isFacebookActive;

        // Check In

        btnCheckIn.setVisibility(View.GONE);
        btnCheckIn_.setVisibility(View.GONE);


        // Sugerencias
        sugerencia1 =(TextView) findViewById(R.id.sugerencia_nombre1);
        sugerencia2 =(TextView) findViewById(R.id.sugerencia_nombre2);
        sugerencia3 =(TextView) findViewById(R.id.sugerencia_nombre3);

        r1 = (RelativeLayout) findViewById(R.id.s1);
        r2 = (RelativeLayout) findViewById(R.id.s2);
        r3 = (RelativeLayout) findViewById(R.id.s3);

        r1.setVisibility(View.INVISIBLE);
        r2.setVisibility(View.INVISIBLE);
        r3.setVisibility(View.INVISIBLE);

        aq = new AQuery(this);

        obtieneEncuesta();
        //cargarPreviewSM();
        cargarPreview();
        obtieneRecomendaciones();

        // Capturas de eventos de los contenedores para la animación
        contenedorTw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esTw = true;
                try {

                    Intent i = new Intent( PreviewActivity.this, SecondPreviewActivity.class);
                    i.putExtra("background", path);
                    i.putExtra("trivia", trivia);
                    i.putExtra("polls", polls);
                    i.putExtra("tweets", (Serializable) programaPreview.Tweets);
                    i.putExtra("programa", programa);
                    i.putExtra("programaPreview", programaPreview);
                    i.putExtra("click", Preview.TWEETS);
                    startActivity(i);
                    overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
                    //context.startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error","errorszdvzv --> "+e.getMessage());
                }

            }
        });

        contenedorEncuesta.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                esEncuesta = true;

                try {
                    Intent i = new Intent( PreviewActivity.this, SecondPreviewActivity.class);
                    i.putExtra("background", path);

                    i.putExtra("trivia", trivia);
                    i.putExtra("polls", polls);
                    i.putExtra("tweets", (Serializable) programaPreview.Tweets);
                    i.putExtra("programa", programa);
                    i.putExtra("programaPreview", programaPreview);
                    i.putExtra("click", Preview.POLLS);
                    startActivity(i);
                    overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
                    //context.startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        contenedorTrivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                esTrivia = true;

                try {

                    Intent i = new Intent( PreviewActivity.this, SecondPreviewActivity.class);
                    i.putExtra("background", path);

                    i.putExtra("trivia", trivia);
                    i.putExtra("polls", polls);
                    i.putExtra("tweets", (Serializable) programaPreview.Tweets);
                    i.putExtra("programa", programa);
                    i.putExtra("programaPreview", programaPreview);
                    i.putExtra("click", Preview.TRIVIA);
                    startActivity(i);
                    overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);

                } catch (Exception e) {
                    e.printStackTrace();
                }

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

                    RelativeLayout r = (RelativeLayout) findViewById(R.id.preview_animado_encuesta);
                    r.setVisibility(View.GONE);

                    RelativeLayout r1 = (RelativeLayout) findViewById(R.id.preview_animado_tw);

                    if (r1.getVisibility() == View.GONE) {
                        r1.setVisibility(View.VISIBLE);
                    }

                    RelativeLayout r2 = (RelativeLayout) findViewById(R.id.preview_animado_trivia);

                    if (r2.getVisibility() == View.GONE) {
                        r2.setVisibility(View.VISIBLE);
                    }

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

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

                    RelativeLayout r = (RelativeLayout) findViewById(R.id.preview_animado_trivia);
                    r.setVisibility(View.GONE);

                    RelativeLayout r1 = (RelativeLayout) findViewById(R.id.preview_animado_encuesta);

                    if (r1.getVisibility() == View.GONE) {
                        r1.setVisibility(View.VISIBLE);
                    }

                    RelativeLayout r2 = (RelativeLayout) findViewById(R.id.preview_animado_tw);

                    if (r2.getVisibility() == View.GONE) {
                        r2.setVisibility(View.VISIBLE);
                    }

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

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

                    RelativeLayout r = (RelativeLayout) findViewById(R.id.preview_animado_tw);
                    r.setVisibility(View.GONE);

                    RelativeLayout r1 = (RelativeLayout) findViewById(R.id.preview_animado_encuesta);
                    r1.setVisibility(View.VISIBLE);

                    RelativeLayout r2 = (RelativeLayout) findViewById(R.id.preview_animado_trivia);
                    r2.setVisibility(View.VISIBLE);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    ft.remove(fragmentoTw);
                    ft.remove(fragmentoBarra);

                    ft.commit();

                    ft = getSupportFragmentManager().beginTransaction();
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

                if(fbActivate){
                    DataLoader data = new DataLoader(getApplicationContext());
                    data.actionCheckIn(UserPreference.getIdNunchee(getApplicationContext()), "7", programaPreview.getIdProgram(),
                            programaPreview.getPChannel().getChannelID());

                    btnCheckIn.startAnimation(animation);
                    btnCheckIn_.setText("+ " + (programaPreview.getCheckIn() + 1));
                    btnCheckIn.setAlpha((float) 0.5);
                    btnCheckIn.setEnabled(false);
                    publishCheckIn();
                }
                else{
                    noPublish();
                }


            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(fbActivate){
                    DataLoader data = new DataLoader(getApplicationContext());
                    data.actionLike(UserPreference.getIdNunchee(getApplicationContext()), "2", programaPreview.getIdProgram(),
                            programaPreview.getPChannel().getChannelID());

                    btnLike.startAnimation(animation);
                    btnLike.setText((programaPreview.getLike() + 1) + " Likes");
                    btnLike.setEnabled(false);
                    btnLike.setAlpha((float) 0.5);
                    publishStory();
                }
                else{
                    noPublish();
                }

            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader data = new DataLoader(getApplicationContext());
                data.actionFavorite(UserPreference.getIdNunchee(getApplicationContext()), "4", programaPreview.getIdProgram(),
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

                if(fbActivate){
                    DataLoader data = new DataLoader(getApplication());
                    data.actionShare(UserPreference.getIdNunchee(getApplication()), "3", programaPreview.getIdProgram()
                            , programaPreview.getPChannel().getChannelID());

                    shareDialog();

                    btnShare.setEnabled(false);
                    btnShare.startAnimation(animation);
                    btnShare.setAlpha((float) 0.5);
                }
                else{
                    noPublish();
                }
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

        createActionBar();
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
    }

    public void noPublish(){
        Toast.makeText(this,"Activa el Autopost para poder publicar",Toast.LENGTH_LONG).show();
    }
    public void cargarPreview(){

        SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH$mm$ss");
        final SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat formatDia = new SimpleDateFormat("MMM dd");
        final Typeface normal = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/SegoeWP.ttf");

        if(programa != null){
            //Log.e("Programa", programa.getTitle());
            DataLoader dataLoader = new DataLoader(getApplication());
            dataLoader.getPreview(new DataLoader.DataLoadedHandler<Program>() {

                @Override
                public void loaded(final Program data) {

                    programaPreview = data;
                    // Inicialización de Fragmentos
                    Log.e("Programa", data.getTitle());
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
                    txtChannel.setText(programaPreview.getPChannel().getChannelCallLetter()+ " " +programaPreview.getPChannel()
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
                    //obtieneEncuesta();
                }

                @Override
                public void error(String error) {
                    super.error(error);
                    Log.e("Preview error 13 ", " error --> " + error);
                    borraLoading();
                    DialogError dialogError = new DialogError();
                    dialogError.show(getSupportFragmentManager(),"");
                }
            }, programa.getIdProgram(), programa.getPChannel().getChannelID(), UserPreference.getIdNunchee(getApplicationContext()),
                    dateFormat.format(programa.getStartDate()), dateFormat.format(programa.getEndDate()));
        }

    }
    public void cargarPreviewSM(){

        SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd HH$mm$ss");
        final SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm");
        final SimpleDateFormat formatDia = new SimpleDateFormat("MMM dd");
        final Typeface normal = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/SegoeWP.ttf");

        if(programa != null){
            //Log.e("Programa", programa.getTitle());
            ServiceManager serviceManager = new ServiceManager(getApplication());
            serviceManager.getPreview(new ServiceManager.ServiceManagerHandler<Program>(){
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
                    /*txtChannel.setText(programa.getPChannel().getChannelCallLetter()+ " " +programa.getPChannel()
                            .getChannelNumber());*/

                    txtChannel.setText(programa.getPChannel().getChannelCallLetter());
                    txtChannel.setTypeface(normal);

                    //Image image = data.urlImage;

                    if (data.urlImage != null) {
                        aq.id(R.id.preview_cabeza_foto).image(data.urlImage);
                        //Log.e("sii",data.urlImage);
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
                        String hastag = data.getHashtags().split(";")[0];
                        hastag = hastag.replace("[","");
                        hastag = hastag.replace("]","");
                        txtHashTags.setText(hastag);
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
                    //obtieneEncuesta();
                }

                @Override
                public void error(String error) {
                    super.error(error);
                    Log.e("Preview error 2 ", " error --> " + error);
                    borraLoading();
                    DialogError dialogError = new DialogError();
                    dialogError.show(getSupportFragmentManager(),"");
                }
            },"UserNunchee",programa.getIdProgram(),programa.IdEpisode ,programa.getPChannel().channelCallLetter,programa.getPChannel().channelImageURL,programa.StartDate,programa.getEndDate());

            /*DataLoader dataLoader = new DataLoader(getApplication());
            dataLoader.getPreview(new DataLoader.DataLoadedHandler<Program>() {*/

            /*}, programa.getIdProgram(), programa.getPChannel().getChannelID(), UserPreference.getIdNunchee(getApplicationContext()),
                    dateFormat.format(programa.getStartDate()), dateFormat.format(programa.getEndDate()));*/
        }

    }
    public void obtieneRecomendaciones(){

        if(programa != null){

            DataLoader dataLoader = new DataLoader(getApplicationContext());
            dataLoader.getRecomendaciones(new DataLoader.DataLoadedHandler<Recommendations>() {

                @Override
                public void loaded(final Recommendations data) {

                    if (data != null) {
                        Log.e("ObtieneRecomendacion", programa.getTitle());
                        //recomendaciones = data;


                        if (data.getSameCategoria().size() > 0) {

                            for (int i = 0; i < data.getSameCategoria().size(); i++) {
                                Image imagen = data.getSameCategoria().get(i).getImageWidthType(Width.ORIGINAL_IMAGE,
                                        Type.BACKDROP_IMAGE);

                                if (imagen != null && i == 0) {

                                    r1.setVisibility(View.VISIBLE);
                                    r1.startAnimation(animLeft);
                                    aq.id(R.id.sugerencia_imagen1).image(imagen.getImagePath());
                                    Log.e("1 Url Image R1 -->", imagen.getImagePath());
                                    sugerencia1.setText(data.getSameCategoria().get(i).getTitle());
                                }

                                if (imagen != null && i == 1) {

                                    if(r1.getVisibility() == View.VISIBLE){
                                        r2.setVisibility(View.VISIBLE);
                                        r2.startAnimation(animLeft);
                                        aq.id(R.id.sugerencia_imagen2).image(imagen.getImagePath());
                                        Log.e("2 Url Image R2 -->", imagen.getImagePath());
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
                                        Log.e("3 Url Image R3 -->",imagen.getImagePath());
                                        sugerencia3.setText(data.getSameCategoria().get(i).getTitle());
                                    }
                                    else{
                                        if(r1.getVisibility() == View.VISIBLE){
                                            r2.setVisibility(View.VISIBLE);
                                            r2.startAnimation(animLeft);
                                            aq.id(R.id.sugerencia_imagen2).image(imagen.getImagePath());
                                            Log.e("3 Url Image R2 -->",imagen.getImagePath());
                                            sugerencia2.setText(data.getSameCategoria().get(i).getTitle());
                                        }
                                        else{
                                            r1.setVisibility(View.VISIBLE);
                                            r1.startAnimation(animLeft);
                                            aq.id(R.id.sugerencia_imagen1).image(imagen.getImagePath());
                                            Log.e(" 3 Url Image R1 -->",imagen.getImagePath());
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
                    Log.e("Preview error 2 ", " Recomendaciones error --> " + error);
                }
            }, programa.getIdProgram(), programa.getPChannel().getChannelID(), UserPreference.getIdNunchee(getApplication()));
        }
    }

    public boolean esActual(Program p){

        Date ahora = new Date();
        if(!((p.getStartDate().getTime() > ahora.getTime())   ||  (p.getEndDate().getTime() < ahora.getTime()))){
            return true;
        }
        else
            return false;
    }

    private void obtieneTrivia(){

        if(programa != null){

            DataLoader dataLoader = new DataLoader(getApplication());
            dataLoader.getTrivia(new DataLoader.DataLoadedHandler<Trivia>() {

                @Override
                public void loaded(final Trivia data) {

                    if(data != null){

                        trivia = data;
                        fragmentoTriviaP = new TriviaMinFragment(trivia);
                        fragmentoTriviaMax = new TriviaMaxFragment(programaPreview,trivia, false);

                        /*if(trivia.getPreguntas().size()>0){

                            TriviaFragment fragmentoTrivia = new TriviaFragment(trivia);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                            ft.replace(R.id.preview_trivia_contenedor, fragmentoTrivia);
                            ft.commit();
                        }*/
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

            DataLoader dataLoader = new DataLoader(getApplication());
            dataLoader.getPolls(new DataLoader.DataLoadedHandler<Polls>() {
                @Override
                public void loaded(final Polls data) {

                    if (data != null) {
                        polls = data;
                        fragmentoEncuestaMax = new PollMaxFragment(polls,programa,programaPreview);
                        fragmentoEncuestaP = new PollMinFragment(polls);

                        /*if(polls.getPreguntas().size() > 0) {

                            PollFragment fragmentoEncuesta = new PollFragment(polls);
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.preview_encuesta_contenedor, fragmentoEncuesta);
                            ft.commit();
                        }*/
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

        inflaterPrivate = LayoutInflater.from(getApplicationContext());
        viewLoading = inflaterPrivate.inflate(R.layout.progress_dialog_pop_corn, null);
        ImageView imgPopCorn = (ImageView) viewLoading.findViewById(R.id.pop_corn_centro);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Animation animaPop = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animacion_pop_hacia_derecha_centro);

        viewLoading.setLayoutParams(params);
        imgPopCorn.startAnimation(animaPop != null ? animaPop : null);

        contenedorLoading.addView(viewLoading);
        viewLoading.bringToFront();

        Log.e("Loading","Loading");

        contenedorLoading.bringToFront();
        contenedorLoading.setEnabled(false);

    }

    public void borraLoading(){

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.deaparece);

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

    public void loadListener(){

        RelativeLayout relative1 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw);
        RelativeLayout relative2 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_2);
        RelativeLayout relative3 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_3);
        RelativeLayout relative4 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_4);
        RelativeLayout relative5 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_5);
        RelativeLayout relative6 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_6);
        RelativeLayout relative7 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_7);
        RelativeLayout relative8 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_8);
        RelativeLayout relative9 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_9);
        RelativeLayout relative10 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_10);

        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        relative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        relative5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        relative6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        relative7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        relative8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        relative9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
        relative10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextActivity();
            }
        });
    }

    public void nextActivity(){
        try {

            Intent i = new Intent( PreviewActivity.this, SecondPreviewActivity.class);
            i.putExtra("background", path);

            i.putExtra("trivia", trivia);
            i.putExtra("polls", polls);
            i.putExtra("tweets", (Serializable) programaPreview.Tweets);
            i.putExtra("programa", programa);
            i.putExtra("programaPreview", programaPreview);
            i.putExtra("click", Preview.TWEETS);
            startActivity(i);
            overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
            //context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error","errorszdvzv --> "+e.getMessage());
        }
    }

    public void noTws(){

        /*Resources res = getResources();
        Drawable d = res.getDrawable(R.drawable.no_tweets);
        ImageView imagen = new ImageView(getApplication());

        imagen.setPadding(5,20,0,0);
        imagen.setImageDrawable(d);*/

        scrollTw.setClickable(false);
        scrollTw.setEnabled(false);

        RelativeLayout relative1 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw);
        RelativeLayout relative2 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_2);
        RelativeLayout relative3 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_3);
        RelativeLayout relative4 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_4);
        RelativeLayout relative5 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_5);
        RelativeLayout relative6 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_6);
        RelativeLayout relative7 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_7);
        RelativeLayout relative8 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_8);
        RelativeLayout relative9 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_9);
        RelativeLayout relative10 = (RelativeLayout) findViewById(R.id.preview_contenedor_tw_10);

        RelativeLayout noTweet = (RelativeLayout) findViewById(R.id.container_no_tweets);
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

        Typeface normal = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Bold.ttf");

        TextView nombre = (TextView) findViewById(R.id.tw_nombre);
        TextView usuario = (TextView) findViewById(R.id.tw_nombre_usuario);
        TextView texto = (TextView) findViewById(R.id.tw_texto);

        TextView nombre2 = (TextView) findViewById(R.id.tw_nombre_2);
        TextView usuario2 = (TextView) findViewById(R.id.tw_nombre_usuario_2);
        TextView texto2 = (TextView) findViewById(R.id.tw_texto_2);

        TextView nombre3 = (TextView) findViewById(R.id.tw_nombre_3);
        TextView usuario3 = (TextView) findViewById(R.id.tw_nombre_usuario_3);
        TextView texto3 = (TextView) findViewById(R.id.tw_texto_3);

        TextView nombre4 = (TextView) findViewById(R.id.tw_nombre_4);
        TextView usuario4 = (TextView) findViewById(R.id.tw_nombre_usuario_4);
        TextView texto4 = (TextView) findViewById(R.id.tw_texto_4);

        TextView nombre5 = (TextView) findViewById(R.id.tw_nombre_5);
        TextView usuario5 = (TextView) findViewById(R.id.tw_nombre_usuario_5);
        TextView texto5 = (TextView) findViewById(R.id.tw_texto_5);

        TextView nombre6 = (TextView) findViewById(R.id.tw_nombre_6);
        TextView usuario6 = (TextView) findViewById(R.id.tw_nombre_usuario_6);
        TextView texto6 = (TextView) findViewById(R.id.tw_texto_6);

        TextView nombre7 = (TextView) findViewById(R.id.tw_nombre_7);
        TextView usuario7 = (TextView) findViewById(R.id.tw_nombre_usuario_7);
        TextView texto7 = (TextView) findViewById(R.id.tw_texto_7);


        TextView nombre8 = (TextView) findViewById(R.id.tw_nombre_8);
        TextView usuario8 = (TextView) findViewById(R.id.tw_nombre_usuario_8);
        TextView texto8 = (TextView) findViewById(R.id.tw_texto_8);

        TextView nombre9 = (TextView) findViewById(R.id.tw_nombre_9);
        TextView usuario9 = (TextView) findViewById(R.id.tw_nombre_usuario_9);
        TextView texto9 = (TextView) findViewById(R.id.tw_texto_9);


        TextView nombre10 = (TextView) findViewById(R.id.tw_nombre_10);
        TextView usuario10 = (TextView) findViewById(R.id.tw_nombre_usuario_10);
        TextView texto10 = (TextView) findViewById(R.id.tw_texto_10);


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
                //ImageView imageView = (ImageView) findViewById(R.id.foto_tw);
               // imageView.setImageBitmap(getRoundedCornerBitmap(getResources().getDrawable(R.drawable.twitter_placeholder), true));
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

                Log.e("posicion",""+posicion);
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

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("com.nunchee", Context.MODE_PRIVATE);

        if(prefs.getBoolean("reminder_" +p.getIdProgram()+p.getEndDate(), false)) {
            Toast.makeText(getApplicationContext(), "El recordatorio ya ha sido creado", Toast.LENGTH_SHORT).show();
            return;
        }


        int  id_calendars[] = getCalendar(getApplicationContext());
        if(id_calendars.length == 0){
            Toast.makeText(getApplicationContext(), "No es posible acceder a su calendario", Toast.LENGTH_SHORT).show();
            return;
        }
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

        ContentResolver cr = getApplication().getContentResolver();
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

        //values.put(CalendarContract.Reminders.MINUTES, 3);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.ALL_DAY,0);


        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Log.e("Uri",""+CalendarContract.Events.CONTENT_URI);

        long eventID = Long.parseLong(uri != null ? uri.getLastPathSegment() : null);

        Log.e("EventID",""+eventID);
        Log.e("Uri Reminder",""+CalendarContract.Reminders.CONTENT_URI);

        ContentValues reminderValues = new ContentValues();

        //reminderValues.put(CalendarContract.Reminders.MINUTES, 3);
        reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        //Uri _reminder = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues);

        prefs.edit().putBoolean("reminder_" + p.getIdProgram() + p.getEndDate(), true).commit();

        DataBase dataBase = new DataBase(this,"",null,1);
        SQLiteDatabase db = dataBase.getWritableDatabase();

        //dataBase.onUpgrade(db,0,0);

        if(db != null){

            String codigo = p.IdProgram;
            String nombre = p.Title;
            String fecha = ""+p.StartDate.getTime();
            String endDate =""+p.EndDate.getTime();
            String codigoChannel = p.PChannel.channelID;
            Image image = p.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
            String urlImage = null;
            if(image != null)
                urlImage = image.getImagePath();

            Log.e("Nombre",p.Title);
            //Insertamos los datos en la tabla reminder
            String query = "INSERT INTO reminder (id,begin_date,end_date,name,channel, image) " + "VALUES ('" + codigo + "',' "+fecha+"', '"
                    + endDate+"', '" + nombre +"', '" + codigoChannel +"','"+urlImage+"')";
            Log.e("Query",query);
            db.execSQL(query);
        }
        db.close();

        Toast t = Toast.makeText(getApplicationContext(),p.getTitle()+" agregado a tus recordatorios",Toast.LENGTH_LONG);
        t.show();
    }

    public int[] getCalendar(Context c) {

        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = c.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

        int aux[] = new int[0];

        if (managedCursor.moveToFirst()) {

            aux = new int[managedCursor.getCount()];

            int cont = 0;
            do {
                aux[cont] = managedCursor.getInt(0);
                cont++;
            } while (managedCursor.moveToNext());

            managedCursor.close();
        }
        return aux;

    }

    public  void shareDialog(){

        SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd' 'HH'$'mm'$'ss");

        String url = "http://nunchee.tv/program.html?program="+programa.getIdProgram()+"&channel="+programa
                .getPChannel().getChannelID()+"&user="+UserPreference.getIdNunchee(getApplication())+"&action=2&startdate="
                +hora.format(programa.getStartDate())+"&enddate="+hora.format(programa.getEndDate());

        Image imagen = programaPreview.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
        String imageUrl;

        if( imagen != null){
            imageUrl = imagen.getImagePath();
        }
        else{
            imageUrl= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
        }
        String title = programa.Title;
        String description;
        if(programaPreview.Description != null)
            description = programaPreview.Description;
        else
            description = "";

        DialogShare dialogShare = new DialogShare(description,imageUrl,title,url);
        dialogShare.show(getSupportFragmentManager(),"");
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

            String url = "http://nunchee.tv/program.html?program="+programa.getIdProgram()+"&channel="+programa
                    .getPChannel().getChannelID()+"&user="+UserPreference.getIdNunchee(getApplication())+"&action=2&startdate="
                    +hora.format(programa.getStartDate())+"&enddate="+hora.format(programa.getEndDate());

            Image imagen = programaPreview.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
            String urlImage;

            if( imagen != null){

                urlImage = imagen.getImagePath();
            }
            else{
                urlImage= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
            }

            String description = " ";

            if(programaPreview.getDescription()!= null){
                description = programaPreview.getDescription();
            }

            Log.e("URL", urlImage);
            Log.e("name", programa.getTitle());
            Log.e("description", description);

            Bundle postParams = new Bundle();
            postParams.putString("name", programa.getTitle());
            postParams.putString("caption", "Movistar TV");
            postParams.putString("description", description);
            postParams.putString("link", url);
            postParams.putString("message", "Me gusta "+programa.getTitle());
            postParams.putString("picture", urlImage);

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
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Ups, algo salió mal, intenta de nuevo",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Publicado correctamente",
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

            String url = "http://nunchee.tv/program.html?program="+programa.getIdProgram()+"&channel="+programa
                    .getPChannel().getChannelID()+"&user="+UserPreference.getIdNunchee(getApplication())+"&action=2&startdate="
                    +hora.format(programa.getStartDate())+"&enddate="+hora.format(programa.getEndDate());

            Image imagen = programaPreview.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
            String urlImage;

            if( imagen != null){

                urlImage = imagen.getImagePath();
            }
            else{
                urlImage= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
            }

            String description = " ";

            if(programaPreview.getDescription()!= null){
                description = programaPreview.getDescription();
            }

            Log.e("URL", urlImage);
            Log.e("name", programa.getTitle());
            Log.e("description", description);

            Bundle postParams = new Bundle();
            postParams.putString("name", programa.getTitle());
            postParams.putString("caption", "Movistar TV");
            postParams.putString("description", description);
            postParams.putString("link", url);
            postParams.putString("message", "Estoy viendo "+programa.getTitle());
            postParams.putString("picture", urlImage);

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
                            Toast.makeText(getApplicationContext(),
                                    error.getErrorMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),
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

    @Override
    public void onBackPressed() {
        background.setBackgroundColor(Color.TRANSPARENT);
        super.onBackPressed();
    }

    private void createActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        final LayoutInflater inflater = LayoutInflater.from(this);

        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);

        View view = inflater.inflate(R.layout.action_bar_preview, null);

        fbActivate = UserPreference.isFacebookActive(getApplication());

        ImageButton back = (ImageButton) view.findViewById(R.id.item_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageButton configuracion = (ImageButton) view.findViewById(R.id.item_configuracion);

        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConfiguration) {

                    isConfiguration = false;
                    isMessage = false;
                    isNotification = false;
                    //contenedorLoading.removeAllViews();
                    contenedorActionbarOption.removeAllViews();
                    //contenedorMenuBar.removeAllViews();
                    /*RelativeLayout r = (RelativeLayout) findViewById(R.id.contenedor_action_bar);
                    r.removeAllViews();*/
                } else {

                    isConfiguration = true;
                    isMessage = false;
                    isNotification = false;

                    //contenedorMenuBar.removeAllViews();
                    View containerConfiguration = inflater.inflate(R.layout.action_bar_configuration, null, false);
                    Typeface light = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP.ttf");
                    Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Bold.ttf");

                    RelativeLayout r1 = (RelativeLayout) containerConfiguration.findViewById(R.id.config_auto_post);
                    RelativeLayout r2 = (RelativeLayout) containerConfiguration.findViewById(R.id.config_tutorial);
                    RelativeLayout r3 = (RelativeLayout) containerConfiguration.findViewById(R.id.config_acerca_de);
                    RelativeLayout r4 = (RelativeLayout) containerConfiguration.findViewById(R.id.confif_terminos_y_condiciones);

                    TextView txtAutoPost = (TextView) containerConfiguration.findViewById(R.id.auto_post_principal);
                    TextView txtTutorial = (TextView) containerConfiguration.findViewById(R.id.tutorial_principal);
                    TextView txtAcercaDe = (TextView) containerConfiguration.findViewById(R.id.acerca_de_principal);
                    TextView txtTerminos = (TextView) containerConfiguration.findViewById(R.id.termino_principal);

                    final TextView txtAutoPost2 = (TextView) containerConfiguration.findViewById(R.id.auto_post_secundario);
                    TextView txtTutorial2 = (TextView) containerConfiguration.findViewById(R.id.tutorial_secundario);
                    TextView txtAcercaDe2 = (TextView) containerConfiguration.findViewById(R.id.acerca_de_secundario);
                    TextView txtTerminos2 = (TextView) containerConfiguration.findViewById(R.id.termino_secundario);

                    txtAcercaDe.setTypeface(bold);
                    txtAutoPost.setTypeface(bold);
                    txtTerminos.setTypeface(bold);
                    txtTutorial.setTypeface(bold);

                    txtAcercaDe2.setTypeface(light);
                    txtAutoPost2.setTypeface(light);
                    txtTerminos2.setTypeface(light);
                    txtTutorial2.setTypeface(light);

                    final ImageView fb = (ImageView) r1.findViewById(R.id.fb_active);
                    //final Drawable noFb = getResources().getDrawable(R.drawable.fb_logo_blue_50);
                    //final Drawable siFb = getResources().getDrawable(R.drawable.fb_logo_blue_50_active);

                    ImageView exit = (ImageView) containerConfiguration.findViewById(R.id.exit);

                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isConfiguration = false;
                            isMessage = false;
                            isNotification = false;
                            contenedorActionbarOption.removeAllViews();
                        }
                    });

                    r1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(fbActivate){
                                fbActivate = false;
                                txtAutoPost2.setText("Desactiva tu post en Facebook");
                                //fb.setImageDrawable(siFb);
                                fb.setAlpha((float)1);
                                UserPreference.setFacebookActive(true,getApplication());

                            }
                            else{
                                fbActivate = true;
                                txtAutoPost2.setText("Activa tu post en Facebook");
                                //fb.setImageDrawable(noFb);
                                fb.setAlpha((float)0.3);
                                UserPreference.setFacebookActive(false,getApplication());
                            }

                        }
                    });
                    r2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    r3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            contenedorLoading.removeAllViews();
                            contenedorActionbarOption.removeAllViews();
                           // contenedorMenuBar.removeAllViews();
                            isConfiguration = false;
                            isMessage = false;
                            isNotification = false;

                            About fg = new About();
                            fg.show(getSupportFragmentManager(),"");
                        }
                    });

                    r4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            contenedorLoading.removeAllViews();
                            contenedorActionbarOption.removeAllViews();
                            //contenedorMenuBar.removeAllViews();
                            isConfiguration = false;
                            isMessage = false;
                            isNotification = false;

                            //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                            Politica fg = new Politica();

                            /*ft.addToBackStack(null);
                            ft.replace(R.id.contenedor_action_bar, fg);
                            ft.commit();*/

                            fg.show(getSupportFragmentManager(),"");
                        }
                    });

                    contenedorActionbarOption.removeAllViews();
                    contenedorActionbarOption.addView(containerConfiguration);

                    /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    ConfigurationFragment dialogError = new ConfigurationFragment();
                    dialogError.setConfigurationDelegate(configurationDelegate);
                    ft.replace(R.id.contenedor_preview, dialogError);
                    ft.commit();*/

                }
            }
        });
        //ImageView imageProfile = (ImageView) view.findViewById(R.id.foto_perfil_actionbar);
        ImageButton mensajes = (ImageButton) view.findViewById(R.id.item_mensajes);
        mensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMessage) {

                    isConfiguration = false;
                    isNotification = false;
                    isMessage = false;
                    contenedorActionbarOption.removeAllViews();
                    //contenedorMenuBar.removeAllViews();
                    /*RelativeLayout r = (RelativeLayout) findViewById(R.id.contenedor_preview);
                    r.removeAllViews();*/
                } else {

                    isConfiguration = false;
                    isNotification = false;
                    isMessage = true;

                    View containerMessage = inflater.inflate(R.layout.action_bar_message, null, false);
                    contenedorActionbarOption.removeAllViews();
                   // contenedorMenuBar.removeAllViews();
                    contenedorActionbarOption.addView(containerMessage);
                    /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    MessageFragment dialogError = new MessageFragment();
                    ft.replace(R.id.contenedor_preview, dialogError);
                    ft.commit();*/
                }
            }
        });

        ImageButton notificacion = (ImageButton) view.findViewById(R.id.item_notificaciones);
        notificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNotification) {

                    isConfiguration = false;
                    isMessage = false;
                    isNotification = false;
                    contenedorActionbarOption.removeAllViews();
                    //contenedorMenuBar.removeAllViews();
                    /*RelativeLayout r = (RelativeLayout) findViewById(R.id.contenedor_preview);
                    r.removeAllViews();*/
                } else {

                    isNotification = true;
                    isConfiguration = false;
                    isMessage = false;

                    //View containerNotification = inflater.inflate(R.layout.action_bar_notification, null, false);
                    //contenedorActionbarOption.removeAllViews();
                    //ontenedorActionbarOption.addView(containerNotification);

                    contenedorActionbarOption.removeAllViews();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    NotificationFragment dialogError = new NotificationFragment();
                    ft.replace(R.id.contenedor_action_bar, dialogError);
                    ft.commit();
                }
            }
        });

        AQuery aq = new AQuery(view);
       /* aq.id(imageProfile).image("http://graph.facebook.com/" + UserPreference.getIdFacebook(PreviewActivity.this)
                + "/picture?type=square");*/

        actionBar.setCustomView(view,layout);

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
