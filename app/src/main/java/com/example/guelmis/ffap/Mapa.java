package com.example.guelmis.ffap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.guelmis.ffap.models.Seller;
import com.example.guelmis.ffap.signaling.ServerSignal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Mapa extends ActionBarActivity implements LocationProvider.LocationCallback {

    public static final String TAG = Mapa.class.getSimpleName();
    private LocationProvider mLocationProvider;
    private LatLng tienda;
    private GoogleMap map;
    private Seller infoTienda;
    LatLng marcadortienda;
    private static LatLng ubicacion = new LatLng(0,0);
    private double currentLatitude,currentLongitude;
    private Polyline newPolyline;
    private LatLngBounds latlngBounds;
    private String sellername;
    private String selleradd;
    TextView selleraddress;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        Intent myIntent = getIntent();
        setActionBar();
        infoTienda = ServerSignal.ShowSeller(Integer.toString(myIntent.getIntExtra("seller_id", 0)));
        sellername = myIntent.getStringExtra("seller_name");
        selleradd = myIntent.getStringExtra("seller_address");
        tienda = infoTienda.getLocation();
        marcadortienda = tienda;
        selleraddress = (TextView) findViewById(R.id.textViewSellerAdd);
        mLocationProvider = new LocationProvider(this, this);
        try
        {
            initializeMap();
            map.setMyLocationEnabled(true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setActionBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ubicación Tiendas");
        getSupportActionBar().setIcon(R.mipmap.ffap);
    }

    @Override
    protected void onResume() {
        super.onResume();
       initializeMap();
        mLocationProvider.connect();
        latlngBounds = createLatLngBoundsObject(ubicacion, tienda);
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    public void handleGetDirectionsResult(ArrayList directionPoints) {
        Polyline newPolyline;
        GoogleMap mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        PolylineOptions rectLine = new PolylineOptions().width(4).color(Color.BLUE);
        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add((LatLng) directionPoints.get(i));
        }
        newPolyline = mMap.addPolyline(rectLine);
        latlngBounds = createLatLngBoundsObject(ubicacion, tienda);
    }

    private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation){
        if (firstLocation != null && secondLocation != null)
        {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(firstLocation).include(secondLocation);

            return builder.build();
        }
        return null;
    }

    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
        asyncTask.execute(map);
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

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ubicacion.latitude, ubicacion.longitude), 14f));
        selleraddress.setText("Dirección Destino: " + selleradd);
    }

    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        ubicacion = new LatLng(currentLatitude, currentLongitude);
        map.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación Actual").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        map.addMarker(new MarkerOptions().position(marcadortienda).title(sellername));
        CrearRuta();
        area();
    }
    public void CrearRuta() {
        findDirections(ubicacion.latitude, ubicacion.longitude, tienda.latitude, tienda.longitude, GMapV2Direction.MODE_DRIVING);
    }
}