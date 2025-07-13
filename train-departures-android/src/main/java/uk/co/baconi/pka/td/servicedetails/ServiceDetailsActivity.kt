package uk.co.baconi.pka.td.servicedetails

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.baconi.pka.common.AccessToken
import uk.co.baconi.pka.common.openldbws.details.ServiceDetails
import uk.co.baconi.pka.common.openldbws.requests.OpenLDBWSApi
import uk.co.baconi.pka.common.openldbws.services.CallingPoint
import uk.co.baconi.pka.common.openldbws.services.CallingPoints
import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.databinding.ActivityServiceDetailsBinding
import uk.co.baconi.pka.td.provideAccessToken
import uk.co.baconi.pka.td.startErrorActivity
import uk.co.baconi.pka.td.tables.updateRow

class ServiceDetailsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ServiceDetailsActivity"
        const val SERVICE_ID = "uk.co.baconi.pka.td.servicedetails.SERVICE_ID"

        private val transformCallPoints: (CallingPoints) -> Iterable<CallingPoint> = { previousCallingPoint ->
            previousCallingPoint.callingPoint ?: emptyList()
        }
    }

    private val openLDBWSApi = OpenLDBWSApi(HttpClient())

    private lateinit var binding: ActivityServiceDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityServiceDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.contentAppBarLayout.toolbar)

        runCatching {
            provideAccessToken()
        }.onSuccess { accessToken ->
            val serviceId = intent.getStringExtra(SERVICE_ID)
            searchForServiceDetails(accessToken, serviceId!!)
        }.onFailure { exception ->
            Log.e(TAG, "Unable to get access token", exception)
            startErrorActivity(exception)
        }

        // TODO - Implement a refresh layout to enable reloading with new data (to update the calling points)

        // TODO - Implement a back other than device back button?
    }

    private fun searchForServiceDetails(accessToken: AccessToken, serviceId: String) = GlobalScope.launch {
        runCatching {
            openLDBWSApi.getServiceDetails(accessToken, serviceId)
        }.onSuccess { results ->
            displayServiceDetailsView(results)
        }.onFailure { exception ->
            Log.e(TAG, "Unable to get service details", exception)
            startErrorActivity(exception)
        }
    }

    private fun displayServiceDetailsView(result: ServiceDetails) = GlobalScope.launch(Dispatchers.Main) {

        val details = binding.contentServiceDetails

        // Update service details layout
        updateRow(details.generatedAtRow, details.generatedAtValue, result.generatedAt)
        updateRow(details.serviceTypeRow, details.serviceTypeValue, result.serviceType)
        updateRow(details.locationRow, details.locationValue, result.locationName)
        updateRow(details.crsRow, details.crsValue, result.crs)
        updateRow(details.operatorRow, details.operatorValue, result.operator)
        updateRow(details.operatorCodeRow, details.operatorCodeValue, result.operatorCode)
        updateRow(details.rsidRow, details.rsidValue, result.rsid)
        updateRow(details.cancelledRow, details.cancelledValue, result.isCancelled)
        updateRow(details.cancelReasonRow, details.cancelReasonValue, result.cancelReason)
        updateRow(details.delayReasonRow, details.delayReasonValue, result.delayReason)
        updateRow(details.overdueMessageRow, details.overdueMessageValue, result.overdueMessage)
        updateRow(details.lengthRow, details.lengthValue, result.length)
        updateRow(details.detachFrontRow, details.detachFrontValue, result.detachFront)
        updateRow(details.reverseFormationRow, details.reverseFormationValue, result.isReverseFormation)
        updateRow(details.platformRow, details.platformValue, result.platform)
        updateRow(details.staRow, details.staValue, result.scheduledArrivalTime)
        updateRow(details.etaRow, details.etaValue, result.estimatedArrivalTime)
        updateRow(details.ataRow, details.ataValue, result.actualArrivalTime)
        updateRow(details.stdRow, details.stdValue, result.scheduledDepartureTime)
        updateRow(details.etdRow, details.etdValue, result.estimatedDepartureTime)
        updateRow(details.atdRow, details.atdValue, result.actualDepartureTime)

        // TODO - Improve if we ever start getting this data
        updateRow(details.adhocAlertsRow, result.adhocAlerts) { messages ->
            details.adhocAlertsValue.text = messages.joinToString("\n")
        }

        // TODO - Improve if we ever start getting this data
        updateRow(details.formationRow, details.formationValue, result.formation)

        val previousCallingPoints: List<CallingPoint> = result
            .previousCallingPoints
            ?.flatMap(transformCallPoints)
            ?: emptyList()

        val currentCallingPoint: CallingPoint = result.toCallingPoint()

        val subsequentCallingPoints: List<CallingPoint> = result
            .subsequentCallingPoints
            ?.flatMap(transformCallPoints)
            ?: emptyList()

        val allCallingPoints: List<CallingPoint> = previousCallingPoints
            .plus(currentCallingPoint)
            .plus(subsequentCallingPoints)

        binding.contentServiceDetails.serviceDetailsCallingPoints.apply {

            // Set the maximum number of rows
            rowCount = allCallingPoints.size + 1

            addView(::TextView) {
                text = context.getString(R.string.service_details_calling_points_schedule)
                setTypeface(typeface, Typeface.BOLD)
                addGridLayout(row = 0, column = 0)
            }

            addView(::Space) {
                addGridLayout(row = 0, column = 1)
            }

            addView(::TextView) {
                text = context.getString(R.string.service_details_calling_points_station)
                setTypeface(typeface, Typeface.BOLD)
                addGridLayout(row = 0, column = 2)
            }

            addView(::TextView) {
                text = context.getString(R.string.service_details_calling_points_status)
                setTypeface(typeface, Typeface.BOLD)
                addGridLayout(row = 0, column = 3)
            }

            addView(::TextView) {
                text = context.getString(R.string.service_details_calling_points_time)
                setTypeface(typeface, Typeface.BOLD)
                addGridLayout(row = 0, column = 4)
            }

            // Create the "tube map" for the calling points
            addView(::ImageView){
                setImageDrawable(CallingPointsDrawable(context, allCallingPoints))
                contentDescription = resources.getString(R.string.service_details_calling_points_image_description)
                updatePadding(top = 5, bottom = 5)
                addGridLayout(column = 1) {
                    rowSpec = GridLayout.spec(1, allCallingPoints.size) // Span across every row
                    setGravity(Gravity.FILL_VERTICAL)
                    width = resources.getDimension(R.dimen.service_details_calling_points_image_width).toInt()
                }
            }

            // Create each calling point on a new row
            allCallingPoints.forEachIndexed { index, (locationName, _, scheduledTime, estimatedTime, actualTime) ->

                val row = index + 1

                addView(::TextView) {
                    text = scheduledTime
                    setTypeface(typeface, Typeface.BOLD)
                    gravity = Gravity.CENTER_HORIZONTAL
                    addGridLayout(row, column = 0) {
                        setGravity(Gravity.FILL)
                    }
                }

                addView(::TextView) {
                    text = locationName
                    updatePadding(right = 25)
                    addGridLayout(row, column = 2) {
                        setGravity(Gravity.FILL)
                    }
                }

                addView(::TextView) {
                    text = if(estimatedTime == null) {
                        context.getString(R.string.service_details_calling_points_status_departed)
                    } else {
                        context.getString(R.string.service_details_calling_points_status_expected)
                    }
                    updatePadding(right = 25)
                    addGridLayout(row, column = 3) {
                        setGravity(Gravity.FILL)
                    }
                }

                addView(::TextView) {
                    text = estimatedTime ?: actualTime
                    addGridLayout(row, column = 4) {
                        setGravity(Gravity.FILL)
                    }
                }
            }
        }
    }

    private fun <A : View> ViewGroup.addView(factory: (Context) -> A, body: A.() -> Unit = {}) {
        val view = factory(context)
        view.body()
        addView(view)
    }

    private fun View.addGridLayout(row: Int = 0, column: Int = 0, body: GridLayout.LayoutParams.() -> Unit = {}) {
        layoutParams = GridLayout.LayoutParams().apply {
            rowSpec = GridLayout.spec(row)
            columnSpec = GridLayout.spec(column)
            body()
        }
    }

    private fun ServiceDetails.toCallingPoint(): CallingPoint = CallingPoint(
        locationName = locationName,
        crs = crs,
        scheduledTime = scheduledDepartureTime,
        estimatedTime = estimatedDepartureTime,
        actualTime = actualDepartureTime,
        isCancelled = isCancelled,
        length = length,
        detachFront = detachFront,
        formation = formation,
        adhocAlerts = adhocAlerts
    )
}