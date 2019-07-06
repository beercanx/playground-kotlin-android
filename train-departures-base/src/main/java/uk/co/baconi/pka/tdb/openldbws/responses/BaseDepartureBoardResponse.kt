package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.skip

data class BaseDepartureBoardResponse(val stationBoardResult: StationBoardResult? = null) {

    companion object {

        internal fun fromXml(parser: XmlPullParser, type: String): BaseDepartureBoardResponse =
            parser.parse(type) { result ->
                when (parser.name) {
                    "GetStationBoardResult" -> result.copy(stationBoardResult = StationBoardResult.fromXml(parser))
                    else -> parser.skip(result)
                }
            }
    }
}