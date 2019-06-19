package uk.co.baconi.pka.td

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
 * Start an error activity for a given Throwable
 */
fun AppCompatActivity.startErrorActivity(throwable: Throwable) {
    startActivity(Intent(this, ErrorActivity::class.java).apply {
        putExtra(ErrorActivity.ERROR_PARCEL, throwable.toErrorParcel())
    })
}