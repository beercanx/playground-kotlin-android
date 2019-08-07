package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.openldbws.departures.Destination.Companion.destinations
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.soap.nrccMessages
import uk.co.baconi.pka.common.xml.*

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
    val destinations: List<Destination>? = null
) {

    companion object {
        fun XmlDeserializer.departures(): Departures = parse("DeparturesBoard") { result ->
            when (getName()) {
                "generatedAt" -> result.copy(generatedAt = readAsText())
                "locationName" -> result.copy(locationName = readAsText())
                "crs" -> result.copy(crs = readAsText())
                "filterLocationName" -> result.copy(filterLocationName = readAsText())
                "filtercrs" -> result.copy(filterCrs = readAsText())
                "filterType" -> result.copy(filterType = readAsText())
                "nrccMessages" -> result.copy(nrccMessages = nrccMessages())
                "platformAvailable" -> result.copy(platformAvailable = readAsBoolean())
                "areServicesAvailable" -> result.copy(areServicesAvailable = readAsBoolean())
                "departures" -> result.copy(destinations = destinations())
                else -> skip(result)
            }
        }
    }

    fun extractServices(): List<Service> = destinations?.mapNotNull(Destination::service) ?: emptyList()

}
