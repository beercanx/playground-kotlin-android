package uk.co.baconi.pka.common

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.expect

class StationCodesTest {

    private val underTest = StationCodes

    @Test
    fun `Should contain exactly 2570 train station names to CSR codes`() {
        expect(2570, "List<StationCodes> size") {
            underTest.stationCodes.size
        }
    }

    @Test
    fun `Should not contain csv headers 'Station Name' and 'CRS Code'`() {
        assertFailsWith<NoSuchElementException> {
            underTest.firstByName("Station Name")
        }
        assertFailsWith<NoSuchElementException> {
            underTest.firstByCode("CRS Code")
        }
    }

    private val testData = listOf(
        "Meadowhall" to "MHS",
        "Sheffield" to "SHF",
        "Leeds" to "LDS",
        "Scarborough" to "SCA",
        "Whitby" to "WTB",
        "Heathrow Airport Terminal 4" to "HAF",
        "Heathrow Airport Terminal 5" to "HWV",
        "Heathrow Airport Terminals 1 2 and 3" to "HXX"
    )

    @Test
    fun `Should contain only one by station name with corresponding CSR code`() {

        expect(testData) {
            testData.flatMap { (name, _)  ->
                underTest.stationCodes.filter(StationCodes.byName(name))
            }.map { stationCode ->
                stationCode.stationName to stationCode.crsCode
            }
        }
    }

    @Test
    fun `Should contain only one by CRS code with corresponding station name`() {

        expect(testData) {
            testData.flatMap { (_, code)  ->
                underTest.stationCodes.filter(StationCodes.byCode(code))
            }.map { stationCode ->
                stationCode.stationName to stationCode.crsCode
            }
        }
    }
}