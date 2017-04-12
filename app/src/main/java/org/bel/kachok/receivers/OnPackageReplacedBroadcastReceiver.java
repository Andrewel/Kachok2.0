package org.bel.kachok.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import org.bel.kachok.R;
import org.bel.kachok.utils.StepDetectionServiceHelper;

/**
 * Receives the broadcast if the own package is replaced and
 * starts the step detection and it's required services if
 * step detection is enabled.
 */
public class OnPackageReplacedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // init preferences
        PreferenceManager.setDefaultValues(context, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(context, R.xml.pref_notification, false);

        // start all services
        StepDetectionServiceHelper.startAllIfEnabled(context);
    }
}
