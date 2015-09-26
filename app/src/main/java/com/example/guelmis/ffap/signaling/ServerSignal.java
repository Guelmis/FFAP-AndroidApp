package com.example.guelmis.ffap.signaling;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mario on 09/22/15.
 */
public class ServerSignal {

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
    public static final String brand_model_map_tag = "brand_model";

    public static InfoQuery spinnersQuery(){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject query = null;
        try {
            query = new JObjRequester().get(spinnersURL, params);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        InfoQuery ret = null;

        ArrayList<String> brands = new ArrayList<String>();
        ArrayList<String> years = new ArrayList<String>();
        HashMap<String, ArrayList<String>> modelmap = new HashMap<String, ArrayList<String>>();

        try {
            for(int i=0; i<query.getJSONArray("brands").length(); i++){
                brands.add(query.getJSONArray("brands").getString(i));
            }
            for(int i=0; i<query.getJSONArray("years").length(); i++){
                years.add(new Integer(query.getJSONArray("years").getInt(i)).toString());
            }
            for(int i=0; i<brands.size(); i++){
                ArrayList<String> temp = new ArrayList<String>();
                for(int j=0; j<query.getJSONObject(brand_model_map_tag).getJSONArray(brands.get(i)).length(); j++){
                    temp.add(query.getJSONObject(brand_model_map_tag).getJSONArray(brands.get(i)).getString(j));
                }
                modelmap.put(brands.get(i), temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ret = new InfoQuery(brands,modelmap,years);
        return ret;
    }

   // public static searchProducts(){}
}
