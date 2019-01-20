package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class BodyFailure(val fault: Fault? = null) : Body()
data class BodySuccess(val getNextDeparturesResponse: GetNextDeparturesResponse? = null) : Body()

sealed class Body {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Body {

            parser.require(XmlPullParser.START_TAG, null, "Body")

            var result: Body = BodyFailure()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "Fault" -> result = BodyFailure(Fault.fromXml(parser))
                    "GetNextDeparturesResponse" -> result = BodySuccess(GetNextDeparturesResponse.fromXml(parser))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Body")

            // If we have a fault then return body failure, else it was a success
            return result
        }
    }
}