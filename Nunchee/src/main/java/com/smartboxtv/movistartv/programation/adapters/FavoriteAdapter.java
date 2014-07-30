package com.smartboxtv.movistartv.programation.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Program;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 20-04-14.
 */
public class FavoriteAdapter extends ArrayAdapter<List<Program>> {

    private final List<List<Program>> listFavorites = new ArrayList<List<Program>>();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

    public FavoriteAdapter(Context context, List<List<Program>> favorites) {
        super(context, R.layout.favorite_program_many,favorites);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(rootView == null){

            holder = new ViewHolder();
            if(listFavorites.get(position).size()>1){

                rootView = inflater.inflate(R.layout.favorite_program_many, null);

                holder.amount = (TextView) (rootView != null ? rootView.findViewById(R.id.favorito_texto_desplegable_cantidad) : null);
                holder.name = (TextView) rootView.findViewById(R.id.favorito_text_desplegable_nombre);
                holder.image = (ImageView) rootView.findViewById((R.id.favorito_imagen_deplegable));
                holder.list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e("CLICK", "segunda lista");

                    }
                });

            }
            else{

                rootView = inflater.inflate(R.layout.favorite_program_only, null);

                holder.channel = (TextView) (rootView != null ? rootView.findViewById(R.id.favorito_programa_unico_texto_canal) : null);
                holder.horary = (TextView) rootView.findViewById(R.id.favorito_programa_unico_texto_hora);
                holder.program = (TextView) rootView.findViewById(R.id.favorito_programa_unico_texto_nombre);
                holder.imageSecond = (ImageView) rootView.findViewById(R.id.favorito_programa_unico_imagen);
            }
            rootView.setTag(holder);
        }
        else{
            holder = (ViewHolder) rootView.getTag();
        }


        Typeface light = Typeface.createFromAsset(getContext().getAssets(), "fonts/SegoeWP-Light.ttf");
        Typeface bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(), "fonts/SegoeWP.ttf");


        if(listFavorites.get(position).size()>1){

            AQuery aq = new AQuery(rootView);

            holder.name.setText(listFavorites.get(position).get(0).getTitle());
            holder.amount.setText("" + listFavorites.get(position).size() + " Programas");

            if(listFavorites.get(position).get(0).getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE)!= null){
                aq.id(holder.image).image(listFavorites.get(position).get(0)
                        .getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE).getImagePath());
            }

            holder.name.setTypeface(bold);
            holder.amount.setTypeface(normal);
        }

        else{
            AQuery aq = new AQuery(rootView);

            if(listFavorites.get(position).get(0).getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE)!= null){

                aq.id(holder.imageSecond).image(listFavorites.get(position).get(0)
                        .getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE).getImagePath());
            }
            holder.channel.setTypeface(light);
            holder.program.setTypeface(bold);
            holder.horary.setTypeface(normal);

            holder.channel.setText(listFavorites.get(position).get(0).getPChannel().getChannelName());
            holder.horary.setText(simpleDateFormat.format(listFavorites.get(position).get(0).getStartDate())+" | "
                    +simpleDateFormat.format(listFavorites.get(position).get(0).getEndDate()));

            holder.program.setText(listFavorites.get(position).get(0).getTitle());

        }

        return rootView;
    }

    static class ViewHolder{
        // PRIMER IF
        TextView name;
        TextView amount;
        ListView list;
        ImageView image;

        //SEGUNDO IF
        TextView program;
        TextView channel;
        TextView horary;
        ImageView imageSecond;
    }
}
