package org.bel.kachok.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.bel.kachok.R;

/**
 * Created by Robin on 11.09.2016.
 */
public class HelpFragment extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_help);
    }
}
