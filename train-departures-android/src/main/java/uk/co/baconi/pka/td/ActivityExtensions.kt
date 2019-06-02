package uk.co.baconi.pka.td

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import uk.co.baconi.pka.tdb.AccessToken

fun AppCompatActivity.provideAccessToken(view: View, handler: (AccessToken) -> Unit) {
    when(val nreApiKey = Settings.NreApiKey.getSetting(this)) {
        is String -> handler(AccessToken(nreApiKey))
        else -> {
            Snackbar.make(view, "No NRE API Key set in the settings.", 5000).show()
        }
    }
}