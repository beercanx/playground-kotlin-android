package uk.co.baconi.pka.td

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.android.synthetic.main.content_app_bar_layout.*
import kotlinx.android.synthetic.main.content_departure_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.baconi.pka.common.AccessToken
import uk.co.baconi.pka.common.openldbws.requests.DepartureBoardType
import uk.co.baconi.pka.common.openldbws.requests.DeparturesType
import uk.co.baconi.pka.common.openldbws.requests.OpenLDBWSApi
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.td.stations.OnStationSelectedListener
import uk.co.baconi.pka.td.stations.StationCodeAdapter
import uk.co.baconi.pka.td.stations.StationSelections
import uk.co.baconi.pka.td.settings.SearchType
import uk.co.baconi.pka.td.settings.Settings
import uk.co.baconi.pka.td.settings.SettingsActivity
import uk.co.baconi.pka.td.tts.configureContentType
import uk.co.baconi.pka.td.tts.configureFocusGain
import uk.co.baconi.pka.td.tts.createTextToSpeech
import java.util.*

class DepartureSearchActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DepartureSearchActivity"
    }

    private val openLDBWSApi = OpenLDBWSApi(HttpClient(OkHttp))
    private val stationSelections = StationSelections(this)

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var searchResults: MutableList<Service>
    private lateinit var viewAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure to be search layout
        setContentView(R.layout.activity_departure_search)
        setSupportActionBar(toolbar)

        // TODO - Extract into component and inject
        if(!this::textToSpeech.isInitialized) {
            textToSpeech = createTextToSpeech(this@DepartureSearchActivity).apply {
                configureContentType()
                configureFocusGain(this@DepartureSearchActivity)
            }
        }

        searchResults = mutableListOf()
        viewAdapter = SearchResultsAdapter(searchResults)

        search_results.apply {
            layoutManager = LinearLayoutManager(this@DepartureSearchActivity)
            adapter = viewAdapter
        }

        search_results_refresh_layout.apply {
            setOnRefreshListener {
                startSearchForDepartures()
            }
        }

        search_criteria_from_auto_complete.apply {
            setAdapter(StationCodeAdapter(this@DepartureSearchActivity))
            //setSelectionByStationCode(stationSelections.getStationSelectionFrom())
            onItemSelectedListener =
                OnStationSelectedListener(stationSelections::saveStationSelectionFrom)
        }

        search_criteria_to_auto_complete.apply {
            setAdapter(StationCodeAdapter(this@DepartureSearchActivity))
            //setSelectionByStationCode(stationSelections.getStationSelectionTo())
            onItemSelectedListener =
                OnStationSelectedListener(stationSelections::saveStationSelectionTo)
        }

        // TODO - Look into search by both CRS Code and Station Name
        // TODO - Create a pop up search view for a specific fake spinner
        // TODO - Populate search with station suggestions
        // TODO - Populate search with recent/recommended stations
    }

    override fun onDestroy() {
        super.onDestroy()
        if(this::textToSpeech.isInitialized) {
            textToSpeech.shutdown()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.app_bar_preferences -> {
            startActivity<SettingsActivity>()
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

    // TODO - Toggle the from and to search selections
    private fun toggleSearchCriteria() {
        //val fromPosition = stationSelections.getStationSelectionFrom()
        //val toPosition = stationSelections.getStationSelectionTo()
        //stationSelections.saveStationSelectionFrom(toPosition)
        //stationSelections.saveStationSelectionTo(fromPosition)
        //search_criteria_from_auto_complete.setSelectionByStationCode(toPosition)
        //search_criteria_to_auto_complete.setSelectionByStationCode(fromPosition)
    }

    private fun startSearchForDepartures() {

        // Turn on the spinner
        search_results_refresh_layout.isRefreshing = true

        runCatching {
            provideAccessToken()
        }.onSuccess { accessToken ->
            searchForDepartures(accessToken)
        }.onFailure { exception ->
            Log.e(TAG, "Unable to get access token", exception)
            handleError(exception)
        }
    }

    private fun searchForDepartures(accessToken: AccessToken) = GlobalScope.launch {

        // TODO - Make work
        //val from = search_criteria_from_auto_complete.selectedItem as StationCode
        //val to = search_criteria_to_auto_complete.selectedItem as StationCode

        val from = stationSelections.getStationSelectionFrom()
        val to = stationSelections.getStationSelectionTo()

        runCatching {
            when(Settings.WhichSearchType.getSetting(this@DepartureSearchActivity)) {
                SearchType.SINGLE_RESULT -> {
                    openLDBWSApi
                        .getDepartures(accessToken, from, to, DeparturesType.FastestDepartures)
                        .extractServices()
                }
                SearchType.MULTIPLE_RESULTS -> {
                    openLDBWSApi
                        .getDepartureBoard(accessToken, from, to, DepartureBoardType.DepartureBoard)
                        .extractServices()
                }
            }
        }.onSuccess { results ->
            displaySearchResultsView(results)
            if(results.isNotEmpty()) {
                speakSearchResult(results.first())
            }
        }.onFailure { exception ->
            Log.e(TAG, "Unable to search for departures", exception)
            handleError(exception)
        }
    }

    private fun handleError(error: Throwable) {

        // Turn off the spinner
        GlobalScope.launch(Dispatchers.Main) {
            search_results_refresh_layout.isRefreshing = false
        }

        // Start the error activity
        startErrorActivity(error)
    }

    private fun displaySearchResultsView(serviceItems: List<Service>) = GlobalScope.launch(Dispatchers.Main) {

        // Turn off the spinner
        search_results_refresh_layout.isRefreshing = false

        // Update the search results
        searchResults.clear()
        searchResults.addAll(serviceItems)
        viewAdapter.notifyDataSetChanged()
    }

    private fun speakSearchResult(service: Service) {

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
