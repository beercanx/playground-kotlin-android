package uk.co.baconi.pka.td

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import arrow.core.Try
import uk.co.baconi.pka.tdb.AccessToken

@Deprecated("Since 2019/06/18, move to using provideAccessToken()")
fun AppCompatActivity.provideAccessToken(view: View, handler: (AccessToken) -> Unit) {
    when(val nreApiKey = Settings.NreApiKey.getSetting(this)) {
        is String -> handler(AccessToken(nreApiKey))
        else -> {
            Snackbar.make(view, "No NRE API Key set in the settings.", 5000).show()
        }
    }
}

class NoNreApiKeyException(message: String) : Exception(message)

fun AppCompatActivity.provideAccessToken(): Try<AccessToken> = Try {
    val nreApiKey = Settings.NreApiKey.getSetting(this)?.trim()
    if(nreApiKey.isNullOrEmpty()) {
        throw NoNreApiKeyException("Unable to find an NRE api key from the app settings.")
    } else {
        AccessToken(nreApiKey)
    }
}