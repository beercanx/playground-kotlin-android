package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.openldbws.departures.Destination.Companion.destination
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class DestinationTest {

    @Test
    fun `Should decode with no csr or service present`() {

        expect(Destination(), "Destination") {
            XmlDeserializer("<destination/>")
                .destination()
        }

        expect(Destination(), "Destination") {
            XmlDeserializer("<destination></destination>")
                .destination()
        }
    }

    @Test
    fun `Should decode with no service present`() {

        expect(Destination(crs = "test-crs-closed"), "Destination") {
            XmlDeserializer("""<destination crs="test-crs-closed"/>""")
                .destination()
        }

        expect(Destination(crs = "test-crs-open"), "Destination") {
            XmlDeserializer("""<destination crs="test-crs-open"></destination>""")
                .destination()
        }
    }

    @Test
    fun `Should decode with service present`() {

        expect(Destination(service = Service()), "Destination") {
            XmlDeserializer("<destination><service/></destination>")
                .destination()
        }

        expect(Destination(service = Service()), "Destination") {
            XmlDeserializer("<destination><service></service></destination>")
                .destination()
        }
    }
}
