package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.haveSize
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser
import kotlin.reflect.full.memberProperties

class StationBoardResultSpec : StringSpec({

    "Should decode with no fields present" {

        StationBoardResult.fromXml(
            XmlParser.fromReader(
                "<GetStationBoardResult/>".reader()
            )
        ).let {
            it.generatedAt shouldBe null
            it.nrccMessages shouldBe null
            it.trainServices shouldBe null
        }

        StationBoardResult.fromXml(
            XmlParser.fromReader(
                "<GetStationBoardResult></GetStationBoardResult>".reader()
            )
        ).let {
            it.generatedAt shouldBe null
            it.nrccMessages shouldBe null
            it.trainServices shouldBe null
        }
    }

    fun underTest(tag: String, value: Any) = StationBoardResult.fromXml(
        XmlParser.fromReader(
            "<GetStationBoardResult><$tag>$value</$tag></GetStationBoardResult>".reader()
        )
    )

    mapOf(
        "generatedAt" to "test-generated-at-value",
        "locationName" to "test-location-name",
        "crs" to "test-crs",
        "filterLocationName" to "test-location-name",
        "filtercrs" to "test-filter-crs",
        "filterType" to "test-filter-type",
        "platformAvailable" to false,
        "areServicesAvailable" to true
    ).forEach { (tag, value) ->
        "Should decode with $tag field present" {
            val field = StationBoardResult::class.memberProperties.find { property -> property.name == tag }
            field?.get(underTest(tag, value)) shouldBe value
        }
    }

    "Should decode with nrccMessages field present" {
        val field = StationBoardResult::class.memberProperties.find { property -> property.name == "nrccMessages" }
        field?.get(underTest("nrccMessages", "")) shouldBe beEmpty<String>()
        field?.get(underTest("nrccMessages", "<message/>")) shouldBe beEmpty<String>()
        field?.get(underTest("nrccMessages", "<message></message>")) shouldBe beEmpty<String>()
    }

    mapOf(
        "trainServices" to "service",
        "busServices" to "service",
        "ferryServices" to "service"
    ).forEach { (tag, innerTag) ->
        "Should decode with $tag field present" {
            val field = StationBoardResult::class.memberProperties.find { property -> property.name == tag }
            field?.get(underTest(tag, "")) shouldBe beEmpty<String>()
            field?.get(underTest(tag, "<$innerTag/>")) shouldBe haveSize<String>(1)
            field?.get(underTest(tag, "<$innerTag></$innerTag>")) shouldBe haveSize<String>(1)
        }
    }
})