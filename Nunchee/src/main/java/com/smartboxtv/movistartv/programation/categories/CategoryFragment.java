package com.smartboxtv.movistartv.programation.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.clean.DataClean;
import com.smartboxtv.movistartv.data.models.CategorieChannel;
import com.smartboxtv.movistartv.data.modelssm.CategorieChannelSM;
import com.smartboxtv.movistartv.programation.delegates.CategoryDelegateGetCategory;
import com.smartboxtv.movistartv.programation.delegates.CategoryDelegateGetDate;

import java.util.Date;

/**
 * Created by Esteban- on 20-04-14.
 */
public class CategoryFragment extends Fragment {

    private CategorieChannel categorieChannel = null;
    private CategorieChannelSM categorieChannelSM = null;
    private Date date = new Date();

    private final FragmentDayBar fragmentBarDay  = new FragmentDayBar();
    private final CategoryFragmentBottomBar fragmentBarBottom = new CategoryFragmentBottomBar();
    private final CategoryFragmentContainer fragmentContainer  = new CategoryFragmentContainer();

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.category_fragment, container, false);

        DataClean.garbageCollector("Category Fragment");

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.categoria_programas, fragmentContainer);

        CategoryDelegateGetCategory delegateGetCategory = new CategoryDelegateGetCategory() {


            @Override
            public void getCategory(CategorieChannel cc) {
                categorieChannel = cc;
                fragmentContainer.cargarProgramas(categorieChannel.getIdChannleCategory(),date);

            }

            @Override
            public void getCategory(CategorieChannelSM cc) {
                categorieChannelSM = cc;
                fragmentContainer.cargarProgramasSM(categorieChannelSM.getId(),date);
            }

        } ;

        fragmentBarBottom.setDel(delegateGetCategory);
        ft.add(R.id.categoria_pie, fragmentBarBottom);

        CategoryDelegateGetDate delegateGetDate = new CategoryDelegateGetDate() {
            @Override
            public void getDate(Date d) {
                date = d;
                if(categorieChannel != null){
                    fragmentContainer.cargarProgramas(categorieChannel.getIdChannleCategory(),date);
                }
                /*if(categorieChannelSM != null){
                    fragmentContainer.cargarProgramasSM(categorieChannelSM.getId(),date);
                }*/
            }
            @Override
            public void loadProgamation(Date d, boolean b, boolean p) {

            }
        };

        fragmentBarDay.setDelegateGetDate(delegateGetDate);
        ft.add(R.id.categoria_dias, fragmentBarDay);
        ft.commit();

        return rootView;
    }
}
