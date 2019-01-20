package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

object ServiceLocations {

    internal fun fromXml(parser: XmlPullParser, type: String): List<ServiceLocation> {

        parser.require(XmlPullParser.START_TAG, null, type)

        val entries = mutableListOf<ServiceLocation>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "location" -> ServiceLocation.fromXml(parser).let(entries::add)
                else -> parser.skip()
            }
        }

        parser.require(XmlPullParser.END_TAG, null, type)

        return entries.toList()
    }
}