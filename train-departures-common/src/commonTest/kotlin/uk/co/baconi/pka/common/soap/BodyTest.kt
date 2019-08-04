package uk.co.baconi.pka.common.soap

import uk.co.baconi.pka.common.openldbws.details.ServiceDetails
import uk.co.baconi.pka.common.openldbws.faults.Fault
import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.XmlDeserializerException
import uk.co.baconi.pka.common.xml.skip
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class BodyTest {

    @Test
    fun `Should throw exception if body is empty`() {

        val exception = assertFailsWith<XmlDeserializerException> {
            XmlDeserializer("<Body/>")
                .body("empty") {
                    throw AssertionError("Parsed empty which shouldn't be possible")
                }
        }

        expect("Body contained no supported inner tag or was empty.", "Exception") {
            exception.message
        }
    }

    @Test
    fun `Should throw a fault when present`() {

        listOf(
            "<Body><Fault/></Body>",
            "<Body><Fault></Fault></Body>",
            "<Body><Fault/><empty/></Body>",
            "<Body><empty/><Fault/></Body>"
        ).forEach { body ->
            expect(Fault(), "Exception") {
                assertFailsWith {
                    XmlDeserializer(body)
                        .body("empty") {
                            skip(ServiceDetails())
                        }
                }
            }
        }
    }

    @Test
    fun `Should decode with GetServiceDetailsResponse present`() {

        XmlDeserializer("<Body><GetServiceDetailsResponse/></Body>")
            .body("GetServiceDetailsResponse") {
                skip(ServiceDetails())
            }.also { serviceDetails ->
                expect(ServiceDetails(), "ServiceDetails") {
                    serviceDetails
                }
            }

    }
}