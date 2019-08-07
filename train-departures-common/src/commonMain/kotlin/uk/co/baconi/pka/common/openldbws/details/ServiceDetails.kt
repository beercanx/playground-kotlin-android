package uk.co.baconi.pka.common.openldbws.details

import uk.co.baconi.pka.common.openldbws.services.CallingPoints
import uk.co.baconi.pka.common.openldbws.services.CallingPoints.Companion.callingPoints
import uk.co.baconi.pka.common.openldbws.services.FormationData
import uk.co.baconi.pka.common.openldbws.services.FormationData.Companion.formationData
import uk.co.baconi.pka.common.soap.adhocAlerts
import uk.co.baconi.pka.common.xml.*

data class ServiceDetails(
    val generatedAt: String? = null,
    val serviceType: String? = null,
    val locationName: String? = null,
    val crs: String? = null,
    val operator: String? = null,
    val operatorCode: String? = null,
    val rsid: String? = null,
    val isCancelled: Boolean? = null,
    val cancelReason: String? = null,
    val delayReason: String? = null,
    val overdueMessage: String? = null,
    val length: Int? = null,
    val detachFront: Boolean? = null,
    val isReverseFormation: Boolean? = null,
    val platform: String? = null,
    val scheduledArrivalTime: String? = null,
    val estimatedArrivalTime: String? = null,
    val actualArrivalTime: String? = null,
    val scheduledDepartureTime: String? = null,
    val estimatedDepartureTime: String? = null,
    val actualDepartureTime: String? = null,
    val adhocAlerts: List<String>? = null,
    val formation: FormationData? = null,
    val previousCallingPoints: List<CallingPoints>? = null,
    val subsequentCallingPoints: List<CallingPoints>? = null
) {
    companion object {
        fun XmlDeserializer.serviceDetails(): ServiceDetails = parse("GetServiceDetailsResult") { result ->
            when (getName()) {
                "generatedAt" -> result.copy(generatedAt = readAsText())
                "serviceType" -> result.copy(serviceType = readAsText())
                "locationName" -> result.copy(locationName = readAsText())
                "crs" -> result.copy(crs = readAsText())
                "operator" -> result.copy(operator = readAsText())
                "operatorCode" -> result.copy(operatorCode = readAsText())
                "rsid" -> result.copy(rsid = readAsText())
                "isCancelled" -> result.copy(isCancelled = readAsBoolean())
                "cancelReason" -> result.copy(cancelReason = readAsText())
                "delayReason" -> result.copy(delayReason = readAsText())
                "overdueMessage" -> result.copy(overdueMessage = readAsText())
                "length" -> result.copy(length = readAsInt())
                "detachFront" -> result.copy(detachFront = readAsBoolean())
                "isReverseFormation" -> result.copy(isReverseFormation = readAsBoolean())
                "platform" -> result.copy(platform = readAsText())
                "sta" -> result.copy(scheduledArrivalTime = readAsText())
                "eta" -> result.copy(estimatedArrivalTime = readAsText())
                "ata" -> result.copy(actualArrivalTime = readAsText())
                "std" -> result.copy(scheduledDepartureTime = readAsText())
                "etd" -> result.copy(estimatedDepartureTime = readAsText())
                "atd" -> result.copy(actualDepartureTime = readAsText())
                "adhocAlerts" -> result.copy(adhocAlerts = adhocAlerts())
                "formation" -> result.copy(formation = formationData())
                "previousCallingPoints" -> result.copy(previousCallingPoints = callingPoints("previousCallingPoints"))
                "subsequentCallingPoints" -> result.copy(subsequentCallingPoints = callingPoints("subsequentCallingPoints"))
                else -> skip(result)
            }
        }
    }
}