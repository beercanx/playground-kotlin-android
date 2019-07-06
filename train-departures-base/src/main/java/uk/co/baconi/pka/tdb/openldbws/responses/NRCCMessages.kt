package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

object NRCCMessages {

    internal fun fromXml(parser: XmlPullParser): List<String> = parser.parse("nrccMessages", emptyList()) { result ->
        when (parser.name) {
            "message" -> when (val message = parser.readAsText()) {
                is String -> result.plus(message)
                else -> result
            }
            else -> parser.skip(result)
        }
    }
}