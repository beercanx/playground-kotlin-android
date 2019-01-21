package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.assertSoftly
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.matchers.string.shouldStartWith
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNot
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.TestExtensions.toResourceInputStream
import uk.co.baconi.pka.tdb.xml.XmlParser

class FaultSpec : StringSpec({

    "Should support [faultcode/faultstring/detail] style faults" {

        val result: Envelope = Envelope.fromXml(
            XmlParser.fromInputStream(
                "openldbws/responses/Soap_Fault_Unknown_Soap_Action.xml".toResourceInputStream()
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
            XmlParser.fromInputStream(
                "openldbws/responses/Soap_Fault_InternalServerError.xml".toResourceInputStream()
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