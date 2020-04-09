package com.pruebas.ades.renobar;


import android.content.Intent;
import android.graphics.Color;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pruebas.ades.renobar.AdaptadorLista.ListaAdapter;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pruebas.ades.renobar.Model.Restaurante;

public class ListaRestaurantes extends AppCompatActivity {

    private ListView listar;
    private List <Restaurante> restaurantes;
    private com.google.android.material.floatingactionbutton.FloatingActionButton fbaMapa;
    private androidx.appcompat.widget.SearchView buscar;
    private ActionBar actionBar;
    private ListaAdapter listaAdapter;
    private ArrayList<Restaurante>miArray;
    private androidx.swiperefreshlayout.widget.SwipeRefreshLayout refrescar;

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

        getWindow().addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor( Color.parseColor ( "#3bb0bf" ));  //Define color app.

        listar=findViewById(R.id.lvListaRestaurantes);
        fbaMapa=findViewById ( R.id.fabMapaInicio );
        restaurantes=new ArrayList<>();
        buscar=findViewById ( R.id.schRestaurante );
        refrescar=findViewById ( R.id.pullToRefresh );


        Task task= new Task ();
        task.execute (  );


        restaurantes.add ( new Restaurante ( "El velero","Plaza cervantes, Alcala de Henares","10 kmts..." ,R.drawable.qr ) );

        restaurantes.add ( new Restaurante ( "El meson de tu pueblo","Av de Beleña, 9 Guadalajara","20 kmts..." ,R.drawable.qr ) );

        restaurantes.add ( new Restaurante ( "La bodega de  oro","Guzman el bueno,30 Madrid","29 kmts..." ,R.drawable.qr ) );

        restaurantes.add ( new Restaurante ( "Fruits vegen","Paseo castellana, 102 Madrid","32 kmts..." ,R.drawable.qr ) );

        listaAdapter=new ListaAdapter ( restaurantes, ListaRestaurantes.this );
        listar.setAdapter(listaAdapter);



        miArray=new ArrayList<>();
        miArray.addAll(restaurantes);
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
                listaAdapter=new ListaAdapter ( restaurantes, ListaRestaurantes.this );
                listar.setAdapter(listaAdapter);

                refrescar.setRefreshing(false);
            }
        } );

        fbaMapa.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent locRestaurante = new Intent ( ListaRestaurantes.this,maps_renobar.class );
                locRestaurante.putExtra ( "localizar", ( Serializable ) restaurantes );
                startActivity ( locRestaurante );
            }
        } );



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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate ( R.menu.menu_buscar, menu );
        return true;
    }


    private void buscar() {

        buscar.setVisibility ( View.VISIBLE );
        buscar.setQueryHint ( "Buscar restaurante" );

        buscar.setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {

             /*   miArray=new ArrayList<>();
                miArray.addAll(restaurantes);*/

                restaurantes.clear ();
                for(Restaurante res: miArray){

                    if(res.getNombre ().toLowerCase (  ).contains ( query.trim ().toLowerCase () )){

                        restaurantes.add ( res );
                        Log.e ( "añadido",res.getNombre () );
                    }else{
                        Log.e ( "borrado",res.getNombre () );
                        restaurantes.remove ( res );
                    }
                }
                listaAdapter=new ListaAdapter ( restaurantes, ListaRestaurantes.this );
                listar.setAdapter(listaAdapter);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase ( Locale.getDefault ());

                restaurantes.clear();
                if(newText.length ()==0){

                restaurantes.addAll(miArray);
                    Log.e ( "longitud",restaurantes.size () + "" );

                }else{
                   for(Restaurante res: miArray){

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

        String registros="",error="",URL="jdbc:mysql://127.0.0.1//sqlades",USUARIO="ades",CONTRASEÑA="almogabar";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Class.forName ( "com.mysql.jdbc.Driver" );
                Connection con=DriverManager.getConnection("jdbc:mysql://bbdd.renobarapp.es/ddb146636","ddb146636",":mM(F3E^LkSS");
                Statement statement=con.createStatement ();
                ResultSet cursor=statement.executeQuery ( "select * from Restaurantes" );

                while(cursor.next ()){

                    registros=cursor.getString ( "id") + " , " + cursor.getString ( "nombre") + " , " +
                          " , " + cursor.getString ( "direccion") + " , " +cursor.getString ( "imagen") + " , " +cursor.getString ( "url");



                }

            } catch (ClassNotFoundException | SQLException e) {
               error= e.toString ();
              e.printStackTrace ();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Log.e ( "resultados" ,registros );

            if(error!=null){
                Log.e ( "error" ,error);
            }
            super.onPostExecute ( aVoid );
        }
    }

}


