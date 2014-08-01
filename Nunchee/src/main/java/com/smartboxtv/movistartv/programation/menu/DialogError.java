package com.smartboxtv.movistartv.programation.menu;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;

/**
 * Created by Esteban- on 09-05-14.
 */
public class DialogError extends DialogFragment {

    private String mensaje = "";

    public DialogError(String error) {
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
        mensaje = error;
    }
    public DialogError() {
        setStyle(DialogFragment.STYLE_NO_TITLE, getTheme());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_error,container,false);
        TextView txtError = (TextView) (rootView != null ? rootView.findViewById(R.id.text_error) : null);
        Button aceptar = (Button) rootView.findViewById(R.id.button_aceptar);
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        txtError.setTypeface(bold);
        aceptar.setTypeface(normal);

        if(txtError.length() > 1){
            txtError.setText(mensaje);
        }

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        /*ImageView imgContainer = (ImageView) rootView.findViewById(R.id.container_error);
        imgContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout  r = (RelativeLayout) getActivity().findViewById(R.id.contenedor_preview);
                r.removeAllViews();
            }
        });*/
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        return rootView;
    }
}
