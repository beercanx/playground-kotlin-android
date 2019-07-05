package uk.co.baconi.pka.td

import android.content.Context
import uk.co.baconi.pka.td.DepartureStatus.*
import uk.co.baconi.pka.tdb.openldbws.responses.ServiceItem

fun ServiceItem.tickerLine(context: Context): String = when(platform) {
    is String -> when(departureStatus) {
        ON_TIME -> context.getString(R.string.search_result_ticker_line_etd_on_time, std, destinationName, platform)
        DELAYED -> context.getString(R.string.search_result_ticker_line_etd_delayed, std, destinationName, platform)
        CANCELLED -> context.getString(R.string.search_result_ticker_line_with_platform_cancelled, std, destinationName, platform)
        else -> context.getString(R.string.search_result_ticker_line_etd_other, std, destinationName, platform, etd)
    }
    else -> when(departureStatus) {
        ON_TIME -> context.getString(R.string.search_result_ticker_line_no_platform_etd_on_time, std, destinationName)
        DELAYED -> context.getString(R.string.search_result_ticker_line_no_platform_etd_delayed, std, destinationName)
        CANCELLED -> context.getString(R.string.search_result_ticker_line_no_platform_cancelled, std, destinationName)
        else -> context.getString(R.string.search_result_ticker_line_no_platform_etd_other, std, destinationName, etd)
    }
}

private val ServiceItem.destinationName
    get() = destination?.first()?.locationName