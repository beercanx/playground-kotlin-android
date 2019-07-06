package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.skip

data class BaseDeparturesResponse(val departuresBoard: DeparturesBoard? = null) {

    companion object {

        internal fun fromXml(parser: XmlPullParser, type: String): BaseDeparturesResponse =
            parser.parse(type) { result ->
                when (parser.name) {
                    "DeparturesBoard" -> result.copy(departuresBoard = DeparturesBoard.fromXml(parser))
                    else -> parser.skip(result)
                }
            }
    }
}