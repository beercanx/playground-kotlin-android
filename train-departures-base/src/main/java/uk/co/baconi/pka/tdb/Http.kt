package uk.co.baconi.pka.tdb

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import uk.co.baconi.pka.tdb.openldbws.requests.GetNextDeparturesRequest
import uk.co.baconi.pka.tdb.openldbws.requests.Request
import uk.co.baconi.pka.tdb.openldbws.responses.Envelope
import uk.co.baconi.pka.tdb.xml.XmlParser

object Http {

    suspend fun performGetNextDeparturesRequest(accessToken: AccessToken, from: StationCode, to: StationCode): Envelope {
        val request = GetNextDeparturesRequest(accessToken, from, to)
        val response = performSoapRequest<String>(request)
        val parser = XmlParser.fromReader(response.reader())
        return Envelope.fromXml(parser)
    }

    private suspend inline fun <reified T> performSoapRequest(request: Request): T = HttpClient(Android) {
        // Configuration
    }.use { client ->
        client.post("https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx") {
            headersOf("SOAPAction", request.action)
            body = TextContent(request.body, ContentType.parse(request.contentType))
        }
    }
}