package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

class Coaches {

    companion object {

        internal fun fromXml(parser: XmlPullParser): List<CoachData> {

            val entries = mutableListOf<CoachData>()

            parser.require(XmlPullParser.START_TAG, null, "coaches")

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "coach" -> CoachData.fromXml(parser).let(entries::add)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "coaches")

            return entries.toList()
        }
    }
}