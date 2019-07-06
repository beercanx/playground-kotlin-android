package uk.co.baconi.pka.tdb.openldbws.responses.servicedetails

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import uk.co.baconi.pka.tdb.openldbws.responses.CallingPoints
import uk.co.baconi.pka.tdb.openldbws.responses.FormationData
import uk.co.baconi.pka.tdb.xml.XmlParser
import kotlin.reflect.full.memberProperties

class ServiceDetailsResultSpec : StringSpec({

    "Should decode with no fields present" {

        ServiceDetailsResult.fromXml(
            XmlParser.fromReader(
                "<GetServiceDetailsResult/>".reader()
            )
        ).let {
            it.generatedAt shouldBe null
            it.adhocAlerts shouldBe null
        }

        ServiceDetailsResult.fromXml(
            XmlParser.fromReader(
                "<GetServiceDetailsResult></GetServiceDetailsResult>".reader()
            )
        ).let {
            it.generatedAt shouldBe null
            it.adhocAlerts shouldBe null
        }
    }

    fun underTest(tag: String, value: Any) =
        ServiceDetailsResult.fromXml(
            XmlParser.fromReader(
                "<GetServiceDetailsResult><$tag>$value</$tag></GetServiceDetailsResult>".reader()
            )
        )

    mapOf(
        "generatedAt" to "test-generatedAt-value",
        "serviceType" to "test-serviceType-value",
        "locationName" to "test-locationName-value",
        "crs" to "test-crs-value",
        "operator" to "test-operator-value",
        "operatorCode" to "test-operatorCode-value",
        "rsid" to "test-rsid-value",
        "isCancelled" to false,
        "cancelReason" to "test-cancelReason-value",
        "delayReason" to "test-delayReason-value",
        "overdueMessage" to "test-overdueMessage-value",
        "length" to 0,
        "detachFront" to false,
        "isReverseFormation" to false,
        "platform" to "test-platform-value",
        "sta" to "test-sta-value",
        "eta" to "test-eta-value",
        "ata" to "test-ata-value",
        "std" to "test-std-value",
        "etd" to "test-etd-value",
        "atd" to "test-atd-value"
    ).forEach { (tag, value) ->
        "Should decode with $tag field present" {
            val field = ServiceDetailsResult::class.memberProperties.find { property -> property.name == tag }
            field?.get(underTest(tag, value)) shouldBe value
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

    mapOf(
        "subsequentCallingPoints" to "callingPointList",
        "previousCallingPoints" to "callingPointList"
    ).forEach { (tag, innerTag) ->
        "Should decode with $tag field present" {
            val field = ServiceDetailsResult::class.memberProperties.find { property -> property.name == tag }
            field?.get(underTest(tag, "")) shouldBe beEmpty<CallingPoints>()
            (field?.get(underTest(tag, "<$innerTag/>")) as List<*>).first() should beInstanceOf<CallingPoints>()
            (field.get(underTest(tag, "<$innerTag></$innerTag>")) as List<*>).first() should beInstanceOf<CallingPoints>()
        }
    }
})