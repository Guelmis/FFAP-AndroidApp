package com.example.guelmis.ffap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.guelmis.ffap.models.LineItem;
import com.example.guelmis.ffap.models.Product;
import com.example.guelmis.ffap.signaling.InfoQuery;
import com.example.guelmis.ffap.signaling.ServerSignal;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Home extends ActionBarActivity {
    private String usuario;
    ArrayList<String> modelos = new ArrayList<String>();
    public static ArrayList<Product> listofprod = null;
    public static ArrayList<LineItem> cart = new ArrayList<LineItem>();
    Button chassis;
    Button piezas;
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
    class NetCheck extends AsyncTask<String,JSONArray,JSONArray>
    {
        private ProgressDialog nDialog;
        private JSONObject json1;
        private JSONArray jsonArr1;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        public JSONArray searchProducts(String qstring, String brand, String model, String year) {

            UserFunction userFunction = new UserFunction();
            JSONArray json = userFunction.search(qstring, brand, model, year);
            return json;
        }

        public JSONArray listProducts(String list) {

            UserFunction userFunction = new UserFunction();
            JSONArray json = userFunction.listObj(list);
            return json;
        }
        //public JSONObject populateSpinners(String list) {

        //    UserFunction userFunction = new UserFunction();
        //    JSONObject json = userFunction.spinnerinfo();
        //    return json;
        // }
        @Override
        protected JSONArray doInBackground(String... args){
            if(args.length != 0 ){
                jsonArr1 = searchProducts(args[0], args[1], args[2], args[3]);
            }
            else{
                jsonArr1 = listProducts("");
            }
            return jsonArr1;
        }
        @Override
        protected void onPostExecute(JSONArray th){

            if(th != null){
                //Toast.makeText(getApplicationContext(), jsonArr1.toString(), Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setTitle("FFAP Home");
        actionbar.setIcon(R.mipmap.ffap);
        busqueda = (EditText) findViewById(R.id.editTextPieza);
        datos = new ArrayList<String>();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);


        ArrayList<String> brands = new ArrayList<String>();
        brands.add("Marca");
        ArrayList<String> years = new ArrayList<String>();
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
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        adaptsp1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brands);
        adaptsp2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modelos);
        adaptsp3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
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
                adaptador.notifyDataSetChanged();
               // listofprod = Serve
                try {
                    JSONArray products = new NetCheck().execute(
                            pbusqueda, brand.equals("Marca")? "":brand,
                            model.equals("Modelo")? "":model,
                            year.equals("Año")? "":year)
                            .get();
                    if(listofprod !=null){
                        listofprod.clear();
                    }
                    listofprod = FillList(products);
                    for(int i=0; i<listofprod.size(); i++){
                        datos.add(listofprod.get(i).getTitle());
                    }
                    adaptador.notifyDataSetChanged();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(Home.this, ProductoTiendas.class);
                try {
                    if (listofprod != null) {
                       /* myIntent.putExtra("title", listofprod.get(position).getTitle());
                        myIntent.putExtra("image_url", listofprod.get(position).getImageurl());
                        myIntent.putExtra("brand", listofprod.get(position).getBrand());
                        myIntent.putExtra("model", listofprod.get(position).getModel());
                        myIntent.putExtra("year", listofprod.get(position).getYear().toString());*/
                        myIntent.putExtra("prod_id", listofprod.get(position).getId());
                        myIntent.putExtra("usuario",usuario);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                startActivity(myIntent);
            }
        });
    }

    private ArrayList<Product> FillList(JSONArray input){
        ArrayList<Product> ret = new ArrayList<Product>();
        for(int i=0; i<input.length(); i++){
            try {
                ret.add(new Product(input.getJSONObject(i).getJSONObject(UserFunction.product_tag).getString("title"),
                        input.getJSONObject(i).getJSONObject(UserFunction.brand_tag).getString("brand_name"),
                        input.getJSONObject(i).getJSONObject(UserFunction.model_tag).getString("model_name"),
                        input.getJSONObject(i).getString("image_url"),
                       // Double.parseDouble(input.getJSONObject(i).getJSONObject(UserFunction.product_tag).getString("price")),
                        Integer.parseInt(input.getJSONObject(i).getJSONObject(UserFunction.model_tag).getString("year")),
                        (input.getJSONObject(i).getJSONObject(UserFunction.product_tag).getString("id"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
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