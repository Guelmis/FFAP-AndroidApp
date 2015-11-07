package com.example.guelmis.ffap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;

public class Resenas extends ActionBarActivity {
    Button publicar;
    EditText resena;
    EditText titulo;
    RatingBar resenarating;
    double ratingSample;
    ActionBar actionbar;
    private String usuario;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resena);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("FFAP Reseñas");
        actionbar.setIcon(R.mipmap.ffap);
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");
        publicar = (Button) findViewById(R.id.btnpublicar);
        resena = (EditText) findViewById(R.id.editTextResena);
        titulo = (EditText) findViewById(R.id.editTextTitle);
        resenarating = (RatingBar) findViewById(R.id.ratingBComment);
        final Intent thisIntent = getIntent();
        ratingSample = 0.0;
        resenarating.setOnRatingBarChangeListener(

                new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                        ratingSample = rating;

                    }
                }
        );

        publicar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ratingSample == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Resenas.this).create();

                    AlertDialog alertDialog1 = new AlertDialog.Builder(Resenas.this).create();
                    alertDialog1.setTitle("Rating debe ser minimo 1");
                    alertDialog1.setMessage("La puntuacion del comentario debe ser de 1 a 5");
                    alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog1.show();
                } else if (ratingSample != 0) {
                    BasicResponse response = ServerSignal.Comment(thisIntent.getStringExtra("usuario"),
                            thisIntent.getStringExtra("seller_id"),
                            titulo.getText().toString(),
                            resena.getText().toString(),
                            Double.toString(ratingSample));
                    if (response.success()) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
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

