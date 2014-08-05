package com.smartboxtv.movistartv.programation.horary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.clean.DataClean;
import com.smartboxtv.movistartv.programation.categories.FragmentDayBar;
import com.smartboxtv.movistartv.programation.delegates.BarDayDelegate;
import com.smartboxtv.movistartv.programation.delegates.HoraryDelegate;

import java.util.Date;

/**
 * Created by Esteban- on 20-04-14.
 */
public class HoraryFragment extends Fragment {

    private final HoraryFragmentPrograms fragmentPrograms = new HoraryFragmentPrograms();
    private final FragmentDayBar fragmentDayBar = new FragmentDayBar();
    //private Date temprano;
    private Date dateLastProgram;
    //private Date actual;
    private boolean activeButtonNow = true;
    private boolean moveToOrigen = false;

    private Date MAX_LIMIT;

    public HoraryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.horary_fragment, container, false);
        DataClean.garbageCollector("Horary Fragment");

        HoraryDelegate horaryDelegate = new HoraryDelegate() {

            @Override
            public void loadMoreProgramation(boolean buttonNow, boolean right, boolean morePrograms , Date d) {         // Determina si Cargar mas programas o hacer Scroll

                fragmentDayBar.LIMIT_TOP = fragmentPrograms.LIMIT_TOP;                              // Actualiza los limites
                fragmentDayBar.LIMIT_BOTTOM = fragmentPrograms.LIMIT_BOTTOM;

                if(buttonNow){
                    fragmentPrograms.hiddeButton();
                    fragmentPrograms.clean();
                    fragmentPrograms.loadProgramation(d);
                    fragmentPrograms.hideButton = true;
                    fragmentDayBar.LIMIT_TOP = fragmentPrograms.LIMIT_TOP;
                }
                else{
                    if(right){

                        if(morePrograms){
                            fragmentPrograms.hiddeButton();
                            fragmentPrograms.clean();
                            fragmentPrograms.loadProgramation(d);
                            fragmentPrograms.hideButton = true;
                            //fragmentPrograms.loadProgramationSM(d);
                            fragmentDayBar.LIMIT_TOP = fragmentPrograms.LIMIT_TOP;
                            Log.e(" More Program 1", "1");
                        }
                        else{
                            fragmentPrograms.hiddeButton();
                            fragmentPrograms.actualizaPosicion(d,false,false);
                            Log.e(" More Program 2", "2");
                        }
                    }
                    else{
                        fragmentPrograms.hiddeButton();
                        fragmentPrograms.actualizaPosicion(d,false,false);
                        Log.e("More program 5", "5");
                    }
                }

            }

            @Override
            public void updateFlag(boolean b) {                                                     // Ni idea xdd
                activeButtonNow = b;
            }

            @Override
            public void hiddeButtons() {
                fragmentPrograms.hiddeButton();
            }
        };

        fragmentDayBar.setHoraryDelegate(horaryDelegate);

        BarDayDelegate barDayDelegate = new BarDayDelegate() {                                      // Delegado correspondiente a la Barra de Día

            @Override
            public void updateDayBar(Date date, boolean right) {

                if(fragmentPrograms.getDateLastProgram() != null)                                   // Sincroniza el limite del horario para la barra y el contenedor de programas
                    dateLastProgram = fragmentPrograms.getDateLastProgram();

                fragmentDayBar.LIMIT_TOP = fragmentPrograms.LIMIT_TOP;

                fragmentDayBar.setDateLastProgram(dateLastProgram);

                if(right){                                                                          //Determina si la barra se mueve hacia la izquierda o derecha según el parametro
                    fragmentDayBar.actualizaPosicionBarra(6, date.getTime());
                    fragmentPrograms.hiddeButton();
                }
                else{
                    fragmentDayBar.actualizaPosicionBarra(-6, date.getTime());
                    fragmentPrograms.hiddeButton();
                }
                fragmentDayBar.imprimeFecha();
            }

            @Override
            public void showButtonNow(boolean show) {                             //Determina si muestra el boton de  "AHORA"
                fragmentDayBar.showButtonNow(show);
                //activeButtonNow = isActive;
            }

            @Override
            public void updateLimit(Date limitTop, Date limitBottom, Date firstTop, Date firstBottom) {
                fragmentDayBar.LIMIT_TOP = limitTop;
                fragmentDayBar.LIMIT_BOTTOM = limitBottom;
                fragmentDayBar.FIRST_LIMIT_TOP = firstTop;
                fragmentDayBar.FIRST_LIMIT_BOTTOM = firstBottom;
            }


            @Override
            public void updateFlag(boolean b) {                                                     // Actualiza variable que determina scroll o carga
               fragmentDayBar.scroll = b;
            }


            @Override
            public void updatePositionBar(Date d) {
                fragmentDayBar.buscaPosicion(d);
                fragmentDayBar.imprimeFecha();
            }

            @Override
            public void hiddeChargeLess() {

            }


        };

        fragmentPrograms.setBarDayDelegate(barDayDelegate);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.add(R.id.horario_programas,fragmentPrograms );
        ft.add(R.id.horario_dias, fragmentDayBar);

        ft.commit();

        return rootView;
    }

    @Override
    public void onDetach() {
        fragmentDayBar.onDetach();
        fragmentPrograms.onDetach();
        super.onDetach();
    }
}
