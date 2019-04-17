package uk.co.baconi.pka.td

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View

import kotlinx.android.synthetic.main.activity_departure_search.fab
import kotlinx.android.synthetic.main.activity_departure_search.settings
import kotlinx.android.synthetic.main.activity_departure_search.toolbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.Actions
import uk.co.baconi.pka.tdb.StationCodes

import java.util.*

class DepartureSearchActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!this::textToSpeech.isInitialized) {
            textToSpeech = TextToSpeech(this) {}
        }

        setContentView(R.layout.activity_departure_search)
        setSupportActionBar(toolbar)

        settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        fab.setOnClickListener { view ->

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val nreApiKey = sharedPreferences.getString("nre_api_key", null)

            when(nreApiKey) {
                is String -> searchForDepartures(nreApiKey, view)
                else -> {
                    Snackbar
                        .make(view, "No NRE API Key set in the settings.", 5000)
                        .setAction("Action", null).show()
                }
            }
        }
    }

    private fun searchForDepartures(nreApiKey: String, view: View) {

        GlobalScope.launch {

            val from = StationCodes.firstByCode("SHF")
            val to = StationCodes.firstByCode("MHS")

            val actionResult = Actions.getFastestDepartures(nreApiKey.let(::AccessToken), from, to)

            val departuresBoard = actionResult?.departuresBoard
            val service = departuresBoard?.departures?.first()?.service
            val platform = service?.platform
            val destination = service?.destination?.first()?.locationName
            val departureTime = service?.std
            val estimatedDepartureTime = service?.etd

            val actualDepartureTime = when(estimatedDepartureTime) {
                null -> "no departure time"
                "On time" -> "is on time"
                "Delayed" -> "is delayed"
                else -> "is expected at $estimatedDepartureTime"
            }

            val resultDisplay = "The $departureTime to $destination on platform $platform $actualDepartureTime"

            Snackbar
                .make(view, resultDisplay, 10000)
                .setAction("Action", null).show()

            // Can be used to detect errors during synthesis via setOnUtteranceProgressListener
            val utteranceId = UUID.randomUUID().toString()

            textToSpeech.language = Locale.UK
            textToSpeech.speak(resultDisplay, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        }
    }
}
