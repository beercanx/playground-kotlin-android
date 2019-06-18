package uk.co.baconi.pka.td

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import arrow.core.Failure
import arrow.core.Success
import arrow.core.Try
import kotlinx.android.synthetic.main.activity_departure_search.*
import kotlinx.android.synthetic.main.content_departure_search.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.baconi.pka.td.errors.ErrorView
import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.Actions
import uk.co.baconi.pka.tdb.StationCode
import uk.co.baconi.pka.tdb.StationCodes
import uk.co.baconi.pka.tdb.openldbws.responses.ServiceItem
import java.util.*


class DepartureSearchActivity : AppCompatActivity(), ErrorView {

    companion object {
        private const val TAG = "DepartureSearchActivity"
        private const val FROM_NAME = "$TAG.FROM_NAME"
        private const val TO_NAME = "$TAG.TO_NAME"
    }

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var searchResults: MutableList<ServiceItem>
    private lateinit var viewAdapter: SearchResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context: Context = this

        // TODO - Extract into component and inject
        if(!this::textToSpeech.isInitialized) {

            textToSpeech = TextToSpeech(context) {
                textToSpeech.language = applicationContext.getCurrentLocale()
            }

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()

            textToSpeech.setAudioAttributes(audioAttributes)

            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            // TODO - Work out how we do it for older devices
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

                    val focusRequests = mutableMapOf<String, AudioFocusRequest>()

                    override fun onStart(utteranceId: String) {

                        val focusGain = when(Settings.WhichSpeechType.getSetting(context)) {
                            SpeechType.PAUSE_OTHER_SOUNDS -> AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE
                            SpeechType.DIM_OTHER_SOUNDS -> AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
                        }

                        val audioFocusRequest = AudioFocusRequest
                            .Builder(focusGain)
                            .build()

                        focusRequests[utteranceId] = audioFocusRequest

                        audioManager.requestAudioFocus(audioFocusRequest)
                    }

                    override fun onDone(utteranceId: String) {
                        val focusRequest = focusRequests[utteranceId]
                        if(focusRequest != null) {
                            audioManager.abandonAudioFocusRequest(focusRequest)
                        }
                    }

                    override fun onStop(utteranceId: String, interrupted: Boolean) {
                        onDone(utteranceId)
                    }

                    override fun onError(utteranceId: String) {
                        onDone(utteranceId)
                    }
                })
            }
        }

        // Configure to be search layout
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

        val fromIntent: String? = intent.getStringExtra(FROM_NAME)
        val toIntent: String? = intent.getStringExtra(TO_NAME)

        search_criteria_from_auto_complete.apply {
            adapter = SearchCriteriaAdapter(context, StationCodes.stationCodes)
            setSelectionByStationName(fromIntent ?: "Meadowhall")
        }

        search_criteria_to_auto_complete.apply {
            adapter = SearchCriteriaAdapter(context, StationCodes.stationCodes)
            setSelectionByStationName(toIntent ?: "Sheffield")
        }

        // Start a search because we've triggered via the menu
        if(fromIntent != null && toIntent != null) {
            startSearchForDepartures()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(this::textToSpeech.isInitialized) {
            textToSpeech.shutdown()
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
            // startSearchForDepartures()
            startActivity(Intent(this, DepartureSearchActivity::class.java).apply {

                val from = search_criteria_from_auto_complete.selectedItem as StationCode
                putExtra(FROM_NAME, from.stationName)

                val to = search_criteria_to_auto_complete.selectedItem as StationCode
                putExtra(TO_NAME, to.stationName)
            })
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

        // Turn on the spinner
        search_results_refresh_layout.isRefreshing = true

        when(val accessToken = provideAccessToken()) {
            is Try.Success<AccessToken> -> {
                searchForDepartures(accessToken.value)
            }
            is Try.Failure -> {
                Log.e(TAG, "Unable to get access token", accessToken.exception)
                handleErrorView(accessToken.exception)
            }
        }
    }

    private fun searchForDepartures(accessToken: AccessToken)  = GlobalScope.launch {

        val from = search_criteria_from_auto_complete.selectedItem as StationCode
        val to = search_criteria_to_auto_complete.selectedItem as StationCode

        val action = when(Settings.WhichSearchType.getSetting(this@DepartureSearchActivity)) {
            SearchType.SINGLE_RESULT -> {
                Actions.Companion::getFastestTrainDeparture
            }
            SearchType.MULTIPLE_RESULTS -> {
                Actions.Companion::getTrainDepartures
            }
        }

        when(val results = action.invoke(accessToken, from, to)) {
            is Success -> {
                displaySearchResultsView(results.value)
                speakSearchResult(results.value.first())
            }
            is Failure -> {
                Log.e(TAG, "Unable to search for departures", results.exception)
                handleErrorView(results.exception)
            }
        }
    }

    private fun handleErrorView(error: Throwable?) = GlobalScope.launch(Dispatchers.Main)  {

        // Turn off the spinner
        search_results_refresh_layout.isRefreshing = false

        // Update the error view
        displayErrorView(error)
    }

    private fun displaySearchResultsView(serviceItems: List<ServiceItem>) = GlobalScope.launch(Dispatchers.Main) {

        // Turn off the spinner
        search_results_refresh_layout.isRefreshing = false

        // Update the search results
        searchResults.clear()
        searchResults.addAll(serviceItems)
        viewAdapter.notifyDataSetChanged()
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
