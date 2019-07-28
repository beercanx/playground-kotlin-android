package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.xml.XmlDeserializer

data class Departures(
    val generatedAt: String? = null,
    val locationName: String? = null,
    val crs: String? = null,
    val filterLocationName: String? = null,
    val filterCrs: String? = null,
    val filterType: String? = null,
    val nrccMessages: List<String>? = null,
    val platformAvailable: Boolean? = null,
    val areServicesAvailable: Boolean? = null,
    val departures: List<Departure>? = null
) {

    companion object {
        fun XmlDeserializer.departures(): Departures = Departures() // TODO - Implement
    }

    fun extractServices(): List<Service> = departures?.mapNotNull(Departure::service) ?: emptyList()

}
