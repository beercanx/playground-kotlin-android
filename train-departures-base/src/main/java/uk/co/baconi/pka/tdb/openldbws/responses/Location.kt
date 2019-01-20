package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class Location(
    val locationName: String?, // Station name [Barnsley]
    val crs: String? // CRS Code [BNY]
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Location {

            parser.require(XmlPullParser.START_TAG, null, "location")

            var locationName: String? = null
            var crs: String? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "locationName" -> locationName = parser.readAsText()
                    "crs" -> crs = parser.readAsText()
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "location")

            return Location(locationName, crs)
        }
    }
}