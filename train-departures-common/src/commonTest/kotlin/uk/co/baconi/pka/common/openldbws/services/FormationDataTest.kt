package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.BaseDtoTest
import uk.co.baconi.pka.common.openldbws.services.FormationData.Companion.formationData
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test

class FormationDataTest : BaseDtoTest<FormationData> {

    override val tag: String = "formation"
    override fun XmlDeserializer.extractor(): FormationData = formationData()

    @Test
    fun `Should decode with no fields present`() {
        XmlDeserializer("<$tag/>").extractor() shouldBe FormationData()
        XmlDeserializer("<$tag></$tag>").extractor() shouldBe FormationData()
    }

    @Test
    fun `Should decode with each field present`() {
        decodeAndGetField("avgLoading", 90, FormationData::averageLoading) shouldBe 90
        decodeAndGetField("coaches", "", FormationData::coaches) shouldBe emptyList()
    }
}