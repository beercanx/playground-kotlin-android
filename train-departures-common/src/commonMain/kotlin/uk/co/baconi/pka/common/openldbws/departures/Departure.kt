package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.openldbws.services.Service

data class Departure(
    val crs: String? = null,
    val service: Service? = null
)