package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class AdhocAlerts(val messages: List<String>) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): AdhocAlerts? {

            parser.require(XmlPullParser.START_TAG, null, "adhocAlerts")

            val entries = mutableListOf<String>()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "adhocAlertText" -> parser.readAsText()?.let(entries::add)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "adhocAlerts")

            return AdhocAlerts(entries.toList())
        }
    }
}