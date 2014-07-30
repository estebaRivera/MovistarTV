package com.smartboxtv.movistartv.programation.preview;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
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
import com.facebook.widget.FacebookDialog;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.database.DataBase;
import com.smartboxtv.movistartv.data.database.DataBaseUser;
import com.smartboxtv.movistartv.data.database.UserNunchee;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.programation.delegates.PreviewImageFavoriteDelegate;
import com.smartboxtv.movistartv.services.DataLoader;
import com.smartboxtv.movistartv.social.DialogShare;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Esteban- on 06-05-14.
 */
public class ActionFragment extends Fragment {

    //private Program program;
    private final Program previewProgram;
    private final Program programa;
    private Button btnLike;
    private Button btnFavorite;
    private Button btnShare;
    private Button btnReminder;

    private Animation animacion;
    private PreviewImageFavoriteDelegate imageFavoriteDelegate;

    //Facebook is Acrive
    private boolean fbActivate;
    private DataBaseUser dataBaseUser;
    private UserNunchee userNunchee;

    // Share Facebook
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private boolean pendingPublishReauthorization = false;
    private UiLifecycleHelper uiHelper;

    public ActionFragment(Program previewProgram , Program program) {
        this.previewProgram = previewProgram;
        this.programa = program;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.preview_fg_action,container,false);

        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");

        btnLike = (Button) (rootView != null ? rootView.findViewById(R.id.preview_boton_like) : null);
        btnReminder = (Button) (rootView != null ? rootView.findViewById(R.id.preview_boton_recordar) : null);
        btnFavorite = (Button) (rootView != null ? rootView.findViewById(R.id.preview_boton_favorito) : null);
        btnShare =  (Button) (rootView != null ? rootView.findViewById(R.id.preview_boton_compartir) : null);

        TextView title = (TextView) rootView.findViewById(R.id.preview_acciones);
        title.setTypeface(normal);

        // Set Typeface
        btnLike.setTypeface(normal);
        btnReminder.setTypeface(normal);
        btnFavorite.setTypeface(normal);
        btnShare.setTypeface(normal);
        animacion = AnimationUtils.loadAnimation(getActivity(), R.anim.agranda);

        // Publish Facebook
        dataBaseUser = new DataBaseUser(getActivity(),"",null,0);
        userNunchee = dataBaseUser.select(UserPreference.getIdFacebook(getActivity()));
        fbActivate = userNunchee.isFacebookActive;

        // Focos
        Resources res = getResources();

        if(previewProgram.getLike()>1)
            btnLike.setText(previewProgram.getLike()+" Likes");
        else if(previewProgram.getLike()== 1)
            btnLike.setText(previewProgram.getLike()+" Like");

        if(previewProgram.isILike()){
            btnLike.setEnabled(false);
            btnLike.setAlpha((float) 0.5);
        }
        if(previewProgram.isFavorite()){
            btnFavorite.setEnabled(false);
            btnFavorite.setAlpha((float) 0.5);
        }

        Drawable drwFocusShare = res.getDrawable(R.drawable.compartir_foco_acc);
        Drawable drwFocusFavorite = res.getDrawable(R.drawable.favorito_foco_acc);
        Drawable drwFocusLike = res.getDrawable(R.drawable.like_foco_acc);
        Drawable drwFocusReminder = res.getDrawable(R.drawable.recordar_foco_acc);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(fbActivate){
                        DataLoader data = new DataLoader(getActivity());
                        data.actionLike(UserPreference.getIdNunchee(getActivity()), "2", previewProgram.getIdProgram(),
                                previewProgram.getPChannel().getChannelID());

                        btnLike.startAnimation(animacion);
                        btnLike.setText((previewProgram.getLike() + 1) + " Likes");
                        btnLike.setEnabled(false);
                        btnLike.setAlpha((float) 0.5);
                        publishStory();
                    }
                    else{
                       noPublish();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Oops! algo inesperado pasó",Toast.LENGTH_LONG).show();
                }
            }
        });


        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataLoader data = new DataLoader(getActivity());
                data.actionFavorite(UserPreference.getIdNunchee(getActivity()), "4", previewProgram.getIdProgram(),
                        previewProgram.getPChannel().getChannelID());

                btnFavorite.startAnimation(animacion);
                btnFavorite.setAlpha((float) 0.5);
                btnFavorite.setEnabled(false);

                if (imageFavoriteDelegate != null) {
                    imageFavoriteDelegate.showImage(true, ActionFragment.this);
                }

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fbActivate){

                    DataLoader data = new DataLoader(getActivity());
                    data.actionShare(UserPreference.getIdNunchee(getActivity()), "3", previewProgram.getIdProgram(),
                            previewProgram.getPChannel().getChannelID());

                    shareDialog();
                    btnShare.setEnabled(false);
                    btnShare.startAnimation(animacion);
                    btnShare.setAlpha((float) 0.5);
                }
                else{
                    noPublish();
                }

            }
        });

        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnReminder.setEnabled(false);
                btnReminder.startAnimation(animacion);
                btnReminder.setAlpha((float) 0.5);
                recordatorio(programa);
            }
        });

        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        return rootView;
    }

    public void noPublish(){
        Toast.makeText(getActivity(),"Activa el Autopost para poder publicar",Toast.LENGTH_LONG).show();
    }
    public void setImageFavoriteDelegate(PreviewImageFavoriteDelegate imageFavoriteDelegate) {
        this.imageFavoriteDelegate = imageFavoriteDelegate;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    void recordatorio(Program p){

        SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm");

        Log.e("Recordar", "Recordar " + p.getTitle());

        int  id_calendars[] = getCalendar(getActivity());
        long calID = id_calendars[0];
        long startMillis;
        long endMillis;

        //String eventUriString = "content://com.android.calendar/events";

        Calendar cal = Calendar.getInstance();

        Log.e("Calendario",cal.toString());

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(p.getStartDate());
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(p.getEndDate());
        endMillis = endTime.getTimeInMillis();

        TimeZone timeZone = TimeZone.getDefault();

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, p.getTitle());

        if(p.getPChannel().getChannelCallLetter()!= null)
            values.put(CalendarContract.Events.DESCRIPTION,p.getPChannel().getChannelCallLetter()+" "
                    + horaFormat.format(p.getStartDate())+" | "+ horaFormat.format(p.getEndDate())+" "+p.getEpisodeTitle());
        else
            values.put(CalendarContract.Events.DESCRIPTION,p.getPChannel().getChannelName()+" "
                    + horaFormat.format(p.getStartDate())+" | "+ horaFormat.format(p.getEndDate())+" "+p.getEpisodeTitle());

        values.put(CalendarContract.Events.CALENDAR_ID, calID);

        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.ALL_DAY,0);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Log.e("Uri",""+CalendarContract.Events.CONTENT_URI);

        long eventID = Long.parseLong(uri.getLastPathSegment());

        Log.e("EventID",""+eventID);
        Log.e("Uri Reminder",""+CalendarContract.Reminders.CONTENT_URI);

        ContentValues reminderValues = new ContentValues();

        //reminderValues.put(CalendarContract.Reminders.MINUTES, 3);
        reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        //Uri _reminder = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues);

        DataBase dataBase = new DataBase(getActivity(),"",null,1);
        SQLiteDatabase db = dataBase.getWritableDatabase();

        //dataBase.onUpgrade(db,0,0);

        if(db != null){

            String codigo = p.IdProgram;
            String nombre = p.Title;
            String fecha = ""+p.StartDate.getTime();
            String endDate =""+p.EndDate.getTime();
            String codigoChannel = p.PChannel.channelID;
            Image image = p.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
            String urlImage = null;
            if(image != null)
                urlImage = image.getImagePath();

            Log.e("Nombre",p.Title);
            //Insert a la tabla reminder
            String query = "INSERT INTO reminder (id,begin_date,end_date,name,channel, image) " + "VALUES ('" + codigo + "',' "+fecha+"', '" + endDate+"', '" + nombre +"', " +
                    "'" + codigoChannel +"','"+urlImage+"')";
            Log.e("Query",query);
            db.execSQL(query);
        }
        db.close();
        Toast t = Toast.makeText(getActivity(),p.getTitle()+" agregado a tus recordatorios",Toast.LENGTH_LONG);

        t.show();

    }

    public int[] getCalendar(Context c) {

        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = c.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

        int aux[] = new int[0];

        if (managedCursor.moveToFirst()) {

            aux = new int[managedCursor.getCount()];

            int cont = 0;
            do {
                aux[cont] = managedCursor.getInt(0);
                cont++;
            } while (managedCursor.moveToNext());

            managedCursor.close();
        }
        return aux;

    }

    public  void shareDialog(){

        SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd' 'HH'$'mm'$'ss");

        String url = "http://www.movistar.cl/PortalMovistarWeb/tv-digital/programacion";

        Image imagen = programa.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
        String imageUrl;

        if( imagen != null){
            imageUrl = imagen.getImagePath();
        }
        else{
            imageUrl= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
        }
        String title = programa.Title;
        String text;
        if(previewProgram.Description != null)
            text = previewProgram.Description;
        else
            text = "";

        DialogShare dialogShare = new DialogShare(text,imageUrl,title,url);
        dialogShare.show(getActivity().getSupportFragmentManager(),"");
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

            String url = "http://www.movistar.cl/PortalMovistarWeb/tv-digital/programacion";

            Image imagen = previewProgram.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
            String urlImage;

            if( imagen != null){

                urlImage = imagen.getImagePath();
            }
            else{
                urlImage = "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
            }

            String description = " ";

            if(previewProgram.getDescription()!= null){
                description = previewProgram.getDescription();
            }

            Log.e("URL",urlImage);
            Log.e("name", previewProgram.getTitle());
            Log.e("description", description);
            Log.e("Titulo", previewProgram.getTitle());

            Bundle postParams = new Bundle();
            postParams.putString("name", previewProgram.getTitle());
            postParams.putString("caption", "Movistar TV");

            postParams.putString("description", description);
            postParams.putString("link", url);
            postParams.putString("message", "Me gusta "+previewProgram.getTitle());
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

    private void publishCheckIn() {
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

            String url = "http://nunchee.tv/program.html?program="+programa.getIdProgram()+"&channel="
                    +programa.getPChannel().getChannelID()+"&user="+ UserPreference.getIdNunchee(getActivity())
                    +"&action=2&startdate="+hora.format(programa.getStartDate())+"&enddate="+hora.format(programa.getEndDate());

            Image imagen = previewProgram.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
            String urlImage;

            if( imagen != null){

                urlImage = imagen.getImagePath();
            }
            else{
                urlImage = "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
            }

            String description = " ";

            if(previewProgram.getDescription()!= null){
                description = previewProgram.getDescription();
            }

            Log.e("URL",urlImage);
            Log.e("name", previewProgram.getTitle());
            Log.e("description", description);
            Log.e("Titulo", previewProgram.getTitle());

            Bundle postParams = new Bundle();
            postParams.putString("name", previewProgram.getTitle());
            postParams.putString("caption", "Movistar TV");

            postParams.putString("description", description);
            postParams.putString("link", url);
            postParams.putString("message", "Estoy viendo "+previewProgram.getTitle());
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


    private final Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }
}
