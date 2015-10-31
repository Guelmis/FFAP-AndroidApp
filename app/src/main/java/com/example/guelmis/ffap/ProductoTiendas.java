package com.example.guelmis.ffap;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.guelmis.ffap.models.Product;
import com.example.guelmis.ffap.models.Stock;
import com.example.guelmis.ffap.signaling.ServerSignal;

import java.util.ArrayList;

public class ProductoTiendas extends ActionBarActivity {
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayList<String> datos;
    String usuario;
    ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producttienda);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("FFAP ProductStores");
        actionbar.setIcon(R.mipmap.ffap);
        datos = new ArrayList<>();
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        List = (ListView) findViewById(R.id.listStoreProduct);
        List.setAdapter(adaptador);

        Intent thisIntent = getIntent();
        usuario = thisIntent.getStringExtra("usuario");

        Product productInfo = ServerSignal.ShowProduct(thisIntent.getStringExtra("prod_id"));

        for(int i=0; i<productInfo.getStocklist().size(); i++){
            datos.add(productInfo.getStocklist().get(i).getSellerName() + "\n" +
                    "Precio: " + productInfo.getStocklist().get(i).getPrice()+ "$");
        }

        final Product product = productInfo;
        final ArrayList<Stock> stocks = productInfo.getStocklist();

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(ProductoTiendas.this, Piezas.class);

                myIntent.putExtra("title", product.getTitle());
                myIntent.putExtra("image_url", product.getImageurl());
                myIntent.putExtra("brand", product.getBrand());
                myIntent.putExtra("model", product.getModel());
                myIntent.putExtra("year", product.getYear().toString());
                myIntent.putExtra("id", product.getId());
                myIntent.putExtra("usuario",usuario);
                myIntent.putExtra("stock_id", stocks.get(position).getId());
                myIntent.putExtra("price", stocks.get(position).getPrice());
                myIntent.putExtra("quantity", stocks.get(position).getQuantity());
                myIntent.putExtra("seller_id", stocks.get(position).getSellerId());
                myIntent.putExtra("seller_name", stocks.get(position).getSellerName());

                startActivity(myIntent);
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