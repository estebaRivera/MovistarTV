package com.smartboxtv.movistartv.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.util.BitmapCache;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.animation.ManagerAnimation;
import com.smartboxtv.movistartv.data.clean.DataClean;
import com.smartboxtv.movistartv.data.database.DataBaseUser;
import com.smartboxtv.movistartv.data.database.UserNunchee;
import com.smartboxtv.movistartv.data.image.ScreenShot;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Esteban- on 26-05-14.
 */
public class SecondPreviewActivity extends ActionBarActivity {

    private RelativeLayout contenedorLoading;
    private RelativeLayout contenedorActionbarOption;

    private DataBaseUser dataBaseUser;
    private UserNunchee userNunchee;

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

    private File file;

    /*private boolean esTrivia = false;
    private boolean esEncuesta = false;
    private boolean esTw = false;
    private boolean facebookActive;*/

    private boolean isNotification = false;
    private boolean isConfiguration = false;
    private boolean fbActivate = true;
    private boolean showSearch = false;

    private boolean ICheckIn = false;
    private boolean ILike = false;
    private boolean AddFavorite = false;
    private boolean IReminderProgram = false;
    private boolean IShare = false;

    private int option;

    public float x;
    public float y;
    public float width;
    public float height;

    private Bitmap bm;
    private  String path;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions, publish stream");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;
    private UiLifecycleHelper uiHelper;

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

        dataBaseUser = new DataBaseUser(getApplicationContext(),"",null,0);
        userNunchee = dataBaseUser.select(UserPreference.getIdFacebook(getApplicationContext()));

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
        path = extra.getString("background");
        file = (File) extra.get("file");

        ICheckIn = extra.getBoolean("check");
        ILike = extra.getBoolean("like");
        IShare = extra.getBoolean("share");
        IReminderProgram = extra.getBoolean("reminder");
        AddFavorite = extra.getBoolean("favorite");

        bm = BitmapFactory.decodeFile(path);
        back.setImageBitmap(bm);

        option = extra.getInt("click");
    }

    @Override
    public void onBackPressed() {
        file.getFreeSpace();
        file.delete();
        finish();
    }

    public void loadFragment(){

        programaPreview.ICheckIn = ICheckIn;
        if(ICheckIn == true){
            programaPreview.setCheckIn(programaPreview.getCheckIn() +1);
            //Log.e(programaPreview.getTitle()+" Cantidad de Check IN","--> "+programaPreview.getCheckIn());
        }
        programaPreview.ILike = ILike;
        if(ILike == true){
            programaPreview.setLike(programaPreview.getLike()+1);
        }
        programaPreview.IsFavorite = AddFavorite;

        // Inicio de fragmentos
        fragmentoAccion = new ActionFragment(programaPreview,programa);
        fragmentoAccion.IReminder = IReminderProgram;
        fragmentoAccion.IShare = IShare;

        fragmentoHeader = new HeaderFragment(programaPreview,programa);
        if(ICheckIn == true){
            fragmentoHeader.NCheckIn = programa.getCheckIn() + 1;
        }
        else{
            fragmentoHeader.NCheckIn = programa.getCheckIn();
        }
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
                overridePendingTransition(R.anim.nada, R.anim.fade_out_activity);
            }
        });

        ImageButton search = (ImageButton) view.findViewById(R.id.icon_search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showSearch){
                    showSearch = false;
                    isConfiguration = false;
                    isNotification = false;

                    RelativeLayout r = (RelativeLayout) findViewById(R.id.view_parent);
                    Bitmap screenShot = ScreenShot.takeScreenshot(r);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    screenShot.compress(Bitmap.CompressFormat.JPEG, 87, stream);
                    byte[] byteArray = stream.toByteArray();

                    try {
                        String filename = getCacheDir()
                                + File.separator + System.currentTimeMillis() + ".jpg";

                        File f = new File(filename);
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(byteArray);
                        fo.close();

                        Intent i = new Intent(SecondPreviewActivity.this, SearchActivity.class);
                        i.putExtra("background", filename);
                        i.putExtra("file", f);
                        startActivityForResult(i, 0);
                        overridePendingTransition(R.anim.fade_actvity, R.anim.fade_out_activity);

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    contenedorActionbarOption.removeAllViews();
                }
                else{
                    showSearch = true;
                }
            }
        });

        ImageButton configuracion = (ImageButton) view.findViewById(R.id.item_configuracion);
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConfiguration) {
                    isConfiguration = false;
                    isNotification = false;
                    showSearch = false;
                    contenedorActionbarOption.removeAllViews();

                } else {

                    isConfiguration = true;
                    isNotification = false;
                    showSearch = false;

                    View containerConfiguration = inflater.inflate(R.layout.action_bar_configuration, null, false);
                    Typeface light = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP.ttf");
                    Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Bold.ttf");

                    final RelativeLayout r1 = (RelativeLayout) containerConfiguration.findViewById(R.id.config_auto_post);
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

                    UserNunchee u = dataBaseUser.select(UserPreference.getIdFacebook(getApplication()));
                    dataBaseUser.close();
                    if(u.isFacebookActive == false){
                        fb.setAlpha((float)0.3);
                        txtAutoPost2.setText("Activa tu post en Facebook");
                    }
                    else{
                        fb.setAlpha((float)1);
                        txtAutoPost2.setText("Desactiva tu post en Facebook");
                    }

                    ImageView exit = (ImageView) containerConfiguration.findViewById(R.id.exit);

                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isConfiguration = false;
                            showSearch = false;
                            isNotification = false;
                            contenedorActionbarOption.removeAllViews();
                        }
                    });

                    r1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ManagerAnimation.selection(r1);
                            AnimatorSet set = new AnimatorSet();

                            UserNunchee u = dataBaseUser.select(UserPreference.getIdFacebook(getApplication()));
                            dataBaseUser.close();

                            if(u.isFacebookActive == true){
                                set.playTogether(
                                        ObjectAnimator.ofFloat(fb, "alpha", 0.3f),
                                        ObjectAnimator.ofFloat(fb, "scaleX", 1, 1.3f),
                                        ObjectAnimator.ofFloat(fb, "scaleY", 1, 1.3f));
                                set.setDuration(400);
                                set.start();
                                set.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        AnimatorSet aux = new AnimatorSet();
                                        aux.playTogether(
                                                ObjectAnimator.ofFloat(fb, "scaleX", 1.3f, 1f),
                                                ObjectAnimator.ofFloat(fb, "scaleY", 1.3f, 1f));
                                        aux.setDuration(400);
                                        aux.start();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {
                                    }
                                });
                                fbActivate = false;
                                txtAutoPost2.setText("Activa tu post en Facebook");
                                userNunchee.isFacebookActive = false;
                                dataBaseUser.updateFacebookActive(UserPreference.getIdFacebook(getApplicationContext()), userNunchee);
                                dataBaseUser.close();
                            }
                            else{

                                set.playTogether(
                                        ObjectAnimator.ofFloat(fb, "alpha",  1f),
                                        ObjectAnimator.ofFloat(fb, "scaleX", 1, 1.3f),
                                        ObjectAnimator.ofFloat(fb, "scaleY", 1, 1.35f));
                                set.setDuration(400);
                                set.start();
                                set.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        AnimatorSet aux = new AnimatorSet();
                                        aux.playTogether(
                                                ObjectAnimator.ofFloat(fb, "scaleX", 1.3f, 1f),
                                                ObjectAnimator.ofFloat(fb, "scaleY", 1.3f, 1f));
                                        aux.setDuration(400);
                                        aux.start();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {
                                    }
                                });
                                fbActivate = true;
                                txtAutoPost2.setText("Desactiva tu post en Facebook");

                                userNunchee.isFacebookActive = true;
                                dataBaseUser.updateFacebookActive(UserPreference.getIdFacebook(getApplicationContext()), userNunchee);
                                dataBaseUser.close();
                            }

                        }
                    });

                    r3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            contenedorLoading.removeAllViews();
                            contenedorActionbarOption.removeAllViews();

                            isConfiguration = false;
                            showSearch = false;
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
                            showSearch = false;
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

        ImageButton notificacion = (ImageButton) view.findViewById(R.id.item_notificaciones);
        notificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNotification) {

                    isConfiguration = false;
                    showSearch = false;
                    isNotification = false;
                    contenedorActionbarOption.removeAllViews();

                } else {

                    isNotification = true;
                    isConfiguration = false;
                    showSearch = false;

                    contenedorActionbarOption.removeAllViews();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    NotificationFragment dialogError = new NotificationFragment();
                    ft.replace(R.id.container_action_bar, dialogError);
                    ft.commit();
                }
            }
        });
        actionBar.setCustomView(view,layout);
    }
    public  void shareDialog(){

        SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd' 'HH'$'mm'$'ss");

        String url = "http://www.movistar.cl/PortalMovistarWeb/tv-digital/guia-de-canales";

        Image imagen = programa.getImageWidthType(Width.ORIGINAL_IMAGE, Type.BACKDROP_IMAGE);
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

        /*DialogShare dialogShare = new DialogShare(text,imageUrl,title,url);
        dialogShare.show(getSupportFragmentManager(),"");*/

        /*OpenGraphObject meal = OpenGraphObject.Factory.createForPost("cooking-app:meal");
        meal.setProperty("title", "Buffalo Tacos");
        meal.setProperty("image", "http://example.com/cooking-app/images/buffalo-tacos.png");
        meal.setProperty("url", "https://example.com/cooking-app/meal/Buffalo-Tacos.html");
        meal.setProperty("description", "Leaner than beef and great flavor.");

        OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
        action.setProperty("meal", meal);

        FacebookDialog shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(this, action, "cooking-app:cook", "meal")
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());*/
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

    @Override
    protected void onDestroy() {
        file.getFreeSpace();
        file.delete();
        super.onDestroy();
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
