package com.example.guelmis.ffap;

/**
 * Created by Guelmis on 7/2/2015.
 */
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register extends Activity {

    Button registrar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        registrar = (Button) findViewById(R.id.btnregister);
        registrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Register.this, Registrado.class);
                startActivity(myIntent);
            }
        });
    }
}