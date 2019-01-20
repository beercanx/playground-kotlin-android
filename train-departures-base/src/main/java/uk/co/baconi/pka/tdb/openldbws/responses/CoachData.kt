package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsInt
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class CoachData(
    val coachClass: String? = null, // [First / Mixed / Standard]
    val loading: Int? = null, // The loading value in % (think this mean how full) [0-100]
    val number: String? = null, // Identifier [A / 12]
    val toilet: ToiletAvailabilityType? = null
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): CoachData {

            parser.require(XmlPullParser.START_TAG, null, "coach")

            var result = CoachData()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "coachClass" -> result = result.copy(coachClass = parser.readAsText())
                    "loading" -> result = result.copy(loading = parser.readAsInt())
                    "number" -> result = result.copy(number = parser.readAsText())
                    "toilet" -> result = result.copy(toilet = ToiletAvailabilityType.fromXml(parser))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "coach")

            return result
        }
    }
}
