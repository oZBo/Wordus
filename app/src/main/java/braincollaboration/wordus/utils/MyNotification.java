package braincollaboration.wordus.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import braincollaboration.wordus.R;
import braincollaboration.wordus.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotification {

    public static void sendInboxStyleNotification(Context context, ArrayList<String> foundWordsList, int wordsSize) {
        Resources res = context.getResources();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(context);
        Notification notification;
        if (foundWordsList.size() > 1) {
            if (foundWordsList.size() < 5) {
                builder.setTicker("Слова найдены!")
                        .setContentTitle(foundWordsList.size() + " слова найдено")
                        .setContentText("Найдено " + foundWordsList.size() + " описания из " + wordsSize + " слов")
                        .setSmallIcon(R.drawable.notify_app_ico_for_api21)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.app_ico))
                        .addAction(R.drawable.ic_play_white_36dp, "Посмотреть", pIntent);
            } else {
                builder.setTicker("Слова найдены!")
                        .setContentTitle(foundWordsList.size() + " слов найдено")
                        .setContentText("Найдено " + foundWordsList.size() + " описаний из " + wordsSize + " слов")
                        .setSmallIcon(R.drawable.notify_app_ico_for_api21)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.app_ico))
                        .addAction(R.drawable.ic_play_white_36dp, "Посмотреть", pIntent);
            }

            if (foundWordsList.size() == 2) {
                notification = new Notification.InboxStyle(builder)
                        .addLine(foundWordsList.get(0))
                        .addLine(foundWordsList.get(1))
                        .build();
            } else if (foundWordsList.size() == 3) {
                notification = new Notification.InboxStyle(builder)
                        .addLine(foundWordsList.get(0))
                        .addLine(foundWordsList.get(1))
                        .addLine(foundWordsList.get(2))
                        .build();
            } else if (foundWordsList.size() == 4) {
                notification = new Notification.InboxStyle(builder)
                        .addLine(foundWordsList.get(0))
                        .addLine(foundWordsList.get(1))
                        .addLine(foundWordsList.get(2))
                        .addLine(foundWordsList.get(3))
                        .build();
            } else {
                notification = new Notification.InboxStyle(builder)
                        .addLine(foundWordsList.get(0))
                        .addLine(foundWordsList.get(1))
                        .addLine(foundWordsList.get(2))
                        .addLine(foundWordsList.get(3))
                        .setSummaryText("+" + (foundWordsList.size() - 4) + " еще").build();

            }
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        } else {
            builder.setContentIntent(pIntent)
                    .setSmallIcon(R.drawable.notify_app_ico_for_api21)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.app_ico))
                    .setTicker("Найдено слово!")
                    .setAutoCancel(true)
                    .setContentTitle("Найдено слово")
                    .setContentText("Найдено описание слова <" + foundWordsList.get(0) + ">.");
            notification = builder.build();
        }
        notification.defaults = Notification.DEFAULT_ALL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.DESCRIPTION_FOUND_NOTIFY_ID, notification);
    }
}
