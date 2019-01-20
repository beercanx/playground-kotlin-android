package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class Departures(val departureItems: List<DepartureItem>) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Departures? {

            parser.require(XmlPullParser.START_TAG, null, "departures")

            val results = mutableListOf<DepartureItem>()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "destination" -> DepartureItem.fromXml(parser).let(results::add)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "departures")

            return Departures(results.toList())
        }
    }
}