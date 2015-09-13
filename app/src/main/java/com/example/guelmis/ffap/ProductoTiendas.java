package com.example.guelmis.ffap;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ProductoTiendas extends Activity {
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayList<String> datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producttienda);
        datos = new ArrayList<String>();
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        List.setAdapter(adaptador);
    }
}
