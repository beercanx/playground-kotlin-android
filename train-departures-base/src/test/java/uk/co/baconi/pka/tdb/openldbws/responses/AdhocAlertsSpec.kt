package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.sequences.containExactly
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class AdhocAlertsSpec : StringSpec({

    "Should decode with no messages present" {

        AdhocAlerts.fromXml(
            XmlParser.fromReader(
                "<adhocAlerts/>".reader()
            )
        ) should beEmpty()

        AdhocAlerts.fromXml(
            XmlParser.fromReader(
                "<adhocAlerts></adhocAlerts>".reader()
            )
        ) should beEmpty()

        AdhocAlerts.fromXml(
            XmlParser.fromReader(
                "<adhocAlerts><adhocAlertText/></adhocAlerts>".reader()
            )
        ) should beEmpty()
    }

    "Should decode with messages present" {

        AdhocAlerts.fromXml(
            XmlParser.fromReader(
                """<adhocAlerts>
                  |    <adhocAlertText>test-alert</adhocAlertText>
                  |    <adhocAlertText>another-test-alert</adhocAlertText>
                  |</adhocAlerts>""".trimMargin().reader()
            )
        ) shouldContainExactly listOf("test-alert", "another-test-alert")
    }
})