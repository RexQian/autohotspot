package com.tmindtech.autohotspot;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * @author RexQian
 * @date 2020-03-06
 */
public class MySettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}
