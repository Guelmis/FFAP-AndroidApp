package com.example.guelmis.ffap.signaling;

import com.example.guelmis.ffap.models.Comment;
import com.example.guelmis.ffap.models.LineItem;
import com.example.guelmis.ffap.models.Order;
import com.example.guelmis.ffap.models.Product;
import com.example.guelmis.ffap.models.Seller;
import com.example.guelmis.ffap.models.Stock;
import com.example.guelmis.ffap.models.Vehicle;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServerSignal {


   // public static final String domain = "http://10.0.0.6:5000/"; //local
    public static final String domain = "http://ffap-itt-2015.herokuapp.com/"; //web

    public static final String loginURL = domain + "mobile_login/";
    public static final String spinnersURL = domain + "info_query/";
    public static final String searchURL = domain + "product_query/search/";
    public static final String sellersURL = domain + "seller_query/";
    public static final String commentURL = domain + "seller_query/comment/";
    public static final String productosURL = domain + "product_query/";
    public static final String cartshowURL = domain + "cart_query/";
    public static final String cartaddURL = domain + "cart_add/";
    public static final String cartremoveURL = domain + "cart_remove/";
    public static final String cartdestroyURL = domain + "cart_destroy/";
    public static final String ordercreateURL = domain + "order_api/create/";
    public static final String ordershowURL = domain + "order_api/";
    public static final String orderconfirmURL = domain + "orders/confirm/";
    public static final String orderlistURL = domain + "order_api/list/";
    public static final String regvehicleURL = domain + "register_vehicle/";
    public static final String showvehicleURL = domain + "show_vehicle/";
    public static final String listvehiclesURL = domain + "list_vehicles/";
    public static final String destroyvehicleURL = domain + "destroy_vehicle/";
    public static final String deliveryetaURL = domain + "delivery_eta/";
    public static final String hook = domain + "hook/";

    public static final String edmunds_pt1 = "https://api.edmunds.com/api/vehicle/v2/vins/";
    public static final String edmunds_pt2 = "?&fmt=json&api_key=cpes64w9wyy4yd8anrvqz74t";

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
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        InfoQuery ret;

        if(query == null){
            return null;
        }

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
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if(pJSONarr == null){
            return new ArrayList<>();
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

    public static BasicResponse Comment(String username, String seller_id, String title, String body, String rating){
        BasicResponse res = new BasicResponse(false, "Error no identificado al comentar.", "");
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject resJSON;

        params.add(new BasicNameValuePair(username_tag, username));
        params.add(new BasicNameValuePair("seller_id", seller_id));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("body", body));
        params.add(new BasicNameValuePair("rating", rating));

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

        try {
            sellerJSON = new JObjRequester().get(sellersURL + seller_id, params);
            JSONArray comments = sellerJSON.getJSONArray("comments");
            for(int i=0; i<comments.length(); i++){
                reviews.add(new Comment(comments.getJSONObject(i).getJSONObject("comment").getString("title"),
                        comments.getJSONObject(i).getJSONObject("comment").getString("body"),
                        comments.getJSONObject(i).getJSONObject(username_tag).getString(username_tag),
                        comments.getJSONObject(i).getJSONObject("comment").getDouble("rating")));
            }

            LatLng location = new LatLng(
                    Double.valueOf(sellerJSON.getJSONObject("location").getString("latitude")),
                    Double.valueOf(sellerJSON.getJSONObject("location").getString("longitude")));

            ret = new Seller(
                    sellerJSON.getString("id"),
                    sellerJSON.getString("name"),
                    sellerJSON.getString("address"),
                    sellerJSON.getString("phone"),
                    sellerJSON.getString(image_tag),
                    reviews,
                    location);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static JSONObject Login(String username, String password){
        ArrayList<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair(username_tag, username));
        params.add(new BasicNameValuePair(password_tag, password));
        params.add(new BasicNameValuePair("role", "client"));

        JSONObject json = JSONParser.postJSONFromUrl(loginURL, params);
        return json;
    }

    public static BasicResponse checkout(String username){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        BasicResponse ret = new BasicResponse(false, "Error no identificado al crear orden.", "");

        params.add(new BasicNameValuePair(username_tag, username));

        try {
            answer = new JObjRequester().post(ordercreateURL, params);
            if(answer.getString(KEY_SUCCESS).equals("true")){
                ret = new BasicResponse(true, answer.getString(KEY_MESSAGE),"");
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

    public static BasicResponse checkout(String username, LatLng location){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        BasicResponse ret = new BasicResponse(false, "Error no identificado al crear orden.", "");

        params.add(new BasicNameValuePair("latitude", Double.toString(location.latitude)));
        params.add(new BasicNameValuePair("longitude", Double.toString(location.longitude)));
        params.add(new BasicNameValuePair(username_tag, username));

        try {
            answer = new JObjRequester().post(ordercreateURL, params);
            if(answer.getString(KEY_SUCCESS).equals("true")){
                ret = new BasicResponse(true, answer.getString(KEY_MESSAGE),"");
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

    public static Vehicle edmundQuery(String vin){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        Vehicle ret = null;

        try {
            answer = new JObjRequester().get(edmunds_pt1 + vin + edmunds_pt2, params);
            ret = new Vehicle(answer.getJSONObject("make").getString("name"),
                    answer.getJSONObject("model").getString("name"),
                    answer.getJSONArray("years").getJSONObject(0).getString("year"),
                    vin);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static BasicResponse regVehicle(String username, Vehicle input){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        BasicResponse ret = new BasicResponse(false, "Error no identificado al registrar vehiculo.", "");

        params.add(new BasicNameValuePair("chassis", input.getVin()));
        params.add(new BasicNameValuePair("brand", input.getBrand()));
        params.add(new BasicNameValuePair("model", input.getModel()));
        params.add(new BasicNameValuePair("year", input.getYear()));
        params.add(new BasicNameValuePair(username_tag, username));

        try {
            answer = new JObjRequester().post(regvehicleURL, params);
            if(answer.getString(KEY_SUCCESS).equals("true")){
                ret = new BasicResponse(true, answer.getString(KEY_MESSAGE), "");
            }
            else{
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

    public static BasicResponse destroyCart(String username){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        BasicResponse ret = new BasicResponse(false, "Error de comunicacion.", "");

        params.add(new BasicNameValuePair(username_tag, username));

        try {
            answer = new JObjRequester().post(cartdestroyURL, params);
            ret = new BasicResponse(true, answer.getString(KEY_MESSAGE), "");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static ArrayList<Vehicle> listVehicles(String username){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONArray answer = null;
        ArrayList<Vehicle> ret = null;

        params.add(new BasicNameValuePair(username_tag, username));

        try {
            answer = new JArrRequester().post(listvehiclesURL, params);
            JSONObject current;
            ret = new ArrayList<>();
            for (int i=0; i<answer.length(); i++){
                current = answer.getJSONObject(i).getJSONObject("vehicle");
                ret.add(new Vehicle(
                        current.getInt("id"),
                        current.getString("brand"),
                        current.getString("model"),
                        current.getString("year"),
                        current.getString("chassis_number")
                ));
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

    public static BasicResponse delVehicle(String username, int vehicleid){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        BasicResponse ret = new BasicResponse(false, "Error no identificado al registrar vehiculo.", "");

        params.add(new BasicNameValuePair("id", Integer.toString(vehicleid)));
        params.add(new BasicNameValuePair(username_tag, username));

        try {
            answer = new JObjRequester().post(destroyvehicleURL, params);
            if(answer.getString(KEY_SUCCESS).equals("true")){
                ret = new BasicResponse(true, answer.getString(KEY_MESSAGE), "");
            }
            else{
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

    public static ArrayList<Order> listOrders(String username, String processed){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONArray answer = null;
        ArrayList<Order> ret = null;

        params.add(new BasicNameValuePair(username_tag, username));
        params.add(new BasicNameValuePair("processed", processed));

        try {
            answer = new JArrRequester().post(orderlistURL, params);
            ret = new ArrayList<>();
            for (int i=0; i<answer.length(); i++){
                JSONObject current = answer.getJSONObject(i).getJSONObject("order");
                JSONArray itemsJSON = answer.getJSONObject(i).getJSONArray("line_items");
                ArrayList<LineItem> lineItems = new ArrayList<>();
                for(int j=0; j<itemsJSON.length(); j++){
                    LineItem item = new LineItem(
                            new Product(itemsJSON.getJSONObject(j).getJSONObject(product_tag).getString("title"),
                                    itemsJSON.getJSONObject(j).getJSONObject(brand_tag).getString("brand_name"),
                                    itemsJSON.getJSONObject(j).getJSONObject(model_tag).getString("model_name"),
                                    itemsJSON.getJSONObject(j).getString(image_tag),
                                    Integer.parseInt(itemsJSON.getJSONObject(j).getJSONObject(model_tag).getString("year")),
                                    (itemsJSON.getJSONObject(j).getJSONObject("item").getString("id"))),
                            new Stock(itemsJSON.getJSONObject(j).getJSONObject("stock").getInt("id"),
                                    itemsJSON.getJSONObject(j).getJSONObject("stock").getDouble("price"),
                                    itemsJSON.getJSONObject(j).getJSONObject("stock").getInt("quantity"),
                                    itemsJSON.getJSONObject(j).getJSONObject("seller").getString("name"),
                                    itemsJSON.getJSONObject(j).getJSONObject("seller").getInt("id")));
                    item.setQuantity(itemsJSON.getJSONObject(j).getJSONObject("item").getInt("quantity"));
                    lineItems.add(item);
                }
                ret.add(new Order(
                        current.getInt("id"),
                        current.getString("address"),
                        current.getString("invoice"),
                        answer.getJSONObject(i).getInt("delivery_id"),
                        current.getBoolean("delivered"),
                        false,
                        current.getBoolean("processed"),
                        current.getString("created_at"),
                        lineItems,
                        current.getString("status")
                ));
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

    public static Order showOrder(int order_id){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        Order ret = null;

        try {
            answer = new JObjRequester().get(ordershowURL + order_id, params);
            JSONArray itemsJSON = answer.getJSONArray("line_items");
            ArrayList<LineItem> lineItems = new ArrayList<>();
            for(int j=0; j<itemsJSON.length(); j++){
                LineItem item = new LineItem(
                        new Product(itemsJSON.getJSONObject(j).getJSONObject(product_tag).getString("title"),
                                itemsJSON.getJSONObject(j).getJSONObject(brand_tag).getString("brand_name"),
                                itemsJSON.getJSONObject(j).getJSONObject(model_tag).getString("model_name"),
                                itemsJSON.getJSONObject(j).getString(image_tag),
                                Integer.parseInt(itemsJSON.getJSONObject(j).getJSONObject(model_tag).getString("year")),
                                (itemsJSON.getJSONObject(j).getJSONObject("item").getString("id"))),
                        new Stock(itemsJSON.getJSONObject(j).getJSONObject("stock").getInt("id"),
                                itemsJSON.getJSONObject(j).getJSONObject("stock").getDouble("price"),
                                itemsJSON.getJSONObject(j).getJSONObject("stock").getInt("quantity"),
                                itemsJSON.getJSONObject(j).getJSONObject("seller").getString("name"),
                                itemsJSON.getJSONObject(j).getJSONObject("seller").getInt("id")));
                item.setQuantity(itemsJSON.getJSONObject(j).getJSONObject("item").getInt("quantity"));
                lineItems.add(item);
            }
            boolean confirmed;
            if(answer.get("confirmed").equals(null)){
                confirmed = false;
            }
            else {
                confirmed = answer.getBoolean("confirmed");
            }
            ret = new Order(
                    answer.getInt("id"),
                    answer.getString("address"),
                    answer.getString("invoice"),
                    answer.getInt("delivery_id"),
                    answer.getBoolean("delivered"),
                    answer.getBoolean("processed"),
                    confirmed,
                    answer.getString("created_at"),
                    lineItems,
                    answer.getString("status")
            );
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static BasicResponse confirmOrder(int order_id, Boolean confrim){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        BasicResponse ret = new BasicResponse(false, "Error de comunicacion.", "");

        params.add(new BasicNameValuePair("id", Integer.toString(order_id)));
        params.add(new BasicNameValuePair("confirm", confrim.toString()));

        try {
            answer = new JObjRequester().post(orderconfirmURL, params);
            if(answer.getString(KEY_SUCCESS).equals("true")){
                ret = new BasicResponse(true, answer.getString(KEY_MESSAGE), "");
            }
            else{
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

    public static BasicResponse deliveryeta(int delivery_id){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        BasicResponse ret = new BasicResponse(false, "Error de comunicacion.", "");

        try {
            answer = new JObjRequester().get(deliveryetaURL+ delivery_id, params);
            ret = new BasicResponse(true, answer.getString("eta"), answer.getString(KEY_STATUS));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static BasicResponse postHook(String invoice){
        ArrayList<NameValuePair> params = new ArrayList<>();
        JSONObject answer = null;
        BasicResponse ret = new BasicResponse(false, "Error de comunicacion.", "");

        params.add(new BasicNameValuePair("invoice", invoice));
        params.add(new BasicNameValuePair("txn_id", invoice));
        params.add(new BasicNameValuePair("payment_status", "Completed"));

        try {
            answer = new JObjRequester().post(hook, params);
            ret = new BasicResponse(true, "", "");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
