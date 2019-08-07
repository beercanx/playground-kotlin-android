package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.xml.*

data class ServiceLocation(
    val locationName: String? = null,
    val crs: String? = null,
    val via: String? = null,
    val futureChangeTo: String? = null,
    val associationIsCancelled: Boolean? = null
) {
    companion object {

        fun XmlDeserializer.serviceLocations(type: String): List<ServiceLocation> = parse(type, emptyList()) { result ->
            when (getName()) {
                "location" -> serviceLocation().let(result::plus)
                else -> skip(result)
            }
        }

        fun XmlDeserializer.serviceLocation(): ServiceLocation = parse("location") { result ->
            when (getName()) {
                "locationName" -> result.copy(locationName = readAsText())
                "crs" -> result.copy(crs = readAsText())
                "via" -> result.copy(via = readAsText())
                "futureChangeTo" -> result.copy(futureChangeTo = readAsText())
                "assocIsCancelled" -> result.copy(associationIsCancelled = readAsBoolean())
                else -> skip(result)
            }
        }
    }
}