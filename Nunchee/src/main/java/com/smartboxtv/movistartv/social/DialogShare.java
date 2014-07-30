package com.smartboxtv.movistartv.social;


import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.services.DataLoader;

/**
 * Created by Esteban- on 31-05-14.
 */
public class DialogShare extends DialogFragment {

    private String text;
    private String imageUrl;
    private String title;
    private String url;
    private String description;

    private String textTw;
    private boolean isTw = false;
    public DialogShare() {
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, getTheme());
    }

    public DialogShare(String text, String imageUrl, String title, String url) {
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, getTheme());
        //this.text = text;
        this.description = text;
        this.imageUrl = imageUrl;
        this.title = title;
        this.url = url;
    }

    public DialogShare(String text, String description, String imageUrl, String title, String url) {
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, getTheme());
        this.text = text;
        this.description = description;
        this.imageUrl = imageUrl;
        this.title = title;
        this.url = url;
    }

    public DialogShare(String text, boolean isTw){
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, getTheme());
        this.textTw = text;
        this.isTw = isTw;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.facebook_compose, container, false);

        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");


        TextView txtTitle  = (TextView) rootView.findViewById(R.id.header_textview);
        Button button = (Button) rootView.findViewById(R.id.post_button);
        final EditText editText = (EditText)rootView.findViewById(R.id.post_edittext);
        txtTitle.setTypeface(bold);
        button.setTypeface(normal);
        AQuery aq = new AQuery(rootView);
        if(!isTw){

            aq.id(R.id.image_preview).image(this.imageUrl);
            editText.setText(this.text);
        }
        else{
            ImageView imageView = (ImageView) rootView.findViewById(R.id.image_preview2);
            imageView.setVisibility(View.VISIBLE);
            aq.id(R.id.image_preview2).image("https://pbs.twimg.com/profile_images/2284174758/v65oai7fxn47qv9nectx.png");
            Button publish =(Button) rootView.findViewById(R.id.post_button);
            publish.setBackgroundColor(Color.parseColor("#9AE4E8"));
            publish.setText("PUBLICAR EN TWITTER");

            TextView header = (TextView) rootView.findViewById(R.id.header_textview);
            header.setText("Publicar en Twitter");
            editText.setText(this.textTw);
        }


        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isTw){
                    if(editText.getText() != null){
                        Toast.makeText(getActivity(), "Publicando...", Toast.LENGTH_LONG).show();
                        SocialUtil socialUtil = new SocialUtil(getActivity());
                        socialUtil.fbshare(getActivity(), editText.getText().toString(), imageUrl ,url ,title,description, new SocialUtil.SocialUtilHandler() {
                            @Override
                            public void done(Exception e) {
                                dismiss();
                                if(e != null){
                                    Toast.makeText(getActivity(),"No Publicado",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Publicando...", Toast.LENGTH_SHORT).show();
                    if(editText.getText() != null){
                        DataLoader dataLoader = new DataLoader(getActivity());
                        dataLoader.updateStatusTw(new DataLoader.DataLoadedHandler<String>(){
                            @Override
                            public void loaded(String data) {
                                dismiss();
                            }

                            @Override
                            public void error(String error) {
                                Toast.makeText(getActivity(),"No Publicado",Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        },editText.getText().toString());

                    }
                }
             }

        });
        return rootView;
    }
}
