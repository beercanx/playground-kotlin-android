package uk.co.baconi.pka.common

import uk.co.baconi.pka.common.xml.XmlDeserializer

interface BaseDtoTest<DTO> : BaseTest {

    val tag: String

    fun XmlDeserializer.extractor(): DTO

    fun <A> expectField(tag: String, value: A, extractField: DTO.() -> A) {
        decodeAndGetField(tag, value, extractField) shouldBe value
    }

    fun <A, B> decodeAndGetField(name: String, value: A, extractField: DTO.() -> B): B {
        return XmlDeserializer("<$tag><$name>$value</$name></$tag>")
            .extractor()
            .extractField()
    }

}