package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.should
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class ServiceLocationsSpec : StringSpec({

    "Should decode with no locations present" {

        ServiceLocations.fromXml(
            XmlParser.fromReader(
                "<origin/>".reader()
            ),
            "origin"
        ) should beEmpty()

        ServiceLocations.fromXml(
            XmlParser.fromReader("<origin></origin>".reader()),
            "origin"
        ) should beEmpty()
    }

    "Should decode with location present" {

        ServiceLocations.fromXml(
            XmlParser.fromReader(
                "<origin><location/></origin>".reader()
            ),
            "origin"
        ).first() should beInstanceOf<ServiceLocation>()

        ServiceLocations.fromXml(
            XmlParser.fromReader(
                "<origin><location></location></origin>".reader()
            ),
            "origin"
        ).first() should beInstanceOf<ServiceLocation>()

        ServiceLocations.fromXml(
            XmlParser.fromReader(
                "<origin><location/><location/></origin>".reader()
            ),
            "origin"
        ) shouldHaveSize 2
    }
})