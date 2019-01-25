package uk.co.baconi.pka.td

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_departure_search.fab
import kotlinx.android.synthetic.main.activity_departure_search.settings
import kotlinx.android.synthetic.main.activity_departure_search.toolbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.baconi.pka.tdb.AccessToken

import uk.co.baconi.pka.tdb.Http
import uk.co.baconi.pka.tdb.StationCodes
import uk.co.baconi.pka.tdb.openldbws.responses.BodySuccess

class DepartureSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_departure_search)
        setSupportActionBar(toolbar)

        settings.setOnClickListener { view ->
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        fab.setOnClickListener { view ->

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

            GlobalScope.launch {

                val departuresBoard = sharedPreferences.getString("nre_api_key", null)?.let(::AccessToken)?.let { apiKey ->
                    val from = StationCodes.firstByCode("SHF")
                    val to = StationCodes.firstByCode("MHS")
                    (Http.performGetNextDeparturesRequest(apiKey, from, to).body as BodySuccess).getNextDeparturesResponse?.departuresBoard
                }

                val service = departuresBoard?.departures?.first()?.service

                val platform = service?.platform
                val destination = service?.destination?.first()?.locationName
                val departureTime = service?.std
                val estimatedDepartureTime = service?.etd

                val actualTime = if(estimatedDepartureTime == "On time") {
                    "is on time"
                } else {
                    "is expected at $estimatedDepartureTime"
                }

                Snackbar
                    .make(view, "The $departureTime to $destination on platform $platform $actualTime", 10000)
                    .setAction("Action", null).show()
            }
        }
    }
}
