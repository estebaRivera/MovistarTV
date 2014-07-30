package com.smartboxtv.movistartv.programation.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.smartboxtv.movistartv.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Esteban- on 20-04-14.
 */
public class AdapterBarDay extends ArrayAdapter<Date> {

    private List<Date> days = new ArrayList<Date>();
    private final SimpleDateFormat format = new SimpleDateFormat("EEEE dd");

    public AdapterBarDay(Context context, List<Date> listDays) {
        super(context, R.layout.category_day, listDays);
        days = listDays;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;
        ViewHolder holder = null;

        if(rootView == null){

            LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView  = inflater.inflate(R.layout.category_day, parent, false);

            holder = new ViewHolder();
            holder.viewDay = (Button) (rootView != null ? rootView.findViewById(R.id.btn_categoria_dia) : null);
            rootView.setTag(holder);
        }

        else{
            holder = (ViewHolder) rootView.getTag();
        }

        Typeface light = Typeface.createFromAsset(getContext().getAssets(), "fonts/SegoeWP-Light.ttf");

        holder.viewDay.setTypeface(light);
        holder.viewDay.setText(capitalize(format.format(days.get(position))));

        return rootView;
    }
    private String capitalize(String line){
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    static class ViewHolder{
        Button viewDay;
    }
}
