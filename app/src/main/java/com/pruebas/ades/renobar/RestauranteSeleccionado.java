package com.pruebas.ades.renobar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RestauranteSeleccionado extends AppCompatActivity {

    private ActionBar actionBar;
    private WebView vistaRestaurante;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_restaurante_seleccionado );


        Toolbar miToolbar = findViewById ( R.id.toolbar_clean );
        setSupportActionBar ( miToolbar );

        actionBar = getSupportActionBar ();
        vistaRestaurante=findViewById ( R.id.wbVistaRestaurante );


        // Muestra una flecha para volver a la actividad principal
        actionBar.setDisplayHomeAsUpEnabled ( true );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
            getSupportActionBar ().setHomeAsUpIndicator ( getResources ().getDrawable ( R.drawable.ic_arrow_back_black_24dp, null ) );
        else
            getSupportActionBar ().setHomeAsUpIndicator ( getResources ().getDrawable ( R.drawable.ic_arrow_back_black_24dp, null ) );

        getWindow ().addFlags ( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS );
        getWindow ().setStatusBarColor ( Color.parseColor ( "#3bb0bf" ) );  //Define color app.

        paqueteUrl();
    }

    private void paqueteUrl() {

        Bundle paquete =getIntent().getExtras();

        if(paquete!=null){

            String url = paquete.getString ( "url" );
            String url2= paquete.getString ( "url2" );

 //            Log.e ("url2",url2);

            if(url != null) {
                vistaRestaurante.getSettings ().setJavaScriptEnabled ( true );
                vistaRestaurante.setWebViewClient ( new WebViewClient () );
                vistaRestaurante.loadUrl ( url );
            }
            if(url2 != null){

                vistaRestaurante.getSettings().setJavaScriptEnabled(true);
                vistaRestaurante.setWebViewClient(new WebViewClient ());

                vistaRestaurante.loadUrl(url2);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intento = null;

        switch (item.getItemId ()) {
            case android.R.id.home:
                intento = new Intent ( getApplicationContext (), ListaRestaurantes.class );
                startActivity ( intento );
                finish ();
                break;



        }
        return super.onOptionsItemSelected ( item );
    }
}
