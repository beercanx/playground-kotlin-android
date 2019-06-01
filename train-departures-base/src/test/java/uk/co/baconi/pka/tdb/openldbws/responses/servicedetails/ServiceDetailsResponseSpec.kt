package uk.co.baconi.pka.tdb.openldbws.responses.servicedetails

import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class ServiceDetailsResponseSpec : StringSpec({

    "Should decode with no response present" {

        ServiceDetailsResponse.fromXml(
            XmlParser.fromReader(
                "<GetServiceDetailsResponse/>".reader()
            )
        ).serviceDetailsResult shouldBe null

        ServiceDetailsResponse.fromXml(
            XmlParser.fromReader(
                "<GetServiceDetailsResponse></GetServiceDetailsResponse>".reader()
            )
        ).serviceDetailsResult shouldBe null
    }

    "Should decode with response present" {

        ServiceDetailsResponse.fromXml(
            XmlParser.fromReader(
                "<GetServiceDetailsResponse><GetServiceDetailsResult/></GetServiceDetailsResponse>".reader()
            )
        ).serviceDetailsResult should beInstanceOf<ServiceDetailsResult>()

        ServiceDetailsResponse.fromXml(
            XmlParser.fromReader(
                "<GetServiceDetailsResponse><GetServiceDetailsResult></GetServiceDetailsResult></GetServiceDetailsResponse>".reader()
            )
        ).serviceDetailsResult should beInstanceOf<ServiceDetailsResult>()
    }
})