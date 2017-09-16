package com.ood.clean.waterball.teampathy.Framework.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ood.clean.waterball.teampathy.Presentation.UI.Activity.MainActivity;
import com.ood.clean.waterball.teampathy.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Firebase";
    private static final String EVENTTYPE = "eventType";
    private NotificationParser notificationParser;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        notificationParser = new NotificationParser(this);

        if (remoteMessage.getData().size() > 0) {
            Map<String,String> data = remoteMessage.getData();
            Log.i(TAG, "Message data payload: " + data);
            Intent intent = parseIntent(data);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            sendNotification(notificationParser.parseModel(data));
        }

        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private Intent parseIntent(Map<String,String> data){
        Intent intent = new Intent();
        for (String key : data.keySet())
        {
            if (key.equals(EVENTTYPE))  // eventType defines an action which all the UIs listening.
                intent.setAction(data.get(EVENTTYPE));
            else  // other data is about the properties UI needs
                intent.putExtra(key, data.get(key));
            Log.i(TAG, "Message: " + key);
        }

        return intent;
    }

    private void sendNotification(NotificationModel model) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_icon)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentTitle(model.title)
                .setContentText(model.message);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }


    //todo don't know how to show custom icon on the notification
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    static class NotificationModel{
        private String title;
        private String message;
        public NotificationModel(String title, String message) {
            this.title = title;
            this.message = message;
        }
    }


}
