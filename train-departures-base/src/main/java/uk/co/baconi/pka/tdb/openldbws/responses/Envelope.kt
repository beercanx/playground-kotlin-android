package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.skip

data class Envelope(val body: Body? = null) {

    companion object {

        fun fromXml(parser: XmlPullParser): Envelope = parser.parse("Envelope") { result ->
            when (parser.name) {
                "Body" -> result.copy(body = Body.fromXml(parser))
                else -> parser.skip(result)
            }
        }
    }
}