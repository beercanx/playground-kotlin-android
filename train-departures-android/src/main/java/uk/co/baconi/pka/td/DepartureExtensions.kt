package uk.co.baconi.pka.td

import uk.co.baconi.pka.common.openldbws.services.CallingPoint
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.td.DepartureStatus.*

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

val Service.departureStatus
    get() = parseDeparture(estimatedDepartureTime)