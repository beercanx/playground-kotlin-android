package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

object AdhocAlerts {

    internal fun fromXml(parser: XmlPullParser): List<String> = parser.parse("adhocAlerts", emptyList()) { result ->
        when (parser.name) {
            "adhocAlertText" -> when (val adhocAlertText = parser.readAsText()) {
                is String -> result.plus(adhocAlertText)
                else -> result
            }
            else -> parser.skip(result)
        }
    }
}