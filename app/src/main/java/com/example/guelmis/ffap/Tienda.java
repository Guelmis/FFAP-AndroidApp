package com.example.guelmis.ffap;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.guelmis.ffap.models.Seller;
import com.example.guelmis.ffap.signaling.ServerSignal;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Tienda  extends ActionBarActivity {

    Button resena;
    Button ubicacion;
    Button comentario;
    ActionBar actionbar;
    private ImageView iv;
    private String usuario;
    TextView direccion;
    TextView tienda;
    RatingBar ratingavg;
    Seller sellerdata;
    TextView telefono;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tienda);
        setActionBar();
        direccion = (TextView) findViewById(R.id.textView6);
        tienda = (TextView) findViewById(R.id.textView5);
        iv = (ImageView) findViewById(R.id.imageV2);
        resena = (Button) findViewById(R.id.btnresena);
        ubicacion = (Button) findViewById(R.id.btnubicacion);
        comentario = (Button) findViewById(R.id.btncomment);
        ratingavg = (RatingBar) findViewById(R.id.ratingBar2);
        telefono = (TextView) findViewById(R.id.textView11);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory()
                .cacheOnDisc().resetViewBeforeLoading()
                .build();
        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");

        final int sellerid = myIntent.getIntExtra("seller_id", 0);

        sellerdata = ServerSignal.ShowSeller(Integer.toString(sellerid));
        imageLoader.displayImage(sellerdata.getLogo_url(), iv, options);
<<<<<<< HEAD
        direccion.setText(sellerdata.getAddress());
        tienda.setText(sellerdata.getName());
        ratingavg.setRating(sellerdata.getAverageRating());
        telefono.setText(sellerdata.getPhone());
=======
        direccion.setText("Dirección: "+ sellerdata.getAddress());
        tienda.setText(sellerdata.getName());
        ratingavg.setRating(sellerdata.getAverageRating());
        telefono.setText("Teléfono: "+ sellerdata.getPhone());
>>>>>>> origin/master

        comentario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Tienda.this, Resenas.class);
                myIntent.putExtra("usuario", usuario);
                myIntent.putExtra("seller_id", Integer.toString(sellerid));
                startActivity(myIntent);
            }
        });
        resena.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Tienda.this,ListaResenas.class);
                myIntent.putExtra("usuario", usuario);
                myIntent.putExtra("seller_id", sellerid);
                startActivity(myIntent);
            }});
        ubicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Tienda.this, Mapa.class);
                myIntent.putExtra("seller_id", sellerid);
                myIntent.putExtra("seller_name", sellerdata.getName());
                myIntent.putExtra("seller_address", sellerdata.getAddress());
                startActivity(myIntent);
            }
        });
    }

    public void setActionBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tienda");
        getSupportActionBar().setIcon(R.mipmap.ffap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        sellerdata = ServerSignal.ShowSeller(sellerdata.getID());
        ratingavg.setRating(sellerdata.getAverageRating());
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
                Intent myIntent = new Intent(this, Carrito.class);
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