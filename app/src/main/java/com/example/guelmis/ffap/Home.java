package com.example.guelmis.ffap;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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
import com.example.guelmis.ffap.models.Vehicle;
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
    private ArrayList<Vehicle> vehiculos;
  //  Button piezas;
    Button buscar;
    Spinner spinner1, spinner2, spinner3, spinner4;
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayAdapter<String> adaptsp1;
    ArrayAdapter<String> adaptsp2;
    ArrayAdapter<String> adaptsp3;
    ArrayAdapter<String> adaptsp4;
    ArrayList<String> brands;
    ArrayList<String> datos;
    HashMap<String, ArrayList<String>> modelmap;
    ActionBar actionbar;
    EditText busqueda;
    EditText chassissearch;
    Button chassissearcher;
    Button chassisregistered;
    ArrayList<String> dvehiculos;
    Vehicle currentVehicle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setActionBar();
        busqueda = (EditText) findViewById(R.id.editTextPieza);
        datos = new ArrayList<>();
        dvehiculos = new ArrayList<>();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        brands = new ArrayList<>();
        brands.add("Marca");
        ArrayList<String> years = new ArrayList<>();
        years.add("Año");

        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");
        TextView username = (TextView) findViewById(R.id.textViewUser);
        username.setText("Bienvenido " + usuario);

        InfoQuery spinnerinfo = ServerSignal.spinnersQuery();
        brands.addAll(spinnerinfo.getBrands());
        years.addAll(spinnerinfo.getYears());

        //esto es para evitar nullpointerexception en el spinner 1
        //el elemento en la posicion 1 del spinner1 es "Marca" pero el hash map no tiene ese key.
        spinnerinfo.getModels().put("Marca", new ArrayList<String>());

        modelmap = spinnerinfo.getModels();
        modelos.add("Modelo");
        chassisregistered = (Button) findViewById(R.id.btnvinregistered);
        List = (ListView) findViewById(R.id.listProducts);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        adaptador = new ArrayAdapter<>(this, R.layout.listviewsmall, R.id.textView15, datos);
        adaptsp1 = new ArrayAdapter<>(this, R.layout.spinnercustom, brands);
        adaptsp2 = new ArrayAdapter<>(this, R.layout.spinnercustom, modelos);
        adaptsp3 = new ArrayAdapter<>(this, R.layout.spinnercustom, years);
        adaptsp4 = new ArrayAdapter<>(this, R.layout.spinnercustom, dvehiculos);
        spinner1.setAdapter(adaptsp1);
        spinner2.setAdapter(adaptsp2);
        spinner3.setAdapter(adaptsp3);
        spinner4.setAdapter(adaptsp4);
        spinner4.setPrompt("Vehiculo");
        refreshVehicles();
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

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    currentVehicle = null;
                }
                else{
                    currentVehicle = vehiculos.get(position-1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentVehicle = null;
            }
        });

        buscar = (Button) findViewById(R.id.btnbuscar);
        chassissearch = (EditText) findViewById(R.id.editTextChassis);
        chassissearcher = (Button) findViewById(R.id.btnchassisbusc);

        chassisregistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentVehicle == null){
                    AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                    alertDialog.setTitle("No se seleccionó ningún vehículo");
                    alertDialog.setMessage("Por favor seleccione un vehículo para la búsqueda");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    listofprod = ServerSignal.searchProducts(busqueda.getText().toString(), currentVehicle.getBrand(),
                            currentVehicle.getModel(), currentVehicle.getYear());
                    refreshList();
                }
                if (listofprod.size() == 0) {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(Home.this).create();
                    alertDialog1.setTitle("Producto no encontrado");
                    alertDialog1.setMessage("No se han encontrado productos para " + currentVehicle.getBrand() + " " +
                            currentVehicle.getModel() + " " + currentVehicle.getYear());
                    alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog1.show();
                    refreshList();
                }
                else {
                    refreshList();
                }
            }
        });
        chassissearcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle("Vehículo no encontrado");
                alertDialog.setMessage("El chassis introducido no ha arrojado resultados");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog1 = new AlertDialog.Builder(Home.this).create();

                Vehicle ref = ServerSignal.edmundQuery(chassissearch.getText().toString());
                if(ref == null){
                    alertDialog.show();
                }
                else{
                    alertDialog1.setTitle("Vehículo encontrado");
                    alertDialog1.setMessage("Su vehículo es: " + ref.getBrand() + " " + ref.getModel() + " " + ref.getYear());
                    alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog1.show();
                    listofprod = ServerSignal.searchProducts(busqueda.getText().toString(), ref.getBrand(), ref.getModel(), ref.getYear());
                    refreshList();
                }
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pbusqueda = busqueda.getText().toString();
                String brand = spinner1.getSelectedItem().toString();
                String model = spinner2.getSelectedItem().toString();
                String year = spinner3.getSelectedItem().toString();
                listofprod = ServerSignal.searchProducts(
                        pbusqueda, brand.equals("Marca")? "":brand,
                        model.equals("Modelo")? "":model,
                        year.equals("Año")? "":year);
                if (listofprod.size() == 0) {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(Home.this).create();
                    alertDialog1.setTitle("Producto no encontrado");
                    alertDialog1.setMessage("No se han encontrado productos con sus especificaciones");
                    alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog1.show();
                    refreshList();
                }
                else {
                    refreshList();
                }
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
                        myIntent.putExtra("producto", listofprod.get(position).getTitle());
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                startActivity(myIntent);
            }
        });
    }

    public void setActionBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setIcon(R.mipmap.ffap);
    }

    @Override
    public void onResume(){
        super.onResume();
        InfoQuery spinnerinfo = ServerSignal.spinnersQuery();
        brands.clear();
        brands.add("Marca");
        brands.addAll(spinnerinfo.getBrands());
        adaptsp1.notifyDataSetChanged();
        spinnerinfo.getModels().put("Marca", new ArrayList<String>());
        modelmap = spinnerinfo.getModels();
        if(listofprod != null){
            listofprod.clear();
            refreshList();
        }
        refreshVehicles();
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
            case R.id.id_logout:
                finishAffinity();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshList(){
        datos.clear();
        for(int i=0; i<listofprod.size(); i++){
            datos.add(listofprod.get(i).getTitle());
        }
        adaptador.notifyDataSetChanged();
    }
    private void refreshVehicles() {
        vehiculos = ServerSignal.listVehicles(usuario);
        if(vehiculos == null){
            vehiculos = new ArrayList<>();
        }
        dvehiculos.clear();
        dvehiculos.add("Mis Vehiculos");
        for(int i=0; i<vehiculos.size(); i++){
            dvehiculos.add(vehiculos.get(i).getDescription());
        }
        adaptsp4.notifyDataSetChanged();
    }
}