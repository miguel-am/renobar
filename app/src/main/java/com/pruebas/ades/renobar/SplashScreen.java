package com.pruebas.ades.renobar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_splashscreen );

        getWindow().addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor( Color.WHITE);  //Define color blanco.

        new Handler ( ).postDelayed ( new Runnable () {
            @Override
            public void run() {


                Intent intent=new Intent ( SplashScreen.this,MainActivity.class );

                startActivity ( intent );


                finish ();
            }
        },1500 );
    }
    }

