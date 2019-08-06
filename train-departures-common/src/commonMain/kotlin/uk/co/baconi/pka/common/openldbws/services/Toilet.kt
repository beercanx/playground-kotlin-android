package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.xml.EventType
import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.readAsText

data class Toilet(
    val status: ToiletStatus? = null,
    val type: String? = null // [Unknown / None / Standard / Accessible]
) {

    companion object {

        fun XmlDeserializer.toilet(): Toilet {

            require(EventType.START_TAG, "toilet")

            val result = Toilet(
                status = getAttributeValue("status")?.let(ToiletStatus.Companion::lookup),
                type = readAsText()
            )

            require(EventType.END_TAG, "toilet")

            return result
        }
    }
}