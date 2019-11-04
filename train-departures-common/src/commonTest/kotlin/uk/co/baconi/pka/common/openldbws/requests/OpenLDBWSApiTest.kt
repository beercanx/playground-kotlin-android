package uk.co.baconi.pka.common.openldbws.requests

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.call
import io.ktor.client.call.receive
import io.ktor.client.request.HttpRequestBuilder
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

    private val mockHttpClient = mockk<HttpClient>(relaxed = true)
    private val mockHttpClientCall = mockk<HttpClientCall>(relaxed = true)
    private val underTest = OpenLDBWSApi(mockHttpClient)

    private val accessToken = AccessToken("access-token")
    private val from = StationCode(stationName = "Meadowhall", crsCode = "MHS")
    private val to = StationCode(stationName = "Sheffield", crsCode = "SHF")

    @Test
    fun `Should support getting departures`() = runTest {

        val httpRequestBuilderSlot = slot<HttpRequestBuilder>()

        mockkStatic("io.ktor.client.call.UtilsKt")
        coEvery { mockHttpClient.call(capture(httpRequestBuilderSlot)) } returns mockHttpClientCall
        coEvery { mockHttpClientCall.receive<String>() } returns """
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
        """.trimMargin()

        underTest.getDepartures(accessToken, from, to, DeparturesType.NextDepartures) shouldBe Departures()

        val httpRequestBuilder = httpRequestBuilderSlot.captured
        httpRequestBuilder.method shouldBe HttpMethod.Post
        httpRequestBuilder.url.buildString() shouldBe "https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx"
        httpRequestBuilder.headers["SOAPAction"] shouldBe "http://thalesgroup.com/RTTI/2015-05-14/ldb/GetNextDepartures"
        httpRequestBuilder.body::class shouldBe TextContent::class
        (httpRequestBuilder.body as TextContent).contentType shouldBe ContentType.Text.Xml.withParameter("charset", "UTF-8")
        (httpRequestBuilder.body as TextContent).text shouldBe """
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

        val httpRequestBuilderSlot = slot<HttpRequestBuilder>()

        mockkStatic("io.ktor.client.call.UtilsKt")
        coEvery { mockHttpClient.call(capture(httpRequestBuilderSlot)) } returns mockHttpClientCall
        coEvery { mockHttpClientCall.receive<String>() } returns """
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
        """.trimMargin()

        underTest.getDepartureBoard(accessToken, from, to, DepartureBoardType.DepartureBoard) shouldBe DepartureBoard()

        val httpRequestBuilder = httpRequestBuilderSlot.captured
        httpRequestBuilder.method shouldBe HttpMethod.Post
        httpRequestBuilder.url.buildString() shouldBe "https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx"
        httpRequestBuilder.headers["SOAPAction"] shouldBe "http://thalesgroup.com/RTTI/2012-01-13/ldb/GetDepartureBoard"
        httpRequestBuilder.body::class shouldBe TextContent::class
        (httpRequestBuilder.body as TextContent).contentType shouldBe ContentType.Text.Xml.withParameter("charset", "UTF-8")
        (httpRequestBuilder.body as TextContent).text shouldBe """
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

        val httpRequestBuilderSlot = slot<HttpRequestBuilder>()

        mockkStatic("io.ktor.client.call.UtilsKt")
        coEvery { mockHttpClient.call(capture(httpRequestBuilderSlot)) } returns mockHttpClientCall
        coEvery { mockHttpClientCall.receive<String>() } returns """
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
        """.trimMargin()

        underTest.getServiceDetails(accessToken, "test-service-id") shouldBe ServiceDetails()

        val httpRequestBuilder = httpRequestBuilderSlot.captured
        httpRequestBuilder.method shouldBe HttpMethod.Post
        httpRequestBuilder.url.buildString() shouldBe "https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb11.asmx"
        httpRequestBuilder.headers["SOAPAction"] shouldBe "http://thalesgroup.com/RTTI/2012-01-13/ldb/GetServiceDetails"
        httpRequestBuilder.body::class shouldBe TextContent::class
        (httpRequestBuilder.body as TextContent).contentType shouldBe ContentType.Text.Xml.withParameter("charset", "UTF-8")
        (httpRequestBuilder.body as TextContent).text shouldBe """
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