package com.example.guelmis.ffap;

import android.app.Notification;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Tienda  extends ActionBarActivity {

    Button resena;
    Button ubicacion;
    Button comentario;
    ActionBar actionbar;
    private ImageView iv;
    private String usuario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tienda);
        iv = (ImageView) findViewById(R.id.imageV1);
        iv.setImageResource(R.drawable.logo);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("FFAP Tienda");
        actionbar.setIcon(R.mipmap.ffap);
        resena = (Button) findViewById(R.id.btnresena);
        ubicacion = (Button) findViewById(R.id.btnubicacion);
        comentario = (Button) findViewById(R.id.btncomment);
        resena.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Tienda.this,VerResenas.class);
                startActivity(myIntent);
            }});
        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");
        ubicacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Tienda.this,Mapa.class);
                startActivity(myIntent);
            }});
        comentario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Tienda.this,Resenas.class);
                startActivity(myIntent);
            }});
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
                Intent myIntent = new Intent(this, Carrito.class);
                myIntent.putExtra("usuario", usuario);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}