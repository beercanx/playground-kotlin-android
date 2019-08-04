package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.openldbws.services.Service.Companion.service
import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.parse
import uk.co.baconi.pka.common.xml.skip

data class Departure(
    val crs: String? = null,
    val service: Service? = null
) {

    companion object {

        fun XmlDeserializer.departures(): List<Departure> = parse("departures", emptyList()) { result ->
            when (getName()) {
                "destination" -> result.plus(departure())
                else -> skip(result)
            }
        }

        fun XmlDeserializer.departure(): Departure = parse("destination", attributes = attributes()) { result ->
            when (getName()) {
                "service" -> result.copy(service = service())
                else -> skip(result)
            }
        }

        private fun XmlDeserializer.attributes() = { result: Departure ->
            when(val crs = getAttributeValue("crs")) {
                is String -> result.copy(crs = crs)
                else -> result
            }
        }
    }
}