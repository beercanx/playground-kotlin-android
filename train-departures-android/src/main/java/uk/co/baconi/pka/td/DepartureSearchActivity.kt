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
import android.widget.ArrayAdapter

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

import java.util.UUID


class DepartureSearchActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DepartureSearchActivity"
    }

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var searchResults: MutableList<ServiceItem>
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

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        search_results_refresh_layout.setOnRefreshListener {
            startSearchForDepartures(recyclerView)
        }

        val crsCodes = StationCodes.stationCodes.map(StationCode::crsCode).sorted()

        // TODO - Implement our own Adapter so we can store StationCode values and render with both name and code
        // TODO - Also look into supporting a filter/search option to help pick

        search_criteria_from_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, crsCodes)
        search_criteria_from_spinner.setSelection(crsCodes.indexOf("MHS")) // TODO - Retrieve from save

        search_criteria_to_spinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, crsCodes)
        search_criteria_to_spinner.setSelection(crsCodes.indexOf("SHF")) // TODO - Retrieve from save
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
            startSearchForDepartures(recyclerView)
            true
        }
        R.id.app_bar_toggle -> {
            toggleSearchCriteria()
            startSearchForDepartures(recyclerView)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Toggle the from and to search selections
    private fun toggleSearchCriteria() {
        val fromPosition = search_criteria_from_spinner.selectedItemPosition
        val toPosition = search_criteria_to_spinner.selectedItemPosition
        search_criteria_from_spinner.setSelection(toPosition, true)
        search_criteria_to_spinner.setSelection(fromPosition, true)
    }

    private fun startSearchForDepartures(view: View) {

        search_results_refresh_layout.isRefreshing = true

        when(val nreApiKey = Settings.NreApiKey.getSetting(this)) {
            is String -> searchForDepartures(nreApiKey.let(::AccessToken))
            else -> {
                Snackbar.make(view, "No NRE API Key set in the settings.", 5000).show()
            }
        }
    }

    private fun searchForDepartures(nreApiKey: AccessToken) = GlobalScope.launch {

        val from = StationCodes.firstByCode(search_criteria_from_spinner.selectedItem as String)
        val to = StationCodes.firstByCode(search_criteria_to_spinner.selectedItem as String)

        val searchType = Settings.WhichSearchType.getSetting(this@DepartureSearchActivity)

        val result: List<ServiceItem>? = when(searchType) {
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
            updateSearchResults(searchType, result)
            speakSearchResult(result.first())
        } else {
            // TODO - Better error messaging required
            Snackbar.make(recyclerView, "Unable to get a result", 10000).show()
        }
    }

    private fun updateSearchResults(searchType: SearchType, serviceItems: List<ServiceItem>) = GlobalScope.launch(Dispatchers.Main) {

        when(searchType) {
            SearchType.SINGLE_RESULT -> {
                if(searchResults.size >= 4) {
                    searchResults.clear()
                }
                searchResults.add(0, serviceItems.first())
            }
            SearchType.MULTIPLE_RESULTS -> {
                searchResults.clear()
                searchResults.addAll(serviceItems)
            }
        }

        viewAdapter.notifyDataSetChanged()

        search_results_refresh_layout.isRefreshing = false
    }

    private fun speakSearchResult(service: ServiceItem) {

        if(Settings.EnableSpeakingFirstResult.getSetting(this)) {

            val platform = service.platform
            val destinationName = service.destination?.first()?.locationName
            val departureTime = service.std

            val actualDepartureTime = when(service.etd) {
                null -> applicationContext.getString(R.string.search_result_etd_other, departureTime)
                "On time" -> applicationContext.getString(R.string.search_result_etd_on_time)
                "Delayed" -> applicationContext.getString(R.string.search_result_etd_delayed)
                else -> applicationContext.getString(R.string.search_result_etd_other, service.etd)
            }

            val speechText = applicationContext.getString(
                R.string.search_result_ticker_line, departureTime, destinationName, platform, actualDepartureTime
            )

            // Can be used to detect errors during synthesis via setOnUtteranceProgressListener
            val utteranceId = UUID.randomUUID().toString()

            textToSpeech.language = applicationContext.getCurrentLocale()

            // TODO - Check error/success response for queueing speech request
            textToSpeech.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)

        } else {
            Log.d(TAG, "Speaking results is disabled")
        }
    }
}
