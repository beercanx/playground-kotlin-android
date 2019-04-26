package uk.co.baconi.pka.td

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import gr.escsoft.michaelprimez.searchablespinner.interfaces.ISpinnerSelectedView
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener

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
    private lateinit var viewAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context: Context = this

        if(!this::textToSpeech.isInitialized) {
            textToSpeech = TextToSpeech(context) {}
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

        val stationNames: List<String> = StationCodes.stationCodes.map(StationCode::stationName).sorted()

        // TODO - Implement our own Adapter so we can store StationCode values and render with both name and code
        // TODO - Refactor into something more useful
        // TODO - Look into search by both CRS Code and Station Name
        val spinnerSelectedView = object: ArrayAdapter<String>(
            context, android.R.layout.simple_dropdown_item_1line, stationNames
        ), ISpinnerSelectedView {
            private val layoutInflater = LayoutInflater.from(context)
            override fun getSelectedView(position: Int): View {
                val view = layoutInflater.inflate(android.R.layout.simple_dropdown_item_1line, null) as TextView
                view.text = getItem(position)
                return view
            }
            override fun getNoSelectionView(): View {
                return layoutInflater.inflate(android.R.layout.simple_dropdown_item_1line, null)
            }
        }

        search_criteria_from_auto_complete.apply {
            setAdapter(spinnerSelectedView)
            selectedItem = "Meadowhall"
            setStatusListener(object : IStatusListener {
                override fun spinnerIsOpening() {
                    search_criteria_to_auto_complete.hideEdit()
                }
                override fun spinnerIsClosing() {
                }
            })
        }

        search_criteria_to_auto_complete.apply {
            setAdapter(spinnerSelectedView)
            selectedItem = "Sheffield"
            setStatusListener(object : IStatusListener {
                override fun spinnerIsOpening() {
                    search_criteria_from_auto_complete.hideEdit()
                }
                override fun spinnerIsClosing() {
                }
            })
        }

        // TODO - Look for a better solution
        search_results_refresh_layout.setOnTouchListener { _, event ->
            if (!search_criteria_from_auto_complete.isInsideSearchEditText(event)) {
                search_criteria_from_auto_complete.hideEdit()
            }
            if (!search_criteria_to_auto_complete.isInsideSearchEditText(event)) {
                search_criteria_to_auto_complete.hideEdit()
            }
            false
        }
    }

    // TODO - Look for a better solution
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!search_criteria_from_auto_complete.isInsideSearchEditText(event)) {
            search_criteria_from_auto_complete.hideEdit()
        }
        if (!search_criteria_to_auto_complete.isInsideSearchEditText(event)) {
            search_criteria_to_auto_complete.hideEdit()
        }
        return super.onTouchEvent(event)
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
        val fromPosition = search_criteria_from_auto_complete.selectedPosition
        val toPosition = search_criteria_to_auto_complete.selectedPosition
        search_criteria_from_auto_complete.setSelectedItem(toPosition)
        search_criteria_to_auto_complete.setSelectedItem(fromPosition)
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

        val from = StationCodes.firstByName(search_criteria_from_auto_complete.selectedItem as String)
        val to = StationCodes.firstByName(search_criteria_to_auto_complete.selectedItem as String)

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
            Snackbar.make(search_results, "Unable to get a result", 10000).show()
        }

        search_results_refresh_layout.isRefreshing = false
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
