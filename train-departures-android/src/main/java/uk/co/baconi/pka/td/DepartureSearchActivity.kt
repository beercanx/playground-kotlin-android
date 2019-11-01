package uk.co.baconi.pka.td

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.android.synthetic.main.content_app_bar_layout.*
import kotlinx.android.synthetic.main.content_departure_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.baconi.pka.common.AccessToken
import uk.co.baconi.pka.common.StationCode
import uk.co.baconi.pka.common.openldbws.requests.DepartureBoardType
import uk.co.baconi.pka.common.openldbws.requests.DeparturesType
import uk.co.baconi.pka.common.openldbws.requests.OpenLDBWSApi
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.td.depatures.StationSelections
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

        // TODO - Look into search by both CRS Code and Station Name
        // TODO - Create a pop up search view for a specific fake spinner
        station_search_layout.isVisible = false
        station_search_view.isSubmitButtonEnabled = true
        station_search_view.isQueryRefinementEnabled = true
        setupStationSelectors(search_criteria_from_auto_complete, stationSelections::getStationSelectionFrom, stationSelections::saveStationSelectionFrom)
        setupStationSelectors(search_criteria_to_auto_complete, stationSelections::getStationSelectionTo, stationSelections::saveStationSelectionTo)
        // TODO - Populate search with station suggestions
        // TODO - Populate search with recent/recommended stations

        // TODO - Look into auto updating based on changes to properties
        updateStationSelections()
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

    private fun getMenuItem(@IdRes id: Int): MenuItem = toolbar.menu.findItem(id)

    private fun setupStationSelectors(view: View, get: () -> StationCode, save: (StationCode) -> Unit) = view.apply {

        val onClickListener = View.OnClickListener {
            Log.d(TAG, "Station selection clicked")
            // TODO - Open and overlay search that only returns on cancelling, aborting or selection from the results.
            getMenuItem(R.id.app_bar_search).isVisible = false
            getMenuItem(R.id.app_bar_toggle).isVisible = false
            departure_search_criteria.isVisible = false
            search_results_refresh_layout.isVisible = false
            station_search_layout.isVisible = true
            // TODO - On Suggestion Click Listener
        }

        findViewById<TextView>(R.id.search_criteria_station_avatar).setOnClickListener(onClickListener)
        findViewById<TextView>(R.id.search_criteria_station_name).setOnClickListener(onClickListener)
    }

    // Update the layouts for station selection
    private fun updateStationSelections() {
        updateStationSelection(search_criteria_from_auto_complete, stationSelections.getStationSelectionFrom())
        updateStationSelection(search_criteria_to_auto_complete, stationSelections.getStationSelectionTo())
    }

    // Update the layout that fakes a spinner with the latest selection
    private fun updateStationSelection(view: View, selection: StationCode) = view.apply {
        findViewById<TextView>(R.id.search_criteria_station_avatar)?.text = selection.crsCode
        findViewById<TextView>(R.id.search_criteria_station_name)?.text = selection.stationName
    }

    // Toggle the from and to search selections
    private fun toggleSearchCriteria() {
        val fromPosition = stationSelections.getStationSelectionFrom()
        val toPosition = stationSelections.getStationSelectionTo()
        stationSelections.saveStationSelectionFrom(toPosition)
        stationSelections.saveStationSelectionTo(fromPosition)
        updateStationSelections()
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
