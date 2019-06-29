package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class CallingPoints(
    val callingPoint: List<CallingPoint>? = null,
    val serviceType: String? = "train",
    val serviceChangeRequired: Boolean? = false,
    val assocIsCancelled: Boolean? = false
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser, type: String): List<CallingPoints> {

            parser.require(XmlPullParser.START_TAG, null, type)

            val results = mutableListOf<CallingPoints>()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "callingPointList" -> fromXml(parser).let(results::add)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, type)

            return results.toList()
        }

        private fun fromXml(parser: XmlPullParser): CallingPoints {

            parser.require(XmlPullParser.START_TAG, null, "callingPointList")

            var result = CallingPoints(
                serviceType = parser.getAttributeValue(null, "serviceType"),
                serviceChangeRequired = parser.getAttributeValue(null, "serviceChangeRequired")?.toBoolean(),
                assocIsCancelled = parser.getAttributeValue(null, "assocIsCancelled")?.toBoolean()
            )

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "callingPoint" -> result = result.copy(callingPoint = result.callingPoint.orEmpty().plus(CallingPoint.fromXml(parser)))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "callingPointList")

            return result
        }
    }
}
