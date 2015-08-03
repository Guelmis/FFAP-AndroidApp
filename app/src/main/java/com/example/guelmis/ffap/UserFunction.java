package com.example.guelmis.ffap;


import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;

public class UserFunction {
    private JSONParser jsonParser;


    public static final String loginURL = "http://ffap-itt-2015.herokuapp.com/mobile_login";
    public static final String searchURL = "http://ffap-itt-2015.herokuapp.com/product_query/search";
    public static final String spinnersURL = "http://ffap-itt-2015.herokuapp.com/info_query";
    public static final String proyectoURL = "http://ffap-itt-2015.herokuapp.com/product_query";

    public static final String product_tag = "product";
    public static final String image_tag = "image_url";
    public static final String model_tag = "model";
    public static final String brand_tag = "brand";
    public static final String login_tag = "login";
    public static final String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chPass";
    private static String searchpro_tag = "format";
    private static String searchproBar_tag = "searchproBar";
    private static String searchpro_offer = "searchproOff";


    // constructor
    public UserFunction(){
        jsonParser = new JSONParser();
    }

    public JSONArray search(String qstring, String brand, String model, String year){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("qstring", qstring));
        params.add(new BasicNameValuePair("brand", brand));
        params.add(new BasicNameValuePair("model", model));
        params.add(new BasicNameValuePair("year", year));
        JSONArray json = jsonParser.postJSONArrFromUrl(searchURL, params);
        return json;
    }

    public JSONObject spinnerinfo(){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("tag", searchpro_tag));
        //params.add(new BasicNameValuePair("search", ));
        JSONObject json = jsonParser.getJSONFromUrl(spinnersURL, params);
        return json;
    }

    public JSONArray listObj(String searchProduct){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", searchpro_tag));
        params.add(new BasicNameValuePair("search", searchProduct));
        JSONArray json = jsonParser.getJSONArrFromUrl(proyectoURL, params);
        return json;
    }
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("username", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.postJSONFromUrl(loginURL, params);
        return json;
    }
}
