package com.smartboxtv.movistartv.programation.favorites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.clean.DataClean;
import com.smartboxtv.movistartv.fragments.NUNCHEE;
import com.smartboxtv.movistartv.programation.customize.HorizontalScrollViewFavorite;
import com.smartboxtv.movistartv.programation.delegates.FavoritoScrollDelegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Esteban- on 20-04-14.
 */
public class FavoriteFragment extends Fragment {

    private final List<Date> days = new ArrayList<Date>();
    private Long inicio = System.currentTimeMillis();
    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
        HorizontalScrollViewFavorite scrollView = (HorizontalScrollViewFavorite) (rootView != null ? rootView.findViewById(
                R.id.favorito_scroll_horizontal) : null);
        DataClean.garbageCollector("Favorite Fragment");
        ((NUNCHEE) getActivity().getApplication()).sendAnaliticsScreen("Favoritos");
        loadListDays();
        loadFragment();

        return rootView;
    }

    public void loadListDays(){

        Date hoy,dia2,dia3,dia4,dia5,dia6,dia7;

        hoy = new Date();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,1);
        dia2 = c.getTime();

        c.add(Calendar.DATE,1);
        dia3 = c.getTime();

        c.add(Calendar.DATE,1);
        dia4 = c.getTime();

        c.add(Calendar.DATE,1);
        dia5 = c.getTime();

        c.add(Calendar.DATE,1);
        dia6 = c.getTime();

        c.add(Calendar.DATE,1);
        dia7 = c.getTime();

        days.add(hoy);
        days.add(dia2);
        days.add(dia3);
        days.add(dia4);
        days.add(dia5);
        days.add(dia6);
        days.add(dia7);
    }

    public void loadFragment(){

        FavoritoScrollDelegate scrollDelegate = new FavoritoScrollDelegate() {
            @Override
            public void scrollChanged() {
                //scrollView.smoothScrollTo(scrollView.getScrollX(),scrollView.getScrollY());

            }
        };

        Long fin = System.currentTimeMillis();
        Long delta = fin - inicio;
        //Log.e("Tiempo FavoriteFragment","inicial "+delta);

        FavoriteFragmentDay fragmento1 = new FavoriteFragmentDay(days.get(0));
        FavoriteFragmentDay fragmento2 = new FavoriteFragmentDay(days.get(1));
        FavoriteFragmentDay fragmento3 = new FavoriteFragmentDay(days.get(2));
        FavoriteFragmentDay fragmento4 = new FavoriteFragmentDay(days.get(3));
        FavoriteFragmentDay fragmento5 = new FavoriteFragmentDay(days.get(4));
        FavoriteFragmentDay fragmento6 = new FavoriteFragmentDay(days.get(5));
        FavoriteFragmentDay fragmento7 = new FavoriteFragmentDay(days.get(6));

        fragmento1.setScrollDelegate(scrollDelegate);
        fragmento2.setScrollDelegate(scrollDelegate);
        fragmento3.setScrollDelegate(scrollDelegate);
        fragmento4.setScrollDelegate(scrollDelegate);
        fragmento5.setScrollDelegate(scrollDelegate);
        fragmento6.setScrollDelegate(scrollDelegate);
        fragmento7.setScrollDelegate(scrollDelegate);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.add(R.id.favorito_dia_1, fragmento1);
        ft.add(R.id.favorito_dia_2, fragmento2);
        ft.add(R.id.favorito_dia_3, fragmento3);
        ft.add(R.id.favorito_dia_4, fragmento4);
        ft.add(R.id.favorito_dia_5, fragmento5);
        ft.add(R.id.favorito_dia_6, fragmento6);
        ft.add(R.id.favorito_dia_7, fragmento7);

        fin = System.currentTimeMillis();
        delta = fin - inicio;
        //Log.e("Tiempo FavoriteFragment", "**** Fin de tiempo " + delta);

        ft.commit();
    }

    @Override
    public void onDestroy() {
        days.clear();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        days.clear();
        super.onDetach();
    }
}
