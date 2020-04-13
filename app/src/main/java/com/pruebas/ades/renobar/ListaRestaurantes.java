package com.pruebas.ades.renobar;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.model.LatLng;
import com.pruebas.ades.renobar.AdaptadorLista.ListaAdapter;


import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pruebas.ades.renobar.Model.Restaurante;

import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;

public class ListaRestaurantes extends AppCompatActivity {

    private ListView listar;
    private List <Restaurante> restaurantes;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fbaMapa;
    private androidx.appcompat.widget.SearchView buscar;
    private ActionBar actionBar;
    private ListaAdapter listaAdapter;
    private ArrayList <Restaurante> miArray;
    private androidx.swiperefreshlayout.widget.SwipeRefreshLayout refrescar;
    private Location locRestaurante;
    private double longitud;
    private double latitud;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_listar );


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

        listar = findViewById ( R.id.lvListaRestaurantes );
        fbaMapa = findViewById ( R.id.fabMapaInicio );
        restaurantes = new ArrayList <> ();
        buscar = findViewById ( R.id.schRestaurante );
        refrescar = findViewById ( R.id.pullToRefresh );


        Task task = new Task ();
        task.execute ();


        refrescar.setColorSchemeResources ( R.color.colorRefresh1 );
        refrescar.setOnRefreshListener ( new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                try {
                    // refrescar.setRefreshing(true);


                    Thread.sleep ( 1500 );
                    refrescar.setColorSchemeResources ( R.color.colorRefresh3 );

                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
                listaAdapter = new ListaAdapter ( restaurantes, ListaRestaurantes.this );
                listar.setAdapter ( listaAdapter );

                refrescar.setRefreshing ( false );
            }
        } );

        fbaMapa.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent locRestaurante = new Intent ( ListaRestaurantes.this, maps_renobar.class );
                locRestaurante.putExtra ( "localizar", ( Serializable ) restaurantes );
                startActivity ( locRestaurante );
            }
        } );



    }

    //metodo para calcular distancias
    private void calcularDistancias() throws IOException {


        try {

            locRestaurante = new Location ( "localizacion 1" );
            final Geocoder geo = new Geocoder ( getApplicationContext () );
            List <Address> direccion = null;

            //objeto y metodo para acceder a la localizacion de servicios
            LocationManager manager = ( LocationManager ) getSystemService ( Context.LOCATION_SERVICE );
            //permisos geolocalizacion
            if ( ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION )
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_COARSE_LOCATION )
                    != PackageManager.PERMISSION_GRANTED ) {

                return;
            }
            //ultima localizacion provista por gps
            Location loc = manager.getLastKnownLocation ( LocationManager.GPS_PROVIDER );
            //restaurantes con todos sus atributos
            for (int i = 0; i < restaurantes.size (); i++) {
                //geolocalizamos direccion de los restaurantes
                direccion = geo.getFromLocationName ( restaurantes.get ( i ).getDireccion (), 1 );

                locRestaurante.setLatitude ( direccion.get ( 0 ).getLatitude () );//latitud restaurante
                locRestaurante.setLongitude ( direccion.get ( 0 ).getLongitude () );//longitud restauarantes

                if ( loc != null ) {
                    Location  locMiUbicacion = new Location ( "localizacion 2" );
                    latitud = loc.getLatitude ();//nuestra latitud
                    longitud = loc.getLongitude ();//nuestra longitud

                    locMiUbicacion.setLatitude ( latitud );//latitud cliente
                    locMiUbicacion.setLongitude ( longitud );//longitud cliente

                    //calculo de distancia con un solo decimal
                    double distancia = ( double ) locRestaurante.distanceTo(locMiUbicacion);
                    double resultado= ( double ) (Math.round ( distancia *100d)/100d);



                   // Resultado de kilometros con redondeo de un decimal
                    Log.e ( String.valueOf ( this ), restaurantes.get ( i ).getNombre ()+  String.format("%.1f", resultado/1000) );

                    //añadimos distancia a la lista de restaurantes
                    restaurantes.get ( i ).setKmts ( String.format("%.1f", resultado/1000)  + " km");
                }
            }

        } catch (IOException e) {
            e.printStackTrace ();
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
                buscar ();
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
    private void buscar() {

        buscar.setVisibility ( View.VISIBLE );
        buscar.setQueryHint ( "Buscar restaurante" );

        buscar.setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {//metodo de busqueda clicar lupa

             /*   miArray=new ArrayList<>();
                miArray.addAll(restaurantes);*/

                restaurantes.clear ();
                for(Restaurante res: miArray){
                    //convertimos el texto de la lista y del buscador en minusculas y si coincide al completo lo mostramos como resultado
                    if(res.getNombre ().toLowerCase (  ).contains ( query.trim ().toLowerCase () )){

                        restaurantes.add ( res );
                        Log.e ( "añadido",res.getNombre () );
                    }else{
                        Log.e ( "borrado",res.getNombre () );
                        restaurantes.remove ( res );
                    }
                }
                //actualizamos lista
                listaAdapter=new ListaAdapter ( restaurantes, ListaRestaurantes.this );
                listar.setAdapter(listaAdapter);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//metodo de busqueda por caracter escrito
                newText=newText.toLowerCase ( Locale.getDefault ());

                restaurantes.clear();
                if(newText.length ()==0){

                restaurantes.addAll(miArray);
                    Log.e ( "longitud",restaurantes.size () + "" );

                }else{
                   for(Restaurante res: miArray){
                       //convertimos el texto de la lista y del buscador en minusculas y si coinciden va mostrando resultados de la lista
                       if(res.getNombre ().toLowerCase (  ).contains ( newText.toLowerCase () )){
                           restaurantes.add(res);
                           Log.e ( "añadido",restaurantes.size () + " --> "  + restaurantes.get ( 0 ).getNombre ());
                           listaAdapter=new ListaAdapter ( restaurantes, ListaRestaurantes.this );
                           listar.setAdapter(listaAdapter);

                       }

                   }
                    listaAdapter.notifyDataSetChanged ();
                    Log.e ( "adapter","" );
                }

                return true;
            }
        } );



        buscar.setOnCloseListener ( new SearchView.OnCloseListener () {
            @Override
            public boolean onClose() {
                //ocultamos el buscador y mostramos la lista
                buscar.setVisibility ( View.GONE );
                restaurantes.clear ();
                restaurantes.addAll(miArray);
                listaAdapter=new ListaAdapter ( restaurantes, ListaRestaurantes.this );
                listar.setAdapter(listaAdapter);
                return false;
            }
        } );
    }
    class Task extends AsyncTask<Void,Void,Void>{

        String registros="",error="",URL="jdbc:mysql://bbdd.renobarapp.es/ddb146636",USUARIO="ddb146636",CONTRASEÑA=":mM(F3E^LkSS";

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {

            try {

               //Class.forName ( "com.mysql.jdbc.Driver" );
               // Connection con=DriverManager.getConnection(URL,USUARIO,CONTRASEÑA);
                BasicDataSource basicDataSource = new BasicDataSource();
                basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
                basicDataSource.setUsername("ddb146636");
                basicDataSource.setPassword(":mM(F3E^LkSS");
                basicDataSource.setUrl("jdbc:mysql://bbdd.renobarapp.es/ddb146636");
                basicDataSource.setValidationQuery ( "select 1" );
                basicDataSource.setRemoveAbandoned(true);
                basicDataSource.setTestOnBorrow(true);
                basicDataSource.setLogAbandoned(true);
                basicDataSource.setTestWhileIdle(true);
                basicDataSource.setTestOnReturn(true);
                basicDataSource.setRemoveAbandonedTimeout(60);

                Connection con=basicDataSource.getConnection ();
                Statement statement=con.createStatement ();
                ResultSet cursor=statement.executeQuery ( "select * from Restaurantes" );


                while(cursor.next ()){

                    registros=cursor.getString ( "id") + " , " + cursor.getString ( "nombre") + " , " +
                            " , " + cursor.getString ( "direccion") + " , " +cursor.getString ( "imagen") + " , " +cursor.getString ( "url");

                    restaurantes.add ( new Restaurante ( cursor.getString ( "nombre"),cursor.getString ( "direccion") ,"32km " ,cursor.getString ( "imagen" )) );



                }
                restaurantes.add ( new Restaurante ( "El velero","Plaza cervantes, Alcala de Henares","10km" ,"https://cdn.pixabay.com/photo/2016/11/18/22/21/architecture-1837150_960_720.jpg" ));

                restaurantes.add ( new Restaurante ( "El meson de tu pueblo","Av de Beleña, 9 Guadalajara","20km" ,"https://cdn.pixabay.com/photo/2015/09/02/12/35/bar-918541_960_720.jpg" ) );

                restaurantes.add ( new Restaurante ( "La bodega de  oro","Guzman el bueno,30 Madrid","29km" ,"https://cdn.pixabay.com/photo/2015/03/26/09/54/restaurant-690569_960_720.jpg" ) );

                restaurantes.add ( new Restaurante ( "Fruits vegen","Paseo castellana, 102 Madrid","32km" ,"https://cdn.pixabay.com/photo/2015/05/31/11/23/table-791167_960_720.jpg" ) );


           calcularDistancias ();

            } catch (SQLException   e) {
                error= e.toString ();
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            listaAdapter=new ListaAdapter ( restaurantes, ListaRestaurantes.this );
            listar.setAdapter(listaAdapter);



            miArray=new ArrayList<>();
            miArray.addAll(restaurantes);

            Log.e ( "resultados" ,registros );

            if(error!=null){
                Log.e ( "error" ,error);
            }
            super.onPostExecute ( aVoid );
        }
    }

}


