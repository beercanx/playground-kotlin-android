package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.openldbws.services.Service.Companion.service
import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.parse
import uk.co.baconi.pka.common.xml.skip

data class Destination(
    val crs: String? = null,
    val service: Service? = null
) {

    companion object {

        fun XmlDeserializer.destinations(): List<Destination> = parse("departures", emptyList()) { result ->
            when (getName()) {
                "destination" -> result.plus(destination())
                else -> skip(result)
            }
        }

        fun XmlDeserializer.destination(): Destination = parse("destination", attributes = attributes()) { result ->
            when (getName()) {
                "service" -> result.copy(service = service())
                else -> skip(result)
            }
        }

        private fun XmlDeserializer.attributes() = { result: Destination ->
            result.copy(crs = getAttributeValue("crs"))
        }
    }
}