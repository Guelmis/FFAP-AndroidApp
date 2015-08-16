package com.example.guelmis.ffap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Carrito extends ActionBarActivity {
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayList<String> datos;
    //private String description;
    //private Double price;
    private String usuario;
    ActionBar actionbar;
    Button eliminar;
    Double itbis;
    Double total;
    JSONArray cart;

    private double calcTotal(ArrayList<LineItem> cart){
        double res = 0;
        for(int i=0; i<cart.size(); i++){
            res += (cart.get(i).getPrice()*cart.get(i).getQuantity());
        }
        return res;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito);
        eliminar = (Button) findViewById(R.id.btnclear);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("FFAP Carrito");
        actionbar.setIcon(R.mipmap.ffap);
        datos = new ArrayList<String>();
        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");
        //description = myIntent.getStringExtra("description");
        //price = myIntent.getDoubleExtra("price", 0);
        final TextView subtotal = (TextView) findViewById(R.id.carrito_subtotal);
        final TextView ITBIS = (TextView) findViewById(R.id.carrito_itbis);
        final TextView Total = (TextView) findViewById(R.id.carrito_total);
        subtotal.setText(new Double(calcTotal(Home.cart)).toString() + "$");
        itbis = (calcTotal(Home.cart) * 0.18);
        ITBIS.setText(itbis + "$".toString());
        total = (calcTotal(Home.cart) + itbis);
        Total.setText(total + "$".toString());
        List = (ListView) findViewById(R.id.listofprod);
        datos = new ArrayList<String>();

        class QueryCart extends AsyncTask<String,JSONArray,JSONArray>
        {
            private ProgressDialog nDialog;
            private JSONObject json1;
            private JSONArray jsonArr1;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            public JSONArray showCart(String username) {

                UserFunction userFunction = new UserFunction();
                JSONArray json = userFunction.showCart(username);
                return json;
            }

            @Override
            protected JSONArray doInBackground(String... args){
                if(args.length != 0 ){
                    jsonArr1 = showCart(args[0]);
                }
                else{
                    jsonArr1 = null;
                }
                return jsonArr1;
            }
            @Override
            protected void onPostExecute(JSONArray th){

                if(th != null){
                    //Toast.makeText(getApplicationContext(), jsonArr1.toString(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error, no se especifica usuario.", Toast.LENGTH_LONG).show();
                }
            }
        }

        cart = new JSONArray();

        try {
            cart = new QueryCart().execute(usuario).get();
            Home.cart = FillCart(cart);
            subtotal.setText(new Double(calcTotal(Home.cart)).toString() + "$");
            itbis = (calcTotal(Home.cart) * 0.18);
            ITBIS.setText(itbis + "$".toString());
            total = (calcTotal(Home.cart) + itbis);
            Total.setText(total + "$".toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(int i =0; i<Home.cart.size(); i++){
            datos.add(Home.cart.get(i).getQuantity() + " x " + Home.cart.get(i).getTitle() );
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        List.setAdapter(adaptador);
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.cart.clear();
                ITBIS.setText("0.0$");
                Total.setText("0.0$");
                datos.clear();
                adaptador.notifyDataSetChanged();
                subtotal.setText(new Double(calcTotal(Home.cart)).toString() + "$");
            }
        });
            }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Caso
            case R.id.id_home:
                Intent intent  = new Intent(Carrito.this, Home.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                return true;
            case R.id.id_carrito:
                startActivity(new Intent(Carrito.this, Carrito.class));
                return true;
        default:
        return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<LineItem> FillCart(JSONArray input){
        ArrayList<LineItem> ret = new ArrayList<LineItem>();
        for(int i=0; i<input.length(); i++){
            try {
                LineItem current = new LineItem(new Product(input.getJSONObject(i).getJSONObject(UserFunction.product_tag).getString("title"),
                        input.getJSONObject(i).getJSONObject(UserFunction.brand_tag).getString("brand_name"),
                        input.getJSONObject(i).getJSONObject(UserFunction.model_tag).getString("model_name"),
                        input.getJSONObject(i).getString("image_url"),
                        Double.parseDouble(input.getJSONObject(i).getJSONObject(UserFunction.product_tag).getString("price")),
                        Integer.parseInt(input.getJSONObject(i).getJSONObject(UserFunction.model_tag).getString("year")),
                        Integer.parseInt(input.getJSONObject(i).getJSONObject(UserFunction.product_tag).getString("id"))));

                current.setQuantity(input.getJSONObject(i).getJSONObject("item").getInt("quantity"));
                ret.add(current);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}