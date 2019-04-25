package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class Envelope(val body: Body? = null) {

    companion object {

        fun fromXml(parser: XmlPullParser): Envelope {

            parser.require(XmlPullParser.START_TAG, null, "Envelope")

            var result = Envelope()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "Body" -> result = result.copy(body = Body.fromXml(parser))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Envelope")

            return result
        }
    }
}