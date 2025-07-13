package uk.co.baconi.pka.td.settings

import android.os.Bundle
import androidx.preference.*
import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.settings.Settings.*

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)
        configureSwitchPreference(EnableColouredAvatars)
        configureSwitchPreference(EnableColouredDepartureTimes)
        configureSwitchPreference(EnableSpeakingFirstResult)
        configureListPreference(WhichSearchType, SearchType.entries.toTypedArray())
        configureListPreference(WhichSpeechType, SpeechType.entries.toTypedArray())
    }

    private fun <A> configureSwitchPreference(setting: Settings<A>) {
        preferenceScreen.get<SwitchPreferenceCompat>(setting.key)?.apply {
            setDefaultValue(setting.default) // TODO - Work out why this doesn't work
        }
    }

    private fun <A> configureListPreference(setting: Settings<A>, values: Array<A>) where A : Enum<A>, A : DisplayValue {
        preferenceScreen.get<ListPreference>(setting.key)?.apply {
            setDefaultValue(setting.default) // TODO - Work out why this doesn't work
            entries = values.map { a -> a.display }.map(::getString).toTypedArray()
            entryValues = values.map { a -> a.name }.toTypedArray()
        }
    }
}