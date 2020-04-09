package com.pruebas.ades.renobar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Portada extends AppCompatActivity {

    private WebView vistaPortada;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_portada );

        getWindow().addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor( Color.parseColor ( "#3bb0bf" ));  //Define color app.

        vistaPortada=findViewById ( R.id.wbVistaPortada );

        cargarPortada();
    }

    private void cargarPortada() {

        String url1 ="https://www.apprenobar.es";
        vistaPortada.getSettings ().setJavaScriptEnabled ( true );
        vistaPortada.setWebViewClient ( new WebViewClient () );
        vistaPortada.loadUrl ( url1 );

        Bundle paquete=getIntent().getExtras();

        if(paquete != null) {

            String url2=paquete.getString ( "qr" );





            if ( paquete != null || paquete.getString ( "qr" ).equals ( url2 ) ){

                vistaPortada.getSettings ().setJavaScriptEnabled ( true );
                vistaPortada.setWebViewClient ( new WebViewClient () );
                vistaPortada.loadUrl ( url2 );

            }
        }
    }

}
