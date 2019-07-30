package uk.co.baconi.pka.common.openldbws.details

import uk.co.baconi.pka.common.openldbws.details.ServiceDetails.Companion.serviceDetails
import uk.co.baconi.pka.common.openldbws.services.CallingPoints
import uk.co.baconi.pka.common.openldbws.services.FormationData
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class ServiceDetailsTest {

    @Test
    fun `Should decode with no fields present`() {

        val verifyResults = { serviceDetails: ServiceDetails ->
            expect(null, "Generated At") {
                serviceDetails.generatedAt
            }
            expect(null, "Adhoc Alerts") {
                serviceDetails.adhocAlerts
            }
        }

        XmlDeserializer("<GetServiceDetailsResult/>")
            .serviceDetails()
            .also(verifyResults)

        XmlDeserializer("<GetServiceDetailsResult></GetServiceDetailsResult>")
            .serviceDetails()
            .also(verifyResults)
    }

    @Test
    fun `Should decode with each field present`() {
        expectField("generatedAt", "test-generatedAt-value", ServiceDetails::generatedAt)
        expectField("serviceType", "test-serviceType-value", ServiceDetails::serviceType)
        expectField("locationName", "test-locationName-value", ServiceDetails::locationName)
        expectField("crs", "test-crs-value", ServiceDetails::crs)
        expectField("operator", "test-operator-value", ServiceDetails::operator)
        expectField("operatorCode", "test-operatorCode-value", ServiceDetails::operatorCode)
        expectField("rsid", "test-rsid-value", ServiceDetails::rsid)
        expectField("isCancelled", false, ServiceDetails::isCancelled)
        expectField("cancelReason", "test-cancelReason-value", ServiceDetails::cancelReason)
        expectField("delayReason", "test-delayReason-value", ServiceDetails::delayReason)
        expectField("overdueMessage", "test-overdueMessage-value", ServiceDetails::overdueMessage)
        expectField("length", 0, ServiceDetails::length)
        expectField("detachFront", false, ServiceDetails::detachFront)
        expectField("isReverseFormation", false, ServiceDetails::isReverseFormation)
        expectField("platform", "test-platform-value", ServiceDetails::platform)
        expectField("sta", "test-sta-value", ServiceDetails::scheduledArrivalTime)
        expectField("eta", "test-eta-value", ServiceDetails::estimatedArrivalTime)
        expectField("ata", "test-ata-value", ServiceDetails::actualArrivalTime)
        expectField("std", "test-std-value", ServiceDetails::scheduledDepartureTime)
        expectField("etd", "test-etd-value", ServiceDetails::estimatedDepartureTime)
        expectField("atd", "test-atd-value", ServiceDetails::actualDepartureTime)
    }

    @Test
    fun `Should decode with adhocAlerts field present`() {
        expect(emptyList(), "adhocAlerts") {
            decodeAndGetField("adhocAlerts", "", ServiceDetails::adhocAlerts)
        }
        expect(emptyList(), "adhocAlerts") {
            decodeAndGetField("adhocAlerts", "<adhocAlertText/>", ServiceDetails::adhocAlerts)
        }
        expect(emptyList(), "adhocAlerts") {
            decodeAndGetField("adhocAlerts", "<adhocAlertText></adhocAlertText>", ServiceDetails::adhocAlerts)
        }
    }

    @Test
    fun `Should decode with formation field present`() {
        expect(FormationData(), "formation") {
            decodeAndGetField("formation", "", ServiceDetails::formation)
        }
        expect(FormationData(), "formation") {
            decodeAndGetField("formation", "<formation/>", ServiceDetails::formation)
        }
        expect(FormationData(), "formation") {
            decodeAndGetField("formation", "<formation></formation>", ServiceDetails::formation)
        }
    }

    @Test
    fun `Should decode with calling point fields present`() {
        expect(emptyList(), "previousCallingPoints") {
            decodeAndGetField("previousCallingPoints", "", ServiceDetails::previousCallingPoints)
        }
        expect(listOf(CallingPoints()), "previousCallingPoints") {
            decodeAndGetField("previousCallingPoints", "<callingPointList/>", ServiceDetails::previousCallingPoints)
        }
        expect(listOf(CallingPoints()), "previousCallingPoints") {
            decodeAndGetField("previousCallingPoints", "<callingPointList></callingPointList>", ServiceDetails::previousCallingPoints)
        }

        expect(emptyList(), "subsequentCallingPoints") {
            decodeAndGetField("subsequentCallingPoints", "", ServiceDetails::subsequentCallingPoints)
        }
        expect(listOf(CallingPoints()), "subsequentCallingPoints") {
            decodeAndGetField("subsequentCallingPoints", "<callingPointList/>", ServiceDetails::subsequentCallingPoints)
        }
        expect(listOf(CallingPoints()), "subsequentCallingPoints") {
            decodeAndGetField("subsequentCallingPoints", "<callingPointList></callingPointList>", ServiceDetails::subsequentCallingPoints)
        }
    }

    private fun <A> expectField(tag: String, value: A, extractField: ServiceDetails.() -> A) {
        expect(value, tag) {
            decodeAndGetField(tag, value, extractField)
        }
    }

    private fun <A, B> decodeAndGetField(tag: String, value: A, extractField: ServiceDetails.() -> B): B {
        return XmlDeserializer("<GetServiceDetailsResult><$tag>$value</$tag></GetServiceDetailsResult>")
            .serviceDetails()
            .extractField()
    }
}