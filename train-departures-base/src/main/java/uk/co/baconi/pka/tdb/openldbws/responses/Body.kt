package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.skip

data class BodyFailure(val fault: Fault? = null) : Body()
data class BodySuccess(
    val departuresResponse: BaseDeparturesResponse? = null,
    val departureBoardResponse: BaseDepartureBoardResponse? = null
) : Body()

sealed class Body {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Body {

            parser.require(XmlPullParser.START_TAG, null, "Body")

            var result: Body = BodyFailure()
            var hasFault = false

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                if(hasFault) {
                    parser.skip()
                    continue
                }
                when (parser.name) {
                    "Fault" -> {
                        result = BodyFailure(Fault.fromXml(parser))
                        hasFault = true
                    }
                    "GetNextDeparturesResponse" -> result = BodySuccess(
                        departuresResponse = BaseDeparturesResponse.fromXml(parser, "GetNextDeparturesResponse")
                    )
                    "GetNextDeparturesWithDetailsResponse" -> result = BodySuccess(
                        departuresResponse = BaseDeparturesResponse.fromXml(parser, "GetNextDeparturesWithDetailsResponse")
                    )
                    "GetFastestDeparturesResponse" -> result = BodySuccess(
                        departuresResponse = BaseDeparturesResponse.fromXml(parser, "GetFastestDeparturesResponse")
                    )
                    "GetFastestDeparturesWithDetailsResponse" -> result = BodySuccess(
                        departuresResponse = BaseDeparturesResponse.fromXml(parser, "GetFastestDeparturesWithDetailsResponse")
                    )
                    "GetDepartureBoardResponse" -> result = BodySuccess(
                        departureBoardResponse = BaseDepartureBoardResponse.fromXml(parser, "GetDepartureBoardResponse")
                    )
                    "GetDepBoardWithDetailsResponse" -> result = BodySuccess(
                        departureBoardResponse = BaseDepartureBoardResponse.fromXml(parser, "GetDepBoardWithDetailsResponse")
                    )
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "Body")

            // If we have a fault then return body failure, else it was a success
            return result
        }
    }
}