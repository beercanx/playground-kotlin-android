package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class Body(val getNextDeparturesResponse: GetNextDeparturesResponse?) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Body? {

            parser.require(XmlPullParser.START_TAG, null, "Body")

            var getNextDeparturesResponse: GetNextDeparturesResponse? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "GetNextDeparturesResponse" -> getNextDeparturesResponse = GetNextDeparturesResponse.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Body")

            return Body(getNextDeparturesResponse)
        }
    }
}