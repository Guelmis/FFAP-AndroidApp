package com.example.guelmis.ffap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.guelmis.ffap.R;
import com.example.guelmis.ffap.models.Order;
import com.example.guelmis.ffap.models.Vehicle;
import com.example.guelmis.ffap.signaling.ServerSignal;

import java.util.ArrayList;

public class Ordenes extends ActionBarActivity {
    ListView ListaOrdenes;
    private String usuario;
    ActionBar actionbar;
    ArrayList<String> datos;
    ArrayAdapter<String> adaptador;
    ArrayList<Order> ordenes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordenes);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("FFAP Ordenes");
        actionbar.setIcon(R.mipmap.ffap);
        ListaOrdenes = (ListView) findViewById(R.id.listViewOrdenes);
        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");
       // Toast.makeText(getApplicationContext(), "Usuario: " + usuario, Toast.LENGTH_LONG).show();
        datos = new ArrayList<>();
        ordenes = ServerSignal.listOrders(usuario);

        if(ordenes != null){
            for(int i=0; i<ordenes.size(); i++){
                Order current = ordenes.get(i);
                datos.add("Orden no. "+ current.getInvoice() + " Fecha: " + current.getCreatedAt());
            }

            adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
            ListaOrdenes.setAdapter(adaptador);
        }
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
                Intent intent  = new Intent(Ordenes.this, Home.class);
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