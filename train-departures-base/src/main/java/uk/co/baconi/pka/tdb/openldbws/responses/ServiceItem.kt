package uk.co.baconi.pka.tdb.openldbws.responses

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.xml.*

/**
 * Covers both ServiceItem and ServiceItemWithCallingPoints (for parents of type WithDetails)
 */
data class ServiceItem(
    val sta: String? = null, // Scheduled Time of Arrival [10:41]
    val eta: String? = null, // Estimated Time of Arrival [On time / 10:54]
    val std: String? = null, // Scheduled Time of Departure [10:41]
    val etd: String? = null, // Estimated Time of Departure [On time / 10:54]
    val platform: String? = null, // null / 1 / bus
    val operator: String? = null, // Northern
    val operatorCode: String? = null, // NT
    val serviceType: String? = null, // [bus / train / ferry]
    val serviceID: String? = null, // Base64 encoded string
    val rsid: String? = null, // Retail Service ID [NT044400]
    val origin: List<ServiceLocation>? = null,
    val destination: List<ServiceLocation>? = null,
    val currentOrigins: List<ServiceLocation>? = null,
    val currentDestinations: List<ServiceLocation>? = null,
    val isCircularRoute: Boolean? = null, // is operating on a circular route
    val isCancelled: Boolean? = null, // Cancelled at this location (from filter)
    val filterLocationCancelled: Boolean? = null, // Service is no longer stopping at destination (to filter)
    val length: Int? = null, // Number of train carriages {0 || null means unknown}
    val detachFront: Boolean? = null, // if the service detaches units from the front at this location
    val isReverseFormation: Boolean? = null, // if the service is operating in the reverse of its normal formation
    val cancelReason: String? = null,
    val delayReason: String? = null,
    val adhocAlerts: List<String>? = null,
    val formation: FormationData? = null,
    val previousCallingPoints: List<CallingPoints>? = null, // A list of lists of the subsequent calling points in the journey.
    val subsequentCallingPoints: List<CallingPoints>? = null // A list of lists of the previous calling points in the journey.
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): ServiceItem = parser.parse("service") { result ->
            when (parser.name) {
                "sta" -> result.copy(sta = parser.readAsText())
                "eta" -> result.copy(eta = parser.readAsText())
                "std" -> result.copy(std = parser.readAsText())
                "etd" -> result.copy(etd = parser.readAsText())
                "platform" -> result.copy(platform = parser.readAsText())
                "operator" -> result.copy(operator = parser.readAsText())
                "operatorCode" -> result.copy(operatorCode = parser.readAsText())
                "serviceType" -> result.copy(serviceType = parser.readAsText())
                "serviceID" -> result.copy(serviceID = parser.readAsText())
                "rsid" -> result.copy(rsid = parser.readAsText())
                "isCircularRoute" -> result.copy(isCircularRoute = parser.readAsBoolean())
                "isCancelled" -> result.copy(isCancelled = parser.readAsBoolean())
                "filterLocationCancelled" -> result.copy(filterLocationCancelled = parser.readAsBoolean())
                "length" -> result.copy(length = parser.readAsInt())
                "detachFront" -> result.copy(detachFront = parser.readAsBoolean())
                "isReverseFormation" -> result.copy(isReverseFormation = parser.readAsBoolean())
                "cancelReason" -> result.copy(cancelReason = parser.readAsText())
                "delayReason" -> result.copy(delayReason = parser.readAsText())
                "adhocAlerts" -> result.copy(adhocAlerts = AdhocAlerts.fromXml(parser))
                "formation" -> result.copy(formation = FormationData.fromXml(parser))
                "origin" -> result.copy(origin = ServiceLocations.fromXml(parser, "origin"))
                "destination" -> result.copy(destination = ServiceLocations.fromXml(parser, "destination"))
                "currentOrigins" -> result.copy(currentOrigins = ServiceLocations.fromXml(parser, "currentOrigins"))
                "currentDestinations" -> result.copy(currentDestinations = ServiceLocations.fromXml(parser, "currentDestinations"))
                "previousCallingPoints" -> result.copy(previousCallingPoints = CallingPoints.fromXml(parser, "previousCallingPoints"))
                "subsequentCallingPoints" -> result.copy(subsequentCallingPoints = CallingPoints.fromXml(parser, "subsequentCallingPoints"))
                else -> parser.skip(result)
            }
        }
    }
}