package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.skip

data class CallingPoints(
    val callingPoint: List<CallingPoint>? = null,
    val serviceType: String? = "train",
    val serviceChangeRequired: Boolean? = false,
    val assocIsCancelled: Boolean? = false
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser, type: String): List<CallingPoints> =
            parser.parse(type, emptyList()) { result ->
                when (parser.name) {
                    "callingPointList" -> fromXml(parser).let(result::plus)
                    else -> parser.skip(result)
                }
            }

        private fun fromXml(parser: XmlPullParser): CallingPoints =
            parser.parse("callingPointList", parsingAttributes = parseAttributes(parser)) { result ->
                when (parser.name) {
                    "callingPoint" -> result.copy(
                        callingPoint = result.callingPoint.orEmpty().plus(
                            CallingPoint.fromXml(
                                parser
                            )
                        )
                    )
                    else -> parser.skip(result)
                }
            }

        private fun parseAttributes(parser: XmlPullParser) = { result: CallingPoints ->
            result.copy(
                serviceType = parser.getAttributeValue(null, "serviceType"),
                serviceChangeRequired = parser.getAttributeValue(null, "serviceChangeRequired")?.toBoolean(),
                assocIsCancelled = parser.getAttributeValue(null, "assocIsCancelled")?.toBoolean()
            )
        }
    }
}
