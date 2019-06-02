package uk.co.baconi.pka.td.servicedetails

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import arrow.core.Try.Failure
import arrow.core.Try.Success
import kotlinx.android.synthetic.main.activity_service_details.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.provideAccessToken
import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.Actions
import uk.co.baconi.pka.tdb.openldbws.responses.servicedetails.ServiceDetailsResult

class ServiceDetailsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ServiceDetailsActivity"
        const val SERVICE_ID = "uk.co.baconi.pka.td.servicedetails.SERVICE_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)
        setSupportActionBar(toolbar)

        provideAccessToken(search_results_refresh_layout) { accessToken ->
            val serviceId = intent.getStringExtra(SERVICE_ID)
            searchForServiceDetails(accessToken, serviceId)
        }

        // TODO - Implement a back other than device back button?
    }

    private fun searchForServiceDetails(accessToken: AccessToken, serviceId: String) = GlobalScope.launch {
        when(val results = Actions.getServiceDetails(accessToken, serviceId)) {
            is Success<ServiceDetailsResult> -> {

                // TODO - updateView(results.value)

                Snackbar.make(
                    search_results_refresh_layout,
                    "Got result for ${results.value.locationName} at ${results.value.platform}",
                    5000
                ).show()
            }
            is Failure -> {
                Log.e(TAG, "Unable to get service details", results.exception)
                Snackbar.make(
                    search_results_refresh_layout,
                    "Problem getting service details: ${results.exception.javaClass}",
                    5000
                ).show()
            }
        }
    }
}