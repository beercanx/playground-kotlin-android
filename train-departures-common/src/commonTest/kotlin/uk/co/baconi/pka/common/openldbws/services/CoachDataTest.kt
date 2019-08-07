package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.BaseDtoTest
import uk.co.baconi.pka.common.openldbws.services.CoachData.Companion.coach
import uk.co.baconi.pka.common.openldbws.services.CoachData.Companion.coaches
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test

class CoachDataTest : BaseDtoTest<CoachData> {

    override val tag: String = "coach"
    override fun XmlDeserializer.extractor(): CoachData = coach()

    @Test
    fun `Should decode with no fields present`() {
        XmlDeserializer("<$tag/>").extractor() shouldBe CoachData()
        XmlDeserializer("<$tag></$tag>").extractor() shouldBe CoachData()
    }

    @Test
    fun `Should decode with each field present`() {
        decodeAndGetField("coachClass", "First", CoachData::coachClass) shouldBe "First"
        decodeAndGetField("loading", 90, CoachData::loading) shouldBe 90
        decodeAndGetField("number", "A", CoachData::number) shouldBe "A"
        decodeAndGetField("toilet", "", CoachData::toilet) shouldBe Toilet()
    }

    @Test
    fun `Should decode with no coaches present`() {
        XmlDeserializer("<coaches/>").coaches() shouldBe emptyList()
        XmlDeserializer("<coaches></coaches>").coaches() shouldBe emptyList()
    }

    @Test
    fun `Should decode with coaches present`() {
        XmlDeserializer("<coaches><coach/></coaches>").coaches() shouldBe listOf(CoachData())
        XmlDeserializer("<coaches><coach></coach></coaches>").coaches() shouldBe listOf(CoachData())
        XmlDeserializer("<coaches><coach/><coach/></coaches>").coaches() shouldBe listOf(CoachData(), CoachData())
    }
}