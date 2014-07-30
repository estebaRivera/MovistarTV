package com.smartboxtv.movistartv.programation.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.delgates.FacebookLikeDelegate;
import com.smartboxtv.movistartv.services.DataLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Esteban- on 20-04-14.
 */
public class CategoryAdapterContainer  extends ArrayAdapter<Program> {

    private List<Program> lista = new ArrayList<Program>();
    private FacebookLikeDelegate facebookDelegate;
    private View item;
    private ViewHolder holder;
    private SimpleDateFormat format;
    private Typeface normal;
    private Typeface bold;
    //private int position;
    //private ViewGroup parent;
    private Program programa;

    private HashMap <String,Program> hashMap = new HashMap <String , Program>();


    public void setFacebookDelegate(FacebookLikeDelegate facebookDelegate) {
        this.facebookDelegate = facebookDelegate;
    }

    public CategoryAdapterContainer(Context context, List<Program> listaProgramas) {

        super(context, R.layout.category_program_container, listaProgramas);
        normal = Typeface.createFromAsset(getContext().getAssets(), "fonts/SegoeWP-Light.ttf");
        bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/SegoeWP-Bold.ttf");
        format = new SimpleDateFormat("HH:mm");
        lista = listaProgramas;
    }

   @Override
   public View getView(final int position, View convertView,ViewGroup parent) {

       holder = null;
       item = convertView;

       //this.position = position;
       Log.e("Category Position","--> "+position);
       //this.parent = parent;
       //setData();

       if (item == null) {

           LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           item = inflater.inflate(R.layout.category_program_container, parent, false);

           holder = new ViewHolder();
           //programa = getItem(position);

           holder.like = (Button) (item != null ? item.findViewById(R.id.categoria_action_like) : null);
           holder.horario = (TextView) item.findViewById(R.id.text_categoria_horario);
           holder.nombre = (TextView) item.findViewById(R.id.text_categoria_nombre);
           holder.canal = (TextView) item.findViewById(R.id.text_categoria_canal);
           holder.image = (ImageView) item.findViewById(R.id.foto_categoria);

           holder.horario.setTypeface(normal);
           holder.nombre.setTypeface(bold);
           holder.canal.setTypeface(normal);

           item.setTag(holder);

       }
       else{

           holder = (ViewHolder) item.getTag();
       }

       format.format(lista.get(position).getEndDate());

       holder.horario.setText( format.format(lista.get(position).StartDate)+"  -  "
               + format.format(lista.get(position).EndDate));

       if(lista.get(position).getTitle().length() < 21)
           holder.nombre.setText(lista.get(position).Title);

       else
           holder.nombre.setText(lista.get(position).Title.substring(0, 20)+"...");

       holder.canal.setText(lista.get(position).PChannel.channelCallLetter);

       AQuery aq = new AQuery(item);
       Image image = lista.get(position).getImageWidthType(Width.ORIGINAL_IMAGE, Type.BACKDROP_IMAGE);

       if(image != null){
           aq.id(holder.image).image(image.ImagePath);
           //Picasso.with(getContext()).load(image.getImagePath()).into(imagen);
       }
       //Log.e("programa","--->" +programa.getTitle());
       if(!lista.get(position).isILike()){

           final ViewHolder finalHolder = holder;
           holder.like.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   actionLike(lista.get(position));
                   finalHolder.like.setScaleX((float)1.6);
                   finalHolder.like.setScaleY((float)1.6);
                   finalHolder.like.setAlpha((float) 0.5);

                   if(facebookDelegate != null){
                       Log.e("Programa Like", "jfopwrefkp" + lista.get(position).getTitle());
                       facebookDelegate.like(lista.get(position));
                   }
               }
           });
       }
       else{
           holder.like.setEnabled(false);
           holder.like.setAlpha((float) 0.5);
           holder.like.setScaleX((float)1.6);
           holder.like.setScaleY((float)1.6);
       }

       return item;
   }

    /*public void setData(){

        if (item == null) {

            LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inflater.inflate(R.layout.category_program_container, parent, false);

            holder = new ViewHolder();
            //programa = getItem(position);

            holder.like = (Button) (item != null ? item.findViewById(R.id.categoria_action_like) : null);
            holder.horario = (TextView) item.findViewById(R.id.text_categoria_horario);
            holder.nombre = (TextView) item.findViewById(R.id.text_categoria_nombre);
            holder.canal = (TextView) item.findViewById(R.id.text_categoria_canal);
            holder.image = (ImageView) item.findViewById(R.id.foto_categoria);

            holder.horario.setTypeface(normal);
            holder.nombre.setTypeface(bold);
            holder.canal.setTypeface(normal);

            item.setTag(holder);

        }
        else{

            holder = (ViewHolder) item.getTag();
        }

        format.format(lista.get(position).getEndDate());

        holder.horario.setText( format.format(lista.get(position).StartDate)+"  -  "
                + format.format(lista.get(position).EndDate));

        if(lista.get(position).getTitle().length() < 21)
            holder.nombre.setText(lista.get(position).Title);

        else
            holder.nombre.setText(lista.get(position).Title.substring(0, 20)+"...");

        holder.canal.setText(lista.get(position).PChannel.channelCallLetter);

        AQuery aq = new AQuery(item);
        Image image = lista.get(position).getImageWidthType(Width.ORIGINAL_IMAGE, Type.BACKDROP_IMAGE);

        if(image != null){
            aq.id(holder.image).image(image.ImagePath);
            //Picasso.with(getContext()).load(image.getImagePath()).into(imagen);
        }
        //Log.e("programa","--->" +programa.getTitle());
        if(!lista.get(position).isILike()){

            final ViewHolder finalHolder = holder;
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    actionLike(lista.get(position));
                    finalHolder.like.setScaleX((float)1.6);
                    finalHolder.like.setScaleY((float)1.6);
                    finalHolder.like.setAlpha((float) 0.5);

                    if(facebookDelegate != null){
                        Log.e("Programa Like", "jfopwrefkp" + lista.get(position).getTitle());
                        facebookDelegate.like(lista.get(position));
                    }
                }
            });
        }
        else{
            holder.like.setEnabled(false);
            holder.like.setAlpha((float) 0.5);
            holder.like.setScaleX((float)1.6);
            holder.like.setScaleY((float)1.6);
        }
    }*/
    public void actionLike(Program p){

        DataLoader dataLoader = new DataLoader(getContext());
        dataLoader.actionLike( UserPreference.getIdNunchee(getContext()),"2",p.IdProgram,p.PChannel
                .channelID);
    }

    public static class ViewHolder{
        Button like;
        TextView horario;
        TextView nombre;
        TextView canal;
        ImageView image;
        //public  boolean seleccionado;
    }
}
