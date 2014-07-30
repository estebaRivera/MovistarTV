package com.smartboxtv.movistartv.programation.categories;

/**
 * Created by Esteban- on 20-04-14.
 */
public class Position {

    private long longDate;
    private int dp;


    public Position(int dp, long nFecha){
        this.dp = dp;
        this.longDate = nFecha;
        aproximaFecha();
    }

    public long getnFecha() {
        return longDate;
    }

    public void setnFecha(long nFecha) {
        this.longDate= nFecha;
        aproximaFecha();
    }

    public int getDp() {
        return dp;
    }

    public void setDp(int dp) {
        this.dp = dp;
    }

    public void aproximaFecha(){
        long residuo = longDate % (300000*6);
        longDate = longDate - residuo;
    }

}
