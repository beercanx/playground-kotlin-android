package uk.co.baconi.pka.common.soap

import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.parse
import uk.co.baconi.pka.common.xml.skip

fun <T> XmlDeserializer.tag(outerTag: String, innerTag: String, innerBody: XmlDeserializer.() -> T): T {
    return parse(tag = outerTag, initial = null) { result: T? ->
        when (getName()) {
            innerTag -> innerBody()
            else -> skip(result)
        }
    } ?: throw Exception("$outerTag was empty or didn't expect it.") // TODO - Provide better exception class
}