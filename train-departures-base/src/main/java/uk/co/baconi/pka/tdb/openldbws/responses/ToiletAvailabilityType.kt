package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.readAsText

enum class ToiletStatus {
    Unknown,
    InService,
    NotInService;

    companion object {
        fun lookup(value: String): ToiletStatus? = values().find {type -> type.name == value }
    }
}

data class ToiletAvailabilityType(
    val status: ToiletStatus? = null,
    val type: String? = null // [Unknown / None / Standard / Accessible]
) {

    companion object {

        fun fromXml(parser: XmlPullParser): ToiletAvailabilityType = parser.parse("toilet") { result ->
            result.copy(
                status = parser.getAttributeValue(null, "status")?.let(ToiletStatus.Companion::lookup),
                type = parser.readAsText()
            )
        }
    }
}