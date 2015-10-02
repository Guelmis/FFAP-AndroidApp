package com.example.guelmis.ffap.signaling;

import com.example.guelmis.ffap.models.Comment;
import com.example.guelmis.ffap.models.LineItem;
import com.example.guelmis.ffap.models.Product;
import com.example.guelmis.ffap.models.Seller;
import com.example.guelmis.ffap.models.Stock;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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

    /*
    public static final String loginURL = "http://10.0.0.21:3000/mobile_login/";
    public static final String searchURL = "http://10.0.0.21:3000/product_query/search/";
    public static final String spinnersURL = "http://10.0.0.21:3000/info_query/";
    public static final String sellersURL = "http://10.0.0.21:3000/seller_query/";
    public static final String productosURL = "http://10.0.0.21:3000/product_query/";
    public static final String cartshowURL = "http://10.0.0.21:3000/cart_query/";
    public static final String cartaddURL = "http://10.0.0.21:3000/cart_add/";
    public static final String cartremoveURL = "http://10.0.0.21:3000/cart_remove/";
    public static final String cartdestroyURL = "http://10.0.0.21:3000/cart_destroy/";
*/

    public static final String loginURL = "http://ffap-itt-2015.herokuapp.com/mobile_login/";
    public static final String searchURL = "http://ffap-itt-2015.herokuapp.com/product_query/search/";
    public static final String spinnersURL = "http://ffap-itt-2015.herokuapp.com/info_query/";
    public static final String sellersURL = "http://ffap-itt-2015.herokuapp.com/seller_query/";
    public static final String commentURL = "http://ffap-itt-2015.herokuapp.com/seller_query/comment/";
    public static final String productosURL = "http://ffap-itt-2015.herokuapp.com/product_query/";
    public static final String cartshowURL = "http://ffap-itt-2015.herokuapp.com/cart_query/";
    public static final String cartaddURL = "http://ffap-itt-2015.herokuapp.com/cart_add/";
    public static final String cartremoveURL = "http://ffap-itt-2015.herokuapp.com/cart_remove/";
    public static final String cartdestroyURL = "http://ffap-itt-2015.herokuapp.com/cart_destroy/";

    public static final String product_tag = "product";
    public static final String image_tag = "image_url";
    public static final String year_tag = "year";
    public static final String model_tag = "model";
    public static final String brand_tag = "brand";
    public static final String query_string_tag = "qstring";
    public static final String login_tag = "login";
    public static final String register_tag = "register";
    public static final String brand_model_map_tag = "brand_model";
    public static final String username_tag = "username";
    public static final String password_tag = "password";

    public static final String KEY_SUCCESS = "success";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_STATUS = "status";

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
        InfoQuery ret;

        ArrayList<String> brands = new ArrayList<String>();
        ArrayList<String> years = new ArrayList<String>();
        HashMap<String, ArrayList<String>> modelmap = new HashMap<String, ArrayList<String>>();

        try {
            for(int i=0; i<query.getJSONArray("brands").length(); i++){
                brands.add(query.getJSONArray("brands").getString(i));
            }
            for(int i=0; i<query.getJSONArray("years").length(); i++){
                years.add(Integer.toString(query.getJSONArray("years").getInt(i)));
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

    public static ArrayList<Product> searchProducts(String keyword, String brand, String model, String year){
        ArrayList<NameValuePair> params = new ArrayList<>();
        if(!keyword.equals("")) {
            params.add(new BasicNameValuePair(query_string_tag,keyword));
        }
        if(!brand.equals("")) {
            params.add(new BasicNameValuePair(brand_tag, brand));
        }
        if(!model.equals("")) {
            params.add(new BasicNameValuePair(model_tag, model));
        }
        if(!year.equals("")) {
            params.add(new BasicNameValuePair(year_tag, year));
        }

        JSONArray pJSONarr = null;
        try {
            pJSONarr = new JArrRequester().post(searchURL, params);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Product> ret = new ArrayList<>();

        for(int i=0; i<pJSONarr.length(); i++) {
            try {
                ret.add(new Product(pJSONarr.getJSONObject(i).getJSONObject(product_tag).getString("title"),
                        pJSONarr.getJSONObject(i).getJSONObject(brand_tag).getString("brand_name"),
                        pJSONarr.getJSONObject(i).getJSONObject(model_tag).getString("model_name"),
                        pJSONarr.getJSONObject(i).getString(image_tag),
                        Integer.parseInt(pJSONarr.getJSONObject(i).getJSONObject(model_tag).getString("year")),
                        (pJSONarr.getJSONObject(i).getJSONObject(product_tag).getString("id"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static Product ShowProduct(String id){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject productJSON = null;
        JSONArray _stocks;
        ArrayList<Stock> stocksList = new ArrayList<>();
        Product ret = null;

        try {
            productJSON = new JObjRequester().get(productosURL + id, params);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            _stocks = productJSON.getJSONArray("stocks");
            for(int i=0; i<_stocks.length(); i++){
                stocksList.add(new Stock(
                        Integer.parseInt(_stocks.getJSONObject(i).getJSONObject("stock").getString("id")),
                        Double.parseDouble(_stocks.getJSONObject(i).getJSONObject("stock").getString("price")),
                        Integer.parseInt(_stocks.getJSONObject(i).getJSONObject("stock").getString("quantity")),
                        _stocks.getJSONObject(i).getJSONObject("seller").getString("name"),
                        Integer.parseInt(_stocks.getJSONObject(i).getJSONObject("seller").getString("id"))
                ));
            }

            ret = new Product(
                    productJSON.getString("title"),
                    productJSON.getJSONObject(brand_tag).getString("brand_name"),
                    productJSON.getJSONObject(model_tag).getString("model_name"),
                    productJSON.getString("image_url"),
                    Integer.parseInt(productJSON.getJSONObject(model_tag).getString("year")),
                    productJSON.getString("id"),

                    stocksList
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static BasicResponse AddToCart(String username, String stock_id){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer;
        BasicResponse ret = new BasicResponse(false, "Error no identificado agregando pieza al carrito.", "");

        params.add(new BasicNameValuePair(username_tag, username));
        params.add(new BasicNameValuePair("stock_id", stock_id));

        try {
            answer = new JObjRequester().post(cartaddURL, params);
            if(answer.getString(KEY_SUCCESS).equals("true")){
                ret = new BasicResponse(true, answer.getString(KEY_MESSAGE), "");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static BasicResponse delFromCart(String username, String line_item_id){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer;
        BasicResponse ret = new BasicResponse(false, "Error no identificado removiendo pieza del carrito.", "");

        params.add(new BasicNameValuePair(username_tag, username));
        params.add(new BasicNameValuePair("line_item_id", line_item_id));

        try {
            answer = new JObjRequester().post(cartremoveURL, params);
            if(answer.getString(KEY_SUCCESS).equals("true")){
                ret = new BasicResponse(true, answer.getString(KEY_MESSAGE), "");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            ret = new BasicResponse(false, "Force", "");
        }

        return ret;
    }

    public static ArrayList<LineItem> ShowCart(String username){
        ArrayList<NameValuePair> params = new ArrayList<>();
        ArrayList<LineItem> cart = new ArrayList<>();
        JSONArray cartJSON;

        params.add(new BasicNameValuePair(username_tag, username));

        try {
            cartJSON = new JArrRequester().post(cartshowURL, params);

            for(int i=0; i<cartJSON.length(); i++){
                LineItem current = new LineItem(
                        new Product(cartJSON.getJSONObject(i).getJSONObject(product_tag).getString("title"),
                                cartJSON.getJSONObject(i).getJSONObject(brand_tag).getString("brand_name"),
                                cartJSON.getJSONObject(i).getJSONObject(model_tag).getString("model_name"),
                                cartJSON.getJSONObject(i).getString(image_tag),
                                Integer.parseInt(cartJSON.getJSONObject(i).getJSONObject(model_tag).getString("year")),
                                (cartJSON.getJSONObject(i).getJSONObject("item").getString("id"))),
                        new Stock(cartJSON.getJSONObject(i).getJSONObject("stock").getInt("id"),
                                cartJSON.getJSONObject(i).getJSONObject("stock").getDouble("price"),
                                cartJSON.getJSONObject(i).getJSONObject("stock").getInt("quantity"),
                                cartJSON.getJSONObject(i).getJSONObject("seller").getString("name"),
                                cartJSON.getJSONObject(i).getJSONObject("seller").getInt("id")));
                current.setQuantity(cartJSON.getJSONObject(i).getJSONObject("item").getInt("quantity"));
                cart.add(current);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cart;
    }

    public static BasicResponse Comment(String username, String seller_id, String title, String body){
        BasicResponse res = new BasicResponse(false, "Error no identificado al comentar.", "");
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject resJSON;

        params.add(new BasicNameValuePair(username_tag, username));
        params.add(new BasicNameValuePair("seller_id", seller_id));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("body", body));

        try {
            resJSON = new JObjRequester().post(commentURL, params);
            if(resJSON.getString(KEY_SUCCESS).equals("true")) {
                res = new BasicResponse(true, resJSON.getString(KEY_MESSAGE), "");
            }
            else {
                res = new BasicResponse(false, resJSON.getString(KEY_MESSAGE), "");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static Seller ShowSeller(String seller_id){
        ArrayList<NameValuePair> params = new ArrayList<>();
        ArrayList<Comment> reviews = new ArrayList<>();
        Seller ret = null;
        JSONObject sellerJSON;

        //params.add(new BasicNameValuePair("seller_id", seller_id));
        try {
            sellerJSON = new JObjRequester().get(sellersURL + seller_id, params);
            JSONArray comments = sellerJSON.getJSONArray("comments");
            for(int i=0; i<comments.length(); i++){
                reviews.add(new Comment(comments.getJSONObject(i).getJSONObject("comment").getString("title"),
                        comments.getJSONObject(i).getJSONObject("comment").getString("body"),
                        comments.getJSONObject(i).getJSONObject(username_tag).getString(username_tag)));
            }

            ret = new Seller(
                    sellerJSON.getString("name"),
                    sellerJSON.getString("address"),
                    sellerJSON.getString("phone"),
                    sellerJSON.getString(image_tag),
                    reviews);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static BasicResponse Login(String username, String password){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        BasicResponse ret = new BasicResponse(false, "Error no identificado al autenticarse.", "");

        params.add(new BasicNameValuePair(username_tag, username));
        params.add(new BasicNameValuePair(password_tag, password));

        try {
            answer = new JObjRequester().post(loginURL, params);
            if(answer.getString(KEY_SUCCESS).equals("true")) {
                ret = new BasicResponse(true, answer.getString(KEY_MESSAGE), "");
            }
            else {
                ret = new BasicResponse(false, answer.getString(KEY_MESSAGE), "");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
