package uk.co.baconi.pka.tdb

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import uk.co.baconi.pka.tdb.openldbws.requests.GetFastestDeparturesRequest
import uk.co.baconi.pka.tdb.openldbws.requests.GetNextDeparturesRequest
import uk.co.baconi.pka.tdb.openldbws.requests.Request
import uk.co.baconi.pka.tdb.openldbws.responses.BodySuccess
import uk.co.baconi.pka.tdb.openldbws.responses.Envelope
import uk.co.baconi.pka.tdb.openldbws.responses.BaseDeparturesResponse
import uk.co.baconi.pka.tdb.xml.XmlParser

object Actions {

    suspend fun getFastestDepartures(accessToken: AccessToken, from: StationCode, to: StationCode): BaseDeparturesResponse? {
        return getBody(GetFastestDeparturesRequest(accessToken, from, to))?.departuresResponse
    }

    suspend fun getNextDepartures(accessToken: AccessToken, from: StationCode, to: StationCode): BaseDeparturesResponse? {
        return getBody(GetNextDeparturesRequest(accessToken, from, to))?.departuresResponse
    }

    private suspend fun getBody(request: Request): BodySuccess? = try {
        val response = performSoapRequest<String>(request)
        val parser = XmlParser.fromReader(response.reader())
        val result = Envelope.fromXml(parser)
        when(result.body) {
            is BodySuccess -> result.body
            else -> null // TODO - Add in logging
        }
    } catch (exception: Exception) {
        // TODO - Add in logging
        null
    }

    // TODO - Look at supporting input streams and passing directly to XmlParser
    private suspend inline fun <reified T> performSoapRequest(request: Request): T = HttpClient(Android).use { client ->
        client.post("https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx") {
            headersOf("SOAPAction", request.action)
            body = TextContent(request.body, ContentType.parse(request.contentType))
        }
    }
}