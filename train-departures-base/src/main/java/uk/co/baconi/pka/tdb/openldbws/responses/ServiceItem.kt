package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.readAsBoolean
import uk.co.baconi.pka.tdb.xml.readAsInt
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class ServiceItem(
    val sta: String?, // Scheduled Time of Arrival [10:41]
    val eta: String?, // Estimated Time of Arrival [On time / 10:54]
    val std: String?, // Scheduled Time of Departure [10:41]
    val etd: String?, // Estimated Time of Departure [On time / 10:54]
    val platform: String?, // null / 1 / bus
    val operator: String?, // Northern
    val operatorCode: String?, // NT
    val serviceType: String?, // [bus / train / ferry]
    val serviceID: String?, // Base64 encoded string
    val rsid: String?, // Retail Service ID [NT044400]
    val origins: List<ServiceLocation>?,
    val destinations: List<ServiceLocation>?,
    val currentOrigins: List<ServiceLocation>?,
    val currentDestinations: List<ServiceLocation>?,
    val isCircularRoute: Boolean?, // is operating on a circular route
    val isCancelled: Boolean?, // Cancelled at this location (from filter)
    val filterLocationCancelled: Boolean?, // Service is no longer stopping at destination (to filter)
    val length: Int?, // Number of train carriages {0 || null means unknown}
    val detachFront: Boolean?, // if the service detaches units from the front at this location
    val isReverseFormation: Boolean?, // if the service is operating in the reverse of its normal formation
    val cancelReason: String?,
    val delayReason: String?,
    val adhocAlerts: AdhocAlerts?,
    val formation: FormationData?

) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): ServiceItem {

            parser.require(XmlPullParser.START_TAG, null, "service")

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
            var origins: List<ServiceLocation>? = null
            var destinations: List<ServiceLocation>? = null
            var currentOrigins: List<ServiceLocation>? = null
            var currentDestinations: List<ServiceLocation>? = null
            var isCircularRoute: Boolean? = null
            var isCancelled: Boolean? = null
            var filterLocationCancelled: Boolean? = null
            var length: Int? = null
            var detachFront: Boolean? = null
            var isReverseFormation: Boolean? = null
            var cancelReason: String? = null
            var delayReason: String? = null
            var adhocAlerts: AdhocAlerts? = null
            var formation: FormationData? = null

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "sta" -> sta = parser.readAsText()
                    "eta" -> eta = parser.readAsText()
                    "std" -> std = parser.readAsText()
                    "etd" -> etd = parser.readAsText()
                    "platform" -> platform = parser.readAsText()
                    "operator" -> operator = parser.readAsText()
                    "operatorCode" -> operatorCode = parser.readAsText()
                    "serviceType" -> serviceType = parser.readAsText()
                    "serviceID" -> serviceID = parser.readAsText()
                    "rsid" -> rsid = parser.readAsText()
                    "origin" -> origins = ServiceLocations.fromXml(parser, "origin")
                    "destination" -> destinations = ServiceLocations.fromXml(parser, "destination")
                    "currentOrigins" -> currentOrigins = ServiceLocations.fromXml(parser, "currentOrigins")
                    "currentDestinations" -> currentDestinations = ServiceLocations.fromXml(parser, "currentDestinations")
                    "isCircularRoute" -> isCircularRoute = parser.readAsBoolean()
                    "isCancelled" -> isCancelled = parser.readAsBoolean()
                    "filterLocationCancelled" -> filterLocationCancelled = parser.readAsBoolean()
                    "length" -> length = parser.readAsInt()
                    "detachFront" -> detachFront = parser.readAsBoolean()
                    "isReverseFormation" -> isReverseFormation = parser.readAsBoolean()
                    "cancelReason" -> cancelReason = parser.readAsText()
                    "delayReason" -> delayReason = parser.readAsText()
                    "adhocAlerts" -> adhocAlerts = AdhocAlerts.fromXml(parser)
                    "formation" -> formation = FormationData.fromXml(parser)
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "service")

            return ServiceItem(
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
                origins,
                destinations,
                currentOrigins,
                currentDestinations,
                isCircularRoute,
                isCancelled,
                filterLocationCancelled,
                length,
                detachFront,
                isReverseFormation,
                cancelReason,
                delayReason,
                adhocAlerts,
                formation
            )
        }
    }
}