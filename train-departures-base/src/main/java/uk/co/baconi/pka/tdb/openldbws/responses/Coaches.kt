package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.skip

object Coaches {

    internal fun fromXml(parser: XmlPullParser): List<CoachData> = parser.parse("coaches", emptyList()) { result ->
        when (parser.name) {
            "coach" -> CoachData.fromXml(parser).let(result::plus)
            else -> parser.skip(result)
        }
    }
}