package com.example.guelmis.ffap;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guelmis.ffap.models.Vehicle;
import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;

public class Chassis extends ActionBarActivity {
    Button vinregister;
    ActionBar actionbar;
    private String usuario;
    EditText vin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_vehiculos);
        vinregister = (Button) findViewById(R.id.btnvinregister);
        vin = (EditText) findViewById(R.id.editTextVin);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("FFAP Registrar Vehículo");
        actionbar.setIcon(R.mipmap.ffap);
        Intent intent = getIntent();
        usuario = intent.getStringExtra("usuario");
        vinregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(Chassis.this).create();

                AlertDialog alertDialog1 = new AlertDialog.Builder(Chassis.this).create();
                alertDialog1.setTitle("Vehiculo no encontrado");
                alertDialog1.setMessage("El chasis introducido no ha arrojado resultados.");
                alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                final AlertDialog alertDialog2 = new AlertDialog.Builder(Chassis.this).create();
                alertDialog1.setTitle("Error al registrar vehiculo.");
                alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                final Vehicle ref = ServerSignal.edmundQuery(vin.getText().toString());
                if(ref == null){
                    alertDialog1.show();
                }
                else{
                    alertDialog.setTitle("Registro de vehículo");
                    alertDialog.setMessage("El vehículo que desea registrar es " + ref.getDescription() + "?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "SI",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    //api register vehicle
                                    BasicResponse response = ServerSignal.regVehicle(usuario, ref);
                                    if (!response.success()) {
                                        alertDialog2.setMessage(response.getMessage());
                                        alertDialog2.show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Vehiculo registrado.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
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