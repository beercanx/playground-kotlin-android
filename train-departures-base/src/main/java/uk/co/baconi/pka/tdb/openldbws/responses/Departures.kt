package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.skip

object Departures {

    internal fun fromXml(parser: XmlPullParser): List<DepartureItem> =
        parser.parse("departures", emptyList()) { result ->
            when (parser.name) {
                "destination" -> result.plus(DepartureItem.fromXml(parser))
                else -> parser.skip(result)
            }
        }
}