package uk.co.baconi.pka.td

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import arrow.core.Try
import uk.co.baconi.pka.td.errors.ErrorActivity
import uk.co.baconi.pka.td.errors.toErrorParcel
import uk.co.baconi.pka.td.settings.Settings
import uk.co.baconi.pka.tdb.AccessToken

class NoNreApiKeyException(message: String) : Exception(message)

fun AppCompatActivity.provideAccessToken(): Try<AccessToken> = Try {
    val nreApiKey = Settings.NreApiKey.getSetting(this)?.trim()
    if(nreApiKey.isNullOrEmpty()) {
        throw NoNreApiKeyException("Unable to find an NRE api key from the app settings.")
    } else {
        AccessToken(nreApiKey)
    }
}

/**
 * Start an activity in a more Kotlin dsl way.
 */
inline fun <reified A> Context.startActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, A::class.java).apply {
        block()
    })
}

/**
 * Start an error activity for a given Throwable.
 */
fun Context.startErrorActivity(throwable: Throwable) {
    startActivity<ErrorActivity> {
        putExtra(ErrorActivity.ERROR_PARCEL, throwable.toErrorParcel())
    }
}