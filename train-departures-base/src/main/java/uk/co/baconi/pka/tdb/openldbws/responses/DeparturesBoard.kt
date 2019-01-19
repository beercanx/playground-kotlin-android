package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsBoolean
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class DeparturesBoard(
    val generatedAt: String?, // [2019-01-13T13:51:17.106902+00:00]
    val locationName: String?, // Station Name [Sheffield]
    val crs: String?, // CRS Code [SHF]
    val nrccMessages: NRCCMessages?, // Messages [Disruption between Bristol Temple Meads and Taunton via Weston-super-Mare.]
    val platformAvailable: Boolean?, // [true|false|null]
    val departures: Departures?
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): DeparturesBoard? {

            parser.require(XmlPullParser.START_TAG, null, "DeparturesBoard")

            var generatedAt: String? = null
            var locationName: String? = null
            var crs: String? = null
            var nrccMessages: NRCCMessages? = null
            var platformAvailable: Boolean? = null
            var departures: Departures? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "generatedAt" -> generatedAt = parser.readAsText()
                    "locationName" -> locationName = parser.readAsText()
                    "crs" -> crs = parser.readAsText()
                    "nrccMessages" -> nrccMessages = NRCCMessages.fromXml(parser)
                    "platformAvailable" -> platformAvailable = parser.readAsBoolean()
                    "departures" -> departures = Departures.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "DeparturesBoard")

            return DeparturesBoard(generatedAt, locationName, crs, nrccMessages, platformAvailable, departures)
        }
    }
}