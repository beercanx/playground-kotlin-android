package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsBoolean
import uk.co.baconi.pka.tdb.xml.readAsInt
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class CallingPoint(
    val locationName: String? = null,
    val crs: String? = null,
    val scheduledTime: String? = null,
    val estimatedTime: String? = null,
    val actualTime: String? = null,
    val isCancelled: Boolean? = null,
    val length: Int? = null,
    val detachFront: Boolean? = null,
    val formation: FormationData? = null,
    val adhocAlerts: List<String>? = null
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): CallingPoint {

            parser.require(XmlPullParser.START_TAG, null, "callingPoint")

            var result = CallingPoint()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "locationName" -> result = result.copy(locationName = parser.readAsText())
                    "crs" -> result = result.copy(crs = parser.readAsText())
                    "st" -> result = result.copy(scheduledTime = parser.readAsText())
                    "et" -> result = result.copy(estimatedTime = parser.readAsText())
                    "at" -> result = result.copy(actualTime = parser.readAsText())
                    "isCancelled" -> result = result.copy(isCancelled = parser.readAsBoolean())
                    "length" -> result = result.copy(length = parser.readAsInt())
                    "detachFront" -> result = result.copy(detachFront = parser.readAsBoolean())
                    "formation" -> result = result.copy(formation = FormationData.fromXml(parser))
                    "adhocAlerts" -> result = result.copy(adhocAlerts = AdhocAlerts.fromXml(parser))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "callingPoint")

            return result
        }
    }
}