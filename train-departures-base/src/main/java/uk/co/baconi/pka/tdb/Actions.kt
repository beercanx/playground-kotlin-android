package uk.co.baconi.pka.tdb

import android.util.Log
import arrow.core.Try
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import uk.co.baconi.pka.tdb.openldbws.requests.*
import uk.co.baconi.pka.tdb.openldbws.responses.*
import uk.co.baconi.pka.tdb.openldbws.responses.servicedetails.ServiceDetailsResult
import uk.co.baconi.pka.tdb.xml.SoapFailure
import uk.co.baconi.pka.tdb.xml.XmlParser

object Actions {

    private const val TAG = "Actions"

    suspend fun getFastestDepartures(accessToken: AccessToken, from: StationCode, to: StationCode): BaseDeparturesResponse? {
        return getBody(GetFastestDeparturesRequest(accessToken, from, to))?.departuresResponse
    }

    suspend fun getNextDepartures(accessToken: AccessToken, from: StationCode, to: StationCode): BaseDeparturesResponse? {
        return getBody(GetNextDeparturesRequest(accessToken, from, to))?.departuresResponse
    }

    suspend fun getDepartureBoard(accessToken: AccessToken, from: StationCode, to: StationCode): BaseDepartureBoardResponse? {
        return getBody(GetDepartureBoardRequest(accessToken, from, to))?.departureBoardResponse
    }

    suspend fun getServiceDetails(accessToken: AccessToken, serviceId: String): Try<ServiceDetailsResult> {
        return getBodyT(GetServiceDetailsRequest(accessToken, serviceId)) {
            serviceDetailsResponse?.serviceDetailsResult
        }
    }

    private suspend inline fun <reified A> getBodyT(request: Request, extract: BodySuccess.() -> A?): Try<A> = Try {
        val response = performSoapRequest<String>(request)
        val parser = XmlParser.fromReader(response.reader())
        val result = Envelope.fromXml(parser)
        when(result.body) {
            is BodySuccess -> when(val extracted = extract(result.body)) {
                is A -> extracted
                else -> throw SoapFailure("Unable to extract [${A::class.java}] from the body.")
            }
            is BodyFailure ->  when(result.body.fault) {
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

    @Deprecated("Since 2019/06/01, looking to move to Try with Success/Failure")
    private suspend fun getBody(request: Request): BodySuccess? = try {
        val response = performSoapRequest<String>(request)
        val parser = XmlParser.fromReader(response.reader())
        val result = Envelope.fromXml(parser)
        when(result.body) {
            is BodySuccess -> result.body
            is BodyFailure ->  when(result.body.fault) {
                is Fault -> {
                    Log.e(TAG, "Decoded response that contained a failure body: ${result.body.fault}")
                    null // TODO - Reconsider using nulls, probably want to surface in the UI what went wrong
                }
                else -> {
                    Log.e(TAG, "Decoded response that contained a no known successful body.")
                    null // TODO - Reconsider using nulls, probably want to surface in the UI what went wrong
                }
            }
            null -> {
                Log.e(TAG, "Decoded response that contained no body.")
                null // TODO - Reconsider using nulls, probably want to surface in the UI what went wrong
            }
        }
    } catch (exception: Exception) {
        Log.e(TAG, "Issues communicating with the OpenLDBWS service.", exception)
        null // TODO - Reconsider using nulls, probably want to surface in the UI what went wrong
    }

    // TODO - Look at supporting input streams and passing directly to XmlParser
    private suspend inline fun <reified T> performSoapRequest(request: Request): T = HttpClient(Android).use { client ->
        client.post("https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx") {
            headersOf("SOAPAction", request.action)
            body = TextContent(request.body, ContentType.parse(request.contentType))
        }
    }
}