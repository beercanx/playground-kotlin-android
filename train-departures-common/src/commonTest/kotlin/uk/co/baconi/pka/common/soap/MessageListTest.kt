package uk.co.baconi.pka.common.soap

import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class MessageListTest {

    @Test
    fun `Should decode AdhocAlerts with no messages present`() {

        val verifyResults = { messages: List<String> ->
            expect(emptyList(), "AdhocAlerts") {
                messages
            }
        }

        XmlDeserializer("<adhocAlerts/>")
            .adhocAlerts()
            .also(verifyResults)

        XmlDeserializer("<adhocAlerts></adhocAlerts>")
            .adhocAlerts()
            .also(verifyResults)

        XmlDeserializer("<adhocAlerts><adhocAlertText/></adhocAlerts>")
            .adhocAlerts()
            .also(verifyResults)
    }

    @Test
    fun `Should decode AdhocAlerts with messages present`() {

        val testData = """
            |<adhocAlerts>
            |  <adhocAlertText>test-alert</adhocAlertText>
            |  <adhocAlertText>another-test-alert</adhocAlertText>
            |</adhocAlerts>""".trimMargin()

        XmlDeserializer(testData)
            .adhocAlerts()
            .also { messages: List<String> ->
                expect(listOf("test-alert", "another-test-alert"), "AdhocAlerts") {
                    messages
                }
            }
    }

    @Test
    fun `Should decode NRCCMessages with no messages present`() {

        val verifyResults = { messages: List<String> ->
            expect(emptyList(), "NRCCMessages") {
                messages
            }
        }

        XmlDeserializer("<nrccMessages/>")
            .nrccMessages()
            .also(verifyResults)

        XmlDeserializer("<nrccMessages></nrccMessages>")
            .nrccMessages()
            .also(verifyResults)

        XmlDeserializer("<nrccMessages><message/></nrccMessages>")
            .nrccMessages()
            .also(verifyResults)
    }

    @Test
    fun `Should decode NRCCMessages with messages present`() {

        val testData = """
            |<nrccMessages>
            |  <message>some-test-message</message>
            |  <message>some-other-test-message</message>
            |</nrccMessages>""".trimMargin()

        XmlDeserializer(testData)
            .nrccMessages()
            .also { messages: List<String> ->
                expect(listOf("some-test-message", "some-other-test-message"), "NRCCMessages") {
                    messages
                }
            }
    }
}