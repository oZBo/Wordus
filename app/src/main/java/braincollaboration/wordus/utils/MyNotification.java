package braincollaboration.wordus.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

import braincollaboration.wordus.R;
import braincollaboration.wordus.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotification {

    public static void sendNotification(Context context, ArrayList<String> foundWordsList, int wordsSize) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationCompat.InboxStyle notificationInbox;


        int size = foundWordsList.size();
        if (size > 1) {
            builder.setAutoCancel(true)
                    .setTicker("Слова найдены!")
                    .setSmallIcon(R.drawable.notify_app_ico_for_api21)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.app_ico))
                    .addAction(R.drawable.ic_play_white_36dp, "Посмотреть", pIntent)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);
            if (size < 5) {
                builder.setContentTitle(foundWordsList.size() + " слова найдено")
                        .setContentText("Найдено " + foundWordsList.size() + " описания из " + wordsSize + " слов");
            } else {
                builder.setContentTitle(foundWordsList.size() + " слов найдено")
                        .setContentText("Найдено " + foundWordsList.size() + " описаний из " + wordsSize + " слов");
            }

            notificationInbox = new NotificationCompat.InboxStyle(builder);
            if (size > 1) {
                notificationInbox
                        .addLine(foundWordsList.get(0))
                        .addLine(foundWordsList.get(1));
            }

            if (size > 2) {
                notificationInbox
                        .addLine(foundWordsList.get(2));
            }

            if (size > 3) {
                notificationInbox
                        .addLine(foundWordsList.get(3));
            }

            if (size > 4) {
                notificationInbox
                        .setSummaryText("+" + (size - 4) + " еще");

            }
            //notificationInbox.defaults = NotificationCompat.DEFAULT_ALL;
            //notificationInbox.build();
            //notificationInbox.flags |= NotificationCompat.FLAG_AUTO_CANCEL;
            notificationManager.notify(Constants.DESCRIPTION_FOUND_NOTIFY_ID, notificationInbox.build());
        } else {
            builder.setContentIntent(pIntent)
                    .setSmallIcon(R.drawable.notify_app_ico_for_api21)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.app_ico))
                    .setTicker("Найдено слово!")
                    .setAutoCancel(true)
                    .setContentTitle("Найдено слово")
                    .setContentText("Найдено описание слова <" + foundWordsList.get(0) + ">.");
            notificationManager.notify(Constants.DESCRIPTION_FOUND_NOTIFY_ID, builder.build());
        }
    }
}
