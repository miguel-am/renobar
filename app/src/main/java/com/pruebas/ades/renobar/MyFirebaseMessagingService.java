package com.pruebas.ades.renobar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
       if(remoteMessage.getData ().size () > 0){
           showNotification ( remoteMessage.getData ().get ( "title" ),
                   remoteMessage.getData ().get ( "message" ));
       }
       if(remoteMessage.getNotification () !=null){

           showNotification ( remoteMessage.getNotification ().getTitle (),
                   remoteMessage.getNotification ().getBody ());

       }
    }

    private RemoteViews getCustomsDesign(String title, String message){

        Context context;
        RemoteViews remoteViews=new RemoteViews(this.getPackageName(), R.layout.notificaciones);
        remoteViews.setTextViewText ( R.id.tvTitle,title );
        remoteViews.setTextViewText ( R.id.tvMessage,message );
        remoteViews.setImageViewResource ( R.id.imgIcono, R.drawable.icoreno);
        return remoteViews;

    }

    public void showNotification(String title, String message){

        Intent intent=new Intent(this,SplashScreen.class);
        String CHANEL_ID="renobar_app";
        intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        Context context;
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder= new NotificationCompat.Builder (this, CHANEL_ID)
                .setSmallIcon ( R.drawable. icoreno )
                .setSound ( uri )
                .setAutoCancel ( true )
                .setVibrate ( new long[]{1000,1000,1000,1000,1000} )
                .setOnlyAlertOnce ( true )
                .setContentIntent ( pendingIntent );

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            builder=builder.setContent ( getCustomsDesign ( title,message ) );
        }else{
            builder=builder.setContentTitle(title)
            .setContentText ( message )
            .setSmallIcon ( R.drawable.icoreno );
        }

        NotificationManager notificationManager = ( NotificationManager ) getSystemService ( Context.NOTIFICATION_SERVICE );

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name;
            NotificationChannel notificationChannel=new NotificationChannel(CHANEL_ID, "renobar",NotificationManager.IMPORTANCE_HIGH );
            notificationChannel.setSound ( uri,null );
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify ( 0,builder.build () );
    }
}
