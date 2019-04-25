package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.should
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class ServiceItemsSpec : StringSpec({

    "Should decode with no services present" {

        ServiceItems.fromXml(
            XmlParser.fromReader(
                "<origin/>".reader()
            ),
            "origin"
        ) should beEmpty()

        ServiceItems.fromXml(
            XmlParser.fromReader("<origin></origin>".reader()),
            "origin"
        ) should beEmpty()
    }

    "Should decode with services present" {

        ServiceItems.fromXml(
            XmlParser.fromReader(
                "<origin><service/></origin>".reader()
            ),
            "origin"
        ).first() should beInstanceOf<ServiceItem>()

        ServiceItems.fromXml(
            XmlParser.fromReader(
                "<origin><service></service></origin>".reader()
            ),
            "origin"
        ).first() should beInstanceOf<ServiceItem>()

        ServiceItems.fromXml(
            XmlParser.fromReader(
                "<origin><service/><service/></origin>".reader()
            ),
            "origin"
        ) shouldHaveSize 2
    }
})