package uk.co.baconi.pka.td

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import kotlin.reflect.KFunction3

sealed class Settings<A>(
    private val key: String,
    private val default: A,
    private val provider: KFunction3<SharedPreferences, String, A, A>
) {

    object NreApiKey : Settings<String?>("nre_api_key", null, SharedPreferences::getString)
    object EnableColouredAvatars : Settings<Boolean>("enable_coloured_avatars", false, SharedPreferences::getBoolean)
    object EnableSpeakingFirstResult : Settings<Boolean>("enable_speaking_first_result", false, SharedPreferences::getBoolean)
    object WhichSearchType : Settings<SearchType>("which_search_type", SearchType.SINGLE_RESULT, ::getSearchType)

    fun getSetting(context: Context): A = getSetting(PreferenceManager.getDefaultSharedPreferences(context))
    private fun getSetting(sharedPreferences: SharedPreferences): A = provider.invoke(sharedPreferences, key, default)

    companion object {
        private fun getSearchType(preferences: SharedPreferences, key: String, default: SearchType): SearchType {
            return preferences.getString(key, null).runCatching(SearchType::valueOf).getOrNull() ?: default
        }
    }
}