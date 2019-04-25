package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class BaseDeparturesResponse(val departuresBoard: DeparturesBoard? = null) {

    companion object {

        internal fun fromXml(parser: XmlPullParser, type: String): BaseDeparturesResponse {

            parser.require(XmlPullParser.START_TAG, null, type)

            var result = BaseDeparturesResponse()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "DeparturesBoard" -> result = result.copy(departuresBoard = DeparturesBoard.fromXml(parser))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, type)

            return result
        }
    }
}