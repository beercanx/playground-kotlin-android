package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.assertSoftly
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.TestExtensions.toResourceInputStream
import uk.co.baconi.pka.tdb.xml.XmlParser

class GetNextDeparturesSpec : StringSpec({

    "Should be able to deserialise example response of Sheffield to Cleethorpes" {

        val result: Envelope = Envelope.fromXml(
            XmlParser.fromReader(
                """
                |<?xml version="1.0" encoding="utf-8"?>
                |<soap:Envelope
                |    xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
                |    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                |    xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                |  <soap:Body>
                |    <GetNextDeparturesResponse
                |        xmlns="http://thalesgroup.com/RTTI/2017-10-01/ldb/">
                |      <DeparturesBoard
                |          xmlns:lt="http://thalesgroup.com/RTTI/2012-01-13/ldb/types"
                |          xmlns:lt6="http://thalesgroup.com/RTTI/2017-02-02/ldb/types"
                |          xmlns:lt7="http://thalesgroup.com/RTTI/2017-10-01/ldb/types"
                |          xmlns:lt4="http://thalesgroup.com/RTTI/2015-11-27/ldb/types"
                |          xmlns:lt5="http://thalesgroup.com/RTTI/2016-02-16/ldb/types"
                |          xmlns:lt2="http://thalesgroup.com/RTTI/2014-02-20/ldb/types"
                |          xmlns:lt3="http://thalesgroup.com/RTTI/2015-05-14/ldb/types">
                |        <lt4:generatedAt>2019-01-13T13:51:17.106902+00:00</lt4:generatedAt>
                |        <lt4:locationName>Sheffield</lt4:locationName>
                |        <lt4:crs>SHF</lt4:crs>
                |        <lt4:nrccMessages>
                |          <lt:message>Disruption between Bristol Temple Meads and Taunton via Weston-super-Mare. More information can be found in &lt;A href="http://nationalrail.co.uk/service_disruptions/210986.aspx "&gt;Latest Travel News.&lt;/A&gt;</lt:message>
                |        </lt4:nrccMessages>
                |        <lt4:platformAvailable>true</lt4:platformAvailable>
                |        <lt7:departures>
                |          <lt7:destination crs="CLE">
                |            <lt7:service>
                |              <lt4:sta>14:08</lt4:sta>
                |              <lt4:eta>On time</lt4:eta>
                |              <lt4:std>14:29</lt4:std>
                |              <lt4:etd>On time</lt4:etd>
                |              <lt4:platform>1B</lt4:platform>
                |              <lt4:operator>TransPennine Express</lt4:operator>
                |              <lt4:operatorCode>TP</lt4:operatorCode>
                |              <lt4:serviceType>train</lt4:serviceType>
                |              <lt4:serviceID>8ipq5Cv9fDMbR0rDg6riKA==</lt4:serviceID>
                |              <lt5:rsid>TP603200</lt5:rsid>
                |              <lt5:origin>
                |                <lt4:location>
                |                  <lt4:locationName>Manchester Airport</lt4:locationName>
                |                  <lt4:crs>MIA</lt4:crs>
                |                </lt4:location>
                |              </lt5:origin>
                |              <lt5:destination>
                |                <lt4:location>
                |                  <lt4:locationName>Cleethorpes</lt4:locationName>
                |                  <lt4:crs>CLE</lt4:crs>
                |                </lt4:location>
                |              </lt5:destination>
                |            </lt7:service>
                |          </lt7:destination>
                |        </lt7:departures>
                |      </DeparturesBoard>
                |    </GetNextDeparturesResponse>
                |  </soap:Body>
                |</soap:Envelope>
                """.trimMargin().reader()
            )
        )

        assertSoftly {

            result.body should beInstanceOf<BodySuccess>()

            val body = result.body as BodySuccess

            val departuresBoard = body.getNextDeparturesResponse?.departuresBoard
            departuresBoard?.generatedAt shouldBe "2019-01-13T13:51:17.106902+00:00"
            departuresBoard?.locationName shouldBe "Sheffield"
            departuresBoard?.crs shouldBe "SHF"
            departuresBoard?.platformAvailable shouldBe true

            departuresBoard?.nrccMessages?.first() shouldContain("Disruption between Bristol Temple Meads and Taunton via Weston-super-Mare")

            val departureItems = departuresBoard?.departures
            departureItems?.size shouldBe 1

            val departureItem = departureItems?.first()
            departureItem?.crs shouldBe "CLE"

            val service = departureItem?.service
            service?.sta shouldBe "14:08"
            service?.eta shouldBe "On time"
            service?.std shouldBe "14:29"
            service?.etd shouldBe "On time"
            service?.platform shouldBe "1B"
            service?.operator shouldBe "TransPennine Express"
            service?.operatorCode shouldBe "TP"
            service?.serviceType shouldBe "train"
            service?.serviceID shouldBe "8ipq5Cv9fDMbR0rDg6riKA=="
            service?.rsid shouldBe "TP603200"

            val origins = service?.origin
            origins?.size shouldBe 1

            val origin = origins?.first()
            origin?.locationName shouldBe "Manchester Airport"
            origin?.crs shouldBe "MIA"

            val destinations = service?.destination
            destinations?.size shouldBe 1

            val destination = destinations?.first()
            destination?.locationName shouldBe "Cleethorpes"
            destination?.crs shouldBe "CLE"
        }
    }
})