package com.example.guelmis.ffap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.guelmis.ffap.models.Seller;
import com.example.guelmis.ffap.signaling.ServerSignal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

    public class MapaCliente extends FragmentActivity implements LocationProvider.LocationCallback {

        public static final String TAG = MapaCliente.class.getSimpleName();
        private LocationProvider mLocationProvider;
        private GoogleMap map;
        private static LatLng ubicacion = new LatLng(0,0);
        private double currentLatitude,currentLongitude;
        Button confirm;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent myIntent = getIntent();
            setContentView(R.layout.mapacliente);
            confirm = (Button) findViewById(R.id.btnlocation);
            mLocationProvider = new LocationProvider(this, this);
            try {
                initializeMap();
                map.setMyLocationEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Carrito.ubicacion = ubicacion;
                    finish();
                }
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            initializeMap();
            mLocationProvider.connect();
            mLocationProvider.connect();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mLocationProvider.disconnect();
        }

        private void initializeMap() {
            if (map == null) {
                map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                        R.id.map)).getMap();
                // check if map is created successfully or not
                if (map == null) {
                    Toast.makeText(getApplicationContext(),
                            "Ha ocurrido un error, no se pudo cargar su ruta", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
        public void area() {

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ubicacion.latitude, ubicacion.longitude), 16f));
        }

        public void handleNewLocation(Location location) {
            Log.d(TAG, location.toString());

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            ubicacion = new LatLng(currentLatitude, currentLongitude);
            map.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación Actual"));
            area();
        }
    }


