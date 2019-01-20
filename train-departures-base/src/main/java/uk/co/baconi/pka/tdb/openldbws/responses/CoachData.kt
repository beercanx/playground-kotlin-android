package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsInt
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class CoachData(
    val coachClass: String?, // [First / Mixed / Standard]
    val loading: Int?, // The loading value in % (think this mean how full) [0-100]
    val number: String?, // Identifier [A / 12]
    val toilet: ToiletAvailabilityType?
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): CoachData {

            parser.require(XmlPullParser.START_TAG, null, "coach")

            var coachClass: String? = null
            var loading: Int? = null
            var number: String? = null
            var toilet: ToiletAvailabilityType? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "coachClass" -> coachClass = parser.readAsText()
                    "loading" -> loading = parser.readAsInt()
                    "number" -> number = parser.readAsText()
                    "toilet" -> toilet = ToiletAvailabilityType.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "coach")

            return CoachData(coachClass, loading, number, toilet)
        }
    }
}
