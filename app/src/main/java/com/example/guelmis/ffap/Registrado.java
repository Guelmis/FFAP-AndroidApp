package com.example.guelmis.ffap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Guelmis on 7/8/2015.
 */

public class Registrado extends Activity {
    Button login;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrado);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Registrado.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}