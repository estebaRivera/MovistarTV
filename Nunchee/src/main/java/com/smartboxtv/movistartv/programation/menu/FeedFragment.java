package com.smartboxtv.movistartv.programation.menu;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.FeedJSON;
import com.smartboxtv.movistartv.data.models.Image;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Esteban- on 08-05-14.
 */
public class FeedFragment extends DialogFragment {

    private final List<FeedJSON> historial;
    private LayoutInflater layoutInflater;
    private View rootView;

    public FeedFragment(List<FeedJSON> historial) {
        this.historial = historial;
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
    }

    @Override
    public void onStart() {

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.height =  WindowManager.LayoutParams.MATCH_PARENT;
        params.width = 750;

        getDialog().getWindow().setAttributes(params);
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.feed_fragment,container,false);
        layoutInflater = inflater;

        setData();
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //getDialog().getWindow().setLayout(750, 700);
        return rootView;
    }
    public void setData(){

        LinearLayout contenedor = (LinearLayout) rootView.findViewById(R.id.lista_feeds);

        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Light.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        Resources res = getResources();
        Drawable favoriteImage, likeImage, checkImage;

        favoriteImage = res.getDrawable(R.drawable.favorito_feed_icon);
        likeImage = res.getDrawable(R.drawable.like_feed_icon);
        checkImage = res.getDrawable(R.drawable.check_in_icon);
        AQuery aq = new AQuery(getActivity());

        View programa_1 = layoutInflater.inflate(R.layout.element_feed_firts, null);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        //RelativeLayout r = (RelativeLayout) programa_1.findViewById(R.id.contenedor_programa_historial);

        TextView txtNameAction1 =  (TextView) programa_1.findViewById(R.id.feed_accion);
        TextView txtDetailAction1 =  (TextView) programa_1.findViewById(R.id.feed_feed_detalle_accion);
        TextView txtDate1 =  (TextView) programa_1.findViewById(R.id.feed_fecha);
        TextView txtCountFriend1 =  (TextView) programa_1.findViewById(R.id.feed_cantidad_amigos);

        ImageView imgFriend1_1 = (ImageView) programa_1.findViewById(R.id.feed_foto_amigo_1);
        ImageView imgFriend2_1 = (ImageView) programa_1.findViewById(R.id.feed_foto_amigo_2);
        ImageView imgFriend3_1 = (ImageView) programa_1.findViewById(R.id.feed_foto_amigo_3);
        ImageView imgFriend4_1 = (ImageView) programa_1.findViewById(R.id.feed_foto_amigo_4);
        ImageView imgFriend5_1 = (ImageView) programa_1.findViewById(R.id.feed_foto_amigo_5);

        ImageView imgProgram_1 = (ImageView) programa_1.findViewById(R.id.feed_imagen);
        ImageView imgAction_1 = (ImageView) programa_1.findViewById(R.id.feed_imagen_accion);

        ImageView back =  (ImageView) programa_1.findViewById(R.id.back_feed);

        txtNameAction1.setTypeface(bold);
        txtDetailAction1.setTypeface(light);
        txtDate1.setTypeface(light);
        txtCountFriend1.setTypeface(light);

        int id = Integer.parseInt(historial.get(0).getAction().getIdAction());

        txtNameAction1.setText(historial.get(0).getAction().getActionName());
        txtDate1.setText(formatoFecha.format(historial.get(0).getDate()));

        Image imagen = historial.get(0).getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE);

        if(imagen != null)
            aq.id(imgProgram_1).image(imagen.getImagePath());

        imagen = historial.get(0).getImageWidthType(Width.ORIGINAL_IMAGE, Type.BACKDROP_IMAGE);


        if(imagen != null)
            aq.id(back).image(imagen.getImagePath());

        switch(id){
            // LIKE
            case 2:     txtDetailAction1.setText("Le diste me gusta a " + historial.get(0).getFeedText());
                        imgAction_1.setImageDrawable(likeImage);
                        break;
            //  CHECK IN
            case 7:     txtDetailAction1.setText("Acabas de hacer Check-in en  " + historial.get(0).getFeedText());
                        imgAction_1.setImageDrawable(checkImage);
                        break;
            // FAVORITO
            case 4:     txtDetailAction1.setText("Agregaste a " + historial.get(0).getFeedText() + " a tus favoritos");
                        imgAction_1.setImageDrawable(favoriteImage);
                        break;
            // SHARE
            case 3:     txtDetailAction1.setText("Has compartido  " + historial.get(0).getFeedText());
                        break;

        }
        programa_1 .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Program p = new Program();
                p.setIdProgram(historial.get(0).getIdPrograma());
                p.getPChannel().setChannelID(historial.get(0).getChannel().getIdChannel());
                //p.setStartDate(historial.get(0).);
                //p.setEndDate();
                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                PreviewFragment preview = new PreviewFragment();
                preview.setPrograma(p);
                ft.replace(R.id.contenedor_preview, preview);
                ft.commit();*/
            }
        });
        contenedor.addView(programa_1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(660,180);
        programa_1.setLayoutParams(params);
        // For
        for(int i = 1 ; i < historial.size(); i++){

            View programa = layoutInflater.inflate(R.layout.element_feed, null);

            TextView  nombreAccion =  (TextView) programa.findViewById(R.id.feed_accion);
            TextView  detalleAccion =  (TextView) programa.findViewById(R.id.feed_feed_detalle_accion);
            TextView  fecha =  (TextView) programa.findViewById(R.id.feed_fecha);
            TextView  cantidadAmigos =  (TextView) programa.findViewById(R.id.feed_cantidad_amigos);

            ImageView fotoAmigo1 = (ImageView) programa.findViewById(R.id.feed_foto_amigo_1);
            ImageView fotoAmigo2 = (ImageView) programa.findViewById(R.id.feed_foto_amigo_2);
            ImageView fotoAmigo3 = (ImageView) programa.findViewById(R.id.feed_foto_amigo_3);
            ImageView fotoAmigo4 = (ImageView) programa.findViewById(R.id.feed_foto_amigo_4);
            ImageView fotoAmigo5 = (ImageView) programa.findViewById(R.id.feed_foto_amigo_5);

            ImageView imagePrograma = (ImageView) programa.findViewById(R.id.feed_imagen);
            ImageView imageAccion = (ImageView) programa.findViewById(R.id.feed_imagen_accion);

            nombreAccion.setTypeface(bold);
            detalleAccion.setTypeface(light);
            fecha.setTypeface(light);
            cantidadAmigos.setTypeface(light);
            id = Integer.parseInt(historial.get(i).getAction().getIdAction());

            Log.e("id", " " + historial.get(i).getAction().getIdAction() + " " + historial.get(i).getAction()
                    .getActionName());
            nombreAccion.setText(historial.get(i).getAction().getActionName());

            imagen = historial.get(i).getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE);
            fecha.setText(formatoFecha.format(historial.get(i).getDate()));

            if(imagen != null)
                aq.id(imagePrograma).image(imagen.getImagePath());

            switch(id){
                // LIKE
                case 2:     detalleAccion.setText("Le diste me gusta a "+historial.get(i).getFeedText());
                            imageAccion.setImageDrawable(likeImage);
                            break;
                //  CHECK IN
                case 7:     detalleAccion.setText("Acabas de hacer Check-in en  "+historial.get(i).getFeedText());
                            imageAccion.setImageDrawable(checkImage);
                            break;
                // FAVORITO
                case 4:     detalleAccion.setText("Agregaste a "+historial.get(i).getFeedText()+" a tus favoritos");
                            imageAccion.setImageDrawable(favoriteImage);
                            break;
                // SHARE
                case 3:     detalleAccion.setText("Has compartido  "+historial.get(i).getFeedText());
                            break;
            }
            contenedor.addView(programa);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(660,135);
            programa.setLayoutParams(params2);
        }

    }
}
