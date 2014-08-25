package com.smartboxtv.movistartv.programation.preview;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.Tweets;
import com.smartboxtv.movistartv.data.models.UserTwitterJSON;
import com.smartboxtv.movistartv.data.modelssm.TweetSM;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.fragments.NUNCHEE;
import com.smartboxtv.movistartv.programation.menu.DialogError;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.services.ServiceManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Esteban- on 06-05-14.
 */
public class TwMaxFragment extends Fragment {

    private Button btnTimeline;
    private Button btnRelacionado;
    private Button btnTwittear;
    private Button btnLogin;

    private TextView titulo2;
    private EditText texto;

    private View rootView;
    private View tws;
    private View cargando;

    private final Program programa;
    private LinearLayout contenedorTws;
    private LayoutInflater inflaterPrivate;

    private int cuentaClick = 0;
    private boolean isTimeline = false;
    private RelativeLayout contenedorLoading;
    private RelativeLayout containerLogin;
    private List<UserTwitterJSON> listTimeLine = new ArrayList<UserTwitterJSON>();
    private List<Tweets> listaRelacionado = new ArrayList<Tweets>();
    private List<TweetSM> listRelacionadoSM = new ArrayList<TweetSM>();
    private static final int CONFIRM_ALERT = 2;

    private  boolean login = false;

    public TwMaxFragment(Program program) {
        this.programa = program;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.preview_fg_tw_max,container,false);
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");

        inflaterPrivate = inflater;     listTimeLine = null;
        contenedorLoading = (RelativeLayout) rootView.findViewById(R.id.contenedor_loading);
        contenedorTws = (LinearLayout) rootView.findViewById(R.id.tws_lista);

        TextView titulo = (TextView) rootView.findViewById(R.id.tw_titulo);
        TextView textLogin = (TextView) rootView.findViewById(R.id.login_twitter_text);

        containerLogin = (RelativeLayout) rootView.findViewById(R.id.login_twitter_container);
        titulo2 = (TextView) rootView.findViewById(R.id.tw_titulo_2);
        btnTimeline = (Button) rootView.findViewById(R.id.tw_btn_timeline);
        btnTwittear = (Button) rootView.findViewById(R.id.tw_twittear);
        btnLogin = (Button) rootView.findViewById(R.id.login_twiter_button);
        texto = (EditText) rootView.findViewById(R.id.tw_tws);

        btnTimeline.setBackgroundResource(R.drawable.evento_tw);
        btnRelacionado = (Button) rootView.findViewById(R.id.tw_btn_relacionado);
        btnRelacionado.setBackgroundResource(R.drawable.evento_tw);

        btnRelacionado.setSelected(true);
        btnTimeline.setSelected(false);

        titulo.setTypeface(normal);
        textLogin.setTypeface(normal);
        titulo2.setTypeface(normal);

        btnTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(login == false)
                    containerLogin.setVisibility(View.VISIBLE);

                btnTimeline.setSelected(true);
                btnRelacionado.setSelected(false);

                ViewGroup.LayoutParams timelineParams = btnTimeline.getLayoutParams();

                timelineParams.height = 40;
                btnTimeline.setLayoutParams(timelineParams);

                ViewGroup.LayoutParams relacionadoParams = btnRelacionado.getLayoutParams();
                relacionadoParams.height  = 35;

                btnRelacionado.setLayoutParams(relacionadoParams);
                titulo2.setText("#TIMELINE");

                contenedorTws.removeAllViews();

                if (login == true) {

                    loading();
                     if (btnTwittear.getVisibility() == View.GONE) {
                            btnTwittear.setVisibility(View.VISIBLE);
                        }
                        DataLoader dataLoader = new DataLoader(getActivity());
                        dataLoader.getTimeLine(new DataLoader.DataLoadedHandler<UserTwitterJSON>() {

                            @Override
                            public void loaded(List<UserTwitterJSON> data) {

                                super.loaded(data);
                                listTimeLine = data;

                                cargaTwsTimeLine();
                                borraLoading();
                            }

                            @Override
                            public void error(String error) {
                                super.error(error);
                                Log.e("Cargar ", "" + error);
                            }
                        });
                }
            }
        });
        btnRelacionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //loading();
                btnTimeline.setSelected(false);
                btnRelacionado.setSelected(true);
                containerLogin.setVisibility(View.GONE);

                int width_screen = UserPreference.getWIDTH_SCREEN(getActivity());

                ViewGroup.LayoutParams timelineParams = btnTimeline.getLayoutParams();

                timelineParams.height = 35;
                btnTimeline.setLayoutParams(timelineParams);
                ViewGroup.LayoutParams relacionadoParams = btnRelacionado.getLayoutParams();

                timelineParams.height = 40;
                btnRelacionado.setLayoutParams(relacionadoParams);
                titulo2.setText("#RELACIONADO");
                contenedorTws.removeAllViews();

                if (btnTwittear.getVisibility() == View.VISIBLE) {
                    btnTwittear.setVisibility(View.GONE);
                }
                if (texto.getVisibility() == View.VISIBLE) {
                    texto.setVisibility(View.GONE);
                }
                cargaRelacionadoSM();

            }
        });
        btnTwittear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cuentaClick++;
                if (texto.getVisibility() == View.GONE)
                    texto.setVisibility(View.VISIBLE);
                else
                    texto.setVisibility(View.GONE);

                String hashTag = programa.getHashtags();
                texto.setText("Estoy viendo " + programa.getTitle() + " en " + programa.getPChannel().getChannelCallLetter()
                        + " por #MovistarTv @MovistarTV " + hashTag);

                if (cuentaClick == 2) {
                    cuentaClick = 0;

                    Toast.makeText(getActivity(),"Publicando...",Toast.LENGTH_SHORT).show();
                    DataLoader dataLoader = new DataLoader(getActivity());
                    dataLoader.updateStatusTw(new DataLoader.DataLoadedHandler<String>() {
                        @Override
                        public void loaded(String data) {
                            ((NUNCHEE)getActivity().getApplication()).sendAnalitics("Twitter");
                            texto.setVisibility(View.GONE);
                            btnTwittear.setEnabled(false);
                        }

                        @Override
                        public void error(String error) {
                            Toast.makeText(getActivity(),"Ups! algo salió mal, intenta de nuevo",Toast.LENGTH_LONG).show();
                        }
                    }, texto.getText().toString());
                }
            }
        });

        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login = true;
                if (listTimeLine == null) {
                    loading();

                    containerLogin.setVisibility(View.GONE);

                    if (btnTwittear.getVisibility() == View.GONE) {
                        btnTwittear.setVisibility(View.VISIBLE);
                    }

                    DataLoader dataLoader = new DataLoader(getActivity());
                    dataLoader.getTimeLine(new DataLoader.DataLoadedHandler<UserTwitterJSON>() {

                        @Override
                        public void loaded(List<UserTwitterJSON> data) {

                            super.loaded(data);
                            listTimeLine = data;

                            cargaTwsTimeLine();
                            borraLoading();
                        }

                        @Override
                        public void error(String error) {
                            btnTwittear.setEnabled(false);
                            borraLoading();
                            DialogError dialogError = new DialogError("En este momento no es posible mostrar este contenido");
                            dialogError.show(getFragmentManager(), "");
                        }
                    });
                }
                else {
                    cargaTwsTimeLine();
                    borraLoading();
                }
            }
        });
        cargaRelacionadoSM();
        return rootView;
    }
    public void cargaTwsTimeLine(){

        AQuery aq = new AQuery(rootView);

        //Log.e("Carga Relacionado ","");

        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        if(listTimeLine.size() > 0){

            int i = 0 ;

            for (UserTwitterJSON aListTimeLine : listTimeLine) {

                if(i % 2 == 0){
                    tws = getLayoutInflater(null).inflate(R.layout.element_tweets, null);
                }
                else{
                    tws = getLayoutInflater(null).inflate(R.layout.element_tweets_impar, null);
                }

                ImageView imageView = (ImageView) tws.findViewById(R.id.foto_tw);
                TextView tw = (TextView) tws.findViewById(R.id.tw_texto);
                TextView nombre = (TextView) tws.findViewById(R.id.tw_nombre);
                TextView usuario = (TextView) tws.findViewById(R.id.tw_nombre_usuario);
                TextView tiempo = (TextView) tws.findViewById(R.id.tiempo_twitter);

                // set Typeface
                nombre.setTypeface(bold);
                usuario.setTypeface(normal);
                tw.setTypeface(normal);
                tiempo.setVisibility(View.GONE);

                aq.id(imageView).image(aListTimeLine.getUsuario().getUrlImagen());
                tw.setText(aListTimeLine.getTexto());

                nombre.setText(aListTimeLine.getUsuario().getUsuario());
                usuario.setText("@" + aListTimeLine.getUsuario().getUsuario());
                contenedorTws.addView(tws);
                i++;
            }
        }
        else{
            borraLoading();
            DialogError dialogError = new DialogError("En este momento no es posible mostrar este contenido");
            dialogError.show(getFragmentManager(),"");
        }
    }
    public void cargaRelacionado(){

        AQuery aq = new AQuery(rootView);

        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        if(programa.getTweets().size() > 0){

            for(int i = 0 ;i < programa.getTweets().size();i++){

                if(i % 2 == 0){
                    tws = getLayoutInflater(null).inflate(R.layout.element_tweets, null);
                }
                else{
                    tws = getLayoutInflater(null).inflate(R.layout.element_tweets_impar, null);
                }

                ImageView imageView = (ImageView) tws.findViewById(R.id.foto_tw);
                TextView tw = (TextView) tws.findViewById(R.id.tw_texto);
                TextView nombre = (TextView) tws.findViewById(R.id.tw_nombre);
                TextView usuario = (TextView) tws.findViewById(R.id.tw_nombre_usuario);

                // set Typeface
                nombre.setTypeface(bold);
                usuario.setTypeface(normal);
                tw.setTypeface(normal);

                aq.id(imageView).image(programa.getTweets().get(i).getUrlImagen());
                tw.setText(programa.getTweets().get(i).getTw());
                nombre.setText(programa.getTweets().get(i).getNombre());
                usuario.setText("@" + programa.getTweets().get(i).getNombreUsuario());
                contenedorTws.addView(tws);
            }
        }
        else{
            //containerNoTw.setVisibility(View.VISIBLE);
        }
    }

    public void cargaRelacionadoSM(){

        final AQuery aq = new AQuery(rootView);
        final Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        final Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        loading();

        ServiceManager serviceManager = new ServiceManager(getActivity());
        serviceManager.getTweets( new ServiceManager.ServiceManagerHandler<TweetSM>(){
            @Override
            public void loaded(List<TweetSM> data) {
                for(int i = 0; i < data.size() ;i++){
                    if(i % 2 == 0){
                        tws = getLayoutInflater(null).inflate(R.layout.element_tweets, null);
                    }
                    else{
                        tws = getLayoutInflater(null).inflate(R.layout.element_tweets_impar, null);
                    }

                    ImageView imageView = (ImageView) tws.findViewById(R.id.foto_tw);
                    TextView tw = (TextView) tws.findViewById(R.id.tw_texto);
                    TextView nombre = (TextView) tws.findViewById(R.id.tw_nombre);
                    TextView usuario = (TextView) tws.findViewById(R.id.tw_nombre_usuario);
                    TextView tiempo = (TextView) tws.findViewById(R.id.tiempo_twitter);

                    // set Typeface
                    nombre.setTypeface(bold);
                    usuario.setTypeface(normal);
                    tw.setTypeface(normal);
                    tiempo.setTypeface(normal);

                    Date d = new Date();

                    DateFormat converter = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
                    Date datetw;
                    converter.setTimeZone(TimeZone.getTimeZone("GMT+4"));

                    datetw = new Date(data.get(i).date.getTime() - 14400000);

                    /*try {
                        datetw = converter.parse(converter.format(data.get(i).date.getTime()));
                    } catch (ParseException e) {
                        datetw = new Date(data.get(i).date.getTime() + 14400000);
                        e.printStackTrace();
                    }*/

                    long delta = d.getTime() - datetw.getTime();

                    int minutos = (int) delta / 60000;
                    int horas = minutos / 60;
                    int dia = horas / 24;

                    if(minutos < 60 ){
                        tiempo.setText("Hace "+minutos+" Minutos");
                    }
                    else if(horas > 0 && horas < 24){
                        tiempo.setText("Hace "+horas+" Horas");
                    }
                    else if(dia == 1){
                        tiempo.setText("Hace un día y "+(horas-24)+" Horas");
                    }
                    else if(dia > 1){
                        tiempo.setText("Hace "+dia+" días y "+(horas%24)+" Horas");
                    }

                    aq.id(imageView).image(data.get(i).getImage());
                    tw.setText(data.get(i).text);
                    nombre.setText(data.get(i).getScreenName());
                    usuario.setText("@" + data.get(i).getName());
                    contenedorTws.addView(tws);
                }

                borraLoading();
            }

            @Override
            public void error(String error) {
                borraLoading();
                DialogError dialogError = new DialogError("En este momento no es posible mostrar este contenido");
                dialogError.show(getFragmentManager(),"");
            }
        },programa.getHashtags(),"40",false);
    }

    public static void confirmationAlert(Context ctx, String title, String message,
                                         DialogInterface.OnClickListener callBack) {
        showAlertDialog(CONFIRM_ALERT, ctx, title, message, callBack, "OK");
    }
    public static void showAlertDialog(int alertType, Context ctx, String title, String message,
                                       DialogInterface.OnClickListener posCallback, String... buttonNames) {
        if ( title == null ) title = ctx.getResources().getString(R.string.app_name);
        if ( message == null ) message = "default message";

        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title)
                .setMessage(message)
                        // false = pressing back button won't dismiss this alert
                .setCancelable(false)
                        // icon on the left of title
                .setIcon(android.R.drawable.ic_dialog_alert);

        switch (alertType) {
            case CONFIRM_ALERT:
                builder.setPositiveButton(buttonNames[0], posCallback);
                break;
        }

        builder.setNegativeButton(buttonNames [buttonNames.length - 1], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    public void loading(){

        cargando = inflaterPrivate.inflate(R.layout.progress_dialog_pop_corn, null);
        ImageView popDerecha = (ImageView) cargando.findViewById(R.id.pop_corn_centro);

        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Animation animaPop = AnimationUtils.loadAnimation(getActivity(), R.anim.animacion_pop_hacia_derecha_centro);

        cargando.setLayoutParams(params);
        popDerecha.startAnimation(animaPop);
        contenedorLoading.addView(cargando);
        cargando.bringToFront();
        //Log.e("Loading","Loading");
        contenedorLoading.bringToFront();
        contenedorLoading.setEnabled(false);
    }

    public void borraLoading(){
        Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.deaparece);
        cargando.startAnimation(anim);
        contenedorLoading.removeView(cargando);
        contenedorLoading.setEnabled(true);
    }
}
