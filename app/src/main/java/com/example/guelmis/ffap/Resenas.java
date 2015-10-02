package com.example.guelmis.ffap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resena);
        publicar = (Button) findViewById(R.id.btnpublicar);
        resena = (EditText) findViewById(R.id.editTextResena);
        titulo = (EditText) findViewById(R.id.editTextTitle);
        final Intent thisIntent = getIntent();

        publicar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BasicResponse response = ServerSignal.Comment(thisIntent.getStringExtra("usuario"),
                        thisIntent.getStringExtra("seller_id"),
                        titulo.getText().toString(),
                        resena.getText().toString());
                if(response.success()) {
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                }
    }

        });
}
}


