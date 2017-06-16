package together.withyou;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.Random;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

/**
 * Created by d.lihodedov on 22.02.2017.
 */
public class NotifyManager {
    String Title;
    String Description;
    Context context;
    public NotifyManager(String title, String description, Context cont){
        Title=title;
       Description=description;
       context=cont;
    }


//    int FM_NOTIFICATION_ID=5;


    protected void addNotification() {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(Title)
                        .setAutoCancel(true)
                        .setContentText(Description)
                        .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("NOTIFY",true);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Random generator = new Random();
        int FM_NOTIFICATION_ID = generator.nextInt(10000);
        // Add as notification
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(FM_NOTIFICATION_ID, builder.build());
    }


}
