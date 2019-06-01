package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser
import kotlin.reflect.full.memberProperties

class ServiceLocationSpec : StringSpec({

    "Should decode with no fields present" {

        ServiceLocation.fromXml(
            XmlParser.fromReader(
                "<location/>".reader()
            )
        ) shouldNotBe null

        ServiceLocation.fromXml(
            XmlParser.fromReader(
                "<location></location>".reader()
            )
        ) shouldNotBe null
    }

    fun underTest(tag: String, value: Any) = ServiceLocation.fromXml(
        XmlParser.fromReader(
            "<location><$tag>$value</$tag></location>".reader()
        )
    )

    mapOf(
        "locationName" to "test-location-name",
        "crs" to "test-crs",
        "via" to "test-via",
        "futureChangeTo" to "test-futureChangeTo",
        "assocIsCancelled" to false
    ).forEach { (tag, value) ->
        "Should decode with $tag field present" {
            val field = ServiceLocation::class.memberProperties.find { property -> property.name == tag }
            field?.get(underTest(tag, value)) shouldBe value
        }
    }
})