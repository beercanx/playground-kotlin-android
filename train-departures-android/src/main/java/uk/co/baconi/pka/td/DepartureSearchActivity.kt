package uk.co.baconi.pka.td

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_departure_search.toolbar
import kotlinx.android.synthetic.main.activity_departure_search.floating_search_button
import kotlinx.android.synthetic.main.content_departure_search.search_results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.Actions
import uk.co.baconi.pka.tdb.StationCodes
import uk.co.baconi.pka.tdb.openldbws.responses.BaseDeparturesResponse

import java.util.*

class DepartureSearchActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DepartureSearchActivity"
    }

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var searchResults: MutableList<BaseDeparturesResponse>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: SearchResultsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!this::textToSpeech.isInitialized) {
            textToSpeech = TextToSpeech(this) {}
        }

        setContentView(R.layout.activity_departure_search)
        setSupportActionBar(toolbar)

        searchResults = mutableListOf()
        viewManager = LinearLayoutManager(this)
        viewAdapter = SearchResultsAdapter(searchResults)
        recyclerView = search_results.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        floating_search_button.setOnClickListener { view ->
            when(val nreApiKey = Settings.NreApiKey.getSetting(this)) {
                is String -> searchForDepartures(nreApiKey, view)
                else -> {
                    Snackbar
                        .make(view, "No NRE API Key set in the settings.", 5000)
                        .setAction("Action", null).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.app_bar_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        R.id.app_bar_search -> {
            Snackbar
                .make(item.actionView, "No NRE API Key set in the settings.", 5000)
                .setAction("Action", null).show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun updateSearchResults(response: BaseDeparturesResponse) = GlobalScope.launch(Dispatchers.Main) {
        searchResults.add(0, response)
        viewAdapter.notifyDataSetChanged()
    }

    private fun speakSearchResult(result: BaseDeparturesResponse) {

        if(Settings.EnableSpeakingFirstResult.getSetting(this)) {

            val departuresBoard = result.departuresBoard
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

            val speechText = "The $departureTime to $destination on platform $platform $actualDepartureTime"

            // Can be used to detect errors during synthesis via setOnUtteranceProgressListener
            val utteranceId = UUID.randomUUID().toString()

            textToSpeech.language = Locale.UK

            // TODO - Check error/success response for queueing speech request
            textToSpeech.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)

        } else {
            Log.d(TAG, "Speaking results is disabled")
        }
    }

    private fun searchForDepartures(nreApiKey: String, view: View) = GlobalScope.launch {

        val from = StationCodes.firstByCode("SHF")
        val to = StationCodes.firstByCode("MHS")

        val result = Actions.getFastestDepartures(nreApiKey.let(::AccessToken), from, to)

        if(result is BaseDeparturesResponse) {
            updateSearchResults(result)
            speakSearchResult(result)
        } else {
            // TODO - Better error messaging required
            Snackbar
                .make(view, "Unable to get a result", 10000)
                .setAction("Action", null).show()
        }
    }
}
