package uk.co.baconi.pka.common.openldbws.services

import uk.co.baconi.pka.common.BaseDtoTest
import uk.co.baconi.pka.common.openldbws.services.CallingPoints.Companion.callingPoints
import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.Test
import kotlin.test.expect

class CallingPointsTest : BaseDtoTest<CallingPoints> {

    override val tag: String = "callingPointList"
    override fun XmlDeserializer.extractor(): CallingPoints = callingPoints()

    @Test
    fun `Should decode with no fields present`() {
        expect(CallingPoints()) {
            XmlDeserializer("<callingPointList/>")
                .callingPoints()
        }
        expect(CallingPoints()) {
            XmlDeserializer("<callingPointList></callingPointList>")
                .callingPoints()
        }
    }

    @Test
    fun `Should decode with each field present`() {
        expect(CallingPoints(serviceType = "bus")) {
            XmlDeserializer("""<callingPointList serviceType="bus" />""")
                .callingPoints()
        }
        expect(CallingPoints(serviceChangeRequired = true)) {
            XmlDeserializer("""<callingPointList serviceChangeRequired="true" />""")
                .callingPoints()
        }
        expect(CallingPoints(associationIsCancelled = true)) {
            XmlDeserializer("""<callingPointList assocIsCancelled="true" />""")
                .callingPoints()
        }
    }

    @Test
    fun `Should decode with calling point field present`() {
        expect(CallingPoints(listOf(CallingPoint()))) {
            XmlDeserializer("<callingPointList><callingPoint/></callingPointList>")
                .callingPoints()
        }
        expect(CallingPoints(listOf(CallingPoint(), CallingPoint()))) {
            XmlDeserializer("<callingPointList><callingPoint/><callingPoint/></callingPointList>")
                .callingPoints()
        }
    }

    @Test
    fun `Should decode with no calling points present`() {
        expect(emptyList()) {
            XmlDeserializer("<subsequentCallingPoints/>")
                .callingPoints("subsequentCallingPoints")
        }
        expect(emptyList()) {
            XmlDeserializer("<subsequentCallingPoints></subsequentCallingPoints>")
                .callingPoints("subsequentCallingPoints")
        }
    }

    @Test
    fun `Should decode with calling points present`() {
        expect(listOf(CallingPoints())) {
            XmlDeserializer("<subsequentCallingPoints><callingPointList/></subsequentCallingPoints>")
                .callingPoints("subsequentCallingPoints")
        }
        expect(listOf(CallingPoints())) {
            XmlDeserializer("<subsequentCallingPoints><callingPointList></callingPointList></subsequentCallingPoints>")
                .callingPoints("subsequentCallingPoints")
        }
        expect(listOf(CallingPoints(),CallingPoints())) {
            XmlDeserializer("<subsequentCallingPoints><callingPointList/><callingPointList/></subsequentCallingPoints>")
                .callingPoints("subsequentCallingPoints")
        }
    }

}