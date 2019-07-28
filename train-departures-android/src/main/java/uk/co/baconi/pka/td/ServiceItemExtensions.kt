package uk.co.baconi.pka.td

import android.content.Context
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.td.DepartureStatus.*

fun Service.tickerLine(context: Context): String = when(platform) {
    is String -> when(departureStatus) {
        ON_TIME -> context.getString(R.string.search_result_ticker_line_etd_on_time, scheduledDepartureTime, destinationName, platform)
        DELAYED -> context.getString(R.string.search_result_ticker_line_etd_delayed, scheduledDepartureTime, destinationName, platform)
        CANCELLED -> context.getString(R.string.search_result_ticker_line_with_platform_cancelled, scheduledDepartureTime, destinationName, platform)
        else -> context.getString(R.string.search_result_ticker_line_etd_other, scheduledDepartureTime, destinationName, platform, estimatedDepartureTime)
    }
    else -> when(departureStatus) {
        ON_TIME -> context.getString(R.string.search_result_ticker_line_no_platform_etd_on_time, scheduledDepartureTime, destinationName)
        DELAYED -> context.getString(R.string.search_result_ticker_line_no_platform_etd_delayed, scheduledDepartureTime, destinationName)
        CANCELLED -> context.getString(R.string.search_result_ticker_line_no_platform_cancelled, scheduledDepartureTime, destinationName)
        else -> context.getString(R.string.search_result_ticker_line_no_platform_etd_other, scheduledDepartureTime, destinationName, estimatedDepartureTime)
    }
}

private val Service.destinationName
    get() = destination?.first()?.locationName