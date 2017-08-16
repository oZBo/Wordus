package braincollaboration.wordus.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class InternetStatusBroadcastReceiver extends BroadcastReceiver {
    IInternetStatusBroadcastReceiverCallback callback;

    public InternetStatusBroadcastReceiver(IInternetStatusBroadcastReceiverCallback callBack) {
        this.callback = callBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Constants.LOG_TAG, "receiver is on");
        if (InternetUtil.isInternetTurnOn(context)) {
            Log.d(Constants.LOG_TAG, "internet is on");
            callback.internetIsOn();
        }
    }
}
