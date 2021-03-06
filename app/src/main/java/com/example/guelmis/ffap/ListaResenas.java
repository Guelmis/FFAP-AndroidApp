package com.example.guelmis.ffap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guelmis.ffap.models.Comment;
import com.example.guelmis.ffap.models.Seller;
import com.example.guelmis.ffap.signaling.ServerSignal;

import java.util.ArrayList;

public class ListaResenas extends ActionBarActivity {
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayList<String> datos;
    ActionBar actionbar;
    private String usuario;
    private static String reviewtotal;
    RatingBar reviewsearch;
    TextView reviewdisplay;
    Seller seller;
    private Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resena_tienda);
        setActionBar();
        reviewsearch = (RatingBar) findViewById(R.id.ratingBarReview);
        reviewdisplay = (TextView) findViewById(R.id.textViewReview);
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");
        datos = new ArrayList<String>();
        adaptador = new ArrayAdapter<String>(this, R.layout.listviewcolor, R.id.textView14, datos);
        List = (ListView) findViewById(R.id.listViewComments);
        List.setAdapter(adaptador);

        Intent myIntent = getIntent();
        int sellerid = myIntent.getIntExtra("seller_id", 0);

        seller = ServerSignal.ShowSeller(Integer.toString(sellerid));

        for(int i=0; i<seller.getReviews().size(); i++){
            long rtg = Math.round(seller.getReviews().get(i).getRating());
            datos.add("Cliente: " + seller.getReviews().get(i).getUsername() + "\n" + "Puntuación: " + rtg + "/5" + " \n"
                    + "Título: " + seller.getReviews().get(i).getTitle() + "\n"
                    + "Comentario: " + seller.getReviews().get(i).getBody());
        }
        reviewdisplay.setText(Integer.toString(datos.size()) +"/" + Integer.toString(datos.size())+ " comentarios" );
        reviewtotal = Integer.toString(datos.size());
        adaptador.notifyDataSetChanged();

        reviewsearch.setOnRatingBarChangeListener(

                new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        datos.clear();
                        for(int i=0; i<seller.getReviews().size(); i++){
                            long rt = Math.round(rating);
                            Comment current = seller.getReviews().get(i);
                            long cmp = Math.round(current.getRating());
                            if(cmp == rt){
                                datos.add("Cliente: " + current.getUsername() + "\n" + "Puntuación: " +
                                        rt + "/5" + " \n" + "Título: " + current.getTitle() + "\n" +
                                        "Comentario: " + current.getBody());
                            }
                        }
                        reviewdisplay.setText(Integer.toString(datos.size()) + "/" + reviewtotal + " comentarios");
                        adaptador.notifyDataSetChanged();
                    }
                }
        );

        final ArrayList<Comment> verresenas = seller.getReviews();
    }
    public void setActionBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lista De Reseñas");
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
            case R.id.id_ordersqueue:
                Intent intent4  = new Intent(this, QueueOrders.class);
                intent4.putExtra("usuario", usuario);
                startActivity(intent4);
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