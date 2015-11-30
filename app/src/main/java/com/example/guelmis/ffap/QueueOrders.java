package com.example.guelmis.ffap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guelmis.ffap.models.LineItem;
import com.example.guelmis.ffap.models.Order;
import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;
import com.google.android.gms.maps.model.LatLng;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by Guelmis on 11/29/2015.
 */
public class QueueOrders extends ActionBarActivity {

    ListView ordenesencola;
    private String usuario;
    ActionBar actionbar;
    ArrayList<String> datos;
    private static int selectedposition;
    ArrayAdapter<String> adaptador;
    ArrayList<Order> ordenes;
    private Toolbar toolbar;
    private TextView fecha;
    private Button ordersqueue;
    private static final int REQUEST_CODE_PAYMENT = 1;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AYwh-1ZAG-hgLPdiAdDGXNpQhLPZjQ7NdsQVpIVC1EIi2z9zF-eqPFI2oi2ls9oSh3mJYzqDWj21WSru";
    // when testing in sandbox, this is likely the -facilitator email address.
    private static final String CONFIG_RECEIVER_EMAIL = "ml.vizard-facilitator@email.com";

    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(CONFIG_CLIENT_ID)
            .acceptCreditCards(true)
// The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Code_Crash")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full"))
            .merchantUserAgreementUri(
                    Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full"));

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordersqueue);
        setActionBar();
        selectedposition = 0;
        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");
        datos = new ArrayList<>();
        ordersqueue = (Button) findViewById(R.id.btnqueueorders);
        fecha = (TextView) findViewById(R.id.textViewFecha);
        fecha.setText(getCurrentDate());
        final Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent);
        ordenesencola = (ListView) findViewById(R.id.listordersqueue);

        ordenes = ServerSignal.listOrders(usuario, "true");
        fillList();
        adaptador = new ArrayAdapter<>(this, R.layout.listviewcolor, R.id.textView14, datos);
        ordenesencola.setAdapter(adaptador);
        ordersqueue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        ordenesencola.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedposition=position;
                launchPayPalPayment(ordenes.get(position));
            }
        });
    }

    private void refresh(){
        datos.clear();
        ordenes = ServerSignal.listOrders(usuario, "true");
        fillList();
        adaptador.notifyDataSetChanged();
    }

    private void fillList(){
        if(ordenes != null){
            for(int i=0; i<ordenes.size(); i++){
                Order current = ordenes.get(i);
              //  if(current.isProcessed() && !current.wasDelivered() && current.getStatus().equals("Incomplete")){
                    String dato = "Orden no: "+ current.getInvoice() + "\n" +"Fecha: " + current.getCreatedAt()
                            + "\n"+ "Hora: " + current.getTime() + "\n";
                    for(Iterator<LineItem> iterator = current.getLineItems().iterator(); iterator.hasNext();){
                        LineItem item = iterator.next();
                        dato += item.getQuantity()+ " x " + item.getTitle()+ "\n";
                    }
                    datos.add(dato);
             //   }
            }
        }
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
        getSupportActionBar().setTitle("Ordenes en Espera");
    }
    private void launchPayPalPayment(Order orden) {

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(0.01),"USD",  orden.getInvoice() + " " + usuario,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(QueueOrders.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                BasicResponse res = ServerSignal.postHook(ordenes.get(selectedposition).getInvoice());
                refresh();
            }

            else if (resultCode == Activity.RESULT_CANCELED) {

            }

            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

                //Toast.makeText(getApplicationContext(), "Payment failed , Try again ", Toast.LENGTH_LONG).show();
                AlertDialog alertDialog = new AlertDialog.Builder(QueueOrders.this).create();
                alertDialog.setTitle("No se pudo realizar la compra");
                alertDialog.setMessage("El carrito esta vacío");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
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
