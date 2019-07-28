package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.xml.XmlDeserializer

data class DepartureBoard(
    val generatedAt: String? = null,
    val locationName: String? = null,
    val crs: String? = null,
    val filterLocationName: String? = null,
    val filterCrs: String? = null,
    val filterType: String? = null,
    val nrccMessages: List<String>? = null,
    val platformAvailable: Boolean? = null,
    val areServicesAvailable: Boolean? = null,
    val trainServices: List<Service>? = null,
    val busServices: List<Service>? = null,
    val ferryServices: List<Service>? = null
) {
    companion object {
        fun XmlDeserializer.departureBoard(): DepartureBoard = DepartureBoard() // TODO - Implement
    }
}