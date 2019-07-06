package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.parse
import uk.co.baconi.pka.tdb.xml.readAsBoolean
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class DeparturesBoard(
    val generatedAt: String? = null, // [2019-01-13T13:51:17.106902+00:00]
    val locationName: String? = null, // Station Name [Sheffield]
    val crs: String? = null, // CRS Code [SHF]
    val filterLocationName: String? = null,
    val filtercrs: String? = null,
    val filterType: String? = null,
    val nrccMessages: List<String>? = null, // Messages [Disruption between Bristol Temple Meads and Taunton via Weston-super-Mare.]
    val platformAvailable: Boolean? = null, // [true|false|null]
    val areServicesAvailable: Boolean? = null, // [true|false|null]
    val departures: List<DepartureItem>? = null
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): DeparturesBoard = parser.parse("DeparturesBoard") { result ->
            when (parser.name) {
                "generatedAt" -> result.copy(generatedAt = parser.readAsText())
                "locationName" -> result.copy(locationName = parser.readAsText())
                "crs" -> result.copy(crs = parser.readAsText())
                "filterLocationName" -> result.copy(filterLocationName = parser.readAsText())
                "filtercrs" -> result.copy(filtercrs = parser.readAsText())
                "filterType" -> result.copy(filterType = parser.readAsText())
                "nrccMessages" -> result.copy(nrccMessages = NRCCMessages.fromXml(parser))
                "platformAvailable" -> result.copy(platformAvailable = parser.readAsBoolean())
                "areServicesAvailable" -> result.copy(areServicesAvailable = parser.readAsBoolean())
                "departures" -> result.copy(departures = Departures.fromXml(parser))
                else -> parser.skip(result)
            }
        }
    }
}