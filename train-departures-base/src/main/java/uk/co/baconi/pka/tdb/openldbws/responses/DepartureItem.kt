package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.skip

data class DepartureItem(val crs: String? = null, val service: ServiceItem? = null) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): DepartureItem =
            parser.parse("destination", parsingAttributes = extractCrs(parser)) { result ->
                when (parser.name) {
                    "service" -> result.copy(service = ServiceItem.fromXml(parser))
                    else -> parser.skip(result)
                }
            }

        private fun extractCrs(parser: XmlPullParser) = { result: DepartureItem ->
            result.copy(crs = parser.getAttributeValue(null, "crs"))
        }
    }
}