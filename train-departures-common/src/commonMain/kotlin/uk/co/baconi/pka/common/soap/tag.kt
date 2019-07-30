package uk.co.baconi.pka.common.soap

import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.XmlDeserializerException
import uk.co.baconi.pka.common.xml.parse
import uk.co.baconi.pka.common.xml.skip

fun <T> XmlDeserializer.tag(outerTag: String, innerTag: String, innerBody: XmlDeserializer.() -> T): T {
    return parse(tag = outerTag, initial = null) { result: T? ->
        when (getName()) {
            innerTag -> innerBody()
            else -> skip(result)
        }
    } ?: throw XmlDeserializerException("$outerTag contained no supported inner tag or was empty.")
}