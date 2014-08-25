package com.smartboxtv.movistartv.programation.preview;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.smartboxtv.movistartv.R;
import com.smartboxtv.movistartv.data.image.Type;
import com.smartboxtv.movistartv.data.image.Width;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.fragments.NUNCHEE;
import com.smartboxtv.movistartv.programation.delegates.PreviewImageFavoriteDelegate;
import com.smartboxtv.movistartv.programation.menu.DialogError;
import com.smartboxtv.movistartv.social.DialogMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Esteban- on 06-05-14.
 */
public class HeaderFragment extends Fragment {

    private final Program programa;
    private final Program programaHora;

    private TextView txtName;
    private TextView txtDate;
    private TextView txtDescription;
    private Button numeroCheck;

    private RelativeLayout containerCheckIn;
    private ImageView imageChannel;

    private ImageView imgFavorite;

    private SimpleDateFormat formatHora;
    private SimpleDateFormat formatDia;

    private RelativeLayout check;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private boolean pendingPublishReauthorization = false;
    private AQuery aq;

    public int NCheckIn = 0;

    // Un delegado cualquiera
    private PreviewImageFavoriteDelegate imageFavoriteDelegate;

    public HeaderFragment(Program program, Program previewProgram) {
        this.programa = program;
        this.programaHora = previewProgram;
    }
    // det del delegado cualquiera
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.preview_fg_header, container, false);

        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH$mm$ss");
        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Bold.ttf");
        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SegoeWP-Light.ttf");


        formatHora = new SimpleDateFormat("HH:mm");
        formatDia = new SimpleDateFormat("MMM dd");
        aq = new AQuery(rootView);

        // TextView
        txtName = (TextView) rootView.findViewById(R.id.preview_nombre);
        txtDate = (TextView) rootView.findViewById(R.id.preview_hora);
        numeroCheck = (Button) rootView.findViewById(R.id.second_preview_check_in_btn);
        txtDescription = (TextView) rootView.findViewById(R.id.preview_descripcion);
        containerCheckIn = (RelativeLayout) rootView.findViewById(R.id.container_check_c);

        check = (RelativeLayout) rootView.findViewById(R.id.second_preview_check_in);

        //numeroCheck.setText(programa.CheckIn);
        Log.e("numero de Check","--> "+NCheckIn);

        if(programa.ICheckIn){
            numeroCheck.setText("+"+(NCheckIn));
            check.setAlpha((float) 0.6);
            check.setEnabled(false);
            check.setClickable(false);
        }
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Publicando...",Toast.LENGTH_LONG).show();
                publishCheckIn();
            }
        });
        numeroCheck.setTypeface(bold);
        txtName.setTypeface(bold);
        txtDescription.setTypeface(light);
        txtDate.setTypeface(normal);

        // ImageView
        imageChannel = (ImageView) rootView.findViewById(R.id.preview_foto_canal);
        ImageView imgProgram = (ImageView) rootView.findViewById(R.id.preview_cabeza_foto);
        imgFavorite = (ImageView) rootView.findViewById(R.id.preview_image_favorito);
        imgFavorite.setVisibility(View.GONE);

        isActual();
        setData();


        return rootView;
    }
    public void muestraImagen(boolean muestaImgen){

        if(muestaImgen){
            imgFavorite.setVisibility(View.VISIBLE);
        }
        else{
            imgFavorite.setVisibility(View.GONE);
        }
    }

    public void setData(){

        txtName.setText(programa.getTitle());

        txtDate.setText(capitalize(formatDia.format(programaHora.getStartDate())) + ", "
                + formatHora.format(programaHora.getStartDate()) + " | " + formatHora.format(programaHora.getEndDate()));


        txtDescription.setText(programa.getDescription());
        //txtChannel.setText(programa.getPChannel().getChannelCallLetter() + " " + programa.getPChannel().getChannelNumber());

        numeroCheck.setText(""+programa.CheckIn);
        Image image  = programa.getImageWidthType(Width.ORIGINAL_IMAGE, Type.BACKDROP_IMAGE);
        if(image != null){
            aq.id(R.id.preview_cabeza_foto).image(image.getImagePath());
        }

        if(programa.isFavorite()){
            imgFavorite.setVisibility(View.VISIBLE);
        }
    }

    public void isActual(){
        Date now = new Date();
        //if(programaHora.getStartDate().getTime()< now.getTime() && now.getTime() < programaHora.EndDate.getTime()){
        if(!((programaHora.StartDate.getTime() > now.getTime())   ||  (programaHora.EndDate.getTime() < now.getTime()))){
            Log.e("Hora",""+programaHora.getStartDate().toString());
            Log.e("Hora-",""+programaHora.getEndDate().toString());
            imageChannel.setVisibility(View.GONE);
            containerCheckIn.setVisibility(View.VISIBLE);
            aq.id(R.id.second_preview_foto_canal).image(programa.getPChannel().getChannelImageURL());


        }
        else{
            imageChannel.setVisibility(View.VISIBLE);
            aq.id(R.id.preview_foto_canal).image(programa.getPChannel().getChannelImageURL());
        }
    }
    public void setImageFavoriteDelegate(PreviewImageFavoriteDelegate imageFavoriteDelegate) {
        this.imageFavoriteDelegate = imageFavoriteDelegate;
    }

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);

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

            String url = "http://www.movistar.cl/PortalMovistarWeb/tv-digital/guia-de-canales";

            Image imagen = programa.getImageWidthType(Width.ORIGINAL_IMAGE,Type.SQUARE_IMAGE);
            String urlImage;

            if( imagen != null){
                urlImage = imagen.getImagePath();
            }
            else{
                urlImage= "https://tvsmartbox.com/MovistarTV/movistartv-post.png";
            }

            String description = " ";

            if(programa.getDescription()!= null){
                description = programa.getDescription();
            }

            Log.e("URL", urlImage);
            Log.e("name", programa.getTitle());
            Log.e("description", description);

            Bundle postParams = new Bundle();
            postParams.putString("name", programa.getTitle());
            postParams.putString("caption", "Movistar TV");
            postParams.putString("description", description);
            postParams.putString("link", url);
            postParams.putString("message", "Estoy viendo "+programa.getTitle());
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

                        if (error == null) {
                            numeroCheck.setText("+"+(programa.CheckIn+1));
                            check.setAlpha((float) 0.6);
                            check.setEnabled(false);
                            check.setClickable(false);
                            ((NUNCHEE)getActivity(). getApplication()).sendAnalitics("Check-In");
                            DialogMessage dialogMessage = new DialogMessage("");
                            dialogMessage.show(getActivity().getSupportFragmentManager(), "");

                        } else {
                            DialogError dialogError = new DialogError("Su mensaje no pudo ser publicado");
                            dialogError.show(getActivity().getSupportFragmentManager(), "");
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
}
