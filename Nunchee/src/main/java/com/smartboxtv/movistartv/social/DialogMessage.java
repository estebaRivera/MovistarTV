package com.smartboxtv.movistartv.social;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartboxtv.movistartv.R;
/**
 * Created by Esteban- on 31-07-14.
 */
public class DialogMessage extends DialogFragment {

    private String message;

    public DialogMessage() {
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, getTheme());
    }

    public DialogMessage(String m) {
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, getTheme());
        this.message = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_message,container,false);
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        TextView textMessage = (TextView) rootView.findViewById(R.id.text_error);
        Button aceptar = (Button) rootView.findViewById(R.id.button_aceptar);

        textMessage.setTypeface(normal);
        aceptar.setTypeface(bold);

        if(!message.isEmpty()){
            textMessage.setText(message);
        }

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return rootView;
    }
}
