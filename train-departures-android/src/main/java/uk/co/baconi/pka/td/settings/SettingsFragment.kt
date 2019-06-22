package uk.co.baconi.pka.td.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import uk.co.baconi.pka.td.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)
    }
}