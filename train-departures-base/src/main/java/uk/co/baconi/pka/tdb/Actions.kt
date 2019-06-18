package uk.co.baconi.pka.tdb

import android.util.Log
import arrow.core.Try
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import uk.co.baconi.pka.tdb.openldbws.requests.GetDepartureBoardRequest
import uk.co.baconi.pka.tdb.openldbws.requests.GetFastestDeparturesRequest
import uk.co.baconi.pka.tdb.openldbws.requests.GetServiceDetailsRequest
import uk.co.baconi.pka.tdb.openldbws.requests.Request
import uk.co.baconi.pka.tdb.openldbws.responses.*
import uk.co.baconi.pka.tdb.openldbws.responses.servicedetails.ServiceDetailsResult
import uk.co.baconi.pka.tdb.xml.SoapFailure
import uk.co.baconi.pka.tdb.xml.XmlParser

// TODO - Work out if this is a sensible pattern for Android
class Actions(
    private val accessToken: AccessToken,
    private val httpClient: HttpClient = HttpClient(Android)
) {

    companion object {

        private const val TAG = "Actions"

        suspend fun getFastestTrainDeparture(
            accessToken: AccessToken,
            from: StationCode,
            to: StationCode
        ): Try<List<ServiceItem>> {
            return Actions(accessToken).getFastestDepartures(from, to)
        }

        suspend fun getTrainDepartures(
            accessToken: AccessToken,
            from: StationCode,
            to: StationCode
        ): Try<List<ServiceItem>> {
            return Actions(accessToken).getTrainDepartures(from, to)
        }

        suspend fun getServiceDetails(accessToken: AccessToken, serviceId: String): Try<ServiceDetailsResult> {
            return Actions(accessToken).getServiceDetails(serviceId)
        }
    }

    suspend fun getFastestDepartures(from: StationCode, to: StationCode): Try<List<ServiceItem>> {
        return getBodyT(GetFastestDeparturesRequest(accessToken, from, to)){
            departuresResponse?.departuresBoard?.departures?.mapNotNull(DepartureItem::service)
        }
    }

    suspend fun getTrainDepartures(from: StationCode, to: StationCode): Try<List<ServiceItem>> {
        return Actions(accessToken).getBodyT(GetDepartureBoardRequest(accessToken, from, to)){
            departureBoardResponse?.stationBoardResult?.trainServices
        }
    }

    suspend fun getServiceDetails(serviceId: String): Try<ServiceDetailsResult> {
        return getBodyT(GetServiceDetailsRequest(accessToken, serviceId)) {
            serviceDetailsResponse?.serviceDetailsResult
        }
    }

    private suspend inline fun <reified A> getBodyT(request: Request, extract: BodySuccess.() -> A?): Try<A> = Try {
        val response = performSoapRequest<String>(request)
        val parser = XmlParser.fromReader(response.reader())
        val result = Envelope.fromXml(parser)
        when (result.body) {
            is BodySuccess -> when (val extracted = extract(result.body)) {
                is A -> extracted
                else -> throw SoapFailure("Unable to extract [${A::class}] from the body.")
            }
            is BodyFailure -> when (result.body.fault) {
                is Fault -> {
                    val message = "Decoded response that contained a failure body: ${result.body.fault}"
                    Log.e(TAG, message)
                    throw SoapFailure(message)
                }
                else -> {
                    val message = "Decoded response that contained a no known successful body."
                    Log.e(TAG, message)
                    throw SoapFailure(message)
                }
            }
            null -> {
                val message = "Decoded response that contained no body."
                Log.e(TAG, message)
                throw SoapFailure(message)
            }
        }
    }

    // TODO - Look at supporting input streams and passing directly to XmlParser
    private suspend inline fun <reified T> performSoapRequest(request: Request): T  {
        return httpClient.post("https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx") {
            headersOf("SOAPAction", request.action)
            body = TextContent(request.body, ContentType.parse(request.contentType))
        }
    }
}