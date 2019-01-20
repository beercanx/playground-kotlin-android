package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

object Coaches {

    internal fun fromXml(parser: XmlPullParser): List<CoachData> {

        parser.require(XmlPullParser.START_TAG, null, "coaches")

        val results = mutableListOf<CoachData>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "coach" -> CoachData.fromXml(parser).let(results::add)
                else -> parser.skip()
            }
        }

        parser.require(XmlPullParser.END_TAG, null, "coaches")

        return results.toList()
    }
}