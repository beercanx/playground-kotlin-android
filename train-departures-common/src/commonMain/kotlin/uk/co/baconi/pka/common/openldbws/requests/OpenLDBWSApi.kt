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
import uk.co.baconi.pka.common.openldbws.details.ServiceDetails.Companion.serviceDetails
import uk.co.baconi.pka.common.soap.body
import uk.co.baconi.pka.common.soap.envelope
import uk.co.baconi.pka.common.soap.tag
import uk.co.baconi.pka.common.xml.*

class OpenLDBWSApi(private val client: HttpClient) {

    private val contentType = ContentType.parse("text/xml;charset=UTF-8")

    //suspend fun getDepartures(accessToken: AccessToken, from: StationCode, to: StationCode, type: DeparturesType): Departures

    //suspend fun getDepartureBoard(accessToken: AccessToken, from: StationCode, to: StationCode, type: DepartureBoardType): DepartureBoard

    /**
     *  Example XML request
     *
     *  <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
     *                 xmlns:typ="http://thalesgroup.com/RTTI/2013-11-28/Token/types"
     *                 xmlns:ldb="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
     *      <soap:Header>
     *          <typ:AccessToken>
     *              <typ:TokenValue>${accessToken}</typ:TokenValue>
     *          </typ:AccessToken>
     *      </soap:Header>
     *      <soap:Body>
     *          <ldb:GetServiceDetailsRequest>
     *              <ldb:serviceID>${serviceID}</ldb:serviceID>
     *          </ldb:GetServiceDetailsRequest>
     *      </soap:Body>
     *  </soap:Envelope>
     *
     *  @throws uk.co.baconi.pka.common.openldbws.faults.Fault
     *  @throws Exception
     */
    suspend fun getServiceDetails(accessToken: AccessToken, serviceID: String): ServiceDetails = client.post<String> {
        openLDBWSEndpoint()
        soapAction(DetailsType.ServiceDetails)
        body {
            build {
                soapRequest(accessToken) {
                    tag("ldb:GetServiceDetailsRequest") {
                        tag("ldb:serviceID") {
                            text(serviceID)
                        }
                    }
                }
            }
        }
    }.deserialize {
        envelope {
            body("GetServiceDetailsResponse") {
                tag("GetServiceDetailsResponse", "GetServiceDetailsResult") {
                    serviceDetails()
                }
            }
        }
    }

    private fun HttpRequestBuilder.soapAction(type: RequestType) {
        header("SOAPAction", type.action)
    }

    private fun HttpRequestBuilder.body(request: XmlSerializer.() -> String) {
        body = TextContent(XmlSerializer().request(), contentType)
    }

    private fun HttpRequestBuilder.openLDBWSEndpoint() {
        url("https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx")
    }

    private fun <T> String.deserialize(process: XmlDeserializer.() -> T) = XmlDeserializer(this).process()
}