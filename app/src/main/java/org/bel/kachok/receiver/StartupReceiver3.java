package org.bel.kachok.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.bel.kachok.service.NotificationService3;
import org.bel.kachok.utils.AppPreference3;

public class StartupReceiver3 extends BroadcastReceiver {

    private static final String TAG = "StartupReceiver3";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNotificationEnabled = AppPreference3.isNotificationEnabled(context);
        NotificationService3.setNotificationServiceAlarm(context, isNotificationEnabled);
    }
}
