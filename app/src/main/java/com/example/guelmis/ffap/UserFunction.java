package com.example.guelmis.ffap;


import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.guelmis.ffap.signaling.JSONParser;

public class UserFunction {
    private JSONParser jsonParser;
/*
    public static final String loginURL = "http://10.0.0.21:3000/mobile_login/";
    public static final String searchURL = "http://10.0.0.21:3000/product_query/search/";
    public static final String spinnersURL = "http://10.0.0.21:3000/info_query/";
    public static final String sellersURL = "http://10.0.0.21:3000/seller_query/";
    public static final String proyectoURL = "http://10.0.0.21:3000/product_query/";
    public static final String cartshowURL = "http://10.0.0.21:3000/cart_query/";
    public static final String cartaddURL = "http://10.0.0.21:3000/cart_add/";
    public static final String cartremoveURL = "http://10.0.0.21:3000/cart_remove/";
    public static final String cartdestroyURL = "http://10.0.0.21:3000/cart_destroy/";
*/

    public static final String loginURL = "http://ffap-itt-2015.herokuapp.com/mobile_login/";
    public static final String searchURL = "http://ffap-itt-2015.herokuapp.com/product_query/search/";
    public static final String spinnersURL = "http://ffap-itt-2015.herokuapp.com/info_query/";
    public static final String sellersURL = "http://ffap-itt-2015.herokuapp.com/seller_query/";
    public static final String proyectoURL = "http://ffap-itt-2015.herokuapp.com/product_query/";
    public static final String cartshowURL = "http://ffap-itt-2015.herokuapp.com/cart_query/";
    public static final String cartaddURL = "http://ffap-itt-2015.herokuapp.com/cart_add/";
    public static final String cartremoveURL = "http://ffap-itt-2015.herokuapp.com/cart_remove/";
    public static final String cartdestroyURL = "http://ffap-itt-2015.herokuapp.com/cart_destroy/";

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

    public JSONObject showprod(String prod_id){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
       // params.add(new BasicNameValuePair("id", prod_id));
        JSONObject json = jsonParser.getJSONFromUrl(proyectoURL + prod_id, params);
        return json;
    }

    public JSONObject showseller(String seller_id){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject json = jsonParser.getJSONFromUrl(sellersURL + seller_id, params);
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

    public JSONArray showCart(String username) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        JSONArray json = jsonParser.postJSONArrFromUrl(cartshowURL, params);
        return json;
    }

    public JSONObject addToCart(String username, String stock_id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("stock_id", stock_id));
        JSONObject json = jsonParser.postJSONFromUrl(cartaddURL, params);
        return json;
    }

    public JSONObject delFromCart(String username, String line_item_id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("line_item_id", line_item_id));
        JSONObject json = jsonParser.postJSONFromUrl(cartremoveURL, params);
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
