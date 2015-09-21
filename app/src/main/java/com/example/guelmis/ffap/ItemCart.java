package com.example.guelmis.ffap;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by mario on 09/20/15.
 */
public class ItemCart extends Activity {
    TextView pieza;
    TextView cantidad;
    Button add;
    Button delete;
    String usuario;
    Button cart;

    class Delete extends AsyncTask<String, JSONObject, JSONObject> {
        private JSONObject json1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public JSONObject delprod(String uname, String pid) {

            UserFunction userFunction = new UserFunction();
            JSONObject json = userFunction.delFromCart(uname, pid);
            return json;
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            json1 = delprod(args[0], args[1]);
            return json1;
        }

        @Override
        protected void onPostExecute(JSONObject th) {

            if (th != null) {
                //Toast.makeText(getApplicationContext(), jsonArr1.toString(), Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    class AddToCart extends AsyncTask<String, JSONObject, JSONObject> {
        private JSONObject json1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public JSONObject addtoCart(String username, String pid) {

            UserFunction userFunction = new UserFunction();
            JSONObject json = userFunction.addToCart(username, pid);
            return json;
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            if (args.length != 0) {
                json1 = addtoCart(args[0], args[1]);
            } else {
                json1 = null;
            }
            return json1;
        }

        @Override
        protected void onPostExecute(JSONObject th) {

            if (th != null) {
                //Toast.makeText(getApplicationContext(), jsonArr1.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error, no se especifica usuario.", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_cart);
        pieza = (TextView) findViewById(R.id.textViewDescripcion);
        cantidad = (TextView) findViewById(R.id.textViewQuantity);
        add = (Button) findViewById(R.id.buttonAdd);
        delete = (Button) findViewById(R.id.buttonDelete);
        cart = (Button) findViewById(R.id.btncart);

        Intent myIntent = getIntent();
        final int position = myIntent.getIntExtra("position", 0);
        final LineItem current_item = Home.cart.get(position);
        usuario = myIntent.getStringExtra("usuario");

        pieza.setText(current_item.getDesc());
        cantidad.setText(new Integer(current_item.getQuantity()).toString());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject addToCart = new AddToCart().execute(usuario,
                            new Integer(current_item.getSelectedStock().getId()).toString()).get();
                    if (addToCart.getString("success").equals("true")) {
                        Home.cart.get(position).setQuantity(Home.cart.get(position).getQuantity() + 1);
                        cantidad.setText(new Integer(current_item.getQuantity()).toString());
                     /*   if(Home.cart.get(position).getQuantity() < 1){
                            Home.cart.remove(position);
                        }*/
                    } else {
                        Toast.makeText(getApplicationContext(), addToCart.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject chCart = new Delete().execute(usuario,
                            new Integer(current_item.getId()).toString()).get();
                    if (chCart.getString("success").equals("true")) {
                        Home.cart.get(position).setQuantity(Home.cart.get(position).getQuantity() - 1);
                        cantidad.setText(new Integer(current_item.getQuantity()).toString());
                        if(Home.cart.get(position).getQuantity() < 1){
                            Home.cart.remove(position);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), chCart.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}