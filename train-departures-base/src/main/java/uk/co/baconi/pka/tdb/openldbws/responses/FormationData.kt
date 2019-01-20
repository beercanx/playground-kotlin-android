package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.openldbws.requests.Request
import uk.co.baconi.pka.tdb.xml.readAsBoolean
import uk.co.baconi.pka.tdb.xml.readAsInt
import uk.co.baconi.pka.tdb.xml.skip

data class FormationData(val avgLoading: Int?, val coaches: List<CoachData>?) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): FormationData? {

            parser.require(XmlPullParser.START_TAG, null, "formation")

            var avgLoading: Int? = null
            var coaches: List<CoachData>? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "avgLoading" -> avgLoading = parser.readAsInt()
                    "coaches" -> coaches = Coaches.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "formation")

            return FormationData(avgLoading, coaches)
        }
    }
}