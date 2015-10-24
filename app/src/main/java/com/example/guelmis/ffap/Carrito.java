package com.example.guelmis.ffap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
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
import com.example.guelmis.ffap.models.Product;
import com.example.guelmis.ffap.models.Stock;
import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Carrito extends ActionBarActivity {
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayList<String> datos;
    private String usuario;
    ActionBar actionbar;
    Button eliminar;
    Double itbis;
    Double total;
    TextView subtotal;
    TextView ITBIS;
    TextView Total;
    Button pagar;
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


    private double calcTotal(ArrayList<LineItem> cart) {
        double res = 0;
        for (int i = 0; i < cart.size(); i++) {
            res += (cart.get(i).getSelectedStock().getPrice() * cart.get(i).getQuantity());
        }
        return res;
    }

    private void actualizaPrecios() {
        subtotal.setText( Double.toString(calcTotal(Home.cart)) + "$");
        itbis = (calcTotal(Home.cart) * 0.18);
        ITBIS.setText(itbis + "$".toString());
        total = (calcTotal(Home.cart) + itbis);
        Total.setText(total + "$".toString());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito);
        eliminar = (Button) findViewById(R.id.btnclear);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("FFAP Carrito");
        actionbar.setIcon(R.mipmap.ffap);
        subtotal = (TextView) findViewById(R.id.carrito_subtotal);
        ITBIS = (TextView) findViewById(R.id.carrito_itbis);
        Total = (TextView) findViewById(R.id.carrito_total);
        pagar = (Button) findViewById(R.id.carrito_pago);

        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");
        actualizaPrecios();
        List = (ListView) findViewById(R.id.listofprod);
        datos = new ArrayList<>();
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent);

        Home.cart = ServerSignal.ShowCart(usuario);
        actualizaPrecios();

        for (int i = 0; i < Home.cart.size(); i++) {
            datos.add(Home.cart.get(i).getQuantity() + " x " + Home.cart.get(i).getTitle());
        }

        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
        List.setAdapter(adaptador);
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(Carrito.this, ItemCart.class);
                myIntent.putExtra("position", position);
                myIntent.putExtra("usuario", usuario);
                startActivity(myIntent);
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.cart.clear();
                actualizaPrecios();
                datos.clear();
                adaptador.notifyDataSetChanged();
            }
        });

    pagar.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
            BasicResponse response = ServerSignal.checkout(usuario);
            if(response.success()) {
                launchPayPalPayment();
            }
            else{
                Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    });
}
    private void launchPayPalPayment() {

        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(0.01),"USD", usuario,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(Carrito.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(getApplicationContext(), "Payment done succesfully ", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Payment failed , Try again ", Toast.LENGTH_LONG).show();

            }

            else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(), "Payment Canceled , Try again ", Toast.LENGTH_LONG).show();


            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

                Toast.makeText(getApplicationContext(), "Payment failed , Try again ", Toast.LENGTH_LONG).show();
                AlertDialog alertDialog = new AlertDialog.Builder(Carrito.this).create();
                alertDialog.setTitle("No se pudo realizar la compra");
                alertDialog.setMessage("El carrito esta vacio");
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
    public void onResume(){
        super.onResume();
        datos.clear();
        Home.cart = ServerSignal.ShowCart(usuario);
        for(int i =0; i<Home.cart.size(); i++){
            datos.add(Home.cart.get(i).getQuantity() + " x " + Home.cart.get(i).getTitle() );
        }
        adaptador.notifyDataSetChanged();
        actualizaPrecios();
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
                Intent intent  = new Intent(Carrito.this, Home.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                return true;
            case R.id.id_carrito:
                startActivity(new Intent(Carrito.this, Carrito.class));
                return true;
        default:
        return super.onOptionsItemSelected(item);
        }
    }
}