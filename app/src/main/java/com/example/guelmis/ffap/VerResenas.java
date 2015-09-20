package com.example.guelmis.ffap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by mario on 09/19/15.
 */
public class VerResenas extends Activity {
    TextView user;
    TextView title;
    TextView review;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resena_select);
        user = (TextView) findViewById(R.id.textViewUserReview);
        title = (TextView) findViewById(R.id.textViewTitle);
        review = (TextView) findViewById(R.id.textViewComment);
        Intent myIntent = getIntent();

        user.setText(myIntent.getStringExtra("username"));
        title.setText(myIntent.getStringExtra("title"));
        review.setText(myIntent.getStringExtra("body"));
    }
}