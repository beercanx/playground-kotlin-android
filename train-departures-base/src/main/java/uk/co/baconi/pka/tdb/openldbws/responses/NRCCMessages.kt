package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

object NRCCMessages {

    internal fun fromXml(parser: XmlPullParser): List<String> {

        parser.require(XmlPullParser.START_TAG, null, "nrccMessages")

        val results = mutableListOf<String>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "message" -> parser.readAsText()?.let(results::add)
                else -> parser.skip()
            }
        }

        parser.require(XmlPullParser.END_TAG, null, "nrccMessages")

        return results.toList()
    }
}