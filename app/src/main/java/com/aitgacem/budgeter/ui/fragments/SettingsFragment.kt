package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.aitgacem.budgeter.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}