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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pruebas.ades.renobar.AdaptadorLista.ListaAdapter;
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
    private com.google.android.material.floatingactionbutton.FloatingActionButton fbaMapa;
    private ArrayList <Restaurante> miArray;
    private ListaAdapter listaAdapter;
    private androidx.appcompat.widget.SearchView buscar;
    private  Geocoder geo=null;
    private List <Address> direccion = null;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_maps_renobar );
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ( SupportMapFragment ) getSupportFragmentManager ()
                .findFragmentById ( R.id.map );
        mapFragment.getMapAsync ( this );

        fbaMapa = findViewById ( R.id.fabListaRestaurantes );
        buscar = findViewById ( R.id.schRestauranteMapa );


        Toolbar miToolbar = findViewById ( R.id.toolbar_clean );
        setSupportActionBar ( miToolbar );

        actionBar = getSupportActionBar ();
        miArray = new ArrayList <> ();


        // Muestra una flecha para volver a la actividad principal
        actionBar.setDisplayHomeAsUpEnabled ( true );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
            getSupportActionBar ().setHomeAsUpIndicator ( getResources ().getDrawable ( R.drawable.ic_arrow_back_black_24dp, null ) );
        else
            getSupportActionBar ().setHomeAsUpIndicator ( getResources ().getDrawable ( R.drawable.ic_arrow_back_black_24dp, null ) );


        getWindow ().addFlags ( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
        getWindow ().setStatusBarColor ( Color.parseColor ( "#3bb0bf" ) );  //Define color app.

        fbaMapa.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent lista = new Intent ( maps_renobar.this, ListaRestaurantes.class );
                startActivity ( lista );
                finish ();
            }
        } );

        Bundle paquete = getIntent ().getExtras ();
        if ( paquete != null ) {

            direRestaurante = paquete.getParcelableArrayList ( "localizar" );
            miArray.addAll ( direRestaurante );

            Log.e ( "resultado listview",direRestaurante.get ( 0 ).getNombre () );

        }
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


            geo = new Geocoder ( getApplicationContext () );
            maxResultados = 1000;

            dire = new ArrayList <> ();
            for (int i = 0; i < direRestaurante.size (); i++) {
                dire.add ( direRestaurante.get ( i ).getDireccion () );
                direccion = geo.getFromLocationName ( direRestaurante.get ( i ).getDireccion (), maxResultados );
//                Log.e ( "restaurantes", String.valueOf ( direccion.get ( i ).getLatitude () ) );
                locRestaurantes = new LatLng ( direccion.get ( 0 ).getLatitude (), direccion.get ( 0 ).getLongitude () );

                mapa.moveCamera ( CameraUpdateFactory.newLatLngZoom ( locRestaurantes, 7 ) );
                mapa.addMarker ( new MarkerOptions ()
                        .position ( locRestaurantes )
                        .title ( direRestaurante.get ( i ).getNombre () )
                        .snippet ( dire.get ( i ) )
                        .icon ( BitmapDescriptorFactory
                                .defaultMarker ( BitmapDescriptorFactory.HUE_GREEN ) ) );
            }


        } catch (IOException e) {
            e.printStackTrace ();
        }


        mapa.setMapType ( GoogleMap.MAP_TYPE_NORMAL );
        mapa.getUiSettings ().setZoomControlsEnabled ( true );
        mapa.setPadding ( 0, 0, 17, 200 );
        mapa.getUiSettings ().setMapToolbarEnabled ( true );
        mapa.getUiSettings ().setMyLocationButtonEnabled ( true );


        infoRestaurante ( mapa );


     /*   // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng ( -34, 151 );
        mapa.addMarker ( new MarkerOptions ().position ( sydney ).title ( "Marker in Sydney" ) );
        mapa.moveCamera ( CameraUpdateFactory.newLatLng ( sydney ) );*/

        miUbicacion ();
    }

    private void infoRestaurante(GoogleMap mapa) {

        mapa.setOnInfoWindowClickListener ( new GoogleMap.OnInfoWindowClickListener () {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String marca = marker.getSnippet ();

                for (Restaurante e : direRestaurante) {

                    if ( marker.getSnippet ().equals ( e.getDireccion () ) ) {
                        Log.e ( "direccion", e.getDireccion () );
                        Intent infoRestaurantes = new Intent ( getApplicationContext (), RestauranteSeleccionado.class );
                        infoRestaurantes.putExtra ( "url2", e.getUrl () );
                        startActivity ( infoRestaurantes );
                        finish ();

                    }

                }

            }
        } );
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

            latitud = localizacion.getLatitude ();//nuestra latitud
            longitud = localizacion.getLongitude ();//nuestra longitud



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

            case R.id.lupa:
                buscar (mapa);
                return true;

        }
        return super.onOptionsItemSelected ( item );
    }

    //dibujar en el toolbar menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate ( R.menu.menu_buscar, menu );
        return true;
    }

    //metodo para buscar restaurantes en el buscador
    //buscamos por caracter o cuando tengamos el texto completo clicando la lupa
    private void buscar(final GoogleMap mapa) {

        buscar.setVisibility ( View.VISIBLE );
        buscar.setQueryHint ( "Buscar restaurante" );

        buscar.setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {//metodo de busqueda clicar lupa

             /*   miArray=new ArrayList<>();
                miArray.addAll(restaurantes);

                direRestaurante.clear ();
                for (Restaurante res : miArray) {
                    //convertimos el texto de la lista y del buscador en minusculas y si coincide al completo lo mostramos como resultado
                    if ( res.getNombre ().toLowerCase ().contains ( query.trim ().toLowerCase () ) ) {

                        direRestaurante.add ( res );
                        Log.e ( "añadido", res.getNombre () );
                    } else {
                        Log.e ( "borrado", res.getNombre () );
                        direRestaurante.remove ( res );
                    }

                }


*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//metodo de busqueda por caracter escrito
                newText = newText.toLowerCase ( Locale.getDefault () );

                direRestaurante.clear ();
                if ( newText.length () == 0 ) {

                    direRestaurante.addAll ( miArray );
                    Log.e ( "longitud", direRestaurante.size () + "" );

                    for(int x=0;x<direRestaurante.size ();x++){

                        mapa.moveCamera ( CameraUpdateFactory.newLatLngZoom ( locRestaurantes, 6 ) );
                        mapa.addMarker ( new MarkerOptions ()
                                .position ( locRestaurantes )
                                .title ( direRestaurante.get ( x ).getNombre () )
                                .snippet ( direRestaurante.get ( x ).getDireccion () )
                                .icon ( BitmapDescriptorFactory
                                        .defaultMarker ( BitmapDescriptorFactory.HUE_GREEN ) ) );

                    }


                } else {
                    for (Restaurante res : miArray) {
                        //convertimos el texto de la lista y del buscador en minusculas y si coinciden va mostrando resultados de la lista
                        if ( res.getNombre ().toLowerCase ().contains ( newText.toLowerCase () ) ) {
                            direRestaurante.add ( res );
                            try {

                                direccion = geo.getFromLocationName ( res.getDireccion (), maxResultados );
                                locRestaurantes = new LatLng ( direccion.get ( 0 ).getLatitude (), direccion.get ( 0 ).getLongitude () );
                                Log.e ( "añadido", direRestaurante.size () + " --> " + direRestaurante.get ( 0 ).getNombre () );

                                mapa.getUiSettings ().setAllGesturesEnabled ( true );
                                mapa.getUiSettings ().setMapToolbarEnabled ( true );
                                mapa.getUiSettings ().setRotateGesturesEnabled ( true );

                                mapa.moveCamera ( CameraUpdateFactory.newLatLngZoom ( locRestaurantes, 15 ) );
                                mapa.addMarker ( new MarkerOptions ()
                                        .position ( locRestaurantes )
                                        .title ( res.getNombre () )
                                        .snippet ( res.getDireccion () )
                                        .icon ( BitmapDescriptorFactory
                                                .defaultMarker ( BitmapDescriptorFactory.HUE_GREEN ) ) );

                            } catch (IOException e) {
                                e.printStackTrace ();
                            }





                        }

                    }

                }

                return true;
            }
        } );


        buscar.setOnCloseListener ( new SearchView.OnCloseListener () {
            @Override
            public boolean onClose() {
                //ocultamos el buscador y mostramos la lista
                buscar.setVisibility ( View.GONE );
                direRestaurante.clear ();
                direRestaurante.addAll(miArray);

                return false;
            }
        } );
    }
}
