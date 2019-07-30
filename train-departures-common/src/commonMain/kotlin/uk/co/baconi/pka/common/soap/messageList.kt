package uk.co.baconi.pka.common.soap

import uk.co.baconi.pka.common.xml.XmlDeserializer
import uk.co.baconi.pka.common.xml.parse
import uk.co.baconi.pka.common.xml.readAsText
import uk.co.baconi.pka.common.xml.skip

fun XmlDeserializer.nrccMessages() = messageList("nrccMessages", "message")

fun XmlDeserializer.adhocAlerts() = messageList("adhocAlerts", "adhocAlertText")

fun XmlDeserializer.messageList(outerTag: String, innerTag: String): List<String> {
    return parse(tag = outerTag, initial = emptyList()) { result ->
        when (getName()) {
            innerTag -> when (val message = readAsText()) {
                is String -> result.plus(message)
                else -> result
            }
            else -> skip(result)
        }
    }
}