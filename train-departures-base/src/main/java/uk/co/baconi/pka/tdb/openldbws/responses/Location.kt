package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readText
import uk.co.baconi.pka.tdb.xml.skip

data class Location(
    val locationName: String?, // Station name [Barnsley]
    val crs: String? // CRS Code [BNY]
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Location? {

            parser.require(XmlPullParser.START_TAG, null, "lt4:location")

            var locationName: String? = null
            var crs: String? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "lt4:locationName" -> locationName = parser.readText()
                    "lt4:crs" -> crs = parser.readText()
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "lt4:location")

            return Location(locationName, crs)
        }
    }
}