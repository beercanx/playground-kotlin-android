package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.openldbws.services.Service.Companion.services
import uk.co.baconi.pka.common.soap.nrccMessages
import uk.co.baconi.pka.common.xml.*

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
        fun XmlDeserializer.departureBoard(): DepartureBoard {
            return parse("GetStationBoardResult") { result ->
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
                    "trainServices" -> result.copy(trainServices = services("trainServices"))
                    "busServices" -> result.copy(busServices = services("busServices"))
                    "ferryServices" -> result.copy(ferryServices = services("ferryServices"))
                    else -> skip(result)
                }
            }
        }
    }

    fun extractServices(): List<Service> = trainServices ?: emptyList()

}