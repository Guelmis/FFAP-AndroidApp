package com.example.guelmis.ffap;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Piezas extends ActionBarActivity {
    //private String title;
    //private String description;
    //private String imageurl;
    //private Double price;
    private String usuario;
    private Button tienda;
    private Button carrito;
    ActionBar actionbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piezas);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("FFAP Productos");
        actionbar.setIcon(R.mipmap.ffap);
        tienda = (Button) findViewById(R.id.btnTienda);
        carrito = (Button) findViewById(R.id.btncarrito);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory()
                .cacheOnDisc().resetViewBeforeLoading()
                .build();

        Intent myIntent = getIntent();
        usuario = myIntent.getStringExtra("usuario");
        final LineItem newprod = new LineItem(new Product(myIntent.getStringExtra("title"),
                myIntent.getStringExtra("brand"), myIntent.getStringExtra("model"), myIntent.getStringExtra("image_url"),
                Double.parseDouble(myIntent.getStringExtra("price")), Integer.parseInt(myIntent.getStringExtra("year"))));
        TextView tv = (TextView)findViewById(R.id.textView1);
        TextView tv2 = (TextView)findViewById(R.id.textView2);
        TextView tv3 = (TextView)findViewById(R.id.textView3);
        tv.setText(""+ newprod.getTitle());
        tv2.setText("Descripcion: " + newprod.getTitle());
        tv3.setText("Precio: " + newprod.getPrice());

        ImageView iv1 = (ImageView) findViewById(R.id.imageV1);
        imageLoader.displayImage(newprod.getImageurl(), iv1, options);
        tienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Piezas.this, Tienda.class);
                myIntent.putExtra("usuario", usuario);
                startActivity(myIntent);
            }
        });
	//Esto necesita cambios
        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Piezas.this, Carrito.class);
                myIntent.putExtra("usuario", usuario);
                if(!Home.cart.contains(newprod)){
                    Home.cart.add(newprod);
                }
                else{
                    Home.cart.get(Home.cart.indexOf(newprod)).addOne();
                }
                //myIntent.putExtra("price", price);
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
                startActivity(new Intent(this, Carrito.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
