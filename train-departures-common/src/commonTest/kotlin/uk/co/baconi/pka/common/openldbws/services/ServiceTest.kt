package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.BaseDtoTest
import uk.co.baconi.pka.common.openldbws.services.Service.Companion.service
import uk.co.baconi.pka.common.openldbws.services.Service.Companion.services
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class ServiceTest : BaseDtoTest<Service> {

    override val tag: String = "service"
    override fun XmlDeserializer.extractor(): Service = service()

    @Test
    fun `Should decode services with no services present`() {

        val verifyResults = { services: List<Service> ->
            expect(emptyList(), "Services") {
                services
            }
        }

        XmlDeserializer("<origin/>")
            .services("origin")
            .also(verifyResults)

        XmlDeserializer("<origin></origin>")
            .services("origin")
            .also(verifyResults)
    }

    @Test
    fun `Should decode services with services present`() {

        expect(listOf(Service()), "Services") {
            XmlDeserializer("<origin><service/></origin>")
                .services("origin")
        }

        expect(listOf(Service()), "Services") {
            XmlDeserializer("<origin><service></service></origin>")
                .services("origin")
        }

        expect(listOf(Service(), Service()), "Services") {
            XmlDeserializer("<origin><service/><service/></origin>")
                .services("origin")
        }
    }

    @Test
    fun `Should decode a service with no fields present`() {

        val verifyResults = { service: Service ->
            expect(Service(), "Service") {
                service
            }
        }

        XmlDeserializer("<service/>")
            .service()
            .also(verifyResults)

        XmlDeserializer("<service></service>")
            .service()
            .also(verifyResults)
    }

    @Test
    fun `Should decode with each field present`() {
        expectField("sta", "test-sta-value", Service::scheduledArrivalTime)
        expectField("eta", "test-eta-value", Service::estimatedArrivalTime)
        expectField("std", "test-std-value", Service::scheduledDepartureTime)
        expectField("etd", "test-etd-value", Service::estimatedDepartureTime)
        expectField("platform", "test-platform-value", Service::platform)
        expectField("operator", "test-operator-value", Service::operator)
        expectField("operatorCode", "test-operatorCode-value", Service::operatorCode)
        expectField("serviceType", "test-serviceType-value", Service::serviceType)
        expectField("serviceID", "test-serviceID-value", Service::serviceID)
        expectField("rsid", "test-rsid-value", Service::retailServiceId)
        expectField("isCircularRoute", false, Service::isCircularRoute)
        expectField("isCancelled", false, Service::isCancelled)
        expectField("filterLocationCancelled", false, Service::filterLocationCancelled)
        expectField("length", 0, Service::length)
        expectField("detachFront", false, Service::detachFront)
        expectField("isReverseFormation", false, Service::isReverseFormation)
        expectField("cancelReason", "test-cancelReason-value", Service::cancelReason)
        expectField("delayReason", "test-delayReason-value", Service::delayReason)
    }

    @Test
    fun `Should decode with adhocAlerts field present`() {
        expect(emptyList(), "adhocAlerts") {
            decodeAndGetField("adhocAlerts", "", Service::adhocAlerts)
        }
        expect(emptyList(), "adhocAlerts") {
            decodeAndGetField("adhocAlerts", "<adhocAlertText/>", Service::adhocAlerts)
        }
        expect(emptyList(), "adhocAlerts") {
            decodeAndGetField("adhocAlerts", "<adhocAlertText></adhocAlertText>", Service::adhocAlerts)
        }
    }

    @Test
    fun `Should decode with formation field present`() {
        expect(FormationData(), "formation") {
            decodeAndGetField("formation", "", Service::formation)
        }
        expect(FormationData(), "formation") {
            decodeAndGetField("formation", "<formation/>", Service::formation)
        }
        expect(FormationData(), "formation") {
            decodeAndGetField("formation", "<formation></formation>", Service::formation)
        }
    }

    @Test
    fun `Should decode with calling point fields present`() {
        expect(emptyList(), "previousCallingPoints") {
            decodeAndGetField("previousCallingPoints", "", Service::previousCallingPoints)
        }
        expect(listOf(CallingPoints()), "previousCallingPoints") {
            decodeAndGetField("previousCallingPoints", "<callingPointList/>", Service::previousCallingPoints)
        }
        expect(listOf(CallingPoints()), "previousCallingPoints") {
            decodeAndGetField("previousCallingPoints", "<callingPointList></callingPointList>", Service::previousCallingPoints)
        }

        expect(emptyList(), "subsequentCallingPoints") {
            decodeAndGetField("subsequentCallingPoints", "", Service::subsequentCallingPoints)
        }
        expect(listOf(CallingPoints()), "subsequentCallingPoints") {
            decodeAndGetField("subsequentCallingPoints", "<callingPointList/>", Service::subsequentCallingPoints)
        }
        expect(listOf(CallingPoints()), "subsequentCallingPoints") {
            decodeAndGetField("subsequentCallingPoints", "<callingPointList></callingPointList>", Service::subsequentCallingPoints)
        }
    }
}