package uk.co.baconi.pka.td

import uk.co.baconi.pka.td.DepartureStatus.*
import uk.co.baconi.pka.tdb.openldbws.responses.CallingPoint
import uk.co.baconi.pka.tdb.openldbws.responses.ServiceItem

enum class DepartureStatus {
    NO_REPORT,
    ON_TIME,
    DELAYED,
    CANCELLED,
    HH_MM
}

private fun parseDeparture(departure: String?) = when(departure) {
    null -> null
    "No report" -> NO_REPORT
    "On time" -> ON_TIME
    "Delayed" -> DELAYED
    "Cancelled" -> CANCELLED
    else -> HH_MM
}

val CallingPoint.departureStatus
    get() = parseDeparture(estimatedTime ?: actualTime)

val ServiceItem.departureStatus
    get() = parseDeparture(etd)