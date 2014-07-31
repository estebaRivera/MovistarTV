package com.smartboxtv.movistartv.services;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.auth.FacebookHandle;
import com.androidquery.auth.TwitterHandle;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.smartboxtv.movistartv.data.models.CategorieChannel;
import com.smartboxtv.movistartv.data.models.Channel;
import com.smartboxtv.movistartv.data.models.FeedAction;
import com.smartboxtv.movistartv.data.models.FeedChannel;
import com.smartboxtv.movistartv.data.models.FeedFriends;
import com.smartboxtv.movistartv.data.models.FeedJSON;
import com.smartboxtv.movistartv.data.models.Image;
import com.smartboxtv.movistartv.data.models.Polls;
import com.smartboxtv.movistartv.data.models.PollsAnswers;
import com.smartboxtv.movistartv.data.models.PollsQuestions;
import com.smartboxtv.movistartv.data.models.Program;
import com.smartboxtv.movistartv.data.models.Recommendations;
import com.smartboxtv.movistartv.data.models.TrendingChannel;
import com.smartboxtv.movistartv.data.models.Trivia;
import com.smartboxtv.movistartv.data.models.TriviaAnswers;
import com.smartboxtv.movistartv.data.models.TriviaQuestion;
import com.smartboxtv.movistartv.data.models.Tweets;
import com.smartboxtv.movistartv.data.models.UserFacebook;
import com.smartboxtv.movistartv.data.models.UserTwitter;
import com.smartboxtv.movistartv.data.models.UserTwitterJSON;
import com.smartboxtv.movistartv.data.modelssm.LiveStream;
import com.smartboxtv.movistartv.data.modelssm.LiveStreamSchedule;
import com.smartboxtv.movistartv.data.preference.UserPreference;
import com.smartboxtv.movistartv.fragments.NUNCHEE;
import com.smartboxtv.movistartv.social.Facebook;
import com.smartboxtv.movistartv.social.Twitter;

public class DataLoader {

    private static final String TAG = "DataLoader";

    private final long EXPIRE = 10000;

    public static final String BASE_URL = "https://api.streammanager.co/api/";
    public static final String API_TOKEN = "8fc221e56408966fe7999c7c1edff220";

    private static final String SERVICES_URL_TRIVIA = "http://190.215.44.18/wcfTrivia/TriviaService.svc/";
    private static final String SERVICES_URL_TRIVIA_AMAZON = "http://wcftrivia.sbtvapps.com/TriviaService.svc/";

    private static final String SERVICES_URL_POLLS = "http://190.215.44.18/wcfPolls/TriviaService.svc/";
    private static final String SERVICES_URL_POLLS_AMAZON = "http://wcfpolls.sbtvapps.com/TriviaService.svc/";//"http://190.215.44.18/wcfPolls/TriviaService.svc/";

    private static final String SERVICES_URL =  "http://190.215.44.18/wcfNunchee2/GLFService.svc/";
    private static final String SERVICES_URL_AMAZON =   "http://nunchee.sbtvapps.com/GLFService.svc/"; //"http://190.215.44.18/wcfNunchee2/GLFService.svc/";

    private static final String SERVICES_URL_TRENDING = "http://190.215.44.18/wcfNunchee2/GLFService.svc/Trending";
    private static final String SERVICES_URL_TRENDING_AMAZON =  "http://nunchee.sbtvapps.com/GLFService.svc/Trending"; //"http://190.215.44.18/wcfNunchee2/GLFService.svc/Trending";

    private static final String URL_TWITTER = "https://api.twitter.com/1.1/statuses/";

    private static String URL_FEED, URL_FEED_PARAMETROS;

    private StringBuilder URL_FINAL;

    private final Context actividad;

    private final AQuery aq;

    public boolean CONNECT_AWS = false;

    private List<FeedJSON> listaHistorial = new ArrayList<FeedJSON>();

    public DataLoader(Context actividad) {
        this.actividad = actividad;
        this.aq = new AQuery(actividad);
    }
    public DataLoader(Activity actividad) {
        this.actividad = actividad;
        this.aq = new AQuery(actividad);
    }

    //1
    public void loginService(String parametro, final DataLoadedHandler<String> handler) {

        Map<String, Object> map = new HashMap<String, Object>();

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }
        URL_FINAL.append("login/");
        URL_FINAL.append(parametro);

        Log.e("URL", URL_FINAL.toString());

        aq.ajax(URL_FINAL.toString(), map , String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                try {

                    String auxiliar = object.replace("\"","");
                    UserPreference.setIdNunchee(auxiliar, actividad);
                    Log.e("idNunchee", auxiliar);

                    handler.loaded(auxiliar);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Login Nunchee",e.toString());
                    handler.error(e.getMessage());
                }
            }
        });
    }

    //2
    public void programRandom(final DataLoadedHandler<Program> handler) {

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }
        URL_FINAL.append("ProgramRandom");

        aq.ajax(URL_FINAL.toString(), JSONArray.class, new AjaxCallback<JSONArray>() {

            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                try {
                    List<Program> list = new ArrayList<Program>();
                    for (int i = 0; i < object.length(); i++) {

                        Program p = parseJsonObject(object.getJSONObject(i), Program.class);
                        p.setListaImage(new ArrayList<Image>());

                        JSONArray jImagen = object.getJSONObject(i).getJSONArray("MainImages");
                        for (int j = 0; j < jImagen.length(); j++) {
                            p.getListaImage().add(parseJsonObject(jImagen.getJSONObject(j), Image.class));
                        }
                        list.add(p);
                    }
                    handler.loaded(list);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.error(e.getMessage());
                }
            }
        });
    }

    //3
    public void getCategories(final DataLoadedHandler<CategorieChannel> handler) {

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("Categories");

        aq.ajax(URL_FINAL.toString(), JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                try {

                    List<CategorieChannel> list = new ArrayList<CategorieChannel>();
                    for (int i = 0; i < object.length(); i++) {

                        CategorieChannel cc = parseJsonObject(object.getJSONObject(i), CategorieChannel.class);
                        list.add(cc);
                    }
                    handler.loaded(list);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.error(e.getMessage());
                }
            }
        });
    }

    //4
    public void getProgramByCategories(final DataLoadedHandler<Program> handler, String idNunchee, Date date, int idCategoria) {

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("ProgramByChannelCategory/");

        String parametro =formatDate(date)+";"+idCategoria+";"+idNunchee ;
        String parametroBase64 = Base64.encodeToString(parametro.getBytes(), Base64.NO_WRAP);

        Log.e("url Categories","-> "+parametro);

        URL_FINAL.append(parametroBase64);
        Log.e("url Categories","-> "+URL_FINAL.toString());
        
        aq.ajax(URL_FINAL.toString(), JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {

                try {
                    List<Program> listaPrograma = new ArrayList<Program>();

                    for (int i = 0; i < object.length(); i++) {

                        Program p = parseJsonObject(object.getJSONObject(i), Program.class);
                        p.setListaImage(new ArrayList<Image>());


                        JSONArray jImagen = object.getJSONObject(i).getJSONArray("MainImages");
                        for (int j = 0; j < jImagen.length(); j++) {
                            p.getListaImage().add(parseJsonObject(jImagen.getJSONObject(j), Image.class));
                        }
                        JSONArray jCanal = object.getJSONObject(i).getJSONArray("PChannel");
                        p.setPChannel(parseJsonObject(jCanal.getJSONObject(0), Channel.class));

                        if(jImagen.length()>1){
                            listaPrograma.add(p);
                        }

                    }
                    handler.loaded(listaPrograma);

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.error(e.getMessage());
                }
            }
        });
    }

    //5     // ACCIONES SOCIALES
    public void actionLike( String idUser, String idAction, String idProgram, String idChannel){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("action/");
        URL_FINAL.append(idUser);    URL_FINAL.append(",");  URL_FINAL.append(idAction); URL_FINAL.append(",");
        URL_FINAL.append(idProgram);   URL_FINAL.append(",");  URL_FINAL.append(idChannel);  URL_FINAL.append(",");
        URL_FINAL.append("0");

        aq.ajax(URL_FINAL.toString(), String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.e("LIKE FACEBOOK", object);
            }
        });
    }

    //6
    public void actionShare( String idUser, String idAction, String idProgram, String idChannel){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("action/");
        URL_FINAL.append(idUser);    URL_FINAL.append(",");  URL_FINAL.append(idAction); URL_FINAL.append(",");
        URL_FINAL.append(idProgram);   URL_FINAL.append(",");  URL_FINAL.append(idChannel);
        URL_FINAL.append(",");  URL_FINAL.append("0");

        aq.ajax(URL_FINAL.toString(), String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.e("SHARE", object);
            }
        });

    }

    //7
    public void actionFavorite( String idUser, String idAction, String idProgram, String idChannel){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("action/");
        URL_FINAL.append(idUser);URL_FINAL.append(",");  URL_FINAL.append(idAction);
        URL_FINAL.append(",");  URL_FINAL.append(idProgram);   URL_FINAL.append(",");
        URL_FINAL.append(idChannel);  URL_FINAL.append(",");  URL_FINAL.append("0");

        aq.ajax(URL_FINAL.toString(), String.class, new AjaxCallback<String>() {

        });
    }

    //8
    public void actionCheckIn( String idUser, String idAction, String idProgram, String idChannel){

        URL_FINAL = new StringBuilder();
        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("action/");
        URL_FINAL.append(idUser);URL_FINAL.append(",");  URL_FINAL.append(idAction);
        URL_FINAL.append(",");  URL_FINAL.append(idProgram);   URL_FINAL.append(",");
        URL_FINAL.append(idChannel);  URL_FINAL.append(",");  URL_FINAL.append("0");

        aq.ajax(URL_FINAL.toString(), String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.e("Check In", object);
            }
        });
    }

    //9
    public void actionPreview( String idUser, String idAction, String idProgram, String idChannel){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("action/");
        URL_FINAL.append(idUser);    URL_FINAL.append(",");  URL_FINAL.append(idAction);
        URL_FINAL.append(",");  URL_FINAL.append(idProgram);   URL_FINAL.append(",");  URL_FINAL.append(idChannel);
        URL_FINAL.append(",");  URL_FINAL.append("0");

        aq.ajax(URL_FINAL.toString(), String.class, new AjaxCallback<String>() {

        });
    }

    //10
    public void getFavoritesPrograms(final DataLoadedHandler<Program> handler, String idNunchee, String fecha){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("FavoriteGuide/");
        URL_FINAL.append(idNunchee);        URL_FINAL.append(",");  URL_FINAL.append(fecha);

        Log.e("URL Favoritos", URL_FINAL.toString());

        aq.ajax(URL_FINAL.toString(), JSONArray.class, EXPIRE, new AjaxCallback<JSONArray>() {

            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                try {
                    List<Program> favorites = new ArrayList<Program>();

                    for (int i = 0; i < object.length(); i++) {

                        for (int k = 0; k < object.getJSONObject(i).getJSONArray("Favorites").length(); k++) {

                            Program p = parseJsonObject(object.getJSONObject(i).getJSONArray("Favorites")
                                    .getJSONObject(k), Program.class);

                            p.setListaImage(new ArrayList<Image>());

                            JSONArray jImagen = object.getJSONObject(i).getJSONArray("Favorites")
                                    .getJSONObject(k).getJSONArray("MainImages");

                            for (int j = 0; j < jImagen.length(); j++) {
                                p.getListaImage().add(parseJsonObject(jImagen.getJSONObject(j), Image.class));
                            }

                            JSONArray jCanal = object.getJSONObject(i).getJSONArray("Favorites")
                                    .getJSONObject(k).getJSONArray("PChannel");

                            p.setPChannel(parseJsonObject(jCanal.getJSONObject(0), Channel.class));
                            favorites.add(p);
                        }
                    }
                    handler.loaded(favorites);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.error(e.getMessage());
                    Log.e("Error carga favoritos", "error -> " + e.getMessage());
                }
            }
        });
    }

    //11
    public void getProgramation(final DataLoadedHandler<Channel> handler,String fecha, String idNunchee, String horas){

        URL_FINAL = new StringBuilder();    URL_FINAL.append(SERVICES_URL.trim());  URL_FINAL.append("Programs/");

        String parametros = fecha+";"+idNunchee+";"+horas+"";
        String parametros64 = Base64.encodeToString(parametros.getBytes(), Base64.NO_WRAP);

        URL_FINAL.append(parametros64);
        aq.ajax(URL_FINAL.toString(), JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {

                    List<Channel> guia = new ArrayList<Channel>();
                    //JSONArray jsonArray = object.getJSONArray("DoWorkResult");

                    /*for(int i = 0;i<jsonArray.length();i++){
                        Channel c = parseJsonObject(jsonArray.getJSONObject(i),Channel.class);

                        c.setChannelPrograms(new ArrayList<Program>());
                        JSONArray jProgramas = jsonArray.getJSONObject(i).getJSONArray("Programs");

                        for(int j = 0 ;j<jProgramas.length();j++){
                            c.getChannelPrograms().add(parseJsonObject(jProgramas.getJSONObject(j), Program.class));
                        }
                        guia.add(c);*/
                    //}
                    handler.loaded(guia);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.error(e.getMessage());
                }
            }
        });
    }

    //12
    public void getPreview(final DataLoadedHandler<Program> handler, String idPrograma, String idCanal, String idNunchee, String fechaInicio, String fechaFin) {

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }
        URL_FINAL.append("ProgramDataById/");
        URL_FINAL.append(idPrograma);   URL_FINAL.append(",");  URL_FINAL.append(idCanal);  URL_FINAL.append(",");
        URL_FINAL.append(idNunchee);    URL_FINAL.append(",");  URL_FINAL.append(fechaInicio);  URL_FINAL.append(",");  URL_FINAL.append(fechaFin);

        aq.ajax(URL_FINAL.toString(), JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {

                    Program p = parseJsonObject(object, Program.class);
                    p.setListaImage(new ArrayList<Image>());

                    if (!object.isNull("MainImages")) {
                        JSONArray jImage = object.getJSONArray("MainImages");

                        for (int j = 0; j < jImage.length(); j++) {
                            p.getListaImage().add(parseJsonObject(jImage.getJSONObject(j), Image.class));
                        }

                        JSONArray jsonArray = object.getJSONArray("PChannel");
                        if (jsonArray != null && jsonArray.length() > 0) {

                            Channel c = parseJsonObject(jsonArray.getJSONObject(0), Channel.class);
                            p.setPChannel(c);
                        }
                    }
                    if (!object.isNull("Tweets")) {

                        JSONArray jTw = object.getJSONArray("Tweets");
                        p.setTweets(new ArrayList<Tweets>());

                        for (int j = 0; j < jTw.length(); j++) {
                            p.getTweets().add(parseJsonObject(jTw.getJSONObject(j), Tweets.class));
                        }
                    } else {
                        p.setTweets(null);
                    }
                    handler.loaded(p);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.error(e.getMessage());
                    Log.e("ERROR", e.getMessage());
                }
            }
        });
    }

    //13
    public void getTrivia(final DataLoadedHandler<Trivia> handler, String programa){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL_TRIVIA);
        }
        else{
            URL_FINAL.append(SERVICES_URL_TRIVIA_AMAZON);
        }

        URL_FINAL.append("Game/");  URL_FINAL.append(programa);

        Log.e("URL Trivia", URL_FINAL.toString());
        aq.ajax(URL_FINAL.toString(), JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {

                    Trivia t = parseJsonObject(object, Trivia.class);
                    t.setPreguntas(new ArrayList<TriviaQuestion>());

                    JSONArray preguntas = object.getJSONArray("Question");
                    for (int i = 0; i < preguntas.length(); i++) {

                        TriviaQuestion q = parseJsonObject(preguntas.getJSONObject(i), TriviaQuestion.class);
                        q.setRespuestas(new ArrayList<TriviaAnswers>());

                        JSONArray respuestas = preguntas.getJSONObject(i).getJSONArray("Answers");
                        for (int j = 0; j < respuestas.length(); j++) {

                            TriviaAnswers a = parseJsonObject(preguntas.getJSONObject(i).getJSONArray("Answers")
                                    .getJSONObject(j), TriviaAnswers.class);
                            q.getRespuestas().add(a);
                        }
                        t.getPreguntas().add(q);
                    }
                    handler.loaded(t);
                } catch (Exception e) {
                    handler.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    //14
    public void getScoreTrivia( final DataLoadedHandler<String> dataLoadedHandler,String idNunchee){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL_TRIVIA);
        }
        else{
            URL_FINAL.append(SERVICES_URL_TRIVIA_AMAZON);
        }

        URL_FINAL.append("Score/"); URL_FINAL.append(idNunchee);

        aq.ajax(URL_FINAL.toString(), String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String object, AjaxStatus status) {
                try {
                    dataLoadedHandler.loaded(object.replace('"', '\0'));
                } catch (Exception e) {
                    e.printStackTrace();
                    dataLoadedHandler.error(" ");
                }
            }
        });
    }

    //15
    public void getPolls(final DataLoadedHandler<Polls> handler, String programa){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL_POLLS);
        }
        else{
            URL_FINAL.append(SERVICES_URL_POLLS_AMAZON);
        }

        URL_FINAL.append("Game/");  URL_FINAL.append(programa);

        Log.e("URL POLLS",URL_FINAL.toString());
        aq.ajax(URL_FINAL.toString(), JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {

                    Polls p = parseJsonObject(object, Polls.class);
                    p.setPreguntas(new ArrayList<PollsQuestions>());

                    JSONArray preguntas = object.getJSONArray("Question");
                    for (int i = 0; i < preguntas.length(); i++) {

                        PollsQuestions q = parseJsonObject(preguntas.getJSONObject(i), PollsQuestions.class);
                        q.setRespuestas(new ArrayList<PollsAnswers>());

                        JSONArray respuestas = preguntas.getJSONObject(i).getJSONArray("Answers");

                        for (int j = 0; j < respuestas.length(); j++) {

                            PollsAnswers a = parseJsonObject(preguntas.getJSONObject(i).getJSONArray("Answers")
                                    .getJSONObject(j), PollsAnswers.class);
                            q.getRespuestas().add(a);
                        }

                        p.getPreguntas().add(q);
                    }
                    handler.loaded(p);
                } catch (Exception e) {
                    handler.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    //16
    public void getRecomendaciones(final DataLoadedHandler<Recommendations> handler, String idPrograma ,String idCanal, String idNunchee){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("Recomendations/"); URL_FINAL.append(idPrograma);
        URL_FINAL.append(","); URL_FINAL.append(idCanal); URL_FINAL.append(",");URL_FINAL.append(idNunchee);

        Log.e("URL RECOMENDACIONES", URL_FINAL.toString());
        aq.ajax(URL_FINAL.toString(), JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {

                    Recommendations r = parseJsonObject(object, Recommendations.class);
                    r.setSameCategoria(new ArrayList<Program>());

                    if (!object.isNull("Recommendations")) {
                        if (!object.getJSONObject("Recommendations").isNull("SameCategory")) {

                            JSONArray programas = object.getJSONObject("Recommendations").getJSONArray("SameCategory");

                            for (int i = 0; i < programas.length(); i++) {
                                if (!programas.isNull(i)) {

                                    JSONObject o = (programas.getJSONObject(i));
                                    Program p = parseJsonObject(o, Program.class);

                                    p.setListaImage(new ArrayList<Image>());
                                    JSONArray images = programas.getJSONObject(i).getJSONArray("MainImages");

                                    for (int j = 0; j < images.length(); j++) {
                                        p.getListaImage().add(parseJsonObject(images.getJSONObject(j), Image.class));
                                    }
                                    r.getSameCategoria().add(p);
                                }
                            }
                        }
                        Log.e("Recomendaciones Data Loader", r.getSameCategoria().toString());
                        handler.loaded(r);
                    } else {
                        //r = null;
                        handler.loaded((Recommendations) null);
                    }
                } catch (Exception e) {
                    handler.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    //17
    public void getTrendingChannel(final DataLoadedHandler<TrendingChannel> handler){

        String url;
        if((CONNECT_AWS == false)){
            url = SERVICES_URL_TRENDING;
        }
        else{
            url = SERVICES_URL_TRENDING_AMAZON;
        }
        Log.e("URL Trendding",url);
        aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {

            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                try {
                    List<TrendingChannel> list = new ArrayList<TrendingChannel>();
                    for (int i = 0; i < object.length(); i++) {

                        TrendingChannel trendingChannel = parseJsonObject(object.getJSONObject(i), TrendingChannel.class);
                        list.add(trendingChannel);
                    }
                    handler.loaded(list);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.error(e.getMessage());

                }
            }
        });

    }

    //18
    public void getTimeLine(final DataLoadedHandler<UserTwitterJSON> dataLoadedHandle){

        URL_FINAL = new StringBuilder();
        URL_FINAL.append(URL_TWITTER);
        URL_FINAL.append("home_timeline.json"); //URL_FINAL.append("/count=50");

        TwitterHandle twitterHandle = new TwitterHandle((Activity) actividad, Twitter.API_KEY,Twitter.API_SECRET);
        aq.auth(twitterHandle).ajax(URL_FINAL.toString(), JSONArray.class, new AjaxCallback<JSONArray>() {

            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                try {
                    List<UserTwitterJSON> listTws = new ArrayList<UserTwitterJSON>();
                    for (int i = 0; i < object.length(); i++) {

                        UserTwitterJSON tw = parseJsonObject(object.getJSONObject(i), UserTwitterJSON.class);
                        UserTwitter u = parseJsonObject(object.getJSONObject(i).getJSONObject("user"), UserTwitter.class);

                        tw.setUsuario(u);
                        listTws.add(tw);
                    }

                    //Log.e("JSONObject",object.toString());
                    dataLoadedHandle.loaded(listTws);
                } catch (Exception e) {
                    Log.e("Error GetTimeLIne", "quizas sea null" + e.getMessage());
                    dataLoadedHandle.error(e.getMessage());
                }
            }
        });
    }

    //19    // UPDATE STATUS TWITTER // POST
    public void updateStatusTw(final DataLoadedHandler<String> dataLoadedHandle,String tw){

        URL_FINAL = new StringBuilder();    URL_FINAL.append(URL_TWITTER); URL_FINAL.append("update.json");

        Map<String, String> map = new HashMap<String, String>();
        map.put("status", tw);

        TwitterHandle twitterHandle = new TwitterHandle((Activity) actividad, Twitter.API_KEY,Twitter.API_SECRET);
        aq.auth(twitterHandle).ajax(URL_FINAL.toString(), map, String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String object, AjaxStatus status) {
                try {
                    dataLoadedHandle.loaded("OK");
                } catch (Exception e) {
                    dataLoadedHandle.error("Ups!");
                    //Log.e("Update estado Tw",e.getMessage());
                }
            }
        });
    }

    public void friendsFacebookNunchee (final DataLoadedHandler<UserFacebook> dataLoadedHandler){

        URL_FINAL = new StringBuilder();
        URL_FINAL.append("https://graph.facebook.com/me/friends?fields=installed,username,picture,name");

        FacebookHandle facebookHandle = new FacebookHandle((Activity) actividad, Facebook.APP_ID, Facebook.PERMISSIONS);
        aq.auth(facebookHandle).ajax(URL_FINAL.toString(), JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                try {
                    List<UserFacebook> listUser = new ArrayList<UserFacebook>();
                    JSONArray jsonArray = object.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        if (!jsonArray.getJSONObject(i).isNull("installed")) {
                            UserFacebook user = parseJsonObject(object.getJSONArray("data")
                                    .getJSONObject(i), UserFacebook.class);

                            Log.e("Friends", user.getNombre());
                            listUser.add(user);
                        }
                    }
                    dataLoadedHandler.loaded(listUser);

                } catch (Exception e) {
                    Log.e("Friends", "catch " + e.getMessage());
                    dataLoadedHandler.error(e.getMessage());
                }
            }
        });
    }

    public void auth(){
        URL_FINAL = new StringBuilder();    URL_FINAL.append(URL_TWITTER); URL_FINAL.append("authenticate");
    }

    //21
    public void votoEncuesta(String idRespuesta){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL_POLLS);
        }
        else{
            URL_FINAL.append(SERVICES_URL_POLLS_AMAZON);
        }
        URL_FINAL.append("/Voto/"); URL_FINAL.append(idRespuesta);

        aq.ajax(URL_FINAL.toString(), String.class, new AjaxCallback<String>() {
        });

    }

    //22
    public void getFavoriteTodos (final DataLoadedHandler<Program> dataLoadedHandler, String idNunchee){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("FavoriteGuideTodos/"); URL_FINAL.append(idNunchee);

        aq.ajax(URL_FINAL.toString(), JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                try {

                    List<Program> lista = new ArrayList<Program>();
                    for (int i = 0; i < object.length(); i++) {

                        JSONArray favorito = object.getJSONObject(i).getJSONArray("Favorites");

                        for (int j = 0; j < favorito.length(); j++) {

                            Program p = parseJsonObject(favorito.getJSONObject(j), Program.class);
                            p.setListaImage(new ArrayList<Image>());

                            Channel c = parseJsonObject(favorito.getJSONObject(j)
                                    .getJSONArray("PChannel").getJSONObject(0), Channel.class);

                            p.setPChannel(c);
                            JSONArray imagenes = favorito.getJSONObject(j).getJSONArray("MainImages");

                            for (int k = 0; k < imagenes.length(); k++) {
                                p.getListaImage().add(parseJsonObject(imagenes.getJSONObject(k), Image.class));
                            }
                            lista.add(p);
                        }
                    }
                    dataLoadedHandler.loaded(lista);
                } catch (Exception e) {
                    Log.e("Favoritos ", "Todos " + e.getMessage());
                    e.printStackTrace();
                    dataLoadedHandler.error(e.getMessage());
                }
            }
        });

    }

    //23
    public  void deleteFavorite(String idNunchee, String time){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("favoriteUpdateOff/"); URL_FINAL.append(idNunchee);
        URL_FINAL.append(",");  URL_FINAL.append("4");  URL_FINAL.append(",");
        URL_FINAL.append(time);

        aq.ajax(URL_FINAL.toString(), String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
            }
        });

    }

    //24
    public void updateScoreTrvia(String score,String level, String idNunchee, String nameProgram){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL_TRIVIA);
        }else{
            URL_FINAL.append(SERVICES_URL_TRIVIA_AMAZON);
        }

        URL_FINAL.append("Trivia/");    URL_FINAL.append(score);  URL_FINAL.append(",");
        URL_FINAL.append(level);    URL_FINAL.append(",");  URL_FINAL.append(idNunchee);
        URL_FINAL.append(",");  URL_FINAL.append(nameProgram);

        aq.ajax(URL_FINAL.toString(), String.class, new AjaxCallback<String>() {

            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                //Log.e("puntaje trivia actualizado",":O "+object);
            }
        });
    }

    //25
    public void getTweets(DataLoadedHandler<Tweets> dataLoadedHandler,String idProgram, String idChannel){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("tweets"); URL_FINAL.append(idProgram); URL_FINAL.append(",");
        URL_FINAL.append(idChannel);
        aq.ajax(URL_FINAL.toString(), JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                super.callback(url, object, status);


            }
        });

    }

    //26
    public void search(final DataLoadedHandler<Program> dataLoadedHandler, String algo){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == true)){
            URL_FINAL.append(SERVICES_URL);
        }else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("Search/"); URL_FINAL.append(algo);
        Log.e("URL", URL_FINAL.toString());
        aq.ajax(URL_FINAL.toString(), JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {

                    List<Program> list = new ArrayList<Program>();
                    JSONArray jsonArray = object.getJSONArray("PData");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        Program p = parseJsonObject(jsonArray.getJSONObject(i), Program.class);
                        Log.e("Programa", p.getTitle());
                        Channel c = parseJsonObject(jsonArray.getJSONObject(i).getJSONArray("PChannel").getJSONObject(0), Channel.class);
                        p.setPChannel(c);
                        JSONArray imagenes = jsonArray.getJSONObject(i).getJSONArray("MainImages");

                        ArrayList<Image> imagesList = new ArrayList<Image>();

                        for (int k = 0; k < imagenes.length(); k++) {
                            imagesList.add(parseJsonObject(imagenes.getJSONObject(k), Image.class));
                        }
                        p.listaImage = imagesList;
                        list.add(p);
                    }
                    dataLoadedHandler.loaded(list);

                } catch (Exception e) {

                    e.printStackTrace();
                    Log.e("Buscar", "df" + e.getMessage());
                    dataLoadedHandler.error(e.getMessage());
                }
            }
        });
    }

    //27
    public void feed(final DataLoadedHandler<FeedJSON> dataLoadedHandler, List<String> ids){

        URL_FINAL = new StringBuilder();

        if((CONNECT_AWS == false)){
            URL_FINAL.append(SERVICES_URL);
        }
        else{
            URL_FINAL.append(SERVICES_URL_AMAZON);
        }

        URL_FINAL.append("Feeds");

        String parametros,parametros64;
        parametros = UserPreference.getIdNunchee(actividad)+";";

        for(int i = 0;i < ids.size();i++){

            if(i< ids.size()-1)
                parametros = parametros +ids.get(i)+",";
            else
                parametros = parametros +ids.get(i);
        }

        parametros64 = "N";
        parametros64 = parametros64+Base64.encodeToString(parametros.getBytes(),Base64.NO_WRAP);

        URL_FEED = URL_FINAL.toString();
        URL_FINAL.append(parametros64);
        URL_FEED_PARAMETROS = parametros64;

        TareaAsync task = new TareaAsync();
        try{
            task.execute(dataLoadedHandler,dataLoadedHandler);
        }
        catch(Exception e){
            dataLoadedHandler.error(e.getMessage());
        }
    }
    public void cancel(){
        aq.ajaxCancel();
    }
    public void loadLiveStreamList(final DataLoadedHandler<LiveStream> loadedHandler) {
        String url = String.format("%slive-stream?token=%s", BASE_URL, API_TOKEN);
        Log.e("Url",url);
        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try{
                    if (!object.isNull("data") && object.getString("status").equals("OK")) {
                        JSONArray list = object.getJSONArray("data");
                        List<LiveStream> streams = new ArrayList<LiveStream>();
                        for (int i = 0; i < list.length(); ++i) {
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

    public void loadLiveStreamSchedule(final LiveStream stream, final DataLoadedHandler<LiveStreamSchedule> loadedHandler) {

        String url = String.format("%slive-stream/%s/schedule?token=%s", BASE_URL, stream.getLiveStreamId(), API_TOKEN);
        Log.e("Url",url);
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
                        loadedHandler.loaded(schedule);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    public String formatDate ( Date date)  {

        SimpleDateFormat targetFormat;
        if((CONNECT_AWS == false)){
            targetFormat= new SimpleDateFormat("dd-MM-yyyy HH:mm:ssZZZ" );
        }
        else{
            targetFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        }

        return  ""+targetFormat.format(date);

    }

    private boolean hasCache(String fileName) {

        return new File(actividad.getCacheDir(), fileName).exists();

    }

    private void saveCacheData(String fileName, JSONArray data)
            throws IOException {
        File cacheFile = new File(actividad.getCacheDir(), fileName);

        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
        }

        FileOutputStream fileStream = new FileOutputStream(cacheFile, false);

        try {
            fileStream.write(data.toString().getBytes());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        } finally {
            fileStream.close();
        }

    }

    private JSONArray readCacheData(String fileName) {
        JSONArray data = null;

        File cacheFile = new File(actividad.getCacheDir(), fileName);

        if (cacheFile.exists()) {
            FileInputStream fileStream = null;
            Scanner scanner = null;
            try {
                fileStream = new FileInputStream(cacheFile);
                scanner = new Scanner(fileStream);

                StringBuilder builder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    builder.append(scanner.nextLine());
                }

                data = new JSONArray(builder.toString());

            } catch (FileNotFoundException e) {
                Log.d(TAG, e.toString());
            } catch (JSONException e) {
                Log.d(TAG, e.toString());
            } finally {
                assert scanner != null;
                scanner.close();
            }
        }

        return data;
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

    public Date cambiaFormatoFecha(String fecha){
        Date d;
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        if(fecha == null){
            return null;
        }
        char separadorZonaHoraria = fecha.charAt(fecha.length()-7);
        if(separadorZonaHoraria == '+' ||separadorZonaHoraria == '-'){
            d = new Date(Long.parseLong(fecha.substring(6, fecha.length() - 7)));
            return d;
        }
        return null;
    }



    public static class DataLoadedHandler<T> {

        public void loaded(T data) {

        }

        public void loaded(List<T> data) {

        }

        public void error(String error) {

        }
    }

    public static class Result {

        private String result;

        @DataMember(member = "result")
        public String getResult() {
            return result;
        }

        @DataMember(member = "result")
        public void setResult(String result) {
            this.result = result;
        }
    }

    public class TareaAsync extends AsyncTask<DataLoadedHandler<FeedJSON>,Void,DataLoadedHandler<FeedJSON>> {

        @Override
        protected DataLoadedHandler<FeedJSON> doInBackground(DataLoadedHandler<FeedJSON>... data) {

            URL url;

            JSONArray jsonArray ;

            HttpURLConnection connection = null;

            List<FeedJSON> listaFeedJSON = new ArrayList<FeedJSON>();

            try {
                //Create connection
                url = new URL(URL_FEED);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/json");

                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(URL_FEED_PARAMETROS.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");

                connection.setUseCaches (false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream ());
                wr.writeBytes (URL_FEED_PARAMETROS);
                wr.flush ();
                wr.close ();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();

                jsonArray = new JSONArray(response.toString());

                for(int i = 0; i < jsonArray.length() && i <30 ;i++){

                    FeedJSON feedJSON;

                    feedJSON = parseJsonObject(jsonArray.getJSONObject(i),FeedJSON.class);

                    FeedAction feedAction = parseJsonObject(jsonArray.getJSONObject(i).getJSONObject("Action"), FeedAction.class);

                    feedJSON.setAction(feedAction);

                    for(int j = 0 ; j < jsonArray.getJSONObject(i).getJSONArray("Friends").length() ; j++){

                        FeedFriends feedFriends = new FeedFriends();

                        feedFriends.setIdUserFacebook( jsonArray.getJSONObject(i).getJSONArray("Friends").getString(j));

                        feedJSON.setFriends(feedFriends);

                    }

                    FeedChannel feedChannel = parseJsonObject(jsonArray.getJSONObject(i).getJSONObject("Channel"),FeedChannel.class);

                    if(!jsonArray.getJSONObject(i).isNull("MainImages")){

                        List<Image> listaImagen = new ArrayList<Image>();

                        for(int j = 0; j < jsonArray.getJSONObject(i).getJSONArray("MainImages").length(); j++){

                            Image imagen = parseJsonObject(jsonArray.getJSONObject(i).getJSONArray("MainImages").getJSONObject(j), Image.class);

                            listaImagen.add(imagen);

                        }

                        feedJSON.setImagenes(listaImagen);
                    }

                    feedJSON.setChannel(feedChannel);

                    listaFeedJSON.add(feedJSON);

                }

                listaHistorial = listaFeedJSON;
            }
            catch (Exception e) {

                Log.e("Error +","aef" + e.getMessage());
                e.printStackTrace();


            } finally {

                if(connection != null) {
                    connection.disconnect();
                }
            }

            DataLoadedHandler<FeedJSON>dataLoadedHandler;

            dataLoadedHandler = data[0];

            return dataLoadedHandler;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
                /*for(int i = 0; i<listaHistorial.size();i++){

                    Log.e("Elemento "+i,listaHistorial.get(i).getFeedText()+" "
                            +listaHistorial.get(i).getAction().getActionName() +" "
                            +listaHistorial.get(i).getChannel().getCallLetter() +" "
                            +listaHistorial.get(i).getDate()+" "
                            //+listaHistorial.get(i).getImagenes().get(0).getImagePath()+"  " +
                            +"amigos"
                            +listaHistorial.get(i).getFriends().toString());
                }*/

        @Override
        protected void onPostExecute(DataLoadedHandler<FeedJSON> feedJSONDataLoadedHandler) {

            super.onPostExecute(feedJSONDataLoadedHandler);

            feedJSONDataLoadedHandler.loaded(listaHistorial);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
