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
        configureListPreference(WhichSearchType, SearchType.values())
        configureListPreference(WhichSpeechType, SpeechType.values())
    }

    private fun <A> configureSwitchPreference(setting: Settings<A>) {
        getPreference<SwitchPreferenceCompat>(setting.key).apply {
            setDefaultValue(setting.default)
        }
    }

    private fun <A> configureListPreference(setting: Settings<A>, values: Array<A>) where A : Enum<A>, A : DisplayValue {
        getPreference<ListPreference>(setting.key).apply {
            setDefaultValue(setting.default)
            entries = values.map { a -> a.display }.map(::getString).toTypedArray()
            entryValues = values.map { a -> a.name }.toTypedArray()
        }
    }

    private inline fun <reified A : Preference> getPreference(key: String) : A {
        val preference = preferenceScreen[key]
        if(preference is A) {
            return preference
        } else {
            TODO("Work out why preference [$key] isn't a [${A::class}]")
        }
    }
}