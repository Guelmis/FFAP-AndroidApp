package com.example.guelmis.ffap;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;

import com.example.guelmis.ffap.signaling.BasicResponse;
import com.example.guelmis.ffap.signaling.ServerSignal;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    Button login;
    EditText email;
    EditText password;
    ActionBar bar;
    ProgressDialog pDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar();
        login = (Button) findViewById(R.id.botonlogin);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPass);

        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                if ((!email.getText().toString().equals("")) && (!password.getText().toString().equals(""))) {
                    // NetAsync(email.getText().toString(), password.getText().toString());
                    NetAsync(view);
                } else if ((!email.getText().toString().equals(""))) {
                    alertDialog.setTitle("No se pudo iniciar sesión");
                    alertDialog.setMessage("El campo de la contraseña está vacío, Por favor introduzca su contraseña");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                } else if ((!password.getText().toString().equals(""))) {
                    alertDialog.setTitle("No se pudo iniciar sesión");
                    alertDialog.setMessage("El campo de usuario está vacío, Por favor introduzca su nombre de usuario");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    alertDialog.setTitle("No se pudo iniciar sesión");
                    alertDialog.setMessage("Campo de usuario y contraseña vacíos, Por favor introduzca su nombre de usuario y contraseña");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
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
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            switch (item.getItemId()) {
                default:
                    return super.onOptionsItemSelected(item);
            }
    }

    private class NetCheck extends AsyncTask<String, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(MainActivity.this);
            nDialog.setTitle("Verificando la conexión a Internet");
            nDialog.setMessage("Por favor, espere..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean th) {

            if (th == true) {
                nDialog.dismiss();
                new ProcessLogin().execute();
            } else {
                nDialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("No se pudo iniciar sesión");
                alertDialog.setMessage("Error al conectarse a la red, Por favor verifique su conexión a Internet");
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

    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String correo, contrasena;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            email = (EditText) findViewById(R.id.editTextEmail);
            password = (EditText) findViewById(R.id.editTextPass);
            correo = email.getText().toString();
            contrasena = password.getText().toString();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle("Comunicandose con el servidor");
            pDialog.setMessage("Autenticando usuario");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

           // UserFunction userFunction = new UserFunction();
            JSONObject json = ServerSignal.Login(correo, contrasena);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(ServerSignal.KEY_SUCCESS) != null) {

                    String res = json.getString(ServerSignal.KEY_SUCCESS);

                    if (res.equals("true")) {

                        pDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        intent.putExtra("usuario", correo);
                        startActivity(intent);

                    } else {

                        pDialog.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("No se pudo iniciar sesión");
                        alertDialog.setMessage("Nombre de usuario y/o contraseña incorrectos, Por favor intentelo de nuevo");
<<<<<<< HEAD
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
=======
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
>>>>>>> origin/master
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void NetAsync(View view) {
        new NetCheck().execute();
    }

<<<<<<< HEAD
    public void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log In");
        getSupportActionBar().setIcon(R.mipmap.ffap);
    }

=======
>>>>>>> origin/master
}