package ro.keravnos.eddie.silence.Model;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;


import ro.keravnos.eddie.silence.R;


import static ro.keravnos.eddie.silence.MainActivity.CHANNEL_ID;


public class Notifications extends Service {

    Context context ;

    public  Notifications(Context context)
    {
        this.context = context;
    }

    public Notifications() {}


    public void create()
   {
       Intent notificationIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
       notificationIntent.setPackage(null); // The golden row !!!
       notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

       //intent.setFlags(intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
       PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

       NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
               .setSmallIcon(R.drawable.ic_icon)
               .setContentTitle("Silence")
               .setContentText("Silence is running")
               .setPriority(NotificationCompat.PRIORITY_DEFAULT).setOngoing(true).setContentIntent(pendingIntent);

       NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
       notificationManager.notify(1,mBuilder.build());

   }

    public void createNotificationChannel()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    @Override
    public IBinder onBind( Intent intent )
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        /*super.onTaskRemoved(rootIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();*/
    }
}
