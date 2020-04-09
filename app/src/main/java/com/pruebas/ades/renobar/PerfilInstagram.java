package com.pruebas.ades.renobar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pruebas.ades.renobar.R;

public class PerfilInstagram extends AppCompatActivity {

    private WebView vista;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_perfil_instagram );

        vista=findViewById ( R.id.wbVista );

        getWindow().addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor( Color.parseColor ( "#3bb0bf" ));  //Define color app.

        cargarInstagram();

    }

    private void cargarInstagram() {


        String url="https://instagram.com/renobar.es?igshid=13l69fm6kay7a";

        vista.getSettings().setJavaScriptEnabled(true);
        vista.setWebViewClient(new WebViewClient ());
        vista.loadUrl(url);
    }
}
