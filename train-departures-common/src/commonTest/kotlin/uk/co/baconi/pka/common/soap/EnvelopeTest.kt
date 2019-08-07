package uk.co.baconi.pka.common.soap

import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.XmlDeserializerException
import uk.co.baconi.pka.common.xml.skip
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class EnvelopeTest {

    @Test
    fun `Should throw exception if envelope is empty`() {

        listOf(
            "<Envelope/>",
            "<Envelope></Envelope>",
            "<Envelope><empty/></Envelope>"
        ).forEach { body ->
            expect("Envelope contained no supported inner tag or was empty.", "Exception") {
                assertFailsWith<XmlDeserializerException> {
                    XmlDeserializer(body)
                        .envelope {
                            skip("fake-body")
                        }
                }.message
            }
        }
    }

    @Test
    fun `Should decode with Body present`() {

        XmlDeserializer("<Envelope><Body/></Envelope>")
            .envelope {
                skip("fake-body")
            }.also { body ->
                expect("fake-body", "Fake Body") {
                    body
                }
            }

    }
}