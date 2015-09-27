package com.example.guelmis.ffap;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.guelmis.ffap.models.LineItem;
import com.example.guelmis.ffap.models.Product;
import com.example.guelmis.ffap.signaling.InfoQuery;
import com.example.guelmis.ffap.signaling.ServerSignal;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends ActionBarActivity {
    private String usuario;
    ArrayList<String> modelos = new ArrayList<>();
    public static ArrayList<Product> listofprod = null;
    public static ArrayList<LineItem> cart = new ArrayList<>();
    Button chassis;
  //  Button piezas;
    Button buscar;
    Spinner spinner1, spinner2, spinner3;
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayAdapter<String> adaptsp1;
    ArrayAdapter<String> adaptsp2;
    ArrayAdapter<String> adaptsp3;
    ArrayList<String> datos;
    ActionBar actionbar;
    EditText busqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayShowHomeEnabled(true);
        }
        actionbar.setTitle("FFAP Home");
        actionbar.setIcon(R.mipmap.ffap);
        busqueda = (EditText) findViewById(R.id.editTextPieza);
        datos = new ArrayList<>();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);


        ArrayList<String> brands = new ArrayList<>();
        brands.add("Marca");
        ArrayList<String> years = new ArrayList<>();
        years.add("Año");
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");
        TextView username = (TextView) findViewById(R.id.textViewUser);
        username.setText("Bienvenido " +usuario);

        InfoQuery spinnerinfo = ServerSignal.spinnersQuery();
        brands.addAll(spinnerinfo.getBrands());
        years.addAll(spinnerinfo.getYears());

        //esto es para evitar nullpointerexception en el spinner 1
        //el elemento en la posicion 1 del spinner1 es "Marca" pero el hash map no tiene ese key.
        spinnerinfo.getModels().put("Marca", new ArrayList<String>());

        final HashMap<String, ArrayList<String>> modelmap = spinnerinfo.getModels();
        modelos.add("Modelo");
        List = (ListView) findViewById(R.id.listProducts);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        adaptsp1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brands);
        adaptsp2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelos);
        adaptsp3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        spinner1.setAdapter(adaptsp1);
        spinner2.setAdapter(adaptsp2);
        spinner3.setAdapter(adaptsp3);
        List.setAdapter(adaptador);

        //Este spinner se actualiza dependiendo de la marca seleccionada, a partir del hashmap dado.
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelos.clear();
                modelos.add("Modelo");
                for (int i = 0; i < modelmap.get(parent.getItemAtPosition(position).toString()).size(); i++) {
                    modelos.add(modelmap.get(parent.getItemAtPosition(position).toString()).get(i));
                }
                adaptsp2.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chassis = (Button) findViewById(R.id.btnchassis);
        buscar = (Button) findViewById(R.id.btnbuscar);
        chassis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Home.this, Chassis.class);
                startActivity(myIntent);
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pbusqueda = busqueda.getText().toString();
                String brand = spinner1.getSelectedItem().toString();
                String model = spinner2.getSelectedItem().toString();
                String year = spinner3.getSelectedItem().toString();
                datos.clear();
                listofprod = ServerSignal.searchProducts(
                        pbusqueda, brand.equals("Marca")? "":brand,
                        model.equals("Modelo")? "":model,
                        year.equals("Año")? "":year);

                for(int i=0; i<listofprod.size(); i++){
                    datos.add(listofprod.get(i).getTitle());
                }
                adaptador.notifyDataSetChanged();
            }
        });
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(Home.this, ProductoTiendas.class);
                try {
                    if (listofprod != null) {
                        myIntent.putExtra("prod_id", listofprod.get(position).getId());
                        myIntent.putExtra("usuario", usuario);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
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
                Intent myIntent = new Intent(this, Carrito.class);
                myIntent.putExtra("usuario", usuario);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}