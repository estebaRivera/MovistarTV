package com.smartboxtv.movistartv.programation.adapterssm;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.modelssm.CategorieChannelSM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 17-06-14.
 */
public class CategoryAdapterSM extends ArrayAdapter<CategorieChannelSM> {

    private List<CategorieChannelSM> listSM = new ArrayList<CategorieChannelSM>();
    private View view;

    public CategoryAdapterSM(Context context, List<CategorieChannelSM> list) {

        super(context, R.layout.category_adapter,list);
        listSM = list;
    }

    public View getSelected() {

        return view;
    }

    public void setSelected(View view) {

        if(this.view != null){
            this.view.setBackgroundColor(Color.parseColor("#17354B"));
        }

        view.setBackgroundColor(Color.parseColor("#7AB700"));
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
        holder.category.setText(listSM.get(position).getName());
        //Log.e("ADAPTER",listSM.get(position).getName());

        return rootView;
    }

    public  static class ViewHolder{
        TextView category;
    }
}
