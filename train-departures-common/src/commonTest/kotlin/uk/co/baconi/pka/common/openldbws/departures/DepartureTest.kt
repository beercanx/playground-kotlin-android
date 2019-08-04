package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.openldbws.departures.Departure.Companion.departure
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class DepartureTest {

    @Test
    fun `Should decode with no csr or service present`() {

        expect(Departure(), "Departure") {
            XmlDeserializer("<destination/>")
                .departure()
        }

        expect(Departure(), "Departure") {
            XmlDeserializer("<destination></destination>")
                .departure()
        }
    }

    @Test
    fun `Should decode with no service present`() {

        expect(Departure(crs = "test-crs-closed"), "Departure") {
            XmlDeserializer("""<destination crs="test-crs-closed"/>""")
                .departure()
        }

        expect(Departure(crs = "test-crs-open"), "Departure") {
            XmlDeserializer("""<destination crs="test-crs-open"></destination>""")
                .departure()
        }
    }

    @Test
    fun `Should decode with service present`() {

        expect(Departure(service = Service()), "Departure") {
            XmlDeserializer("<destination><service/></destination>")
                .departure()
        }

        expect(Departure(service = Service()), "Departure") {
            XmlDeserializer("<destination><service></service></destination>")
                .departure()
        }
    }
}
