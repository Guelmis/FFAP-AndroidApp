package com.example.guelmis.ffap;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guelmis.ffap.models.LineItem;
import com.example.guelmis.ffap.models.Product;
import com.example.guelmis.ffap.models.Stock;
import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Piezas extends ActionBarActivity {
    private String usuario;
    private Button tienda;
    private Button carrito;
    ActionBar actionbar;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piezas);
        setActionBar();
        tienda = (Button) findViewById(R.id.btnTienda);
        carrito = (Button) findViewById(R.id.btncarrito);
        final ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory()
                .cacheOnDisc().resetViewBeforeLoading()
                .build();

        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");
        final LineItem newprod = new LineItem(new Product(myIntent.getStringExtra("title"),
                myIntent.getStringExtra("brand"), myIntent.getStringExtra("model"), myIntent.getStringExtra("image_url"),
                 Integer.parseInt(myIntent.getStringExtra("year")),
                myIntent.getStringExtra("id")),
                new Stock(myIntent.getIntExtra("stock_id", 0),
                    myIntent.getDoubleExtra("price", 0.00), myIntent.getIntExtra("quantity", 0),
                    myIntent.getStringExtra("seller_name"), myIntent.getIntExtra("seller_id", 0))
        );

        TextView tv = (TextView)findViewById(R.id.textView1);
        TextView tv2 = (TextView)findViewById(R.id.textView2);
        TextView tv3 = (TextView)findViewById(R.id.textView3);
        tv.setText(""+ newprod.getTitle());
        tv2.setText(newprod.getTitle());
        tv3.setText("$" + Math.round(newprod.getSelectedStock().getPrice()));

        ImageView iv1 = (ImageView) findViewById(R.id.imageV1);
        imageLoader.displayImage(newprod.getImageurl(), iv1, options);
        tienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Piezas.this, Tienda.class);
                myIntent.putExtra("usuario", usuario);
                myIntent.putExtra("seller_id", newprod.getSelectedStock().getSellerId());
                startActivity(myIntent);
            }
        });

        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Piezas.this, Carrito.class);
                myIntent.putExtra("usuario", usuario);
                myIntent.putExtra("image_url", newprod.getImageurl());
                BasicResponse response = ServerSignal.AddToCart(usuario, Integer.toString(newprod.getSelectedStock().getId()));
                if(response.success()){
                    startActivity(myIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void setActionBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Productos");
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
