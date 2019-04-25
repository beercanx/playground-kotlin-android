package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.assertSoftly
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.matchers.string.shouldStartWith
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNot
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser

class FaultSpec : StringSpec({

    "Should support [faultcode/faultstring/detail] style faults" {

        val result: Envelope = Envelope.fromXml(
            XmlParser.fromReader(
                """
                |<?xml version="1.0" encoding="utf-8"?>
                |<soap:Envelope
                |    xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"
                |    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                |    xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                |  <soap:Body>
                |    <soap:Fault>
                |      <faultcode>soap:Client</faultcode>
                |      <faultstring>Server did not recognize the value of HTTP Header SOAPAction: http://thalesgroup.com/RTTI/2015-05-14/ldb/GetDepartureBoard.</faultstring>
                |      <detail />
                |    </soap:Fault>
                |  </soap:Body>
                |</soap:Envelope>
                """.trimMargin().reader()
            )
        )

        result.body should beInstanceOf<BodyFailure>()
        result.body shouldNot beInstanceOf<BodySuccess>()

        val body = result.body as BodyFailure

        assertSoftly {
            body.fault?.code shouldBe FaultCodes.Client
            body.fault?.reason shouldStartWith "Server did not recognize the value of HTTP Header SOAPAction"
        }
    }

    "Should support [Code/Reason/Detail] style faults" {

        val result: Envelope = Envelope.fromXml(
            XmlParser.fromReader(
                """
                |<?xml version="1.0" encoding="utf-8"?>
                |<soap:Envelope
                |    xmlns:soap="http://www.w3.org/2003/05/soap-envelope"
                |    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                |    xmlns:xsd="http://www.w3.org/2001/XMLSchema">
                |  <soap:Body>
                |    <soap:Fault>
                |      <soap:Code>
                |        <soap:Value>soap:Receiver</soap:Value>
                |      </soap:Code>
                |      <soap:Reason>
                |        <soap:Text xml:lang="en">Unexpected server error</soap:Text>
                |      </soap:Reason>
                |      <soap:Detail />
                |    </soap:Fault>
                |  </soap:Body>
                |</soap:Envelope>
                """.trimMargin().reader()
            )
        )

        result.body should beInstanceOf<BodyFailure>()
        result.body shouldNot beInstanceOf<BodySuccess>()

        val body = result.body as BodyFailure

        assertSoftly {
            body.fault?.code shouldBe FaultCodes.Receiver
            body.fault?.reason shouldBe "Unexpected server error"
        }
    }
})