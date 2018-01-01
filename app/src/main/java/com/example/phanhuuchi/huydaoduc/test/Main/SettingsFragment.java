package com.example.phanhuuchi.huydaoduc.test.Main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.phanhuuchi.huydaoduc.test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    // thêm fragment này vào settingActivity để show preference thay vì show fragment screen

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.preferences,rootKey);
    }


}
