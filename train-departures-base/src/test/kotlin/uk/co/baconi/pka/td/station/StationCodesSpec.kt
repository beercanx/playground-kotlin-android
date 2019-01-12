package uk.co.baconi.pka.td.station

import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.td.station.StationCodes.byCode
import uk.co.baconi.pka.td.station.StationCodes.byName

/**
 * Created by James Bacon on 12/01/2019.
 */
class StationCodesSpec : StringSpec({

    val underTest = StationCodes

    "Should contain exactly 2570 train station names to CSR codes" {
        underTest.stationCodes().toList() shouldHaveSize 2570
    }

    mapOf(
        "Meadowhall" to "MHS",
        "Sheffield" to "SHF",
        "Leeds" to "LDS",
        "Scarborough" to "SCA",
        "Whitby" to "WTB"
    ).forEach { name, code ->
        "Should contain only one by station name [$name] with CSR code [$code]" {
            val searchResults = underTest.stationCodes().filter(byName(name))
            searchResults shouldHaveSize 1
            searchResults.first().crsCode shouldBe code
        }

        "Should contain only one by CRS code [$code] with station name [$name]" {
            val searchResults = underTest.stationCodes().filter(byCode(code))
            searchResults shouldHaveSize 1
            searchResults.first().stationName shouldBe name
        }
    }

    "Should not contain csv headers 'Station Name' and 'CRS Code'" {
        underTest.stationCodes().filter(byName("Station Name")) shouldHaveSize 0
        underTest.stationCodes().filter(byCode("CRS Code")) shouldHaveSize 0
    }
})