package org.bel.kachok.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;

import org.bel.kachok.R;
import org.bel.kachok.fragments.DailyReportFragment;
import org.bel.kachok.fragments.MainFragment;
import org.bel.kachok.fragments.MonthlyReportFragment;
import org.bel.kachok.fragments.WeeklyReportFragment;
import org.bel.kachok.utils.StepDetectionServiceHelper;

/**
 * Main view incl. navigation drawer and fragments
 *
 * @author Tobias Neidig, Karola Marky
 * @version 20161214
 */
public class MainActivity extends BaseActivity implements DailyReportFragment.OnFragmentInteractionListener, WeeklyReportFragment.OnFragmentInteractionListener, MonthlyReportFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init preferences
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);

        // Load first view
        final android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new MainFragment(), "MainFragment");
        fragmentTransaction.commit();

        // Start step detection if enabled and not yet started
        StepDetectionServiceHelper.startAllIfEnabled(this);
        //Log.i(LOG_TAG, "NotesActivity initialized");
    }
//Install main screen
    @Override
    protected int getNavigationDrawerID() {
        return R.id.menu_home;
    }

}
