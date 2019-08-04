package uk.co.baconi.pka.common

import uk.co.baconi.pka.common.xml.XmlDeserializer
import kotlin.test.expect

interface BaseDtoTest<DTO> {

    val tag: String

    fun XmlDeserializer.extractor(): DTO

    fun <A> expectField(tag: String, value: A, extractField: DTO.() -> A) {
        expect(value, tag) {
            decodeAndGetField(tag, value, extractField)
        }
    }

    fun <A, B> decodeAndGetField(tag: String, value: A, extractField: DTO.() -> B): B {
        return XmlDeserializer("<${this.tag}><$tag>$value</$tag></${this.tag}>")
            .extractor()
            .extractField()
    }

}