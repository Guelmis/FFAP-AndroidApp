package com.example.guelmis.ffap;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.guelmis.ffap.models.Product;
import com.example.guelmis.ffap.models.Stock;
import com.example.guelmis.ffap.signaling.ServerSignal;

import java.util.ArrayList;

public class ProductoTiendas extends Activity {
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayList<String> datos;
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producttienda);
        datos = new ArrayList<>();
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        List = (ListView) findViewById(R.id.listStoreProduct);
        List.setAdapter(adaptador);

        Intent thisIntent = getIntent();
        usuario = thisIntent.getStringExtra("usuario");

        Product productInfo = ServerSignal.ShowProduct(thisIntent.getStringExtra("prod_id"));

        for(int i=0; i<productInfo.getStocklist().size(); i++){
            datos.add(productInfo.getStocklist().get(i).getSellerName() + ", $" + productInfo.getStocklist().get(i).getPrice());
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

}
