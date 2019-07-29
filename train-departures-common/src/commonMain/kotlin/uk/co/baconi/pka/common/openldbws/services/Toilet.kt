package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.parse
import uk.co.baconi.pka.common.xml.readAsText

data class Toilet(
    val status: ToiletStatus? = null,
    val type: String? = null // [Unknown / None / Standard / Accessible]
) {

    companion object {

        fun XmlDeserializer.toilet(): Toilet = parse("toilet") { result ->
            result.copy(
                status = getAttributeValue("status")?.let(ToiletStatus.Companion::lookup),
                type = readAsText()
            )
        }
    }
}