package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.*

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

        internal fun fromXml(parser: XmlPullParser): CallingPoint = parser.parse("callingPoint") { result ->
            when (parser.name) {
                "locationName" -> result.copy(locationName = parser.readAsText())
                "crs" -> result.copy(crs = parser.readAsText())
                "st" -> result.copy(scheduledTime = parser.readAsText())
                "et" -> result.copy(estimatedTime = parser.readAsText())
                "at" -> result.copy(actualTime = parser.readAsText())
                "isCancelled" -> result.copy(isCancelled = parser.readAsBoolean())
                "length" -> result.copy(length = parser.readAsInt())
                "detachFront" -> result.copy(detachFront = parser.readAsBoolean())
                "formation" -> result.copy(formation = FormationData.fromXml(parser))
                "adhocAlerts" -> result.copy(adhocAlerts = AdhocAlerts.fromXml(parser))
                else -> parser.skip(result)
            }
        }
    }
}