package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beEmpty
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser
import kotlin.reflect.full.memberProperties

class DeparturesBoardSpec : StringSpec({

    "Should decode with no fields present" {

        DeparturesBoard.fromXml(
            XmlParser.fromReader(
                "<DeparturesBoard/>".reader()
            )
        ).let {
            it.generatedAt shouldBe null
            it.nrccMessages shouldBe null
            it.departures shouldBe null
        }

        DeparturesBoard.fromXml(
            XmlParser.fromReader(
                "<DeparturesBoard></DeparturesBoard>".reader()
            )
        ).let {
            it.generatedAt shouldBe null
            it.nrccMessages shouldBe null
            it.departures shouldBe null
        }
    }

    fun underTest(tag: String, value: Any) = DeparturesBoard.fromXml(
        XmlParser.fromReader(
            "<DeparturesBoard><$tag>$value</$tag></DeparturesBoard>".reader()
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
    ).forEach { tag, value ->
        "Should decode with $tag field present" {
            val field = DeparturesBoard::class.memberProperties.find { property -> property.name == tag }
            field?.get(underTest(tag, value)) shouldBe value
        }
    }

    mapOf(
        "nrccMessages" to "message",
        "departures" to "departures"
    ).forEach { tag, innerTag ->
        "Should decode with $tag field present" {
            val field = DeparturesBoard::class.memberProperties.find { property -> property.name == tag }
            field?.get(underTest(tag, "")) shouldBe beEmpty<String>()
            field?.get(underTest(tag, "<$innerTag/>")) shouldBe beEmpty<String>()
            field?.get(underTest(tag, "<$innerTag></$innerTag>")) shouldBe beEmpty<String>()
        }
    }
})