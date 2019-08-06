package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.BaseDtoTest
import uk.co.baconi.pka.common.openldbws.services.CallingPoint.Companion.callingPoint
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test

class CallingPointTest : BaseDtoTest<CallingPoint> {

    override val tag: String = "callingPoint"
    override fun XmlDeserializer.extractor(): CallingPoint = callingPoint()

    @Test
    fun `Should decode with no fields present`() {
        XmlDeserializer("<callingPoint/>").callingPoint() shouldBe CallingPoint()
        XmlDeserializer("<callingPoint></callingPoint>").callingPoint() shouldBe CallingPoint()
    }

    @Test
    fun `Should decode with each field present`() {
        decodeAndGetField("locationName", "Hull", CallingPoint::locationName) shouldBe "Hull"
        decodeAndGetField("crs", "HUL", CallingPoint::crs) shouldBe "HUL"
        decodeAndGetField("st", "11:20", CallingPoint::scheduledTime) shouldBe "11:20"
        decodeAndGetField("et", "11:25", CallingPoint::estimatedTime) shouldBe "11:25"
        decodeAndGetField("at", "11:30", CallingPoint::actualTime) shouldBe "11:30"
        decodeAndGetField("isCancelled", true, CallingPoint::isCancelled) shouldBe true
        decodeAndGetField("length", 3, CallingPoint::length) shouldBe 3
        decodeAndGetField("detachFront", true, CallingPoint::detachFront) shouldBe true
        decodeAndGetField("formation", "", CallingPoint::formation) shouldBe FormationData()
        decodeAndGetField("adhocAlerts", "", CallingPoint::adhocAlerts) shouldBe emptyList()
        decodeAndGetField("adhocAlerts", "<adhocAlertText/>", CallingPoint::adhocAlerts) shouldBe emptyList()
        decodeAndGetField(
            "adhocAlerts", "<adhocAlertText>alert</adhocAlertText>", { adhocAlerts }
        ) shouldBe listOf("alert")
    }
}