package com.pruebas.ades.renobar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pruebas.ades.renobar.Model.Restaurante;

public class maps_renobar extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mapa;
    private double longitud;
    private double latitud;
    private int maxResultados;
    private LatLng locRestaurantes;
    private ArrayList <Restaurante> direRestaurante;
    private Location locRestaurante;
    private Location locMiUbicacion;
    private ArrayList <String> dire;
    private ActionBar actionBar;

//    Toolbar miToolbar = findViewById ( R.id. toolbar_clean);


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maps_renobar );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ( SupportMapFragment ) getSupportFragmentManager ()
                .findFragmentById ( R.id.map );
        mapFragment.getMapAsync ( this );


        Toolbar miToolbar = findViewById ( R.id.toolbar_clean );
        setSupportActionBar ( miToolbar );

        actionBar = getSupportActionBar ();


        // Muestra una flecha para volver a la actividad principal
        actionBar.setDisplayHomeAsUpEnabled ( true );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
            getSupportActionBar ().setHomeAsUpIndicator ( getResources ().getDrawable ( R.drawable.ic_arrow_back_black_24dp, null ) );
        else
            getSupportActionBar ().setHomeAsUpIndicator ( getResources ().getDrawable ( R.drawable.ic_arrow_back_black_24dp, null ) );


        getWindow ().addFlags ( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
        getWindow ().setStatusBarColor ( Color.parseColor ( "#3bb0bf" ) );  //Define color app.
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            mapa = googleMap;
            locRestaurante = new Location ( "localizacion 1" );
            final Geocoder geo = new Geocoder ( getApplicationContext () );
            List <Address> direccion = null;

            maxResultados = 5;
            Bundle paquete = getIntent ().getExtras ();
            if ( paquete != null ) {

                direRestaurante = paquete.getParcelableArrayList ( "localizar" );
            }
            dire = new ArrayList <> ();
            for (int i = 0; i < direRestaurante.size (); i++) {
                dire.add ( direRestaurante.get ( i ).getDireccion () );
                direccion = geo.getFromLocationName ( direRestaurante.get ( i ).getDireccion (), maxResultados );
                locRestaurantes = new LatLng ( direccion.get ( 0 ).getLatitude (), direccion.get ( 0 ).getLongitude () );

                mapa.moveCamera ( CameraUpdateFactory.newLatLngZoom ( locRestaurantes, 7 ) );
                mapa.addMarker ( new MarkerOptions ()
                        .position ( locRestaurantes )
                        .title ( "Renobar" )
                        .snippet ( dire.get ( i ) )
                        .icon ( BitmapDescriptorFactory
                                .defaultMarker ( BitmapDescriptorFactory.HUE_GREEN ) ) );
            }


                locRestaurante.setLatitude ( direccion.get ( 0 ).getLatitude () );
                locRestaurante.setLongitude ( direccion.get ( 0 ).getLongitude () );


        } catch (IOException e) {
            e.printStackTrace ();
        }


        mapa.setMapType ( GoogleMap.MAP_TYPE_NORMAL );
        mapa.getUiSettings ().setZoomControlsEnabled ( true );
        mapa.getUiSettings ().setAllGesturesEnabled ( true );



     /*   // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng ( -34, 151 );
        mapa.addMarker ( new MarkerOptions ().position ( sydney ).title ( "Marker in Sydney" ) );
        mapa.moveCamera ( CameraUpdateFactory.newLatLng ( sydney ) );*/

        miUbicacion ();
    }

    private void miUbicacion() {

        if ( ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            if ( ActivityCompat.shouldShowRequestPermissionRationale ( this, Manifest.permission.ACCESS_FINE_LOCATION ) ) {
                ActivityCompat.requestPermissions ( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5 );
            } else {
                ActivityCompat.requestPermissions ( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5 );
            }

            return;
        }


        //saber nuestra ultima ubicacion conocida y laactualizamos con el metodo creado(actualizarUbicacion(loc))
        LocationManager manager = ( LocationManager ) getSystemService ( Context.LOCATION_SERVICE );
        Location loc = manager.getLastKnownLocation ( LocationManager.GPS_PROVIDER );
        actualizarUbicacion ( loc );
    }

    private void actualizarUbicacion(Location localizacion) {


        if ( localizacion != null ) {
            locMiUbicacion = new Location ( "localizacion 2" );
            latitud = localizacion.getLatitude ();//nuestra latitud
            longitud = localizacion.getLongitude ();//nuestra longitud

            locMiUbicacion.setLatitude ( localizacion.getLatitude () );
            locMiUbicacion.setLongitude ( localizacion.getLongitude () );

            int distancia = ( int ) locRestaurante.distanceTo ( locMiUbicacion );
            Toast.makeText ( getApplicationContext (), "Kilometros/metros: " + distancia, Toast.LENGTH_LONG ).show ();
            LatLng localizacionActualizada = new LatLng ( latitud, longitud );//instanciamos un objeto LatLng con nuestra latitud y longitud


            //añadimos nuestra localizacion con un marcador o un (icono) en el mapa
            mapa.addMarker ( new MarkerOptions ()
                    .position ( localizacionActualizada ).title ( "Renobar: " )
                    .snippet ( "Mi Ubicación" )
                    .icon ( BitmapDescriptorFactory
                            .fromResource ( android.R.drawable.ic_menu_mylocation ) )
                    .icon ( BitmapDescriptorFactory
                            .defaultMarker ( BitmapDescriptorFactory.HUE_RED ) ) );

            mapa.getUiSettings ().setCompassEnabled ( true );//ponemos en true el icono que queremos que muestre en el mapa
            mapa.getUiSettings ().setAllGesturesEnabled ( true );

            //verificamos permisos
            if ( ActivityCompat.checkSelfPermission ( this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION ) ==
                    PackageManager.PERMISSION_GRANTED ) {
                mapa.setMyLocationEnabled ( true );//ponemos a true nuestra localizacion
                mapa.getUiSettings ().setCompassEnabled ( true );//ponemos en true el icono que queremos que muestre en el mapa
                mapa.getUiSettings ().setAllGesturesEnabled ( true );
            }
        }
    }

    //volvemos a la actividad anterior con la flecha (<-) del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intento = null;

        switch (item.getItemId ()) {
            case android.R.id.home:
                intento = new Intent ( getApplicationContext (), MainActivity.class );
                startActivity ( intento );
                finish ();
                break;



        }
        return super.onOptionsItemSelected ( item );
    }



}
