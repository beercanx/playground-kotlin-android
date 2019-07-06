package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.readAsInt
import uk.co.baconi.pka.tdb.xml.skip

data class FormationData(val avgLoading: Int? = null, val coaches: List<CoachData>? = null) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): FormationData = parser.parse("formation") { result ->
            when (parser.name) {
                "avgLoading" -> result.copy(avgLoading = parser.readAsInt())
                "coaches" -> result.copy(coaches = Coaches.fromXml(parser))
                else -> parser.skip(result)
            }
        }
    }
}