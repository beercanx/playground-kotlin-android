package uk.co.baconi.pka.td.settings

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.reflect.KFunction3

sealed class Settings<A>(
    val key: String,
    val default: A,
    private val provider: KFunction3<SharedPreferences, String, A, A>
) {

    object NreApiKey : Settings<String?>("nre_api_key", null, SharedPreferences::getString)
    object EnableColouredAvatars : Settings<Boolean>("enable_coloured_avatars", false, SharedPreferences::getBoolean)
    object EnableColouredDepartureTimes : Settings<Boolean>("enable_coloured_departure_times", true, SharedPreferences::getBoolean)
    object EnableSpeakingFirstResult : Settings<Boolean>("enable_speaking_first_result", true, SharedPreferences::getBoolean)
    object WhichSearchType : Settings<SearchType>("which_search_type", SearchType.MULTIPLE_RESULTS, Companion::getSearchType)
    object WhichSpeechType : Settings<SpeechType>("which_speech_type", SpeechType.PAUSE_OTHER_SOUNDS, Companion::getSpeechType)

    fun getSetting(context: Context): A = getSetting(getPreferenceManager(context))
    private fun getSetting(sharedPreferences: SharedPreferences): A = provider.invoke(sharedPreferences, key, default)

    companion object {
        private fun getPreferenceManager(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)

        private fun getSearchType(preferences: SharedPreferences, key: String, default: SearchType): SearchType {
            return preferences.getString(key, null).enumLookUp(SearchType::valueOf) ?: default
        }

        private fun getSpeechType(preferences: SharedPreferences, key: String, default: SpeechType): SpeechType {
            return preferences.getString(key, null).enumLookUp(SpeechType::valueOf) ?: default
        }

        private fun <E : Enum<E>> String?.enumLookUp(lookup: (String) -> E): E? {
            return this?.runCatching(lookup)?.getOrNull()
        }
    }
}