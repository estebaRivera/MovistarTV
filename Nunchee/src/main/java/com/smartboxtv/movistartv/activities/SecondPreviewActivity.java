package com.smartboxtv.movistartv.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.clean.DataClean;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Polls;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.Trivia;
import com.smartboxtv.movistartv.data.models.Tweets;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.delgates.SeconPreviewDelegate;
import com.smartboxtv.movistartv.programation.delegates.PreviewImageFavoriteDelegate;
import com.smartboxtv.movistartv.programation.menu.About;
import com.smartboxtv.movistartv.programation.menu.NotificationFragment;
import com.smartboxtv.movistartv.programation.menu.Politica;
import com.smartboxtv.movistartv.programation.preview.ActionFragment;
import com.smartboxtv.movistartv.programation.preview.BarFragment;
import com.smartboxtv.movistartv.programation.preview.HeaderFragment;
import com.smartboxtv.movistartv.programation.preview.PollMaxFragment;
import com.smartboxtv.movistartv.programation.preview.PollMinFragment;
import com.smartboxtv.movistartv.programation.preview.Preview;
import com.smartboxtv.movistartv.programation.preview.TriviaMaxFragment;
import com.smartboxtv.movistartv.programation.preview.TriviaMinFragment;
import com.smartboxtv.movistartv.programation.preview.TwFragment;
import com.smartboxtv.movistartv.programation.preview.TwMaxFragment;
import com.smartboxtv.movistartv.social.DialogShare;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 26-05-14.
 */
public class SecondPreviewActivity extends ActionBarActivity {

    private RelativeLayout contenedorLoading;
    private RelativeLayout contenedorActionbarOption;

    private RelativeLayout wrapperTrivia;
    private RelativeLayout wrapperPolls;
    private RelativeLayout wrapperTws;
    private RelativeLayout wrapperMax;

    private RelativeLayout cuadrante1;
    private RelativeLayout cuadrante2;
    private RelativeLayout cuadrante3;
    private RelativeLayout cuadrante4;

    private Button btnFavorite;
    private Button btnLike;
    private Button btnReminder;
    private Button btnShare;

    private List<Tweets> listTweets = new ArrayList<Tweets>();
    private Trivia trivia = new Trivia();
    private Polls polls = new Polls();

    //Fragmentos
    private PollMaxFragment fgPolls;
    private TriviaMaxFragment fragmentTriviaMax;
    private TwMaxFragment fragmentTwMax;

    private PollMinFragment fragmentoEncuestaP;
    private TriviaMinFragment fragmentoTriviaP;
    private BarFragment fragmentoBarra;
    private TwFragment fragmentoTw;
    private HeaderFragment fragmentoHeader;
    private ActionFragment fragmentoAccion;

    private Program programa;
    private Program programaPreview;

    private boolean esTrivia = false;
    private boolean esEncuesta = false;
    private boolean esTw = false;
    private boolean facebookActive;

    private boolean isMessage = false;
    private boolean isNotification = false;
    private boolean isConfiguration = false;
    private boolean fbActivate = true;
    private int option;

    public float x;
    public float y;
    public float width;
    public float height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_preview);
        DataClean.garbageCollector("Seconds Preview");

        contenedorLoading = (RelativeLayout) findViewById(R.id.container_loading);
        contenedorActionbarOption = (RelativeLayout) findViewById(R.id.container_action_bar);

        wrapperTrivia = (RelativeLayout) findViewById(R.id.wrapper__trivia_min);
        wrapperPolls = (RelativeLayout) findViewById(R.id.wrapper_polls_min);
        wrapperTws = (RelativeLayout) findViewById(R.id.wrapper_tw_min);
        wrapperMax = (RelativeLayout) findViewById(R.id.container_seconds);

        cuadrante1 = (RelativeLayout) findViewById(R.id.cuadrante1);
        cuadrante2 = (RelativeLayout) findViewById(R.id.cuadrante2);
        cuadrante3 = (RelativeLayout) findViewById(R.id.cuadrante3);
        cuadrante4 = (RelativeLayout) findViewById(R.id.cuadrante4);

        loadExtra();
        loadListener();
        loadFragment();
        createActionBar();

        wrapperTws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToastMakeText("Tw");
                //transformerTranslation1(wrapperTws,wrapperMax);C
                option = Preview.TWEETS;
                loadFragment();
            }
        });
        wrapperPolls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToastMakeText("Polls");
                //if(wrapperTrivia.getVisibility() != View.GONE)
                  //  transformerTranslation1(wrapperPolls, wrapperMax);
                //else
                    //transformerTranslation2(wrapperPolls,wrapperMax);
                option = Preview.POLLS;
                loadFragment();
            }
        });
        wrapperTrivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToastMakeText("Trivia");
                option = Preview.TRIVIA;
                wrapperMax.bringToFront();
                //transformerTranslation2(wrapperTrivia, wrapperMax);
                loadFragment();
            }
        });

        PreviewImageFavoriteDelegate  imageFavoriteDelegate = new PreviewImageFavoriteDelegate() {
            @Override
            public void showImage(boolean mostrarImagen, ActionFragment fg) {
                fragmentoHeader.muestraImagen(mostrarImagen);
            }
        };
        fragmentoAccion.setImageFavoriteDelegate(imageFavoriteDelegate);
    }

    private void  loadListener(){

    }
    private void loadExtra(){

        ImageView back = (ImageView) findViewById(R.id.background);
        Bundle extra = this.getIntent().getExtras();

        trivia = (Trivia) extra.get("trivia");
        polls =  (Polls) extra.get("polls");
        listTweets = (List<Tweets>) extra.get("tweets");
        programa = (Program) extra.get("programa");
        programaPreview = (Program) extra.get("programaPreview");
        String path = extra.getString("background");

        Bitmap bm = BitmapFactory.decodeFile(path);
        back.setImageBitmap(bm);

        option = extra.getInt("click");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public void loadFragment(){

        // Inicio de fragmentos
        fragmentoAccion = new ActionFragment(programaPreview,programa); // ver bien
        fragmentoHeader = new HeaderFragment(programaPreview,programa);
        fragmentoBarra = new BarFragment();
        fragmentoEncuestaP = new PollMinFragment(polls);
        fragmentoTriviaP = new TriviaMinFragment(trivia);
        fragmentoTw = new TwFragment(programaPreview);

        SeconPreviewDelegate delegate = new SeconPreviewDelegate() {
            @Override
            public void loadFragments() {
                option = Preview.TWEETS;
                loadFragment();
            }
        };
        fragmentoTw.setDelegate(delegate);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);

        ft.replace(R.id.wrapper_header, fragmentoHeader);
        ft.replace(R.id.wrapper_action, fragmentoAccion);
        ft.replace(R.id.wrapper_bar, fragmentoBarra);

        switch (option){

            case Preview.TWEETS:

                                    fragmentTwMax = new TwMaxFragment(programaPreview);
                                    wrapperTws.setVisibility(View.GONE);
                                    wrapperPolls.setVisibility(View.VISIBLE);
                                    wrapperTrivia.setVisibility(View.VISIBLE);
                                    ft.replace(R.id.wrapper_tweets, fragmentTwMax);
                                    ft.replace(R.id.wrapper__trivia_min, fragmentoTriviaP);
                                    ft.replace(R.id.wrapper_polls_min, fragmentoEncuestaP);
                                    //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                    ft.commit();
                                    break;

            case Preview.POLLS:

                                    fgPolls = new PollMaxFragment(polls,programa, programaPreview);
                                    wrapperTws.setVisibility(View.VISIBLE);
                                    wrapperPolls.setVisibility(View.GONE);
                                    wrapperTrivia.setVisibility(View.VISIBLE);
                                    ft.replace(R.id.wrapper_tweets, fgPolls);
                                    ft.replace(R.id.wrapper__trivia_min, fragmentoTriviaP);
                                    ft.replace(R.id.wrapper_tw_min, fragmentoTw);
                                    //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                    ft.commit();
                                    break;

            case Preview.TRIVIA:
                                    fragmentTriviaMax = new TriviaMaxFragment(programaPreview,trivia,false);
                                    wrapperTws.setVisibility(View.VISIBLE);
                                    wrapperPolls.setVisibility(View.VISIBLE);
                                    wrapperTrivia.setVisibility(View.GONE);
                                    ft.replace(R.id.wrapper_tweets, fragmentTriviaMax);
                                    ft.replace(R.id.wrapper_polls_min, fragmentoEncuestaP);
                                    ft.replace(R.id.wrapper_tw_min, fragmentoTw);
                                    //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                    ft.commit();
                                    break;
        }
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

                    contenedorActionbarOption.removeAllViews();

                } else {

                    isConfiguration = true;
                    isMessage = false;
                    isNotification = false;

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

                                fb.setAlpha((float)1);
                                UserPreference.setFacebookActive(true,getApplication());

                            }
                            else{
                                fbActivate = true;
                                txtAutoPost2.setText("Activa tu post en Facebook");

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

                            isConfiguration = false;
                            isMessage = false;
                            isNotification = false;

                            Politica fg = new Politica();
                            fg.show(getSupportFragmentManager(),"");
                        }
                    });

                    contenedorActionbarOption.removeAllViews();
                    contenedorActionbarOption.addView(containerConfiguration);

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

                } else {

                    isConfiguration = false;
                    isNotification = false;
                    isMessage = true;

                    View containerMessage = inflater.inflate(R.layout.action_bar_message, null, false);
                    contenedorActionbarOption.removeAllViews();
                    contenedorActionbarOption.addView(containerMessage);

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

                } else {

                    isNotification = true;
                    isConfiguration = false;
                    isMessage = false;

                    contenedorActionbarOption.removeAllViews();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    NotificationFragment dialogError = new NotificationFragment();
                    ft.replace(R.id.container_action_bar, dialogError);
                    ft.commit();
                }
            }
        });

        AQuery aq = new AQuery(view);
        /*aq.id(imageProfile).image("http://graph.facebook.com/" + UserPreference.getIdFacebook(SecondPreviewActivity.this)
                + "/picture?type=square");*/

        actionBar.setCustomView(view,layout);
    }
    public  void shareDialog(){

        SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd' 'HH'$'mm'$'ss");

        String url = "http://nunchee.tv/program.html?program="+programa.getIdProgram()+"&channel="+programa
                .getPChannel().getChannelID()+"&user="+UserPreference.getIdNunchee(getApplication())+"&action=2&startdate="
                +hora.format(programa.getStartDate())+"&enddate="+hora.format(programa.getEndDate());

        Image imagen = programaPreview.getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE);
        String imageUrl;

        if( imagen != null){
            imageUrl = imagen.getImagePath();
        }
        else{
            imageUrl= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
        }
        String title = programa.Title;
        String text;
        if(programaPreview.Description != null)
            text = programaPreview.Description;
        else
            text = "";

        DialogShare dialogShare = new DialogShare(text,imageUrl,title,url);
        dialogShare.show(getSupportFragmentManager(),"");
    }
    public void transformerTranslation1(View quieto, View mueve){

        quieto.setPivotX(0);
        quieto.setPivotY(0);

        float x1 = quieto.getX();
        float y1 = quieto.getY();

        x = mueve.getX();
        y = mueve.getY();

        width = mueve.getMeasuredWidth();
        height = mueve.getMeasuredHeight();



        float anchoQuieto = quieto.getMeasuredWidth();
        float largoQuieto = quieto.getMeasuredHeight();

        float anchoMueve = mueve.getMeasuredWidth();
        float largoMueve = mueve.getMeasuredHeight();

        ObjectAnimator animTranslationX = ObjectAnimator.ofFloat(mueve, View.TRANSLATION_X, -(657)); // 657;
        ObjectAnimator animTranslationY = ObjectAnimator.ofFloat(mueve, View.TRANSLATION_Y, 164); // 164

        ObjectAnimator animTransformerWidth = ObjectAnimator.ofFloat(mueve, View.SCALE_X, porcentaje(anchoMueve,anchoQuieto));
        ObjectAnimator animTranslationHeight = ObjectAnimator.ofFloat(mueve, View.SCALE_Y, porcentaje(largoMueve, largoQuieto));
        ObjectAnimator animCuadrante1Alpha = ObjectAnimator.ofFloat(cuadrante1, View.ALPHA,5);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1500);
        animatorSet.playTogether(animTranslationX, animTranslationY, animTransformerWidth, animTranslationHeight,animCuadrante1Alpha);
        //animatorSet.playTogether(animCuadrante1Alpha);
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                loadFragment();
                back(wrapperMax);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    public void transformerTranslation2 (View quieto, View mueve){

        quieto.setPivotX(0);
        quieto.setPivotY(0);

        float x1 = quieto.getX();
        float y1 = quieto.getY();

        x = mueve.getX();
        y = mueve.getY();

        width = mueve.getMeasuredWidth();
        height = mueve.getMeasuredHeight();

        float anchoQuieto = quieto.getMeasuredWidth();
        float largoQuieto = quieto.getMeasuredHeight();

        float anchoMueve = mueve.getMeasuredWidth();
        float largoMueve = mueve.getMeasuredHeight();

        ObjectAnimator animTranslationX = ObjectAnimator.ofFloat(mueve, View.TRANSLATION_X, -(657)); // 657;
        ObjectAnimator animTranslationY = ObjectAnimator.ofFloat(mueve, View.TRANSLATION_Y, 293); // 164

        ObjectAnimator animTransformerWidth = ObjectAnimator.ofFloat(mueve, View.SCALE_X, porcentaje(anchoMueve,anchoQuieto));
        ObjectAnimator animTranslationHeight = ObjectAnimator.ofFloat(mueve, View.SCALE_Y, porcentaje(largoMueve, largoQuieto));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1500);
        animatorSet.playTogether(animTranslationX, animTranslationY, animTransformerWidth, animTranslationHeight);
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                loadFragment();
                back(wrapperMax);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    public void back(View mueve){

        ObjectAnimator animTranslationX = ObjectAnimator.ofFloat(mueve, View.TRANSLATION_X, 0); // 657;
        ObjectAnimator animTranslationY = ObjectAnimator.ofFloat(mueve, View.TRANSLATION_Y, 0); // 164

        ObjectAnimator animTransformerWidth = ObjectAnimator.ofFloat(mueve, View.SCALE_X, 1);
        ObjectAnimator animTranslationHeight = ObjectAnimator.ofFloat(mueve, View.SCALE_Y, 1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(100);
        animatorSet.playTogether(animTranslationX, animTranslationY, animTransformerWidth, animTranslationHeight);
        animatorSet.start();
    }

    public  float porcentaje(float a, float b){
        return  (b / a);
    }
}
