package com.smartboxtv.movistartv.programation.categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.models.CategorieChannel;
import com.smartboxtv.movistartv.data.modelssm.CategorieChannelSM;
import com.smartboxtv.movistartv.programation.adapters.CategoryAdapter;
import com.smartboxtv.movistartv.programation.adapterssm.CategoryAdapterSM;
import com.smartboxtv.movistartv.programation.delegates.CategoryDelegateGetCategory;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.services.ServiceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Esteban- on 20-04-14.
 */
public class CategoryFragmentBottomBar extends Fragment {

    private GridView gridView;
    private CategoryAdapter adapter;
    private CategoryAdapterSM adapterSM;
    private List<CategorieChannel> listaCategoria = new ArrayList<CategorieChannel>();
    private List<CategorieChannelSM> categorieList = new ArrayList<CategorieChannelSM>();
    private CategoryDelegateGetCategory del;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.category_fragment_bar_bottom, container, false);
        gridView = (GridView) (rootView != null ? rootView.findViewById(R.id.gridView) : null);

        cargaCategorias();
        //cargaCategoriaSM();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                adapter.setSelected(v);
                //adapterSM.setSelected(v);
                //Log.e("Nombre Categotia",listaCategoria.get(position).channelCategoryName);
                if(del!=null){
                    del.getCategory(listaCategoria.get(position));
                    //del.getCategory(categorieList.get(position));
                }
            }

        });
        return rootView;
    }

    public void cargaCategorias(){

        DataLoader dataLoader = new DataLoader(getActivity());
        dataLoader.getCategories(new DataLoader.DataLoadedHandler<CategorieChannel>() {

            @Override
            public void loaded(List<CategorieChannel> data) {

                listaCategoria = data;
                ordenaCategorias();

                adapter = new CategoryAdapter(getActivity(),listaCategoria);
                gridView.setNumColumns(listaCategoria.size());
                gridView.setAdapter(adapter);

                if(del!=null){
                    del.getCategory(listaCategoria.get(0));
                }
            }
            @Override
            public void error(String error) {
                super.error(error);
            }
        });
    }

    public void cargaCategoriaSM(){

        ServiceManager serviceManager = new ServiceManager(getActivity());
        serviceManager.getCategories(new ServiceManager.ServiceManagerHandler<CategorieChannelSM>(){
            @Override
            public void loaded(List<CategorieChannelSM> data) {
//                Log.e("DATA",data.toString());
                categorieList = data;
                ordenaCategoriasSM();
                adapterSM = new CategoryAdapterSM(getActivity(),categorieList);
                gridView.setNumColumns(categorieList.size());
                gridView.setAdapter(adapterSM);

                if(del!=null){
                    del.getCategory(categorieList.get(0));
                }
            }

            @Override
            public void error(String error) {
                Log.e("DATA ERROR",error);
                super.error(error);
            }
        },"es",2);
    }
    public void ordenaCategorias(){

        Collections.sort(listaCategoria, new Comparator<CategorieChannel>() {
            @Override
            public int compare(CategorieChannel categoriaCanal, CategorieChannel categoriaCanal2) {
                return new Integer(categoriaCanal.getIndex()).compareTo(new Integer(categoriaCanal2.getIndex()));
            }
        });
    }

    public void ordenaCategoriasSM(){

        Collections.sort(categorieList, new Comparator<CategorieChannelSM>() {
            @Override
            public int compare(CategorieChannelSM categoriaCanal, CategorieChannelSM categoriaCanal2) {
                return new Integer(categoriaCanal.getId()).compareTo(new Integer(categoriaCanal2.getId()));
            }
        });
    }

    public void setDel(CategoryDelegateGetCategory del) {
        this.del = del;
    }

    static class Holder{
        View viewSelected;

    }

}
