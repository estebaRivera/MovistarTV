package com.smartboxtv.movistartv.programation.adapterssm;

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
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.modelssm.datacategory.ProgramsCategorySM;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.delgates.FacebookLikeDelegate;
import com.smartboxtv.movistartv.services.DataLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Esteban- on 17-06-14.
 */
public class CategoryAdapterContainerSM extends ArrayAdapter<ProgramsCategorySM> {

    //private List<Program> lista = new ArrayList<Program>();
    //private List<ScheduleSM> listCategories = new ArrayList<ScheduleSM>();
    //private List<ChannelSM> listChannel = new ArrayList<ChannelSM>();
    private List<ProgramsCategorySM> listPrograms = new ArrayList<ProgramsCategorySM>();
    private FacebookLikeDelegate facebookDelegate;
    private View item;
    private ViewHolder holder;
    private SimpleDateFormat format;
    private Typeface normal;
    private Typeface bold;
    private Program programa;
    private final String TAG = "Adapter Custom";

    private HashMap<String,Program> hashMap = new HashMap <String , Program>();


    public void setFacebookDelegate(FacebookLikeDelegate facebookDelegate) {
        this.facebookDelegate = facebookDelegate;
    }

    public CategoryAdapterContainerSM(Context context, List<ProgramsCategorySM> list) {

        super(context, R.layout.category_program_container, list);
        Log.e("Tamaño constructor ","--> "+list.size());
        normal = Typeface.createFromAsset(getContext().getAssets(), "fonts/SegoeWP-Light.ttf");
        bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/SegoeWP-Bold.ttf");
        format = new SimpleDateFormat("HH:mm");
        listPrograms = list;

    }


    @Override
    public View getView(final int position, View convertView,ViewGroup parent) {

        holder = null;
        item = convertView;

        Log.e("Category Position", "--> " + position);
        Log.e("Nombre", "--> " + listPrograms.get(position).episode.getName());
        Log.e("URL", "--> " + listPrograms.get(position).getImage());
        Log.e("...--","...--");

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

            Log.e("...--","...--");

        }
        else{

            holder = (ViewHolder) item.getTag();
        }

        /*try{
            format.parse(listCategories.get(position).getEndDate());
        }
        catch(ParseException e){
            Log.e("Error kznxvlznv",e.getMessage());

        }*/
        format.format(listPrograms.get(position).getEnd());


        holder.horario.setText( format.format(listPrograms.get(position).start)+"  -  "+format.format(listPrograms.get(position).end));

        //holder.horario.setText( "00:00 - 12:00");

        //holder.horario.setText( format.format(listCategories.get(position).start)+"  -  "+listCategories.get(position).end);
               // + format.format(listCategories.get(position).end));

        if(listPrograms.get(position).title.length() < 21)
            holder.nombre.setText(listPrograms.get(position).title);

        else
            holder.nombre.setText(listPrograms.get(position).title.substring(0, 20)+"...");

        //holder.canal.setText(listChannel.get(0).name);
        holder.canal.setText(listPrograms.get(position).channel.callLetter);

        AQuery aq = new AQuery(item);
        //Image image = listCategories.get(position).getImageWidthType(Width.ORIGINAL_IMAGE, Type.BACKDROP_IMAGE);

        //if(image != null){
        if(listPrograms.get(position).image != null){
            aq.id(holder.image).image(listPrograms.get(position).image);
        }
        else{
            Log.e("Nombre Programa sin foto","--> "+listPrograms.get(position).title);
            aq.id(holder.image).image("http://a4.mzstatic.com/us/r30/Purple4/v4/d5/ef/74/d5ef741e-be45-f366-08f3-33268453b35d/mzl.ppbpewkq.175x175-75.jpg");
        }
        //if(!lista.get(position).isILike()){
        if(false){

            final ViewHolder finalHolder = holder;
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //actionLike(lista.get(position));
                    finalHolder.like.setScaleX((float)1.6);
                    finalHolder.like.setScaleY((float)1.6);
                    finalHolder.like.setAlpha((float) 0.5);

                    if(facebookDelegate != null){
                        //Log.e("Programa Like", "jfopwrefkp" + lista.get(position).getTitle());
                        //facebookDelegate.like(lista.get(position));
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
    /*@Override
    public int getCount()
    {
        Log.v(TAG, "in getCount() "+listCategories.size());
        return listCategories.size();
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
    }
}
