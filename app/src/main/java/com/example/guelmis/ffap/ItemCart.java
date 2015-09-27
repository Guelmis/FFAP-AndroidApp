package com.example.guelmis.ffap;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guelmis.ffap.models.LineItem;
import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;

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
        cantidad.setText(Integer.toString(current_item.getQuantity()));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicResponse addToCart = ServerSignal.AddToCart(usuario,
                        Integer.toString(current_item.getSelectedStock().getId()));
                if (addToCart.success()) {
                    Home.cart.get(position).setQuantity(Home.cart.get(position).getQuantity() + 1);
                    cantidad.setText(Integer.toString(current_item.getQuantity()));
                } else {
                    Toast.makeText(getApplicationContext(), addToCart.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicResponse chCart = ServerSignal.delFromCart(usuario,
                        new Integer(current_item.getId()).toString());
                if (chCart.success()) {
                    Home.cart.get(position).setQuantity(Home.cart.get(position).getQuantity() - 1);
                    cantidad.setText(new Integer(current_item.getQuantity()).toString());
                    if(Home.cart.get(position).getQuantity() < 1){
                        Home.cart.remove(position);
                        finish();
                    }
                } else if(!chCart.getMessage().equals("Force")) {
                    Toast.makeText(getApplicationContext(), chCart.getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    Home.cart.remove(position);
                    finish();
                }
            }});

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}