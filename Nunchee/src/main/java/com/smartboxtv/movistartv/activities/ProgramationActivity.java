package com.smartboxtv.movistartv.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.Fragment;
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
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.animation.ManagerAnimation;
import com.smartboxtv.movistartv.data.clean.DataClean;
import com.smartboxtv.movistartv.data.database.DataBaseUser;
import com.smartboxtv.movistartv.data.database.UserNunchee;
import com.smartboxtv.movistartv.data.image.ScreenShot;
import com.smartboxtv.movistartv.data.models.Channel;
import com.smartboxtv.movistartv.data.models.FeedJSON;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.TrendingChannel;
import com.smartboxtv.movistartv.data.models.UserFacebook;
import com.smartboxtv.movistartv.data.modelssm.LiveSM;
import com.smartboxtv.movistartv.data.modelssm.LiveStream;
import com.smartboxtv.movistartv.data.modelssm.LiveStreamSchedule;
import com.smartboxtv.movistartv.data.modelssm.TokenIssue;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.data.preference.UserPreferenceSM;
import com.smartboxtv.movistartv.delgates.UpdateNotificationDelegate;
import com.smartboxtv.movistartv.fragments.NUNCHEE;
import com.smartboxtv.movistartv.programation.categories.CategoryFragment;
import com.smartboxtv.movistartv.programation.favorites.FavoriteFragment;
import com.smartboxtv.movistartv.programation.horary.HoraryFragment;
import com.smartboxtv.movistartv.programation.menu.About;
import com.smartboxtv.movistartv.programation.menu.DialogError;
import com.smartboxtv.movistartv.programation.menu.FavoriteMenuFragment;
import com.smartboxtv.movistartv.programation.menu.FeedFragment;
import com.smartboxtv.movistartv.programation.menu.NotificationFragment;
import com.smartboxtv.movistartv.programation.menu.Politica;
import com.smartboxtv.movistartv.programation.preview.TriviaMinFragment;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.services.ServiceManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ProgramationActivity extends ActionBarActivity{

    private Button horario;
    private Button favorito;
    private Button categoria;
    private ImageButton menu;

    private DataBaseUser dataBaseUser;
    private UserNunchee userNunchee;

    private Fragment fg;
    private FragmentTransaction ft;
    //private List<FeedJSON> listFeed;
    private List<UserFacebook> friends;
    private List<TrendingChannel> lista = new ArrayList<TrendingChannel>();

    // Fragmentos pequeños

    private TriviaMinFragment fragmentoTriviaP;
    // Loading
    private View viewLoading;
    private LayoutInflater inflaterPrivate;
    private RelativeLayout contenedorLoading;
    private RelativeLayout contenedorExitMenu;
    private RelativeLayout contenedorMenuBar;
    private RelativeLayout trending;
    private RelativeLayout liveContainer;

    private ScrollView scrollViewTrending;
    private ScrollView scrollViewLive;

    private ImageView iconMasLive;
    private ImageView iconMenosLive;
    private ImageView iconMasTrending;
    private ImageView iconMenosTrending;

    private List<View> listViewLive = new ArrayList<View>();
    private List<View> listViewTrending = new ArrayList<View>();

    //private RelativeLayout contenedorActionbarOption;
    private boolean isConfiguration = false;
    private boolean isNotification = false;
    private boolean isSlideMenuOpen = false;
    //private boolean fbActivate;
    private boolean showSearch = false;
    private boolean isMenuLiveOpen = false;
    private boolean isTrendingOpen = false;
    private boolean isOnLiveStream = false;

    private List<LiveStreamSchedule> liveStreamSchedules = new ArrayList<LiveStreamSchedule>();
    private List<LiveStreamSchedule> liveStreamSchedulesAUX = new ArrayList<LiveStreamSchedule>();
    private List<LiveStream> liveStreams = new ArrayList<LiveStream>();

    private UpdateNotificationDelegate notificationDelegate;
    private UiLifecycleHelper uiHelper;

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DataClean.garbageCollector("Recommended Activity");
        Resources res = getResources();
        Drawable background = res.getDrawable(R.drawable.back_nav_s);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(background);

        //((NUNCHEE)getApplication()).catchGlobal();

        setContentView(R.layout.programation_fragment);
        Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Bold.ttf");

        inflaterPrivate = LayoutInflater.from(getApplicationContext());
        contenedorLoading = (RelativeLayout) findViewById(R.id.contenedor_preview);
        //contenedorActionbarOption = (RelativeLayout) findViewById(R.id.contenedor_action_bar);
        contenedorMenuBar = (RelativeLayout) findViewById(R.id.contenedor_menu_bar);
        contenedorExitMenu = (RelativeLayout) findViewById(R.id.contenedor_menu);

        // trending channel y menú live
        trending = (RelativeLayout) findViewById(R.id.trending_channel);
        liveContainer = (RelativeLayout) findViewById(R.id.btn_en_vivo);

        horario = (Button) findViewById(R.id.boton_horario);
        horario.setTypeface(bold);
        horario.setBackgroundResource(R.drawable.effect);
        horario.setSelected(true);

        categoria = (Button) findViewById(R.id.boton_categoria);
        categoria.setTypeface(bold);
        categoria.setBackgroundResource(R.drawable.effect);

        favorito = (Button) findViewById(R.id.boton_favorito);
        favorito.setTypeface(bold);
        favorito.setBackgroundResource(R.drawable.effect);

        HoraryFragment horaryFragment = new HoraryFragment();

        dataBaseUser = new DataBaseUser(getApplicationContext(),"",null,0);
        userNunchee = dataBaseUser.select(UserPreference.getIdFacebook(getApplicationContext()));
        //fbActivate = userNunchee.isFacebookActive;

        ft =  getSupportFragmentManager().beginTransaction();
        ft.add(R.id.contenedor, horaryFragment);
        ft.commit();
        fg = horaryFragment;

        try{
            getLiveStreamShedule();
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e("Error Live","--> "+e.getMessage());
        }

        DataLoader dataLoader = new DataLoader(getApplication());
        dataLoader.getTrendingChannel( new DataLoader.DataLoadedHandler<TrendingChannel>(){
            @Override
            public void loaded(List<TrendingChannel> data) {
                super.loaded(data);

                lista = data;
                setDataTrendinChannel();
            }
            @Override
            public void error(String error) {
                setDataTrendinChannel();
                super.error(error);
            }
        });

        horario.setOnClickListener(new View.OnClickListener() {                                             // Declaración de evento de Listener por boton
            @Override
            public void onClick(View view) {

                if(!horario.isSelected()){

                    horario.setSelected(true);
                    categoria.setSelected(false);
                    favorito.setSelected(false);

                    int id;
                    if(fg != null){

                        id = fg.getId();
                        ft =  getSupportFragmentManager().beginTransaction();
                        ft.commit();
                    }
                    else{
                        id = R.id.contenedor;
                    }

                    HoraryFragment horaryFragment = new HoraryFragment();

                    ft =  getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.contenedor, horaryFragment);
                    ft.commit();
                    fg = horaryFragment;
                }
            }
        });

        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!categoria.isSelected()){

                    horario.setSelected(false);
                    categoria.setSelected(true);
                    favorito.setSelected(false);

                    int id;
                    if(fg != null){

                        id = fg.getId();
                        ft =  getSupportFragmentManager().beginTransaction();
                        ft.detach(fg);
                        ft.commit();
                    }
                    else{
                        id = R.id.contenedor;
                    }

                    CategoryFragment categoryFragment = new CategoryFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); // Llena el RelativeLayout (contenedor) con el XML correspondiente a la Clase

                    ft.replace(id, categoryFragment);
                    ft.commit();

                    fg = categoryFragment;
                }
            }
        });

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!favorito.isSelected()){

                    horario.setSelected(false);
                    categoria.setSelected(false);
                    favorito.setSelected(true);

                    int id;
                    if(fg != null){

                        id = fg.getId();
                        FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();

                        ft.detach(fg);
                        ft.commit();
                    }
                    else{
                        id = R.id.contenedor;
                    }
                    FavoriteFragment favoriteFragment = new FavoriteFragment();
                    FragmentTransaction ft =  getSupportFragmentManager().beginTransaction(); // Llena el RelativeLayout (contenedor) con el XML correspondiente a la Clase

                    ft.replace(id, favoriteFragment);
                    ft.commit();

                    fg = favoriteFragment;
                }
            }
        });
        createActionBar();
        createMenuLive();
    }

    public void setDataTrendinChannel(){

        AQuery aq;
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.lista_canales);
        scrollViewTrending = (ScrollView) findViewById(R.id.scroll_trending);
        scrollViewTrending.setPivotY(0);

        //RelativeLayout trending = (RelativeLayout) findViewById(R.id.trending_channel);

        iconMasTrending = (ImageView) findViewById(R.id.icono_trending_mas);
        iconMenosTrending = (ImageView) findViewById(R.id.icono_trending_menos);


        trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTrendingOpen){
                    hideTrending();
                }
                else{
                    showTrending();
                }
            }
        });

        if(lista.size()>0){
            for(int i = 0; i < lista.size(); i++){
                Program program = new Program();
                program.setTitle(lista.get(i).getNombrePrograma());
                program.setStartDate(lista.get(i).getFechaInicio());
                program.setEndDate(lista.get(i).getFechaFin());
                program.setIdProgram(lista.get(i).getIdPrograma());
                program.setPChannel(new Channel());
                program.getPChannel().setChannelID(lista.get(i).getIdChannel());
                final Program p = program;

                if(i%2 == 0){

                    LayoutInflater inflater = LayoutInflater.from(this);
                    View channel = inflater.inflate(R.layout.slide_menu_trending_channel, null);

                    aq = new AQuery(channel);
                    TextView name = (TextView) channel.findViewById(R.id.menu_nombre_canal);
                    TextView position = (TextView) channel.findViewById(R.id.menu_posicion);

                    ImageView image = (ImageView) channel.findViewById(R.id.menu_foto_canal);
                    ImageView topOne = (ImageView) channel.findViewById(R.id.top_one);

                    if(i == 0)
                        topOne.setVisibility(View.VISIBLE);

                    name.setText(lista.get(i).getNombreCanal());
                    position.setText(lista.get(i).getPosicion());

                    aq.id(image).image(lista.get(i).getFotoCanal());

                    linearLayout.addView(channel);
                    channel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideSlideMenu2(p);
                        }
                    });
                    listViewTrending.add(linearLayout);
                }
                else{
                    LayoutInflater inflater = LayoutInflater.from(this);
                    View channel = inflater.inflate(R.layout.slide_menu_trending_channel_2, null);

                    aq = new AQuery(channel);
                    TextView name = (TextView) channel.findViewById(R.id.menu_nombre_canal);
                    TextView position = (TextView) channel.findViewById(R.id.menu_posicion);

                    ImageView image = (ImageView) channel.findViewById(R.id.menu_foto_canal);

                    name.setText(lista.get(i).getNombreCanal());
                    position.setText(lista.get(i).getPosicion());

                    aq.id(image).image(lista.get(i).getFotoCanal());
                    linearLayout.addView(channel);
                    channel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            hideSlideMenu2(p);
                        }
                    });

                    listViewTrending.add(linearLayout);
                }
            }
        }
        else{
            RelativeLayout r = (RelativeLayout) findViewById(R.id.wrapper_no_trending);
            r.setVisibility(View.VISIBLE);

        }
    }

    public void showTrending(){
        isTrendingOpen = true;
        iconMasTrending.setVisibility(View.GONE);
        iconMenosTrending.setVisibility(View.VISIBLE);
        scrollViewTrending.setVisibility(View.VISIBLE);
        ManagerAnimation.scaleYList(listViewTrending);
        if(isMenuLiveOpen){
           isMenuLiveOpen = false;
            hideLive();
        }
    }

    public void hideTrending(){
        isTrendingOpen = false;
        iconMasTrending.setVisibility(View.VISIBLE);
        iconMenosTrending.setVisibility(View.GONE);
        ManagerAnimation.noScaleYList(listViewTrending);
        scrollViewTrending.setVisibility(View.GONE);
    }

    public void showLive(){
        isMenuLiveOpen = true;
        iconMasLive.setVisibility(View.GONE);
        iconMenosLive.setVisibility(View.VISIBLE);
        scrollViewLive.setVisibility(View.VISIBLE);
        ManagerAnimation.scaleYList(listViewLive);
        if(isTrendingOpen){
            isTrendingOpen = false;
            hideTrending();
        }
    }
    public void hideLive(){
        isMenuLiveOpen = false;
        iconMasLive.setVisibility(View.VISIBLE);
        iconMenosLive.setVisibility(View.GONE);
        ManagerAnimation.noScaleYList(listViewLive);
        scrollViewLive.setVisibility(View.GONE);
    }

    private void createMenuLive(){

        final LinearLayout containerScrollView = (LinearLayout) findViewById(R.id.container_list_live);
        final List<LiveSM> list = new ArrayList<LiveSM>();

        scrollViewLive = (ScrollView) findViewById(R.id.list_live);
        scrollViewLive.setVisibility(View.GONE);

        for (LiveStreamSchedule liveStreamSchedule : liveStreamSchedules) {
            list.add(new LiveSM("", liveStreamSchedule.getName(), "", liveStreamSchedule.getStartDate(), "", liveStreamSchedule));
        }

        LayoutInflater inflater = LayoutInflater.from(this);

        iconMasLive = (ImageView) findViewById(R.id.icono_live_mas);
        iconMenosLive = (ImageView) findViewById(R.id.icono_live_menos);

        SimpleDateFormat format = new SimpleDateFormat("EEEE HH:mm");
        long ahora = new Date().getTime();
        Date fecha;

        for(int count  = 0 ; count < list.size(); count++){

            if(list.size() > 0){
                RelativeLayout r = (RelativeLayout) findViewById(R.id.contenedor_live);
                r.setVisibility(View.GONE);
            }
           liveStreamSchedulesAUX.add(liveStreamSchedules.get(count));
            fecha = new Date (list.get(count).date.getTime() - 14400000);
            if(count % 2 == 0){
                View program = inflater.inflate(R.layout.element_live, null);
                AQuery aq = new AQuery(program);
                TextView name = (TextView) program.findViewById(R.id.name_live);
                TextView date = (TextView) program.findViewById(R.id.date_live);

                ImageButton play = (ImageButton) program.findViewById(R.id.play_live);

                // Comprobación del largo del texto
                if(list.get(count).nombre.length()< 26){
                    name.setText(list.get(count).nombre);
                }
                else{
                    name.setText(list.get(count).nombre.substring(0,24)+"...");
                }
                // Comprobración del programa En VIVO
                if(ahora >  fecha.getTime()){
                    date.setText("AHORA");
                }
                else{
                    date.setText(format.format(fecha).toUpperCase());
                }

                if(!list.get(count).image.isEmpty())
                    aq.id(R.id.image_live).image(list.get(count).image);

                final int finalI = count;

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isSlideMenuOpen) {
                            showSlideMenu();
                        } else {
                            hideSlideMenu3(list.get(finalI));
                        }
                    }
                });
                listViewLive.add(program);
                containerScrollView.addView(program);
            }
            else{
                View program = inflater.inflate(R.layout.element_live_impar, null);
                AQuery aq = new AQuery(program);
                TextView name = (TextView) program.findViewById(R.id.name_live);
                TextView date = (TextView) program.findViewById(R.id.date_live);

                ImageButton play = (ImageButton) program.findViewById(R.id.play_live);

                // comprobación del latgo del texto
                if(list.get(count).nombre.length()< 26){
                    name.setText(list.get(count).nombre);
                }
                else{
                    name.setText(list.get(count).nombre.substring(0,24)+"...");
                }
                // Comprobración del programa En VIVO
                if(ahora >  fecha.getTime()){
                    date.setText("AHORA");
                }
                else{
                    date.setText(format.format(fecha).toUpperCase());
                }

                if(!list.get(count).image.isEmpty())
                    aq.id(R.id.image_live).image(list.get(count).image);

                final int finalI = count;

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isSlideMenuOpen) {
                            showSlideMenu();
                        } else {
                            hideSlideMenu3(list.get(finalI));
                        }
                    }
                });
                listViewLive.add(program);
                containerScrollView.addView(program);
            }
        }

        liveContainer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMenuLiveOpen){
                    hideLive();
                }
                else{
                    showLive();
                }
            }
        });

    }

    private void createActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        final LayoutInflater inflater = LayoutInflater.from(this);

        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);

        View view = inflater.inflate(R.layout.action_bar, null);

        ImageView iconSearch = (ImageView) view.findViewById(R.id.icon_search);
        menu = (ImageButton)view.findViewById(R.id.item_menu);

        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showSearch == true) {
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

                        Intent i = new Intent(ProgramationActivity.this, SearchActivity.class);
                        i.putExtra("background", filename);
                        i.putExtra("file", f);
                        startActivityForResult(i, 0);
                        overridePendingTransition(R.anim.fade_actvity, R.anim.fade_out_activity);

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    contenedorMenuBar.removeAllViews();
                } else {
                    showSearch = true;
                }
            }
        });

        final ImageButton configuracion = (ImageButton) view.findViewById(R.id.item_configuracion);
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConfiguration == true) {
                    showSearch = false;
                    isConfiguration = false;
                    isNotification = false;
                    contenedorMenuBar.removeAllViews();

                } else {

                    showSearch = false;
                    isConfiguration = true;
                    isNotification = false;

                    contenedorMenuBar.removeAllViews();
                    final View containerConfiguration = inflater.inflate(R.layout.action_bar_configuration, null, false);
                    Typeface light = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP.ttf");
                    Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Bold.ttf");

                    ImageView arrow = (ImageView) containerConfiguration.findViewById(R.id.configuration_arrow);
                    ImageView imageExit = (ImageView) containerConfiguration.findViewById(R.id.exit);
                    RelativeLayout wrapper = (RelativeLayout) containerConfiguration.findViewById(R.id.wrapper);

                    ManagerAnimation.fade(wrapper, arrow, imageExit);

                    final RelativeLayout r1 = (RelativeLayout) containerConfiguration.findViewById(R.id.config_auto_post);
                    final RelativeLayout r2 = (RelativeLayout) containerConfiguration.findViewById(R.id.config_tutorial);
                    final RelativeLayout r3 = (RelativeLayout) containerConfiguration.findViewById(R.id.config_acerca_de);
                    final RelativeLayout r4 = (RelativeLayout) containerConfiguration.findViewById(R.id.confif_terminos_y_condiciones);

                    TextView txtAutoPost = (TextView) containerConfiguration.findViewById(R.id.auto_post_principal);
                    TextView txtTutorial = (TextView) containerConfiguration.findViewById(R.id.tutorial_principal);
                    TextView txtAcercaDe = (TextView) containerConfiguration.findViewById(R.id.acerca_de_principal);
                    TextView txtTerminos = (TextView) containerConfiguration.findViewById(R.id.termino_principal);

                    final TextView txtAutoPost2 = (TextView) containerConfiguration.findViewById(R.id.auto_post_secundario);
                    TextView txtTutorial2 = (TextView) containerConfiguration.findViewById(R.id.tutorial_secundario);
                    TextView txtAcercaDe2 = (TextView) containerConfiguration.findViewById(R.id.acerca_de_secundario);
                    TextView txtTerminos2 = (TextView) containerConfiguration.findViewById(R.id.termino_secundario);

                    TextView title = (TextView) containerConfiguration.findViewById(R.id.configuracion_principal);

                    txtAcercaDe.setTypeface(bold);
                    txtAutoPost.setTypeface(bold);
                    txtTerminos.setTypeface(bold);
                    txtTutorial.setTypeface(bold);
                    title.setTypeface(bold);

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
                            contenedorMenuBar.removeAllViews();
                            //isNotification = false;
                        }
                    });

                    r1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ManagerAnimation.selection(r1);
                            AnimatorSet set = new AnimatorSet();
                            UserNunchee u = dataBaseUser.select(UserPreference.getIdFacebook(getApplication()));
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
                                //fbActivate = false;
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
                                //fbActivate = true;
                                txtAutoPost2.setText("Desactiva tu post en Facebook");

                                userNunchee.isFacebookActive = true;
                                dataBaseUser.updateFacebookActive(UserPreference.getIdFacebook(getApplicationContext()),userNunchee);
                                dataBaseUser.close();
                            }

                        }
                    });
                    r2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ManagerAnimation.selection(r2);
                            Intent intent = new Intent(ProgramationActivity.this, TutorialActivity.class);
                            UserPreference.setShowTutorial(true, getApplicationContext());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    });

                    r3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ManagerAnimation.selection(r3);
                            contenedorLoading.removeAllViews();
                            contenedorMenuBar.removeAllViews();
                            isConfiguration = false;
                            isNotification = false;
                            showSearch = false;

                            About fg = new About();
                            fg.show(getSupportFragmentManager(),"");
                        }
                    });

                    r4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ManagerAnimation.selection(r4);
                            contenedorLoading.removeAllViews();
                            contenedorMenuBar.removeAllViews();
                            isConfiguration = false;
                            isNotification = false;
                            showSearch = false;

                            Politica fg = new Politica();
                            fg.show(getSupportFragmentManager(),"");
                        }
                    });

                    contenedorMenuBar.addView(containerConfiguration);
                }
            }
        });

        notificationDelegate = new UpdateNotificationDelegate() {
            @Override
            public void updateNotification(boolean isNotification2) {

                    //isConfiguration = !isConfiguration;
                    isNotification = !isNotification;
                    //showSearch = !showSearch;
                    contenedorMenuBar.removeAllViews();
            }
        };
        final ImageButton notificacion = (ImageButton) view.findViewById(R.id.item_notificaciones);
        notificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNotification == true) {

                    isConfiguration = false;
                    isNotification = false;
                    showSearch = false;

                    contenedorMenuBar.removeAllViews();

                } else {

                    isNotification = true;
                    isConfiguration = false;
                    showSearch = false;
                    contenedorMenuBar.removeAllViews();
                    //contenedorActionbarOption.removeAllViews();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    NotificationFragment dialogError = new NotificationFragment();
                    dialogError.setNotificationDelegate(notificationDelegate);
                    ft.replace(R.id.contenedor_menu_bar, dialogError);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSlideMenuOpen) {
                    hideSlideMenu();
                }
                else {
                    showSlideMenu();
                }
            }
        });

        // Menu
        final RelativeLayout recomendado = (RelativeLayout) findViewById(R.id.btn_recomendado);
        final RelativeLayout historial = (RelativeLayout) findViewById(R.id.btn_historial);
        final RelativeLayout salir = (RelativeLayout) findViewById(R.id.btn_salir);
        final RelativeLayout favoritos = (RelativeLayout) findViewById(R.id.btn_favoritos);

        TextView userName = (TextView) findViewById(R.id.name_profile);
        Typeface normal = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP.ttf");

        userName.setText(UserPreference.getNombreFacebook(ProgramationActivity.this));
        userName.setTypeface(normal);

        AQuery aq = new AQuery(this);
        aq.id(R.id.image_profile).image("http://graph.facebook.com/" + UserPreference.getIdFacebook(ProgramationActivity.this)
                + "/picture?type=square");

        historial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideSlideMenu();
                loading();
                final DataLoader dataLoader = new DataLoader(ProgramationActivity.this);
                dataLoader.friendsFacebookNunchee(new DataLoader.DataLoadedHandler<UserFacebook>() {
                    @Override
                    public void loaded(List<UserFacebook> data) {

                        friends = data;
                        dataLoader.feed(new DataLoader.DataLoadedHandler<FeedJSON>() {
                            @Override
                            public void loaded(List<FeedJSON> dataFeed) {

                                FeedFragment fragmentoFeed = new FeedFragment(dataFeed);
                                fragmentoFeed.show(getSupportFragmentManager(),"");
                                borraLoading();
                            }
                            @Override
                            public void error(String error) {
                                super.error(error);
                                Log.e("Program Activity error", " Historial error --> " + error);
                                borraLoading();

                                DialogError dialogError = new DialogError();
                                dialogError.show(getSupportFragmentManager(),"");
                            }
                        }, getIdFriends());
                    }

                    @Override
                    public void error(String error) {
                        borraLoading();
                        super.error(error);
                        Log.e("Program Activity error", " Historial(amiigos Face) error --> " + error);

                    }
                });
            }
        });

        favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSlideMenu();
                loading();
                if(((NUNCHEE)getApplication()).CONNECT_SERVICES_PYTHON == false){
                    DataLoader dataLoader = new DataLoader(getApplication());
                    dataLoader.getFavoriteTodos(new DataLoader.DataLoadedHandler<Program>() {
                        @Override
                        public void loaded(List<Program> data) {

                            FavoriteMenuFragment favoritosFragmento = new FavoriteMenuFragment(data);
                            favoritosFragmento.show(getSupportFragmentManager(),"");
                            borraLoading();
                        }

                        @Override
                        public void error(String error) {
                            super.error(error);
                            Log.e("Program Activity error", " Favoritos error --> " + error);
                            borraLoading();
                            DialogError dialogError = new DialogError();
                            dialogError.show(getSupportFragmentManager(), "");
                        }
                    }, UserPreference.getIdNunchee(getApplication()));
                }
                else{

                    ServiceManager serviceManager = new ServiceManager(getApplication());
                    serviceManager.getFavoriteAll(new ServiceManager.ServiceManagerHandler<Program>(){
                        @Override
                        public void loaded(List<Program> data) {
                            super.loaded(data);
                            Log.e("Data",data.toString());
                            FavoriteMenuFragment favoritosFragmento = new FavoriteMenuFragment(data);
                            favoritosFragmento.show(getSupportFragmentManager(),"");
                            borraLoading();
                        }

                        @Override
                        public void error(String error) {
                            super.error(error);
                            Log.e("Program Activity error 2", " Favoritos error --> " + error);
                            borraLoading();
                            DialogError dialogError = new DialogError();
                            dialogError.show(getSupportFragmentManager(), "");
                        }
                    }, UserPreferenceSM.getIdNunchee(getApplication()));
                }

            }
        });

        recomendado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProgramationActivity.this, RecommendedActivity.class);
                startActivity(i);
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((NUNCHEE) getApplication()).CONNECT_SERVICES_PYTHON == false){
                    disconnectFacebookAccount();
                }
                else{

                    String androidID = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);

                    ServiceManager serviceManager = new ServiceManager(getApplication());

                    serviceManager.logoutFacebook(new ServiceManager.ServiceManagerHandler<String>(){
                        @Override
                        public void loaded(String data) {
                            super.loaded(data);
                            Log.e("Data", data);
                            ((NUNCHEE)getApplication()).RELOGIN = false;
                            loginActivity();
                        }

                        @Override
                        public void error(String error) {
                            super.error(error);
                            Log.e("Error","--> "+error);
                        }
                    }, UserPreferenceSM.getTokenFacebook(getApplication()),androidID,"1",UserPreferenceSM.getTokenMovistar(getApplication()));
                }
            }
        });

        actionBar.setCustomView(view,layout);

    }

    public void disconnectFacebookAccount()
    {
        new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            protected void onPreExecute() {
                Toast.makeText(getApplicationContext(),"Disconnecting Facebook account...", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Boolean doInBackground(Void... params)
            {
                if (Session.getActiveSession() != null)
                    Session.getActiveSession().closeAndClearTokenInformation();

                return true;
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                if (result == null || result == false) {
                    onFailedFacebookDisconnect();
                }

                if (result)
                    loginActivity();
                return;
            }
        }.execute();
    }

    public void loginActivity(){
        Intent i = new Intent(ProgramationActivity.this, LoginActivity.class);
        startActivity(i);
    }

    public void onFailedFacebookDisconnect(){
        Toast.makeText(getApplicationContext(),"Disconnecting Facebook Failed", Toast.LENGTH_LONG).show();
    }

    public void onSucceededFacebookDisconnect(){
        Toast.makeText(getApplicationContext(),"onSucceededFacebookDisconnect(", Toast.LENGTH_LONG).show();
    }

    public List<String> getIdFriends(){
        List<String> lista = new ArrayList<String>();
        for (UserFacebook friend : friends) lista.add(friend.getIdUsuario());
        return lista;
    }

    public void loading(){

        viewLoading = inflaterPrivate.inflate(R.layout.progress_dialog_pop_corn, null);
        ImageView imgPopCorn = (ImageView) viewLoading.findViewById(R.id.pop_corn_centro);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Animation animaPop = AnimationUtils.loadAnimation(getApplication(), R.anim.animacion_pop_hacia_derecha_centro);

        viewLoading.setLayoutParams(params);
        imgPopCorn.startAnimation(animaPop);

        contenedorLoading.addView(viewLoading);
        viewLoading.bringToFront();

        Log.e("Loading","Loading");

        contenedorLoading.bringToFront();
        contenedorLoading.setEnabled(false);

    }

    public void borraLoading(){

        Animation anim = AnimationUtils.loadAnimation(getApplication(),R.anim.deaparece);

        viewLoading.startAnimation(anim);
        contenedorLoading.removeView(viewLoading);
        contenedorLoading.setEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)

    public void hideSlideMenu(){

        float slideMenuPosition;
        float menuButtonPosition;

        slideMenuPosition = -290.0f;
        menuButtonPosition = 20.0f;

        isSlideMenuOpen = false;
        contenedorExitMenu.setVisibility(View.GONE);

        View slideMenuContainer = findViewById(R.id.menu_principal);

        ObjectAnimator animatorSlideMenu = ObjectAnimator.ofFloat(slideMenuContainer,"x",slideMenuPosition);
        ObjectAnimator animatorMenuButton = ObjectAnimator.ofFloat(menu,"x",menuButtonPosition);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(animatorSlideMenu, animatorMenuButton);
        animSet.setDuration(600);
        animSet.start();

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)

    public void hideSlideMenu2(final Program p){

        float slideMenuPosition;
        float menuButtonPosition;

        slideMenuPosition = -290.0f;
        menuButtonPosition = 20.0f;

        isSlideMenuOpen = false;
        contenedorExitMenu.setVisibility(View.GONE);

        View slideMenuContainer = findViewById(R.id.menu_principal);

        ObjectAnimator animatorSlideMenu = ObjectAnimator.ofFloat(slideMenuContainer,"x",slideMenuPosition);
        ObjectAnimator animatorMenuButton = ObjectAnimator.ofFloat(menu,"x",menuButtonPosition);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(600);
        animSet.playTogether(animatorSlideMenu, animatorMenuButton);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                RelativeLayout r = (RelativeLayout) findViewById(R.id.view_parent);
                Bitmap screenShot = ScreenShot.takeScreenshot(r);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                screenShot.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] byteArray = stream.toByteArray();

                try {
                    String filename = getCacheDir()
                            + File.separator + System.currentTimeMillis() + ".jpg";

                    File f = new File(filename);
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(byteArray);
                    fo.close();

                    Intent i = new Intent(ProgramationActivity.this, PreviewActivity.class);
                    i.putExtra("background", filename);
                    i.putExtra("programa", p);
                    i.putExtra("file",f);
                    startActivity(i);
                    overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animSet.start();

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)

    public void showSlideMenu(){

        float slideMenuPosition;
        float menuButtonPosition;

        slideMenuPosition =  0.0f;
        menuButtonPosition = 258.0f;

        View slideMenuContainer = findViewById(R.id.menu_principal);

        ObjectAnimator animatorSlideMenu = ObjectAnimator.ofFloat(slideMenuContainer,"x",slideMenuPosition);
        ObjectAnimator animatorMenuButton = ObjectAnimator.ofFloat(menu,"x",menuButtonPosition);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(animatorSlideMenu, animatorMenuButton);
        animSet.setDuration(600);
        animSet.start();

        contenedorExitMenu.setVisibility(View.VISIBLE);
        contenedorExitMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSlideMenu();
            }
        });

        //contenedorActionbarOption.removeAllViews();
        contenedorMenuBar.removeAllViews();

        isSlideMenuOpen = true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void hideSlideMenu3(final LiveSM live){

        float slideMenuPosition;
        float menuButtonPosition;

        slideMenuPosition = -290.0f;
        menuButtonPosition = 20.0f;

        //contenedorActionbarOption.removeAllViews();
        contenedorMenuBar.removeAllViews();

        isSlideMenuOpen = false;
        contenedorExitMenu.setVisibility(View.GONE);

        View slideMenuContainer = findViewById(R.id.menu_principal);

        ObjectAnimator animatorSlideMenu = ObjectAnimator.ofFloat(slideMenuContainer,"x",slideMenuPosition);
        ObjectAnimator animatorMenuButton = ObjectAnimator.ofFloat(menu,"x",menuButtonPosition);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(600);
        animSet.playTogether(animatorSlideMenu, animatorMenuButton);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                loading();
                displayLive(live.liveStreamSchedule,1,live);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animSet.start();

    }
    private void displayLive(final LiveStreamSchedule live, final int index,final LiveSM liveSM) {

        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.issueTokenForLive(live.getStream().getLiveStreamId(), new ServiceManager.ServiceManagerHandler<TokenIssue>() {
            @Override
            public void loaded(TokenIssue data) {

                if (data.getStatus().equalsIgnoreCase("OK")) {

                    String url = String.format("https://mdstrm.com/live-stream-playlist/%s.m3u8?access_token=%s", live.getStream().getLiveStreamId(), data.getAccessToken());
                    liveSM.url = url;
                    nextActivity(liveSM);
                }
                else if(data.getStatus().equalsIgnoreCase("ERROR")){
                    DialogError messageDialog = new DialogError("En este momento no se puede acceder a este contenido");
                    messageDialog.show(getSupportFragmentManager(), "");
                    borraLoading();

                }else{
                    DialogError messageDialog = new DialogError("En este momento no se puede acceder a este contenido");
                    messageDialog.show(getSupportFragmentManager(), "");
                }
            }

            @Override
            public void error(String error) {
                Log.e("ERROR ddd","--> "+error);
                DialogError messageDialog = new DialogError("En este momento no se puede acceder a este contenido");
                messageDialog.show(getSupportFragmentManager(), "");
                borraLoading();
            }
        });
    }
    private void getLiveStreamShedule(){

        final ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.loadLiveStreamList(new ServiceManager.ServiceManagerHandler<LiveStream>() {
            @Override
            public void loaded(List<LiveStream> data) {
                liveStreams = data;

                for (LiveStream aData : data) {
                    serviceManager.loadLiveStreamSchedule(aData, new ServiceManager.ServiceManagerHandler<LiveStreamSchedule>() {
                        @Override
                        public void loaded(List<LiveStreamSchedule> data2) {
                            /*for(int j = 0; j < data2.size();j++)
                                liveStreamSchedules.add(data2.get(j));*/
                            liveStreamSchedules = data2;
                            createMenuLive();
                        }

                        @Override
                        public void error(String error) {
                            Log.e("Error Schedule", "--> " + error);
                        }
                    });
                }
                Collections.sort(liveStreamSchedules, new Comparator<LiveStreamSchedule>() {
                    @Override
                    public int compare(LiveStreamSchedule lhs, LiveStreamSchedule rhs) {
                        if (lhs.getStartDate().getTime() > rhs.getStartDate().getTime()) {
                            return 1;
                        } else if (lhs.getStartDate().getTime() < rhs.getStartDate().getTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
            }

            @Override
            public void error(String error) {
                Log.e("Error", "Live SM");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //uiHelper.onActivityResult(requestCode, resultCode, data);
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //uiHelper.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    private void nextActivity(final LiveSM liveSM){

        RelativeLayout r = (RelativeLayout) findViewById(R.id.view_parent);
        Bitmap screenShot = ScreenShot.takeScreenshot(r);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        screenShot.compress(Bitmap.CompressFormat.JPEG, 75, stream);
        byte[] byteArray = stream.toByteArray();

        try {
            final String filename = getCacheDir()
                    + File.separator + System.currentTimeMillis() + ".jpg";

            File f = new File(filename);
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(byteArray);
            fo.close();

            runOnUiThread(new Runnable() {

                public void run() {
                    Intent i = new Intent(ProgramationActivity.this, PlayerActivity.class);
                    i.putExtra("background", filename);
                    i.putExtra("live", liveSM);
                    startActivity(i);
                }
            });
            overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
            borraLoading();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error catch","-->"+e.getMessage());
        }
    }
    private List<LiveStreamSchedule> countMatchLive() {
        List<LiveStreamSchedule> list = new ArrayList<LiveStreamSchedule>();
        Long now = new Date().getTime();
        for (LiveStreamSchedule lss : liveStreamSchedules) {
            if (lss.getStartDate().getTime() < now && lss.getEndDate().getTime() > now) {
                list.add(lss);
            }
        }
        return list;
    }
    static{
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
