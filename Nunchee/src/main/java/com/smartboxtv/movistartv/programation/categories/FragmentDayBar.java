package com.smartboxtv.movistartv.programation.categories;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.programation.adapters.AdapterBarDay;
import com.smartboxtv.movistartv.programation.customize.HorizontalScrollViewBarDays;
import com.smartboxtv.movistartv.programation.delegates.CategoryDelegateGetDate;
import com.smartboxtv.movistartv.programation.delegates.HoraryDelegate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Esteban- on 20-04-14.
 */
public class FragmentDayBar extends Fragment {

    private CategoryDelegateGetDate delegateGetDate;
    private HorizontalScrollViewBarDays horizontalScrollView;
    private HoraryDelegate horaryDelegate;

    private final List<Date> days = new ArrayList<Date>();

    private int compensacion;
    private int delta;

    private final long FIVE_MINUTES = 300000;
    private final long THIRTY_MINUTES = FIVE_MINUTES * 6;
    private final long ONE_HOUR = THIRTY_MINUTES * 2;
    private final int DAY = 288;
    private final int MARGIN = 1;

    private Date dateNow;
    private long longActual = new Date().getTime();
    private Position firstPosition;
    private Position actualPosition;
    private Position beginPosition;

    private Animation mueveDerecha;
    private Animation mueveIzquierda;
    private TextView hora;
    private ImageView ahora;
    private ImageView backDate;
    private boolean init = false;
    private boolean visible = false;

    // Horry
    public Button loadMore;
    public Date LIMIT_TOP;
    public Date LIMIT_BOTTOM;
    public Date FIRST_LIMIT_TOP;
    public Date FIRST_LIMIT_BOTTOM;
    public boolean scroll = false;
    private boolean isAnimation = true;
    private AnimatorSet set;
    public FragmentDayBar() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View  rootView = inflater.inflate(R.layout.category_fragment_bar_day, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.grilla_categoria_dias);
        horizontalScrollView = (HorizontalScrollViewBarDays) rootView.findViewById(R.id.categoria_scroll_horizontal);

        backDate = (ImageView) rootView.findViewById(R.id.imagen_hora);
        ahora = (ImageView)rootView.findViewById(R.id.btn_categoria_ahora);
        ahora.setVisibility(View.GONE); visible = false;
        loadMore = (Button) getActivity().findViewById(R.id.btn_more);

        //ImageView imageHorario = (ImageView) rootView.findViewById(R.id.imagen_hora);
        hora = (TextView) rootView.findViewById(R.id.texto_hora);

        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        aproximaFecha();
        dateNow = new Date(longActual);
        LIMIT_TOP = new Date(longActual+ 21600000);
        determinaFechaActual();

        AdapterBarDay adapter = new AdapterBarDay(getActivity(), days);

        gridView.setNumColumns(days.size());
        gridView.setAdapter(adapter);

        delta = UserPreference.getWIDTH_SCREEN(getActivity())/2;
        hora.setTypeface(bold);
        mueveDerecha = AnimationUtils.loadAnimation(getActivity(), R.anim.derecha_ahora);
        mueveIzquierda = AnimationUtils.loadAnimation(getActivity(), R.anim.derecha_out_ahora);

        firstPosition = new Position(0,days.get(0).getTime());
        actualPosition = initialPosition();
        beginPosition = actualPosition;

        imprimeFechaInicial();

        Log.e("Actual posicion DP",""+actualPosition.getDp());
        Log.e("Actual posicion Long",""+actualPosition.getnFecha());
        Log.e("Fecha actual ",""+new Date (longActual));

        ahora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actualPosition = initialPosition();
                horizontalScrollView.scrollTo(actualPosition.getDp(),0);
                imprimeFecha(longActual);
                mueveScroll(actualPosition .getDp());

                if(delegateGetDate != null){ // categoria
                    delegateGetDate.getDate(new Date(actualPosition.getnFecha()));
                }
                if(horaryDelegate != null){ // horario

                    Date d = new Date(longActual);
                    horaryDelegate.loadMoreProgramation(false,true, scroll, d);

                }
                ahora.setVisibility(View.GONE);

                visible = false;
            }
        });
        HorizontalScrollViewBarDays.OnScrollChangedListener onScrollChangedListener = new HorizontalScrollViewBarDays.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(HorizontalScrollViewBarDays who, int currHorizontal, int currVertical, int prevHorizontal, int prevVertical,ScrollState newState) {
                long delay;
                set = new AnimatorSet();
                switch (newState){

                    case STARTED:
                                    break;

                    case MOVING:    delay =  Math.abs((currHorizontal + delta) * FIVE_MINUTES - dateNow.getTime());
                                    if(delay > ONE_HOUR && !visible){
                                        ahora.setVisibility(View.VISIBLE);
                                        ahora.startAnimation(mueveDerecha);
                                        visible = true;
                                    }
                                    else if(delay < ONE_HOUR && visible){

                                        ahora.setVisibility(View.GONE);
                                        visible = false;
                                    }

                                    horaryDelegate.hiddeButtons();
                                    determinaCompensacion(currHorizontal);
                                    actualizaPosicion(currHorizontal, (firstPosition.getnFecha()) + ((currHorizontal - compensacion) * FIVE_MINUTES) + (FIVE_MINUTES * (delta - 20)));

                                    imprimeFecha();
                                    ahora.setVisibility(View.VISIBLE);
                                    if(isAnimation){
                                        isAnimation = false;
                                        set.playTogether(
                                            ObjectAnimator.ofFloat(backDate, "scaleX", 1, 1.5f),
                                            ObjectAnimator.ofFloat(backDate, "scaleY", 1, 1.05f),
                                            ObjectAnimator.ofFloat(hora, "scaleX", 1, 1.5f),
                                            ObjectAnimator.ofFloat(hora, "scaleY", 1, 1.5f)
                                        );
                                        set.setDuration(500).start();
                                    }

                                    break;

                    case STOPPED:   delay =  Math.abs((currHorizontal + delta)* FIVE_MINUTES - dateNow.getTime());

                                    if(delay > ONE_HOUR && !visible){
                                        ahora.setVisibility(View.VISIBLE);
                                        ahora.startAnimation(mueveDerecha);
                                        visible = true;
                                    }
                                    else if(delay < ONE_HOUR && visible){
                                        ahora.setVisibility(View.GONE);
                                        visible = false;
                                    }
                                    determinaCompensacion(currHorizontal);
                                    horaryDelegate.hiddeButtons();
                                    actualizaPosicion(currHorizontal,(firstPosition.getnFecha())+((currHorizontal - compensacion)* FIVE_MINUTES)+(FIVE_MINUTES * (delta - 20)));
                                    imprimeFecha();


                                    break;
                }
            }

            @Override
            public void onScrollStateChanged(HorizontalScrollViewBarDays who, ScrollState newState) {

                switch (newState){

                    case STARTED:   /*set.playTogether(
                                        ObjectAnimator.ofFloat(backDate, "scaleX", 1, 1.5f),
                                        ObjectAnimator.ofFloat(backDate, "scaleY", 1, 1.05f),
                                        ObjectAnimator.ofFloat(hora, "scaleX", 1, 1.5f),
                                        ObjectAnimator.ofFloat(hora, "scaleY", 1, 1.5f)
                                    );
                                    set.setDuration(500).start();*/
                                    break;

                    case MOVING:
                                    break;
                    case STOPPED:
                                    if(delegateGetDate != null){// Categoria
                                        delegateGetDate.getDate(new Date(actualPosition.getnFecha()));
                                    }

                                    if(horaryDelegate != null){// horario

                                        Date d = new Date(actualPosition.getnFecha());
                                        if(d.getTime() >= LIMIT_BOTTOM.getTime() && d.getTime() <= LIMIT_TOP.getTime()){
                                            horaryDelegate.loadMoreProgramation(true,true,false,d);
                                            scroll = false;
                                        }
                                        else{
                                           horaryDelegate.loadMoreProgramation(true,true,true,d);
                                           scroll = true;
                                        }
                                    }
                                    isAnimation = true;
                                    /*set.playTogether(

                                            ObjectAnimator.ofFloat(backDate, "scaleX", 1.5f, 1),
                                            ObjectAnimator.ofFloat(backDate, "scaleY", 1.05f, 1),
                                            ObjectAnimator.ofFloat(hora, "scaleX", 1.5f, 1),
                                            ObjectAnimator.ofFloat(hora, "scaleY", 1.5f, 1)
                                    );
                                    set.setDuration(500).start();*/
                                    break;
                }
            }
        };

        horizontalScrollView.setmOnScrollChangedListener(onScrollChangedListener);
        mueveScroll(actualPosition.getDp());
        return rootView;
    }

    public  void showButtonNow(boolean show){
        if(show){
            if(ahora.getVisibility() == View.GONE){
                ahora.setVisibility(View.VISIBLE);
                ahora.startAnimation(mueveDerecha);
                visible = true;
            }
        }
        else{

            ahora.setVisibility(View.GONE);
            visible = false;
        }
    }

    public Position initialPosition(){
        int HOUR = 12;
        int ORIGEN = 2 * (DAY + MARGIN);
        int dpInicial = aproximaDp((buscaPosicionHora(new Date(longActual)) * HOUR) + (20) + ORIGEN - delta);                                      // Cambiar a Dinamico
        return new Position(dpInicial, longActual - ((delta * FIVE_MINUTES)));
    }

    public void actualizaPosicion(int a, long b){
        actualPosition.setDp(aproximaDp(a));
        actualPosition.setnFecha(b);
    }
    public void actualizaPosicionBarra(int a, long b){

        actualizaPosicion((aproximaDp(horizontalScrollView.getxScroll() + a)), b);
        horizontalScrollView.smoothScrollBy(aproximaDp(a),0);
    }
    public void buscaPosicion(Date d){

        Log.e("Fecha nuevo ",""+new Date(d.getTime()));
        long old = longActual;
        long actual = d.getTime();
        int diferencia = (int) ((actual- old) / 1800000);

        Log.e("diferenciaa",""+diferencia);

        int delta = diferencia * 6;


        Log.e("dp actual ",""+horizontalScrollView.getxScroll());
        Log.e("dp nuevo ",""+(horizontalScrollView.getxScroll()+ delta));
        Log.e("Fecha actual ",""+new Date (old));
        Log.e("Fecha nuevo ",""+new Date(d.getTime()));

        actualizaPosicionBarra(aproximaDp( delta), d.getTime());


    }

    public void imprimeFecha(){ // Imprime fecha desde el listener

        //Animation zoom = AnimationUtils.loadAnimation( getActivity(), R.anim.zoom_out);
        //Animation zoom2 = AnimationUtils.loadAnimation( getActivity(), R.anim.zoom_out_2);
        //hora.startAnimation(zoom);
        //backDate.startAnimation(zoom2);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        hora.setText(format.format(actualPosition.getnFecha()));


    }
    public void imprimeFecha(long l){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        hora.setText(format.format(l));

    }
    public void imprimeFechaInicial(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        hora.setText(format.format(longActual));
    }
    public void mueveScroll(int l){
        horizontalScrollView.setxScroll(l);
    }

    public void aproximaFecha(){
        long residuo = longActual % (FIVE_MINUTES * 6);
        longActual = longActual - residuo;
    }

    public int buscaPosicionHora(Date d){
        return d.getHours();
    }
    public void determinaCompensacion(int l){

        if( l > 577 ){
             compensacion    =   (( l / DAY )* MARGIN);
        }
        else{
            compensacion    =   2;
        }
    }
    public int aproximaDp(int old_dp){

        int residuo = old_dp % 6;
        int dp_aproximado = old_dp - residuo;
        Log.e("de ",""+old_dp);
        Log.e("a ",""+dp_aproximado);
        return dp_aproximado;
    }

    public void setHoraryDelegate(HoraryDelegate horaryDelegate) {
        this.horaryDelegate = horaryDelegate;
    }

    public void setDelegateGetDate(CategoryDelegateGetDate delegateGetDate) {
        this.delegateGetDate = delegateGetDate;
    }

    public void setDateLastProgram(Date dateLastProgram) {
        Date dateLastProgram1 = dateLastProgram;
    }

    public void determinaFechaActual(){

        Date day0;
        Date day1;
        Date day2;
        Date day3;
        Date day4;
        Date day5;
        Date day6;
        Date day7;
        Date day8;
        Date day9;
        Date day10;
        Date day11;
        Date day12;

        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, -2);
        day0 = c.getTime();
        day0.setHours(0);
        day0.setSeconds(0);
        day0.setMinutes(0);

        c.add(Calendar.DATE, 1);
        day1 = c.getTime();

        c.add(Calendar.DATE, 2);
        day2 = c.getTime();

        c.add(Calendar.DATE, 1);
        day3 = c.getTime();

        c.add(Calendar.DATE, 1);
        day4 = c.getTime();

        c.add(Calendar.DATE, 1);
        day5 = c.getTime();

        c.add(Calendar.DATE, 1);
        day6 = c.getTime();

        c.add(Calendar.DATE, 1);
        day7 = c.getTime();

        c.add(Calendar.DATE, 1);
        day8 = c.getTime();

        c.add(Calendar.DATE, 1);
        day9 = c.getTime();

        c.add(Calendar.DATE, 1);
        day10 = c.getTime();

        c.add(Calendar.DATE, 1);
        day11 = c.getTime();

        c.add(Calendar.DATE, 1);
        day12 = c.getTime();

        days.add(0,day0);
        days.add(1,day1);
        days.add(2, dateNow);
        days.add(3,day2);
        days.add(4,day3);
        days.add(5,day4);
        days.add(6,day5);
        days.add(7,day6);
        days.add(8,day7);
        days.add(9,day8);
        days.add(10,day9);
        days.add(11,day10);
        days.add(12,day11);
        days.add(13,day12);
    }
}
