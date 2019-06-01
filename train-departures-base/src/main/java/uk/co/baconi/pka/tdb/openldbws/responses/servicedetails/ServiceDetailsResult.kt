package uk.co.baconi.pka.tdb.openldbws.responses.servicedetails

import org.xmlpull.v1.XmlPullParser
import uk.co.baconi.pka.tdb.openldbws.responses.AdhocAlerts
import uk.co.baconi.pka.tdb.openldbws.responses.CallingPoints
import uk.co.baconi.pka.tdb.openldbws.responses.FormationData
import uk.co.baconi.pka.tdb.xml.readAsBoolean
import uk.co.baconi.pka.tdb.xml.readAsInt
import uk.co.baconi.pka.tdb.xml.readAsText
import uk.co.baconi.pka.tdb.xml.skip

data class ServiceDetailsResult(
    val generatedAt: String? = null,
    val serviceType: String? = null,
    val locationName: String? = null,
    val crs: String? = null,
    val operator: String? = null,
    val operatorCode: String? = null,
    val rsid: String? = null,
    val isCancelled: Boolean? = null,
    val cancelReason: String? = null,
    val delayReason: String? = null,
    val overdueMessage: String? = null,
    val length: Int? = null,
    val detachFront: Boolean? = null,
    val isReverseFormation: Boolean? = null,
    val platform: String? = null,
    val sta: String? = null,
    val eta: String? = null,
    val ata: String? = null,
    val std: String? = null,
    val etd: String? = null,
    val atd: String? = null,
    val adhocAlerts: List<String>? = null,
    val formation: FormationData? = null,
    val previousCallingPoints: List<CallingPoints>? = null,
    val subsequentCallingPoints: List<CallingPoints>? = null
) {

    companion object {

        internal fun fromXml(parser: XmlPullParser): ServiceDetailsResult {

            parser.require(XmlPullParser.START_TAG, null, "GetServiceDetailsResult")

            var result = ServiceDetailsResult()

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                when (parser.name) {
                    "generatedAt" -> result = result.copy(generatedAt = parser.readAsText())
                    "serviceType" -> result = result.copy(serviceType = parser.readAsText())
                    "locationName" -> result = result.copy(locationName = parser.readAsText())
                    "crs" -> result = result.copy(crs = parser.readAsText())
                    "operator" -> result = result.copy(operator = parser.readAsText())
                    "operatorCode" -> result = result.copy(operatorCode = parser.readAsText())
                    "rsid" -> result = result.copy(rsid = parser.readAsText())
                    "isCancelled" -> result = result.copy(isCancelled = parser.readAsBoolean())
                    "cancelReason" -> result = result.copy(cancelReason = parser.readAsText())
                    "delayReason" -> result = result.copy(delayReason = parser.readAsText())
                    "overdueMessage" -> result = result.copy(overdueMessage = parser.readAsText())
                    "length" -> result = result.copy(length = parser.readAsInt())
                    "detachFront" -> result = result.copy(detachFront = parser.readAsBoolean())
                    "isReverseFormation" -> result = result.copy(isReverseFormation = parser.readAsBoolean())
                    "platform" -> result = result.copy(platform = parser.readAsText())
                    "sta" -> result = result.copy(sta = parser.readAsText())
                    "eta" -> result = result.copy(eta = parser.readAsText())
                    "ata" -> result = result.copy(ata = parser.readAsText())
                    "std" -> result = result.copy(std = parser.readAsText())
                    "etd" -> result = result.copy(etd = parser.readAsText())
                    "atd" -> result = result.copy(atd = parser.readAsText())
                    "adhocAlerts" -> result = result.copy(adhocAlerts = AdhocAlerts.fromXml(parser))
                    "formation" -> result = result.copy(formation = FormationData.fromXml(parser))
                    "previousCallingPoints" -> result = result.copy(previousCallingPoints = CallingPoints.fromXml(parser, "previousCallingPoints"))
                    "subsequentCallingPoints" -> result = result.copy(subsequentCallingPoints = CallingPoints.fromXml(parser, "subsequentCallingPoints"))
                    else -> parser.skip()
                }
            }

            parser.require(XmlPullParser.END_TAG, null, "GetServiceDetailsResult")

            return result
        }
    }
}