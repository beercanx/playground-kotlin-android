package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readText
import uk.co.baconi.pka.tdb.xml.skip

data class Service(
    val sta: String?, // Scheduled Time of Arrival [10:41]
    val eta: String?, // Estimated Time of Arrival [On time / 10:54]
    val std: String?, // Scheduled Time of Departure [10:41]
    val etd: String?, // Estimated Time of Departure [On time / 10:54]
    val platform: String?, // null / 1 / bus
    val operator: String?, // Northern
    val operatorCode: String?, // NT
    val serviceType: String?, // bus / train
    val serviceID: String?, // Base64 encoded string
    val rsid: String?, // NT044400
    val origin: Location?,
    val destination: Location?
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): Service? {

            parser.require(XmlPullParser.START_TAG, null, "lt7:service")

            var sta: String? = null
            var eta: String? = null
            var std: String? = null
            var etd: String? = null
            var platform: String? = null
            var operator: String? = null
            var operatorCode: String? = null
            var serviceType: String? = null
            var serviceID: String? = null
            var rsid: String? = null
            var origin: Location? = null
            var destination: Location? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "lt4:sta" -> sta = parser.readText()
                    "lt4:eta" -> eta = parser.readText()
                    "lt4:std" -> std = parser.readText()
                    "lt4:etd" -> etd = parser.readText()
                    "lt4:platform" -> platform = parser.readText()
                    "lt4:operator" -> operator = parser.readText()
                    "lt4:operatorCode" -> operatorCode = parser.readText()
                    "lt4:serviceType" -> serviceType = parser.readText()
                    "lt4:serviceID" -> serviceID = parser.readText()
                    "lt5:rsid" -> rsid = parser.readText()
                    "lt5:origin" -> origin = readLocation(parser, "lt5:origin")
                    "lt5:destination" -> destination = readLocation(parser, "lt5:destination")
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "lt7:service")

            return Service(
                sta,
                eta,
                std,
                etd,
                platform,
                operator,
                operatorCode,
                serviceType,
                serviceID,
                rsid,
                origin,
                destination
            )
        }

        private fun readLocation(parser: XmlPullParser, type: String): Location? {

            parser.require(XmlPullParser.START_TAG, null, type)

            var location: Location? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "lt4:location" -> location = Location.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, type)

            return location
        }
    }
}