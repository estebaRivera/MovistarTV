package com.smartboxtv.movistartv.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.animation.ManagerAnimation;
import com.smartboxtv.movistartv.data.clean.DataClean;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.modelssm.LiveSM;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.programation.menu.About;
import com.smartboxtv.movistartv.programation.menu.NotificationFragment;
import com.smartboxtv.movistartv.programation.menu.Politica;

/**
 * Created by Esteban- on 08-07-14.
 */
public class PlayerActivity extends ActionBarActivity {

    private VideoView videoView;
    private boolean isPlaying = false;
    private boolean isFullScreen = false;
    private boolean isShowControlBar = false;
    //private LiveSM programLive;

    private boolean esTrivia = false;
    private boolean esEncuesta = false;
    private boolean esTw = false;

    private boolean isMessage = false;
    private boolean isNotification = false;
    private boolean isConfiguration = false;
    private boolean fbActivate;

    private ProgressDialog progress;

    private RelativeLayout contenedorAnimacion;
    private RelativeLayout contenedorTw;
    private RelativeLayout contenedorTrivia;
    private RelativeLayout contenedorEncuesta;
    private RelativeLayout contenedorSugeridos;
    private RelativeLayout contenedorHeader;
    private RelativeLayout contenedorLoading;
    private RelativeLayout contenedorActionbarOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);
        DataClean.garbageCollector("Player Activity");

        Bundle extra = this.getIntent().getExtras();
        LiveSM liveProgram = (LiveSM) extra.get("live");

        String path =   extra.getString("background");
        Bitmap image = BitmapFactory.decodeFile(path);

        progress = new ProgressDialog(this);
        progress.show();
        progress.setContentView(R.layout.progress_dialog_pop_corn);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        ImageView background = (ImageView) findViewById(R.id.background);
        background.setImageBitmap(image);

        videoView = (VideoView) findViewById(R.id.video_player);

       // String url = "http://live.hls.http.13.ztreaming.com/13hd/13hd-900.m3u8";
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Log.e("hlsdnkjzfbd", "  kdsfnfsdkjn");
                if (isShowControlBar) {
                    isShowControlBar = false;
                    hidenControlBar();
                    Log.e("hiden", "  kdsfnfsdkjn");
                } else {
                    isShowControlBar = true;
                    showControlBar();
                    Log.e("show", "  kdsfnfsdkjn");
                }
                return false;
            }
        });

        //String url = "http://hls01-07.az.myvideo.az/hls-live/livepkgr/idman/idman/idman.m3u8#www.rojadirecta.me";

        ImageButton tv = (ImageButton) findViewById(R.id.btn_tv);
        ImageButton play = (ImageButton) findViewById(R.id.btn_play);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullScreen();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        start(liveProgram);
        createActionBar();
    }

    void start(LiveSM live){
        videoView.setVideoPath(live.url);
        videoView.start();
        progress.dismiss();
    }

    void play(){
        if(isPlaying){
            isPlaying = false;
            videoView.pause();
        }else{
            isPlaying = true;
            videoView.start();
        }
    }

    private void showControlBar(){

        RelativeLayout r = (RelativeLayout) findViewById(R.id.control_bar);
        ManagerAnimation.showControlBar(r);
    }
    private void hidenControlBar(){
        RelativeLayout r = (RelativeLayout) findViewById(R.id.control_bar);
        ManagerAnimation.hidenControlBar(r);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    void fullScreen(){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.y ;
        int height = size.x;

        if(!isFullScreen){

            isFullScreen = true;

            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();

            /*RelativeLayout r = (RelativeLayout) findViewById(R.id.control_bar);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) r.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_BASELINE, R.id.video_player);
            params.width = params.MATCH_PARENT;
            params.height = 50;
            r.setLayoutParams(params);
            //r.setVisibility(View.GONE);

            RelativeLayout.LayoutParams paramsVideo = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
            paramsVideo.height = params.MATCH_PARENT;
            paramsVideo.width =  params.MATCH_PARENT;
            videoView.setLayoutParams(paramsVideo);*/

            RelativeLayout r = (RelativeLayout) findViewById(R.id.container_video_player);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) r.getLayoutParams();
            //params.addRule(RelativeLayout.ALIGN_BASELINE, R.id.video_player);
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            r.setLayoutParams(params);
        }
        else{
            isFullScreen = false;

            ActionBar actionBar = getSupportActionBar();
            actionBar.show();

            /*RelativeLayout r = (RelativeLayout) findViewById(R.id.control_bar);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) r.getLayoutParams();
            params.width = 620;
            params.height = 50;

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params1.height = 430;
            params1.width = 620;

            videoView.setLayoutParams(params1);
            r.setLayoutParams(params);

            //params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);*/

            RelativeLayout r = (RelativeLayout) findViewById(R.id.container_video_player);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) r.getLayoutParams();

            params.height = 430;
            params.width = 620;
            r.setLayoutParams(params);
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
       /* aq.id(imageProfile).image("http://graph.facebook.com/" + UserPreference.getIdFacebook(PlayerActivity.this)
                + "/picture?type=square");*/

        actionBar.setCustomView(view,layout);

    }
}
