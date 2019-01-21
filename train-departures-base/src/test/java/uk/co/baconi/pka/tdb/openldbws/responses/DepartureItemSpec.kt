package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class DepartureItemSpec : StringSpec({

    "Should decode with no csr or service present" {

        DepartureItem.fromXml(
            XmlParser.fromReader(
                "<destination/>".reader()
            )
        ).let {
            it.crs shouldBe null
            it.service shouldBe null
        }

        DepartureItem.fromXml(
            XmlParser.fromReader(
                "<destination></destination>".reader()
            )
        ).let {
            it.crs shouldBe null
            it.service shouldBe null
        }
    }

    "Should decode with no service present" {

        DepartureItem.fromXml(
            XmlParser.fromReader(
                """<destination crs="test-crs-closed"/>""".reader()
            )
        ).let {
            it.crs shouldBe "test-crs-closed"
            it.service shouldBe null
        }

        DepartureItem.fromXml(
            XmlParser.fromReader(
                """<destination crs="test-crs-open"></destination>""".reader()
            )
        ).let {
            it.crs shouldBe "test-crs-open"
            it.service shouldBe null
        }
    }

    "Should decode with service present" {

        DepartureItem.fromXml(
            XmlParser.fromReader(
                "<destination><service/></destination>".reader()
            )
        ).service should beInstanceOf<ServiceItem>()

        DepartureItem.fromXml(
            XmlParser.fromReader(
                "<destination><service></service></destination>".reader()
            )
        ).service should beInstanceOf<ServiceItem>()
    }
})