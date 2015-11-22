package com.example.guelmis.ffap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guelmis.ffap.models.LineItem;
import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ItemCart extends ActionBarActivity {
    TextView pieza;
    TextView cantidad;
    Button add;
    Button delete;
    String usuario;
    Button cart;
    ActionBar actionbar;
    private Toolbar toolbar;
    private ImageView img4;
    private String imageurl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_cart);
        setActionBar();
        img4 = (ImageView) findViewById (R.id.imageV4);
        pieza = (TextView) findViewById(R.id.textViewDescripcion);
        cantidad = (TextView) findViewById(R.id.textViewQuantity);
        add = (Button) findViewById(R.id.buttonAdd);
        delete = (Button) findViewById(R.id.buttonDelete);
        cart = (Button) findViewById(R.id.btncart);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory()
                .cacheOnDisc().resetViewBeforeLoading()
                .build();

        Intent myIntent = getIntent();
        final int position = myIntent.getIntExtra("position", 0);
        final LineItem current_item = Home.cart.get(position);

        usuario = myIntent.getStringExtra("usuario");
        pieza.setText(current_item.getTitle());
        cantidad.setText(Integer.toString(current_item.getQuantity()));
        imageLoader.displayImage(imageurl, img4, options);
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
                    if (Home.cart.get(position).getQuantity() < 1) {
                        Home.cart.remove(position);
                        finish();
                    }
                } else if (!chCart.getMessage().equals("Force")) {
                    Toast.makeText(getApplicationContext(), chCart.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Home.cart.remove(position);
                    finish();
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
    public void setActionBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ItemCart");
        getSupportActionBar().setIcon(R.mipmap.ffap);
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
                Intent intent  = new Intent(this, Home.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                return true;
            case R.id.id_carrito:
                Intent myIntent  = new Intent(this, Carrito.class);
                myIntent.putExtra("usuario", usuario);
                startActivity(myIntent);
                return true;
            case R.id.id_ordenes:
                Intent intent1  = new Intent(this, Ordenes.class);
                intent1.putExtra("usuario", usuario);
                startActivity(intent1);
                return true;
            case R.id.id_vehiculos:
                Intent intent2  = new Intent(this, Vehiculos.class);
                intent2.putExtra("usuario", usuario);
                startActivity(intent2);
                return true;
            case R.id.id_chassis:
                Intent intent3  = new Intent(this, Chassis.class);
                intent3.putExtra("usuario", usuario);
                startActivity(intent3);
                return true;
            case R.id.id_logout:
                finishAffinity();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}