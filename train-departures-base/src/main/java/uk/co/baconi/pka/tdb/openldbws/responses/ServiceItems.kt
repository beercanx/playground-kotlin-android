package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.skip

object ServiceItems {

    internal fun fromXml(parser: XmlPullParser, type: String): List<ServiceItem> =
        parser.parse(type, emptyList()) { result ->
            when (parser.name) {
                "service" -> ServiceItem.fromXml(parser).let(result::plus)
                else -> parser.skip(result)
            }
        }
}