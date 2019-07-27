package uk.co.baconi.pka.common.openldbws.requests

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import uk.co.baconi.pka.common.AccessToken
import uk.co.baconi.pka.common.openldbws.details.ServiceDetails

//internal expect fun departuresRequest(type: DepartureBoardType, accessToken: AccessToken, serviceID: String): String
//internal expect fun departuresResponse(type: DepartureBoardType, response: String): Departures

//internal expect fun departureBoardRequest(type: DepartureBoardType, accessToken: AccessToken, serviceID: String): String
//internal expect fun departureBoardResponse(type: DepartureBoardType, response: String): DepartureBoard

internal expect fun serviceDetailsRequest(accessToken: AccessToken, serviceID: String): String
internal expect fun serviceDetailsResponse(response: String): ServiceDetails

class OpenLDBWSApi(private val client: HttpClient) {

    private val contentType = ContentType.parse("text/xml;charset=UTF-8")

    //suspend fun getDepartures(accessToken: AccessToken, from: StationCode, to: StationCode, type: DeparturesType): Departures

    //suspend fun getDepartureBoard(accessToken: AccessToken, from: StationCode, to: StationCode, type: DepartureBoardType): DepartureBoard

    /**
     * @throws uk.co.baconi.pka.common.openldbws.faults.Fault
     */
    suspend fun getServiceDetails(accessToken: AccessToken, serviceID: String): ServiceDetails = client.post<String> {
        openLDBWSEndpoint()
        soapAction(DetailsType.ServiceDetails)
        body(serviceDetailsRequest(accessToken, serviceID))
    }.let { response ->
        serviceDetailsResponse(response)
    }

    private fun HttpRequestBuilder.soapAction(type: RequestType) {
        header("SOAPAction", type.action)
    }

    private fun HttpRequestBuilder.body(request: String) {
        body = TextContent(request, contentType)
    }

    private fun HttpRequestBuilder.openLDBWSEndpoint() {
        url("https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx")
    }
}