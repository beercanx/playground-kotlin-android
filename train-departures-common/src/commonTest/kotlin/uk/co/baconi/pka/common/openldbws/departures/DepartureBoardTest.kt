package uk.co.baconi.pka.common.openldbws.departures

import uk.co.baconi.pka.common.BaseDtoTest
import uk.co.baconi.pka.common.openldbws.departures.DepartureBoard.Companion.departureBoard
import uk.co.baconi.pka.common.openldbws.services.Service
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class DepartureBoardTest : BaseDtoTest<DepartureBoard> {

    override val tag: String = "GetStationBoardResult"
    override fun XmlDeserializer.extractor(): DepartureBoard = departureBoard()

    @Test
    fun `Should decode with no fields present`() {
        expect(DepartureBoard(), "DepartureBoard") {
            XmlDeserializer("<$tag/>")
                .departureBoard()
        }
        expect(DepartureBoard(), "DepartureBoard") {
            XmlDeserializer("<$tag></$tag>")
                .departureBoard()
        }
    }

    @Test
    fun `Should decode with each field present`() {
        expectField("generatedAt", "test-generated-at-value", DepartureBoard::generatedAt)
        expectField("locationName", "test-location-name", DepartureBoard::locationName)
        expectField("crs", "test-crs", DepartureBoard::crs)
        expectField("filterLocationName", "test-location-name", DepartureBoard::filterLocationName)
        expectField("filtercrs", "test-filter-crs", DepartureBoard::filterCrs)
        expectField("filterType", "test-filter-type", DepartureBoard::filterType)
        expectField("platformAvailable", false, DepartureBoard::platformAvailable)
        expectField("areServicesAvailable", true, DepartureBoard::areServicesAvailable)
    }

    @Test
    fun `Should decode with nrccMessages field present`() {
        expect(emptyList(), "nrccMessages") {
            decodeAndGetField("nrccMessages", "", DepartureBoard::nrccMessages)
        }
        expect(emptyList(), "nrccMessages") {
            decodeAndGetField("nrccMessages", "<message/>", DepartureBoard::nrccMessages)
        }
        expect(emptyList(), "nrccMessages") {
            decodeAndGetField("nrccMessages", "<message></message>", DepartureBoard::nrccMessages)
        }
    }

    @Test
    fun `Should decode with service fields present`() {
        mapOf(
            "busServices" to DepartureBoard::busServices,
            "trainServices" to DepartureBoard::trainServices,
            "ferryServices" to DepartureBoard::ferryServices
        ).forEach { (tag, field) ->
            expect(emptyList(), tag) {
                decodeAndGetField(tag, "", field)
            }
            expect(listOf(Service()), tag) {
                decodeAndGetField(tag, "<service/>", field)
            }
            expect(listOf(Service()), tag) {
                decodeAndGetField(tag, "<service></service>", field)
            }
        }
    }
}