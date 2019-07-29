package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.openldbws.services.CallingPoint.Companion.callingPoint
import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.parse
import uk.co.baconi.pka.common.xml.skip

data class CallingPoints(
    val callingPoint: List<CallingPoint>? = null,
    val serviceType: String? = "train",
    val serviceChangeRequired: Boolean? = false,
    val associationIsCancelled: Boolean? = false
) {

    companion object {

        fun XmlDeserializer.callingPoints(type: String): List<CallingPoints> = parse(type, emptyList()) { result ->
            when (getName()) {
                "callingPointList" -> callingPoints().let(result::plus)
                else -> skip(result)
            }
        }

        fun XmlDeserializer.callingPoints(): CallingPoints = parse("callingPointList", attributes = attributes()) { result ->
            when (getName()) {
                "callingPoint" -> result.copy(
                    callingPoint = result.callingPoint.orEmpty().plus(
                        callingPoint()
                    )
                )
                else -> skip(result)
            }
        }

        fun XmlDeserializer.attributes() = { result: CallingPoints ->
            result.copy(
                serviceType = getAttributeValue("serviceType"),
                serviceChangeRequired = getAttributeValue("serviceChangeRequired")?.toBoolean(),
                associationIsCancelled = getAttributeValue("assocIsCancelled")?.toBoolean()
            )
        }
    }
}