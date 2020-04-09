package com.pruebas.ades.renobar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private View view;
    private ActionBar actionBar;
    private ImageView imgLogo;
    private Button btnQr,btnInstagram,btnRestaurantes;
    private TextView tvPortada;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        Toolbar miToolbar = findViewById ( R.id.toolbar_clean );
        setSupportActionBar ( miToolbar );

        getSupportActionBar ().setDisplayShowTitleEnabled ( false );
        actionBar = getSupportActionBar ();

        getWindow().addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor( Color.parseColor ( "#3bb0bf" ));  //Define color app.

        imgLogo = findViewById ( R.id.imgLogo );
        btnQr= findViewById(R.id.btnQr);
        btnInstagram= findViewById(R.id.btnInstagram);
        btnRestaurantes= findViewById(R.id.btnRestaurante);

        tvPortada=findViewById(R.id.tvAppRenobar);

        tvPortada.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent portada=new Intent(MainActivity.this,Portada.class);
                startActivity ( portada );
            }
        } );

        btnInstagram.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent instagram=new Intent(MainActivity.this,PerfilInstagram.class);
                startActivity ( instagram );

            }
        } );

        btnRestaurantes.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent listaRestaurante =new Intent(MainActivity.this, ListaRestaurantes.class);
                startActivity ( listaRestaurante );

            }
        } );

        btnQr.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                new IntentIntegrator ( MainActivity.this ).initiateScan ();

            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode,resultCode,data );
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        Context context=this;
        if(result != null)
            if(result.getContents () != null && result.getContents ().contains ( "renobar" )) {
                Intent qr=new Intent(context,Portada.class);
                qr.putExtra ( "qr",result.getContents () );
                startActivity ( qr );
                Log.e ( "result", "Es correcto" + result.getContents ());
            }else {

                AlertDialog.Builder error=new AlertDialog.Builder(this);
                error.setTitle ( "Scan QR" );
                error.setMessage ( "Código QR no valido, intentelo de nuevo.." );
                AlertDialog alertDialog=error.create ();
                alertDialog.show ();

                Log.e ( "result", "no es correcto "  + result.getContents ());
            }
    }
}
