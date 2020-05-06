package com.pruebas.ades.renobar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Portada extends AppCompatActivity {

    private WebView vistaPortada;
    private ActionBar actionBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_portada );

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

        vistaPortada=findViewById ( R.id.wbVistaPortada );

        cargarPortada();
    }

    private void cargarPortada() {


        Bundle paquete=getIntent().getExtras();

        if(paquete != null) {

            String url2=paquete.getString ( "qr" );
            String appRenobar=paquete.getString ( "appempresas" );
            String areaPrivada=paquete.getString ( "area" );

            vistaPortada.getSettings ().setJavaScriptEnabled ( true );
            vistaPortada.setWebViewClient ( new WebViewClient () );

            if(url2 !=null) {
                vistaPortada.loadUrl ( url2 );
            }

            if(appRenobar!=null){
                vistaPortada.loadUrl ( appRenobar );
            }

            if(areaPrivada!=null){
                vistaPortada.loadUrl ( areaPrivada );
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
