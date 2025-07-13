package uk.co.baconi.pka.common.openldbws.requests

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.utils.io.InternalAPI
import uk.co.baconi.pka.common.AccessToken
import uk.co.baconi.pka.common.stations.StationCode
import uk.co.baconi.pka.common.openldbws.departures.DepartureBoard
import uk.co.baconi.pka.common.openldbws.departures.DepartureBoard.Companion.departureBoard
import uk.co.baconi.pka.common.openldbws.departures.Departures
import uk.co.baconi.pka.common.openldbws.departures.Departures.Companion.departures
import uk.co.baconi.pka.common.openldbws.details.ServiceDetails
import uk.co.baconi.pka.common.openldbws.details.ServiceDetails.Companion.serviceDetails
import uk.co.baconi.pka.common.soap.body
import uk.co.baconi.pka.common.soap.envelope
import uk.co.baconi.pka.common.soap.tag
import uk.co.baconi.pka.common.xml.*

@OptIn(InternalAPI::class)
class OpenLDBWSApi(private val client: HttpClient) {

    private val contentType = ContentType.parse("text/xml;charset=UTF-8")

    /**
     *  Sends a request to the OpenLDBWS api to get departures:
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
     *          <ldb:${type}Request>
     *              <ldb:crs>${from}</ldb:crs>
     *              <ldb:filterCrs>${to}</ldb:filterCrs>
     *              <ldb:filterType>to</ldb:filterType>
     *              <ldb:numRows>8</ldb:numRows>
     *              <ldb:timeOffset>0</ldb:timeOffset>
     *              <ldb:timeWindow>120</ldb:timeWindow>
     *          </ldb:${type}Request>
     *      </soap:Body>
     *  </soap:Envelope>
     */
    suspend fun getDepartures(accessToken: AccessToken, from: StationCode, to: StationCode, type: DeparturesType): Departures {
        return soapRequest(accessToken, type, {
            departureRequest(type, from, to)
        }, {
            body(type.responseTag) {
                tag(type.responseTag, "DeparturesBoard") {
                    departures()
                }
            }
        })
    }

    /**
     *  Sends a request to the OpenLDBWS api to get a departure board:
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
     *          <ldb:${type}Request>
     *              <ldb:crs>${from}</ldb:crs>
     *              <ldb:filterCrs>${to}</ldb:filterCrs>
     *              <ldb:filterType>to</ldb:filterType>
     *              <ldb:numRows>8</ldb:numRows>
     *              <ldb:timeOffset>0</ldb:timeOffset>
     *              <ldb:timeWindow>120</ldb:timeWindow>
     *          </ldb:${type}Request>
     *      </soap:Body>
     *  </soap:Envelope>
     */
    suspend fun getDepartureBoard(accessToken: AccessToken, from: StationCode, to: StationCode, type: DepartureBoardType): DepartureBoard {
        return soapRequest(accessToken, type, {
            departureRequest(type, from, to)
        }, {
            body(type.responseTag) {
                tag(type.responseTag, "GetStationBoardResult") {
                    departureBoard()
                }
            }
        })
    }

    /**
     *  Sends a request to the OpenLDBWS api to get service details:
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
    suspend fun getServiceDetails(accessToken: AccessToken, serviceID: String): ServiceDetails {
        return soapRequest(accessToken, DetailsType.ServiceDetails, {
            tag("ldb:GetServiceDetailsRequest") {
                tag("ldb:serviceID") {
                    text(serviceID)
                }
            }
        }, {
            body("GetServiceDetailsResponse") {
                tag("GetServiceDetailsResponse", "GetServiceDetailsResult") {
                    serviceDetails()
                }
            }
        })
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

    private fun XmlSerializer.departureRequest(type: RequestType, from: StationCode, to: StationCode) {
        tag("ldb:${type.requestTag}") {
            tag("ldb:crs") {
                text(from.crsCode)
            }
            if(type is DeparturesType) {
                tag("ldb:filterList") {
                    tag("ldb:crs") {
                        text(to.crsCode)
                    }
                }
            }
            if(type is DepartureBoardType) {
                tag("ldb:filterCrs") {
                    text(to.crsCode)
                }
                tag("ldb:filterType") {
                    text("to")
                }
                tag("ldb:numRows") {
                    text("8") // TODO - Probably look at making this dynamic based on devices screen size.
                }
            }
            tag("ldb:timeOffset") {
                text("0")
            }
            tag("ldb:timeWindow") {
                text("120")
            }
        }
    }

    private suspend fun <T> soapRequest(
        accessToken: AccessToken,
        type: RequestType,
        request: XmlSerializer.() -> Unit,
        response: XmlDeserializer.() -> T
    ): T = client.post {
        openLDBWSEndpoint()
        soapAction(type)
        body {
            build {
                soapRequest(accessToken) {
                    request()
                }
            }
        }
    }.bodyAsText().deserialize {
        envelope {
            response()
        }
    }
}