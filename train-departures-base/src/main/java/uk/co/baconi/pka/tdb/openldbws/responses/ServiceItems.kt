package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

object ServiceItems {

    internal fun fromXml(parser: XmlPullParser, type: String): List<ServiceItem> {

        parser.require(XmlPullParser.START_TAG, null, type)

        val results = mutableListOf<ServiceItem>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "service" -> ServiceItem.fromXml(parser).let(results::add)
                else -> parser.skip()
            }
        }

        parser.require(XmlPullParser.END_TAG, null, type)

        return results.toList()
    }
}