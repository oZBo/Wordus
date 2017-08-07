package braincollaboration.wordus.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InternetConnectionStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (InternetUtil.isInternetTurnOn(context)) {

        }
    }
}
