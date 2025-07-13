package uk.co.baconi.pka.td

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import uk.co.baconi.pka.common.AccessToken
import uk.co.baconi.pka.td.errors.ErrorActivity
import uk.co.baconi.pka.td.errors.toErrorParcel
import uk.co.baconi.pka.td.settings.Settings

class NoNreApiKeyException(message: String) : Exception(message)

fun AppCompatActivity.provideAccessToken(): AccessToken {
    val nreApiKey = Settings.NreApiKey.getSetting(this)?.trim()
    if(nreApiKey.isNullOrEmpty()) {
        throw NoNreApiKeyException("Unable to find an NRE api key from the app settings.")
    } else {
        return AccessToken(nreApiKey)
    }
}

/**
 * Start an activity in a more Kotlin dsl way.
 */
inline fun <reified A> Context.startActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, A::class.java).apply(block)
    startActivity(intent)
}

/**
 * Start an error activity for a given Throwable.
 */
fun Context.startErrorActivity(throwable: Throwable) {
    startActivity<ErrorActivity> {
        putExtra(ErrorActivity.ERROR_PARCEL, throwable.toErrorParcel())
    }
}