package com.smartboxtv.movistartv.programation.categories;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.activities.PreviewActivity;
import com.smartboxtv.movistartv.data.database.DataBaseUser;
import com.smartboxtv.movistartv.data.database.UserNunchee;
import com.smartboxtv.movistartv.data.image.ScreenShot;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Channel;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.modelssm.datacategory.ProgramsCategorySM;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.delgates.FacebookLikeDelegate;
import com.smartboxtv.movistartv.programation.adapters.CategoryAdapterContainer;
import com.smartboxtv.movistartv.programation.adapterssm.CategoryAdapterContainerSM;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.services.ServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Created by Esteban- on 20-04-14.
 */
public class CategoryFragmentContainer extends Fragment{

    private GridView gridView;
    private CategoryAdapterContainer adapter;
    private CategoryAdapterContainerSM adapterSM;
    private List<Program> programList = new ArrayList<Program>();
    //private List<ScheduleSM> scheduleSMList = new ArrayList<ScheduleSM>();
    //private List<ChannelSM> listChannel = new ArrayList<ChannelSM>();
    private List<ProgramsCategorySM> listPrograms = new ArrayList<ProgramsCategorySM>();

    private Program program;

    // Share Facebook
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private boolean pendingPublishReauthorization = false;
    private FacebookLikeDelegate facebookDelegate;
    private UiLifecycleHelper uiHelper;

    // Loading
    private RelativeLayout containerLoading;
    private Long fin,delta, inicio = System.currentTimeMillis();
    private View loading;

    //Facebook is Acrive
    private boolean fbActivate;
    private DataBaseUser dataBaseUser;
    private UserNunchee userNunchee;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.category_fragment_container, container, false);

        fin = System.currentTimeMillis();
        delta = fin - inicio;
        Log.e("Tiempo Categoria","Tiempo inicial "+delta);

        gridView = (GridView) (rootView != null ? rootView.findViewById(R.id.grilla_programas_categorias) : null);
        containerLoading = (RelativeLayout) getActivity().findViewById(R.id.contenedor_preview);

        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

        // Publish Facebook
        dataBaseUser = new DataBaseUser(getActivity(),"",null,0);
        userNunchee = dataBaseUser.select(UserPreference.getIdFacebook(getActivity()));
        fbActivate = userNunchee.isFacebookActive;

        facebookDelegate= new FacebookLikeDelegate() {
            @Override
            public void like(Program p) {

                program = p;
                Log.e("Programa Like ", p.getTitle());
                if(p != null){
                    if(fbActivate)
                        publishStory();
                    else{
                        noPublish();
                    }
                }
                else{
                    Log.e("Action Like ","Programa null");
                }
            }

            @Override
            public void checkin(Program p) {
                if(p != null){
                    if(fbActivate)
                        publishStoryCheckIn();
                    else{
                        noPublish();
                    }
                }
                else{
                    Log.e("Action Check In ","Programa Null");
                }
            }
        };
        return rootView;
    }
   private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    public void noPublish(){
        Toast.makeText(getActivity(),"Activa el Autopost para poder publicar",Toast.LENGTH_LONG).show();
    }

    public void cargarProgramas(int idCategoria, Date date){

        loading();

        DataLoader dataLoader = new DataLoader(getActivity());
        dataLoader.getProgramByCategories(new DataLoader.DataLoadedHandler<Program>() {

            @Override
            public void loaded(List<Program> data) {

                programList = new ArrayList<Program>();

                for(int i = 0; i < data.size() ;i++){

                    if(data.get(i).getImageWidthType(Width.ORIGINAL_IMAGE, Type.BACKDROP_IMAGE)!= null){
                        programList.add(data.get(i));
                    }
                }

                ordenaLista();

                adapter = new CategoryAdapterContainer(getActivity(), programList);
                adapter.setFacebookDelegate(facebookDelegate);

                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Program p = programList.get(i);
                        //Log.e("Programa preview--", p.getTitle());
                        /*PreviewFragment preview = new PreviewFragment(p);
                        preview.setPrograma(p);
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.addToBackStack(null);
                        ft.replace(R.id.contenedor_preview, preview);
                        ft.commit();*/

                        RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.view_parent);
                        Bitmap screenShot = ScreenShot.takeScreenshot(r);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        screenShot.compress(Bitmap.CompressFormat.JPEG, 95, stream);
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
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);
                            //context.startActivity(i);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

                /*fin = System.currentTimeMillis();
                delta = fin - inicio;
                Log.e("Tiempo categoria","Tiempo final "+delta);*/

                borraLoading();
            }
            @Override
            public void error(String error) {
                super.error(error);
            }

        }, UserPreference.getIdNunchee(getActivity()),date,idCategoria);
    }

    public void cargarProgramasSM(int idCategoria, Date date){

        loading();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Date  inicio = new Date(date.getTime()- 3600000);
        Date  end = new Date(date.getTime()+ 3600000);

        ServiceManager serviceManager = new ServiceManager(getActivity());
        serviceManager.getProgramationByCategories(new ServiceManager.ServiceManagerHandler<ProgramsCategorySM>(){

            @Override
            public void loaded(List<ProgramsCategorySM> data) {

                listPrograms = data;
                ordenaListaSM();
                //List<ScheduleSM> totalSchedule = new ArrayList<ScheduleSM>();
                int count = 0;
                /*for (int i = 0 ; i < data.size() ;i++){
                    for(int j = 0; j < data.get(i).listSchedule.size();j++){
                        ScheduleSM sm = data.get(i).listSchedule.get(j);
                        totalSchedule.add(sm);
                        count = count + data.get(i).listSchedule.size();
                    }
                }*/
                //listChannel.get(0).listSchedule = scheduleSMList;
                adapterSM = new CategoryAdapterContainerSM(getActivity(),listPrograms);
                adapterSM.setFacebookDelegate(facebookDelegate);
                gridView.setAdapter(adapterSM);
                gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Program p = new Program();
                        p.Title = listPrograms.get(i).title;
                        p.StartDate = listPrograms.get(i).start;
                        p.EndDate = listPrograms.get(i).end;
                        p.PChannel = new Channel();
                        p.PChannel.channelID = ""+listPrograms.get(i).channel.idChannel;
                        p.PChannel.channelCallLetter = listPrograms.get(i).channel.callLetter;
                        p.PChannel.channelImageURL = listPrograms.get(i).channel.urlImage;
                        p.IdProgram = ""+listPrograms.get(i).id;
                        p.IdEpisode = ""+listPrograms.get(i).episode.id;
                        Log.e("Id Episode","--> "+listPrograms.get(i).episode.id);

                        RelativeLayout r = (RelativeLayout) getActivity().findViewById(R.id.view_parent);
                        Bitmap screenShot = ScreenShot.takeScreenshot(r);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        screenShot.compress(Bitmap.CompressFormat.JPEG, 95, stream);
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
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.zoom_in_preview, R.anim.nada);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                borraLoading();
            }

            @Override
            public void error(String error) {
                Log.e("DATA ERROR","--> "+error);
            }
        },"1","CHL","ES",format.format(inicio),format.format(end),"1",""+idCategoria);
    }

    public void loading(){
        loading = getLayoutInflater(null).inflate(R.layout.progress_dialog_pop_corn, null);
        ImageView popCorn = (ImageView) (loading != null ? loading.findViewById(R.id.pop_corn_centro) : null);
        RelativeLayout containerPopCorn = (RelativeLayout) loading.findViewById(R.id.pop_corn);
        containerPopCorn.setBackgroundColor(Color.parseColor("#77000000"));
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Animation animaPop = AnimationUtils.loadAnimation(getActivity(), R.anim.animacion_pop_hacia_derecha_centro);
        loading.setLayoutParams(params);
        popCorn.startAnimation(animaPop != null ? animaPop : null);
        containerLoading.addView(loading);
        loading.bringToFront();
        containerLoading.setEnabled(false);
    }
   public void borraLoading(){
        Animation anim = AnimationUtils.loadAnimation(getActivity(),R.anim.deaparece);
        loading.startAnimation(anim != null ? anim : null);
        containerLoading.removeView(loading);
        containerLoading.setEnabled(true);
    }

    public void ordenaLista(){
        Collections.sort(programList);
    }

    public void ordenaListaSM(){
        Collections.sort(listPrograms);
    }
    private void publishStory() {
            Session session = Session.getActiveSession();

            if (session != null){

                // Check for publish permissions
                Log.e("Session","No null");
                List<String> permissions = session.getPermissions();
                if (!isSubsetOf(PERMISSIONS, permissions)) {
                    pendingPublishReauthorization = true;
                    Session.NewPermissionsRequest newPermissionsRequest = new Session
                            .NewPermissionsRequest(this, PERMISSIONS);
                    session.requestNewPublishPermissions(newPermissionsRequest);
                    return;
                }
                SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd' 'HH'$'mm'$'ss");

                String url = "http://nunchee.tv/program.html?program="+program.getIdProgram()+"&channel="
                        +program.getPChannel().getChannelID()+"&user="+UserPreference.getIdNunchee(getActivity())
                        +"&action=2&startdate="+hora.format(program.getStartDate())+"&enddate="+hora.format(program.getEndDate());

                Image imagen = program.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
                String urlImage;

                if( imagen != null){

                    urlImage = imagen.getImagePath();
                }
                else{
                    urlImage= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
                }

                String description = " ";

                if(program.getDescription()!= null){
                    description = program.getDescription();
                }
                Log.e("URL",urlImage);
                Log.e("name", program.getTitle());
                Log.e("description", description);
                Log.e("Titulo",program.getTitle());

                Bundle postParams = new Bundle();
                postParams.putString("name", program.getTitle());
                postParams.putString("caption", "Movistar TV");
                postParams.putString("description", description);
                postParams.putString("link", url);
                postParams.putString("message", "Me gusta "+program.getTitle());
                postParams.putString("picture", urlImage);

                Request.Callback callback= new Request.Callback() {
                    public void onCompleted(Response response) {

                        String postId = null;
                        if(response != null){

                            GraphObject graphObject = response.getGraphObject();
                            if(graphObject != null){

                                JSONObject graphResponse = response
                                        .getGraphObject()
                                        .getInnerJSONObject();

                                try {
                                    postId = graphResponse.getString("id");
                                } catch (JSONException e) {
                                    Log.i("TAG",
                                            "JSON error "+ e.getMessage());
                                }
                            }

                            FacebookRequestError error = response.getError();
                            if (error != null) {
                                Toast.makeText(getActivity()
                                        .getApplicationContext(),
                                        "Ups, algo salió mal, intenta de nuevo",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity()
                                        .getApplicationContext(),
                                        "Publicado correctamente",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                };

                Request request = new Request(session, "me/feed", postParams,
                        HttpMethod.POST, callback);

                RequestAsyncTask task = new RequestAsyncTask(request);
                task.execute();
            }
            else{
                Log.e("Session","Null");
            }

        }
    private void publishStoryCheckIn() {
        Session session = Session.getActiveSession();

        if (session != null){

            // Check for publish permissions
            Log.e("Session","No null");
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(this, PERMISSIONS);
                session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }
            SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd' 'HH'$'mm'$'ss");

            String url = "http://nunchee.tv/program.html?program="+program.getIdProgram()+"&channel="
                    +program.getPChannel().getChannelID()+"&user="+UserPreference.getIdNunchee(getActivity())
                    +"&action=2&startdate="+hora.format(program.getStartDate())+"&enddate="+hora.format(program.getEndDate());

            String imagen;

            if(program.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE)!= null){

                imagen = program.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE).getImagePath();
            }
            else{
                imagen= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
            }

            Bundle postParams = new Bundle();
            postParams.putString("name", program.getTitle());
            postParams.putString("caption", "Movistar TV");
            postParams.putString("description", program.getDescription());
            postParams.putString("link", url);
            postParams.putString("message", "Check-In en  "+program.getTitle());
            postParams.putString("picture", imagen);

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {

                    String postId = null;
                    if(response != null){

                        GraphObject graphObject = response.getGraphObject();
                        if(graphObject != null){

                            JSONObject graphResponse = response
                                    .getGraphObject()
                                    .getInnerJSONObject();

                            try {
                                postId = graphResponse.getString("id");
                            } catch (JSONException e) {
                                Log.i("TAG",
                                        "JSON error "+ e.getMessage());
                            }
                        }

                        FacebookRequestError error = response.getError();
                        if (error != null) {
                            Toast.makeText(getActivity()
                                    .getApplicationContext(),
                                    error.getErrorMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity()
                                    .getApplicationContext(),
                                    postId,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }
            };

            Request request = new Request(session, "me/feed", postParams,
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }
        else{
            Log.e("Session","Null");
        }

    }
    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

   private void onSessionStateChange(Session session, SessionState state, Exception exception) {

        if (pendingPublishReauthorization &&
                state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
            pendingPublishReauthorization = false;
            publishStory();
        }
    }
}
