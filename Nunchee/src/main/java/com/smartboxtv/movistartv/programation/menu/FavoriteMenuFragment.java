package com.smartboxtv.movistartv.programation.menu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.data.preference.UserPreferenceSM;
import com.smartboxtv.movistartv.fragments.NUNCHEE;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.services.ServiceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 08-05-14.
 */
public class FavoriteMenuFragment extends DialogFragment {

    private List<Program> favoritos = new ArrayList<Program>();
    private LayoutInflater inflater2;
    private LinearLayout listaProgramas;
    private int x;
    private int antigua_x;

    public FavoriteMenuFragment(List<Program> favoritos) {
        this.favoritos = favoritos;
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());

    }

    @Override
    public void onStart(){
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.height =  WindowManager.LayoutParams.MATCH_PARENT;
        params.width = 750;

        getDialog().getWindow().setAttributes(params);

        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.favorite_menu_fragment,container,false);
        inflater2 = inflater;
        listaProgramas = (LinearLayout) (rootView != null ? rootView.findViewById(R.id.lista_favoritos) : null);

        for (Program favorito : favoritos) {
            setData(favorito);
        }

        return rootView;
    }
    public void setData(final Program p){

        final View programa = inflater2.inflate(R.layout.element_favorite_menu, null);
        final RelativeLayout contendorFavorito = (RelativeLayout) (programa != null ? programa.findViewById(R.id.contenedor_programa_favorito) : null);

        LinearLayout.LayoutParams params;

        TextView nombre = (TextView) programa.findViewById(R.id.favorito_nombre_programa);
        TextView canal = (TextView) programa.findViewById(R.id.favorito_nombre_canal);

        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Light.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        ImageView borrar = (ImageView) programa.findViewById(R.id.boton_borrar);

        contendorFavorito.setPivotX(0);
        contendorFavorito.setPivotY(0);

        nombre.setTypeface(light);
        canal.setTypeface(bold);

        RelativeLayout contenedorBorrar = (RelativeLayout) programa.findViewById(R.id.contenedor_boton_borrar);

        final ImageView foto = (ImageView) programa.findViewById(R.id.imagen_canal);
        final ImageView corazon = (ImageView) programa.findViewById(R.id.favorito_corazon);

        AQuery aq = new AQuery(programa);

        Image image  = p.getImageWidthType(Width.ORIGINAL_IMAGE, Type.BACKDROP_IMAGE);

        if(image != null)
            aq.id(foto).image(image.getImagePath());

        nombre.setText(p.getTitle());
        canal.setText(p.getPChannel().getChannelName());
        Resources res = getResources();

        final Drawable noFavorito = res.getDrawable(R.drawable.heart_break);
        final Drawable siFavorito = res.getDrawable(R.drawable.heart_fav_admin);

        contenedorBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((NUNCHEE)getActivity().getApplication()).CONNECT_SERVICES_PYTHON == false){
                    DataLoader dataLoader = new DataLoader(getActivity());
                    dataLoader.deleteFavorite(UserPreference.getIdNunchee(getActivity()), p.getDate().substring(6, 16));

                    listaProgramas.removeView(programa);
                    Toast.makeText(getActivity(), "Favorito borrado", Toast.LENGTH_LONG).show();
                }
                else{
                    ServiceManager serviceManager = new ServiceManager(getActivity());
                    serviceManager.removeFavorite(new ServiceManager.ServiceManagerHandler<String>(){

                        @Override
                        public void loaded(String data) {
                            listaProgramas.removeView(programa);
                            Toast.makeText(getActivity(), "Favorito borrado", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void error(String error) {
                            super.error(error);
                            Log.e("error remove favorite","");
                        }
                    }, UserPreferenceSM.getIdNunchee(getActivity()),p.IdProgram);
                }



            }
        });

        contendorFavorito.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP :    x = (int) motionEvent.getX();

                                                    int delta = antigua_x - x;
                                                    if (delta > 50) {
                                                        float tamaño = 0.85f;

                                                        ObjectAnimator animatorSlideMenu = ObjectAnimator.ofFloat(contendorFavorito,View.SCALE_X,tamaño);

                                                        AnimatorSet animSet = new AnimatorSet();
                                                        animSet.playTogether(animatorSlideMenu);
                                                        animSet.start();

                                                        corazon.setImageDrawable(noFavorito);
                                                    }
                                                    else if(delta < -50){
                                                        float tamaño = 1f;

                                                        ObjectAnimator animatorSlideMenu = ObjectAnimator.ofFloat(contendorFavorito,View.SCALE_X,tamaño);

                                                        AnimatorSet animSet = new AnimatorSet();
                                                        animSet.playTogether(animatorSlideMenu);
                                                        animSet.start();

                                                        corazon.setImageDrawable(siFavorito);
                                                    }

                    case MotionEvent.ACTION_DOWN :  antigua_x = (int) motionEvent.getX();
                                                    break;
                }
                return true;
            }
        });

        listaProgramas.addView(programa);
        params = (LinearLayout.LayoutParams) programa.getLayoutParams();
        assert params != null;
        params.setMargins(0,0,0,15);
        programa.setLayoutParams(params);

    }
}
