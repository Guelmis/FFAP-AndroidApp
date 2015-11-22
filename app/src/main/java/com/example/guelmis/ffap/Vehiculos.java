package com.example.guelmis.ffap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.guelmis.ffap.models.Vehicle;
import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;

import java.util.ArrayList;
import java.util.List;


public class Vehiculos extends ActionBarActivity {
    ListView ListaVehiculos;
    private String usuario;
    ActionBar actionbar;
    ArrayList<String> datos;
    ArrayAdapter<String> adaptador;
    ArrayList<Vehicle> vehiculos;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehiculos);
        setActionBar();
        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");
        datos = new ArrayList<>();

        ListaVehiculos = (ListView) findViewById(R.id.listViewVehiculos);

        vehiculos = ServerSignal.listVehicles(usuario);
        if(vehiculos != null){
            Vehicle current;
            for (int i=0; i<vehiculos.size(); i++){
                current= vehiculos.get(i);
                datos.add("Vehículo: " + current.getDescription() + "\nChassis: " + current.getVin());
            }
            adaptador = new ArrayAdapter<>(this, R.layout.listviewsmall, R.id.textView15, datos);
            ListaVehiculos.setAdapter(adaptador);
        }
        else{
            AlertDialog alertDialog1 = new AlertDialog.Builder(Vehiculos.this).create();
            alertDialog1.setTitle("Error");
            alertDialog1.setMessage("Error no identificado al buscar lista de vehiculos.");
            alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog1.show();
        }

        ListaVehiculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog alertDialog1 = new AlertDialog.Builder(Vehiculos.this).create();
                alertDialog1.setTitle("Eliminar Vehículo");
                alertDialog1.setMessage("Desea eliminar el vehículo seleccionado?");
                alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // eliminar
                                BasicResponse response = ServerSignal.delVehicle(usuario, vehiculos.get(position).getId());
                                if (response.success()) {
                                    datos.remove(position);
                                    vehiculos.remove(position);
                                    adaptador.notifyDataSetChanged();
                                } else {
                                    AlertDialog alertDialog2 = new AlertDialog.Builder(Vehiculos.this).create();
                                    alertDialog2.setTitle("Error al Eliminar Vehículo");
                                    alertDialog2.setMessage(response.getMessage());
                                    alertDialog2.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                            }
                        });
                alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog1.show();
            }
        });
    }

    public void setActionBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vehiculos Registrados");
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