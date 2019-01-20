package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsText

enum class ToiletStatus {

    Unknown,
    InService,
    NotInService;

    companion object {
        fun lookup(value: String): ToiletStatus? = ToiletStatus.values().find {type -> type.name == value }
    }
}

data class ToiletAvailabilityType(
    val status: ToiletStatus?,
    val type: String? // [Unknown / None / Standard / Accessible]
) {

    companion object {

        fun fromXml(parser: XmlPullParser): ToiletAvailabilityType {

            parser.require(XmlPullParser.START_TAG, null, "toilet")

            val status: ToiletStatus? = parser.getAttributeValue(null, "status")?.let(ToiletStatus.Companion::lookup)
            val type: String? = parser.readAsText()

            parser.require(XmlPullParser.END_TAG, null, "toilet")

            return ToiletAvailabilityType(status, type)
        }
    }
}