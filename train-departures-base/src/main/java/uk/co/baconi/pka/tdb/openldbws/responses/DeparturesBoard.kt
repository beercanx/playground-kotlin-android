package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readBoolean
import uk.co.baconi.pka.tdb.xml.readText
import uk.co.baconi.pka.tdb.xml.skip

data class DeparturesBoard(
    val generatedAt: String?,
    val locationName: String?,
    val crs: String?,
    val nrccMessages: NRCCMessages?,
    val platformAvailable: Boolean?,
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
                    "lt4:generatedAt" -> generatedAt = parser.readText()
                    "lt4:locationName" -> locationName = parser.readText()
                    "lt4:crs" -> crs = parser.readText()
                    "lt4:nrccMessages" -> nrccMessages = NRCCMessages.fromXml(parser)
                    "lt4:platformAvailable" -> platformAvailable = parser.readBoolean()
                    "lt7:departures" -> departures = Departures.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "DeparturesBoard")

            return DeparturesBoard(generatedAt, locationName, crs, nrccMessages, platformAvailable, departures)
        }
    }
}