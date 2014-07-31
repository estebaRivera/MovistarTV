package com.smartboxtv.movistartv.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.Session;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.animation.ManagerAnimation;
import com.smartboxtv.movistartv.data.image.ScreenShot;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Channel;
import com.smartboxtv.movistartv.data.models.FeedJSON;
import com.smartboxtv.movistartv.data.models.Polls;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.Trivia;
import com.smartboxtv.movistartv.data.models.Tweets;
import com.smartboxtv.movistartv.data.models.UserFacebook;
import com.smartboxtv.movistartv.data.modelssm.LiveSM;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.delgates.UpdateNotificationDelegate;
import com.smartboxtv.movistartv.programation.menu.About;
import com.smartboxtv.movistartv.programation.menu.DialogError;
import com.smartboxtv.movistartv.programation.menu.FavoriteMenuFragment;
import com.smartboxtv.movistartv.programation.menu.FeedFragment;
import com.smartboxtv.movistartv.programation.menu.NotificationFragment;
import com.smartboxtv.movistartv.programation.menu.Politica;
import com.smartboxtv.movistartv.services.DataLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Esteban- on 23-07-14.
 */
public class SearchActivity extends ActionBarActivity {

    private View viewLoading;
    private LayoutInflater inflaterPrivate;
    private RelativeLayout contenedorLoading;
    private RelativeLayout contenedorExitMenu;
    private RelativeLayout contenedorMenuBar;
    private RelativeLayout contenedorNoResult;
    //private RelativeLayout contenedorActionbarOption;
    private boolean isConfiguration = false;
    private boolean isMessage = false;
    private boolean isNotification = false;
    private boolean isSlideMenuOpen = false;
    private boolean fbActivate;
    private boolean showSearch = false;
    private boolean showMenuLive = false;
    private boolean isTrendingOpen = true;

    private List<Program> programList = new ArrayList<Program>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_bar_search);
        loadBack();
        createActionBar();

        final EditText editText = (EditText) findViewById(R.id.text_search);
        ImageView exit = (ImageView) findViewById(R.id.exit);
        ImageView search = (ImageView) findViewById(R.id.image_search);
        ImageView searchExit = (ImageView) findViewById(R.id.image_search_exit);
        inflaterPrivate = LayoutInflater.from(this);
        viewLoading = inflaterPrivate.inflate(R.layout.progress_dialog_search, null);
        contenedorLoading = (RelativeLayout) findViewById(R.id.contenedor_loading);
        overridePendingTransition(R.anim.fade_actvity, R.anim.fade_out_activity);
        LinearLayout wrapper = (LinearLayout) findViewById(R.id.wrapper);
        contenedorNoResult = (RelativeLayout) findViewById(R.id.no_result);

        //ManagerAnimation.fade(wrapper,arrow,exit);


        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( (keyCode == KeyEvent.KEYCODE_SEARCH)) {
                    clean();
                    loading();
                    search(editText.getText().toString());
                    return true;
                }

                return false;
            }
        });

        //String text;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("palabra", editText.getText().toString());
                clean();
                loading();
                search(editText.getText().toString());

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            }
        });
        searchExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    public void clean(){
        RelativeLayout n = (RelativeLayout) findViewById(R.id.no_result);
        n.setVisibility(View.GONE);
        /*RelativeLayout r = (RelativeLayout) findViewById(R.id.no_result);
        r.removeAllViews();*/
    }


    public void search(String cosa){

        if(!cosa.isEmpty()){

            DataLoader dataLoader = new DataLoader(this);
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

                    borraLoading();
                }
            },cosa);
        }
        else{
            Toast.makeText(this , "Ingrese una palabra", Toast.LENGTH_LONG).show();
        }

    }

    public void setData(){
        Typeface bold = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface light = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Light.ttf");
        AQuery aq = new AQuery(this);

        if(programList.size() > 0){
            // RelativeLayout r = (RelativeLayout) rootView.findViewById(R.id.container_message);

            contenedorNoResult.setVisibility(View.GONE);
            LinearLayout l = (LinearLayout) findViewById(R.id.list_data_result);
            l.removeAllViews();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE HH:mm");
            LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //r.setVisibility(View.VISIBLE);
            Log.e("update", "reminder +1");
            LinearLayout[] list = new LinearLayout [programList.size()];
            for(int i = 0 ; i < programList.size();i++){

                final int finalI = i;
                list[i] = new LinearLayout(this);
                View programaResult = inf.inflate(R.layout.element_notification, null);

                TextView name = (TextView) programaResult.findViewById(R.id.txt_nombre_notification);
                TextView date = (TextView) programaResult.findViewById(R.id.txt_fecha_notification);

                ImageView image = (ImageView) programaResult.findViewById(R.id.img_notifications);

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
                programaResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Program p = new Program();
                        p.EndDate = programList.get(finalI).getEndDate();
                        p.StartDate = programList.get(finalI).getStartDate();
                        p.IdProgram = programList.get(finalI).IdProgram;
                        p.PChannel = new Channel();
                        p.PChannel.channelID = programList.get(finalI).PChannel.channelID;

                        RelativeLayout r = (RelativeLayout) findViewById(R.id.view_parent);
                        Bitmap screenShot = ScreenShot.takeScreenshot(r);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        screenShot.compress(Bitmap.CompressFormat.JPEG, 95, stream);
                        byte[] byteArray = stream.toByteArray();

                        try {
                            String filename = getCacheDir()
                                    + File.separator + System.currentTimeMillis() + ".jpg";

                            File f = new File(filename);
                            f.createNewFile();
                            FileOutputStream fo = new FileOutputStream(f);
                            fo.write(byteArray);
                            fo.close();

                            Intent intent = new Intent(SearchActivity.this, PreviewActivity.class);
                            intent.putExtra("background", filename);
                            intent.putExtra("programa", p);
                            startActivity(intent);
                            overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
                            //rootView.setVisibility(View.GONE);
                            //onDestroy();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                list[i].addView(programaResult);
                l.addView(list[i]);
            }
        }
        else{
            Log.e("No resultado","zdgfgzdg");
            contenedorNoResult.setVisibility(View.VISIBLE);
            contenedorNoResult.bringToFront();
        }
    }

    public void loading(){

        clean();
        LayoutInflater inflaterPrivate = LayoutInflater.from(this);

        ImageView imgPopCorn = (ImageView) viewLoading.findViewById(R.id.pop_corn_centro);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Animation animaPop = AnimationUtils.loadAnimation(this, R.anim.animacion_pop_hacia_derecha_centro);

        viewLoading.setLayoutParams(params);
        imgPopCorn.startAnimation(animaPop != null ? animaPop : null);

        contenedorLoading.addView(viewLoading);
        viewLoading.bringToFront();

        Log.e("Loading","Loading");

        contenedorLoading.bringToFront();
        contenedorLoading.setEnabled(false);

    }

    public void borraLoading(){

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.deaparece);

        viewLoading.startAnimation(anim);
        contenedorLoading.removeView(viewLoading);
        contenedorLoading.setEnabled(true);

    }

    public void loadBack(){
        ImageView back = (ImageView) findViewById(R.id.back);
        Bundle extra = this.getIntent().getExtras();
        String path = extra.getString("background");
        Bitmap bm = BitmapFactory.decodeFile(path);
        back.setImageBitmap(bm);
    }



    private void createActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        final LayoutInflater inflater = LayoutInflater.from(this);

        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);

        View view = inflater.inflate(R.layout.action_bar_2_search_a, null);
        ImageView iconSearch = (ImageView) view.findViewById(R.id.icon_search);

        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        actionBar.setCustomView(view,layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
