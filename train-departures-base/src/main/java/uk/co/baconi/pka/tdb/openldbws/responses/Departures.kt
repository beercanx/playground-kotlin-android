package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class Departures(val destination: List<Destination>) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Departures? {

            val entries = mutableListOf<Destination>()

            parser.require(XmlPullParser.START_TAG, null, "departures")

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "destination" -> Destination.fromXml(parser)?.let(entries::add)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "departures")

            return Departures(entries)
        }
    }
}