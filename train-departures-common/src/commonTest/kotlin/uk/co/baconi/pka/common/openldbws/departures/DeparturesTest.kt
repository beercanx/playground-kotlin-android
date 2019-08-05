package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.BaseDtoTest
import uk.co.baconi.pka.common.openldbws.departures.Departures.Companion.departures
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class DeparturesTest : BaseDtoTest<Departures> {

    override val tag: String = "DeparturesBoard"

    override fun XmlDeserializer.extractor(): Departures = departures()

    @Test
    fun `Should decode with no fields present`() {
        expect(Departures(), "Departures") {
            XmlDeserializer("<DeparturesBoard/>")
                .departures()
        }
        expect(Departures(), "Departures") {
            XmlDeserializer("<DeparturesBoard></DeparturesBoard>")
                .departures()
        }
    }

    @Test
    fun `Should decode with each field present`() {
        expectField("generatedAt", "test-generated-at-value", Departures::generatedAt)
        expectField("locationName", "test-location-name", Departures::locationName)
        expectField("crs", "test-crs", Departures::crs)
        expectField("filterLocationName", "test-location-name", Departures::filterLocationName)
        expectField("filtercrs", "test-filter-crs", Departures::filterCrs)
        expectField("filterType", "test-filter-type", Departures::filterType)
        expectField("platformAvailable", false, Departures::platformAvailable)
        expectField("areServicesAvailable", true, Departures::areServicesAvailable)
    }

    @Test
    fun `Should decode with nrccMessages field present`() {
        expect(emptyList(), "nrccMessages") {
            decodeAndGetField("nrccMessages", "", Departures::nrccMessages)
        }
        expect(emptyList(), "nrccMessages") {
            decodeAndGetField("nrccMessages", "<message/>", Departures::nrccMessages)
        }
        expect(emptyList(), "nrccMessages") {
            decodeAndGetField("nrccMessages", "<message></message>", Departures::nrccMessages)
        }
    }

    @Test
    fun `Should decode with departures field present`() {
        expect(emptyList(), "departures") {
            decodeAndGetField("departures", "", Departures::destinations)
        }
        expect(listOf(Destination()), "departures") {
            decodeAndGetField("departures", "<destination/>", Departures::destinations)
        }
        expect(listOf(Destination()), "departures") {
            decodeAndGetField("departures", "<destination></destination>", Departures::destinations)
        }
    }
}