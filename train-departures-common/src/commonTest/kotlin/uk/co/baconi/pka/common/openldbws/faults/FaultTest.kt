package uk.co.baconi.pka.common.openldbws.faults

import uk.co.baconi.pka.common.openldbws.faults.Fault.Companion.fault
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class FaultTest {

    @Test
    fun `Should support 'faultcode, faultstring, detail' style faults`() {

        val fault = XmlDeserializer(
            """
            |<soap:Fault xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            |  <faultcode>soap:Client</faultcode>
            |  <faultstring>Server did not recognize the value of HTTP Header SOAPAction: http://thalesgroup.com/RTTI/2015-05-14/ldb/GetDepartureBoard.</faultstring>
            |  <detail />
            |</soap:Fault>
            """.trimMargin()
        ).fault()

        expect(FaultCodes.Client, "Fault Code") {
            fault.code
        }

        expectStartsWith("Server did not recognize the value of HTTP Header SOAPAction") {
            fault.reason
        }
    }

    @Test
    fun `Should support 'CodeReason, Detail' style faults`() {

        val fault = XmlDeserializer(
            """
            |<soap:Fault xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
            |  <soap:Code>
            |    <soap:Value>soap:Receiver</soap:Value>
            |  </soap:Code>
            |  <soap:Reason>
            |    <soap:Text xml:lang="en">Unexpected server error</soap:Text>
            |  </soap:Reason>
            |  <soap:Detail />
            |</soap:Fault>
            """.trimMargin()
        ).fault()

        expect(FaultCodes.Receiver, "Fault Code") {
            fault.code
        }

        expect("Unexpected server error", "Fault Reason") {
            fault.reason
        }
    }

    private fun expectStartsWith(expectedValue: String, actualValue: () -> String?) {
        val value = actualValue()
        expect(true, "Expected string [$value] to start with [$expectedValue]") {
            value?.startsWith(expectedValue)
        }
    }
}