package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class Destination(val crs: String?, val service: Service?) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Destination? {

            parser.require(XmlPullParser.START_TAG, null, "lt7:destination")

            val crs: String? = parser.getAttributeValue(null, "crs")
            var service: Service? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "lt7:service" -> service = Service.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "lt7:destination")

            return Destination(crs, service)
        }
    }
}