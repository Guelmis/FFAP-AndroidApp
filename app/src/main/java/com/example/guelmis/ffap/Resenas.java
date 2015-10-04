package com.example.guelmis.ffap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;

/**
 * Created by Guelmis on 7/8/2015.
 */
public class Resenas extends Activity {
    Button publicar;
    EditText resena;
    EditText titulo;
    RatingBar resenarating;
    TextView prueba;
    double ratingSample;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resena);
        publicar = (Button) findViewById(R.id.btnpublicar);
        resena = (EditText) findViewById(R.id.editTextResena);
        titulo = (EditText) findViewById(R.id.editTextTitle);
        resenarating = (RatingBar) findViewById(R.id.ratingBComment);
        prueba = (TextView) findViewById(R.id.textVTest);
        final Intent thisIntent = getIntent();
        ratingSample = 0.0;
        resenarating.setOnRatingBarChangeListener(

                new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                        prueba.setText(String.valueOf(rating));
                        ratingSample = rating;

                    }
                }
        );

        publicar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
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

        });
    }
}


