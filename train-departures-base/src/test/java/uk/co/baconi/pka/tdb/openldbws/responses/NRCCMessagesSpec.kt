package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.containAll
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class NRCCMessagesSpec : StringSpec({

    "Should decode with no messages present" {

        NRCCMessages.fromXml(
            XmlParser.fromReader(
                "<nrccMessages/>".reader()
            )
        ) shouldBe beEmpty<String>()

        NRCCMessages.fromXml(
            XmlParser.fromReader(
                "<nrccMessages></nrccMessages>".reader()
            )
        ) shouldBe beEmpty<String>()

        NRCCMessages.fromXml(
            XmlParser.fromReader(
                "<nrccMessages><message/></nrccMessages>".reader()
            )
        ) shouldBe beEmpty<String>()
    }

    "Should decode with messages present" {

        NRCCMessages.fromXml(
            XmlParser.fromReader(
                """<nrccMessages>
                   |   <message>some-test-message</message>
                   |   <message>some-other-test-message</message>
                   |</nrccMessages>""".trimMargin().reader()
            )
        ) shouldContainExactly listOf("some-test-message", "some-other-test-message")
    }
})