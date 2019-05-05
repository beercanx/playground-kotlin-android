package uk.co.baconi.pka.td

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Spinner

import kotlinx.android.synthetic.main.activity_departure_search.*
import kotlinx.android.synthetic.main.content_departure_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.Actions
import uk.co.baconi.pka.tdb.StationCode
import uk.co.baconi.pka.tdb.StationCodes
import uk.co.baconi.pka.tdb.openldbws.responses.DepartureItem
import uk.co.baconi.pka.tdb.openldbws.responses.ServiceItem
import java.util.*


class DepartureSearchActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DepartureSearchActivity"
    }

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var searchResults: MutableList<ServiceItem>
    private lateinit var viewAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context: Context = this

        if(!this::textToSpeech.isInitialized) {

            textToSpeech = TextToSpeech(context) {

                // On Init Listener

                textToSpeech.language = applicationContext.getCurrentLocale()

                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()

                textToSpeech.setAudioAttributes(audioAttributes)
            }
        }

        setContentView(R.layout.activity_departure_search)
        setSupportActionBar(toolbar)

        searchResults = mutableListOf()
        viewAdapter = SearchResultsAdapter(searchResults)

        search_results.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
        }

        search_results_refresh_layout.apply {
            setOnRefreshListener {
                startSearchForDepartures()
            }
        }

        // TODO - Look into search by both CRS Code and Station Name

        search_criteria_from_auto_complete.apply {
            adapter = SearchCriteriaAdapter(context, StationCodes.stationCodes)
            setSelectionByStationName("Meadowhall")
        }

        search_criteria_to_auto_complete.apply {
            adapter = SearchCriteriaAdapter(context, StationCodes.stationCodes)
            setSelectionByStationName("Sheffield")
        }
    }

    private fun Spinner.setSelectionByStationName(name: String) {
        setSelection(StationCodes.stationCodes.indexOfFirst(StationCodes.byName(name)))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.app_bar_preferences -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        R.id.app_bar_search -> {
            startSearchForDepartures()
            true
        }
        R.id.app_bar_toggle -> {
            toggleSearchCriteria()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Toggle the from and to search selections
    private fun toggleSearchCriteria() {
        val fromPosition = search_criteria_from_auto_complete.selectedItemPosition
        val toPosition = search_criteria_to_auto_complete.selectedItemPosition
        search_criteria_from_auto_complete.setSelection(toPosition)
        search_criteria_to_auto_complete.setSelection(fromPosition)
    }

    private fun startSearchForDepartures() {

        search_results_refresh_layout.isRefreshing = true

        when(val nreApiKey = Settings.NreApiKey.getSetting(this)) {
            is String -> searchForDepartures(nreApiKey.let(::AccessToken))
            else -> {
                Snackbar.make(search_results, "No NRE API Key set in the settings.", 5000).show()
            }
        }
    }

    private fun searchForDepartures(nreApiKey: AccessToken) = GlobalScope.launch {

        val from = search_criteria_from_auto_complete.selectedItem as StationCode
        val to = search_criteria_to_auto_complete.selectedItem as StationCode

        val result: List<ServiceItem>? = when(Settings.WhichSearchType.getSetting(this@DepartureSearchActivity)) {
            SearchType.SINGLE_RESULT -> {
                Actions.getFastestDepartures(nreApiKey, from, to)
                    ?.departuresBoard
                    ?.departures
                    ?.mapNotNull(DepartureItem::service)
            }
            SearchType.MULTIPLE_RESULTS -> {
                Actions.getDepartureBoard(nreApiKey, from, to)
                    ?.stationBoardResult
                    ?.trainServices
            }
        }

        if(result is List<ServiceItem>) {
            updateSearchResults(result)
            speakSearchResult(result.first())
        } else {
            // TODO - Better error messaging required
            Snackbar.make(search_results, "Unable to get a result from [${from.stationName}] to [${to.stationName}]", 10000).show()
        }
    }

    private fun updateSearchResults(serviceItems: List<ServiceItem>) = GlobalScope.launch(Dispatchers.Main) {
        searchResults.clear()
        searchResults.addAll(serviceItems)
        viewAdapter.notifyDataSetChanged()

        search_results_refresh_layout.isRefreshing = false
    }

    private fun speakSearchResult(service: ServiceItem) {

        if(Settings.EnableSpeakingFirstResult.getSetting(this)) {

            val speechText = service.tickerLine(this)

            // Can be used to detect errors during synthesis via setOnUtteranceProgressListener
            val utteranceId = UUID.randomUUID().toString()

            // TODO - Check error/success response for queueing speech request
            textToSpeech.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)

        } else {
            Log.d(TAG, "Speaking results is disabled")
        }
    }
}
