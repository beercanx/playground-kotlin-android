package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.openldbws.services.FormationData.Companion.formationData
import uk.co.baconi.pka.common.soap.adhocAlerts
import uk.co.baconi.pka.common.xml.*

data class CallingPoint(
    val locationName: String? = null,
    val crs: String? = null,
    val scheduledTime: String? = null,
    val estimatedTime: String? = null,
    val actualTime: String? = null,
    val isCancelled: Boolean? = null,
    val length: Int? = null,
    val detachFront: Boolean? = null,
    val formation: FormationData? = null,
    val adhocAlerts: List<String>? = null
) {

    companion object {

        fun XmlDeserializer.callingPoint(): CallingPoint = parse("callingPoint") { result ->
            when (getName()) {
                "locationName" -> result.copy(locationName = readAsText())
                "crs" -> result.copy(crs = readAsText())
                "st" -> result.copy(scheduledTime = readAsText())
                "et" -> result.copy(estimatedTime = readAsText())
                "at" -> result.copy(actualTime = readAsText())
                "isCancelled" -> result.copy(isCancelled = readAsBoolean())
                "length" -> result.copy(length = readAsInt())
                "detachFront" -> result.copy(detachFront = readAsBoolean())
                "formation" -> result.copy(formation = formationData())
                "adhocAlerts" -> result.copy(adhocAlerts = adhocAlerts())
                else -> skip(result)
            }
        }
    }
}