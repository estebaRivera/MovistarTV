package com.smartboxtv.movistartv.programation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 20-04-14.
 */
public class FavoriteAdapterProgram extends ArrayAdapter<Program> {

    private List<Program> listFavorites = new ArrayList<Program>();


    public FavoriteAdapterProgram(Context context, List<Program> favorites) {

        super(context, R.layout.favorite_program_only,favorites);
            listFavorites= favorites;
     }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;
        ViewHolder holder;

        if(rootView == null){

            LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView = inflater.inflate(R.layout.favorite_program_ultimate,null);

            holder = new ViewHolder();
            holder.channel = (TextView) (rootView != null ? rootView.findViewById(R.id.favorito_texto_canal) : null);
            holder.horary = (TextView) rootView.findViewById(R.id.favorito_text_horario);
            holder.sinopsis = (TextView) rootView.findViewById(R.id.favorito_texto_sinopsis);

            rootView.setTag(holder);
        }
        else{
            holder = (ViewHolder)rootView.getTag();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        holder.channel.setText(listFavorites.get(position).getPChannel().getChannelName());
        holder.horary.setText(simpleDateFormat.format(listFavorites.get(position).getStartDate())+" | "
                + simpleDateFormat.format(listFavorites.get(position).getEndDate()));

        holder.sinopsis.setText(listFavorites.get(position).getEpisodeTitle());

        return rootView;
    }

    static class ViewHolder{

        TextView channel;
        TextView horary;
        TextView sinopsis;

    }
}
