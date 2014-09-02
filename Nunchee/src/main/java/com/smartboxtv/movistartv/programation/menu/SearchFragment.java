package com.smartboxtv.movistartv.programation.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.activities.PreviewActivity;
import com.smartboxtv.movistartv.data.image.ScreenShot;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Channel;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.modelssm.datahorary.DataResultSM;
import com.smartboxtv.movistartv.services.DataLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Esteban- on 02-07-14.
 */
public class SearchFragment extends Fragment {

    private View rootView;
    private List<Program> programList = new ArrayList<Program>();
    private DataResultSM resultado;
    private View viewLoading;
    private RelativeLayout contenedorLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.action_bar_search,container,false);
        final EditText editText = (EditText) rootView.findViewById(R.id.text_search);
        ImageView exit = (ImageView) rootView.findViewById(R.id.exit);
        ImageView search = (ImageView) rootView.findViewById(R.id.image_search);
        ImageView searchExit = (ImageView) rootView.findViewById(R.id.image_search_exit);
        LayoutInflater inflaterPrivate = LayoutInflater.from(getActivity());
        viewLoading = inflaterPrivate.inflate(R.layout.progress_dialog_search, null);
        contenedorLoading = (RelativeLayout) rootView.findViewById(R.id.contenedor_loading);

        LinearLayout wrapper = (LinearLayout) rootView.findViewById(R.id.wrapper);

        //ManagerAnimation.fade(wrapper,arrow,exit);

        //String text;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("palabra", editText.getText().toString());
                loading();
                search(editText.getText().toString());
            }
        });
        searchExit.setOnClickListener(new View.OnClickListener() { // X
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
            }
        });
        return rootView;
    }

    public void searchSM(){
        //Toast.makeText(getActivity(),"Buscando... (Si claro)", Toast.LENGTH_LONG).show();
        //ServiceManager serviceManager
    }

    public void clean(){
        /*RelativeLayout n = (RelativeLayout) rootView.findViewById(R.id.no_result);
        n.setVisibility(View.GONE);*/
        RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.no_result);
        r.removeAllViews();
    }

    public void search(String cosa){

        if(!cosa.isEmpty()){

            DataLoader dataLoader = new DataLoader(getActivity());
            dataLoader.search(new DataLoader.DataLoadedHandler<Program>(){
                @Override
                public void loaded(List<Program> data) {
                    programList = data;
                    clean();
                    setData();
                    borraLoading();
                }

                @Override
                public void error(String error) {
                    Log.e("Error","--> "+error);
                }
            },cosa);
        }
        else{
            Toast.makeText(getActivity(), "Ingrese una palabra",Toast.LENGTH_LONG).show();
        }

    }

    public void setData(){

        //Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Light.ttf");
        AQuery aq = new AQuery(rootView);

        if(programList.size() > 0){
           // RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.container_message);

            RelativeLayout n = (RelativeLayout) rootView.findViewById(R.id.no_result);
            n.setVisibility(View.GONE);

            LinearLayout l = (LinearLayout) rootView.findViewById(R.id.list_data_result);
            l.removeAllViews();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE HH:mm");
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //r.setVisibility(View.VISIBLE);
            Log.e("update", "reminder +1");
            LinearLayout[] list = new LinearLayout [programList.size()];
            for(int i = 0 ; i < programList.size();i++){

                final int finalI = i;
                list[i] = new LinearLayout(getActivity());
                View notification = inf.inflate(R.layout.element_notification, null);

                TextView name = (TextView) notification.findViewById(R.id.txt_nombre_notification);
                TextView date = (TextView) notification.findViewById(R.id.txt_fecha_notification);

                ImageView image = (ImageView) notification.findViewById(R.id.img_notifications);

                if(programList.get(i).getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE)!=null)

                    aq.id(image).image(programList.get(i).getImageWidthType(Width.ORIGINAL_IMAGE, Type.SQUARE_IMAGE).getImagePath());
                    date.setTypeface(light);
                    name.setTypeface(bold);
                    if(programList.get(i).getTitle().length() > 22){
                        name.setText(programList.get(i).getTitle().substring(0,19)+"...");
                    }
                    else{
                        name.setText(programList.get(i).getTitle());
                    }

                    date.setText(dateFormat.format(programList.get(i).getStartDate()));

                notification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Program p = new Program();
                        p.EndDate = programList.get(finalI).getEndDate();
                        p.StartDate = programList.get(finalI).getStartDate();
                        p.IdProgram = programList.get(finalI).IdProgram;
                        p.PChannel = new Channel();
                        p.PChannel.channelID = programList.get(finalI).PChannel.channelID;

                        rootView.setVisibility(View.INVISIBLE);
                        RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.view_parent);
                        Bitmap screenShot = ScreenShot.takeScreenshot(r);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        screenShot.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] byteArray = stream.toByteArray();

                        try {
                            String filename = getActivity().getCacheDir()
                                    + File.separator + System.currentTimeMillis() + ".jpg";

                            File f = new File(filename);
                            f.createNewFile();
                            FileOutputStream fo = new FileOutputStream(f);
                            fo.write(byteArray);
                            fo.close();

                            Intent intent = new Intent(getActivity(), PreviewActivity.class);
                            intent.putExtra("background", filename);
                            intent.putExtra("programa", p);
                            intent.putExtra("file",f);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
                            rootView.setVisibility(View.GONE);
                            onDestroy();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                list[i].addView(notification);
                l.addView(list[i]);
            }
        }
        else{
            RelativeLayout n = (RelativeLayout) rootView.findViewById(R.id.no_result);
            n.setVisibility(View.VISIBLE);
        }
    }
    public void finish(){

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(rootView, View.ALPHA,1,0);
        AnimatorSet animView = new AnimatorSet();

        animView.play(animatorX);
        animView.setDuration(400);

        animView.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //onDetach();

                RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.contenedor_menu_bar);
                r.removeAllViews();
                onDestroy();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animView.start();
    }

    public void loading(){

        LayoutInflater inflaterPrivate = LayoutInflater.from(getActivity());

        ImageView imgPopCorn = (ImageView) viewLoading.findViewById(R.id.pop_corn_centro);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Animation animaPop = AnimationUtils.loadAnimation(getActivity(), R.anim.animacion_pop_hacia_derecha_centro);

        viewLoading.setLayoutParams(params);
        imgPopCorn.startAnimation(animaPop != null ? animaPop : null);

        contenedorLoading.addView(viewLoading);
        viewLoading.bringToFront();

        Log.e("Loading","Loading");

        contenedorLoading.bringToFront();
        contenedorLoading.setEnabled(false);

    }

    public void borraLoading(){

        Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.deaparece);

        assert anim != null;
        viewLoading.startAnimation(anim);
        contenedorLoading.removeView(viewLoading);
        contenedorLoading.setEnabled(true);

    }
}
