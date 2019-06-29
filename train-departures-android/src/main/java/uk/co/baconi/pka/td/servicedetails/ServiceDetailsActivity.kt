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
import arrow.core.Try.Failure
import arrow.core.Try.Success
import kotlinx.android.synthetic.main.content_app_bar_layout.*
import kotlinx.android.synthetic.main.content_service_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.provideAccessToken
import uk.co.baconi.pka.td.startErrorActivity
import uk.co.baconi.pka.td.tables.updateRow
import uk.co.baconi.pka.tdb.AccessToken
import uk.co.baconi.pka.tdb.Actions
import uk.co.baconi.pka.tdb.openldbws.responses.CallingPoint
import uk.co.baconi.pka.tdb.openldbws.responses.CallingPoints
import uk.co.baconi.pka.tdb.openldbws.responses.servicedetails.ServiceDetailsResult

class ServiceDetailsActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ServiceDetailsActivity"
        const val SERVICE_ID = "uk.co.baconi.pka.td.servicedetails.SERVICE_ID"

        private val transformCallPoints: (CallingPoints) -> Iterable<CallingPoint> = { previousCallingPoint ->
            previousCallingPoint.callingPoint ?: emptyList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure to be service details layout
        setContentView(R.layout.activity_service_details)
        setSupportActionBar(toolbar)

        when(val accessToken = provideAccessToken()) {
            is Success<AccessToken> -> {
                val serviceId = intent.getStringExtra(SERVICE_ID)
                searchForServiceDetails(accessToken.value, serviceId)
            }
            is Failure -> {
                Log.e(TAG, "Unable to get access token", accessToken.exception)
                startErrorActivity(accessToken.exception)
            }
        }

        // TODO - Implement a refresh layout to enable reloading with new data (to update the calling points)

        // TODO - Implement a back other than device back button?
    }

    private fun searchForServiceDetails(accessToken: AccessToken, serviceId: String) = GlobalScope.launch {
        when(val results = Actions.getServiceDetails(accessToken, serviceId)) {
            is Success<ServiceDetailsResult> -> {
                displayServiceDetailsView(results.value)
            }
            is Failure -> {
                Log.e(TAG, "Unable to get service details", results.exception)
                startErrorActivity(results.exception)
            }
        }
    }

    private fun displayServiceDetailsView(result: ServiceDetailsResult) = GlobalScope.launch(Dispatchers.Main) {

        // Update service details layout
        updateRow(generated_at_row, generated_at_value, result.generatedAt)
        updateRow(service_type_row, service_type_value, result.serviceType)
        updateRow(location_row, location_value, result.locationName)
        updateRow(crs_row, crs_value, result.crs)
        updateRow(operator_row, operator_value, result.operator)
        updateRow(operator_code_row, operator_code_value, result.operatorCode)
        updateRow(rsid_row, rsid_value, result.rsid)
        updateRow(cancelled_row, cancelled_value, result.isCancelled)
        updateRow(cancel_reason_row, cancel_reason_value, result.cancelReason)
        updateRow(delay_reason_row, delay_reason_value, result.delayReason)
        updateRow(overdue_message_row, overdue_message_value, result.overdueMessage)
        updateRow(length_row, length_value, result.length)
        updateRow(detach_front_row, detach_front_value, result.detachFront)
        updateRow(reverse_formation_row, reverse_formation_value, result.isReverseFormation)
        updateRow(platform_row, platform_value, result.platform)
        updateRow(sta_row, sta_value, result.sta)
        updateRow(eta_row, eta_value, result.eta)
        updateRow(ata_row, ata_value, result.ata)
        updateRow(std_row, std_value, result.std)
        updateRow(etd_row, etd_value, result.etd)
        updateRow(atd_row, atd_value, result.atd)

        // TODO - Improve if we ever start getting this data
        updateRow(adhoc_alerts_row, result.adhocAlerts) { messages ->
            adhoc_alerts_value.text = messages.joinToString("\n")
        }

        // TODO - Improve if we ever start getting this data
        updateRow(formation_row, formation_value, result.formation)

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

        service_details_calling_points.apply {

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

    private fun ServiceDetailsResult.toCallingPoint(): CallingPoint = CallingPoint(
        locationName = locationName,
        crs = crs,
        scheduledTime = std,
        estimatedTime = etd,
        actualTime = atd,
        isCancelled = isCancelled,
        length = length,
        detachFront = detachFront,
        formation = formation,
        adhocAlerts = adhocAlerts
    )
}