package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsInt
import uk.co.baconi.pka.tdb.xml.skip

data class FormationData(val avgLoading: Int? = null, val coaches: List<CoachData>? = null) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): FormationData? {

            parser.require(XmlPullParser.START_TAG, null, "formation")

            var result = FormationData()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "avgLoading" -> result = result.copy(avgLoading = parser.readAsInt())
                    "coaches" -> result = result.copy(coaches = Coaches.fromXml(parser))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "formation")

            return result
        }
    }
}