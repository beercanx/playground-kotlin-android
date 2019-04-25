package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

object AdhocAlerts {

    internal fun fromXml(parser: XmlPullParser): List<String> {

        parser.require(XmlPullParser.START_TAG, null, "adhocAlerts")

        val results = mutableListOf<String>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "adhocAlertText" -> parser.readAsText()?.let(results::add)
                else -> parser.skip()
            }
        }

        parser.require(XmlPullParser.END_TAG, null, "adhocAlerts")

        return results.toList()
    }
}