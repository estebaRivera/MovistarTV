package com.smartboxtv.movistartv.programation.delegates;

import java.util.Date;

/**
 * Created by Esteban- on 15-05-14.
 */
public abstract class HoraryDelegate {                                                              //Clase para implementar en la barra de dia

    public abstract void loadMoreProgramation(boolean buttonNow, boolean right, boolean morePrograms,Date d);          // Determina si carga mas programas o actualiza posición

    public abstract  void updateFlag(boolean b);                                                    // Ni idea xdd

    public abstract  void hiddeButtons();

}
