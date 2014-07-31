package com.smartboxtv.movistartv.programation.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.models.CategorieChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 20-04-14.
 */
public class CategoryAdapter extends ArrayAdapter<CategorieChannel> {

    private List<CategorieChannel> list = new ArrayList<CategorieChannel>();
    private View view;

    public CategoryAdapter(Context context, List<CategorieChannel> listaCategoria) {

        super(context, R.layout.category_adapter,listaCategoria);
        list = listaCategoria;
    }

    public View getSelected() {
        return view;
    }

    /*public void setSelected(View view) {

        if(this.view != null){
            this.view.setBackgroundColor(Color.parseColor("#7AB700"));
            //this.view.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        view.setBackgroundColor(Color.parseColor("#17354B"));
        //view.setBackgroundColor(Color.parseColor("#00FF00"));
        this.view = view;
    }*/

    public void setSelected(View view) {

        if(this.view != null){
            this.view.setBackgroundColor(Color.parseColor("#7AB700"));
        }
        view.setBackgroundColor(Color.parseColor("#17354B"));
        this.view = view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;
        ViewHolder holder = null;

        if(rootView == null){

            LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rootView  = inflater.inflate(R.layout.category_adapter,parent, false);

            holder = new ViewHolder();
            holder.category = (TextView) (rootView != null ? rootView.findViewById(R.id.txt_categoria) : null);
            rootView.setTag(holder);
        }
        else{
            holder = (ViewHolder)rootView.getTag();
        }

        if(position == 0 && this.getSelected()== null){
            this.setSelected(rootView);
        }

        Typeface normal = Typeface.createFromAsset(getContext().getAssets(), "fonts/SegoeWP.ttf");

        holder.category.setTypeface(normal);
        holder.category.setText(list.get(position).channelCategoryName);

        return rootView;
    }

    public  static class ViewHolder{
        TextView category;
    }
}
