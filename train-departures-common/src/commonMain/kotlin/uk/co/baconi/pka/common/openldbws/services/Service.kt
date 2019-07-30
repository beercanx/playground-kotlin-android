package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.openldbws.services.CallingPoints.Companion.callingPoints
import uk.co.baconi.pka.common.openldbws.services.FormationData.Companion.formationData
import uk.co.baconi.pka.common.openldbws.services.ServiceLocation.Companion.serviceLocations
import uk.co.baconi.pka.common.soap.adhocAlerts
import uk.co.baconi.pka.common.xml.*

/**
 * Covers both ServiceItem and ServiceItemWithCallingPoints (for parents of type WithDetails)
 */
data class Service(
    val scheduledArrivalTime: String? = null, // sta
    val estimatedArrivalTime: String? = null, // eta
    val scheduledDepartureTime: String? = null, // std
    val estimatedDepartureTime: String? = null, // etd
    val platform: String? = null,
    val operator: String? = null,
    val operatorCode: String? = null,
    val serviceType: String? = null,
    val serviceID: String? = null,
    val retailServiceId: String? = null, // rsid
    val origin: List<ServiceLocation>? = null,
    val destination: List<ServiceLocation>? = null,
    val currentOrigins: List<ServiceLocation>? = null,
    val currentDestinations: List<ServiceLocation>? = null,
    val isCircularRoute: Boolean? = null,
    val isCancelled: Boolean? = null,
    val filterLocationCancelled: Boolean? = null,
    val length: Int? = null,
    val detachFront: Boolean? = null,
    val isReverseFormation: Boolean? = null,
    val cancelReason: String? = null,
    val delayReason: String? = null,
    val adhocAlerts: List<String>? = null,
    val formation: FormationData? = null,
    val previousCallingPoints: List<CallingPoints>? = null,
    val subsequentCallingPoints: List<CallingPoints>? = null
) {
    companion object {

        fun XmlDeserializer.services(type: String): List<Service> = parse(type, emptyList()) { result ->
            when (getName()) {
                "service" -> service().let(result::plus)
                else -> skip(result)
            }
        }

        fun XmlDeserializer.service(): Service = parse("service") { result ->
            when (getName()) {
                "sta" -> result.copy(scheduledArrivalTime = readAsText())
                "eta" -> result.copy(estimatedArrivalTime = readAsText())
                "std" -> result.copy(scheduledDepartureTime = readAsText())
                "etd" -> result.copy(estimatedDepartureTime = readAsText())
                "platform" -> result.copy(platform = readAsText())
                "operator" -> result.copy(operator = readAsText())
                "operatorCode" -> result.copy(operatorCode = readAsText())
                "serviceType" -> result.copy(serviceType = readAsText())
                "serviceID" -> result.copy(serviceID = readAsText())
                "rsid" -> result.copy(retailServiceId = readAsText())
                "isCircularRoute" -> result.copy(isCircularRoute = readAsBoolean())
                "isCancelled" -> result.copy(isCancelled = readAsBoolean())
                "filterLocationCancelled" -> result.copy(filterLocationCancelled = readAsBoolean())
                "length" -> result.copy(length = readAsInt())
                "detachFront" -> result.copy(detachFront = readAsBoolean())
                "isReverseFormation" -> result.copy(isReverseFormation = readAsBoolean())
                "cancelReason" -> result.copy(cancelReason = readAsText())
                "delayReason" -> result.copy(delayReason = readAsText())
                "adhocAlerts" -> result.copy(adhocAlerts = adhocAlerts())
                "formation" -> result.copy(formation = formationData())
                "origin" -> result.copy(origin = serviceLocations("origin"))
                "destination" -> result.copy(destination = serviceLocations("destination"))
                "currentOrigins" -> result.copy(currentOrigins = serviceLocations("currentOrigins"))
                "currentDestinations" -> result.copy(currentDestinations = serviceLocations("currentDestinations"))
                "previousCallingPoints" -> result.copy(previousCallingPoints = callingPoints("previousCallingPoints"))
                "subsequentCallingPoints" -> result.copy(subsequentCallingPoints = callingPoints("subsequentCallingPoints"))
                else -> skip(result)
            }
        }
    }
}
