package com.smartboxtv.movistartv.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;
import com.smartboxtv.movistartv.animation.ManagerAnimation;
import com.smartboxtv.movistartv.data.clean.DataClean;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.data.preference.UserPreferenceSM;
import com.smartboxtv.movistartv.delgates.RecommendedDelegate;
import com.smartboxtv.movistartv.fragments.RecommendedFragment;
import com.smartboxtv.movistartv.programation.menu.DialogError;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.services.ServiceManager;

import java.util.List;

/**
 * Created by Esteban- on 18-04-14.
 */
public class RecommendedActivity extends ActionBarActivity {

    private final RecommendedFragment[] listaFragmento = new RecommendedFragment[8];
    private List<Program> programas;
    private TextView textContinuar;
    private RelativeLayout skip;
    private RelativeLayout layer;
    private AQuery aq;
    private int indice;
    private final int MAX = 5;
    private int intentos = 0;
    private boolean toast = false;
    private boolean first = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommended_activity);

        DataClean.garbageCollector("Recommended Activity");
        beginFragment();
        loadRecommended();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Button saltar = (Button) findViewById(R.id.volver_recomendacion);
        final TextView nombre = (TextView) findViewById(R.id.nombre_contacto);
        layer  = (RelativeLayout) findViewById(R.id.exit);

        textContinuar = (TextView) findViewById(R.id.volver_text);
        skip = (RelativeLayout) findViewById(R.id.container_saltar);

        layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layer.setVisibility(View.GONE);
            }
        });

        Typeface normal = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Bold.ttf");
        textContinuar.setTypeface(bold);

        nombre.setText(UserPreference.getNombreFacebook(RecommendedActivity.this));
        nombre.setTypeface(normal);

        Animation animacion = AnimationUtils.loadAnimation(getApplication(), R.anim.animacion_sugeridos);
        Animation animacion2 = AnimationUtils.loadAnimation(getApplication(), R.anim.animacion_sugeridos_2);
        Animation animacion3 = AnimationUtils.loadAnimation(getApplication(), R.anim.animacion_sugeridos_3);
        Animation animacion4 = AnimationUtils.loadAnimation(getApplication(), R.anim.animacion_sugeridos_4);
        Animation animacion5 = AnimationUtils.loadAnimation(getApplication(), R.anim.animacion_sugeridos_5);
        Animation animacion6 = AnimationUtils.loadAnimation(getApplication(), R.anim.animacion_sugeridos_6);
        Animation animacion7 = AnimationUtils.loadAnimation(getApplication(), R.anim.animacion_sugeridos_7);
        Animation animacion8 = AnimationUtils.loadAnimation(getApplication(), R.anim.animacion_sugeridos_8);

        aq = new AQuery(this);
        aq.id(R.id.foto_perfil).image("http://graph.facebook.com/" + UserPreference.getIdFacebook(RecommendedActivity.this) + "/picture?type=normal");

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecommendedActivity.this, ProgramationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.animacion_abajo);
                finish();
            }
        });

        /*ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.removeFavorite(new ServiceManager.ServiceManagerHandler<String>() {
            @Override
            public void loaded(String data) {
                super.loaded(data);
                Log.e("DATA LOAD","LOAD");
                Log.e("DATA LOAD",data);
            }

            @Override
            public void error(String error) {
                super.error(error);
                Log.e("DATA LOAD","ERROR");
            }
        }, "", "");*/

        Log.e("id Usuario Recommendation", UserPreferenceSM.getIdNunchee(getApplication()));

        RecommendedDelegate delegate = new RecommendedDelegate() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void like(Program p, RecommendedFragment fragment) {
                if(!first){
                    first = false;
                    ManagerAnimation.alpha(skip);
                    textContinuar.setText("Continuar");

                }
                Program newProgram;
                do{
                    newProgram = newProgram();
                    if(newProgram!= null)
                        fragment.setData(newProgram);
                }while(newProgram == null && intentos < MAX);

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void dislike(Program p, RecommendedFragment fragment) {
                if(!first){
                    first = false;
                    ManagerAnimation.alpha(skip);
                    textContinuar.setText("Continuar");
                }
                Program newProgram;
                do{
                    newProgram = newProgram();
                    if(newProgram!= null)
                        fragment.setData(newProgram);
                }while(newProgram == null && intentos < MAX);
            }

        };

        listaFragmento[0].setDelegate(delegate);
        listaFragmento[1].setDelegate(delegate);
        listaFragmento[2].setDelegate(delegate);
        listaFragmento[3].setDelegate(delegate);
        listaFragmento[4].setDelegate(delegate);
        listaFragmento[5].setDelegate(delegate);
        listaFragmento[6].setDelegate(delegate);
        listaFragmento[7].setDelegate(delegate);

        assert animacion != null;
        listaFragmento[0].getView().startAnimation(animacion);
        assert animacion2 != null;
        listaFragmento[1].getView().startAnimation(animacion2);
        assert animacion3 != null;
        listaFragmento[2].getView().startAnimation(animacion3);
        assert animacion4 != null;
        listaFragmento[3].getView().startAnimation(animacion4);
        assert animacion5 != null;
        listaFragmento[4].getView().startAnimation(animacion5);
        assert animacion6 != null;
        listaFragmento[5].getView().startAnimation(animacion6);
        assert animacion7 != null;
        listaFragmento[6].getView().startAnimation(animacion7);
        assert animacion8 != null;
        listaFragmento[7].getView().startAnimation(animacion8);
    }
    public void loadRecommended(){

        //if(((NUNCHEE) getApplicationContext()).CONNECT_SERVICES_PYTHON == false){
        if(false){
            DataLoader dataLoader = new DataLoader(this);
            dataLoader.programRandom(new DataLoader.DataLoadedHandler<Program>() {

                @Override
                public void loaded(List<Program> data) {
                    Log.e("Numero de recomendado","--> "+data.size());

                    programas = data;
                    for(int i = 0; i < 8 ;i++){
                        listaFragmento[i].setData(data.get(i));
                        indice = i;
                    }
                }

                @Override
                public void error(String error) {
                    Log.e("Recommend Activity error", " LoadDataMore error --> " + error);
                    clean();
                    DialogError dialogError = new DialogError("Por el momento no hay recomendaciones");
                    dialogError.show(getSupportFragmentManager(),"");
                    super.error(error);
                }
            });
        }
        else{

            ServiceManager serviceManager  = new ServiceManager(this);
            serviceManager.getRecommendationRandom( new ServiceManager.ServiceManagerHandler<Program>(){
                @Override
                public void loaded(List<Program> data) {
                    Log.e("Numero de recomendado SM !!!!!!!","--> "+data.size());

                    programas = data;
                    for(int i = 0; i < 8 ;i++){
                        listaFragmento[i].setData(data.get(i));
                        indice = i;
                    }
                }
                @Override
                public void error(String error) {
                    Log.e("Recommend Activity error", " LoadDataMore error --> " + error);
                    clean();
                    DialogError dialogError = new DialogError("Por el momento no hay recomendaciones");
                    dialogError.show(getSupportFragmentManager(),"");
                    super.error(error);
                }
            },"","");
        }
    }
    public void loadMoreRecommended(){
        //if(((NUNCHEE) getApplicationContext()).CONNECT_SERVICES_PYTHON == false){
        if(false){
        DataLoader dataLoader = new DataLoader(this);
        dataLoader.programRandom(new DataLoader.DataLoadedHandler<Program>() {

            @Override
            public void loaded(List<Program> data) {
                Log.e("Numero de recomendado","--> "+data.size());
                for (Program aData : data) {
                    programas.add(aData);
                }
            }
            @Override
            public void error(String error) {
                Log.e("Recommend Activity error", " LoadDataMore error --> " + error);
                clean();
                DialogError dialogError = new DialogError("Por el momento no más hay recomendaciones");
                dialogError.show(getSupportFragmentManager(),"");
                super.error(error);
            }
        });
        }
        else{

            ServiceManager serviceManager  = new ServiceManager(this);
            serviceManager.getRecommendationRandom( new ServiceManager.ServiceManagerHandler<Program>(){
                @Override
                public void loaded(List<Program> data) {
                    Log.e("Numero de recomendado SM !!!!!!!","--> "+data.size());

                    for (Program aData : data) {
                        programas.add(aData);
                    }
                }
                @Override
                public void error(String error) {
                    Log.e("Recommend Activity error", " LoadDataMore error --> " + error);
                    clean();
                    DialogError dialogError = new DialogError("Por el momento no hay recomendaciones");
                    dialogError.show(getSupportFragmentManager(),"");
                    super.error(error);
                }
            },"","");
        }

    }
    public Program newProgram(){
        indice++;
        try{
            if(indice % 20 == 0)
                loadMoreRecommended();
            return programas.get(indice);
        }
        catch (IndexOutOfBoundsException e){
            ++intentos;
            loadMoreRecommended();
            if(!toast){
                toast = true;
                DataClean.garbageCollector("Login Activity");
            }
            return  null;
        }
    }
    public void beginFragment(){
        listaFragmento[0] = (RecommendedFragment) getSupportFragmentManager().findFragmentById(R.id.recomendacion_1);
        listaFragmento[1] = (RecommendedFragment) getSupportFragmentManager().findFragmentById(R.id.recomendacion_2);
        listaFragmento[2] = (RecommendedFragment) getSupportFragmentManager().findFragmentById(R.id.recomendacion_3);
        listaFragmento[3] = (RecommendedFragment) getSupportFragmentManager().findFragmentById(R.id.recomendacion_4);
        listaFragmento[4] = (RecommendedFragment) getSupportFragmentManager().findFragmentById(R.id.recomendacion_5);
        listaFragmento[5] = (RecommendedFragment) getSupportFragmentManager().findFragmentById(R.id.recomendacion_6);
        listaFragmento[6] = (RecommendedFragment) getSupportFragmentManager().findFragmentById(R.id.recomendacion_7);
        listaFragmento[7] = (RecommendedFragment) getSupportFragmentManager().findFragmentById(R.id.recomendacion_8);
    }

    public void clean(){
        /*TableLayout t = (TableLayout) findViewById(R.id.table_sugerencias);
        t.setVisibility(View.GONE);*/
       /* listaFragmento[0].gone();
        listaFragmento[1].gone();
        listaFragmento[2].gone();
        listaFragmento[3].gone();
        listaFragmento[4].gone();
        listaFragmento[5].gone();
        listaFragmento[6].gone();
        listaFragmento[7].gone();*/
        layer.setVisibility(View.GONE);
    }
    @Override
    protected void onDestroy() {

        this.aq.clear();
        this.listaFragmento[0].onDestroy();
        this.listaFragmento[1].onDestroy();
        this.listaFragmento[2].onDestroy();
        this.listaFragmento[3].onDestroy();
        this.listaFragmento[4].onDestroy();
        this.listaFragmento[5].onDestroy();
        this.listaFragmento[6].onDestroy();
        this.listaFragmento[7].onDestroy();
        BitmapAjaxCallback.clearCache();
        AQUtility.cleanCacheAsync(this);
        super.onDestroy();
    }
}
