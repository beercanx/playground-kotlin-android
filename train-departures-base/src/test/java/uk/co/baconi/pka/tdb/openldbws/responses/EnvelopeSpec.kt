package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class EnvelopeSpec : StringSpec({

    "Should decode with no body present" {

        Envelope.fromXml(
            XmlParser.fromReader(
                "<Envelope/>".reader()
            )
        ).body shouldBe null

        Envelope.fromXml(
            XmlParser.fromReader(
                "<Envelope></Envelope>".reader()
            )
        ).body shouldBe null
    }

    "Should decode with body present" {

        Envelope.fromXml(
            XmlParser.fromReader(
                "<Envelope><Body/></Envelope>".reader()
            )
        ).body should beInstanceOf<BodyFailure>() // Because no success was provided
    }
})