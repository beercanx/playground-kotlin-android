package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.BaseTest
import uk.co.baconi.pka.common.openldbws.services.Toilet.Companion.toilet
import uk.co.baconi.pka.common.openldbws.services.ToiletStatus.*
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test

class ToiletTest : BaseTest {

    @Test
    fun `Should decode with no fields present`() {
        XmlDeserializer("<toilet/>").toilet() shouldBe Toilet()
        XmlDeserializer("<toilet></toilet>").toilet() shouldBe Toilet()
    }

    @Test
    fun `Should decode with each field present`() {
        XmlDeserializer("""<toilet status="Unknown"></toilet>""").toilet() shouldBe Toilet(status = Unknown)
        XmlDeserializer("""<toilet status="InService"></toilet>""").toilet() shouldBe Toilet(status = InService)
        XmlDeserializer("""<toilet status="NotInService"></toilet>""").toilet() shouldBe Toilet(status = NotInService)
        XmlDeserializer("""<toilet>Accessible</toilet>""").toilet() shouldBe Toilet(type = "Accessible")
    }
}