package uk.co.baconi.pka.td

import android.content.Context
import uk.co.baconi.pka.tdb.openldbws.responses.ServiceItem

fun ServiceItem.tickerLine(context: Context): String = when(platform) {
    is String -> when(etd) {
        null -> context.getString(R.string.search_result_ticker_line_etd_other, std, destinationName, platform, std)
        "On time" -> context.getString(R.string.search_result_ticker_line_etd_on_time, std, destinationName, platform)
        "Delayed" -> context.getString(R.string.search_result_ticker_line_etd_delayed, std, destinationName, platform)
        else -> context.getString(R.string.search_result_ticker_line_etd_other, std, destinationName, platform, etd)
    }
    else -> when(etd) {
        null -> context.getString(R.string.search_result_ticker_line_no_platform_etd_other, std, destinationName, std)
        "On time" -> context.getString(R.string.search_result_ticker_line_no_platform_etd_on_time, std, destinationName)
        "Delayed" -> context.getString(R.string.search_result_ticker_line_no_platform_etd_delayed, std, destinationName)
        else -> context.getString(R.string.search_result_ticker_line_no_platform_etd_other, std, destinationName, etd)
    }
}

private val ServiceItem.destinationName
    get() = destination?.first()?.locationName