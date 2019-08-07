package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.BaseDtoTest
import uk.co.baconi.pka.common.openldbws.services.ServiceLocation.Companion.serviceLocation
import uk.co.baconi.pka.common.openldbws.services.ServiceLocation.Companion.serviceLocations
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class ServiceLocationTest : BaseDtoTest<ServiceLocation> {

    override val tag: String = "location"
    override fun XmlDeserializer.extractor(): ServiceLocation = serviceLocation()

    @Test
    fun `Should decode with no fields present`() {

        expect(ServiceLocation(), "ServiceLocation") {
            XmlDeserializer("<location/>")
                .serviceLocation()
        }

        expect(ServiceLocation(), "ServiceLocation") {
            XmlDeserializer("<location></location>")
                .serviceLocation()
        }
    }

    @Test
    fun `Should decode with each field present`() {
        expectField("locationName", "test-location-name", ServiceLocation::locationName)
        expectField("crs", "test-crs", ServiceLocation::crs)
        expectField("via", "test-via", ServiceLocation::via)
        expectField("futureChangeTo", "test-futureChangeTo", ServiceLocation::futureChangeTo)
        expectField("assocIsCancelled", false, ServiceLocation::associationIsCancelled)
    }

    @Test
    fun `Should decode with no service locations present`() {

        expect(emptyList(), "ServiceLocations") {
            XmlDeserializer("<origin/>")
                .serviceLocations("origin")
        }

        expect(emptyList(), "ServiceLocations") {
            XmlDeserializer("<origin></origin>")
                .serviceLocations("origin")
        }
    }

    @Test
    fun `Should decode with service locations present`() {

        expect(listOf(ServiceLocation()), "ServiceLocations") {
            XmlDeserializer("<origin><location/></origin>")
                .serviceLocations("origin")
        }

        expect(listOf(ServiceLocation()), "ServiceLocations") {
            XmlDeserializer("<origin><location></location></origin>")
                .serviceLocations("origin")
        }

        expect(listOf(ServiceLocation(),ServiceLocation()), "ServiceLocations") {
            XmlDeserializer("<origin><location/><location/></origin>")
                .serviceLocations("origin")
        }
    }
}