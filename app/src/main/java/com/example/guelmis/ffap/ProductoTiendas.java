package com.example.guelmis.ffap;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.guelmis.ffap.models.Product;
import com.example.guelmis.ffap.models.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProductoTiendas extends Activity {
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayList<String> datos;
    String usuario;

    class ShowProduct extends AsyncTask<String,JSONObject,JSONObject>
    {
        private ProgressDialog nDialog;
        private JSONObject json1;
        private JSONArray jsonArr1;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        public JSONObject getProdInfo(String id) {

            UserFunction userFunction = new UserFunction();
            JSONObject json = userFunction.showprod(id);
            return json;
        }

        @Override
        protected JSONObject doInBackground(String... args){
            json1 = getProdInfo(args[0]);
            return json1;
        }
        @Override
        protected void onPostExecute(JSONObject th){

            if(th != null){
            }
            else{
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producttienda);
        datos = new ArrayList<String>();
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        List = (ListView) findViewById(R.id.listStoreProduct);
        List.setAdapter(adaptador);

        JSONObject productoJSON;
        Intent thisIntent = getIntent();
        usuario = thisIntent.getStringExtra("usuario");
        Product productInfo = null;
        JSONArray stocksJSON;
        ArrayList<Stock> stocksList = null;

        try {
            productoJSON = new ShowProduct().execute(thisIntent.getStringExtra("prod_id")).get();
            productInfo = new Product(
                    productoJSON.getString("title"),
                    productoJSON.getJSONObject(UserFunction.brand_tag).getString("brand_name"),
                    productoJSON.getJSONObject(UserFunction.model_tag).getString("model_name"),
                    productoJSON.getString("image_url"),
                    Integer.parseInt(productoJSON.getJSONObject(UserFunction.model_tag).getString("year")),
                    productoJSON.getString("id")
                    );
            stocksJSON = productoJSON.getJSONArray("stocks");
            stocksList = listStocks(stocksJSON);

            for(int i=0; i<stocksList.size(); i++){
                datos.add(stocksList.get(i).getSellerName() + ", $" + stocksList.get(i).getPrice());
            }
            //adaptador.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final Product product = productInfo;
        final ArrayList<Stock> stocks = stocksList;

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(ProductoTiendas.this, Piezas.class);
                try {
                    if (stocks != null && product != null) {
                        myIntent.putExtra("title", product.getTitle());
                        myIntent.putExtra("image_url", product.getImageurl());
                        myIntent.putExtra("brand", product.getBrand());
                        myIntent.putExtra("model", product.getModel());
                        myIntent.putExtra("year", product.getYear().toString());
                        myIntent.putExtra("id", product.getId());
                        myIntent.putExtra("usuario",usuario);

                        myIntent.putExtra("stock_id", stocks.get(position).getId());
                        myIntent.putExtra("price", stocks.get(position).getPrice());
                        myIntent.putExtra("quantity", stocks.get(position).getQuantity());
                        myIntent.putExtra("seller_id", stocks.get(position).getSellerId());
                        myIntent.putExtra("seller_name", stocks.get(position).getSellerName());
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                startActivity(myIntent);
            }
        });
    }

    private ArrayList<Stock> listStocks(JSONArray _stocks) throws JSONException {
        ArrayList<Stock> stocksList = new ArrayList<Stock>();
        for(int i=0; i<_stocks.length(); i++){
            stocksList.add(new Stock(
                    Integer.parseInt(_stocks.getJSONObject(i).getJSONObject("stock").getString("id")),
                    Double.parseDouble(_stocks.getJSONObject(i).getJSONObject("stock").getString("price")),
                    Integer.parseInt(_stocks.getJSONObject(i).getJSONObject("stock").getString("quantity")),
                    _stocks.getJSONObject(i).getJSONObject("seller").getString("name"),
                    Integer.parseInt(_stocks.getJSONObject(i).getJSONObject("seller").getString("id"))
            ));
        }
        return stocksList;
    }
}
