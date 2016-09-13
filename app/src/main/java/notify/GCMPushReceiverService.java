package notify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import svecw.smartcampus.CollegeWallActivity;
import svecw.smartcampus.R;
import utils.Constants;

/**
 * Created by Pavan_Kusuma on 8/21/2016.
 */
public class GCMPushReceiverService extends GcmListenerService {

    final static String Group_Key = Constants.appName;
    int notificationNumber=0;

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        String title = data.getString("title");
        String message = data.getString("message");
        //Displaying a notification with the message
        sendNotification(title, message);
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String title, String message) {

        SharedPreferences prefs = getSharedPreferences(Constants.appName+"_notify", Context.MODE_PRIVATE);
        notificationNumber = prefs.getInt("notificationNumber", 0);

        //notificationManager.notify(notificationNumber , noBuilder.build());
        SharedPreferences.Editor editor = prefs.edit();
        notificationNumber++;
        editor.putInt("notificationNumber", notificationNumber);
        editor.commit();


        Intent intent = new Intent(this, CollegeWallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setGroup(Group_Key)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(sound)
                .setNumber(notificationNumber)
                .setContentIntent(pendingIntent);




        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification




    }
}
