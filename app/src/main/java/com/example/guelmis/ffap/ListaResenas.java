package com.example.guelmis.ffap;

/**
 * Created by Guelmis on 7/2/2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.guelmis.ffap.models.Comment;
import com.example.guelmis.ffap.models.Seller;
import com.example.guelmis.ffap.signaling.ServerSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListaResenas extends Activity {
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayList<String> datos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resena_tienda);
        datos = new ArrayList<String>();
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        List = (ListView) findViewById(R.id.listViewComments);
        List.setAdapter(adaptador);

        Intent myIntent = getIntent();
        int sellerid = myIntent.getIntExtra("seller_id", 0);

        Seller seller = ServerSignal.ShowSeller(Integer.toString(sellerid));

        for(int i=0; i<seller.getReviews().size(); i++){
            datos.add(seller.getReviews().get(i).getTitle() + " -- " + seller.getReviews().get(i).getUsername());
        }
        adaptador.notifyDataSetChanged();

        final ArrayList<Comment> verresenas = seller.getReviews();

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(ListaResenas.this, VerResenas.class);
                myIntent.putExtra("title", verresenas.get(position).getTitle());
                myIntent.putExtra("username", verresenas.get(position).getUsername());
                myIntent.putExtra("body", verresenas.get(position).getBody());
                myIntent.putExtra("rating", verresenas.get(position).getRating());
                startActivity(myIntent);
            }
        });
    }
}