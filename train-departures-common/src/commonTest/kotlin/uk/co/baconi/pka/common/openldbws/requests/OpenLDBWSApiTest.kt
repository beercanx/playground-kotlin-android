package uk.co.baconi.pka.common.openldbws.requests

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.TextContent
import io.mockk.*
import uk.co.baconi.pka.common.AccessToken
import uk.co.baconi.pka.common.BaseTest
import uk.co.baconi.pka.common.stations.StationCode
import uk.co.baconi.pka.common.openldbws.departures.DepartureBoard
import uk.co.baconi.pka.common.openldbws.departures.Departures
import uk.co.baconi.pka.common.openldbws.details.ServiceDetails
import uk.co.baconi.pka.common.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class OpenLDBWSApiTest : BaseTest {

    private val accessToken = AccessToken("access-token")
    private val from = StationCode(stationName = "Meadowhall", crsCode = "MHS")
    private val to = StationCode(stationName = "Sheffield", crsCode = "SHF")

    @Test
    fun `Should support getting departures`() = runTest {

        val httpRequestDataSlot = slot<HttpRequestData>()

        val mockEngine = MockEngine { request ->
            httpRequestDataSlot.captured = request
            respondOk("""
            |<?xml version="1.0" encoding="utf-8"?>
            |<soap:Envelope
            |   xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
            |   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            |   xmlns:xsd="http://www.w3.org/2001/XMLSchema">
            |   <soap:Body>
            |       <GetNextDeparturesResponse>
            |           <DeparturesBoard/>
            |       </GetNextDeparturesResponse>
            |   </soap:Body>
            |</soap:Envelope>
        """.trimMargin())
        }

        val underTest = OpenLDBWSApi(HttpClient(mockEngine))
        underTest.getDepartures(accessToken, from, to, DeparturesType.NextDepartures) shouldBe Departures()

        val httpRequestData = httpRequestDataSlot.captured
        httpRequestData.method shouldBe HttpMethod.Post
        httpRequestData.url.toString() shouldBe "https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx"
        httpRequestData.headers["SOAPAction"] shouldBe "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDepartures"
        httpRequestData.body::class shouldBe TextContent::class
        (httpRequestData.body as TextContent).contentType shouldBe ContentType.Text.Xml.withParameter("charset", "UTF-8")
        (httpRequestData.body as TextContent).text shouldBe """
            <?xml version='1.0' encoding='utf-8' ?>
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:typ="http://thalesgroup.com/RTTI/2013-11-28/Token/types" xmlns:ldb="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
                <soap:Header>
                    <typ:AccessToken>
                        <typ:TokenValue>access-token</typ:TokenValue>
                    </typ:AccessToken>
                </soap:Header>
                <soap:Body>
                    <ldb:GetNextDeparturesRequest>
                        <ldb:crs>MHS</ldb:crs>
                        <ldb:filterList>
                            <ldb:crs>SHF</ldb:crs>
                        </ldb:filterList>
                        <ldb:timeOffset>0</ldb:timeOffset>
                        <ldb:timeWindow>120</ldb:timeWindow>
                    </ldb:GetNextDeparturesRequest>
                </soap:Body>
            </soap:Envelope>
        """.trimFlat()
    }

    @Test
    fun `Should support getting a departure board`() = runTest {

        val httpRequestDataSlot = slot<HttpRequestData>()

        val mockEngine = MockEngine { request ->
            httpRequestDataSlot.captured = request
            respondOk("""
            |<?xml version="1.0" encoding="utf-8"?>
            |<soap:Envelope
            |   xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
            |   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            |   xmlns:xsd="http://www.w3.org/2001/XMLSchema">
            |   <soap:Body>
            |       <GetDepartureBoardResponse>
            |           <GetStationBoardResult/>
            |       </GetDepartureBoardResponse>
            |   </soap:Body>
            |</soap:Envelope>
        """.trimMargin())
        }

        val underTest = OpenLDBWSApi(HttpClient(mockEngine))
        underTest.getDepartureBoard(accessToken, from, to, DepartureBoardType.DepartureBoard) shouldBe DepartureBoard()

        val httpRequestData = httpRequestDataSlot.captured
        httpRequestData.method shouldBe HttpMethod.Post
        httpRequestData.url.toString() shouldBe "https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx"
        httpRequestData.headers["SOAPAction"] shouldBe "http://thalesgroup.com/RTTI/2012-01-13/ldb/GetDepartureBoard"
        httpRequestData.body::class shouldBe TextContent::class
        (httpRequestData.body as TextContent).contentType shouldBe ContentType.Text.Xml.withParameter("charset", "UTF-8")
        (httpRequestData.body as TextContent).text shouldBe """
            <?xml version='1.0' encoding='utf-8' ?>
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:typ="http://thalesgroup.com/RTTI/2013-11-28/Token/types" xmlns:ldb="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
                <soap:Header>
                    <typ:AccessToken>
                        <typ:TokenValue>access-token</typ:TokenValue>
                    </typ:AccessToken>
                </soap:Header>
                <soap:Body>
                    <ldb:GetDepartureBoardRequest>
                        <ldb:crs>MHS</ldb:crs>
                        <ldb:filterCrs>SHF</ldb:filterCrs>
                        <ldb:filterType>to</ldb:filterType>
                        <ldb:numRows>8</ldb:numRows>
                        <ldb:timeOffset>0</ldb:timeOffset>
                        <ldb:timeWindow>120</ldb:timeWindow>
                    </ldb:GetDepartureBoardRequest>
                </soap:Body>
            </soap:Envelope>
        """.trimFlat()
    }

    @Test
    fun `Should support getting service details`() = runTest {

        val httpRequestDataSlot = slot<HttpRequestData>()

        val mockEngine = MockEngine { request ->
            httpRequestDataSlot.captured = request
            respondOk("""
            |<?xml version="1.0" encoding="utf-8"?>
            |<soap:Envelope
            |   xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
            |   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            |   xmlns:xsd="http://www.w3.org/2001/XMLSchema">
            |   <soap:Body>
            |       <GetServiceDetailsResponse>
            |           <GetServiceDetailsResult/>
            |       </GetServiceDetailsResponse>
            |   </soap:Body>
            |</soap:Envelope>
        """.trimMargin())
        }

        val underTest = OpenLDBWSApi(HttpClient(mockEngine))
        underTest.getServiceDetails(accessToken, "test-service-id") shouldBe ServiceDetails()

        val httpRequestData = httpRequestDataSlot.captured
        httpRequestData.method shouldBe HttpMethod.Post
        httpRequestData.url.toString() shouldBe "https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx"
        httpRequestData.headers["SOAPAction"] shouldBe "http://thalesgroup.com/RTTI/2012-01-13/ldb/GetServiceDetails"
        httpRequestData.body::class shouldBe TextContent::class
        (httpRequestData.body as TextContent).contentType shouldBe ContentType.Text.Xml.withParameter("charset", "UTF-8")
        (httpRequestData.body as TextContent).text shouldBe """
            <?xml version='1.0' encoding='utf-8' ?>
            <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:typ="http://thalesgroup.com/RTTI/2013-11-28/Token/types" xmlns:ldb="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
                <soap:Header>
                    <typ:AccessToken>
                        <typ:TokenValue>access-token</typ:TokenValue>
                    </typ:AccessToken>
                </soap:Header>
                <soap:Body>
                    <ldb:GetServiceDetailsRequest>
                        <ldb:serviceID>test-service-id</ldb:serviceID>
                    </ldb:GetServiceDetailsRequest>
                </soap:Body>
            </soap:Envelope>
        """.trimFlat()
    }

    @BeforeTest
    fun before() {
        clearAllMocks()
    }
}