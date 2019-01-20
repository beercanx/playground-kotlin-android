package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsBoolean
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class ServiceLocation(
    val locationName: String?, // Station name [Barnsley]
    val crs: String?, // CRS Code [BNY]
    val via: String?, // [via Leeds]
    val futureChangeTo: String?, // [Bus / Ferry / Train]
    val assocIsCancelled: Boolean?
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): ServiceLocation {

            parser.require(XmlPullParser.START_TAG, null, "location")

            var locationName: String? = null
            var crs: String? = null
            var via: String? = null
            var futureChangeTo: String? = null
            var assocIsCancelled: Boolean? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "locationName" -> locationName = parser.readAsText()
                    "crs" -> crs = parser.readAsText()
                    "via" -> via = parser.readAsText()
                    "futureChangeTo" -> futureChangeTo = parser.readAsText()
                    "assocIsCancelled" -> assocIsCancelled = parser.readAsBoolean()
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "location")

            return ServiceLocation(locationName, crs, via, futureChangeTo, assocIsCancelled)
        }
    }
}