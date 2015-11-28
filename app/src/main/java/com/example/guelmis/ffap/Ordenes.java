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
import android.widget.TextView;

import com.example.guelmis.ffap.models.LineItem;
import com.example.guelmis.ffap.models.Order;
import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class Ordenes extends ActionBarActivity {
    ListView ListaOrdenes;
    private String usuario;
    ActionBar actionbar;
    ArrayList<String> datos;
    ArrayAdapter<String> adaptador;
    ArrayList<Order> ordenes;
    private Toolbar toolbar;
    private TextView textViewDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordenes);
        setActionBar();
        ListaOrdenes = (ListView) findViewById(R.id.listViewOrdenes);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewDate.setText(getCurrentDate());
        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");
        datos = new ArrayList<>();
        ordenes = ServerSignal.listOrders(usuario);

        if(ordenes != null){
            for(int i=0; i<ordenes.size(); i++){
                Order current = ordenes.get(i);
                String dato = "Orden no: "+ current.getInvoice() + "\n" +"Fecha: " + current.getCreatedAt()
                        + "\n"+ "Hora: " + current.getTime() + "\n";
                for(Iterator<LineItem> iterator = current.getLineItems().iterator(); iterator.hasNext();){
                    LineItem item = iterator.next();
                    dato += item.getQuantity()+ " x " + item.getTitle()+ "\n";
                }
                datos.add(dato);
            }

            adaptador = new ArrayAdapter<>(this, R.layout.listviewcolor, R.id.textView14, datos);
            ListaOrdenes.setAdapter(adaptador);
        }

        ListaOrdenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Order selected;
                if (ordenes.get(position).isConfirmed()){
                    return;
                }
                else{
                    selected = ServerSignal.showOrder((ordenes.get(position).getId()));
                    if (selected.isConfirmed()) {
                        ordenes.get(position).setConfirmed(true);
                        return;
                    }
                }

                if (selected.wasDelivered()) {
                    confirm(selected);
                } else {
                    showEta(selected);
                }
            }
        });
    }

    private void confirm(final Order input){
        AlertDialog alertDialog = new AlertDialog.Builder(Ordenes.this).create();
        alertDialog.setTitle("Confirmar orden");
        alertDialog.setMessage("¿Su pedido fue entregado satisfactoriamente?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sendConfirmation(input.getId(), false);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "SI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sendConfirmation(input.getId(), true);
                       // input.setConfirmed(true);
                    }
                });
        alertDialog.show();
    }

    private void sendConfirmation(int order_id, boolean value){
        final AlertDialog result = new AlertDialog.Builder(Ordenes.this).create();
        result.setTitle("Confirmar orden");
        result.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        BasicResponse res = ServerSignal.confirmOrder(order_id, value);
        if(res.success()){
            result.setMessage("Gracias por notificarnos.");
        }
        else {
            result.setTitle("Error");
            result.setMessage(res.getMessage());
        }
        result.show();
    }

    private void showEta(Order input){
        BasicResponse eta = ServerSignal.deliveryeta(input.getDelivery_id());
        AlertDialog alertDialog = new AlertDialog.Builder(Ordenes.this).create();
        alertDialog.setTitle("Estatus de la orden");
        if(eta.getStatus().equals("OK")){
            String esttime = eta.getMessage().split("\\.")[0];
            alertDialog.setMessage("Su orden se entregará en aproximadamente " + esttime + " minutos");
        }
        else{
            alertDialog.setMessage(eta.getMessage());
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private String getCurrentDate()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public void setActionBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ordenes");
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