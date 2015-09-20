package com.example.guelmis.ffap;

/**
 * Created by Guelmis on 7/2/2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListaResenas extends Activity {
    ListView List;
    ArrayAdapter<String> adaptador;
    ArrayList<String> datos;

    class ShowSeller extends AsyncTask<String,JSONObject,JSONObject>
    {
        private ProgressDialog nDialog;
        private JSONObject json1;
        private JSONArray jsonArr1;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        public JSONObject getSellerInfo(String id) {

            UserFunction userFunction = new UserFunction();
            JSONObject json = userFunction.showseller(id);
            return json;
        }

        @Override
        protected JSONObject doInBackground(String... args){
            json1 = getSellerInfo(args[0]);
            return json1;
        }
        @Override
        protected void onPostExecute(JSONObject th){

            if(th != null){
            }
            else{
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resena_tienda);
        datos = new ArrayList<String>();
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        List = (ListView) findViewById(R.id.listViewComments);
        List.setAdapter(adaptador);

        Intent myIntent = getIntent();
        JSONObject sellerJSON = null;
        int sellerid = myIntent.getIntExtra("seller_id", 0);
        ArrayList<Comment> resenas = null;

        try {
            sellerJSON = new ShowSeller().execute(Integer.toString(sellerid)).get();
            resenas = FillList(sellerJSON);
            for(int i=0; i<resenas.size(); i++){
                datos.add(resenas.get(i).getTitle() + " -- " + resenas.get(i).getUsername());
            }
            adaptador.notifyDataSetChanged();
            //....
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<Comment> verresenas = resenas;

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(ListaResenas.this, VerResenas.class);
                myIntent.putExtra("title", verresenas.get(position).getTitle());
                myIntent.putExtra("username", verresenas.get(position).getUsername());
                myIntent.putExtra("body", verresenas.get(position).getBody());
                startActivity(myIntent);
            }
        });
    }

    private ArrayList<Comment> FillList(JSONObject input) throws JSONException {
        ArrayList<Comment> arr = new ArrayList<Comment>();
        JSONArray comments = input.getJSONArray("comments");
        for(int i=0; i<comments.length(); i++){
            arr.add(new Comment(comments.getJSONObject(i).getJSONObject("comment").getString("title"),
                    comments.getJSONObject(i).getJSONObject("comment").getString("body"),
                    comments.getJSONObject(i).getJSONObject("username").getString("username")));
        }
        return arr;
    }
}