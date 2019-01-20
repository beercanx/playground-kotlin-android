package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class DepartureItem(val crs: String?, val service: ServiceItem?) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): DepartureItem {

            parser.require(XmlPullParser.START_TAG, null, "destination")

            val crs: String? = parser.getAttributeValue(null, "crs")
            var service: ServiceItem? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "service" -> service = ServiceItem.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "destination")

            return DepartureItem(crs, service)
        }
    }
}