package com.smartboxtv.movistartv.programation.favorites;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.activities.PreviewActivity;
import com.smartboxtv.movistartv.data.database.DataBase;
import com.smartboxtv.movistartv.data.image.ScreenShot;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Channel;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.modelssm.datafavorites.ProgramFavoriteSM;
import com.smartboxtv.movistartv.data.modelssm.datafavorites.ScheduleFavoriteSM;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.fragments.NUNCHEE;
import com.smartboxtv.movistartv.programation.delegates.FavoritoScrollDelegate;
import com.smartboxtv.movistartv.programation.menu.DialogError;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.services.ServiceManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Esteban- on 20-04-14.
 */
public class FavoriteFragmentDay extends Fragment {

    private SimpleDateFormat format, format1, format2, format3, formatAWS, horaFormat;
    private List<List<Program>> listFavorites = new ArrayList<List<Program>>();
    private List<Program> favorites = new ArrayList<Program>();

    private List<ProgramFavoriteSM> listFavoriteSM = new ArrayList<ProgramFavoriteSM>();

    private Typeface bold;
    private Typeface normal;
    private TextView textDay;
    private TextView textDate;
    private TextView textNoFavorite;

    private LinearLayout linearProgramas;
    private RelativeLayout noFavoriteContainer;
    private String dateString;
    private AQuery aq;
    private Date fecha;
    private FavoritoScrollDelegate scrollDelegate;
    // Loading
    private FrameLayout containerLoading;
    private View loading;
    private Animation animacion;
    // delete
    private Long fin,delta, inicio = System.currentTimeMillis();

    @Override
    public void onDestroy() {
        listFavorites.clear();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        listFavorites.clear();
        super.onDetach();
    }

    public FavoriteFragmentDay() {
    }

    public FavoriteFragmentDay(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.favorite_fragment_day, container, false);

        fin = System.currentTimeMillis();
        delta = fin - inicio;
        //Log.e("Tiempo FavoriteFragmentDay","Tiempo inicial "+delta);

        animacion = AnimationUtils.loadAnimation(getActivity(), R.anim.agranda);

        textDay = (TextView) (rootView != null ? rootView.findViewById(R.id.favorito_dia) : null);
        textDate = (TextView) rootView.findViewById(R.id.favorito_fecha);
        textNoFavorite = (TextView) rootView.findViewById(R.id.no_favorite_text);

        normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        containerLoading = (FrameLayout) rootView.findViewById(R.id.favorito_contiene_loading);
        linearProgramas = (LinearLayout) rootView.findViewById(R.id.programas_favoritos);
        noFavoriteContainer = (RelativeLayout) rootView.findViewById(R.id.no_favorite_container);

        format = new SimpleDateFormat("EEEE");
        format1 = new SimpleDateFormat("dd");

        format2 = new SimpleDateFormat("MMMM");
        format3 = new SimpleDateFormat("dd-MM-yyyy");
        horaFormat = new SimpleDateFormat("HH:mm");

        formatAWS = new SimpleDateFormat("yyyy-MM-dd");

        aq = new AQuery(rootView);

        setText();
        setDate(fecha);
        loadFavorites();
        //loadFavoriteSM();

        return rootView;
    }

    public void setText(){

        textDay.setTypeface(bold);
        textDate.setTypeface(normal);
        textNoFavorite.setTypeface(normal);

        if(!isToday(fecha))
            textDay.setText(capitalize(format.format(fecha)));
        else
            textDay.setText("Hoy");

        textDate.setText(format1.format(fecha) + " " + capitalize(format2.format(fecha)));
    }

    public void setDate(Date fecha) {

        this.fecha = fecha;
        if(((NUNCHEE)getActivity().getApplication()).CONNECT_AWS){
            dateString = format3.format(fecha);
        }
        else{
            dateString = formatAWS.format(fecha);
        }
    }

    public void loadFavorites(){

        loading();
        DataLoader data = new DataLoader(getActivity());
        data.getFavoritesPrograms(new DataLoader.DataLoadedHandler<Program>(){

            @Override
            public void loaded(List<Program> data) {

                favorites = data;
                separatePrograms();
                setData();
                borraLoading();
            }
            @Override
            public void error(String error) {
                borraLoading();
                super.error(error);
                DialogError dialogError = new DialogError("Ha tardado más de lo debido");
                dialogError.show(getActivity().getSupportFragmentManager(), "");
            }
        }, UserPreference.getIdNunchee(getActivity()), dateString);
    }

    public void setData(){

        View[] containerPrograms = new View[listFavorites.size()];
        for(int i = 0; i < listFavorites.size();i++){

            if(listFavorites.get(i).size()>1){
                containerPrograms[i] = getLayoutInflater(null).inflate(R.layout.favorite_program_many, null);

                // FIND VIEW BY ID
                ImageView imagen = (ImageView) containerPrograms[i].findViewById(R.id.favorito_imagen_deplegable);
                TextView nombre = (TextView) containerPrograms[i].findViewById(R.id.favorito_text_desplegable_nombre);
                TextView cantidad = (TextView) containerPrograms[i].findViewById(R.id.favorito_texto_desplegable_cantidad);


                //Set TypeFace
                nombre.setTypeface(bold);
                cantidad.setTypeface(normal);

                // SET TEXT
                nombre.setText(listFavorites.get(i).get(0).Title);

                if(listFavorites.get(i).size()>1)
                    cantidad.setText(listFavorites.get(i).size() +" Programas");
                else
                    cantidad.setText(listFavorites.get(i).size() +" Programa");

                //IMAGEN
                if(listFavorites.get(i).get(0).getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE)!= null){

                    aq.id(imagen).image(listFavorites.get(i).get(0).getImageWidthType(Width.ORIGINAL_IMAGE,
                            Type.SQUARE_IMAGE).ImagePath);
                }
                View[] singleProgram = new View[listFavorites.get(i).size()];

                final LinearLayout linearLayoutUnico = (LinearLayout) containerPrograms[i].findViewById(R.id.favorito_programa_unico);
                linearLayoutUnico.setVisibility(View.GONE);
                containerPrograms[i].setTag(null);

                // Efecto acordeón
                containerPrograms[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (linearLayoutUnico.getVisibility() == View.GONE) {
                            linearLayoutUnico.setVisibility(View.VISIBLE);
                            if(scrollDelegate != null){
                                scrollDelegate.scrollChanged();
                            }
                        }
                        else {
                            linearLayoutUnico.setVisibility(View.GONE);
                            if(scrollDelegate != null){
                                scrollDelegate.scrollChanged();
                            }
                        }

                    }
                });

                for(int j = 0; j< listFavorites.get(i).size();j++){


                    singleProgram[j] = getLayoutInflater(null).inflate(R.layout.favorite_program_ultimate, null);

                    //FIND VIEW BY ID
                    TextView  nombrePrograma = (TextView) singleProgram[j].findViewById(R.id.favorito_texto_canal);
                    TextView  horaPrograma = (TextView) singleProgram[j].findViewById(R.id.favorito_text_horario);
                    TextView sinopsisPrograma = (TextView) singleProgram[j].findViewById(R.id.favorito_texto_sinopsis);

                    //SET TYPEFACE
                    nombrePrograma.setTypeface(bold);
                    horaPrograma.setTypeface(normal);
                    sinopsisPrograma.setTypeface(normal);

                    //SET TEXT
                    nombrePrograma.setText(listFavorites.get(i).get(j).getTitle());
                    horaPrograma.setText(horaFormat.format(listFavorites.get(i).get(j).StartDate)+" | "
                            + horaFormat.format(listFavorites.get(i).get(j).EndDate));

                    sinopsisPrograma.setText(listFavorites.get(i).get(j).EpisodeTitle);
                    singleProgram[j].setTag(listFavorites.get(i).get(j));

                    singleProgram[j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Program p = (Program) view.getTag();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.addToBackStack(null);

                            RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.view_parent);
                            Bitmap screenShot = ScreenShot.takeScreenshot(r);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            screenShot.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                            byte[] byteArray = stream.toByteArray();

                            try {
                                String filename = getActivity().getCacheDir()
                                        + File.separator + System.currentTimeMillis() + ".jpg";

                                File f = new File(filename);
                                f.createNewFile();
                                FileOutputStream fo = new FileOutputStream(f);
                                fo.write(byteArray);
                                fo.close();

                                Intent i = new Intent(getActivity(), PreviewActivity.class);
                                i.putExtra("background", filename);
                                i.putExtra("programa", p);
                                i.putExtra("file",f);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
                                //context.startActivity(i);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    linearLayoutUnico.addView(singleProgram[j]);

                    // Boton Recordar
                    final ImageView recordar = (ImageView) singleProgram[j].findViewById(R.id.favorito_imagen_horario);

                    recordar.setTag(listFavorites.get(i).get(j));
                    recordar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Program p = (Program) view.getTag();
                            recordar.setEnabled(false);
                            recordar.startAnimation(animacion);
                            recordar.setAlpha((float) 0.5);
                            createReminder(p);
                        }
                    });
                }
            }
            //Programa unico

            else if(listFavorites.get(i).size() == 1){

                containerPrograms[i] = getLayoutInflater(null).inflate(R.layout.favorite_program_only, null);

                //FIND VIEW BY ID
                TextView nombre = (TextView) containerPrograms[i].findViewById(R.id.favorito_programa_unico_texto_nombre);
                TextView hora = (TextView) containerPrograms[i].findViewById(R.id.favorito_programa_unico_texto_hora);
                TextView canal = (TextView) containerPrograms[i].findViewById(R.id.favorito_programa_unico_texto_canal);

                ImageView foto = (ImageView) containerPrograms[i].findViewById(R.id.favorito_programa_unico_imagen);
                final Button recordar = (Button) containerPrograms[i].findViewById(R.id.favorito_programa_unico_boton_recordar);

                recordar.setTag(listFavorites.get(i).get(0));
                recordar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Program p = (Program) view.getTag();
                        recordar.setEnabled(false);
                        recordar.startAnimation(animacion);
                        recordar.setAlpha((float) 0.5);
                        createReminder(p);
                    }
                });

                //Set TypeFace
                nombre.setTypeface(bold);
                hora.setTypeface(normal);
                canal.setTypeface(normal);

                // SetView
                nombre.setText(listFavorites.get(i).get(0).getTitle());

                hora.setText(horaFormat.format(listFavorites.get(i).get(0).getStartDate())+" | "
                        + horaFormat.format(listFavorites.get(i).get(0).getEndDate()));

                //canal.setText(listFavorites.get(i).get(0).getPChannel().getChannelName());
                canal.setText(listFavorites.get(i).get(0).PChannel.channelName);


                if(listFavorites.get(i).get(0).getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE)!= null){

                    aq.id(foto).image(listFavorites.get(i).get(0).getImageWidthType(Width.ORIGINAL_IMAGE,
                            Type.SQUARE_IMAGE).ImagePath);
                }

                containerPrograms[i].setTag(listFavorites.get(i).get(0));

                containerPrograms[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Program p = (Program) view.getTag();

                        RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.view_parent);
                        Bitmap screenShot = ScreenShot.takeScreenshot(r);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        screenShot.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] byteArray = stream.toByteArray();

                        try {
                            String filename = getActivity().getCacheDir()
                                    + File.separator + System.currentTimeMillis() + ".jpg";

                            File f = new File(filename);
                            f.createNewFile();
                            FileOutputStream fo = new FileOutputStream(f);
                            fo.write(byteArray);
                            fo.close();

                            Intent intent = new Intent(getActivity(), PreviewActivity.class);
                            intent.putExtra("background", filename);
                            intent.putExtra("programa", p);
                            intent.putExtra("file",f);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
                            //context.startActivity(i);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
            linearProgramas.addView(containerPrograms[i]);
        }

        fin = System.currentTimeMillis();
        delta = fin - inicio;
        Log.e("Tiempo FavoriteFragmentDay","Tiempo fin "+delta);
    }

    public void loadFavoriteSM(){
        loading();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("");
        ServiceManager serviceManager = new ServiceManager(getActivity());
        serviceManager.getFavoriteForDay(new ServiceManager.ServiceManagerHandler<ProgramFavoriteSM>(){
            @Override
            public void loaded(List<ProgramFavoriteSM> data) {
                listFavoriteSM = data;
                try{
                    if(!data.isEmpty()){
                        setDataSM();
                    }
                    else{
                        noFavorite();
                    }
                    borraLoading();
                }
                catch(Exception e){
                    e.printStackTrace();
                    Log.e("ERROR", "--> "+e.getMessage());
                }
            }

            @Override
            public void error(String error) {
                super.error(error);
                Log.e("LoadFavorite","--> "+error);
                DialogError dialogError = new DialogError("Ha tardado más de lo debido");
                dialogError.show(getActivity().getSupportFragmentManager(), "");
            }
        },"",format3.format(fecha),"");
    }

    public void noFavorite(){
        noFavoriteContainer.setVisibility(View.VISIBLE);
    }
    public void setDataSM(){

        View[] containerPrograms = new View[listFavoriteSM.size()];
        for(int i = 0; i < listFavoriteSM.size();i++){

            if(listFavoriteSM.get(i).schedule.size() > 1){
                containerPrograms[i] = getLayoutInflater(null).inflate(R.layout.favorite_program_many, null);

                // FIND VIEW BY ID
                ImageView imagen = (ImageView) containerPrograms[i].findViewById(R.id.favorito_imagen_deplegable);
                TextView nombre = (TextView) containerPrograms[i].findViewById(R.id.favorito_text_desplegable_nombre);
                TextView cantidad = (TextView) containerPrograms[i].findViewById(R.id.favorito_texto_desplegable_cantidad);

                //Set TypeFace
                nombre.setTypeface(bold);
                cantidad.setTypeface(normal);

                // SET TEXT
                if(listFavoriteSM.get(i).name != null){
                    nombre.setText(listFavoriteSM.get(i).name);
                }

                if(listFavoriteSM.get(i).schedule.size()>1)
                    cantidad.setText(listFavoriteSM.get(i).schedule.size() +" Episodios");
                else
                    cantidad.setText(listFavoriteSM.get(i).schedule.size() +" Episodio");

                if(listFavoriteSM.get(i).image != null){
                    aq.id(imagen).image(listFavoriteSM.get(i).image);
                }
                View[] singleProgram = new View[listFavoriteSM.get(i).schedule.size()];

                final LinearLayout linearLayoutUnico = (LinearLayout) containerPrograms[i].findViewById(R.id.favorito_programa_unico);
                linearLayoutUnico.setVisibility(View.GONE);
                containerPrograms[i].setTag(null);

                // Efecto acordeón
                containerPrograms[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (linearLayoutUnico.getVisibility() == View.GONE) {
                            linearLayoutUnico.setVisibility(View.VISIBLE);
                            if(scrollDelegate != null){
                                scrollDelegate.scrollChanged();
                            }
                        }
                        else {
                            linearLayoutUnico.setVisibility(View.GONE);
                            if(scrollDelegate != null){
                                scrollDelegate.scrollChanged();
                            }
                        }
                    }
                });

                for(int j = 0; j< listFavoriteSM.get(i).schedule.size();j++){


                    singleProgram[j] = getLayoutInflater(null).inflate(R.layout.favorite_program_ultimate, null);

                    //FIND VIEW BY ID
                    TextView  nombrePrograma = (TextView) singleProgram[j].findViewById(R.id.favorito_texto_canal);
                    TextView  horaPrograma = (TextView) singleProgram[j].findViewById(R.id.favorito_text_horario);
                    TextView sinopsisPrograma = (TextView) singleProgram[j].findViewById(R.id.favorito_texto_sinopsis);

                    //SET TYPEFACE
                    nombrePrograma.setTypeface(bold);
                    horaPrograma.setTypeface(normal);
                    sinopsisPrograma.setTypeface(normal);

                    //SET TEXT
                    if(listFavoriteSM.get(i).schedule.get(j).name.length() > 29)
                        nombrePrograma.setText(listFavoriteSM.get(i).schedule.get(j).name.replace(".","").substring(0,26)+"...");
                    else
                        nombrePrograma.setText(listFavoriteSM.get(i).schedule.get(j).name.replace(".", "")); 
                    horaPrograma.setText(horaFormat.format(listFavoriteSM.get(i).schedule.get(j).startDate)+" | "
                            + horaFormat.format(listFavoriteSM.get(i).schedule.get(j).endDate));

                    sinopsisPrograma.setText(listFavoriteSM.get(i).schedule.get(j).description);

                    Program program = new Program();
                    program.IdProgram = ""+listFavoriteSM.get(i).id;
                    program.PChannel = new Channel();
                    program.PChannel.channelID = ""+listFavoriteSM.get(i).getSchedule().get(j).channel.id;
                    program.PChannel.channelImageURL = listFavoriteSM.get(i).getSchedule().get(j).channel.image;
                    program.PChannel.channelNumber = ""+listFavoriteSM.get(i).getSchedule().get(j).channel.id;
                    program.PChannel.channelCallLetter = listFavoriteSM.get(i).getSchedule().get(j).channel.callLetter;
                    program.Title = listFavoriteSM.get(i).name;
                    program.StartDate = listFavoriteSM.get(i).getSchedule().get(j).startDate;
                    program.EndDate = listFavoriteSM.get(i).getSchedule().get(j).endDate;
                    program.IdEpisode = ""+listFavoriteSM.get(i).getSchedule().get(j).id;

                    singleProgram[j].setTag(program);

                    singleProgram[j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Program p = (Program) view.getTag();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.addToBackStack(null);

                            RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.view_parent);
                            Bitmap screenShot = ScreenShot.takeScreenshot(r);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            screenShot.compress(Bitmap.CompressFormat.JPEG,80, stream);
                            byte[] byteArray = stream.toByteArray();

                            try {
                                String filename = getActivity().getCacheDir()
                                        + File.separator + System.currentTimeMillis() + ".jpg";

                                File f = new File(filename);
                                f.createNewFile();
                                FileOutputStream fo = new FileOutputStream(f);
                                fo.write(byteArray);
                                fo.close();

                                Intent i = new Intent(getActivity(), PreviewActivity.class);
                                i.putExtra("background", filename);
                                i.putExtra("programa", p);
                                i.putExtra("file",f);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    linearLayoutUnico.addView(singleProgram[j]);

                    // Boton Recordar
                    ImageView recordar = (ImageView) singleProgram[j].findViewById(R.id.favorito_imagen_horario);

                    recordar.setTag(listFavoriteSM.get(i).getSchedule().get(j));
                    final int finalI = i;
                    recordar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            ScheduleFavoriteSM  s = (ScheduleFavoriteSM) view.getTag();
                            createReminder(s,listFavoriteSM.get(finalI).name,""+listFavoriteSM.get(finalI).id,listFavoriteSM.get(finalI).image);

                        }
                    });
                }
            }
            //Programa unico

            else if(listFavoriteSM.get(i).schedule.size() == 1){

                containerPrograms[i] = getLayoutInflater(null).inflate(R.layout.favorite_program_only, null);

                //FIND VIEW BY ID
                TextView nombre = (TextView) containerPrograms[i].findViewById(R.id.favorito_programa_unico_texto_nombre);
                TextView hora = (TextView) containerPrograms[i].findViewById(R.id.favorito_programa_unico_texto_hora);
                TextView canal = (TextView) containerPrograms[i].findViewById(R.id.favorito_programa_unico_texto_canal);

                ImageView foto = (ImageView) containerPrograms[i].findViewById(R.id.favorito_programa_unico_imagen);
                Button recordar = (Button) containerPrograms[i].findViewById(R.id.favorito_programa_unico_boton_recordar);

                recordar.setTag(listFavoriteSM.get(i).schedule);
                /*recordar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Program p = (Program) view.getTag();
                        createReminder(p);
                    }
                });*/

                //Set TypeFace
                nombre.setTypeface(bold);
                hora.setTypeface(normal);
                canal.setTypeface(normal);

                // SetView
                nombre.setText(listFavoriteSM.get(i).schedule.get(0).name);

                /*hora.setText(horaFormat.format(listFavoriteSM.get(i).getStartDate())+" | "
                        + horaFormat.format(listFavorites.get(i).get(0).getEndDate()));*/

                hora.setText(horaFormat.format(listFavoriteSM.get(i).schedule.get(0).startDate)+" | "
                        + horaFormat.format(listFavoriteSM.get(i).schedule.get(0).getEndDate()));
                //canal.setText(listFavorites.get(i).get(0).getPChannel().getChannelName());
                canal.setText(listFavoriteSM.get(i).schedule.get(0).channel.callLetter);

                if(listFavoriteSM.get(i).getImage()!= null){
                    aq.id(foto).image(listFavoriteSM.get(i).image);
                }

                /*if(listFavorites.get(i).get(0).getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE)!= null){

                    aq.id(foto).image(listFavorites.get(i).get(0).getImageWidthType(Width.ORIGINAL_IMAGE,
                            Type.SQUARE_IMAGE).ImagePath);
                }*/

                Program program = new Program();
                program.IdProgram = ""+listFavoriteSM.get(i).id;
                program.PChannel = new Channel();
                program.PChannel.channelID = ""+listFavoriteSM.get(i).getSchedule().get(0).channel.id;
                program.PChannel.channelImageURL = listFavoriteSM.get(i).getSchedule().get(0).channel.image;
                program.PChannel.channelCallLetter = listFavoriteSM.get(i).getSchedule().get(0).channel.callLetter;
                program.Title = listFavoriteSM.get(i).name;
                program.StartDate = listFavoriteSM.get(i).getSchedule().get(0).startDate;
                program.EndDate = listFavoriteSM.get(i).getSchedule().get(0).endDate;
                program.IdEpisode = ""+listFavoriteSM.get(i).getSchedule().get(0).id;
                //containerPrograms[i].setTag(listFavoriteSM.get(i).getSchedule().get(0));
                containerPrograms[i].setTag(program);

                containerPrograms[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Program p = (Program) view.getTag();

                        RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.view_parent);
                        Bitmap screenShot = ScreenShot.takeScreenshot(r);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        screenShot.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] byteArray = stream.toByteArray();

                        try {
                            String filename = getActivity().getCacheDir()
                                    + File.separator + System.currentTimeMillis() + ".jpg";

                            File f = new File(filename);
                            f.createNewFile();
                            FileOutputStream fo = new FileOutputStream(f);
                            fo.write(byteArray);
                            fo.close();

                            Intent intent = new Intent(getActivity(), PreviewActivity.class);
                            intent.putExtra("background", filename);
                            intent.putExtra("programa", p);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
                            //context.startActivity(i);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
            linearProgramas.addView(containerPrograms[i]);
        }

        fin = System.currentTimeMillis();
        delta = fin - inicio;
        Log.e("Tiempo FavoriteFragmentDay","Tiempo fin "+delta);
    }
    public void loading(){
        loading = getLayoutInflater(null).inflate(R.layout.progress_dialog_pop_corn, null);
        RelativeLayout containerPopCorn = (RelativeLayout) (loading != null ? loading.findViewById(R.id.pop_corn) : null);
        containerPopCorn.setBackgroundColor(Color.TRANSPARENT);
        ImageView popCorn = (ImageView) loading.findViewById(R.id.pop_corn_centro);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Animation animaPop = AnimationUtils.loadAnimation(getActivity(), R.anim.animacion_pop_hacia_derecha_centro);
        loading.setLayoutParams(params);
        popCorn.startAnimation(animaPop != null ? animaPop : null);
        containerLoading.addView(loading);
        loading.bringToFront();
        containerLoading.setEnabled(false);
    }
    public void borraLoading(){
        Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.deaparece);
        loading.startAnimation(anim != null ? anim : null);
        containerLoading.removeView(loading);
        containerLoading.setEnabled(true);
    }
    private String capitalize(String line){
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);

    }
    public void setScrollDelegate(FavoritoScrollDelegate scrollDelegate) {
        this.scrollDelegate = scrollDelegate;
    }

    public boolean isToday(Date d){
        return d.getDay() == new Date().getDay();
    }

    public void separatePrograms(){

        HashMap<String,List<Program>> hashMap = new HashMap<String,List<Program>>();

        for (Program favorite : favorites) {

            if (!hashMap.containsKey(favorite.getTitle())) {
                hashMap.put(favorite.getTitle(), new ArrayList<Program>());
            }
            hashMap.get(favorite.getTitle()).add(favorite);
        }
        listFavorites = new ArrayList<List<Program>>(hashMap.values());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void createReminder(Program p){

        Log.e("Recordar","Recordar "+ p.getTitle());

        int  id_calendars[] = getCalendar(getActivity());
        long calID = id_calendars[0];
        long startMillis;
        long endMillis;

        Calendar cal = Calendar.getInstance();
        Log.e("Calendario",cal.toString());

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(p.getStartDate());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(p.getEndDate());
        endMillis = endTime.getTimeInMillis();

        TimeZone timeZone = TimeZone.getDefault();

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, p.getTitle());

        if(p.getPChannel().getChannelCallLetter()!= null)
            values.put(CalendarContract.Events.DESCRIPTION,p.getPChannel().getChannelCallLetter()+" "
                    + horaFormat.format(p.getStartDate())+" | "+ horaFormat.format(p.getEndDate())+" "+p.getEpisodeTitle());
        else
            values.put(CalendarContract.Events.DESCRIPTION,p.getPChannel().getChannelName()+" "
                    + horaFormat.format(p.getStartDate())+" | "+ horaFormat.format(p.getEndDate())+" "+p.getEpisodeTitle());

        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.ALL_DAY,0);


        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventID = Long.parseLong( uri != null ? uri.getLastPathSegment() : null);

        ContentValues reminderValues = new ContentValues();
        reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

        DataBase dataBase = new DataBase(getActivity(),"",null,1);
        SQLiteDatabase db = dataBase.getWritableDatabase();

        //db.execSQL("");
        //dataBase.onUpgrade(db,0,0);
        if(db != null){

            String codigo = p.IdProgram;
            String nombre = p.Title;
            String fecha = ""+p.StartDate.getTime();
            String endDate =""+p.EndDate.getTime();
            String codigoChannel = p.PChannel.channelID;
            Image image = p.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
            String urlImage = null;
            if(image != null)
            urlImage = image.getImagePath();

            Log.e("Nombre",p.Title);
            //Insertamos los datos en la tabla reminder
            String query = "INSERT INTO reminder (id,begin_date,end_date,name,channel, image) " + "VALUES ('" + codigo + "',' "+fecha+"', '" + endDate+"', '" + nombre +"', '" + codigoChannel +"','"+urlImage+"')";
            Log.e("Query",query);
            db.execSQL(query);
        }
        db.close();
        Toast t = Toast.makeText(getActivity(),p.getTitle()+" agregado a tus recordatorios",Toast.LENGTH_LONG);
        t.show();

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void createReminder(ScheduleFavoriteSM sm, String programa, String id, String urlImage){

        //Log.e("Recordar","Recordar "+ p.getTitle());

        int  id_calendars[] = getCalendar(getActivity());
        long calID = id_calendars[0];
        long startMillis;
        long endMillis;

        Calendar cal = Calendar.getInstance();
        Log.e("Calendario",cal.toString());

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(sm.getStartDate());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(sm.getEndDate());
        endMillis = endTime.getTimeInMillis();

        TimeZone timeZone = TimeZone.getDefault();

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        //values.put(CalendarContract.Reminders.MINUTES, 3);
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, sm.getName());

        if(sm.channel.callLetter!= null)
            values.put(CalendarContract.Events.DESCRIPTION,sm.channel.callLetter+" "
                    + horaFormat.format(sm.getStartDate())+" | "+ horaFormat.format(sm.getEndDate())+" "+programa);
        else
            values.put(CalendarContract.Events.DESCRIPTION,sm.channel.callLetter+" "
                    + horaFormat.format(sm.getStartDate())+" | "+ horaFormat.format(sm.getEndDate())+" "+programa);

        values.put(CalendarContract.Events.CALENDAR_ID, calID);

        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.ALL_DAY,0);


        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventID = Long.parseLong( uri != null ? uri.getLastPathSegment() : null);

        ContentValues reminderValues = new ContentValues();

        reminderValues.put(CalendarContract.Reminders.MINUTES, 3);
        reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

        DataBase dataBase = new DataBase(getActivity(),"",null,1);
        SQLiteDatabase db = dataBase.getWritableDatabase();

        //db.execSQL("");
        //dataBase.onUpgrade(db,0,0);
        if(db != null){

            String codigo = id;
            String nombre = programa;
            String fecha = ""+sm.startDate.getTime();
            String endDate =""+sm.endDate.getTime();
            String codigoChannel = ""+sm.channel.id;
            Log.e("Nombre",programa);
            //Insertamos los datos en la tabla reminder
            String query = "INSERT INTO reminder (id,begin_date ,end_date ,name ,channel, image) " + "VALUES ('" + codigo + "',' "+fecha+"', '" + endDate+"', " +
                    "'" + nombre +"', '" + codigoChannel +"','"+urlImage+"')";
            Log.e("Query",query);
            db.execSQL(query);
        }
        db.close();

        Toast t = Toast.makeText(getActivity(),programa+" agregado a tus recordatorios",Toast.LENGTH_LONG);
        t.show();

    }

    public int[] getCalendar(Context c) {

        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = c.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

        int aux[] = new int[0];

        if (managedCursor.moveToFirst()) {

            aux = new int[managedCursor.getCount()];

            int cont = 0;
            do {
                aux[cont] = managedCursor.getInt(0);
                cont++;
            } while (managedCursor.moveToNext());

            managedCursor.close();
        }
        return aux;

    }
}
