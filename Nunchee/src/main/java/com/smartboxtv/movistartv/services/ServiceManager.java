package com.smartboxtv.movistartv.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.smartboxtv.movistartv.data.database.UserNunchee;
import com.smartboxtv.movistartv.data.models.Channel;
import com.smartboxtv.movistartv.data.models.FeedJSON;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.Recommendations;
import com.smartboxtv.movistartv.data.models.Trivia;
import com.smartboxtv.movistartv.data.models.TriviaAnswers;
import com.smartboxtv.movistartv.data.models.TriviaQuestion;
import com.smartboxtv.movistartv.data.modelssm.CategorieChannelSM;
import com.smartboxtv.movistartv.data.modelssm.ChannelSM;
import com.smartboxtv.movistartv.data.modelssm.DataLoginSM;
import com.smartboxtv.movistartv.data.modelssm.EpisodeSM;
import com.smartboxtv.movistartv.data.modelssm.LiveStream;
import com.smartboxtv.movistartv.data.modelssm.LiveStreamSchedule;
import com.smartboxtv.movistartv.data.modelssm.ProgramSM;
import com.smartboxtv.movistartv.data.modelssm.ScheduleSM;
import com.smartboxtv.movistartv.data.modelssm.TokenIssue;
import com.smartboxtv.movistartv.data.modelssm.TweetSM;
import com.smartboxtv.movistartv.data.modelssm.datacategory.ChannelCategorySM;
import com.smartboxtv.movistartv.data.modelssm.datacategory.ProgramsCategorySM;
import com.smartboxtv.movistartv.data.modelssm.datafavorites.ChannelFavoriteSM;
import com.smartboxtv.movistartv.data.modelssm.datafavorites.ProgramFavoriteSM;
import com.smartboxtv.movistartv.data.modelssm.datafavorites.ScheduleFavoriteSM;
import com.smartboxtv.movistartv.data.modelssm.datahorary.DataResultSM;
import com.smartboxtv.movistartv.data.modelssm.datarecommendation.EpisodeRecommendationSM;
import com.smartboxtv.movistartv.data.modelssm.datarecommendation.RecommendationSM;
import com.smartboxtv.movistartv.data.preference.UserPreferenceSM;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Esteban- on 16-06-14.
 */
public class ServiceManager {

    private static final String TAG = "SERVICE MANAGER";
    private static final String ERROR = "SERVICIO CAIDO";
    private final long EXPIRE = 10000;

    private final int PROGRAM_TYPE_BACKDROP = 0;
    private final int PROGRAM_TYPE_POSTER = 1;
    private final int PROGRAM_TYPE_SQUARE = 2;
    private final int CHANNEL_TYPE_ICON_LIGHT = 3;
    private final int CHANNEL_TYPE_ICON_DARK = 4;

    private final String TEST_URL = "http://54.207.109.226:5500/api/1.0/guide/";
    private final String USER = "53bdadc825822c11f84fc067";

    private static final String BASE_URL_SERVICES = "http://23.21.72.216:80/nunchee/api/1.0/guide";
    private static final String BASE_URL_SERVICES_USER = "http://23.21.72.216:80/nunchee/api/1.0/users";

    private static final String BASE_URL_SERVICES_RECOMMENDATION = "http://23.21.72.216:80/nunchee/api/1.0/recommendation";
    private static final String BASE_URL_STREAM_MANAGER = "https://api.streammanager.co/api/";

    private static final String API_TOKEN = "8fc221e56408966fe7999c7c1edff220";//"b5430d55dc64849b1a34e877267e72ba";//"b5430d55dc64849b1a34e877267e72ba";//"bace2022792e7943635001c8696a013f";
    private static final String SERVICES_URL_TRENDING = "http://190.215.44.18/wcfNunchee2/GLFService.svc/Trending";
    private static final String URL_TWITTER = "https://api.twitter.com/1.1/statuses/";
    public static final String REFRESH_GRANT_TYPE = "refresh_token";
    private static String tokenMovistar;
    private static String tokenFacebook;
    private static String idDevice;

    private AQuery aq;
    private String URL;
    private long inicio, fin, delta;
    private final Context context;

    private int nivelTrivia = 1;
    private Trivia trivia;
    private List<TriviaQuestion> questions;

    private int count = 0;

    private List<FeedJSON> listaHistorial = new ArrayList<FeedJSON>();

    public ServiceManager(Context context) {
        this.context = context;
        aq = new AQuery(context);
    }

    public ServiceManager(Activity activity) {
        this.context = activity;
        aq = new AQuery(context);
    }

    // Login y Logout
    public void  loginStandar(){

    }


    public void  loginFacebook( final ServiceManagerHandler<DataLoginSM> handler, final String token, final String deviceId){

        String deviceType = "1";
        URL = BASE_URL_SERVICES_USER+"/loginFacebook?facebook_token="+token+"&device_id="+deviceId+"&device_type="+deviceType;
        tokenFacebook = token;
        idDevice = deviceId;
        Log.e(TAG +"url login", URL);
        Map<String, Object> map = new HashMap<String, Object>();

        aq.ajax(URL, map, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                if (object != null && status.getCode() == 200) {
                    try {
                        Log.e("DFG", object.getString("message"));
                        if (object.getString("message").equals("OK")) {
                            JSONObject data = object.getJSONObject("data");
                            String id = data.getString("user");
                            String token = data.getString("token");
                            DataLoginSM dataLoginSM = new DataLoginSM();
                            dataLoginSM.id = id;
                            dataLoginSM.token = token;
                            tokenMovistar = token;
                            Log.e("token Movistar TV", token);
                            AutoLogin autoLogin = new AutoLogin();
                            autoLogin.execute();
                            handler.loaded(dataLoginSM);

                        }else if (object.getString("message").equals("The user not exists") && count < 3) {

                            count++;
                            Log.e("Registro","Usuario");
                            userFacebook(new ServiceManagerHandler<String>() {
                                @Override
                                public void loaded(String data) {
                                    loginFacebook(handler, token, deviceId);
                                }

                                @Override
                                public void error(String error) {
                                    super.error(error);
                                }
                            }, token);
                        }
                        else if(object.getString("message").equals("#2 - Users(1) at the limit(1), you need to close 1 session(s)")){
                            Log.e("Limite de tiempo","Limite de tiempo");
                        }
                        else{
                            Log.e("Ni una"," Waaaaaaaaaaa");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG + " loginFacebook", e.getMessage());
                        handler.error(e.getMessage());
                    }
                } else {
                    Log.e(TAG + " loginFacebook-", ERROR);
                    Log.e(TAG, object.toString());
                    handler.error(ERROR);
                }
            }
        }.header("Content-Type", "application/x-www-form-urlencoded"));
    }

    public void  reLoginFacebook( final String facebookToken, final String deviceId){

        String deviceType = "1";
        URL = BASE_URL_SERVICES_USER+"/loginFacebook?facebook_token="+facebookToken+"&device_id="+deviceId+"&device_type="+deviceType+"&device_token="+tokenMovistar;

        Map<String, Object> map = new HashMap<String, Object>();
        Log.e(TAG+" reLogin",URL);
        aq.ajax(URL,map, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                if (object != null && status.getCode() == 200) {
                    try {
                        if (object.getString("message").equals("OK")) {
                            JSONObject data = object.getJSONObject("data");
                            String id = data.getString("user");
                            String token = data.getString("token");
                            tokenMovistar = token;
                            Log.e(TAG +"Token nuevo ", tokenMovistar);
                            AutoLogin autoLogin = new AutoLogin();
                            autoLogin.execute();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG + " n ", e.getMessage());
                    }
                } else {
                    Log.e(TAG + " reLoginFacebook-", ERROR);
                }
            }
        }.header("Content-Type", "application/x-www-form-urlencoded"));
    }

    public void userFacebook( final ServiceManagerHandler<String> handler,String token){

        String language = "es";
        String provider = "1";

        URL = BASE_URL_SERVICES_USER+"/userFacebook?facebook_token="+token+"&language="+language+"&provider="+provider;
        Map<String, Object> map = new HashMap<String, Object>();

        Log.e("userFacebook",URL);
        aq.ajax(URL, map, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if (object != null) {
                    try {
                        String result = object.getString("message");
                        if (result.equals("OK")){
                            handler.loaded(result);
                        }
                        else{
                            Log.e("Error 5656", result);
                            handler.error(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.error(e.getMessage());
                        Log.e(TAG + " userFacebook", e.getMessage());
                    }
                } else {
                    handler.error("object null");
                }
            }
        }.header("Content-Type", "application/x-www-form-urlencoded"));
    }
    public void logoutFacebook(final ServiceManagerHandler <String> handler,String facebookToken, String deviceId, String deviceType, String deviceToken){

        URL = BASE_URL_SERVICES_USER+"/logoutFacebook?facebook_token="+facebookToken+"&device_id"+deviceId+"&device_type"+deviceType+"&device_token"+deviceToken;
        Map<String, Object> map = new HashMap<String, Object>();

        Log.e(TAG, URL);
        aq.ajax(URL, map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if (object != null && status.getCode() == 200) {

                    try {
                        if (object.getString("message").equals("OK")) {
                            handler.loaded("OK");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG + " logoutFacebook", e.getMessage());
                        handler.error(e.getMessage());
                    }
                } else {
                    handler.error(ERROR);
                }
            }
        }.header("Content-Type", "application/x-www-form-urlencoded"));
    }

    // Geo

    // CATEGORIES
    // Servicio numero 1
    public void getCategories(final ServiceManagerHandler<CategorieChannelSM> handler, String language, int id){
        inicio = System.currentTimeMillis();

        URL = BASE_URL_SERVICES+"getCategories?table="+id+"&language="+language;

        Log.e(TAG+" getCategories",URL);
        aq.ajax(URL,JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {


                if(!object.isNull("data")){
                    //Log.e("DATA",object.toString());
                    //Toast.makeText(aq.getContext(), status.getCode() + ":" + object.toString(), Toast.LENGTH_LONG).show();
                    try {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray categories = data.getJSONArray("categories");
                        List<CategorieChannelSM> list = new ArrayList<CategorieChannelSM>();

                        for(int i = 0; i<categories.length(); i++){
                            CategorieChannelSM cc = parseJsonObject(categories.getJSONObject(i), CategorieChannelSM.class);
                            //Log.e("Nombre categoria "+i,cc.name);
                            //Language language = parseJsonObject(categories.getJSONObject(i).getJSONObject("language"),Language.class);
                            //cc.setLanguage(new Language()) ;
                            //cc.setLanguage(language);
                            fin = System.currentTimeMillis();
                            delta = fin - inicio;
                            Log.e("Tiempo por categoria",cc.name+" --> "+delta);
                            list.add(cc);
                        }
                        Log.e("Tiempo total","--> "+delta);
                        handler.loaded(list);
                    } catch (Exception e) {
                        Log.e("ERROR --",e.getMessage());
                        e.printStackTrace();
                        handler.error(e.getMessage());
                    }
                }
                else if(object!= null){
                    Log.e(TAG,"Error 2 :" + status.getCode());
                    Toast.makeText(aq.getContext(), "Error 2 :" + status.getCode(), Toast.LENGTH_LONG).show();
                }
                else{
                    Log.e(TAG,"Error :" + status.getCode());
                    Toast.makeText(aq.getContext(), "Error:" + status.getCode(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //  Servicio numero 2
    public void getProgramationByCategories(final ServiceManagerHandler<ProgramsCategorySM> handler, String userNunchee, String country, String language, String dateStart,
                                            String dateEnd, String deviceType, String idCategory ){

       userNunchee = "53a4397f25822c0710b299e9";
       inicio = System.currentTimeMillis();
       URL =   BASE_URL_SERVICES+"getPrograms?user="+userNunchee+"&country="+country+"&language="+language+"&start="+dateStart+"&end="+dateEnd+"&device_type"
                +"channel_image_type=3&program_image_type=1";
        //Log.e(TAG,URL);

        //URL = TEST_URL+"getPrograms?user="+USER+"&category="+idCategory+"&start="+dateStart+"&end="+dateEnd+"&device_type=1&channel_image_type=4&program_image_type=0";
        Log.e(TAG+" getProgramationbyCategories",URL);

        aq.ajax(URL,JSONObject.class,new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if(object != null){
                    try{
                            JSONObject data = object.getJSONObject("data");
                            JSONArray guide = data.getJSONArray("programs");
                            List<ProgramsCategorySM> list = new ArrayList<ProgramsCategorySM>();

                            if(guide != null){
                                for(int i = 0; i < guide.length();i++){
                                    ProgramsCategorySM p = parseJsonObject(guide.getJSONObject(i), ProgramsCategorySM.class);

                                    JSONObject episode = guide.getJSONObject(i).getJSONObject("episode");
                                    JSONObject channel = guide.getJSONObject(i).getJSONObject("channel");

                                    EpisodeSM e = parseJsonObject(episode, EpisodeSM.class);
                                    ChannelCategorySM c = parseJsonObject(channel, ChannelCategorySM.class);

                                    p.episode = e;
                                    p.channel = c;
                                    if(p.image != null){
                                        list.add(p);
                                    }
                                }

                                handler.loaded(list);
                            }
                        } catch (Exception e) {
                            handler.error(e.getMessage());
                            e.printStackTrace();

                    }
                }
                else{
                    Log.e(ERROR,"");
                    handler.error(ERROR);
                }
            }
        });
    }

    // HORARY
    // Servicio numero 3
    public void getProgramation(final ServiceManagerHandler<ChannelSM> handler, String userNunchee, String dateStart,
                                            String dateEnd, String deviceType, String idCategory ){

        userNunchee = "53a4397f25822c0710b299e9";
        dateStart = "07:00:00 01-07-2014";
        dateEnd = "12:00:00 01-07-2014";
        inicio = System.currentTimeMillis();

        URL =   BASE_URL_SERVICES+"guideForUser?user="+userNunchee+"&start="+dateStart+"&end="+dateEnd+"&device_type"
                +"channel_image_type=3&program_image_type=1";

        Log.e(TAG+" getProgramation",URL);
        aq.ajax(URL,JSONObject.class,new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if(object != null){
                    try{
                        JSONObject data = object.getJSONObject("data");
                        JSONArray guide = data.getJSONArray("channels_guide");
                        List<ChannelSM> list = new ArrayList<ChannelSM>();

                        if(guide != null){
                            for(int i = 0; i < guide.length();i++){
                                ChannelSM c = parseJsonObject(guide.getJSONObject(i), ChannelSM.class);
                                //Log.e("Nombre Canal "+i,c.name);
                                List<ScheduleSM> listSchedule = new ArrayList<ScheduleSM>();
                                //List<ProgramSM> listProgram = new ArrayList<ProgramSM>();
                                //List<EpisodeSM> listEpisode = new ArrayList<EpisodeSM>();

                                JSONArray schedule = guide.getJSONObject(i).getJSONArray("schedule");
                                for(int j = 0; j < schedule.length() ; j++){

                                    ScheduleSM s = parseJsonObject(schedule.getJSONObject(j), ScheduleSM.class);
                                    //Log.e("ScheduleSM",s.title);
                                    JSONObject episode = schedule.getJSONObject(j).getJSONObject("episode");
                                    JSONObject program = schedule.getJSONObject(j).getJSONObject("program");
                                    ProgramSM p = parseJsonObject(program, ProgramSM.class);
                                    EpisodeSM e = parseJsonObject(episode, EpisodeSM.class);
                                    s.program = p;
                                    s.episode = e;
                                    listSchedule.add(s);
                                }
                                c.listSchedule = listSchedule;
                                list.add(c);
                            }
                        }
                        int count = 0;
                        for(int i = 0; i < list.size();i++){
                            count = count + list.get(i).listSchedule.size();
                            for(int j = 0;j < list.get(i).listSchedule.size()  ;j++){
                                Log.e("Canal - Programa",""+list.get(i).getName()+" - "+list.get(i).getListProgram().get(j).title+" - "+list.get(i).getListProgram().get(j).getStartDate());
                            }

                        }
                        fin = System.currentTimeMillis();
                        delta = ((fin - inicio) / 1000);
                        Log.e("Tamaño lista final","--> "+count);
                        Log.e("Tiempo total","--> "+delta);
                        handler.loaded(list);
                    }
                    catch (Exception e){
                        Log.e("error",e.getMessage());
                        e.printStackTrace();
                        handler.error(e.getMessage());
                    }

                }
                else{
                    Log.e(ERROR,"getProgramation");
                    handler.error(ERROR);
                }
            }
        });
    }

    // FAVORITES
    //  Servicio numero 4
    public void getFavoriteForDay( final ServiceManagerHandler<ProgramFavoriteSM> handler, String userNunchee, String startDate, String endDate){

        userNunchee = "53bdadc825822c11f84fc067";

        URL = TEST_URL+"getFavoriteForDay?user="+USER+"&date="+startDate+"&device_type=1&channel_image_type=4&program_image_type=2";
        Log.e(TAG+" getFavoriteForDay",URL);

        AjaxCallback<JSONObject> ajaxCallback = new AjaxCallback<JSONObject>(){
            @Override
            public void callback( String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);

                if(status.getCode() == AjaxStatus.NETWORK_ERROR){
                    Log.e("Status",""+status.getTime());
                    Log.e("Status",""+status.getMessage());

                }
                if(object != null){
                    try {

                        JSONArray programs = object.getJSONObject("data").getJSONArray("programs");
                        List<ProgramFavoriteSM> list = new ArrayList<ProgramFavoriteSM>();

                        for(int i = 0; i < programs.length() ;i++){

                            ProgramFavoriteSM p = parseJsonObject(programs.getJSONObject(i),ProgramFavoriteSM.class);
                            JSONArray schedule = programs.getJSONObject(i).getJSONArray("schedule");
                            List<ScheduleFavoriteSM> listSchedule = new ArrayList<ScheduleFavoriteSM>();
                            //Log.e("Programa",p.name);
                            for(int j = 0 ; j < schedule.length() ;j++){
                                ScheduleFavoriteSM s = parseJsonObject(schedule.getJSONObject(j),ScheduleFavoriteSM.class);
                                ChannelFavoriteSM c = parseJsonObject(schedule.getJSONObject(j).getJSONObject("channel"),ChannelFavoriteSM.class);
                                //Log.e("Schedule",s.name);
                                //Log.e("Nombre canal",c.callLetter);
                                //Log.e("image",c.image);
                                //Log.e("Numero",""+c.id );
                                s.channel = c;
                                listSchedule.add(s);
                                p.schedule = listSchedule;
                            }
                            list.add(p);
                        }
                        handler.loaded(list);

                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.error(e.getMessage());
                        Log.e(TAG, e.getMessage());
                    }
                }
                else{
                    Log.e(ERROR,"getFavoriteForDay");
                }
            }
        };

        ajaxCallback.setTimeout(15000);
        aq.ajax(URL,JSONObject.class, ajaxCallback);
    }

    // PREVIEW
    //  Servicio numero 5
    public void getPreview(final ServiceManagerHandler<Program> handler,String userNunchee, String idProgram, String idEpisode, String nombreCanal, String imageCanal, Date start, Date end){

        userNunchee = "53bdadc825822c11f84fc067";

        URL =   BASE_URL_SERVICES+"getProgram?user="+userNunchee+"&program="+idProgram+"&device_type=1";

        final Program p = new Program();
        p.PChannel = new Channel();
        p.PChannel.channelCallLetter = nombreCanal;
        if(imageCanal != null)
            p.PChannel.channelImageURL = imageCanal;
        else
            p.PChannel.channelImageURL = "zdsvnzflkv";
        p.EndDate = end;
        p.StartDate = start;


        aq.ajax(URL,JSONObject.class, new AjaxCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if(object != null){
                    try{
                        JSONObject program = object.getJSONObject("data").getJSONObject("program");

                        //p.IsFavorite = program.getBoolean("IsFavorite");
                        //p.ILike = program.getBoolean("ILike");
                        //p.ICheckIn = program.getBoolean("ICheckIn");

                        JSONArray images = program.getJSONArray("image");
                        if(images.length()> 0 ){
                            String img = images.getString(0);
                            p.urlImage = img;
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e("Error preview","--> "+e.getMessage());
                    }
                }
                else{
                    Log.e(ERROR, "getPreview");
                }
            }
        });

        if(idEpisode == null){
            idEpisode = "1";
        }
        else if(idEpisode.isEmpty()){
            idEpisode = "1";
        }
        URL = BASE_URL_SERVICES+"getEpisode?user="+userNunchee+"&episode="+idEpisode;
        //URL = TEST_URL+"getEpisode?user="+USER+"&episode="+idEpisode;
        Log.e(TAG+" getEpisodes",URL);;
        aq.ajax(URL,JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if(object != null){
                    try{
                        JSONObject  episode = object.getJSONObject("data").getJSONObject("episode");
                        p.EpisodeTitle = episode.getString("name");
                        p.Description = episode.getJSONObject("program").getString("description");
                        p.descriptionEpisode = episode.getString("description");
                        p.Title = episode.getJSONObject("program").getString("name");
                        p.Hashtags = episode.getString("hashtag")+";"+episode.getJSONObject("program").getString("hashtag");
                        String e = "Preview";
                        Log.e(e,p.Title);
                        //Log.e(e,p.PChannel.channelCallLetter);
                        Log.e(e,p.PChannel.channelImageURL);
                        Log.e(e,p.EndDate.toString());
                        Log.e(e,p.StartDate.toString());
                        Log.e(e,p.descriptionEpisode);
                        Log.e(e,p.EpisodeTitle );

                        handler.loaded(p);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e(ERROR, "Nos fuimos a la B ");
                    }
                }
                else{
                    Log.e(ERROR, "getEpisode");
                }
            }
        });
    }
    // TWETTS
    // Servicio numero 6
    public void getTweets(final ServiceManagerHandler<TweetSM> handler, String searchValues, String limit, boolean retweet){

        URL = BASE_URL_SERVICES+"/getTwitt?search_values="+searchValues+"&retweet=false&limit="+limit;
        //URL = "http://192.168.1.173:8000/api/1.0/guide/getTwitt?search_values=%23tvn&retweet=false";
        Log.e(TAG + " getTweets", URL);
        aq.ajax(URL, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if(object!= null){
                    try{
                        JSONArray tweets = object.getJSONObject("data").getJSONArray("tweets");
                        List<TweetSM> listTw = new ArrayList<TweetSM>();

                        for(int i = 0 ; i < tweets.length(); i++){
                            TweetSM t = parseJsonObject(tweets.getJSONObject(i),TweetSM.class);
                            listTw.add(t);
                        }

                        handler.loaded(listTw);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        handler.error(e.getMessage());
                        Log.e("Error tw", "-->" + e.getMessage());
                    }
                }
                else{
                    handler.error("");
                    Log.e(ERROR, "getTwwts");
                }

            }
        });
    }
    // TRIVIA

    public void getTrivia(final ServiceManagerHandler<Trivia> handler,final String idPrograma,final String userNunchee,final String level){

        /*userNunchee = "53bdadc825822c11f84fc067";
        level = "1";*/
        URL = "http://23.21.72.216:80/nunchee/api/1.0/guide/getQuestions?user=53bdadc825822c11f84fc067&program=1763&level="+nivelTrivia;

        if(nivelTrivia == 1){
            trivia = new Trivia();
            questions = new ArrayList<TriviaQuestion>();
            trivia.setPreguntas(questions);
        }

        Log.e(TAG+ " getTrivia", URL);
        aq.ajax(URL, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if (object != null && status.getCode() == 200) {
                    try {
                        JSONArray data = object.getJSONArray("data");
                        for(int i = 0; i <  data.length() ;i++){
                            TriviaQuestion question = new TriviaQuestion();
                            question.setLevel(nivelTrivia);
                            question.setId(data.getJSONObject(i).getInt("id"));
                            question.setType(data.getJSONObject(i).getInt("type"));
                            question.setText(data.getJSONObject(i).getString("text"));
                            question.setTextAlt(data.getJSONObject(i).getString("image"));

                            JSONArray answers = data.getJSONObject(i).getJSONArray("answers");
                            List<TriviaAnswers> listTriviaAnswers = new ArrayList<TriviaAnswers>();
                            for(int j = 0; j < answers.length();j++){

                                TriviaAnswers triviaAnswers = new TriviaAnswers();
                                triviaAnswers.setRespuesta(answers.getJSONObject(j).getString("text"));
                                triviaAnswers.setValor(answers.getJSONObject(j).getBoolean("real_option"));
                                listTriviaAnswers.add(triviaAnswers);
                            }
                            question.setRespuestas(listTriviaAnswers);
                            questions.add(question);
                        }
                        nivelTrivia++;
                        if(nivelTrivia < 4){
                            getTrivia(handler, idPrograma,userNunchee,level);
                        }
                        else{
                            nivelTrivia = 0;
                            handler.loaded(trivia);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG + " getTrivia ", e.getMessage());
                        handler.error(e.getMessage());
                    }
                } else {
                    Log.e(TAG + " getTrivia", ERROR);
                }
            }
        });
    }

    // POLLS

    // ACTION SOCIAL
    // Servicio numero
    public void addLike(final ServiceManagerHandler<String> handler,String userNunchee, String idChannel, String idProgram, String idDevice){

        userNunchee = "53bdadc825822c11f84fc067";   idChannel = "96";   idProgram = "1763"; idDevice = "123456789";
        Map<String, Object> map = new HashMap<String, Object>();

        URL = "http://23.21.72.216:80/nunchee/api/1.0/guide/addLike?user="+userNunchee+"&device="+idDevice+"&channel="+idChannel+"&program="+idProgram;
        aq.ajax(URL,map,String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                Log.e("status",status.getMessage());
                Log.e("object",object);
                if(object != null){
                    try{
                        handler.loaded(object);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e(TAG+"addLike",e.getMessage());
                        handler.error(e.getMessage());
                    }
                }
                else{
                    Log.e(TAG+" addLike",ERROR);
                }
            }
        } );
    }
    // Servicio numero
    public void removeLike(final ServiceManagerHandler<String> handler, String userNunchee, String idProgram){

        userNunchee = "53bdadc825822c11f84fc067";   idProgram = "1763";
        URL = "http://23.21.72.216:80/nunchee/api/1.0/guide/removeLike?user="+userNunchee+"&program="+idProgram;
        Map<String, Object> map = new HashMap<String, Object>();

        aq.ajax(URL, map, String.class , new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                Log.e("status",status.getMessage());
                Log.e("object",object);
                if(object != null){
                    try{
                        handler.loaded(object);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e(TAG+"removeLike",e.getMessage());
                        handler.error(e.getMessage());
                    }
                }
                else{
                    Log.e(TAG+" removeLike",ERROR);
                }
            }
        });
    }

    // Servicio numero
    public void addFavorite(final ServiceManagerHandler<String> handler,String userNunchee, String idChannel, String idProgram, String idDevice){

        userNunchee = "53bdadc825822c11f84fc067";   idChannel = "96";   idProgram = "1763"; idDevice = "123456789";
        Map<String, Object> map = new HashMap<String, Object>();

        URL = "http://23.21.72.216:80/nunchee/api/1.0/guide/addFavorite?user="+userNunchee+"&device="+idDevice+"&channel="+idChannel+"&program="+idProgram;
        aq.ajax(URL,map,String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                Log.e("status-",status.getMessage());
                Log.e("object-",object);
                if(object != null){
                    try{
                        handler.loaded(object);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e(TAG+"addFavorite",e.getMessage());
                        handler.error(e.getMessage());
                    }
                }
                else{
                    Log.e(TAG+" addFavorite",ERROR);
                }
            }
        } );
    }
    // Servicio numero
    public void removeFavorite(final ServiceManagerHandler<String> handler, String userNunchee, String idProgram){

        userNunchee = "53bdadc825822c11f84fc067";   idProgram = "1763";
        URL = "http://23.21.72.216:80/nunchee/api/1.0/guide/removeFavorite?user="+userNunchee+"&program="+idProgram;
        Map<String, Object> map = new HashMap<String, Object>();

        aq.ajax(URL, map, String.class , new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                Log.e("status--",status.getMessage());
                Log.e("object--",object);
                if(object != null){
                    try{
                        handler.loaded(object);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e(TAG+"removeFavorite",e.getMessage());
                        handler.error(e.getMessage());
                    }
                }
                else{
                    Log.e(TAG+" removeFavorite",ERROR);
                }
            }
        });
    }

    // Servicio numero
    public void addCheckIn(final ServiceManagerHandler<String> handler,String userNunchee, String idChannel, String idProgram, String idDevice){

        userNunchee = "53bdadc825822c11f84fc067";   idChannel = "96";   idProgram = "1763"; idDevice = "123456789";
        Map<String, Object> map = new HashMap<String, Object>();

        URL = "http://23.21.72.216:80/nunchee/api/1.0/guide/addCheckIn?user="+userNunchee+"&device="+idDevice+"&channel="+idChannel+"&program="+idProgram;
        //URL = "http://23.21.72.216:80/nunchee/api/1.0/guide/addCheckIn?user=53bdadc825822c11f84fc067&device=123456789&channel=96&program=1763";
        aq.ajax(URL,map,String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                Log.e("status check",status.getMessage());
                Log.e("object check",object);
                if(object != null){
                    try{
                        handler.loaded(object);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e(TAG+"addFavorite",e.getMessage());
                        handler.error(e.getMessage());
                    }
                }
                else{
                    Log.e(TAG+" addFavorite",ERROR);
                }
            }
        } );
    }
    // Servicio numero
    public void addShared(final ServiceManagerHandler<String> handler,String userNunchee, String idChannel, String idProgram, String idDevice){

        userNunchee = "53bdadc825822c11f84fc067";   idChannel = "96";   idProgram = "1763"; idDevice = "123456789";
        Map<String, Object> map = new HashMap<String, Object>();

        URL = "http://23.21.72.216:80/nunchee/api/1.0/guide/addFavorite?user="+userNunchee+"&device="+idDevice+"&channel="+idChannel+"&program="+idProgram;
        aq.ajax(URL,map,String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                Log.e("status shared",status.getMessage());
                Log.e("object shared",object);
                if(object != null){
                    try{
                        handler.loaded(object);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e(TAG+"addFavorite",e.getMessage());
                        handler.error(e.getMessage());
                    }
                }
                else{
                    Log.e(TAG+" addFavorite",ERROR);
                }
            }
        } );
    }
    public void addViewed(final ServiceManagerHandler<String> handler,String userNunchee, String idChannel, String idProgram, String idDevice){

        userNunchee = "53bdadc825822c11f84fc067";   idChannel = "96";   idProgram = "1763"; idDevice = "123456789";
        Map<String, Object> map = new HashMap<String, Object>();

        URL = "http://23.21.72.216:80/nunchee/api/1.0/guide/addViewed?user="+userNunchee+"&device="+idDevice+"&channel="+idChannel+"&program="+idProgram;
        aq.ajax(URL,map,String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                Log.e("status shared",status.getMessage());
                Log.e("object shared",object);
                if(object != null){
                    try{
                        handler.loaded(object);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Log.e(TAG+"addFavorite",e.getMessage());
                        handler.error(e.getMessage());
                    }
                }
                else{
                    Log.e(TAG+" addFavorite",ERROR);
                }
            }
        } );
    }

    // RECOMMENDATIONS
    public void getRecommendations(final ServiceManagerHandler<RecommendationSM> handler , String userNunchee, String idProgram, String deviceType, String date){
        userNunchee = "53bdadc825822c11f84fc067";
        idProgram = "1763";
        date = "12%3A00%3A00%2005-08-2014"; // 12:00:00 05-08-2014
        deviceType = "1";
        String imageType = "1";
        URL = BASE_URL_SERVICES_RECOMMENDATION+"/getRecommendation?user="+userNunchee+"&program="+idProgram+"&device_type="+deviceType+"&image_type="+imageType+"&date="+date;
        //URL = "http://23.21.72.216:80/nunchee/api/1.0/recommendation/getRecommendation?user=53bdadc825822c11f84fc067&program=1763&device_type=1&image_type=1&date=12%3A00%3A00%2005-08-2014";

        aq.ajax(URL, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if (object != null) {
                    try {
                        List<RecommendationSM> listRecommendation = new ArrayList<RecommendationSM>();
                        JSONArray recommendation = object.getJSONObject("data").getJSONArray("recommendation");

                        for (int i = 0; i < recommendation.length(); i++) {
                            RecommendationSM r = parseJsonObject(recommendation.getJSONObject(i), RecommendationSM.class);
                            //if (!recommendation.getJSONObject(i).isNull("episode")) {
                                EpisodeRecommendationSM e = parseJsonObject(recommendation.getJSONObject(i).getJSONObject("episode"), EpisodeRecommendationSM.class);
                                r.episode = e;
                            //}*/
                            listRecommendation.add(r);
                        }
                        handler.loaded(listRecommendation);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.error(e.getMessage());
                        Log.e(TAG + " getRecommendation", e.getMessage());
                    }
                } else {
                    Log.e(TAG + " getRecommendation", ERROR);
                }
            }
        });
    }

    public void getRecommendationRandom(final ServiceManagerHandler<Program> handler, String userNunchee, String deviceType){

        userNunchee = "53bdadc825822c11f84fc067";
        deviceType = "1";

        URL = BASE_URL_SERVICES_RECOMMENDATION+"/getRandom?user="+userNunchee+"&device_type="+deviceType+"&program_image_type="+PROGRAM_TYPE_POSTER+"&limit=24";

        aq.ajax(URL,JSONObject.class, new AjaxCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if(object != null){
                        List<Program> listProgram = new ArrayList<Program>();
                    try{
                        JSONArray recommendations = object.getJSONObject("data").getJSONArray("recommendation");

                        for(int i = 0 ; i < recommendations.length() ;i++){
                            Program program = new Program();
                            program.Title = recommendations.getJSONObject(i).getString("name");
                            program.IdProgram = recommendations.getJSONObject(i).getString("id");
                            program.listaImage = new ArrayList<Image>();
                            Image image = new Image();
                            image.setImagePath(recommendations.getJSONObject(i).getString("image"));
                            program.listaImage.add(image);
                            listProgram.add(program);
                        }

                        handler.loaded(listProgram);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        handler.error(e.getMessage());
                        Log.e(TAG+" getRecommendation",e.getMessage());
                    }
                }
                else{
                    Log.e(TAG + " getRecommendationRandom", ERROR);
                }
            }
        });
    }

    // SEARCH
    public void search(final ServiceManagerHandler<DataResultSM> handler, String userNunchee, String text){
        userNunchee = "53bdadc825822c11f84fc067";
        URL = BASE_URL_SERVICES+"/search?user="+userNunchee+"&country=CL&title="+text+"&language=0&limit=5&device_type=1&image_type=0";

        aq.ajax(URL,JSONObject.class,new AjaxCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                if(object != null){
                    try{
                        List<DataResultSM> listResult = new ArrayList<DataResultSM>();
                        JSONArray results = object.getJSONObject("data").getJSONArray("results");

                        for(int i = 0; i < results.length() ;i++){
                            DataResultSM dataResultSM = parseJsonObject(results.getJSONObject(i), DataResultSM.class);
                            listResult.add(dataResultSM);
                        }
                        handler.loaded(listResult);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        handler.error(e.getMessage());
                        Log.e("Error","--> "+e.getMessage());
                    }
                }
                else{
                    Log.e(ERROR,"search");
                }

            }
        });
    }

    // LIVE
    public void loadLiveStreamList(final ServiceManagerHandler<LiveStream> loadedHandler) {
        String url = String.format("%slive-stream?token=%s", BASE_URL_STREAM_MANAGER, API_TOKEN);
        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try{
                    if (!object.isNull("data") && object.getString("status").equals("OK")) {
                        JSONArray list = object.getJSONArray("data");
                        List<LiveStream> streams = new ArrayList<LiveStream>();
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject raw = list.getJSONObject(i);
                            streams.add(parseJsonObject(raw, LiveStream.class));
                        }

                        loadedHandler.loaded(streams);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    public void loadLiveStreamSchedule(final LiveStream stream, final ServiceManagerHandler<LiveStreamSchedule> loadedHandler) {

        String url = String.format("%slive-stream/%s/schedule?token=%s", BASE_URL_STREAM_MANAGER, stream.getLiveStreamId(), API_TOKEN);
        //Log.e("Url Stram Shedule",url);
        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try{
                    if (!object.isNull("data") && object.getString("status").equals("OK")) {
                        JSONArray list = object.getJSONArray("data");
                        List<LiveStreamSchedule> schedule = new ArrayList<LiveStreamSchedule>();
                        for (int i = 0; i < list.length(); ++i) {
                            JSONObject raw = list.getJSONObject(i);
                            LiveStreamSchedule item = parseJsonObject(raw, LiveStreamSchedule.class);
                            item.setStream(stream);
                            schedule.add(item);
                        }
                        //Log.e("Tamaño jsdbfs","--> "+schedule.size());
                        loadedHandler.loaded(schedule);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG+" loadLiveStreamShedule", e.getMessage());
                }
            }
        });
    }
    public void issueTokenForLive(String mediaId, final ServiceManagerHandler<TokenIssue> handler) {

        String url = String.format("%saccess/issue?type=live&token=%s&id=%s", BASE_URL_STREAM_MANAGER, API_TOKEN, mediaId);
        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                try {
                    TokenIssue tokenIssue = parseJsonObject(object, TokenIssue.class);
                    String u = String.format("%saccess/issue?type=live&token=%s&id=%s", BASE_URL_STREAM_MANAGER, API_TOKEN,tokenIssue.getAccessToken() );
                    handler.loaded(tokenIssue);
                }
                catch (Exception exception) {
                    handler.error(exception.getMessage());
                }
            }
        });
    }

    /*public Boolean checkToken(final int cont){
        try{
            SharedPreferences prefs = context.getSharedPreferences("co.winsportsonline.wso", Context.MODE_PRIVATE);
            String url = String.format("%s?grant_type=%s&refres_token=%s", URL_LOGIN, REFRESH_GRANT_TYPE, prefs.getString("refresh_token", null));
            final Boolean[] res = {false};
            aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>(){
                @Override
                public void callback(String url, JSONObject accessObj, AjaxStatus status) {
                    try{
                        saveAccessData(accessObj.getString("token_type"),
                                accessObj.getString("access_token"),
                                accessObj.getString("refresh_token"));
                        res[0] = true;
                    }catch(Exception e){
                        if(cont==1){
                            res[0] = false;
                        }else{
                            res[0] = checkToken(1);
                        }
                    }
                }
            }.header("X-Requested-With","XMLHttpRequest"));
            return res[0];
        }catch(Exception e){
            Log.e(TAG,"CheckToken: "+e.getMessage());
        }
        return false;
    }*/


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////O/////T/////R/////O/////S////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String formatDate ( Date date)  {

        SimpleDateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZZ" );

        return  ""+targetFormat.format(date);

    }

    private <T> JSONObject encodeJsonObject(T obj)
            throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, JSONException {

        JSONObject result = new JSONObject();

        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.getName().startsWith("get")) {
                String variableName = method.getAnnotation(DataMember.class)
                        .member();
                Object value = method.invoke(obj);

                result.put(variableName, value);
            }
        }

        return result;
    }

    public <T> T parseJsonObject(JSONObject jsonObj, Class<T> type)
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, JSONException {

        T result = type.newInstance();

        for (Method method : type.getMethods()) {
            if (method.getName().startsWith("set")) {
                DataMember dataMember = method.getAnnotation(DataMember.class);

                if (dataMember == null) {
                    continue;
                }

                String variableName = dataMember.member();
                Object value = jsonObj.isNull(variableName) ? null : jsonObj
                        .get(variableName);

                if (value != null) {
                    @SuppressWarnings("rawtypes")
                    Class[] params = method.getParameterTypes();
                    if (params[0] == String.class) {
                        method.invoke(result, value.toString());
                    }
                    else if(params[0]== Date.class){
                        method.invoke(result, cambiaFormatoFecha(value.toString()));
                    }
                    else {
                        method.invoke(result, value);
                    }
                }
            }
        }
        return result;
    }

    public Date cambiaFormatoFecha(String fecha)  {


        String format = "yyyy-MM-dd'T'HH:mm";
        DateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
        Date d = null;
        try {
            d = df.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(d == null){
            Log.e("Formato Date","Null");
            return null;
        }
        return d;
    }
    public static class ServiceManagerHandler<T> {

        public void loaded(T data) {

        }

        public void loaded(List<T> data) {

        }

        public void error(String error) {
        }
    }

    public String getTokenMovistar(){
        return UserPreferenceSM.getTokenMovistar(this.context);
    }

    public class AutoLogin extends AsyncTask<Void,Void,Void>{

        private long inicio;
        private long fin;
        private long delta;
        private final int MINUTO = 60000;
        private final int MINUTO_x15 = MINUTO * 15;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.e("PreExecute","AutoLogin");
            inicio = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            do{
                fin = System.currentTimeMillis();
                delta = fin - inicio;
            }while(delta <MINUTO_x15);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Log.e("POST Execute","AutoLogin");
            reLoginFacebook(tokenFacebook,tokenMovistar);
        }

    }

}
