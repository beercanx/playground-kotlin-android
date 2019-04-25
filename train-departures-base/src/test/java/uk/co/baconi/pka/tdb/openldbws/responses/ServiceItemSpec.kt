package uk.co.baconi.pka.tdb.openldbws.responses

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.xml.XmlParser
import kotlin.reflect.full.memberProperties

class ServiceItemSpec : StringSpec({

    "Should decode with no fields present" {

        ServiceItem.fromXml(
            XmlParser.fromReader(
                "<service/>".reader()
            )
        ) shouldNotBe null

        ServiceItem.fromXml(
            XmlParser.fromReader(
                "<service></service>".reader()
            )
        ) shouldNotBe null
    }

    fun underTest(tag: String, value: Any) = ServiceItem.fromXml(
        XmlParser.fromReader(
            "<service><$tag>$value</$tag></service>".reader()
        )
    )

    mapOf(
        "sta" to "test-sta-value",
        "eta" to "test-eta-value",
        "std" to "test-std-value",
        "etd" to "test-etd-value",
        "platform" to "test-platform-value",
        "operator" to "test-operator-value",
        "operatorCode" to "test-operatorCode-value",
        "serviceType" to "test-serviceType-value",
        "serviceID" to "test-serviceID-value",
        "rsid" to "test-rsid-value",
        "isCircularRoute" to false,
        "isCancelled" to false,
        "filterLocationCancelled" to false,
        "length" to 0,
        "detachFront" to false,
        "isReverseFormation" to false,
        "cancelReason" to "test-cancelReason-value",
        "delayReason" to "test-delayReason-value"
    ).forEach { tag, value ->
        "Should decode with $tag field present" {
            val field = ServiceItem::class.memberProperties.find { property -> property.name == tag }
            field?.get(underTest(tag, value)) shouldBe value
        }
    }

    mapOf(
        "origin" to "location",
        "destination" to "location",
        "currentOrigins" to "location",
        "currentDestinations" to "location"
    ).forEach { tag, innerTag ->
        "Should decode with $tag field present" {
            val field = ServiceItem::class.memberProperties.find { property -> property.name == tag }
            field?.get(underTest(tag, "")) shouldBe beEmpty<ServiceLocation>()
            (field?.get(underTest(tag, "<$innerTag/>")) as List<*>).first() should beInstanceOf<ServiceLocation>()
            (field.get(underTest(tag, "<$innerTag></$innerTag>")) as List<*>).first() should beInstanceOf<ServiceLocation>()
        }
    }

    "Should decode with adhocAlerts field present" {
        underTest("adhocAlerts", "").adhocAlerts shouldBe beEmpty<String>()
        underTest("adhocAlerts", "<adhocAlertText/>").adhocAlerts shouldBe beEmpty<String>()
        underTest("adhocAlerts", "<adhocAlertText></adhocAlertText>").adhocAlerts shouldBe beEmpty<String>()
    }

    "Should decode with formation field present" {
        underTest("formation", "").formation should beInstanceOf<FormationData>()
        underTest("formation", "<formation/>").formation should beInstanceOf<FormationData>()
        underTest("formation", "<formation></formation>").formation should beInstanceOf<FormationData>()
    }
})